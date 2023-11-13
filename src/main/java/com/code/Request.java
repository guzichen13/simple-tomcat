package com.code;

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

    public Request(StringBuffer method, StringBuffer url, StringBuffer protocol) {
        this.method = method.toString();
        this.url = url.toString();
        this.protocol = protocol.toString();
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
}
