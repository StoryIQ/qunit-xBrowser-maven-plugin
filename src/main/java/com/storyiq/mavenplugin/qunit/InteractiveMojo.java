package com.storyiq.mavenplugin.qunit;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
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
public class InteractiveMojo extends AbstractMojo {

    /**
     * The port for running HTTP access to the JavaScript files. When not
     * specified the default will be 4200.
     * 
     * @parameter expression="${qunit.http.port}" default-value="4200"
     * @required
     */
    private int port;

    /**
     * The source directory containing the QUnit test files. Defaults to
     * <code>"${basedir}/src/test/javascript"</code>
     * 
     * @parameter default-value="${basedir}/src/test/javascript"
     * @required
     */
    private File testSourceDirectory;

    /**
     * The HTTP URL that the QUnit test source directory should be mapped to.
     * Defaults to the root e.g. "http://localhost/"
     * 
     * @parameter default-value="/"
     * @required
     */
    private String testSourceContext;

    /**
     * The source directories containing the JavaScript code under test and any
     * dependencies.
     * 
     * @parameter
     * @required
     */
    private Mapping[] sourcePaths;

    private HttpService service;

    @Override
    public void execute() throws MojoExecutionException {
        final Log log = getLog();
        service = new HttpService(port, getResourceMap());
        try {
            log.info("Starting HTTP Service");
            service.start();
            log.info("HTTP Service Started");
        } catch (Exception e) {
            throw new MojoExecutionException("Starting HTTP Service Failed", e);
        }
        try {
            service.waitUntilFinished();
        } catch (InterruptedException e) {
            log.warn("HTTP Service has failed to shutdown correctly", e);
        }
    }

    private Map<String, URL> getResourceMap() throws MojoExecutionException {
        final Log log = getLog();
        final Map<String, URL> map = new HashMap<String, URL>();

        validateDirectory(testSourceDirectory, "Test Source");

        if (sourcePaths != null) {
            for (Mapping resource : sourcePaths) {
                String resourceContext = resource.getContext();
                if (resourceContext == null
                        || "".equals(resourceContext.trim())) {
                    throw new MojoExecutionException(
                            "Resource context is empty");
                }

                if (map.containsKey(resourceContext)) {
                    throw new MojoExecutionException(
                            "Duplicate resource contexts found");
                }

                if (testSourceContext.equals(resourceContext)) {
                    throw new MojoExecutionException(
                            "Resource context matches test source context");
                }

                File directory = resource.getDirectory();
                validateDirectory(directory, "Mapped source");
                try {
                    map.put(resourceContext, directory.toURI().toURL());
                } catch (MalformedURLException e) {
                    log.warn("Ignoring " + directory.getPath()
                            + ". Could not convert resource directory to URL",
                            e);
                }
            }
        }

        try {
            map.put(testSourceContext, testSourceDirectory.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new MojoExecutionException(
                    "Error occured translating test source directory to a URL",
                    e);
        }

        return map;
    }

    private void validateDirectory(File directory, String directoryType)
            throws MojoExecutionException {
        if (directory == null) {
            throw new MojoExecutionException(directoryType
                    + " directory is required");
        }
        if (!directory.exists()) {
            throw new MojoExecutionException(directoryType
                    + " directory does not exist");
        }
        if (!directory.isDirectory()) {
            throw new MojoExecutionException(directoryType
                    + " must be a directory");
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public File getTestSourceDirectory() {
        return testSourceDirectory;
    }

    public void setTestSourceDirectory(File testSourceDirectory) {
        this.testSourceDirectory = testSourceDirectory;
    }

    public String getTestSourceContext() {
        return testSourceContext;
    }

    public void setTestSourceContext(String testSourceContext) {
        this.testSourceContext = testSourceContext;
    }

    public Mapping[] getSourcePaths() {
        return sourcePaths;
    }

    public void setSourcePaths(Mapping[] testPath) {
        this.sourcePaths = testPath;
    }

    public HttpService getServer() {
        return service;
    }

    public void setServer(HttpService server) {
        this.service = server;
    }
}
