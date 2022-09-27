package com.bernheims.applitools.testng.pageapi;

import org.testng.annotations.Test;

import java.io.IOException;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.DataProvider;

public class KeypressTest extends EyesChromeDrivenTest {
    
    private static final String pageURL = "https://the-internet.herokuapp.com/key_presses";

    @Test( priority = 20, dataProvider = "keyPresses" )
    public void keyPressPageTest(String testKey, String pressKey, String expected) throws WebDriverException, IOException {
        System.out.printf("Running test %s getting %s\n", methodName, pageURL);
        
        // Load the page.
        driver.get(pageURL);
        
        new KeypressPage(driver, eyes)
            .checkPageLoaded()
            .pressKey(pressKey)
            .changePageBackground("PALEGREEN")
            //.changeResultBackground("LIGHTBLUE")
            //.changeResultColor("BLACK")
            //.changeResultColor("DARKRED")
            //.changeResultColor("BLUE")
            .checkResult(expected);
            //.saveScreenshot();
    }
    
    @DataProvider
    public Object[][] keyPresses() {
        return new Object[][] {
            new Object[] {
                    "a", "a", "A"
            }, new Object[] {
                    "y", "y", "Y"
            }, new Object[] {
                    "Z", "Z", "Z"
            }
        };
    }

}
