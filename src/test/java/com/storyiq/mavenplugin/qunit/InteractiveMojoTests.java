package com.storyiq.mavenplugin.qunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class InteractiveMojoTests {

    private static final String TEST_FOLDER_CONTEXT = "/test";
    private static final String ROOT_CONTEXT = "/";
    private static final File DIRECTORY = new File(".");

    @Test
    public void noDuplicateResourceContexts() {
        InteractiveMojo mojo = createMojo();
        mojo.setTestSourceContext(ROOT_CONTEXT);
        mojo.setTestSourceDirectory(DIRECTORY);
        mojo.setSourcePaths(new Mapping[] {
                new Mapping(TEST_FOLDER_CONTEXT, DIRECTORY),
                new Mapping(TEST_FOLDER_CONTEXT, DIRECTORY) });

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("Duplicate resource contexts found", e.getMessage());
        }
    }

    @Test
    public void resourceContextsShouldNotMatchTestSourceContext() {
        InteractiveMojo mojo = createMojo();
        mojo.setTestSourceContext(TEST_FOLDER_CONTEXT);
        mojo.setTestSourceDirectory(DIRECTORY);
        mojo.setSourcePaths(new Mapping[] { new Mapping(TEST_FOLDER_CONTEXT,
                DIRECTORY) });

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("Resource context matches test source context",
                    e.getMessage());
        }
    }

    @Test
    public void testSourceDirectoryMustExist() {
        InteractiveMojo mojo = createMojo();
        mojo.setTestSourceContext(ROOT_CONTEXT);
        File mockDirectory = mock(File.class);
        when(mockDirectory.exists()).thenReturn(false);
        mojo.setTestSourceDirectory(mockDirectory);
        mojo.setSourcePaths(new Mapping[] { new Mapping(TEST_FOLDER_CONTEXT,
                DIRECTORY) });

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("Test Source directory does not exist", e.getMessage());
        }
    }

    @Test
    public void testSourceDirectoryMustBeADirectory() throws URISyntaxException {
        InteractiveMojo mojo = createMojo();
        mojo.setTestSourceContext(ROOT_CONTEXT);
        File mockDirectory = mock(File.class);
        when(mockDirectory.toURI()).thenReturn(new URI("file:/"));
        when(mockDirectory.exists()).thenReturn(true);
        when(mockDirectory.isDirectory()).thenReturn(false);
        mojo.setTestSourceDirectory(mockDirectory);
        mojo.setSourcePaths(new Mapping[] { new Mapping(TEST_FOLDER_CONTEXT,
                DIRECTORY) });

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("Test Source must be a directory", e.getMessage());
        }
    }

    @Test
    public void mappedSourcePathDirectoriesMustExist() {
        InteractiveMojo mojo = createMojo();
        mojo.setTestSourceContext(ROOT_CONTEXT);
        File mockDirectory = mock(File.class);
        when(mockDirectory.exists()).thenReturn(false);
        mojo.setTestSourceDirectory(DIRECTORY);
        mojo.setSourcePaths(new Mapping[] { new Mapping(TEST_FOLDER_CONTEXT,
                mockDirectory) });

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("Mapped source directory does not exist",
                    e.getMessage());
        }
    }

    @Test(timeout = 5000)
    public void mappedSourceDirectoryMustBeADirectory()
            throws URISyntaxException {
        InteractiveMojo mojo = createMojo();
        mojo.setTestSourceContext(ROOT_CONTEXT);
        File mockDirectory = mock(File.class);
        when(mockDirectory.toURI()).thenReturn(new URI("file:/"));
        when(mockDirectory.exists()).thenReturn(true);
        when(mockDirectory.isDirectory()).thenReturn(false);
        mojo.setTestSourceDirectory(DIRECTORY);
        mojo.setSourcePaths(new Mapping[] { new Mapping(TEST_FOLDER_CONTEXT,
                mockDirectory) });

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("Mapped source must be a directory", e.getMessage());
        }
    }

    private InteractiveMojo createMojo() {
        InteractiveMojo mojo = new InteractiveMojo();
        mojo.setServer(mock(HttpService.class));
        return mojo;
    }

    @Test
    public void mappedContextsShouldNotBeEmpty() {
        InteractiveMojo mojo = createMojo();
        mojo.setTestSourceContext(ROOT_CONTEXT);
        mojo.setTestSourceDirectory(DIRECTORY);
        mojo.setSourcePaths(new Mapping[] { new Mapping("", DIRECTORY),
                new Mapping(TEST_FOLDER_CONTEXT, DIRECTORY) });

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("Resource context is empty", e.getMessage());
        }
    }

    @Test
    public void mappedDirectoryShouldNotBeNull() {
        InteractiveMojo mojo = createMojo();
        mojo.setTestSourceContext(ROOT_CONTEXT);
        mojo.setTestSourceDirectory(DIRECTORY);
        mojo.setSourcePaths(new Mapping[] { new Mapping(TEST_FOLDER_CONTEXT,
                null) });

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("Mapped source directory is required", e.getMessage());
        }
    }
}
