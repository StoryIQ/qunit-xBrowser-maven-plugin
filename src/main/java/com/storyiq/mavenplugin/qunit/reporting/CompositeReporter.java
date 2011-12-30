package com.storyiq.mavenplugin.qunit.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeReporter implements ResultReporter {

    private List<ResultReporter> reporters = new ArrayList<ResultReporter>();

    public CompositeReporter(ResultReporter ... reporters) {
        this.reporters = Arrays.asList(reporters);
    }
    
    public CompositeReporter(List<ResultReporter> reporters) {
        this.reporters = reporters;
    }

    public List<ResultReporter> getReporters() {
        return reporters;
    }

    public void setReporters(List<ResultReporter> reporters) {
        this.reporters = reporters;
    }

    @Override
    public void startTest(String name) {
        for (ResultReporter reporter : reporters) {
            reporter.startTest(name);
        }
    }

    @Override
    public void testStopped(int total, int passed, int failed) {
        for (ResultReporter reporter : reporters) {
            reporter.testStopped(total, passed, failed);
        }
    }

}
