package org.altoromutual;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Baseclass {

	WebDriver driver;
	Select s;
	public void chromebrowserConfig() {
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
	}
	
	public void openUrl(String url) {
		driver.get(url);
	}
	
	public void pageMaximize() {
		driver.manage().window().maximize();
	}
	
	public WebElement locateById(String id) {
		WebElement element = driver.findElement(By.id(id));
		return element;
	}
	
	public WebElement locateByName(String name) {
		WebElement element = driver.findElement(By.name(name));
		return element;
	}
	
	public void tap(WebElement Element) {
		Element.click();
	}
	
	public void textInput(WebElement Element,String value) {
		Element.sendKeys(value);
	}
	
	public String getInput(WebElement Element,String value) {
		String attribute = Element.getAttribute("value");
		return attribute;	
	}
	
	
	public String excelRead(String path,String sheet,int row,int cell) throws IOException {
		String value=null;
		File f=new File(path);
		FileInputStream fin=new FileInputStream (f);
		Workbook w=new XSSFWorkbook(fin);
		Sheet s = w.getSheet(sheet);
		Row r = s.getRow(row);
		Cell c = r.getCell(cell);
		System.out.println(c);
		int cellType = c.getCellType();
		if (cellType==1) {
			value = c.getStringCellValue();
			System.out.println(value);
		}
		else if (cellType==0) {
			if (DateUtil.isCellDateFormatted(c)) {
				Date dateCellValue = c.getDateCellValue();
				SimpleDateFormat sim=new SimpleDateFormat("dd/mm/yyyy");
				value = sim.format(dateCellValue);
				System.out.println(value);
			}
			else {
				double numericCellValue = c.getNumericCellValue();
				long l=(long)numericCellValue;
				value = String.valueOf(l);
				System.out.println(value);		
			}
		}
		return value;
		
	}
	
	
	
	
	
	
	
}
