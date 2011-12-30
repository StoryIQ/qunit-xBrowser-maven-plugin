package com.storyiq.mavenplugin.qunit.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.storyiq.mavenplugin.qunit.reporting.ResultReporter;

public class ResultListener {

    private static final int TEST_FINISH_TIMEOUT_IN_SECONDS = 120;
    private static final String TOTAL_TESTS_RUN_CSS_CLASS = "total";
    private static final String TOTAL_FAILED_CSS_CLASS = "failed";
    private static final String TOTAL_PASSED_CSS_CLASS = "passed";
    private static final String TEST_RESULT_TOTALS_PARENT_ID = "qunit-testresult";
    private static final String QUNIT_TEST_LIST_ID = "qunit-tests";

    private final WebDriver driver;

    public ResultListener(WebDriver driver) {
        this.driver = driver;
    }

    public void listenTo(String url, ResultReporter reporter) {
        driver.get(url);
        try {
            WebDriverWait startWait = new WebDriverWait(driver, 10);
            startWait.until(new TestsHaveStarted());
        } catch (TimeoutException e) {
            reporter.aborted("Test did not start");
            return;
        }

        try {
            WebDriverWait finishWait = new WebDriverWait(driver,
                    TEST_FINISH_TIMEOUT_IN_SECONDS);
            finishWait.until(new TestsHaveFinished());

        } catch (TimeoutException e) {
            reporter.aborted(String.format(
                    "Test did not finish within %1d seconds",
                    TEST_FINISH_TIMEOUT_IN_SECONDS));
        }
            
        final List<WebElement> testCases = driver.findElements(By.cssSelector("#qunit-tests > li"));
        int passedTotal = 0;
        int failedTotal = 0;
        int skippedTotal = 0;
        for (WebElement result : testCases) {
            String cssClass = result.getAttribute("class");
            if (cssClass.equalsIgnoreCase("pass")) {
                passedTotal++;
            } else if (cssClass.equalsIgnoreCase("fail")) {
                failedTotal++;
            } else {
                skippedTotal++;
            }
        }
        reporter.testEnd(testCases.size(), passedTotal, failedTotal, skippedTotal);
        

    }

    private int getCountFromResult(WebElement qunitResults, String reportClass) {
        WebElement passed = qunitResults.findElement(By.className(reportClass));
        return Integer.valueOf(passed.getText());
    }

    private class TestsHaveFinished implements ExpectedCondition<Boolean> {

        @Override
        public Boolean apply(WebDriver d) {
            final WebElement qunitResults = d.findElement(By
                    .id(TEST_RESULT_TOTALS_PARENT_ID));
            getCountFromResult(qunitResults, TOTAL_PASSED_CSS_CLASS);
            getCountFromResult(qunitResults, TOTAL_FAILED_CSS_CLASS);
            getCountFromResult(qunitResults, TOTAL_TESTS_RUN_CSS_CLASS);
            return true;
        }
    }

    private class TestsHaveStarted implements ExpectedCondition<Boolean> {

        @Override
        public Boolean apply(WebDriver driver) {
            final WebElement testList = driver.findElement(By
                    .id(QUNIT_TEST_LIST_ID));
            final List<WebElement> testCases = testList.findElements(By
                    .tagName("li"));
            // TODO: check we haven't missed the start
            return testCases.size() > 0;
        }

    }
}
