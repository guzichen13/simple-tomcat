package com.code;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

public class Context {

    private String appName;
    private Map<String, Servlet> urlPatternsMapping = new HashMap<>();

    public Context(String appName) {
        this.appName = appName;
    }

    public void addUrlPatternsMapping(String urlPattern, Servlet servlet) {
        urlPatternsMapping.put(urlPattern, servlet);
    }

    public Servlet getByUrlPattern(String urlPattern) {
        for (String key : urlPatternsMapping.keySet()) {
            if (key.contains(urlPattern)) {
                return urlPatternsMapping.get(key);
            }
        }
        return null;
    }
}
