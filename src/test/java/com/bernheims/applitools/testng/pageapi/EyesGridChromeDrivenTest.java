package com.bernheims.applitools.testng.pageapi;

import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;

public class EyesGridChromeDrivenTest extends EyesChromeDrivenTest {

    protected void createRunner() {
        String thisMethod = thisMethod();
        System.out.printf("BeforeSuite : %s - creating runner\n", thisMethod);
        // Create the runner for the Ultrafast Grid.
        // Concurrency refers to the number of visual checkpoints Applitools will perform in parallel.
        // Warning: If you have a free account, then concurrency will be limited to 1.
        System.out.printf("BeforeClass : %s - creating runner\n", thisMethod);
        runner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));
    }
    
    protected Configuration createConfiguration() {
        String thisMethod = thisMethod();
        System.out.printf("BeforeSuite : %s for %s\n", thisMethod, suiteName);
        
        config = super.createConfiguration();

        System.out.printf("BeforeSuite : %s adding browser and device configurations\n", thisMethod, suiteName);

        // Add different desktop browsers with different viewports for cross-browser testing in the Ultrafast Grid.
        /*if (baselineEnvironment.isPresent()) {
            System.out.printf("BeforeSuite : %s using baseline environment %s\n", thisMethod, baselineEnvironment.get());
            config.addBrowser(1024, 768, BrowserType.CHROME, baselineEnvironment.get()); // This is the "desktop-browser" baseline
            //config.addBrowser(1920, 1080, BrowserType.CHROME, baselineEnvironment.get());
            //config.addBrowser(3840, 2160, BrowserType.FIREFOX, baselineEnvironment.get());
            //config.addBrowser(3008, 1692, BrowserType.SAFARI, baselineEnvironment.get());
            //config.addBrowser(2560, 1440, BrowserType.SAFARI_TWO_VERSIONS_BACK, baselineEnvironment.get());
            //config.addBrowser(1504, 846, BrowserType.EDGE_LEGACY, baselineEnvironment.get());
            //config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM, baselineEnvironment.get());
            //config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM_TWO_VERSION_BACK, baselineEnvironment.get());
            //config.addBrowser(1024, 768, BrowserType.IE_11, baselineEnvironment.get());
            //config.addBrowser(1024, 768, BrowserType.IE_10, baselineEnvironment.get());
        } else {*/
            config.addBrowser(1024, 768, BrowserType.CHROME); // This is the "desktop-browser" baseline
            //config.addBrowser(1920, 1080, BrowserType.CHROME);
            //config.addBrowser(3840, 2160, BrowserType.FIREFOX);
            //config.addBrowser(3008, 1692, BrowserType.SAFARI);
            //config.addBrowser(2560, 1440, BrowserType.SAFARI_TWO_VERSIONS_BACK);
            //config.addBrowser(1504, 846, BrowserType.EDGE_LEGACY);
            //config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM);
            //config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM_TWO_VERSION_BACK);
            //config.addBrowser(1024, 768, BrowserType.IE_11);
            //config.addBrowser(1024, 768, BrowserType.IE_10);
        //}

        // Add mobile emulation devices with different orientations for cross-browser testing in the Ultrafast Grid.
        /*if (mobileBaselineEnvironment.isPresent()) {
            System.out.printf("BeforeSuite : %s using mobile baseline environment %s\n", thisMethod, mobileBaselineEnvironment.get());
            config.addDeviceEmulation(DeviceName.Pixel_5, ScreenOrientation.PORTRAIT, mobileBaselineEnvironment.get()); // I think this is the "mobile-browser" baseline
            //config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.PORTRAIT, mobileBaselineEnvironment.get());
            //config.addDeviceEmulation(DeviceName.iPhone_11_Pro, ScreenOrientation.PORTRAIT, mobileBaselineEnvironment.get());
            //config.addDeviceEmulation(DeviceName.Pixel_4_XL, ScreenOrientation.LANDSCAPE, mobileBaselineEnvironment.get());
            //config.addDeviceEmulation(DeviceName.OnePlus_7T, ScreenOrientation.LANDSCAPE, mobileBaselineEnvironment.get());
            //config.addDeviceEmulation(DeviceName.Kindle_Fire_HDX, ScreenOrientation.PORTRAIT, mobileBaselineEnvironment.get());
        } else {*/
            config.addDeviceEmulation(DeviceName.Pixel_5, ScreenOrientation.PORTRAIT); // I think this is the "mobile-browser" baseline
            //config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.PORTRAIT);
            //config.addDeviceEmulation(DeviceName.iPhone_11_Pro, ScreenOrientation.PORTRAIT);
            //config.addDeviceEmulation(DeviceName.Pixel_4_XL, ScreenOrientation.LANDSCAPE);
            //config.addDeviceEmulation(DeviceName.OnePlus_7T, ScreenOrientation.LANDSCAPE);
            //config.addDeviceEmulation(DeviceName.Kindle_Fire_HDX, ScreenOrientation.PORTRAIT);
        //}

        return config;
    }

}