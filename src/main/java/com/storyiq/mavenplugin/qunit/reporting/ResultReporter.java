package com.storyiq.mavenplugin.qunit.reporting;

public interface ResultReporter {

    public abstract void startTest(String name);

    public abstract void testStopped(int total, int passed, int failed);

}