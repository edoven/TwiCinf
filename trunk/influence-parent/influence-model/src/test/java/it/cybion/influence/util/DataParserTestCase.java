package it.cybion.influence.util;

import static org.testng.AssertJUnit.assertEquals;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

public class DataParserTestCase {

	
	@Test
	public void parseTwitterDataTEST() {
		//twitterDataFormat = "MMM dd, yyyy hh:mm:ss a"
		String twitterData = "oct 12, 1965 01:23:11 AM";
		DateTime dateTime = DataParser.parseDatasetTwitterData(twitterData);
		assertEquals(dateTime.getMonthOfYear(), 10);
		assertEquals(dateTime.getDayOfMonth(), 12);
		assertEquals(dateTime.getYear(), 1965);
		assertEquals(dateTime.getHourOfDay(), 1);
		assertEquals(dateTime.getMinuteOfHour(), 23);
		assertEquals(dateTime.getSecondOfMinute(), 11);
		
	}
}
