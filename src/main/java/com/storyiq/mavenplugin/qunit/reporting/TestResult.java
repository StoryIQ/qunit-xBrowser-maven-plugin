package com.storyiq.mavenplugin.qunit.reporting;

public enum TestResult {

    PASSED, FAILED, SKIPPED;

    public static TestResult getResult(String name) {
        if (name.equalsIgnoreCase("pass")) {
            return PASSED;
        } else if (name.equalsIgnoreCase("fail")) {
            return FAILED;
        } else {
            return SKIPPED;
        }
    }
}
