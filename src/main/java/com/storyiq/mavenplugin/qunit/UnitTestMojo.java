package com.storyiq.mavenplugin.qunit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import com.storyiq.mavenplugin.qunit.reporting.CompositeReporter;
import com.storyiq.mavenplugin.qunit.reporting.LoggingReporter;
import com.storyiq.mavenplugin.qunit.reporting.ResultReporter;
import com.storyiq.mavenplugin.qunit.reporting.SummaryReporter;
import com.storyiq.mavenplugin.qunit.reporting.TextFileResultReporter;
import com.storyiq.mavenplugin.qunit.selenium.BrowserManager;

/**
 * Runs JavaScript units tests with QUnit in the specified Web browser.
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
     * Set this to "true" to bypass JavaScript Unit tests entirely.
     * 
     * @parameter default-value="false" expression="${maven.test.skip}"
     */
    private boolean skip;

    /**
     * The directory for publishing test results
     * 
     * @parameter default-value="${project.build.directory}/qunit"
     */
    private File reportDirectory;

    /**
     * The web browser to use for Unit Testing. <code><br/>
     * &lt;browser><br/>
     * &nbsp;&lt;name>firefox&lt;/name><br/>
     * &lt;/browser><br/>
     * 
     * @parameter
     * @required
     */
    private Browser browser;

    private final FileSetManager fileManager = new FileSetManager();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log log = getLog();
        if (skip) {
            log.info("Skipping QUnit tests");
            return;
        }

        if (browser == null || browser.getName() == null) {
            throw new MojoExecutionException(
                    "The browser property has not been defined. Which web browser should I be using?");
        }

        startHttpService();
        UrlFactory urlFactory = new UrlFactory(getTestSourceContext(),
                getPort());

        TextFileResultReporter fileReport = new TextFileResultReporter(
                reportDirectory);
        SummaryReporter resultsSummary = new SummaryReporter(System.out);
        ResultReporter reporter = new CompositeReporter(
                new LoggingReporter(log), resultsSummary, fileReport);
        UnitTestRunner runner = new UnitTestRunner(reporter, urlFactory,
                new BrowserManager(browser.getName(), browser.getProperties()));
        try {
            runner.runTests(getQUnitTests());
        } catch (Exception e) {
            throw new MojoExecutionException("Test Run Error. Aborting...", e);
        } finally {
            runner.shutdown();
            stopHttpService();
        }

        resultsSummary.printSummary();
        if (resultsSummary.hasFailure()) {
            throw new MojoFailureException("One or more test failures");
        }
    }

    private void stopHttpService() {
        final Log log = getLog();
        try {
            service.stop();
            service.waitUntilFinished();
            log.info("HTTP Service Stopped");
        } catch (Exception e) {
            log.warn("HTTP Service has failed to shutdown correctly", e);
        }
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

    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public Browser getBrowser() {
        return browser;
    }

}
