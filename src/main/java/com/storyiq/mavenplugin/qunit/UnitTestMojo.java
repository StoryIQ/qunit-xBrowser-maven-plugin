package com.storyiq.mavenplugin.qunit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

/**
 * 
 * @goal test
 * @phase test
 */
public class UnitTestMojo extends AbstractQUnitMojo {

    /**
     * A list of &lt;include> elements specifying the tests (by pattern) that
     * should be included in testing. When not specified the default includes
     * will be <code><br/>
     * &lt;includes><br/>
     * &nbsp;&lt;include>**&#47;Test*.html&lt;/include><br/>
     * &nbsp;&lt;include>**&#47;*Test.html&lt;/include><br/>
     * &lt;/includes><br/>
     * 
     * @parameter
     */
    private String[] includes;

    /**
     * A list of &lt;exclude> elements specifying the tests (by pattern) that
     * should be excluded from testing.
     * 
     * @parameter
     */
    private String[] excludes;

    /**
     * Set this to "true" to bypass unit tests entirely.
     * 
     * @parameter default-value="false" expression="${maven.test.skip}"
     */
    private boolean skip;

    private final FileSetManager fileManager = new FileSetManager();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log log = getLog();
        if (skip) {
            log.info("Skipping QUnit tests");
            return;
        }

        service = new HttpService(getPort(), getResourceMap());
        try {
            log.info("Starting HTTP Service");
            service.start();
            log.info("HTTP Service Started");
        } catch (Exception e) {
            throw new MojoExecutionException("Starting HTTP Service Failed", e);
        }

        UrlFactory urlFactory = new UrlFactory(getTestSourceContext(), getPort());
        for (String test : getQUnitTests()) {
            log.info(urlFactory.getUrlOfTest(test));
        }
        try {
            service.stop();
            service.waitUntilFinished();
        } catch (Exception e) {
            log.warn("HTTP Service has failed to shutdown correctly", e);
        }
        log.info("HTTP Service Stopped");
    }

    private String[] getQUnitTests() {
        FileSet fileSet = new FileSet();
        fileSet.setDirectory(getTestSourceDirectory().getAbsolutePath());
        fileSet.setIncludes(getIncludesWithDefaults());
        fileSet.setExcludes(getExcludesWithDefaults());
        return fileManager.getIncludedFiles(fileSet);
    }

    private List<String> getIncludesWithDefaults() {
        if (includes == null) {
            return Arrays.asList("**/*Test.html", "**/Test*.html");
        } else {
            return Arrays.asList(includes);
        }
    }

    private List<String> getExcludesWithDefaults() {
        if (excludes == null) {
            return new ArrayList<String>();
        } else {
            return Arrays.asList(excludes);
        }
    }   
    
}
