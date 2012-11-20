package it.cybion.influence.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataParser {
	
	
	private static final String twitterDataFormat = "MMM dd, yyyy hh:mm:ss a";
	
	
	/*
	public static long parseTwitterData(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat(dataFormat, Locale.US);
		Date date = null;
		try {
			date = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		if (date==null)
			return -1;
		else
			return date.getTime();		
	}
	*/
	
	public static Date parseTwitterData(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat(twitterDataFormat, Locale.US);
		Date date = null;
		try {
			date = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return date;		
	}
}
