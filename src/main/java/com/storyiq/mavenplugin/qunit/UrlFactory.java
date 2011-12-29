package com.storyiq.mavenplugin.qunit;

public class UrlFactory {

    private final String testContext;
    private final int port;
    
    public UrlFactory(String testContext, int port) {
        super();
        this.testContext = testContext;
        this.port = port;
    }

    public String getUrlOfTest(String name) {
        String filename = name.replaceAll("\\\\", "/");
        StringBuffer url = new StringBuffer("http://localhost:");
        url.append(port);
        url.append(testContext);
        if (!(filename.startsWith("/") || testContext.endsWith("/"))) {
            url.append("/");
        }
        url.append(filename);
        return url.toString();
    }
   
}
