package com.storyiq.mavenplugin.qunit.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class TextFileResultReporter implements ResultReporter {

    private File directory;
    private PrintStream report;

    public TextFileResultReporter(File directory) {
        this.directory = directory;
    }

    @Override
    public void suiteStart(String name) {
        String fileName = name + ".txt";
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            report = new PrintStream(new File(directory, fileName));
        } catch (FileNotFoundException e) {

        }
    }

    @Override
    public void testStarted(String url, String name) {
        report.println("-------------------------------------------------------------------------------");
        report.printf("Test set: %s (%s)\n", name, url);
        report.println("-------------------------------------------------------------------------------");
    }

    @Override
    public void testEnd(int total, int passed, int failed, int skipped) {
        // TODO Auto-generated method stub

    }

    @Override
    public void suiteEnd() {
        if (report != null) {
            report.flush();
            report.close();
        }
    }

    @Override
    public void aborted(String reason) {
        report.printf("Aborted: %s\n", reason);
    }

    @Override
    public void recordResult(TestResult status, String testName,
            String moduleName, ArrayList<TestMethodResult> failureMessages) {
        if (status == TestResult.FAILED) {
            report.printf("Failed: %s\n", testName);
            for (TestMethodResult result : failureMessages) {
                report.printf("    %s\n", result.getMethodName());
                report.printf("        Expected: %s\n", result.getExpected());
                report.printf("        Actual: %s\n", result.getActual());
                report.printf("        Source: %s\n", result.getSourceLine());
            }
        }

    }

}
