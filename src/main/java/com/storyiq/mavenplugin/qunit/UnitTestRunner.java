package com.storyiq.mavenplugin.qunit;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.storyiq.mavenplugin.qunit.reporting.ResultReporter;
import com.storyiq.mavenplugin.qunit.selenium.BrowserManager;
import com.storyiq.mavenplugin.qunit.selenium.ResultListener;

public class UnitTestRunner {

    static {
        // shut up Selenium logging
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.WARNING);
    }

    private final ResultReporter reporter;
    private final UrlFactory urlProvider;
    private final BrowserManager driverProvider;

    public UnitTestRunner(ResultReporter reporter, UrlFactory urlFactory,
            BrowserManager driverProvider) {
        this.reporter = reporter;
        this.urlProvider = urlFactory;
        this.driverProvider = driverProvider;
    }

    public void shutdown() {
        driverProvider.close();
    }

    public void runTests(String[] unitTests) {
        ResultListener pageObject = new ResultListener(driverProvider);
        for (String name : unitTests) {
            reporter.suiteStart(name);
            String urlOfTest = urlProvider.getUrlOfTest(name);
            pageObject.listenTo(urlOfTest, reporter);
            reporter.suiteEnd();
        }
    }

}
