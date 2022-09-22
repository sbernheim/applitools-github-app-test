package com.bernheims.applitools.testng.pageapi;

import java.lang.reflect.Method;
import java.util.Optional;

import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.applitools.eyes.AccessibilityGuidelinesVersion;
import com.applitools.eyes.AccessibilityLevel;
import com.applitools.eyes.AccessibilitySettings;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;

public class EyesChromeDrivenTest extends ChromeDrivenTest {

    // Test control inputs to read once and share for all tests
    //protected static String applitoolsApiKey;
    //protected static Optional<String> branchName;
    //protected static Optional<String> baselineEnvironment;
    //protected static Optional<String> mobileBaselineEnvironment;
    protected static boolean isDisabled;
    protected static boolean isAccessibilityValidationEnabled;
    protected static AccessibilityGuidelinesVersion accessibilityGuidelinesVersion;
    protected static AccessibilityLevel accessibilityLevel;

    // Applitools objects to share for all tests
    //protected static BatchInfo batch;
    protected static Configuration config;
    protected static EyesRunner runner;
    
    // An eyes object for each test
    protected Eyes eyes;
    
    // Default batch, branch and app names for representation in the Eyes dashboard
    protected static final String defaultAppName = "PageObject API Test Examples";
    protected static final String defaultBatchName = "PageObject API Selenium Chromedriver with Eyes Test";
    protected static String appName;
    protected static String batchName;
    //protected static Optional<String> batchId;
    
    protected Optional<String> suiteBatchName() {
        return Optional.ofNullable(suiteName.startsWith("Default suite") ? null : suiteName);
    }
    
    protected void readSettings() {
        String thisMethod = thisMethod();

        // Read the Applitools API key from a System property or environment variable.
        // To find your Applitools API key:
        // https://applitools.com/tutorials/getting-started/setting-up-your-environment.html
        String applitoolsApiKey = getSetting("applitools.api.key", "APPLITOOLS_API_KEY")
                .orElseThrow(() -> new RuntimeException("The APPLITOOLS_API_KEY environment variable is not set!"));
        System.out.printf("BeforeSuite : %s - found APPLITOOLS_API_KEY '%s'\n", thisMethod, applitoolsApiKey);

        isDisabled = getBooleanSetting("applitools.api.disabled", "APPLITOOLS_DISABLED");
        System.out.printf("BeforeSuite : %s - found isDisabled setting = %b\n", thisMethod, isDisabled);

        isAccessibilityValidationEnabled = getBooleanSetting("applitools.api.accessibility.validation.enabled", "APPLITOOLS_ACCESSIBILITY_VALIDATION_ENABLED");
        System.out.printf("BeforeSuite : %s - found accessibility validation enabled setting = %b\n", thisMethod, isAccessibilityValidationEnabled);
        
        accessibilityLevel = getTransformedSetting(
                    "applitools.api.accessibility.level", 
                    "APPLITOOLS_ACCESSIBILITY_LEVEL",
                    (l) -> AccessibilityLevel.valueOf(l),
                    AccessibilityLevel.AA
                );
        
        accessibilityGuidelinesVersion = getTransformedSetting(
                    "applitools.api.accessibility.guidelines.version", 
                    "APPLITOOLS_ACCESSIBILITY_GUIDELINES_VERSION",
                    (g) -> AccessibilityGuidelinesVersion.valueOf(g),
                    AccessibilityGuidelinesVersion.WCAG_2_0
                );
        
        /*branchName = getSetting("applitools.api.branch", "APPLITOOLS_BRANCH_NAME");
        if (branchName.isPresent()) System.out.printf("BeforeSuite : %s - found branch name setting '%s'\n", thisMethod, branchName);
        
        baselineEnvironment = getSetting("applitools.api.baseline.environment", "APPLITOOLS_BASELINE_ENVIRONMENT");
        if (baselineEnvironment.isPresent()) System.out.printf("BeforeSuite : %s - found baseline environment setting '%s'\n", thisMethod, baselineEnvironment);

        mobileBaselineEnvironment = getSetting("applitools.api.baseline.environment.mobile", "APPLITOOLS_MOBILE_BASELINE_ENVIRONMENT");
        if (mobileBaselineEnvironment.isPresent()) System.out.printf("BeforeSuite : %s - found mobile baseline environment setting '%s'\n", thisMethod, mobileBaselineEnvironment);
	*/

        // Use the batchId set by the Applitools Jenkins plug-in if defined,
        //batchId = getSetting("applitools.api.batch.id", "APPLITOOLS_BATCH_ID");

        // Use the batchName set by the Applitools Jenkins plug-in if defined,
        // or the TestNG suite name if is it not the default "Default suite" name,
        // or the defaultBatchName static final constant defined above.
        batchName = getSetting("applitools.api.batch.name", "APPLITOOLS_BATCH_NAME").or(this::suiteBatchName).orElse(defaultBatchName);
    }
    
