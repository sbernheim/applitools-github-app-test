package com.bernheims.applitools.testng.pageapi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.applitools.eyes.selenium.Eyes;


public class KeypressPage extends EyesPageObject {

	@FindBy(id = "result")
	private WebElement result;
	
	@FindBy(id = "target")
	private WebElement keyPressInputField;

	protected static String defaultName = "Keypress Page";
	
	public KeypressPage(WebDriver driver, Eyes eyes) {
		super(driver, eyes, defaultName);
	}
	
	public KeypressPage checkPageLoaded() {
		Assert.assertTrue(keyPressInputField.isDisplayed(), String.format("%s target text field was not found", pageName()));
		targetPage().ignoreAccessibilityOf("target").check();
		return this;
	}
	
	public KeypressPage pressKey(String key) {
        new Actions(driver).sendKeys(key).perform();
		return this;
	}

	public String getResult() {
        return result.getText().replaceFirst("You entered: ", "");
	}
	
	public KeypressPage changePageBackground(String colorName) {
	    super.changePageBackground(colorName);
	    return this;
	}
	
	public KeypressPage changeResultBackground(String colorName) {
	    super.changeElementBackground("p#result", colorName);
	    return this;
	}
	
	public KeypressPage changeResultColor(String colorName) {
	    super.changeElementColor("p#result", colorName);
	    return this;
	}
	
	public KeypressPage checkResult(String expected) {
	    String pressResult = this.getResult();
	    Assert.assertEquals(pressResult, expected, String.format("Key Press result '%s' was not expected '%s'", pressResult, expected));
	    //checkPage();
	    //checkPageLayout();
	    //checkPageContent();
	    //checkWindowWithContentRegion(result);
	    //checkWindowWithResultContentRegion();
	    WebElement resultE = driver.findElement(By.id("result"));
	    //checkPageContent().checkElementById("result","resultElement");
	    //eyes.check(Target.window().fully().withName(pageName()).content(resultE));
        //eyes.check(Target.window().fully().withName(pageName()).content(By.id("result")));
	    targetPage().ignoreAccessibilityOf("target").contentOf(resultE).validateAccessibilityOfBoldText(resultE).check();
	    //checkElement(resultE, "resultElement");
	    //checkElementById("result", "resultElement");
	    //eyes.check("resultElement", Target.region(resultE).timeout(-1).fully().content());
	    targetOf(resultE).content().noTimeout().check();
		return this;
	}
	
}