package com.storyiq.mavenplugin.qunit.reporting;

import java.util.List;

public interface ResultReporter {

    public abstract void suiteStart(String name);

    public abstract void testEnd(int total, int passed, int failed, int skipped);

    public abstract void suiteEnd();

    public abstract void aborted(String string);

    public abstract void testStarted(String url, String name);

    public abstract void recordResult(TestResult status, String testName,
            String moduleName, List<TestMethodResult> failedMethods);

}