package com.code;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
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
    public ServletOutputStream getOutputStream() throws IOException {
        return super.getOutputStream();
    }

    /**
     * 发送响应
     */
    public void complete() {

    }
}
