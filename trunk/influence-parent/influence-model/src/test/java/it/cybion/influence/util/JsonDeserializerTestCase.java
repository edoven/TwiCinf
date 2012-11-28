package it.cybion.influence.util;

import it.cybion.influence.IO.InputReader;
import it.cybion.influence.model.*;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class JsonDeserializerTestCase {
	
	private JsonDeserializer jsonDeserializer;

    private static final Logger logger = Logger.getLogger(JsonDeserializerTestCase.class);
	
	@BeforeClass
	public void setup() throws IOException
	{
		jsonDeserializer = new JsonDeserializer();
        logger.info("started");
	}

    @AfterClass
	public void shutdown()
	{
    	jsonDeserializer = null;
	}


	@Test
	public void shouldDeserializeJsonToObject1() throws ParseException
	{
		String jsonString = null;
		
		try {
			jsonString = InputReader.fileContentToSingleLine("src/test/resources/tweet01.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Tweet tweet = jsonDeserializer.deserializeJsonStringToTweet(jsonString);
		assertNotNull(tweet);
		
		assertEquals(tweet.getCreatedAt(), DataParser.parseTwitterData("Oct 19, 2012 12:44:30 PM"));
        assertEquals(tweet.getId(), "259243620450848770");
        assertEquals(tweet.getText(), "#basket, liomatic perugia affronta il casalpusterlengo\nhttp://t.co/YzzMkIao");
        assertEquals(tweet.getSource(), "web");
        assertEquals(tweet.isTruncated(), false);
        assertEquals(tweet.getInReplyToStatusId(), "-1");
        assertEquals(tweet.getInReplyToUserId(), "-1");
        assertEquals(tweet.isFavorited(), false);
        assertEquals(tweet.getRetweetCount(), 0);


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
        assertEquals(user.getCreatedAt(), DataParser.parseTwitterData("Dec 1, 2011 10:49:25 AM"));
        assertEquals(user.getFavouritesCount(), 0);
        assertEquals(user.getLang(), "it");
        assertEquals(user.getStatusesCount(), 996);
        assertEquals(user.getListedCount(), 3);
	}

	@Test
	public void shouldDeserializeJsonToObject2()
	{
		String jsonString = null;
		
		try {
			jsonString = InputReader.fileContentToSingleLine("src/test/resources/tweet02.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Tweet tweet = jsonDeserializer.deserializeJsonStringToTweet(jsonString);
		assertNotNull(tweet);
		
		assertEquals(tweet.getCreatedAt(), DataParser.parseTwitterData("Oct 19, 2012 1:02:39 PM") );
        assertEquals(tweet.getId(), "259248190040178700");
        assertEquals(tweet.getText(), "@cristianotoni @rapierpa @SAPItalia @jessyb86 Se venite in Umbria da oggi Eurochocolate,sul nostro blog c'è scritto come muoversi meglio:-)");
        assertEquals(tweet.getSource(), "web");
        assertEquals(tweet.isTruncated(), false);
        assertEquals(tweet.getInReplyToStatusId(), "-1");
        assertEquals(tweet.getInReplyToUserId(), "18632224");
        assertEquals(tweet.isFavorited(), false);
        assertEquals(tweet.getRetweetCount(), 0);

        
        List<UserMentionEntity> userMentionEntities = tweet.getUserMentionEntities();
        assertEquals(userMentionEntities.size(), 4);
        
        UserMentionEntity userMentionEntity = userMentionEntities.get(0);
        //userMentionEntity 1 - the first one
        assertEquals(userMentionEntity.getStart(), 0);
        assertEquals(userMentionEntity.getEnd(), 14);
        assertEquals(userMentionEntity.getName() ,"Cristiano Toni");
        assertEquals(userMentionEntity.getScreenName(), "cristianotoni");
        assertEquals(userMentionEntity.getId(), "18632224");       
        //userMentionEntity 2 - the second one
        userMentionEntity = userMentionEntities.get(1);
        assertEquals(userMentionEntity.getStart(), 15);
        assertEquals(userMentionEntity.getEnd(), 24);
        assertEquals(userMentionEntity.getName() ,"Raffaella Pierpaoli");
        assertEquals(userMentionEntity.getScreenName(), "rapierpa");
        assertEquals(userMentionEntity.getId(), "18032603");
        //userMentionEntity 3 - the third one
        userMentionEntity = userMentionEntities.get(2);
        assertEquals(userMentionEntity.getStart(), 25);
        assertEquals(userMentionEntity.getEnd(), 35);
        assertEquals(userMentionEntity.getName() ,"SAP Italia");
        assertEquals(userMentionEntity.getScreenName(), "SAPItalia");
        assertEquals(userMentionEntity.getId(), "552681849");
        //userMentionEntity 4 - the fourth one
        userMentionEntity = userMentionEntities.get(3);
        assertEquals(userMentionEntity.getStart(), 36);
        assertEquals(userMentionEntity.getEnd(), 45);
        assertEquals(userMentionEntity.getName() ,"Jessica Bonaiti");
        assertEquals(userMentionEntity.getScreenName(), "jessyb86");
        assertEquals(userMentionEntity.getId(), "223104473");

        //BEWARE! Does the empty list get initialitiated or is null????
        List<UrlEntity> urlEntities = tweet.getUrlEntities();
        assertEquals(urlEntities.size(), 0);
        
        //BEWARE! Does the empty list get initialitiated or is null????
        List<HashtagEntity> hashtagEntities = tweet.getHashtagEntities();
        assertEquals(hashtagEntities.size(), 0);
       

        User user = tweet.getUser();
        assertEquals(user.getId(), 293373251);
        assertEquals(user.getName(), "Paola Gigante");
        assertEquals(user.getScreenName(), "PaolaGigante62");
        assertEquals(user.getLocation(), "inguaribilmente fuori tempo");
        assertEquals(user.getDescription(), "nonostante l'età ... sono giovanissima:-) tanto entusiasmo, curiosità, voglia di imparare e di conoscere! ");
        assertEquals(user.isContributorsEnabled(), false);
        assertEquals(user.getUrl().toString(), "http://blog.aspasiel.it");
        assertEquals(user.isProtected(), false);
        assertEquals(user.getFollowersCount(), 98);
        assertEquals(user.getFriendsCount(), 193);
        assertEquals(user.getCreatedAt(), DataParser.parseTwitterData("May 5, 2011 9:19:00 AM"));
        assertEquals(user.getFavouritesCount(), 81);
        assertEquals(user.getLang(), "it");
        assertEquals(user.getStatusesCount(), 1171);
        assertEquals(user.getListedCount(), 1);
		
    }
	
	
	@Test
	public void shouldDeserializeJsonToObject3()
	{
		String jsonString = null;
		
		try {
			jsonString = InputReader.fileContentToSingleLine("src/test/resources/tweet03.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Tweet tweet = jsonDeserializer.deserializeJsonStringToTweet(jsonString);
		assertNotNull(tweet);
		
		assertEquals(tweet.getCreatedAt(), DataParser.parseTwitterData("Oct 19, 2012 1:24:53 PM"));
        assertEquals(tweet.getId(), "259253783605936130");
        assertEquals(tweet.getText(), "RT @ansa_it: Via Eurochocolate con torta i-phone. Citta' gia' affollata di turisti http://t.co/S8tppZeH");
        assertEquals(tweet.getSource(), "<a href=\"http://twitter.com/download/iphone\" rel=\"nofollow\">Twitter for iPhone</a>");
        assertEquals(tweet.isTruncated(), false);
        assertEquals(tweet.getInReplyToStatusId(), "-1");
        assertEquals(tweet.getInReplyToUserId(), "-1");
        assertEquals(tweet.isFavorited(), false);
        assertEquals(tweet.getRetweetCount(), 3);
        
        /*
         * retweeted status
         */
        Tweet retweetedStatus = tweet.getRetweetedStatus();
        assertEquals(retweetedStatus.getCreatedAt(), DataParser.parseTwitterData("Oct 19, 2012 1:05:04 PM"));
        assertEquals(retweetedStatus.getId(), "259248797421539330");
        assertEquals(retweetedStatus.getText(), "Via Eurochocolate con torta i-phone. Citta' gia' affollata di turisti http://t.co/S8tppZeH");
        assertEquals(retweetedStatus.getSource(),"<a href=\"http://www.timendum.net/\" rel=\"nofollow\">Timendum.net</a>");
        assertEquals(retweetedStatus.isTruncated(), false);
        assertEquals(retweetedStatus.getInReplyToStatusId(), "-1");
        assertEquals(retweetedStatus.getInReplyToUserId(), "-1");
        assertEquals(retweetedStatus.isFavorited(), false);
        assertEquals(retweetedStatus.getRetweetCount(), 3);

        List<UrlEntity> retweetedStatusUrlEntities = retweetedStatus.getUrlEntities();
        assertEquals(retweetedStatusUrlEntities.size(), 1);
        UrlEntity retweetedStatusUrlEntitY = retweetedStatusUrlEntities.get(0);
        assertEquals(retweetedStatusUrlEntitY.getStart(), 70);
        assertEquals(retweetedStatusUrlEntitY.getEnd(), 90);
        assertEquals(retweetedStatusUrlEntitY.getUrl().toString(), "http://t.co/S8tppZeH");
        assertEquals(retweetedStatusUrlEntitY.getExpandedURL().toString(), "http://bit.ly/Vc8uyK");
        assertEquals(retweetedStatusUrlEntitY.getDisplayURL(), "bit.ly/Vc8uyK");
        
        assertEquals(retweetedStatus.getHashtagEntities().size(), 0);
        User retweetedStatusUser = retweetedStatus.getUser();
        assertEquals(retweetedStatusUser.getId(), 19235153);
        assertEquals(retweetedStatusUser.getName(), "Ansa it");
        assertEquals(retweetedStatusUser.getScreenName(), "ansa_it");
        assertEquals(retweetedStatusUser.getLocation(), "Web");
        assertEquals(retweetedStatusUser.getDescription(), "Account NON UFFICIALE di ansa.it, creato tramite TwitterFeed");
        assertEquals(retweetedStatusUser.isContributorsEnabled(), false);
        assertEquals(retweetedStatusUser.getUrl().toString(), "http://ansa.it/");
        assertEquals(retweetedStatusUser.isProtected(), false);
        assertEquals(retweetedStatusUser.getFollowersCount(), 89057);
        assertEquals(retweetedStatusUser.getFriendsCount(), 1);
        assertEquals(retweetedStatusUser.getCreatedAt(), DataParser.parseTwitterData("Jan 20, 2009 2:08:34 PM"));
        assertEquals(retweetedStatusUser.getFavouritesCount(), 0);
        assertEquals(retweetedStatusUser.getLang(), "en");
        assertEquals(retweetedStatusUser.getStatusesCount(), 85073);
        assertEquals(retweetedStatusUser.getListedCount(), 1655);
        /*
         * rewteetedStatus end
         */

        
        List<UserMentionEntity> userMentionEntities = tweet.getUserMentionEntities();
        assertEquals(userMentionEntities.size(), 1);
        
        UserMentionEntity userMentionEntity = userMentionEntities.get(0);
        //userMentionEntity 1 - the first one
        assertEquals(userMentionEntity.getStart(), 3);
        assertEquals(userMentionEntity.getEnd(), 11);
        assertEquals(userMentionEntity.getName() ,"Ansa it");
        assertEquals(userMentionEntity.getScreenName(), "ansa_it");
        assertEquals(userMentionEntity.getId(), "19235153");       
        


        List<UrlEntity> urlEntities = tweet.getUrlEntities();
        assertEquals(urlEntities.size(), 1);
        UrlEntity urlEntity = tweet.getUrlEntities().get(0);
        assertEquals(urlEntity.getStart(), 83);
        assertEquals(urlEntity.getEnd(), 103);
        assertEquals(urlEntity.getUrl().toString(), "http://t.co/S8tppZeH");
        assertEquals(urlEntity.getExpandedURL().toString(), "http://bit.ly/Vc8uyK");
        assertEquals(urlEntity.getDisplayURL(), "bit.ly/Vc8uyK");
        

        List<HashtagEntity> hashtagEntities = tweet.getHashtagEntities();
        assertEquals(hashtagEntities.size(), 0);
       

        User user = tweet.getUser();
        assertEquals(user.getId(), 90604665);
        assertEquals(user.getName(), "LadyElle_");
        assertEquals(user.getScreenName(), "lauramesolella");
        assertEquals(user.getLocation(), "italia");
        assertEquals(user.getDescription(), "I'm a big big girl in a big big world!!");
        assertEquals(user.isContributorsEnabled(), false);
        assertEquals(user.getUrl(), null);
        assertEquals(user.isProtected(), false);
        assertEquals(user.getFollowersCount(), 61);
        assertEquals(user.getFriendsCount(), 266);
        assertEquals(user.getCreatedAt(), DataParser.parseTwitterData("Nov 17, 2009 11:42:55 AM"));
        assertEquals(user.getFavouritesCount(), 2);
        assertEquals(user.getLang(), "it");
        assertEquals(user.getStatusesCount(), 518);
        assertEquals(user.getListedCount(), 0);
		
    }

    
}
