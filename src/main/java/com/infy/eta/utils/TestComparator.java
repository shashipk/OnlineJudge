package com.infy.eta.utils;

/**
 * Created by Amit Joshi on 10/8/2015.
 */
public class TestComparator {

	public boolean matchTestResults(String programOutput, String originalOutput){
		if(programOutput.equalsIgnoreCase(originalOutput)) return true;
		else return false;
	}
}
