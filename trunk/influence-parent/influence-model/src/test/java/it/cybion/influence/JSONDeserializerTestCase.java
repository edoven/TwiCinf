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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


/**
 */
public class JSONDeserializerTestCase
{

	String json01;
	String json02;
	String json03;
	Gson gson;

    private static final Logger logger = Logger.getLogger(JSONDeserializerTestCase.class);

	
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
        gson = null;
        json01 = null;
        json02 = null;
        json03 = null;
	}


	@Test
	public void shouldDeserializeJsonToObject1() throws ParseException
	{
		Tweet tweet = gson.fromJson(json01, Tweet.class);
		assertNotNull(tweet);
        assertEquals(tweet.getId(), "259243620450848770");
	}

	@Test
	public void shouldDeserializeJsonToObject3()
	{
		Tweet tweet = gson.fromJson(json03, Tweet.class);
		assertEquals(tweet.getText(), "#basket, liomatic perugia affronta il casalpusterlengo\nhttp://t.co/YzzMkIao");
        assertEquals(tweet.getSource(), "web");
        assertEquals(tweet.isTruncated(), false);
        assertEquals(tweet.getInReplyToStatusId(), "-1");
        assertEquals(tweet.getInReplyToUserId(), "-1");
        assertEquals(tweet.isFavorited(), false);
        assertEquals(tweet.getRetweetCount(), 0);
        assertEquals(tweet.wasRetweetedByMe(), false);

        UrlEntity urlEntity = tweet.getUrlEntities().get(0);

        assertEquals(urlEntity.getStart(), 55);
        assertEquals(urlEntity.getEnd(), 75);
        assertEquals(urlEntity.getUrl().toString(), "http://t.co/YzzMkIao");
        assertEquals(urlEntity.getExpandedURL().toString(),
                "http://www.perugiatoday.it/sport/liomatic-perugia-casalpusterlengo-21-ottobre-2012.html");

        HashtagEntity hashtagEntity = tweet.getHashtagEntities().get(0);
        assertEquals(hashtagEntity.getStart(), 0);
        assertEquals(hashtagEntity.getEnd(), 7);
        assertEquals(hashtagEntity.getText(), "basket");


        User user = tweet.getUser();
        assertEquals(user.getId(), 425699035);
        assertEquals(user.getName(), "PerugiaToday");
        assertEquals(user.getScreenName(), "PerugiaToday");
        assertEquals(user.getLocation(), "Perugia");
        assertEquals(user.getDescription(), "");
        assertEquals(user.isContributorsEnabled(), false);
        assertEquals(user.getUrl().toString(), "http://www.perugiatoday.it/");
        assertEquals(user.isProtected(), false);
        assertEquals(user.getFollowersCount(), 123);
        assertEquals(user.getFriendsCount(), 93);
        //TO_INSERT	"createdAt":"Dec 1, 2011 10:49:25 AM",
        assertEquals(user.getFavouritesCount(), 0);
        assertEquals(user.getLang(), "it");
        assertEquals(user.getStatusesCount(), 996);
        assertEquals(user.getListedCount(), 3);
    }

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }
}
