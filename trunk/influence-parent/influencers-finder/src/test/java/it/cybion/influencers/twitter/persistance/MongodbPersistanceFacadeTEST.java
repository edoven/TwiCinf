package it.cybion.influencers.twitter.persistance;

import it.cybion.influencers.twitter.persistance.mongodb.MongodbPersistanceManager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;


import static org.testng.Assert.assertEquals;


public class MongodbPersistanceFacadeTEST {
	
	@Test(enabled = false)
	public void deleteUser() throws UnknownHostException{
		MongodbPersistanceManager persistanceManager = new MongodbPersistanceManager("localhost", "testdb", "testcollection");
		persistanceManager.removeUser(425699035l);
	}
	
	@Test
	public void insertionDeletionTEST() throws UnknownHostException , UserNotProfileEnriched, UserNotPresentException{
		String userJson = "{\"id\": 425699035,\"name\": \"PerugiaToday\",\"screenName\": \"PerugiaToday\",\"location\": \"Perugia\",\"description\": \"sono fatto cosi e cosa\",\"isContributorsEnabled\": false,\"profileImageUrl\": \"http://a0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"profileImageUrlHttps\": \"https://si0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"url\": \"http://www.perugiatoday.it/\",\"isProtected\": false,\"followersCount\": 123,\"profileBackgroundColor\": \"C0DEED\",\"profileTextColor\": \"333333\",\"profileLinkColor\": \"0084B4\",\"profileSidebarFillColor\": \"DDEEF6\",\"profileSidebarBorderColor\": \"C0DEED\",\"profileUseBackgroundImage\": true,\"showAllInlineMedia\": false,\"friendsCount\": 93,\"createdAt\": \"Dec 1, 2011 10:49:25 AM\",\"favouritesCount\": 0,\"utcOffset\": -1,\"profileBackgroundImageUrl\": \"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundImageUrlHttps\": \"https://si0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundTiled\": false,\"lang\": \"it\",\"statusesCount\": 996,\"isGeoEnabled\": false,\"isVerified\": false,\"translator\": false,\"listedCount\": 3,\"isFollowRequestSent\": false}";
		//System.out.println(userJson);
		MongodbPersistanceManager persistanceManager = new MongodbPersistanceManager("localhost", "testdb", "testcollection");
		persistanceManager.putUser(userJson);
		assertEquals(persistanceManager.getDescription(425699035l), "sono fatto cosi e cosa");
		persistanceManager.removeUser(425699035l);
		
		try {
			persistanceManager.getDescription(425699035l);
		} catch (UserNotPresentException e) {
			assertEquals(true, true);
			return;
		}
		assertEquals(false, true);
	}
	
	
	@Test
	public void insertionAndRetrivalTEST() throws UnknownHostException , UserNotProfileEnriched, UserNotPresentException{
		String userJson = "{\"id\": 425699035,\"name\": \"PerugiaToday\",\"screenName\": \"PerugiaToday\",\"location\": \"Perugia\",\"description\": \"sono fatto cosi e cosa\",\"isContributorsEnabled\": false,\"profileImageUrl\": \"http://a0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"profileImageUrlHttps\": \"https://si0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"url\": \"http://www.perugiatoday.it/\",\"isProtected\": false,\"followersCount\": 123,\"profileBackgroundColor\": \"C0DEED\",\"profileTextColor\": \"333333\",\"profileLinkColor\": \"0084B4\",\"profileSidebarFillColor\": \"DDEEF6\",\"profileSidebarBorderColor\": \"C0DEED\",\"profileUseBackgroundImage\": true,\"showAllInlineMedia\": false,\"friendsCount\": 93,\"createdAt\": \"Dec 1, 2011 10:49:25 AM\",\"favouritesCount\": 0,\"utcOffset\": -1,\"profileBackgroundImageUrl\": \"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundImageUrlHttps\": \"https://si0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundTiled\": false,\"lang\": \"it\",\"statusesCount\": 996,\"isGeoEnabled\": false,\"isVerified\": false,\"translator\": false,\"listedCount\": 3,\"isFollowRequestSent\": false}";
		MongodbPersistanceManager persistanceManager = new MongodbPersistanceManager("localhost", "testdb", "testcollection");
		persistanceManager.putUser(userJson);
		String retrievedUserJson = persistanceManager.getUser(425699035l);		
		persistanceManager.removeUser(425699035l);
		
		DBObject originalUser = (DBObject) JSON.parse(userJson);
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		
		assertEquals(originalUser.get("id"), retrievedUser.get("id"));
		assertEquals(originalUser.get("screen_name"), retrievedUser.get("screen_name"));
		assertEquals(originalUser.get("description"), retrievedUser.get("description"));
		assertEquals(originalUser.get("name"), retrievedUser.get("name"));
		assertEquals(originalUser.get("profileTextColor"), retrievedUser.get("profileTextColor"));
		assertEquals(originalUser.get("profileImageUrlHttps"), retrievedUser.get("profileImageUrlHttps"));		
	}
	
	
	@Test
	public void addFriendsTEST() throws UserNotPresentException, UnknownHostException, UserNotFriendsEnrichedException {
		MongodbPersistanceManager persistanceManager = new MongodbPersistanceManager("localhost", "testdb", "testcollection");
		//user creation
		DBObject user = new BasicDBObject();
		Long userId = 1111l;
		user.put("id", userId);
		//friends ids creation
		List<Long> friendsIds = new ArrayList<Long>();
		Long friendOneId = 2222l; friendsIds.add(friendOneId);
		Long friendTwoId = 2222l; friendsIds.add(friendTwoId);
		Long friendThreeId = 2222l; friendsIds.add(friendThreeId);
		//user insertion
		persistanceManager.putUser(user.toString());
		//friends insertion
		persistanceManager.putFriends(userId, friendsIds);
		
		//getting friends
		List<Long> retrievedFriendsIds = persistanceManager.getFriends(userId);
		assertEquals( retrievedFriendsIds.size() , friendsIds.size() );
		
		//getting user (it should have been enriched)
		String retrievedUserJson = persistanceManager.getUser(userId);
		DBObject retrievedUser = (DBObject) JSON.parse(retrievedUserJson);
		retrievedFriendsIds = (List<Long>) retrievedUser.get("friends");
		assertEquals( retrievedFriendsIds.size() , friendsIds.size() );
		
		persistanceManager.removeUser(userId);
		persistanceManager.removeUser(friendOneId);
		persistanceManager.removeUser(friendTwoId);
		persistanceManager.removeUser(friendThreeId);
		
	}
	
	

}
