package com.storyiq.mavenplugin.qunit;

import java.io.File;

public class Mapping {

    private String context;
    
    private File directory;

    public Mapping(String context, File resource) {
        this.context = context;
        this.directory = resource;
    }

    public Mapping() {
        
    }
    
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File resource) {
        this.directory = resource;
    }
    
}
