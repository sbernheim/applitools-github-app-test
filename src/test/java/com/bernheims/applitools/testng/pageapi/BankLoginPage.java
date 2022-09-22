package com.bernheims.applitools.testng.pageapi;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.applitools.eyes.selenium.Eyes;

public class BankLoginPage extends EyesPageObject {

	@FindBy(id = "username")
	private WebElement username;
	
	@FindBy(id = "password")
	private WebElement password;

	@FindBy(id = "log-in")
	private WebElement loginButton;
	
	protected static String defaultName = "Bank Login Page";
	
	public BankLoginPage(WebDriver driver, Eyes eyes) {
		super(driver, eyes, defaultName);
	}
	
	public BankLoginPage checkLoginForm() {
		Assert.assertTrue(loginButton.isDisplayed(), String.format("%s login button is not visible", pageName()));
		check();
		return this;
	}
	
	public BankLoginPage enterUserame(String username) {
		this.username.clear();
		this.username.sendKeys(username);
		return this;
	}

	public BankLoginPage enterPassword(String password) {
		this.password.clear();
		this.password.sendKeys(password);
		return this;
	}
	
	public BankMainPage submit() {
		loginButton.click();
		return new BankMainPage(driver, eyes);
	}
	
}