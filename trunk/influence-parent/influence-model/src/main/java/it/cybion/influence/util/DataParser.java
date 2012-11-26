package it.cybion.influence.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;


public class DataParser {
	
	
	private static final String twitterDataFormat = "MMM dd, yyyy hh:mm:ss a";

	public static DateTime parseTwitterData(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat(twitterDataFormat, Locale.US);
		Date date = null;
		try {
			date = sdf.parse(data);
		} catch (ParseException e) {
            //TODO exceptions should be managed and dealt with
			e.printStackTrace();
		}		
		return new DateTime(date);		
	}
}
