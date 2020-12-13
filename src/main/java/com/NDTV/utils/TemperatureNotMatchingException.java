package com.NDTV.utils;

public class TemperatureNotMatchingException extends Exception{

	public TemperatureNotMatchingException() {
		System.out.println("Temperature difference is more than the specified range!!");
	}
}
