package com.storyiq.mavenplugin.qunit.reporting;

import java.util.ArrayList;

import org.apache.maven.plugin.logging.Log;

public class LoggingReporter implements ResultReporter {

    private final Log log;

    public LoggingReporter(Log log) {
        this.log = log;
    }

    @Override
    public void suiteStart(String name) {
        log.debug("Run test: " + name);
    }

    @Override
    public void testEnd(int total, int passed, int failed, int skipped) {
        log.debug(String.format(
                "%1d tests of %1d passed, %1d failed, %1d skipped", passed,
                total, failed, skipped));
    }

    @Override
    public void suiteEnd() {
    }

    @Override
    public void aborted(String string) {
    }

    @Override
    public void testStarted(String url, String name) {
    }

    @Override
    public void recordResult(TestResult status, String testName,
            String moduleName, ArrayList<TestMethodResult> failureMessages) {

    }

}
