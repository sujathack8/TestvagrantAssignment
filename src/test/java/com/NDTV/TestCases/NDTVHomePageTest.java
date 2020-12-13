package com.NDTV.TestCases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.NDTV.Pages.NDTVHomePage;
import com.NDTV.base.TestBase;
import com.NDTV.utils.Excel_Reader;
import com.NDTV.utils.TemperatureNotMatchingException;
import com.NDTV.utils.TestUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class NDTVHomePageTest extends TestBase{

	public ExtentHtmlReporter htmlReporter;
//	public ExtentReports extent;
	public ExtentTest test;
	
	Excel_Reader reader = new Excel_Reader(TestUtil.TEST_DATA_PATH);
	ExtentTest logger;
	
	ExtentReports extent = new ExtentReports();
	
	NDTVHomePage ndtvHomePage;
	
	Map<String,String> tempMap = new HashMap<String, String>();
	
	public NDTVHomePageTest() {
		super();
	}

	@BeforeTest
	public void setExtent() throws IOException {
		// specify location of the report
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH mm ss").format(new Date());
		ExtentHtmlReporter reporter=new ExtentHtmlReporter("./Reports/NDTVHomePageTest_"+timeStamp+".html");
		reporter.config().setTheme(Theme.DARK);
		extent.attachReporter(reporter);
		logger=extent.createTest("NDTVHomePageTest");
	}

	@AfterTest
	public void endReport() {
		extent.flush();
	}


	
	@BeforeSuite
	public void setUp() {
		initialization();
		ndtvHomePage = new NDTVHomePage();
	}
	
	@Test(priority=1)
	public void verifyNDTVWeatherPage() {
		ndtvHomePage.clickOnNoThanks();
		logger.log(Status.PASS, "Clicked on No Thanks");
		
		ndtvHomePage.clickOnNaviagtionMenu();
		logger.log(Status.PASS, "Clicked on Navigation menu");
		
		ndtvHomePage.clickOnWeather();
		logger.log(Status.PASS, "Clicked on weather");
		
		ndtvHomePage.uncheckAllCities();
		logger.log(Status.PASS, "Unchecked all cities");
		
		ndtvHomePage.inputSearchBox(reader.getCellData("Data", "City", 2));
		logger.log(Status.PASS, "Searched for the city");
		
		ndtvHomePage.selectcity();
		logger.log(Status.PASS, "Selected the searched city");
		
		tempMap = ndtvHomePage.fetchAllTemperatures();
		logger.log(Status.PASS, "Fecthed temperature from UI is "+tempMap.get("Temperature in centigrade"));
		
		boolean checkCity = ndtvHomePage.checkIfCityIsPresent(reader.getCellData("Data", "City", 2));
		Assert.assertEquals(checkCity, true);
		logger.log(Status.PASS, "Searched city name is present in the UI");
	}
	
	@Test(priority=2, dependsOnMethods="verifyNDTVWeatherPage")
	public void validateRestAPIInfoAganistUIValues() {
		String strCityID = reader.getCellData("Data", "CityID", 2);
		String[] strCityIDArr = strCityID.split("\\.");
		String strAppID = reader.getCellData("Data", "AppID", 2);
		RestAssured.baseURI="http://api.openweathermap.org";
		String strResponse = given().when().queryParam("id", strCityIDArr[0]).queryParam("appid", strAppID).get("/data/2.5/weather").then().extract().response().asString();
		System.out.println("strResponse :: "+strResponse);
		
		JsonPath jPath = new JsonPath(strResponse);
		System.out.println(jPath.get("main.temp"));
		String strKelvinTemp = jPath.get("main.temp").toString();
		
		double jsonTemp = TestUtil.kelvinToCentigradeTo(strKelvinTemp);
		
		if(Math.abs(Integer.valueOf(tempMap.get("Temperature in centigrade").substring(0, 2)) - jsonTemp ) > 2 ) {
			new TemperatureNotMatchingException();
			logger.log(Status.FAIL, "Temperature difference is more than the specified range!!");
		}
		else {
			logger.log(Status.PASS, "Temperature difference is within the specified range!!");
		}
	}
	
	@AfterSuite
	public void tearDown() throws IOException {
		  driver.quit();
	}
}
