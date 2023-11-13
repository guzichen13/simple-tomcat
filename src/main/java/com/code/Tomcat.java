package com.code;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tomcat {

    private Map<String, Context> contextMap = new HashMap<>();

    public void start() {

        // Socket连接
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(20);

            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new SocketProcessor(socket, this));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        // 部署
        tomcat.deployApps();
        tomcat.start();
    }

    /**
     * 部署
     */
    private void deployApps() {
        // user.dir当前工程; webapps子目录
        File webapps = new File(System.getProperty("user.dir"), "webapps");
        for (String app : Objects.requireNonNull(webapps.list())) {
            System.out.println(app);
            deployApp(webapps, app);
        }
    }

    private void deployApp(File webApps, String appName) {

        Context context = new Context(appName);

        // 找到有哪些servlet
        File appDirectory = new File(webApps, appName);
        File classesDirectory = new File(appDirectory, "classes");

        List<File> files = getAllFilePath(classesDirectory);

        for (File clazz : files) {
            System.out.println("clazz = " + clazz);
            System.out.println("classesDirectory.getPath() = " + classesDirectory.getPath());

            // 路径转换为类名全路径
            String name = clazz.getPath();
            name = name.replace(classesDirectory.getPath() + "\\", "");
            name = name.replace(".class", "");
            name = name.replace("\\", ".");

            System.out.println(name);

            // 通过类名全路径使用loadClass拿到class
            try {
                WebappClassLoader classLoader = new WebappClassLoader(new URL[]{classesDirectory.toURL()});
                Class<?> servletClass = classLoader.loadClass(name);
                // 是否继承HttpServlet
                if (HttpServlet.class.isAssignableFrom(servletClass)) {
                    // 如果存在WebServlet注解 获取内容
                    if (servletClass.isAnnotationPresent(WebServlet.class)) {
                        WebServlet annotation = servletClass.getAnnotation(WebServlet.class);
                        String[] urlPatterns = annotation.urlPatterns();

                        // 解析映射规则添加到context中
                        for (String urlPattern : urlPatterns) {
                            context.addUrlPatternsMapping(urlPattern, (Servlet) servletClass.newInstance());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        contextMap.put(appName, context);
    }

    /**
     * 递归遍历文件夹以及子文件夹下有哪些文件
     * @param srcFile
     * @return
     */
    private List<File> getAllFilePath(File srcFile) {
        List<File> result = new ArrayList<>();
        File[] files = srcFile.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    result.addAll(getAllFilePath(file));
                } else {
                    result.add(file);
                }
            }
        }
        return result;
    }

    public Map<String, Context> getContextMap() {
        return contextMap;
    }
}
