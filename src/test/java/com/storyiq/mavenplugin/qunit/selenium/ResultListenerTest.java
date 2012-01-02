package com.storyiq.mavenplugin.qunit.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.FileResource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.storyiq.mavenplugin.qunit.reporting.ResultReporter;
import com.storyiq.mavenplugin.qunit.reporting.TestMethodResult;
import com.storyiq.mavenplugin.qunit.reporting.TestResult;

@RunWith(Parameterized.class)
public class ResultListenerTest implements WebDriverProvider {

    private final Class<? extends WebDriver> driverClass;
    private static Server server = new Server();
    private WebDriver driver;

    @Parameterized.Parameters
    public static Collection<Class<?>[]> browsers() {
        List<Class<?>[]> driverClasses = new ArrayList<Class<?>[]>();
        driverClasses.add(new Class<?>[] { FirefoxDriver.class });
        if (System.getProperty("os.name").startsWith("Windows")) {
            driverClasses.add(new Class<?>[] { InternetExplorerDriver.class });
        }
        if (System.getProperty("webdriver.chrome.driver") != null) {
            driverClasses.add(new Class<?>[] { ChromeDriver.class });
        }
        return driverClasses;
    }

    @BeforeClass
    public static void startJetty() throws Exception {
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(4200);
        server.addConnector(connector);

        HandlerList handlers = new HandlerList();
        URL directory = ResultListenerTest.class.getResource("/mockedQunit");
        ResourceHandler directoryHandler = new ResourceHandler();
        directoryHandler.setDirectoriesListed(true);
        directoryHandler.setBaseResource(new FileResource(directory));
        handlers.addHandler(directoryHandler);

        handlers.addHandler(new DefaultHandler());

        server.setHandler(handlers);
        server.start();
    }

    @AfterClass
    public static void stopJetty() throws Exception {
        server.stop();
        server.join();
    }

    public ResultListenerTest(Class<? extends WebDriver> driverClass) {
        this.driverClass = driverClass;
    }

    @Before
    public void startBrowser() throws InstantiationException,
            IllegalAccessException {
        driver = driverClass.newInstance();
    }

    @After
    public void stopBrowser() {
        driver.quit();
    }

    @Test
    public void failedQUnitTestWithoutModuleName() {
        ResultReporter reporter = mock(ResultReporter.class);

        ResultListener listener = new ResultListener(this);
        listener.listenTo("http://localhost:4200/noModuleName.html", reporter);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(reporter, times(1)).recordResult(any(TestResult.class),
                anyString(), argument.capture(),
                anyListOf(TestMethodResult.class));
        assertNull(argument.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test(timeout = 15000)
    public void understandsAllQUnitAssertionFailures() {
        ResultReporter reporter = mock(ResultReporter.class);

        ResultListener listener = new ResultListener(this);
        listener.setFinishTimeout(10);
        listener.listenTo("http://localhost:4200/allAssertionFailures.html",
                reporter);

        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(reporter, times(1)).recordResult(eq(TestResult.FAILED),
                anyString(), anyString(), argument.capture());
        List<TestMethodResult> failures = argument.getValue();
        assertEquals(8, failures.size());
    }

    @Test(timeout = 15000)
    public void qunitAbortsOnUnitTestLoadingFailure() {
        ResultReporter reporter = mock(ResultReporter.class);

        ResultListener listener = new ResultListener(this);
        listener.listenTo("http://localhost:4200/startState.html", reporter);

        verify(reporter, times(1)).aborted(eq("Test did not start"));
        verify(reporter, never()).testStarted(anyString(), anyString());
    }

    @Test(timeout = 15000)
    public void qunitAbortsIfUnitTestsHangs() {
        ResultReporter reporter = mock(ResultReporter.class);

        ResultListener listener = new ResultListener(this);
        listener.setFinishTimeout(10);
        listener.listenTo("http://localhost:4200/hangingTests.html", reporter);

        verify(reporter, times(1)).testStarted(anyString(), anyString());
        verify(reporter, times(2)).recordResult(eq(TestResult.PASSED),
                anyString(), anyString(), anyListOf(TestMethodResult.class));
        verify(reporter, times(1)).recordResult(eq(TestResult.SKIPPED),
                anyString(), anyString(), anyListOf(TestMethodResult.class));
        verify(reporter, times(1)).aborted(
                startsWith("Test did not finish within"));
    }

    @Override
    public WebDriver createDriver() {
        return driver;
    }
}
