package it.cybion.influence;

import it.cybion.influence.model.Tweet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


import static org.testng.Assert.assertTrue;

/**
 */
public class JSONDeserializerTestCase
{

	String json01;
	String json02;
	String json03;
	private static final Logger logger = Logger.getLogger(JSONDeserializerTestCase.class);
	Gson gson;
	
	
	@BeforeClass
	public void setup() throws IOException
	{
		gson = new GsonBuilder().create();
		
		json01 = readFile("src/test/resources/tweet01.json");
		json02 = readFile("src/test/resources/tweet01.json");
		json03 = readFile("src/test/resources/tweet01.json");
	}

	@AfterClass
	public void shutdown()
	{

	}


	@Test
	public void shouldDeserializeJsonToObject1()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json01, Tweet.class);
		
		Assert.assertEquals(tweet.getId(), "259243620450848768");

	}
	
	@Test
	public void shouldDeserializeJsonToObject2()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json02, Tweet.class);
		
		

	}
	
	@Test
	public void shouldDeserializeJsonToObject3()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		

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
