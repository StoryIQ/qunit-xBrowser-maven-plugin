package com.storyiq.mavenplugin.qunit.reporting;

import java.io.PrintStream;

public class SummaryReporter implements ResultReporter {

    private final PrintStream out;
    
    private int totalTests = 0;
    private int totalFiles = 0;
    private int passed = 0;
    private int failed = 0;
    
    public SummaryReporter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void startTest(String name) {
        out.println("Running " + name);
    }

    @Override
    public void testStopped(int total, int passed, int failed) {
        totalFiles++;
        totalTests += total;
        this.passed += passed;
        this.failed += failed;
        out.printf("Tests run: %1d, Failures: %1d", total, failed);
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
        out.printf("Tests run: %1d, Failures: %1d\n", totalTests, failed);
        out.println();
    }

}
