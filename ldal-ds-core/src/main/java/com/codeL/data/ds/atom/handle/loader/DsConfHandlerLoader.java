package com.codeL.data.ds.atom.handle.loader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

public class DsConfHandlerLoader {

    private static final Logger logger = LoggerFactory.getLogger(DsConfHandlerLoader.class);

    public static final DsConfHandlerLoader LOADER = new DsConfHandlerLoader();

    public void load(Map<String, Class<?>> extensionClasses, String dir, String type) {
        String fileName = dir + type;
        try {
            Enumeration<URL> urls;
            urls = DsConfHandlerLoader.class.getClassLoader().getResources(fileName);
            loadFromClass(extensionClasses, urls, Thread.currentThread().getContextClassLoader());
        } catch (Throwable t) {
            logger.error("Exception occurred when loading class (interface:{}, description file:{}", type, fileName, t);
        }
    }

    private void loadFromClass(Map<String, Class<?>> extensionClasses, Enumeration<java.net.URL> urls, ClassLoader classLoader) {
        if (urls != null) {
            while (urls.hasMoreElements()) {
                java.net.URL resourceURL = urls.nextElement();
                loadResource(extensionClasses, classLoader, resourceURL);
            }
        }
    }

    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, java.net.URL resourceURL) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
                String line;
                String clazz = null;
                while ((line = reader.readLine()) != null) {
                    final int ci = line.indexOf('#');
                    if (ci >= 0) {
                        line = line.substring(0, ci);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        String name = null;
                        int i = line.indexOf('=');
                        if (i > 0) {
                            name = line.substring(0, i).trim();
                            clazz = line.substring(i + 1).trim();
                        } else {
                            clazz = line;
                        }
                        if (StringUtils.isNotEmpty(clazz)) {
                            loadClass(extensionClasses, Class.forName(clazz, true, classLoader), name);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("Exception occurred when loading class in url:{}", resourceURL, t);
        }
    }

    private void loadClass(Map<String, Class<?>> extensionClasses, Class<?> clazz, String name) {
        extensionClasses.put(name, clazz);
    }

}
