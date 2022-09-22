package com.bernheims.applitools.testng.pageapi;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

public class BankLoginTest extends EyesChromeDrivenTest {
    
    String pageURL = "https://demo.applitools.com";

    @Test( priority = 10, dataProvider = "loginPairs" )
    public void loginPageTest(String username, String password) {
        // This test covers login for the Applitools demo site, which is a dummy banking app.
        // The interactions use typical Selenium WebDriver calls,
        // but the verifications use one-line snapshot calls with Applitools Eyes.
        // If the page ever changes, then Applitools will detect the changes and highlight them in the dashboard.
        // Traditional assertions that scrape the page for text values are not needed here.

        System.out.printf("Running test %s getting %s\n", methodName, pageURL);
        
        // Load the login page.
        driver.get(pageURL);

        String closingTime = new BankLoginPage(driver, eyes)
                .checkLoginForm()
                .enterUserame(username)
                .enterPassword(password)
                .submit()
                .checkLoginSuccess()
                .getClosingTime();
        
        System.out.printf("For test %s bank closing time was '%s'\n", methodName, closingTime);
    }
    
    @DataProvider
    public Object[][] loginPairs() {
        return new Object[][] {
            new Object[] {
                    "applibot", "I<3VisualTests"
            }, new Object[] {
                    "nullpasswd", ""
            }, new Object[] {
                    "randomuser", "123456"
            }
        };
    }
    
}
