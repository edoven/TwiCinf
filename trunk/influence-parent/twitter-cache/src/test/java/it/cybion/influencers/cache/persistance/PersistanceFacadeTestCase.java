package it.cybion.influencers.cache.persistance;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import it.cybion.influencers.cache.persistance.exceptions.*;
import it.cybion.influencers.cache.utils.CalendarManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;



public class PersistanceFacadeTestCase
{

	private static final Logger logger = Logger.getLogger(PersistanceFacadeTestCase.class);

	private PersistenceFacade persistenceFacade;

	@BeforeClass
	public void init() throws UnknownHostException
	{
		persistenceFacade = PersistenceFacade.getInstance("localhost", "testdb");
	}

	@Test(enabled = true)
	public void insertionAndRetrivalTEST() throws UnknownHostException, UserNotProfileEnrichedException, UserNotPresentException
	{
		String originalUserJson = "{\"id\": 425699035,\"name\": \"PerugiaToday\",\"screenName\": \"PerugiaToday\",\"location\": \"Perugia\",\"description\": \"sono fatto cosi e cosa\",\"isContributorsEnabled\": false,\"profileImageUrl\": \"http://a0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"profileImageUrlHttps\": \"https://si0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"url\": \"http://www.perugiatoday.it/\",\"isProtected\": false,\"followersCount\": 123,\"profileBackgroundColor\": \"C0DEED\",\"profileTextColor\": \"333333\",\"profileLinkColor\": \"0084B4\",\"profileSidebarFillColor\": \"DDEEF6\",\"profileSidebarBorderColor\": \"C0DEED\",\"profileUseBackgroundImage\": true,\"showAllInlineMedia\": false,\"friendsCount\": 93,\"createdAt\": \"Dec 1, 2011 10:49:25 AM\",\"favouritesCount\": 0,\"utcOffset\": -1,\"profileBackgroundImageUrl\": \"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundImageUrlHttps\": \"https://si0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundTiled\": false,\"lang\": \"it\",\"statusesCount\": 996,\"isGeoEnabled\": false,\"isVerified\": false,\"translator\": false,\"listedCount\": 3,\"isFollowRequestSent\": false}";
		persistenceFacade.putUser(originalUserJson);
		String retrievedUserJson = persistenceFacade.getUser(425699035l);
		persistenceFacade.removeUser(425699035l);

		try
		{
			persistenceFacade.getDescription(425699035l);
		} catch (UserNotPresentException e)
		{
			assertEquals(true, true);
			return;
		}
		assertEquals(false, true);

		DBObject originalUser = (DBObject) JSON.parse(originalUserJson);
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);

