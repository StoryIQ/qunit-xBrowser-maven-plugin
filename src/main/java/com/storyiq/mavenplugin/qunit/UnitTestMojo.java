package com.storyiq.mavenplugin.qunit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/**
 * 
 * @goal test
 * @phase test
 */
public class UnitTestMojo extends AbstractQUnitMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log log = getLog();
        service = new HttpService(getPort(), getResourceMap());
        try {
            log.info("Starting HTTP Service");
            service.start();
            log.info("HTTP Service Started");
        } catch (Exception e) {
            throw new MojoExecutionException("Starting HTTP Service Failed", e);
        }
        
        try {
            service.stop();
            service.waitUntilFinished();
        } catch (Exception e) {
            log.warn("HTTP Service has failed to shutdown correctly", e);
        }
    }

}
