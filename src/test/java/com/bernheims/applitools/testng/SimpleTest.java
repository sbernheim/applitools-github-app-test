package com.bernheims.applitools.testng;

import java.lang.ArithmeticException;
import java.lang.NumberFormatException;

import org.testng.Assert;
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

public class SimpleTest {

    //@Test(dependsOnGroups = { "First" }, dataProvider = "dp")
    @Test(dataProvider = "dp")
    public void testNothing(Integer n, String s) {
        System.out.printf("Running TestNG test SimpleTest with %d '%s'!\n", n, s);
    }
    
    @Test(groups = "First", expectedExceptions = {ArithmeticException.class, NumberFormatException.class}, dataProvider = "divs")
    public void thisTestThrowsAnException(int dividend, String divisorStr, int expected) {
        System.out.println("This test should throw an exception!");
        int divisor = Integer.parseInt(divisorStr);
        int result = dividend / divisor;
        System.out.printf("The result of (%d / %d) was %d\n", dividend, divisor, result);
        Assert.assertEquals(result, expected, String.format("The result was not %d!", expected));
    }

    @BeforeMethod
    public void beforeMethod() {

    }

    @AfterMethod
    public void afterMethod() {
    }

    @DataProvider
    public Object[][] dp() {
        return new Object[][] {
                new Object[] {
                        1, "a"
                }, new Object[] {
                        2, "b"
                },
        };
    }
    
    @DataProvider
    public Object[][] divs() {
        return new Object[][] {
            new Object[] {
                    6, "0", 3
            }, new Object[] {
                    6, "three", 2
            }, new Object[] {
                    6, "2", 3
            }
        };
    }

    @BeforeClass
    public void beforeClass() {
    }

    @AfterClass
    public void afterClass() {
    }

    @BeforeTest
    public void beforeTest() {
    }

    @AfterTest
    public void afterTest() {
    }

    @BeforeSuite
    public void beforeSuite() {
    }

    @AfterSuite
    public void afterSuite() {
    }

}
