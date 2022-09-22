package com.bernheims.applitools.testng.pageapi;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageObject {

	protected String name;
	protected WebDriver driver;
	
	public PageObject(WebDriver driver, String name) {
		this.driver = driver;
	    this.name = StringUtils.isBlank(name) ? this.getClass().getSimpleName() : name;
		PageFactory.initElements(driver, this);
	}

	public PageObject(WebDriver driver) {
	    this(driver, null);
	}

	public String pageName() {
	    return name;
	}
	
}
