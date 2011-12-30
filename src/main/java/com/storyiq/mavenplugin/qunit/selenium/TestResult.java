package com.storyiq.mavenplugin.qunit.selenium;

public class TestResult {

    private int passed;
    private int failed;
    private int total;

    public TestResult(int passed, int failed, int total) {
        super();
        this.passed = passed;
        this.failed = failed;
        this.total = total;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean hasFailure() {
        return failed > 0;
    }
}
