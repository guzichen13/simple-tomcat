package com.code;

public class Request {

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

    public Request(String method, String url, String protocol) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
    }

    public Request(StringBuffer method, StringBuffer url, StringBuffer protocol) {
        this.method = method.toString();
        this.url = url.toString();
        this.protocol = protocol.toString();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }
}
