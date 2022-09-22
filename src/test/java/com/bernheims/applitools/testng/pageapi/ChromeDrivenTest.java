package com.bernheims.applitools.testng.pageapi;

import java.io.File;
import java.io.IOException;
import java.lang.StackWalker.Option;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class ChromeDrivenTest {

	protected static WebDriver driver;
	protected static boolean headless;
	protected static final boolean defaultHeadless = true;
	protected static long implicitWaitSeconds;
	protected static final long defaultImplicitWaitSeconds = 10;
	protected static String suiteName;
	protected static String testName;
	protected static String className;
	protected String methodName;
	
    protected Optional<String> getSetting(String property, String envvar) {
        return Optional.ofNullable(System.getProperty(property))
                .or(() -> Optional.ofNullable(System.getenv(envvar)));
    }
    
    protected String getSetting(String property, String envvar, String defaultValue) {
        return getSetting(property, envvar).orElse(defaultValue);
    }
    
    protected Boolean getBooleanSetting(String property, String envvar, Boolean defaultValue) {
        return getSetting(property, envvar).map(Boolean::parseBoolean).orElse(defaultValue);
    }
    
    protected Boolean getBooleanSetting(String property, String envvar) {
        return getBooleanSetting(property, envvar, Boolean.FALSE);
    }
    
    protected <R> Optional<R> getTransformedSetting(String property, String envvar, Function<String, ? extends R> mapper) {
        return getSetting(property, envvar).map(mapper);
    }
    
    protected <R> R getTransformedSetting(String property, String envvar, Function<String, ? extends R> mapper, R defaultValue) {
        Optional<R> o = getTransformedSetting(property, envvar, mapper);
        return o.isPresent() ? o.get() : defaultValue;
    }
    
    protected boolean settingIsNotBlank(Optional<String> setting) {
        return setting.isPresent() && StringUtils.isNotBlank(setting.get());
    }
    
    @BeforeSuite
    protected void setUpDriver(ITestContext ctx) {
        suiteName = ctx.getSuite().getName();
        System.out.printf("BeforeSuite : %s for %s\n", thisMethod(), suiteName);

        // Read the headless mode setting from a system property or use the default
        // Use headless mode for Continuous Integration (CI) execution.
        // Use headed mode for local development.
        headless = getBooleanSetting("webdriver.chrome.headless", "WEBDRIVER_HEADLESS", defaultHeadless);

        System.out.printf("BeforeSuite : %s - property headless = %b\n", thisMethod(), headless);

		// Get an implicitWaitSeconds setting from a system property or use the default
		implicitWaitSeconds = getTransformedSetting(
		            "webdriver.implicitWaitSeconds",
		            "WEBDRIVER_IMPLICIT_WAIT_SECONDS",
		            Long::parseLong,
		            defaultImplicitWaitSeconds
		        );

        System.out.printf("BeforeSuite : %s - property implicitWaitSeconds = %d\n", thisMethod(), implicitWaitSeconds);
    }

    @BeforeTest
    protected void printBeforeTestName(ITestContext ctx) {
        testName = ctx.getName();
        System.out.printf("BeforeTest  : %s for %s\n", thisMethod(), testName);
    }

	@BeforeClass
	protected void printBeforeClassName() {
        System.out.printf("BeforeClass : %s for %s\n", thisMethod(), this.getClass().getSimpleName());
        //System.out.printf("Before: Class for %s\n", ChromeDrivenTest.class.getSimpleName());
	}
	
	@BeforeMethod
	public void printBeforeMethodName(Method testMethod, Object[] params) {
        methodName = String.format("%s#%s", testMethod.getName(), params[0]);
        System.out.printf("BeforeMethod: %s for %s\n", thisMethod(), methodName);

        // Open the browser with the ChromeDriver instance.
        driver = new ChromeDriver(new ChromeOptions().setHeadless(headless));
        
        // For larger projects, use explicit waits for better control.
        // https://www.selenium.dev/documentation/webdriver/waits/
        // The following call works for Selenium 4:
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));

        // If you are using Selenium 3, use the following call instead:
        driver.manage().timeouts().implicitlyWait(implicitWaitSeconds, TimeUnit.SECONDS);
	}
	
	@AfterMethod
	public void deleteAllCookies(Method testMethod) {
        System.out.printf("AfterMethod : %s for %s\n", thisMethod(), testMethod.getName());
		//driver.manage().deleteAllCookies();
        driver.close();
	}
	
	@AfterClass
	 public void printAfterClassName(){
        System.out.printf("AfterClass  : %s for %s\n", thisMethod(), this.getClass().getSimpleName());
        //System.out.printf("After:  Class for %s\n", ChromeDrivenTest.class.getSimpleName());
	}
	
    @AfterTest
    protected void printAfterTestName(ITestContext ctx) {
        System.out.printf("AfterTest   : %s for %s\n", thisMethod(), testName);
    }

    @AfterSuite
    protected void tearDownChromeDriver() {
        System.out.printf("AfterSuite  : %s for %s\n", thisMethod(), suiteName);
    }
    
    protected ChromeDrivenTest takeScreenshot() throws WebDriverException, IOException {
        TakesScreenshot picDriver = (TakesScreenshot) driver;
        File savedPic = new File(System.getenv("HOME") + File.separator + "Screenshot-" + Instant.now().getEpochSecond() + ".png");
        FileUtils.writeByteArrayToFile(savedPic, picDriver.getScreenshotAs(OutputType.BYTES));
        System.out.printf("Screenshot saved to %si\n", savedPic.getAbsolutePath());
        return this;
    }

	protected static String thisMethod() {
	    StackWalker walker = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);
	    return walker.walk(frames -> frames
	        .skip(1)
	        .findFirst()
	        .map((f) -> String.format("%s.%s", f.getClassName(), f.getMethodName())))
	        .orElse(null);
	}
}
