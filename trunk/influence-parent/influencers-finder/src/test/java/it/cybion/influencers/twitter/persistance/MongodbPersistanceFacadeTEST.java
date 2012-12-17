package it.cybion.influencers.twitter.persistance;

import it.cybion.influencers.twitter.persistance.mongodb.MongodbPersistanceManager;

import java.net.UnknownHostException;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;


public class MongodbPersistanceFacadeTEST {
	
	@Test
	public void insertionDeletionTest() throws UnknownHostException , UserNotProfileEnriched, UserNotPresentException{
		String userJson = "{\"id\": 425699035,\"name\": \"PerugiaToday\",\"screenName\": \"PerugiaToday\",\"location\": \"Perugia\",\"description\": \"sono fatto cosi e cosa\",\"isContributorsEnabled\": false,\"profileImageUrl\": \"http://a0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"profileImageUrlHttps\": \"https://si0.twimg.com/profile_images/1667564455/logoPerugia_normal.jpg\",\"url\": \"http://www.perugiatoday.it/\",\"isProtected\": false,\"followersCount\": 123,\"profileBackgroundColor\": \"C0DEED\",\"profileTextColor\": \"333333\",\"profileLinkColor\": \"0084B4\",\"profileSidebarFillColor\": \"DDEEF6\",\"profileSidebarBorderColor\": \"C0DEED\",\"profileUseBackgroundImage\": true,\"showAllInlineMedia\": false,\"friendsCount\": 93,\"createdAt\": \"Dec 1, 2011 10:49:25 AM\",\"favouritesCount\": 0,\"utcOffset\": -1,\"profileBackgroundImageUrl\": \"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundImageUrlHttps\": \"https://si0.twimg.com/images/themes/theme1/bg.png\",\"profileBackgroundTiled\": false,\"lang\": \"it\",\"statusesCount\": 996,\"isGeoEnabled\": false,\"isVerified\": false,\"translator\": false,\"listedCount\": 3,\"isFollowRequestSent\": false}";
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
	
	
	

}
