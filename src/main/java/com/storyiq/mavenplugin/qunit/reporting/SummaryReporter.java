package com.storyiq.mavenplugin.qunit.reporting;

import java.io.PrintStream;
import java.util.ArrayList;

public class SummaryReporter implements ResultReporter {

    private final PrintStream out;

    private int totalTests = 0;
    private int totalFiles = 0;
    private int passed = 0;
    private int totalFails = 0;
    private int skipped = 0;
    private boolean failure = false;

    public SummaryReporter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void suiteStart(String name) {

    }

    @Override
    public void testStarted(String url, String name) {
        out.printf("Starting %s (%s)\n", name, url);
    }

    @Override
    public void testEnd(int total, int passed, int failed, int skipped) {
        totalFiles++;
        totalTests += total;
        this.passed += passed;
        this.totalFails += failed;
        this.skipped += skipped;

        out.printf("Result: %1d tests of %1d passed, %1d failed, %1d skipped",
                passed, total, failed, skipped);
        if (failed > 0) {
            out.print(" <<< FAILURE!");
        }
        out.println();
    }

    @Override
    public void recordResult(TestResult status, String testName,
            String moduleName, ArrayList<TestMethodResult> failureMessages) {
        if (status == TestResult.FAILED) {
            failure = true;
            for (TestMethodResult result : failureMessages) {
                out.printf("Failed: %s - %s\n", testName,
                        result.getMethodName());
            }
        }

    }

    public boolean hasFailure() {
        return failure;
    }

    public void printSummary() {
        out.println();
        out.println("Results :");
        out.println();
        out.printf("Total: %1d, Passed: %1d, Failures: %1d, Skipped: %1d\n",
                totalTests, passed, totalFails, skipped);
        out.println();
    }

    @Override
    public void suiteEnd() {

    }

    @Override
    public void aborted(String string) {
        out.println(string);
        failure = true;
    }

}
