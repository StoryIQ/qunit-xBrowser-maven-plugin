package com.storyiq.mavenplugin.qunit;

import java.util.Properties;

public class Browser {

    /**
     * The name of the Web Browser.
     * 
     * @parameter
     * @required
     */
    private String name;

    /**
     * Request certain desired capabilities of the Web Browser. These directly
     * map to WebDriver's
     * <code><a href="http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/remote/DesiredCapabilities.html">desired capabilities</a></code>
     * 
     * @parameter
     */
    private Properties properties = new Properties();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
