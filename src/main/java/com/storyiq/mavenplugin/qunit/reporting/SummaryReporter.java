package com.storyiq.mavenplugin.qunit.reporting;

import java.io.PrintStream;

public class SummaryReporter implements ResultReporter {

    private final PrintStream out;

    private int totalTests = 0;
    private int totalFiles = 0;
    private int passed = 0;
    private int failed = 0;
    private int skipped = 0;

    public SummaryReporter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void suiteStart(String name) {
        out.println("Running " + name);
    }

    @Override
    public void testEnd(int total, int passed, int failed, int skipped) {
        totalFiles++;
        totalTests += total;
        this.passed += passed;
        this.failed += failed;
        this.skipped += skipped;
        out.printf("Result: %1d tests of %1d passed, %1d failed, %1d skipped",
                passed, total, failed, skipped);
        if (failed > 0) {
            out.print(" <<< FAILURE!");
        }
        out.println();
    }

    public boolean hasFailure() {
        return failed > 0;
    }

    public void printSummary() {
        out.println();
        out.println("Results :");
        out.println();
        out.printf("Total: %1d, Passed: %1d, Failures: %1d, Skipped: %1d\n", totalTests, passed, failed, skipped);
        out.println();
    }

    @Override
    public void suiteEnd() {
        // TODO Auto-generated method stub

    }

    @Override
    public void aborted(String string) {
        out.println(string);
    }

}
