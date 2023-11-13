package com.code;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义类加载器因为webapps
 */
public class WebappClassLoader extends URLClassLoader {

    public WebappClassLoader(URL[] urls) {
        super(urls);
    }
}