		assertEquals(originalUser.get("id"), retrievedUser.get("id"));
		assertEquals(originalUser.get("screen_name"), retrievedUser.get("screen_name"));
		assertEquals(originalUser.get("description"), retrievedUser.get("description"));
		assertEquals(originalUser.get("name"), retrievedUser.get("name"));
		assertEquals(originalUser.get("profileTextColor"), retrievedUser.get("profileTextColor"));
		assertEquals(originalUser.get("profileImageUrlHttps"), retrievedUser.get("profileImageUrlHttps"));
	}

	@Test(enabled = true)
	public void insertionAndUpdatingTEST() throws UnknownHostException, UserNotProfileEnrichedException, UserNotPresentException
	{

		logger.info("==1==");
		int id = 1;
		String userJson = "{\"id\": " + id + "}";
		persistenceFacade.putUser(userJson);
		String retrievedUserJson = persistenceFacade.getUser(new Long(id));
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		logger.info(retrievedUser);
		Assert.assertEquals(retrievedUser.get("id"), id);

		logger.info("==2==");
		userJson = "{\"id\": " + id + " ,\"name\": \"Bob Dole\"}";
		persistenceFacade.putUser(userJson);
		retrievedUserJson = persistenceFacade.getUser(new Long(id));
		retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		logger.info(retrievedUser);
		Assert.assertEquals(retrievedUser.get("id"), id);
		Assert.assertNotNull(retrievedUser.get("name"));
		Assert.assertEquals(retrievedUser.get("name"), "Bob Dole");

		logger.info("==3==");
		// now let's check if the field "name" remains untouched
		userJson = "{\"id\": " + id + "}";
		persistenceFacade.putUser(userJson);
		retrievedUserJson = persistenceFacade.getUser(new Long(id));
		retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		logger.info(retrievedUser);
		Assert.assertEquals(retrievedUser.get("id"), id);
		Assert.assertNotNull(retrievedUser.get("name"));
		Assert.assertNotNull(retrievedUser.get("name"));
		Assert.assertEquals(retrievedUser.get("name"), "Bob Dole");

		persistenceFacade.removeUser((long) id);

	}

	@Test(enabled = true)
	public void addFriendsTEST() throws UnknownHostException, UserNotFriendsEnrichedException, UserNotPresentException
	{
		// user creation
		DBObject user = new BasicDBObject();
		Long userId = 1111l;
		user.put("id", userId);
		// friends ids creation
		List<Long> friendsIds = new ArrayList<Long>();
		Long friendOneId = 2222l;
		friendsIds.add(friendOneId);
		Long friendTwoId = 2222l;
		friendsIds.add(friendTwoId);
		Long friendThreeId = 2222l;
		friendsIds.add(friendThreeId);
		// user insertion
		persistenceFacade.putUser(user.toString());
		try
		{
			persistenceFacade.getUser(userId);
			Assert.assertTrue(true);
		} catch (UserNotPresentException e)
		{
			Assert.assertTrue(false);
		}
		// friends insertion
		persistenceFacade.putFriends(userId, friendsIds);
		// check if all friends are inserted
		for (Long friendId : friendsIds)
		{
			try
			{
				persistenceFacade.getUser(friendId);
				Assert.assertTrue(true);
			} catch (UserNotPresentException e)
			{
				Assert.assertTrue(false);
			}
		}

		// test getFriends
		List<Long> retrievedFriendsIds = persistenceFacade.getFriends(userId);
		assertEquals(retrievedFriendsIds.size(), friendsIds.size());
		for (long friendId : retrievedFriendsIds)
		{
			Assert.assertTrue(friendsIds.contains(friendId));
		}

		// getting user (it should have been enriched)
		String retrievedUserJson = persistenceFacade.getUser(userId);
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		List<Integer> intList = (List<Integer>) retrievedUser.get("friends");
		for (int intElement : intList)
		{
			long friendId = (long) intElement;
			Assert.assertTrue(friendsIds.contains(friendId));
		}
		assertEquals(intList.size(), friendsIds.size());

		persistenceFacade.removeUser(userId);
		persistenceFacade.removeUser(friendOneId);
		persistenceFacade.removeUser(friendTwoId);
		persistenceFacade.removeUser(friendThreeId);
	}

	@Test(enabled = true)
	public void addFollowersTEST() throws UserNotPresentException, UnknownHostException, UserNotFollowersEnrichedException
	{
		// MongodbPersistanceManager persistanceManager = new
		// MongodbPersistanceManager("localhost", "testdb", "testcollection");
		// user creation
		DBObject user = new BasicDBObject();
		Long userId = 1111l;
		user.put("id", userId);
		// friends ids creation
		List<Long> followersIds = new ArrayList<Long>();
		Long followerOneId = 2222l;
		followersIds.add(followerOneId);
		Long followerTwoId = 2222l;
		followersIds.add(followerTwoId);
		Long followerThreeId = 2222l;
		followersIds.add(followerThreeId);
		// user insertion
		persistenceFacade.putUser(user.toString());
		// friends insertion
		persistenceFacade.putFollowers(userId, followersIds);

		// getting friends
		List<Long> retrievedFollowersIds = persistenceFacade.getFollowers(userId);
		assertEquals(retrievedFollowersIds.size(), followersIds.size());

		// getting user (it should have been enriched)
		String retrievedUserJson = persistenceFacade.getUser(userId);
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		List<Integer> intList = (List<Integer>) retrievedUser.get("followers");
		for (Integer intElement : intList)
		{
			long followerId = (long) intElement;
			Assert.assertTrue(followersIds.contains(followerId));
		}
		assertEquals(intList.size(), followersIds.size());

		persistenceFacade.removeUser(userId);
		persistenceFacade.removeUser(followerOneId);
		persistenceFacade.removeUser(followerTwoId);
		persistenceFacade.removeUser(followerThreeId);
	}

	@Test(enabled = true)
	public void userNotPresentExceptionTEST()
	{
		try
		{
			persistenceFacade.getUser(534529555443l);
		} catch (UserNotPresentException e)
		{
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	@Test
	public void getStatus() throws UserNotPresentException, UserNotProfileEnrichedException
	{
		String userToInsertJson = "{\"name\": \"Twitter API\", \"id\": 6253282, \"description\":\"my description\", \"status\": {\"text\": \"this is my last status\"}}";
		persistenceFacade.putUser(userToInsertJson);
		String retrievedStatus = persistenceFacade.getStatus(6253282L);
		Assert.assertEquals("this is my last status", retrievedStatus);
		persistenceFacade.removeUser(6253282L);
	}

	@Test
	public void getTweets()
	{
		String tweet = "{\"id\": 1, \"user\": {\"id\": 1 }, \"text\": \"text1\" }";
		persistenceFacade.putTweet(tweet);
		try
		{
			Assert.assertTrue(persistenceFacade.getUpTo200Tweets(1).size() > 0);
		} catch (UserWithNoTweetsException e)
		{
			Assert.assertTrue(false);
		}
		persistenceFacade.removeTweet(1L);
	}
	
	
	@Test
	public void getTweetsByDateWithCoveredRange()
	{
		persistenceFacade.removeTweet(1L);
		persistenceFacade.removeTweet(2L);
		persistenceFacade.removeTweet(3L);
		persistenceFacade.removeTweet(4L);
		String tweet1 = "{\"id\": 1, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Thu Jan 10 19:14:38 +0000 2013\"}";
		String tweet2 = "{\"id\": 2, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Tue Jan 15 19:14:38 +0000 2013\"}";
		String tweet3 = "{\"id\": 3, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Wed Jan 16 19:14:38 +0000 2013\"}";
		String tweet4 = "{\"id\": 4, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Sun Jan 20 19:14:38 +0000 2013\"}";
		persistenceFacade.putTweet(tweet1);
		persistenceFacade.putTweet(tweet2);
		persistenceFacade.putTweet(tweet3);
		persistenceFacade.putTweet(tweet4);
		
		Date fromDate = CalendarManager.getDate(2013, 1, 12);
		Date toDate   = CalendarManager.getDate(2013, 1, 18);
		
		try
		{
			List<String> tweets = persistenceFacade.getTweetsByDate(1, fromDate, toDate);
			Assert.assertTrue(tweets.size()==2);
		}
		catch (UserWithNoTweetsException e)
		{
			Assert.assertTrue(false);
		}
		catch (DataRangeNotCoveredException e)
		{
			Assert.assertTrue(false);
		}
			
		persistenceFacade.removeTweet(1L);
		persistenceFacade.removeTweet(2L);
		persistenceFacade.removeTweet(3L);
		persistenceFacade.removeTweet(4L);
	}
	
	@Test
	public void getTweetsByDateWithUncoveredRangeTooEarly()
	{
		persistenceFacade.removeTweet(1L);
		persistenceFacade.removeTweet(2L);
		persistenceFacade.removeTweet(3L);
		persistenceFacade.removeTweet(4L);
		String tweet1 = "{\"id\": 1, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Thu Jan 10 19:14:38 +0000 2013\"}";
		String tweet2 = "{\"id\": 2, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Tue Jan 15 19:14:38 +0000 2013\"}";
		String tweet3 = "{\"id\": 3, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Wed Jan 16 19:14:38 +0000 2013\"}";
		String tweet4 = "{\"id\": 4, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Sun Jan 20 19:14:38 +0000 2013\"}";
		persistenceFacade.putTweet(tweet1);
		persistenceFacade.putTweet(tweet2);
		persistenceFacade.putTweet(tweet3);
		persistenceFacade.putTweet(tweet4);
		
		Date fromDate = CalendarManager.getDate(2013, 1, 1);
		Date toDate   = CalendarManager.getDate(2013, 1, 18);
		
		try
		{
			List<String> tweets = persistenceFacade.getTweetsByDate(1, fromDate, toDate);
			Assert.assertTrue(false);
		}
		catch (UserWithNoTweetsException e)
		{
			Assert.assertTrue(false);
		}
		catch (DataRangeNotCoveredException e)
		{
			Assert.assertTrue(true);
		}
			
		persistenceFacade.removeTweet(1L);
		persistenceFacade.removeTweet(2L);
		persistenceFacade.removeTweet(3L);
		persistenceFacade.removeTweet(4L);
	}
	
	@Test
	public void getTweetsByDateWithUncoveredRangeTooLate()
	{
		persistenceFacade.removeTweet(1L);
		persistenceFacade.removeTweet(2L);
		persistenceFacade.removeTweet(3L);
		persistenceFacade.removeTweet(4L);
		String tweet1 = "{\"id\": 1, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Thu Jan 10 19:14:38 +0000 2013\"}";
		String tweet2 = "{\"id\": 2, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Tue Jan 15 19:14:38 +0000 2013\"}";
		String tweet3 = "{\"id\": 3, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Wed Jan 16 19:14:38 +0000 2013\"}";
		String tweet4 = "{\"id\": 4, \"user\": {\"id\": 1 }, \"text\": \"text1\", \"created_at\": \"Sun Jan 20 19:14:38 +0000 2013\"}";
		persistenceFacade.putTweet(tweet1);
		persistenceFacade.putTweet(tweet2);
		persistenceFacade.putTweet(tweet3);
		persistenceFacade.putTweet(tweet4);
		
		Date fromDate = CalendarManager.getDate(2013, 1, 12);
		Date toDate   = CalendarManager.getDate(2013, 1, 22);
		
		try
		{
			List<String> tweets = persistenceFacade.getTweetsByDate(1, fromDate, toDate);
			Assert.assertTrue(false);
		}
		catch (UserWithNoTweetsException e)
		{
			Assert.assertTrue(false);
		}
		catch (DataRangeNotCoveredException e)
		{
			Assert.assertTrue(true);
		}
			
		persistenceFacade.removeTweet(1L);
		persistenceFacade.removeTweet(2L);
		persistenceFacade.removeTweet(3L);
		persistenceFacade.removeTweet(4L);
	}

}