    protected void createRunner() {
        String thisMethod = thisMethod();
        System.out.printf("BeforeSuite : %s - creating ClassicRunner\n", thisMethod);
        runner = new ClassicRunner();
    }

    protected EyesRunner runner() {
        if (runner == null) createRunner();
        return runner;
    }
    
    protected BatchInfo createBatchInfo() {
        String thisMethod = thisMethod();
        System.out.printf("BeforeSuite : %s - creating batch info with name '%s'\n", thisMethod, batchName);

        // Create a new batch for tests.
        // A batch is the collection of visual checkpoints for a test suite.
        // Batches are displayed in the dashboard, so use meaningful names.
        BatchInfo batch = new BatchInfo(batchName);

        // Use the batch ID set by the Applitools Jenkins plug-in if it is set
        /*if (settingIsNotBlank(batchId)) {
            batch.setId(batchId.get());
            System.out.printf("BeforeSuite : %s - setting batch id '%s'\n", thisMethod, batch.getId());
        }*/

        return batch;
    }
    
    protected Configuration createConfiguration() {
        String thisMethod = thisMethod();
        System.out.printf("BeforeSuite : %s - creating eyes configuration for batch '%s'\n", thisMethod, batchName);

        config = new Configuration();

        /*if (settingIsNotBlank(branchName)) {
            config.setBranchName(branchName.get());
            System.out.printf("BeforeSuite : %s - setting branch name '%s'\n", thisMethod, branchName);
        }*/
        
        if (isAccessibilityValidationEnabled) {
            config.setAccessibilityValidation(new AccessibilitySettings(accessibilityLevel, accessibilityGuidelinesVersion));
            System.out.printf("BeforeSuite : %s - setting Accessibility validation [ Level:%s GuidelinesVersion:%s ]\n", thisMethod, accessibilityLevel, accessibilityGuidelinesVersion);
        }

        // Set the Applitools API key so test results are uploaded to your account.
        // If you don't explicitly set the API key with this call,
        // then the SDK will automatically read the `APPLITOOLS_API_KEY` environment variable to fetch it.
        /*System.out.printf("BeforeSuite : %s - setting API key\n", thisMethod);
        config.setApiKey(applitoolsApiKey);
        System.out.printf("BeforeSuite : %s - API key set '%s'\n", thisMethod, applitoolsApiKey);*/

        if (isDisabled) {
            config.setIsDisabled(true);
            System.out.printf("BeforeSuite : %s -\n\n\tNOTE: Applitools Eyes API calls are disabled!!!  <-------- %b\n\n", thisMethod, isDisabled);
        }

        // Set the batch for the config.
        config.setBatch(createBatchInfo());
        
        return config;
    }

    @BeforeSuite
    protected void setupEyesConfiguration(ITestContext ctx) {
        String thisMethod = thisMethod();
        System.out.printf("BeforeSuite : %s for %s\n", thisMethod, suiteName);

        // TODO - Is it possible to set custom attributes in the TestNG XML file at the Suite or Test level?
        ISuite suite = ctx.getSuite();
        //suite.setAttribute("randomAttribute", "Here is a random attribute value!");
        suite.getAttributeNames().stream().forEachOrdered(a -> System.out.printf("ATTRIBUTE '%s' VALUE '%s'\n", a, suite.getAttribute(a)));

        readSettings();
        
        // Create the Applitools Eyes test runner
        System.out.printf("BeforeSuite : %s - creating runner\n", thisMethod);
        createRunner();

        // Create a configuration for Applitools Eyes.
        System.out.printf("BeforeSuite : %s - creating config\n", thisMethod);
        createConfiguration();
    }
    
