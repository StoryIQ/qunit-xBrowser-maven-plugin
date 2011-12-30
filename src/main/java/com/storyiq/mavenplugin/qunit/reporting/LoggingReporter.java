package com.storyiq.mavenplugin.qunit.reporting;

import org.apache.maven.plugin.logging.Log;

public class LoggingReporter implements ResultReporter {

    private final Log log;

    public LoggingReporter(Log log) {
        this.log = log;
    }

    @Override
    public void startTest(String name) {
        log.debug("Run test: " + name);
    }

    @Override
    public void testStopped(int total, int passed, int failed) {
        log.debug(String.format("%1d tests of %1d passed, %1d failed", passed,
                total, failed));
    }

}
