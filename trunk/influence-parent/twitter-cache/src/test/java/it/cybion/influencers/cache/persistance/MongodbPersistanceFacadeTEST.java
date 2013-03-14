package it.cybion.influencers.cache.persistance;


import it.cybion.influencers.cache.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFollowersEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotFriendsEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotProfileEnrichedException;
import it.cybion.influencers.cache.persistance.exceptions.UserWithNoTweetsException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;



public class MongodbPersistanceFacadeTEST
{

	private static final Logger logger = Logger.getLogger(MongodbPersistanceFacadeTEST.class);

	private MongodbPersistanceFacade persistanceFacade;

	@BeforeClass
	public void init() throws UnknownHostException
	{
		persistanceFacade = new MongodbPersistanceFacade("localhost", "testdb");
	}

	@Test(enabled = true)
	public void insertionAndRetrivalTEST() throws UnknownHostException, UserNotProfileEnrichedException, UserNotPresentException
	{
		String originalUserJson = "{\"id\": 425699035,\"name\": \"PerugiaToday\",\"screenName\": \"PerugiaToday\",\"location\": \"Perugia\",\"description\": \"sono fatto cosi e cosa\",\"isContributorsEnabled\": false,\"profileImageUrl\": \"http://a0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"profileImageUrlHttps\": \"https://si0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"url\": \"http://www.perugiatoday.it/\",\"isProtected\": false,\"followersCount\": 123,\"profileBackgroundColor\": \"C0DEED\",\"profileTextColor\": \"333333\",\"profileLinkColor\": \"0084B4\",\"profileSidebarFillColor\": \"DDEEF6\",\"profileSidebarBorderColor\": \"C0DEED\",\"profileUseBackgroundImage\": true,\"showAllInlineMedia\": false,\"friendsCount\": 93,\"createdAt\": \"Dec 1, 2011 10:49:25 AM\",\"favouritesCount\": 0,\"utcOffset\": -1,\"profileBackgroundImageUrl\": \"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundImageUrlHttps\": \"https://si0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundTiled\": false,\"lang\": \"it\",\"statusesCount\": 996,\"isGeoEnabled\": false,\"isVerified\": false,\"translator\": false,\"listedCount\": 3,\"isFollowRequestSent\": false}";
		persistanceFacade.putUser(originalUserJson);
		String retrievedUserJson = persistanceFacade.getUser(425699035l);
		persistanceFacade.removeUser(425699035l);

		try
		{
			persistanceFacade.getDescription(425699035l);
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
		persistanceFacade.putUser(userJson);
		String retrievedUserJson = persistanceFacade.getUser(new Long(id));
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		logger.info(retrievedUser);
		Assert.assertEquals(retrievedUser.get("id"), id);

		logger.info("==2==");
		userJson = "{\"id\": " + id + " ,\"name\": \"Bob Dole\"}";
		persistanceFacade.putUser(userJson);
		retrievedUserJson = persistanceFacade.getUser(new Long(id));
		retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		logger.info(retrievedUser);
		Assert.assertEquals(retrievedUser.get("id"), id);
		Assert.assertNotNull(retrievedUser.get("name"));
		Assert.assertEquals(retrievedUser.get("name"), "Bob Dole");

		logger.info("==3==");
		// now let's check if the field "name" remains untouched
		userJson = "{\"id\": " + id + "}";
		persistanceFacade.putUser(userJson);
		retrievedUserJson = persistanceFacade.getUser(new Long(id));
		retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		logger.info(retrievedUser);
		Assert.assertEquals(retrievedUser.get("id"), id);
		Assert.assertNotNull(retrievedUser.get("name"));
		Assert.assertNotNull(retrievedUser.get("name"));
		Assert.assertEquals(retrievedUser.get("name"), "Bob Dole");

		persistanceFacade.removeUser((long) id);

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
		persistanceFacade.putUser(user.toString());
		try
		{
			persistanceFacade.getUser(userId);
			Assert.assertTrue(true);
		} catch (UserNotPresentException e)
		{
			Assert.assertTrue(false);
		}
		// friends insertion
		persistanceFacade.putFriends(userId, friendsIds);
		// check if all friends are inserted
		for (Long friendId : friendsIds)
		{
			try
			{
				persistanceFacade.getUser(friendId);
				Assert.assertTrue(true);
			} catch (UserNotPresentException e)
			{
				Assert.assertTrue(false);
			}
		}

		// test getFriends
		List<Long> retrievedFriendsIds = persistanceFacade.getFriends(userId);
		assertEquals(retrievedFriendsIds.size(), friendsIds.size());
		for (long friendId : retrievedFriendsIds)
		{
			Assert.assertTrue(friendsIds.contains(friendId));
		}

		// getting user (it should have been enriched)
		String retrievedUserJson = persistanceFacade.getUser(userId);
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		List<Integer> intList = (List<Integer>) retrievedUser.get("friends");
		for (int intElement : intList)
		{
			long friendId = (long) intElement;
			Assert.assertTrue(friendsIds.contains(friendId));
		}
		assertEquals(intList.size(), friendsIds.size());

		persistanceFacade.removeUser(userId);
		persistanceFacade.removeUser(friendOneId);
		persistanceFacade.removeUser(friendTwoId);
		persistanceFacade.removeUser(friendThreeId);
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
		persistanceFacade.putUser(user.toString());
		// friends insertion
		persistanceFacade.putFollowers(userId, followersIds);

		// getting friends
		List<Long> retrievedFollowersIds = persistanceFacade.getFollowers(userId);
		assertEquals(retrievedFollowersIds.size(), followersIds.size());

		// getting user (it should have been enriched)
		String retrievedUserJson = persistanceFacade.getUser(userId);
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		List<Integer> intList = (List<Integer>) retrievedUser.get("followers");
		for (Integer intElement : intList)
		{
			long followerId = (long) intElement;
			Assert.assertTrue(followersIds.contains(followerId));
		}
		assertEquals(intList.size(), followersIds.size());

		persistanceFacade.removeUser(userId);
		persistanceFacade.removeUser(followerOneId);
		persistanceFacade.removeUser(followerTwoId);
		persistanceFacade.removeUser(followerThreeId);
	}

	@Test(enabled = true)
	public void userNotPresentExceptionTEST()
	{
		try
		{
			persistanceFacade.getUser(534529555443l);
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
		persistanceFacade.putUser(userToInsertJson);
		String retrievedStatus = persistanceFacade.getStatus(6253282L);
		Assert.assertEquals("this is my last status", retrievedStatus);
		persistanceFacade.removeUser(6253282L);
	}

	@Test
	public void getTweets()
	{
		String tweet = "{\"id\": 1, \"user\": {\"id\": 1 }, \"text\": \"text1\" }";
		persistanceFacade.putTweet(tweet);
		try
		{
			Assert.assertTrue(persistanceFacade.getUpTo200Tweets(1).size() > 0);
		} catch (UserWithNoTweetsException e)
		{
			Assert.assertTrue(false);
		}
	}

}
