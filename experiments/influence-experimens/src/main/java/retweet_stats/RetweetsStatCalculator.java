package retweet_stats;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.TwitterException;

import com.google.gson.Gson;

import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.TwitterFacadeFactory;




public class RetweetsStatCalculator {

	private class Tweet {
		User user;
		Tweet retweeted_status;		
//		String text;
	}
	
	private class User {
		long id;
	}
	
	
	public static void main(String[] args) throws UnknownHostException {
		TwitterFacade twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		List<Long> usersId = new ArrayList<Long>();
		Integer[] usersIdsArray = {97977502, 29013669, 92367751, 23438487, 130114910, 42643323, 327292714, 157994424, 81197480, 92343661, 74849509, 142638317, 17097782, 100057386, 183734502, 349886570, 308520976, 87463937, 30702034, 24124842, 271694553, 64523749, 7092102, 379730648, 191856490, 197010861, 79989712, 462403479, 15933910, 20779908, 398955526, 435352705, 551898284, 278041763, 52813110, 116174993, 112781497, 574490996, 26494583, 326638638, 26774034, 223102849, 112453391, 475859683, 27470993, 69329030, 461531291, 402009629, 129587268, 47376364, 130281555, 348379865, 537957550, 218676661, 35459922, 132653134, 156655580, 479699433, 390296111, 113514650, 216622248, 185149826, 88636693, 16302583, 131888680, 37827616, 65890872, 289234427, 29232633, 41204900, 153051851, 25976668, 196488420, 127858434, 97511450, 19923638, 40457114, 289596760, 72950235, 159825515, 28726228, 308357008, 24155818, 52983468, 485261216, 568162128, 49239873, 34609682, 237196824, 378478956, 47459700, 67185748, 416998506, 127635597, 16193578, 20298371, 193265709, 27844479, 160556184, 91324330, 130788967, 15790423, 237369668, 76135448, 215810002, 111332587, 38222344, 29178412, 102079657, 367250108, 125075583, 20177423, 194084746, 196514953, 49165016, 47082931, 225918996, 162790036, 19247844, 90154058, 328931134, 46164460, 99935436, 28194129, 154639409, 600022653, 276000097, 27003342, 111284269, 26994801, 61150091, 74069002, 50546729, 425814228, 283693195, 35229877, 116142776, 122311567, 14399483, 94404302, 474785639, 17221180, 276964194, 219306730, 192564127, 94777137, 304868426, 211205300, 33212890, 7046912, 155829534, 46104812, 244143740, 138714078, 115539092, 146593792, 221382131, 84883140, 53665497, 287319280, 221815363, 24190981, 590147247, 115626888, 251592753, 562066991, 93883261, 94337947, 156671765, 19148708, 8641682, 18683376, 326359913, 153474021, 294472679, 57272246, 15196388, 550994804, 80093667, 46178833, 49577249, 197720024, 112196433, 90984732, 19399719, 218263629, 21554575, 27016235, 241213066, 20993978, 250314584, 988880377, 64405581, 247458352, 124509213, 259414009, 369426625, 28823223, 47106150, 395582367, 66977585, 411104150, 398047289, 256687977, 294664152, 187585908, 9265172, 37491839, 26239721, 412321799, 17863930, 115654968, 316308465, 627376905, 85826164, 80908764, 136361303, 293164794, 15317676, 407828589, 484959594, 20908109, 249333512, 280415695, 18572379, 372215028, 45320710, 407835946, 389707793, 207911009, 23959878, 900022465, 225029801, 159131717, 124777527, 19212009, 396045481, 13935482, 620408706, 21476507, 72568426, 223636148, 528515362, 247796999, 20225875, 280491424, 34301869, 211439655, 65512738, 109842963, 51399547, 72806348, 176052314, 381964686, 17064593, 84348538, 40718259, 205339205, 134044290, 194918664, 291914238, 17207345, 92488375, 22494444, 235555523, 53004677, 132888646};
		for (int i = 0; i < usersIdsArray.length; i++) {
			usersId.add(new Long(usersIdsArray[i]));
		}
		
		printRetweetsStats(twitterFacade, usersId);
		System.out.println("input size = "+usersId.size());
		System.exit(0);
	}
	
	public static void printRetweetsStats(TwitterFacade twitterFacade, List<Long> usersIds) {
		int count = 0;
		Map<Long,Integer> user2TotalRetweets = new HashMap<Long,Integer>();
		for (Long userId : usersIds) {
			System.out.println("getRetweetsStatusForUser for user "+(count++)+"/"+usersIds.size());
			try {
				Map<Long, Integer> users2Retweets = getRetweetsStatusForUser(twitterFacade, userId);
				for (Long user : users2Retweets.keySet()) {
					if (usersIds.contains(user)) {
						System.out.println("among dataset: " + user + " - "+users2Retweets.get(user));
						if (user2TotalRetweets.containsKey(user))
							user2TotalRetweets.put(user, user2TotalRetweets.get(user)+users2Retweets.get(user));
						else
							user2TotalRetweets.put(user, users2Retweets.get(user));
					}
				}
			} catch (TwitterException e) {
				continue;
			}
		}
		
		System.out.println(user2TotalRetweets);
		System.out.println("user2TotalRetweets.size()="+user2TotalRetweets.size());
	}
	
	public static Map<Long, Integer> getRetweetsStatusForUser(TwitterFacade twitterFacade, Long userId) throws TwitterException {
		Map<Long, Integer> users2Retweets = new HashMap<Long,Integer>();
		List<String> tweetsJsons = twitterFacade.getUpTo200Tweets(userId);
		List<Tweet> tweets = new ArrayList<Tweet>();
		Gson gson = new Gson();
		for (String tweetJson : tweetsJsons) {
			tweets.add(gson.fromJson(tweetJson, Tweet.class));
		}
		for (Tweet tweet : tweets) {
			if (tweet.retweeted_status!=null) {
				long userIdn = tweet.retweeted_status.user.id;
				if (users2Retweets.containsKey(userIdn))
					users2Retweets.put(userIdn, users2Retweets.get(userIdn));
				else
					users2Retweets.put(userIdn, 1);
			}	
		}
		return users2Retweets;
	}
	
}
