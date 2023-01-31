package org.altoromutual;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Login extends Baseclass{

	WebDriver driver;
	@BeforeClass
	private void browserLaunch() throws IOException {
		//Chrome browser configuration
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		
		//To read the data from testdata excel file and Navigate to the application
		driver.get(excelRead("C:\\Users\\sarav\\eclipse-workspace\\ProjectAltoroMutual\\Excel\\Test Data.xlsx", "sheet1", 1, 2));
		driver.manage().window().maximize();
	}
	
//	@BeforeMethod
//	public void Start() {
//		Date d=new Date("dd/mm/YYYY");
//		System.out.println("Start time" + d);
//	}

	@Test(priority = 0)
	private void loginCredentials() throws IOException {
		driver.findElement(By.id("LoginLink")).click();
		//Get data from the excel and Try login with given incorrect credentials
		WebElement username = driver.findElement(By.id("uid"));
		textInput(username, excelRead("C:\\Users\\sarav\\eclipse-workspace\\ProjectAltoroMutual\\Excel\\Test Data.xlsx", "sheet1", 5, 2));
		WebElement passwrd = driver.findElement(By.id("passw"));
		textInput(passwrd, excelRead("C:\\Users\\sarav\\eclipse-workspace\\ProjectAltoroMutual\\Excel\\Test Data.xlsx", "sheet1", 5, 3));
		driver.findElement(By.name("btnSubmit")).click();
		
		//Get data from excel and Retry login with correct credentials
		WebElement txt_user = driver.findElement(By.id("uid"));
		textInput(txt_user, excelRead("C:\\Users\\sarav\\eclipse-workspace\\ProjectAltoroMutual\\Excel\\Test Data.xlsx", "sheet1", 4, 2));
		String user_name = txt_user.getAttribute("value");
		
		//Validate that you are signed in after trying with correct credentials
		Assert.assertEquals(user_name, excelRead("C:\\Users\\sarav\\eclipse-workspace\\ProjectAltoroMutual\\Excel\\Test Data.xlsx", "sheet1", 4, 2), "valid username");
		WebElement txt_passwrd = driver.findElement(By.id("passw"));
		textInput(txt_passwrd, excelRead("C:\\Users\\sarav\\eclipse-workspace\\ProjectAltoroMutual\\Excel\\Test Data.xlsx", "sheet1", 4, 3));
		String password = txt_passwrd.getAttribute("value");
		Assert.assertEquals(password, excelRead("C:\\Users\\sarav\\eclipse-workspace\\ProjectAltoroMutual\\Excel\\Test Data.xlsx", "sheet1", 4, 3), "valid password");
		driver.findElement(By.name("btnSubmit")).click();
	}

	@Test(priority = 1)
	private void accountSummary() {
		//Click on the view Account Summary option
		driver.findElement(By.id("MenuHyperLink1")).click();
		WebElement dropdown = driver.findElement(By.id("listAccounts"));
		
		//Select the 800001 Checking account option and click on the Go button
		Select s= new Select(dropdown);
		s.selectByIndex(1);
		driver.findElement(By.id("btnGetAccount")).click();
		WebElement dropdown1 = driver.findElement(By.id("listAccounts"));
		Select s1=new Select(dropdown1);
		
		//Get alloptions for Assert the Available Balance in the account of 800003 checking
		List<WebElement> options = s1.getOptions();
		for (int i = 0; i < options.size(); i++) {
			WebElement element = options.get(i);
			String text = element.getText();
			if(text.equals("800003")) {
				String availableText = driver.findElement(By.xpath("//td[contains(text(),'Available')]//following-sibling::td")).getText();
				Assert.assertEquals("$710681364474788000000", availableText);
			}
		}		
	}

	@Test(priority = 2)
	private void transaction() throws InterruptedException {
		//Click on the Transfer Funds option 
		driver.findElement(By.id("MenuHyperLink3")).click();
		WebElement dropdown = driver.findElement(By.id("toAccount"));
		Select s=new Select(dropdown);
		s.selectByIndex(1);
		
		//Enter the amount of 9876 & click on the Transfer Funds button 
		driver.findElement(By.id("transferAmount")).sendKeys("9876");
		driver.findElement(By.id("transfer")).click();
		Thread.sleep(3000);
		//Validate the Transaction Details message is shown below with correct amount
		WebElement txt_details = driver.findElement(By.id("_ctl0__ctl0_Content_Main_postResp"));
		String message = txt_details.getText();
		System.out.println(message);
		driver.findElement(By.id("MenuHyperLink2")).click();
		WebElement allFirstRowData = driver.findElement(By.xpath("//table[@id='_ctl0__ctl0_Content_Main_MyTransactions']//tr[2]"));
		Assert.assertTrue(allFirstRowData.getText().contains("800001"));
		WebElement allSecondRowData = driver.findElement(By.xpath("//table[@id='_ctl0__ctl0_Content_Main_MyTransactions']//tr[3]"));
		Assert.assertTrue(allSecondRowData.getText().contains("800000"));
	}

	@Test(priority = 3)
	private void formFilling() {
		driver.findElement(By.id("HyperLink3")).click();
		driver.findElement(By.xpath("//a[text()='online form']")).click();
		driver.findElement(By.name("email_addr")).sendKeys("saravanan@gmail.com");
		driver.findElement(By.name("subject")).sendKeys("Transactions History");
		driver.findElement(By.name("comments")).sendKeys(
				"I have transferred the amount of $9876 from 800000 to 800001 that was processed successfully");
		driver.findElement(By.name("submit")).click();
		WebElement text = driver.findElement(By.xpath("//div[@class='fl']"));
		String text2 = text.getText();
		System.out.println(text2);
		driver.findElement(By.id("LoginLink")).click();
	}
	
//	@AfterMethod
//	public void stop() {
//		Date d=new Date("dd/mm/YYYY");
//		System.out.println("End time" + d);
//	}
//
//	 @AfterClass
//	 private void closeBrowser() {
//	 driver.quit();
//	 }

}
