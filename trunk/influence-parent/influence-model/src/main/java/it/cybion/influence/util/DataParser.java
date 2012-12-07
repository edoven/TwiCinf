package it.cybion.influence.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;


public class DataParser {
	
	
	private static final String datasetTwitterDataFormat = "MMM dd, yyyy hh:mm:ss a";
	private static final String originalTwitterDataFormat = "EEE MMM dd hh:mm:ss ZZZZZ yyyy";

	public static DateTime parseDatasetTwitterData(String data) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datasetTwitterDataFormat, Locale.US);
		Date date = null;
		try {
			date = simpleDateFormat.parse(data);
		} catch (ParseException e) {
		//TODO : what is the best way to handle this exception?
        // i would not catch it, letting upper layers to deal with it and decide if applying a default value or not
			return new DateTime(); 
		}		
		return new DateTime(date);		
	}
	
	
	public static DateTime parseOriginalTwitterData(String data) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(originalTwitterDataFormat, Locale.US);
		Date date = null;
		try {
			date = simpleDateFormat.parse(data);
		} catch (ParseException e) {
		//TODO : what is the best way to handle this exception?
        // i would not catch it, letting upper layers to deal with it and decide if applying a default value or not
			return new DateTime(); 
		}		
		return new DateTime(date);		
	}
}
