package com.storyiq.mavenplugin.qunit.selenium;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.storyiq.mavenplugin.qunit.reporting.ResultReporter;
import com.storyiq.mavenplugin.qunit.reporting.TestMethodResult;
import com.storyiq.mavenplugin.qunit.reporting.TestResult;

public class ResultListener {

    private static final String QUNIT_TEST_CASE_CSS_SELECTOR = "#qunit-tests > li";
    private static final int TEST_FINISH_TIMEOUT_IN_SECONDS = 120;
    private static final String TOTAL_TESTS_RUN_CSS_CLASS = "total";
    private static final String TOTAL_FAILED_CSS_CLASS = "failed";
    private static final String TOTAL_PASSED_CSS_CLASS = "passed";
    private static final String TEST_RESULT_TOTALS_PARENT_ID = "qunit-testresult";

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

        WebElement header = driver.findElement(By.id("qunit-header"));
        String name = header.getText();

        reporter.testStarted(url, name);

        try {
            WebDriverWait finishWait = new WebDriverWait(driver,
                    TEST_FINISH_TIMEOUT_IN_SECONDS);
            finishWait.until(new TestsHaveFinished());

        } catch (TimeoutException e) {
            reporter.aborted(String.format(
                    "Test did not finish within %1d seconds",
                    TEST_FINISH_TIMEOUT_IN_SECONDS));
        }

        final List<WebElement> testCases = driver.findElements(By
                .cssSelector(QUNIT_TEST_CASE_CSS_SELECTOR));
        int passedTotal = 0;
        int failedTotal = 0;
        int skippedTotal = 0;
        for (WebElement result : testCases) {
            String cssClass = result.getAttribute("class");
            TestResult status = TestResult.getResult(cssClass);

            switch (status) {
            case PASSED:
                passedTotal++;
                break;
            case FAILED:
                failedTotal++;
                break;
            case SKIPPED:
                skippedTotal++;
                break;
            }

            String moduleName = getModuleName(result);

            WebElement testNameElement = result.findElement(By
                    .className("test-name"));
            String testName = testNameElement.getText();

            ArrayList<TestMethodResult> failureMessages = new ArrayList<TestMethodResult>();
            if (status == TestResult.FAILED) {
                List<WebElement> failures = result.findElements(By
                        .cssSelector("ol > li.fail"));
                for (WebElement failure : failures) {
                    TestMethodResult method = new TestMethodResult();
                    try {
                        WebElement methodName = failure.findElement(By
                                .cssSelector(".test-message"));
                        WebElement expected = failure.findElement(By
                                .cssSelector(".test-expected > td"));
                        WebElement actual = failure.findElement(By
                                .cssSelector(".test-actual > td"));
                        WebElement sourceLine = failure.findElement(By
                                .cssSelector(".test-source > td"));

                        method.setMethodName(methodName.getText());
                        method.setExpected(expected.getText());
                        method.setActual(actual.getText());
                        method.setSourceLine(sourceLine.getText());
                    } catch (NoSuchElementException e) {
                        method.setMethodName(failure.getText());
                    }

                    failureMessages.add(method);
                }
            }

            reporter.recordResult(status, testName, moduleName, failureMessages);
        }
        reporter.testEnd(testCases.size(), passedTotal, failedTotal,
                skippedTotal);

    }

    private String getModuleName(WebElement result) {
        String moduleName;
        try {
            WebElement moduleNameElement = result.findElement(By
                    .className("module-name"));
            moduleName = moduleNameElement.getText();
        } catch (NoSuchElementException e) {
            moduleName = null;
        }
        return moduleName;
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

        private int getCountFromResult(WebElement qunitResults,
                String reportClass) {
            WebElement passed = qunitResults.findElement(By
                    .className(reportClass));
            return Integer.valueOf(passed.getText());
        }
    }

    private class TestsHaveStarted implements ExpectedCondition<Boolean> {

        @Override
        public Boolean apply(WebDriver driver) {
            final List<WebElement> testCases = driver.findElements(By
                    .cssSelector(QUNIT_TEST_CASE_CSS_SELECTOR));
            // check we haven't missed the start
            WebElement passedTests = null;
            try {
                passedTests = driver.findElement(By
                        .cssSelector("#qunit-testresult > span.passed"));
            } catch (NoSuchElementException e) {
            }

            return testCases.size() > 0 || passedTests != null;
        }

    }
}
