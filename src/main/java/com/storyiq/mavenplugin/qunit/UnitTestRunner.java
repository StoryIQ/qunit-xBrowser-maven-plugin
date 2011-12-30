package com.storyiq.mavenplugin.qunit;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.storyiq.mavenplugin.qunit.reporting.ResultReporter;
import com.storyiq.mavenplugin.qunit.selenium.QUnitPageObject;
import com.storyiq.mavenplugin.qunit.selenium.TestResult;

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

    public void run(String name, String testUrl) {
        reporter.startTest(name);
        driver.get(testUrl);
        QUnitPageObject pageObject = new QUnitPageObject(driver);
        try {
            TestResult result = pageObject.getResult();
            reporter.testStopped(result.getTotal(), result.getPassed(),
                    result.getFailed());
        } catch (TimeoutException e) {
            // TODO: Report this timeout
        }

    }

    public void shutdown() {
        driver.close();
    }

    public void runTests(String[] unitTests) {
        for (String name : unitTests) {
            run(name, urlProvider.getUrlOfTest(name));
        }
    }

}
