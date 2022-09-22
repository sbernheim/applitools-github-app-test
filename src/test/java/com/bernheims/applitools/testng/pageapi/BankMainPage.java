package com.bernheims.applitools.testng.pageapi;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.applitools.eyes.selenium.Eyes;

public class BankMainPage extends EyesPageObject {

    @FindBy(id = "time")
    private WebElement closingTime;
    
    private static String defaultName = "Bank Main Page";
    
	public BankMainPage(WebDriver driver, Eyes eyes) {
		super(driver, eyes, defaultName);
	}

	public BankMainPage checkLoginSuccess() {
		targetPage().strict().check();
		Assert.assertTrue(closingTime.isDisplayed(), String.format("%s does not display closing time after login", pageName()));
		return this;
	}

	public String getClosingTime(){
		return closingTime.getText().replaceFirst("Your nearest branch closes in:\s*", "");
	}

}