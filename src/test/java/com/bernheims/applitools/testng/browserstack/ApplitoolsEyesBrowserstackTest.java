package com.bernheims.applitools.testng.browserstack;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.fluent.BatchClose;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class ApplitoolsEyesBrowserstackTest {
    
    private static final String batchName = "Example: Selenium Java TestNG on Browserstack";
    private static final String appName = "TestNG Selenium Browserstack Examples";
    private static final String buildTag = "Selenium 3 Win/IEX Tests";

    // Test control inputs to read once and share for all tests
    private static String applitoolsApiKey;
    //private static boolean headless;

    // Applitools objects to share for all tests
    private static BatchInfo batch;
    private static Configuration config;
    private static ClassicRunner runner;
    
    // Browserstack authentication variables
    private static String browserstackUrl;

    // Test-specific objects
    private WebDriver driver;
    private Eyes eyes;
    private String methodName = "undef";
    private String testName = "undef";
    private String testSuite = "undef";

    @BeforeClass
    public void beforeClass() {
        System.out.printf("Before: Class for %s\n", this.getClass().getSimpleName());
        
        // Read the Applitools API key from an environment variable.
        // To find your Applitools API key:
        // https://applitools.com/tutorials/getting-started/setting-up-your-environment.html
        applitoolsApiKey = System.getenv("APPLITOOLS_API_KEY");
        
        String browserstackUsername = System.getenv("BROWSERSTACK_USERNAME");
        String browserstackAccessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (! (browserstackUsername.isBlank() || browserstackAccessKey.isBlank()) ) { 
            browserstackUrl = String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub", browserstackUsername, browserstackAccessKey);
        } else {
            browserstackUrl = "https://hub-cloud.browserstack.com/wd/hub";
        }

        // Read the headless mode setting from an environment variable.
        // Use headless mode for Continuous Integration (CI) execution.
        // Use headed mode for local development.
        //headless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "true"));

        System.out.printf("Before: Class for %s - APPLITOOLS creating runner\n", this.getClass().getSimpleName());
        // Create the runner
        runner = new ClassicRunner();

        System.out.printf("Before: Class for %s - APPLITOOLS creating batch info\n", this.getClass().getSimpleName());
       // Create a new batch for tests.
        // A batch is the collection of visual checkpoints for a test suite.
        // Batches are displayed in the dashboard, so use meaningful names.
        batch = new BatchInfo(batchName);


        System.out.printf("Before: Class for %s - APPLITOOLS creating config\n", this.getClass().getSimpleName());
        // Create a configuration for Applitools Eyes.
        config = new Configuration();

        // Set the Applitools API key so test results are uploaded to your account.
        // If you don't explicitly set the API key with this call,
        // then the SDK will automatically read the `APPLITOOLS_API_KEY` environment variable to fetch it.
        System.out.printf("Before: Class for %s - APPLITOOLS setting api key\n", this.getClass().getSimpleName());
        config.setApiKey(applitoolsApiKey);
        System.out.printf("Before: Class for %s - APPLITOOLS api key set '%s'\n", this.getClass().getSimpleName(), applitoolsApiKey);

        // Set the batch for the config.
        config.setBatch(batch);

        // Add 3 desktop browsers with different viewports for cross-browser testing in the Ultrafast Grid.
        // Other browsers are also available, like Edge and IE.
        /*config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(1600, 1200, BrowserType.FIREFOX);
        config.addBrowser(1024, 768, BrowserType.SAFARI);
        config.addBrowser(1024, 768, BrowserType.SAFARI_TWO_VERSIONS_BACK);*/

        // Add 2 mobile emulation devices with different orientations for cross-browser testing in the Ultrafast Grid.
        // Other mobile devices are available, including iOS.
        /*config.addDeviceEmulation(DeviceName.Pixel_5, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.OnePlus_7T, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.Kindle_Fire_HDX, ScreenOrientation.PORTRAIT);*/

    }

    @BeforeMethod
    public void beforeMethod(Method testMethod, Object[] params) throws MalformedURLException {
        System.out.println("Before: Method for " + testMethod.getName());
        
        methodName = String.format("%s#%s", testMethod.getName(), params[2]);
        
        // This method sets up each test with its own ChromeDriver and Applitools Eyes objects.

        DesiredCapabilities dc = new DesiredCapabilities();
        
        // Set capabilities if you are NOT using the Browserstack JavaAgent to pick them up from
        // a browserstack settings XML file (like browserstack.xml)
        //
        // -javaagent:/Users/sebastian/.m2/repository/com/browserstack/browserstack-java-sdk/1.0.6/browserstack-java-sdk-1.0.6.jar

        // dc.setCapability("os", "Windows");
        // dc.setCapability("os_version", "7");
        // dc.setCapability("os_version", "10");
        // dc.setCapability("browser", "IE");
        // dc.setCapability("browser_version", "8");
        // dc.setCapability("browser_version", "11");

        // dc.setCapability("os", "OS X");
        // dc.setCapability("os_version", "Monterey");
        // dc.setCapability("browser", "Chrome");
        // dc.setCapability("browser_version", "104");

        dc.setCapability("name", methodName);
        dc.setCapability("build", buildTag);
        dc.setCapability("project", appName);

        driver = new RemoteWebDriver(new URL(browserstackUrl), dc);
        // Open the browser with the ChromeDriver instance.
        // Even though this test will run visual checkpoints on different browsers in the Ultrafast Grid,
        // it still needs to run the test one time locally to capture snapshots.
        //driver = new ChromeDriver(new ChromeOptions().setHeadless(headless));

        // Set an implicit wait of 10 seconds.
        // For larger projects, use explicit waits for better control.
        // https://www.selenium.dev/documentation/webdriver/waits/
        // The following call works for Selenium 4:
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

        // If you are using Selenium 3, use the following call instead:
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        // Create the Applitools Eyes object connected to the VisualGridRunner and set its configuration.
        eyes = new Eyes(runner);
        eyes.setConfiguration(config);
        
        // TODO - temporarily disabled
        //eyes.setIsDisabled(true);
        
        System.out.printf("Before: Method for %s - EYES opening eyes\n", methodName);
        
        // Open Eyes to start visual testing.
        // It is a recommended practice to set all four inputs:
        eyes.open(
                driver,                                      // WebDriver object to "watch"
                appName,                                     // The name of the app under test
                methodName,                                    // The name of the test case
                new RectangleSize(1024, 768));              // The viewport size for the local browser
 
        System.out.printf("Before: Method for %s - EYES opened\n", methodName);
     }

    @Test( dataProvider = "loginPairs")
    public void loginPageTest(String pageURL, String pageName, String username, String password) {
        // This test covers login for the Applitools demo site, which is a dummy banking app.
        // The interactions use typical Selenium WebDriver calls,
        // but the verifications use one-line snapshot calls with Applitools Eyes.
        // If the page ever changes, then Applitools will detect the changes and highlight them in the dashboard.
        // Traditional assertions that scrape the page for text values are not needed here.

        System.out.printf("Running test %s getting %s\n", methodName, pageURL);
        
        // Load the login page.
        driver.get(pageURL);

        // Verify the full login page loaded correctly.
        eyes.check(Target.window().fully().withName("Login page"));

        // Perform login.
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("log-in")).click();

        // Verify the full main page loaded correctly.
        // This snapshot uses LAYOUT match level to avoid differences in closing time text.
        eyes.check(Target.window().fully().withName("Main page").layout());
    }
    
    @DataProvider
    public Object[][] loginPairs() {
        String pageURL = "https://demo.applitools.com";
        String pageName = "Login page";
        return new Object[][] {
            new Object[] {
                    pageURL, pageName, "applibot", "I<3VisualTests"
            /*}, new Object[] {
                    pageURL, pageName, "nullpasswd", ""
            }, new Object[] {
                    pageURL, pageName, "randomuser", "123456"*/
            }
        };
    }
    
    @Test( dataProvider = "keyPresses" )
    public void keyPressPageTest(String pageURL, String pageName, String pressKey, String expected) {
        System.out.printf("Running test %s getting %s\n", methodName, pageURL);
        
        // Load the login page.
        driver.get(pageURL);

        // Verify the keyPress page loaded correctly.
        eyes.check(Target.window().fully().withName(pageName));

        // Press the key and let it go!
        new Actions(driver).sendKeys(pressKey).perform();
        
        String resultText = driver.findElement(By.id("result")).getText();
        String result = resultText.replaceFirst("You entered: ", "");
        System.out.printf("Result on page was '%s' expected '%s'\n", result, expected);

        // Verify the full main page loaded correctly.
        // This snapshot uses LAYOUT match level to avoid differences in closing time text.
        eyes.check(Target.window().fully().withName("KeyPress Result").layout());
        
        Assert.assertEquals(result, expected, String.format("Key Press result '%s' was not expected '%s'", result, expected));
    }
    
    @DataProvider
    public Object[][] keyPresses() {
        String pageURL = "https://the-internet.herokuapp.com/key_presses";
        String pageName = "Key Presses";
        return new Object[][] {
            new Object[] {
                    pageURL, pageName, "a", "A"
            /*}, new Object[] {
                    pageURL, pageName, "Z", "Z"*/
            }
        };
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("After:  Method for " + methodName);

        // Quit the WebDriver instance.
        driver.quit();

        // Close Eyes to tell the server it should display the results.
        eyes.closeAsync();

        // Warning: `eyes.closeAsync()` will NOT wait for visual checkpoints to complete.
        // You will need to check the Applitools dashboard for visual results per checkpoint.
        // Note that "unresolved" and "failed" visual checkpoints will not cause the TestNG test to fail.

        // If you want the TestNG test to wait synchronously for all checkpoints to complete, then use `eyes.close()`.
        // If any checkpoints are unresolved or failed, then `eyes.close()` will make the TestNG test fail.
    }

     @AfterClass
    public void afterClass() {
        System.out.printf("After:  Class for %s\n", this.getClass().getSimpleName());

        new BatchClose().setBatchId(Arrays.asList(batch.getId())).close();

        // Close the batch and report visual differences to the console.
        // Note that it forces TestNG to wait synchronously for all visual checkpoints to complete.
        TestResultsSummary allTestResults = runner.getAllTestResults();
        System.out.println(allTestResults);
    }

    @BeforeTest
    public void beforeTest(ITestContext ctx) {
        testName = ctx.getName();
        System.out.println("Before: Test for " + testName);
    }

    @AfterTest
    public void afterTest(ITestContext ctx) {
        System.out.println("After:  Test for " + testName);
    }

    @BeforeSuite
    public void beforeSuite(ITestContext ctx) {
        testSuite = ctx.getSuite().getName();
        System.out.println("Before: Suite for " + testSuite);
        /*
        // Read the Applitools API key from an environment variable.
        // To find your Applitools API key:
        // https://applitools.com/tutorials/getting-started/setting-up-your-environment.html
        applitoolsApiKey = System.getenv("APPLITOOLS_API_KEY");

        // Read the headless mode setting from an environment variable.
        // Use headless mode for Continuous Integration (CI) execution.
        // Use headed mode for local development.
        headless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "true"));

        // Create the runner for the Ultrafast Grid.
        // Concurrency refers to the number of visual checkpoints Applitools will perform in parallel.
        // Warning: If you have a free account, then concurrency will be limited to 1.
        runner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));

        // Create a new batch for tests.
        // A batch is the collection of visual checkpoints for a test suite.
        // Batches are displayed in the dashboard, so use meaningful names.
        batch = new BatchInfo("Example: Selenium Java TestNG with the Ultrafast Grid");
*/
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("After:  Suite for " + testSuite);
/*
        // Close the batch and report visual differences to the console.
        // Note that it forces TestNG to wait synchronously for all visual checkpoints to complete.
        TestResultsSummary allTestResults = runner.getAllTestResults();
        System.out.println(allTestResults);
        */
    }

}
