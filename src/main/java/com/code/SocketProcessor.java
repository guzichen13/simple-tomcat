package com.code;

import javax.servlet.Servlet;
import java.io.InputStream;
import java.net.Socket;

public class SocketProcessor implements Runnable {

    private final Socket socket;

    private final Tomcat tomcat;

    public SocketProcessor(Socket socket, Tomcat tomcat) {
        this.socket = socket;
        this.tomcat = tomcat;
    }

    @Override
    public void run() {
        processSocket(socket);
    }

    /**
     * 处理socket连接
     * @param socket
     */
    private void processSocket(Socket socket) {
        // 读/写数据
        try {
            InputStream inputStream = socket.getInputStream();
            // 从socket连接上读取1kb的字节数据
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);

            for (byte aByte : bytes) {
                System.out.print((char) aByte);
            }
            System.out.println();
            System.out.println("=========================================================>");

            // 解析字节流 遇到一个空格就退出
            int pos = 0;
            int begin = 0, end = 0;
            for (; pos < bytes.length; pos++, end++) {
                if (bytes[pos] == ' ') break;
            }

            // 组合字节流空格之前的字符流，转换成字符串就是请求方法
            StringBuffer method = new StringBuffer();
            for (; begin < end; begin++) {
                method.append((char) bytes[begin]);
            }

            System.out.println("method = " + method);

            // 解析url
            pos++;
            begin++;
            end++;
            for (; pos < bytes.length; pos++, end++) {
                if (bytes[pos] == ' ') break;
            }
            StringBuffer url = new StringBuffer();
            for (; begin < end; begin++) {
                url.append((char) bytes[begin]);
            }

            System.out.println("url = " + url);

            // 解析协议版本
            pos++;
            begin++;
            end++;
            for (; pos < bytes.length; pos++, end++) {
                // 应为\r\n http协议换行为回车符换行符
                if (bytes[pos   ] == '\r' && bytes[pos + 1] == '\n') break;
            }
            StringBuffer protocol = new StringBuffer();
            for (; begin < end; begin++) {
                protocol.append((char) bytes[begin]);
            }

            System.out.println("protocol = " + protocol);

            Request request = new Request(method, url, protocol, socket);
            Response response = new Response(request);

            // 根据request匹配servlet、执行doGet/doPost方法
            // Servlet servlet = new Servlet();
            // servlet.service(request, response);

            // 根据请求路径匹配servlet
            String requestUrl = request.getRequestURL().toString();
            requestUrl = requestUrl.substring(1);
            String[] parts = requestUrl.split("/");

            // 获取到应用名称
            String appName = parts[0];

            // 根据应用名称找到context
            Context context = tomcat.getContextMap().get(appName);

            if (parts.length > 1) {
                // 根据请求地址找到servlet
                Servlet servlet = context.getByUrlPattern(parts[1]);
                // 如果找不到servlet返回默认servlet
                if (servlet != null) {
                    servlet.service(request, response);
                    // 发送响应
                    response.complete();
                } else {
                    DefaultServlet defaultServlet = new DefaultServlet();
                    defaultServlet.service(request, response);
                    // 发送响应
                    response.complete();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
