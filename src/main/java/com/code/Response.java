package com.code;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response extends AbstractHttpServletResponse {

    /**
     * 状态码
     */
    private int status = 200;

    /**
     * 状态信息
     */
    private String message = "OK";

    /**
     * 响应头
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * 请求
     */
    private Request request;

    /**
     * socket连接的outputStream
     */
    private OutputStream socketOutputStream;

    /**
     * 空格
     */
    private byte SP = ' ';

    /**
     * 回车
     */
    private byte CR = '\r';

    /**
     * 换行
     */
    private byte LF = '\n';

    /**
     * 响应体数据
     */
    private ResponseServletOutputStream responseServletOutputStream = new ResponseServletOutputStream();

    /**
     * 一个请求对应一个响应
     * @param request
     */
    public Response(Request request) {
        this.request = request;
        try {
            this.socketOutputStream = request.getSocket().getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setStatus(int i, String s) {
        status = i;
        message = s;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void addHeader(String s, String s1) {
        headers.put(s, s1);
    }

    @Override
    public ResponseServletOutputStream getOutputStream() throws IOException {
        return responseServletOutputStream;
    }

    /**
     * 发送响应
     */
    public void complete() {
        try {
            sendResponseLine();
            sendResponseHeader();
            sendResponseBody();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送请求体
     */
    private void sendResponseBody() throws IOException {
        socketOutputStream.write(getOutputStream().getBytes());
    }

    /**
     * 发送请求头
     */
    private void sendResponseHeader() throws IOException {

        /**
         * 如果响应头里没有长度则添加
         */
        if (!headers.containsKey("Content-Length")) {
            addHeader("Content-Length", String.valueOf(getOutputStream().getPos()));
        }

        /**
         * 如果未指定响应类型则添加纯文本头
         */
        if (!headers.containsKey("Content-Type")) {
            addHeader("Content-Type", "text/plain;charset=utf-8");
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            socketOutputStream.write(key.getBytes());
            socketOutputStream.write(":".getBytes());
            socketOutputStream.write(value.getBytes());
            socketOutputStream.write(CR);
            socketOutputStream.write(LF);
        }
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);
    }

    /**
     * 发送请求行
     */
    private void sendResponseLine() throws IOException {
        socketOutputStream.write(request.getProtocol().getBytes());
        socketOutputStream.write(SP);
        socketOutputStream.write(status);
        socketOutputStream.write(SP);
        socketOutputStream.write(message.getBytes());
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);
    }
}
