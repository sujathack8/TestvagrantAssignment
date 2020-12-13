package com.NDTV.Pages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.NDTV.base.TestBase;
import com.NDTV.utils.TestUtil;

public class NDTVHomePage extends TestBase{

	@FindBy(xpath="//a[text()='No Thanks']")
	WebElement noThanks;
	
	@FindBy(xpath="//a[@class='topnavmore']")
	WebElement NavigationMenu;
	
	@FindBy(xpath="//a[text()='WEATHER']")
	WebElement weatherOption;
	
	@FindBy(xpath="//input[@id='searchBox']")
	WebElement searchBox;
	
	@FindBy(xpath="//div[@class='message' and not(contains(@style, 'display: none;'))]//input")
	List<WebElement> suggestionCheckBox;
	
	//fetch title to check if selected city is present in the map
	@FindBy(xpath="//div[@class='outerContainer']")
	List<WebElement> allCities;
	
	@FindBy(xpath="//div[@class='message' and not(contains(@style, 'display: none;'))]//input")
	WebElement suggestedCityCheckBox;
	
	@FindBy(xpath="//div[@class='temperatureContainer']/span")
	List<WebElement> allTemperatures;
	
	@FindBy(xpath="//div[@class='cityText']")
	WebElement cityText;
	
	public NDTVHomePage() {
		PageFactory.initElements(driver, this);
	}
	
	public void clickOnNaviagtionMenu() {
		NavigationMenu.click();
	}
	
	public void clickOnWeather() {
		weatherOption.click();
	}
	
	/**
	 * Check whether city is checked or not, uncheck if checked
	 */
	public void uncheckAllCities() {
		for(int i=0; i< suggestionCheckBox.size(); i++) {
			try {
				if(suggestionCheckBox.get(i).getAttribute("checked").equalsIgnoreCase("true")) {
					TestUtil.jsClick(driver, suggestionCheckBox.get(i));
				}
			} catch (Exception e) {
				System.err.println("Don't have checked attribute");
			}
		}
	}
	
	public void clickOnNoThanks() {
		noThanks.click();
	}
	
	public void inputSearchBox(String strSearchCity) {
		searchBox.sendKeys(strSearchCity);
	}
	
	public void selectcity() {
		suggestedCityCheckBox.click();
	}
	
	public Map<String, String> fetchAllTemperatures() {
		int counter=0;
		System.out.println(allTemperatures.size());
		Map<String, String> allTempMap = new LinkedHashMap<String, String>();
		allTempMap.put("Temperature in centigrade", "");
		allTempMap.put("Temperature in Farenhite", "");
		for(Map.Entry<String, String> entry : allTempMap.entrySet()) {
			allTempMap.replace(entry.getKey(), allTemperatures.get(counter).getText());
			counter++;
		}
		return allTempMap;
	}
	
	public boolean checkIfCityIsPresent(String strCity) {
		System.out.println("cityText.getText() :: "+cityText.getText());
		
		if(cityText.getText().equals(strCity)) {
			return true;
		}
		else {
			return false;
		}
	}
}