    @BeforeTest
    protected void setAppName(ITestContext ctx) {
        System.out.printf("BeforeTest  : %s for %s\n", thisMethod(), testName);
        ctx.getAttributeNames().stream().forEachOrdered(a -> System.out.printf("ATTRIBUTE '%s' VALUE '%s'\n", a, ctx.getAttribute(a)));
        if (!testName.startsWith("Default ")) {
            appName = testName;
        }
    }
    
    @BeforeClass
    protected void setClassInfo() {
        String thisMethod = thisMethod();
        System.out.printf("BeforeClass : %s for %s\n", thisMethod, this.getClass().getSimpleName());

    }

    @BeforeMethod
    public void openEyesForAppTestCase(Method testMethod, Object[] params) {
        String thisMethod = thisMethod();
        System.out.printf("BeforeMethod: %s for %s\n", thisMethod, methodName);

        // Create the Applitools Eyes object connected to the VisualGridRunner and set its configuration.
        eyes = new Eyes(runner());
        eyes.setConfiguration(config);
        
        /* if (baselineEnvironment.isPresent()) {
            eyes.setBaselineEnvName(baselineEnvironment.get());
            eyes.setMatchLevel(MatchLevel.LAYOUT2);
        } */
        
        /*if (isDisabled) {
            eyes.setIsDisabled(true);
            System.out.printf("NOTE: Applitools Eyes API calls are disabled!!!  <-------- %b\n", isDisabled);
        }*/
        
        // Open Eyes to start visual testing.
        // It is a recommended practice to set all four inputs:
        System.out.printf("BeforeMethod: %s - EYES opening app named '%s' and test case '%s'\n", thisMethod, appName, methodName);
        eyes.open(
                driver,                        // WebDriver object to "watch"
                appName,                       // The name of the app under test
                methodName,                    // The name of the test case
                new RectangleSize(1024, 768)); // The viewport size for the local browser
        
        System.out.printf("BeforeMethod: %s - EYES opened\n", thisMethod);
    }

    @AfterMethod
    public void closeEyesForAppTestCase() {
        String thisMethod = thisMethod();
        System.out.printf("AfterMethod : %s for %s\n", thisMethod, methodName);

        // Close Eyes to tell the server it should display the results.
        System.out.printf("AfterMethod : %s for %s - EYES closing async\n", thisMethod, methodName);
        eyes.closeAsync();
        System.out.printf("AfterMethod : %s for %s - EYES closed\n", thisMethod, methodName);

        // Warning: `eyes.closeAsync()` will NOT wait for visual checkpoints to complete.
        // You will need to check the Applitools dashboard for visual results per checkpoint.
        // Note that "unresolved" and "failed" visual checkpoints will not cause the TestNG test to fail.

        // If you want the TestNG test to wait synchronously for all checkpoints to complete, then use `eyes.close()`.
        // If any checkpoints are unresolved or failed, then `eyes.close()` will make the TestNG test fail.
    }

    @AfterClass
    protected void tearDownEyes() {
        System.out.printf("AfterClass  : %s for %s\n", thisMethod(), this.getClass().getSimpleName());
    }
    
    @AfterSuite
    protected void closeDriverAndPrintResults() {
        System.out.printf("AfterSuite  : %s for %s\n", thisMethod(), suiteName);

        // Close the batch and report visual differences to the console.
        // Note that it forces TestNG to wait synchronously for all visual checkpoints to complete.
        TestResultsSummary allTestResults = runner.getAllTestResults();
        System.out.println(allTestResults);
        
        // No need to explicitly close the Runner
        //runner.close(batchName);
    }
}
