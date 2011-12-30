package com.storyiq.mavenplugin.qunit.reporting;

public interface ResultReporter {

    public abstract void suiteStart(String name);

    public abstract void testEnd(int total, int passed, int failed, int skipped);

    public abstract void suiteEnd();

    public abstract void aborted(String string);

}