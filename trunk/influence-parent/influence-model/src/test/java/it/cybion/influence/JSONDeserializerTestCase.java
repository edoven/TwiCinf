package it.cybion.influence;

import it.cybion.influence.model.HashtagEntity;
import it.cybion.influence.model.Tweet;
import it.cybion.influence.model.UrlEntity;
import it.cybion.influence.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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

	/*
	 * createdAt
	 */
	@Test
	public void shouldDeserializeJsonToObject1() throws ParseException
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json01, Tweet.class);

	//	Assert.assertEquals(tweet.getCreatedAt(), ###);
		

	}


	
	/*
	 * id
	 */
	@Test
	public void shouldDeserializeJsonToObject2()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json01, Tweet.class);
		
		//logger.info(tweet.getId());
		
		Assert.assertEquals(tweet.getId(), "259243620450848768");

	}
/*	
	
	
	
	/*
	 * text
	 */
	@Test
	public void shouldDeserializeJsonToObject3()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		Assert.assertEquals(tweet.getText(), "#basket, liomatic perugia affronta il casalpusterlengo\nhttp://t.co/YzzMkIao");

	}
	
	
	/*
	 * source
	 */
	@Test
	public void shouldDeserializeJsonToObject4()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		Assert.assertEquals(tweet.getSource(), "web");

	}
	
	
	/*
	 * isTruncated
	 */
	@Test
	public void shouldDeserializeJsonToObject5()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		Assert.assertEquals(tweet.isTruncated(), false);

	}
	
	
	/*
	 * inReplyToStatusId
	 */
	@Test
	public void shouldDeserializeJsonToObject6()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		Assert.assertEquals(tweet.getInReplyToStatusId(), "-1");

	}
	
	
	/*
	 * inReplyToUserId
	 */
	@Test
	public void shouldDeserializeJsonToObject7()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		Assert.assertEquals(tweet.getInReplyToUserId(), "-1");

	}
	
	
	/*
	 * isFavorited
	 */
	@Test
	public void shouldDeserializeJsonToObject8()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		Assert.assertEquals(tweet.isFavorited(), false);

	}
	
	

	/*
	 * retweetCount
	 */
	@Test
	public void shouldDeserializeJsonToObject9()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		Assert.assertEquals(tweet.getRetweetCount(), 0);

	}
	
	
	
	/*
	 * wasRetweetedByMe
	 */
	@Test
	public void shouldDeserializeJsonToObject10()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		
		Assert.assertEquals(tweet.wasRetweetedByMe(), false);

	}
	
	
	/*
	 * urlEntities.get(0)
	 */
	@Test
	public void shouldDeserializeJsonToObject11()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		UrlEntity urlEntity = tweet.getUrlEntities().get(0);
		
		Assert.assertEquals(urlEntity.getStart(), 55);
		Assert.assertEquals(urlEntity.getEnd(), 75);
		Assert.assertEquals(urlEntity.getUrl().toString(), "http://t.co/YzzMkIao");
		Assert.assertEquals(urlEntity.getExpandedURL().toString(), "http://www.perugiatoday.it/sport/liomatic-perugia-casalpusterlengo-21-ottobre-2012.html");
		Assert.assertEquals(urlEntity.getDisplayURL(), "perugiatoday.it/sport/liomatic…");
	}
	
	
	/*
	 * hashtagEntities.get(0) 0,7,basket
	 */
	@Test
	public void shouldDeserializeJsonToObject12()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		HashtagEntity hashtagEntity = tweet.getHashtagEntities().get(0);
		
		Assert.assertEquals(hashtagEntity.getStart(), 0);
		Assert.assertEquals(hashtagEntity.getEnd(), 7);
		Assert.assertEquals(hashtagEntity.getText(), "basket");
	}
	
		/*
		"id":425699035,
		"name":"PerugiaToday",
		"screenName":"PerugiaToday",
		"location":"Perugia",
		"description":"",
		"isContributorsEnabled":false,
		"url":"http://www.perugiatoday.it/",
		"isProtected":false,
		"followersCount":123,
		"friendsCount":93,
	TO_INSERT	"createdAt":"Dec 1, 2011 10:49:25 AM",
		"favouritesCount":0,
		"lang":"it",
		"statusesCount":996,
		"listedCount":3,
		*/
	/*
	 * user
	 */
	@Test
	public void shouldDeserializeJsonToObject13()
	{
		
		Gson gson = new GsonBuilder().create();
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		User user = tweet.getUser();
		

		Assert.assertEquals(user.getId(), 425699035);
		Assert.assertEquals(user.getName(), "PerugiaToday");
		Assert.assertEquals(user.getScreenName(), "PerugiaToday");
		Assert.assertEquals(user.getLocation(), "Perugia");
		Assert.assertEquals(user.getDescription(), "");
		Assert.assertEquals(user.isContributorsEnabled(), false);
		Assert.assertEquals(user.getUrl().toString(), "http://www.perugiatoday.it/");
		Assert.assertEquals(user.isProtected(), false);
		Assert.assertEquals(user.getFollowersCount(), 123);
		Assert.assertEquals(user.getFriendsCount(), 93);
	//TO_INSERT	"createdAt":"Dec 1, 2011 10:49:25 AM",
		Assert.assertEquals(user.getFavouritesCount(), 0);
		Assert.assertEquals(user.getLang(), "it");
		Assert.assertEquals(user.getStatusesCount(), 996);
		Assert.assertEquals(user.getListedCount(), 3);
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
