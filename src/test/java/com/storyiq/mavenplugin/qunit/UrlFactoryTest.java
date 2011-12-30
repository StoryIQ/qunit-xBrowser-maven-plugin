package com.storyiq.mavenplugin.qunit;

import static org.junit.Assert.*;
import org.junit.Test;

public class UrlFactoryTest {

    @Test
    public void includeHttpPortInUrl() {
        UrlFactory factory = new UrlFactory("/context", 4200);
        String url = factory.getUrlOfTest("example.html");
        assertEquals("http://localhost:4200/context/example.html", url);
    }
    
    @Test
    public void addTrailingSlashToContext() {
        UrlFactory factory = new UrlFactory("/context", 4200);
        String url = factory.getUrlOfTest("example.html");
        assertEquals("http://localhost:4200/context/example.html", url);
    }
    
    @Test
    public void dontAddTrailingSlashToRootContext() {
        UrlFactory factory = new UrlFactory("/", 4200);
        String url = factory.getUrlOfTest("example.html");
        assertEquals("http://localhost:4200/example.html", url);
    }
    
    @Test
    public void fileNameStartsWithASlash() {
        UrlFactory factory = new UrlFactory("/context", 4200);
        String url = factory.getUrlOfTest("/example.html");
        assertEquals("http://localhost:4200/context/example.html", url);
    }
    
    @Test
    public void replaceWindowsFilesystemSlashes() {
        UrlFactory factory = new UrlFactory("/context", 4200);
        String url = factory.getUrlOfTest("hello\\example.html");
        assertEquals("http://localhost:4200/context/hello/example.html", url);
    }
}
