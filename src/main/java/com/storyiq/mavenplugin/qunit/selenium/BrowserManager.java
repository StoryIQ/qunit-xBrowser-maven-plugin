package com.storyiq.mavenplugin.qunit.selenium;

import java.util.Map.Entry;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserManager implements WebDriverProvider {

    private WebDriver webDriverInstance;
    private String browser;
    private Properties properties = new Properties();

    public BrowserManager() {

    }

    public BrowserManager(String browser, Properties properties) {
        this.browser = browser;
        this.properties = properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowser() {
        return browser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.storyiq.mavenplugin.qunit.selenium.WebDriverProvider#createDriver()
     */
    @Override
    public WebDriver createDriver() {
        if (webDriverInstance != null) {
            return webDriverInstance;
        }
        DesiredCapabilities desiredCapabilities;

        if ("firefox".equalsIgnoreCase(browser)) {
            desiredCapabilities = populateCapabilities(DesiredCapabilities
                    .firefox());
            webDriverInstance = new FirefoxDriver(desiredCapabilities);
        } else if ("chrome".equalsIgnoreCase(browser)) {
            desiredCapabilities = populateCapabilities(DesiredCapabilities
                    .chrome());
            webDriverInstance = new ChromeDriver(desiredCapabilities);
        } else if ("ie".equalsIgnoreCase(browser)) {
            desiredCapabilities = populateCapabilities(DesiredCapabilities
                    .internetExplorer());
            webDriverInstance = new InternetExplorerDriver(desiredCapabilities);
        } else {
            throw new IllegalArgumentException("Unknown Browser " + browser);
        }

        return webDriverInstance;
    }

    private DesiredCapabilities populateCapabilities(
            DesiredCapabilities capabilities) {
        for (Entry<Object, Object> property : properties.entrySet()) {
            capabilities.setCapability((String) property.getKey(),
                    property.getValue());
        }
        return capabilities;
    }

    public void close() {
        if (webDriverInstance != null) {
            webDriverInstance.quit();
        }
    }
}
