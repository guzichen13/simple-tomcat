package com.code;

import java.net.Socket;

public class Request extends AbstractHttpServletRequest {

    /**
     * 请求方法
     */
    private String method;
    /**
     * url
     */
    private String url;
    /**
     * 请求协议版本
     */
    private String protocol;

    /**
     * 请求对应的socket连接
     */
    private Socket socket;

    public Request(StringBuffer method, StringBuffer url, StringBuffer protocol, Socket socket) {
        this.method = method.toString();
        this.url = url.toString();
        this.protocol = protocol.toString();
        this.socket = socket;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(url);
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public Socket getSocket() {
        return socket;
    }
}
