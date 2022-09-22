package com.bernheims.applitools.testng.pageapi;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.applitools.eyes.AccessibilityRegionType;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.SeleniumCheckSettings;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.support.Colors;
import org.openqa.selenium.support.Color;

public abstract class EyesPageObject extends PageObject {

	protected Eyes eyes;
	protected SeleniumCheckSettings target;
	
	public EyesPageObject(WebDriver driver, Eyes eyes, String name) {
	    super(driver, name);
		this.eyes = eyes;
	}
	
	public EyesPageObject(WebDriver driver, Eyes eyes) {
	    this(driver, eyes, null);
	}
	
	public String pageName() {
	    return name;
	}

	public EyesPageObject saveScreenshot() throws WebDriverException, IOException {
        TakesScreenshot picDriver = (TakesScreenshot) driver;
        File savedPic = new File(System.getenv("HOME") + File.separator + "Screenshot-" + Instant.now().getEpochSecond() + ".png");
        FileUtils.writeByteArrayToFile(savedPic, picDriver.getScreenshotAs(OutputType.BYTES));
        System.out.printf("Screenshot saved to %si\n", savedPic.getAbsolutePath());
        return this;
    }
	
	public EyesPageObject changePageBackground(String colorName) {
	    return changePageBackground(Colors.valueOf(colorName).getColorValue());
	}
	
	public EyesPageObject changePageBackground(Color color) {
	    return changeElementBackground("body", color);
	}
	
	public EyesPageObject changeElementBackground(String selectorQuery, String colorName) {
	    return changeElementBackground(selectorQuery, Colors.valueOf(colorName).getColorValue());
	}
	
	public EyesPageObject changeElementBackground(String selectorQuery, Color color) {
	    String code = String.format("document.querySelector('%s').style.background = '%s';", selectorQuery, color.asRgb());
	    return executeJavaScript(code);
	}
	
	public EyesPageObject changeElementColor(String selectorQuery, String colorName) {
	    return changeElementColor(selectorQuery, Colors.valueOf(colorName).getColorValue());
	}
	
	public EyesPageObject changeElementColor(String selectorQuery, Color color) {
	    String code = String.format("document.querySelector('%s').style.color = '%s';", selectorQuery, color.asRgb());
	    return executeJavaScript(code);
	}
	
	public EyesPageObject executeJavaScript(String javaScriptCode) {
	    JavascriptExecutor executor = (JavascriptExecutor) driver;
	    System.out.printf("Executing Javascript:\n\t%s\n\n", javaScriptCode);
	    executor.executeScript(javaScriptCode);
	    return this;
	}
	
	protected SeleniumCheckSettings target() {
	    if (target == null) targetPage();
	    return target;
	}
	
	protected EyesPageObject targetPage() {
	    target = Target.window().fully().withName(pageName());
	    return this;
	}
	
	protected EyesPageObject targetOf(WebElement element) {
	    target = Target.region(element);
	    return this;
	}
	
	protected EyesPageObject targetOf(By by) {
	    target = Target.region(by);
	    return this;
	}
	
	protected EyesPageObject named(String name) {
	    target = target().withName(name);
	    return this;
	}
	
	protected EyesPageObject exact() {
	    target = target().exact();
	    return this;
	}
	
	protected EyesPageObject strict() {
	    target = target().strict();
	    return this;
	}
	
	protected EyesPageObject layout() {
	    target = target().layout();
	    return this;
	}
	
	protected EyesPageObject content() {
	    target = target().content();
	    return this;
	}
	
	protected EyesPageObject contentOf(WebElement element) {
	    target = target().content(element);
	    return this;
	}
	
	protected EyesPageObject contentOf(By by) {
	    target = target().content(by);
	    return this;
	}
	
	protected EyesPageObject validateAccessibilityOf(WebElement element, AccessibilityRegionType type) {
	    target = target().accessibility(element, type);
	    return this;
	}
	
	protected EyesPageObject validateAccessibilityOf(By by, AccessibilityRegionType type) {
	    target = target().accessibility(by, type);
	    return this;
	}
	
	protected EyesPageObject ignoreAccessibilityOf(WebElement element) {
	    return validateAccessibilityOf(element, AccessibilityRegionType.IgnoreContrast);
	}
	
	protected EyesPageObject ignoreAccessibilityOf(By by) {
	    return validateAccessibilityOf(by, AccessibilityRegionType.IgnoreContrast);
	}
	
	protected EyesPageObject ignoreAccessibilityOf(String id) {
	    return validateAccessibilityOf(By.id(id), AccessibilityRegionType.IgnoreContrast);
	}
	
	protected EyesPageObject validateAccessibilityOfGraphic(WebElement element) {
	    return validateAccessibilityOf(element, AccessibilityRegionType.GraphicalObject);
	}
	
	protected EyesPageObject validateAccessibilityOfGraphic(By by) {
	    return validateAccessibilityOf(by, AccessibilityRegionType.GraphicalObject);
	}
	
	protected EyesPageObject validateAccessibilityOfGraphic(String id) {
	    return validateAccessibilityOf(By.id(id), AccessibilityRegionType.GraphicalObject);
	}
	
	protected EyesPageObject validateAccessibilityOfBoldText(WebElement element) {
	    return validateAccessibilityOf(element, AccessibilityRegionType.BoldText);
	}
	
	protected EyesPageObject validateAccessibilityOfBoldText(By by) {
	    return validateAccessibilityOf(by, AccessibilityRegionType.BoldText);
	}
	
	protected EyesPageObject validateAccessibilityOfBoldText(String id) {
	    return validateAccessibilityOf(By.id(id), AccessibilityRegionType.BoldText);
	}
	
	protected EyesPageObject timeout(int timeoutInMilliseconds) {
	    target = target().timeout(timeoutInMilliseconds);
	    return this;
	}
	
	protected EyesPageObject noTimeout() {
	    target = target().timeout(-1);
	    return this;
	}
	
	public EyesPageObject check() {
	    eyes.check(target());
	    return this;
	}
	
}
