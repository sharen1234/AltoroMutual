package org.altoromutual;



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
import org.openqa.selenium.WebElement;

public class Baseclass {
	
	public void textInput(WebElement Element,String value) {
		Element.sendKeys(value);
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
