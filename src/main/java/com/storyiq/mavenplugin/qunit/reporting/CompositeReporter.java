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
    public void suiteStart(String name) {
        for (ResultReporter reporter : reporters) {
            reporter.suiteStart(name);
        }
    }

    @Override
    public void testEnd(int total, int passed, int failed, int skipped) {
        for (ResultReporter reporter : reporters) {
            reporter.testEnd(total, passed, failed, skipped);
        }
    }

    @Override
    public void suiteEnd() {
        for (ResultReporter reporter : reporters) {
            reporter.suiteEnd();
        }        
    }

    @Override
    public void aborted(String string) {
        for (ResultReporter reporter : reporters) {
            reporter.aborted(string);
        }        
    }

}
