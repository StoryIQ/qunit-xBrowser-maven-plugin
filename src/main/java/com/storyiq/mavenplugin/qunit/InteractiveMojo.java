package com.storyiq.mavenplugin.qunit;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Goal which starts QUnit plugin in interactive mode. This allows developers to
 * manually run QUnit tests in a web browser to receive feedback. Refreshing the
 * QUnit test in the web browser will load any JavaScript code changes from
 * disk, allowing easy TDD of JavaScript without having to restart any services.
 * 
 * @goal interactive
 * @requiresDirectInvocation true
 */
public class InteractiveMojo extends AbstractQUnitMojo {

    @Override
    public void execute() throws MojoExecutionException {
        final Log log = getLog();
        startHttpService();
        try {
            service.waitUntilFinished();
        } catch (InterruptedException e) {
            log.warn("HTTP Service has failed to shutdown correctly", e);
        }
    }
}
