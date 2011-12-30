package com.storyiq.mavenplugin.qunit.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QUnitPageObject {

    private static final String TOTAL_TESTS_RUN_CSS_CLASS = "total";
    private static final String TOTAL_FAILED_CSS_CLASS = "failed";
    private static final String TOTAL_PASSED_CSS_CLASS = "passed";
    private static final String TEST_RESULT_TOTALS_PARENT_ID = "qunit-testresult";

    private final WebDriver driver;

    public QUnitPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public TestResult getResult() throws TimeoutException {

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        webDriverWait.until(new TestsHaveFinished());
        final WebElement qunitResults = driver.findElement(By
                .id(TEST_RESULT_TOTALS_PARENT_ID));

        int passed = getCountFromResult(qunitResults, TOTAL_PASSED_CSS_CLASS);
        int failed = getCountFromResult(qunitResults, TOTAL_FAILED_CSS_CLASS);
        int total = getCountFromResult(qunitResults, TOTAL_TESTS_RUN_CSS_CLASS);

        TestResult result = new TestResult(passed, failed, total);
        return result;
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
            final int passed = getCountFromResult(qunitResults,
                    TOTAL_PASSED_CSS_CLASS);
            final int failed = getCountFromResult(qunitResults,
                    TOTAL_FAILED_CSS_CLASS);
            final int total = getCountFromResult(qunitResults,
                    TOTAL_TESTS_RUN_CSS_CLASS);
            return total == passed + failed;
        }
    }
}
