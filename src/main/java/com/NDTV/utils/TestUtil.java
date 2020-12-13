package com.NDTV.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtil {

	public static long PAGE_LOAD_TIMEOUT = 60;
	public static long IMPLICIT_WAIT_TIME = 10;
	public static String TEST_DATA_PATH = System.getProperty("user.dir")
			+ "\\src\\main\\java\\com\\NDTV\\testData\\TestData.xlsx";

	public static void waitForElementExplicitly(WebDriver driver, WebDriverWait wait, WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void jsWindowScroll(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,500)");
	}

	public void changeBrowserResolution(WebDriver driver) {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("document.body.style.zoom = '0.80'");
	}
	
	public static void jsClick(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor)driver;

		js.executeScript("arguments[0].click();", element);    
		
	}
	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	public static String addDaysToDate(String oldDate, int NoOfDays) 
	{
		
		System.out.println("Date before Addition: "+oldDate);
		//Specifying date format that matches the given date
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		try{
		   //Setting the date to the given date
		   c.setTime(sdf.parse(oldDate));
		}catch(ParseException e){
			e.printStackTrace();
		 }
		   
		//Number of Days to add
		c.add(Calendar.DAY_OF_MONTH, NoOfDays);  
		//Date after adding the days to the given date
		String newDate = sdf.format(c.getTime());  
		//Displaying the new Date after addition of Days
		System.out.println("Date after Addition: "+newDate);
		
		return newDate;
		
	}
	
	public static void writeIntoExcel(String strValue) throws IOException {
		File srcFile = new File(TEST_DATA_PATH);
		FileInputStream fis = new FileInputStream(srcFile);
		XSSFWorkbook xssfWb = new XSSFWorkbook(fis);
		XSSFSheet xssfSheet = xssfWb.getSheet("EmoloyeeDetails");
		xssfSheet.getRow(0).createCell(6).setCellValue("SuccessMessage");
		xssfSheet.getRow(1).createCell(6).setCellValue(strValue);

		FileOutputStream fos = new FileOutputStream(srcFile);
		xssfWb.write(fos);
		xssfWb.close();
	}
	
	public static double kelvinToCentigradeTo(String strTempInKelvin) {
		double tempInCelsius = (Double.valueOf(strTempInKelvin) - 273.15);
		
		return tempInCelsius;
	}
}
