package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;

import java.util.concurrent.TimeUnit;

public class LoginTests {
	WebDriver driver;
	LoginPage loginPage;
	ExtentReports extent;
	ExtentTest test;

	@BeforeClass
	public void setup() {
		// Initialize WebDriver
		// System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://practice.expandtesting.com/login");

		// Initialize Extent Reports
		extent = new ExtentReports();
		extent.setSystemInfo("Tester", "Madhu Ranjan");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("Browser", "Chrome");
	}

	@Test
	public void testValidLogin() {
		test = extent.createTest("Valid Login Test");
		try {
			loginPage = new LoginPage(driver);
			loginPage.enterUsername("practice");
			loginPage.enterPassword("SuperSecretPassword!");
			loginPage.clickLogin();

			Assert.assertTrue(driver.getTitle().contains("Dashboard"));
			test.log(Status.PASS, "Valid login test passed");
		} catch (Exception e) {
			test.log(Status.FAIL, "Valid login test failed: " + e.getMessage());
		}
	}

	@Test
	public void testInvalidLogin() {
		test = extent.createTest("Invalid Login Test");
		try {
			loginPage = new LoginPage(driver);
			loginPage.enterUsername("invalidUser");
			loginPage.enterPassword("invalidPass");
			loginPage.clickLogin();

			Assert.assertTrue(driver.getPageSource().contains("Invalid credentials"));
			test.log(Status.PASS, "Invalid login test passed");
		} catch (Exception e) {
			test.log(Status.FAIL, "Invalid login test failed: " + e.getMessage());
		}
	}

	@AfterClass
	public void teardown() {
		// Close browser
		driver.quit();

		// Write Extent Reports
		extent.flush();
	}
}
