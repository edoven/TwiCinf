package it.cybion.influence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.json.DataObjectFactory;

import static org.testng.Assert.assertTrue;

/**
 */
public class JSONDeserializerTestCase
{

	String json01;
	String json02;
	String json03;
	Logger logger;
	
	@BeforeClass
	public void setup() throws IOException
	{
		json01 = readFile("src/test/resources/tweet01.json");
		json02 = readFile("src/test/resources/tweet01.json");
		json03 = readFile("src/test/resources/tweet01.json");
	}

	@AfterClass
	public void shutdown()
	{

	}


	@Test
	public void shouldDeserializeJsonToObject() throws TwitterException
	{
		

	}

	private String readFile(String file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null)
		{
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		return stringBuilder.toString();
	}
}
