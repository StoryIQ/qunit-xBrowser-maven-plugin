package com.storyiq.mavenplugin.qunit.selenium;

import org.openqa.selenium.WebDriver;

public interface WebDriverProvider {

    public abstract WebDriver createDriver();

}