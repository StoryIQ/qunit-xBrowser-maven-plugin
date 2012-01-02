package com.storyiq.mavenplugin.qunit.selenium;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeNotNull;

import java.util.Properties;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class WebDriverProviderTest {

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionForUnknownBroser() {
        Properties browserProperties = new Properties();
        BrowserManager provider = new BrowserManager();
        provider.setProperties(browserProperties);
        provider.setBrowser("iFox");

        provider.createDriver();
    }

    @Test
    public void canCreateLocalFirefoxBrowser() {
        Properties browserProperties = new Properties();
        BrowserManager provider = new BrowserManager();
        provider.setProperties(browserProperties);
        provider.setBrowser("firefox");

        WebDriver driver = provider.createDriver();
        assertNotNull(driver);
        driver.quit();
    }

    @Test
    public void canCreateLocalChromeBrowser() {
        assumeNotNull(System.getProperty("webdriver.chrome.driver"));

        Properties browserProperties = new Properties();
        BrowserManager provider = new BrowserManager();
        provider.setProperties(browserProperties);
        provider.setBrowser("chrome");

        WebDriver driver = provider.createDriver();
        assertNotNull(driver);
        driver.quit();
    }

    @Test
    public void canCreateLocalInternetExplorerBrowser() {
        assumeNotNull(System.getProperty("os.name").startsWith("Windows"));

        Properties browserProperties = new Properties();
        BrowserManager provider = new BrowserManager();
        provider.setProperties(browserProperties);
        provider.setBrowser("ie");

        WebDriver driver = provider.createDriver();
        assertNotNull(driver);
        driver.quit();
    }
}
