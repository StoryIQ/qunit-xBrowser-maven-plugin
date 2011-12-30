package com.storyiq.mavenplugin.qunit;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.storyiq.mavenplugin.qunit.reporting.ResultReporter;
import com.storyiq.mavenplugin.qunit.selenium.ResultListener;

public class UnitTestRunner {

    static {
        // shut up Selenium logging
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.WARNING);
    }
    
    private final ResultReporter reporter;
    private final UrlFactory urlProvider;
    private final WebDriver driver;

    public UnitTestRunner(ResultReporter reporter, UrlFactory urlFactory) {
        this.reporter = reporter;
        this.urlProvider = urlFactory;
        driver = new FirefoxDriver();
    }

    public void shutdown() {
        driver.close();
    }

    public void runTests(String[] unitTests) {
        ResultListener pageObject = new ResultListener(driver);
        for (String name : unitTests) {
            reporter.suiteStart(name);
            String urlOfTest = urlProvider.getUrlOfTest(name);
            pageObject.listenTo(urlOfTest, reporter);
            reporter.suiteEnd();
        }
    }

}
