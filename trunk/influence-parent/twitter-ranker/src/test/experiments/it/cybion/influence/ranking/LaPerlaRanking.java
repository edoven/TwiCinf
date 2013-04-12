package it.cybion.influence.ranking;

import it.cybion.influence.ranking.topic.TopicScorer;
import it.cybion.influence.ranking.topic.dictionary.DictionaryTopicScorer;
import it.cybion.influence.ranking.utils.ListFileReader;
import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.utils.CalendarManager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.TwitterException;
import utils.TwitterFacadeFactory;

public class LaPerlaRanking
{
	public static void main(String[] args) throws UnknownHostException, TwitterException
	{
		TwitterCache twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		List<Long> laPerla2800UserIds = ListFileReader.readLongListFile("/home/godzy/Dropbox/universita/tesi/laPerla/laPerla2800UsersIds.txt");
		printByFollowers(laPerla2800UserIds,twitterFacade);
		List<Long> usersToRank = UsersSampling.getSamplingByFollowers(laPerla2800UserIds, twitterFacade);
//		List<Long> usersToRank = filterByFollowersCount(twitterFacade,laPerla2800UserIds, 5000);
		laPerla2800UserIds = null;
		TopicScorer topicScorere = getDictionaryTopicScorer();
//		TweetToTopicDistanceCalculator topicDistanceCalculator = getLuceneTweetToTopicDistanceCalculator(laPerla2800UserIds);
		RankingCalculator rankingCalculator = new RankingCalculator(twitterFacade, topicScorere);
		Date fromDate = CalendarManager.getDate(2013, 2, 28);
		Date toDate   = CalendarManager.getDate(2013, 3, 7);	
//		List<RankedUser> users = rankingCalculator.getRankedUsersWithoutUrlsResolution(usersToRank,fromDate, toDate);
		List<RankedUser> users = rankingCalculator.getRankedUsersWithUrlsResolution(usersToRank,fromDate, toDate);

		printInfo(users);		
		System.exit(0);
	}
	
	
	
	
	
	private static void printByFollowers(List<Long> usersIds, TwitterCache twitterCache) throws TwitterException
	{
		int followers0to100 = 0,
			followers100to500 = 0,
			followers500to2000 = 0,
			followers2000to5000 = 0,
			followers5000toMax = 0;
//		twitterCache.getDescriptions(usersIds);
		for (Long userId : usersIds)
		{
			int followers;
			try
			{
				followers = twitterCache.getFollowersCount(userId);
			}
			catch (TwitterException e)
			{
				System.out.println("Problem with user with id "+userId);
				continue;
			}
			if (followers<100)
				followers0to100++;
			if (followers>=100 && followers<500)
				followers100to500++;
			if (followers>=500 && followers<2000)
				followers500to2000++;
			if (followers>=2000 && followers<5000)
				followers2000to5000++;
			if (followers>=5000)
				followers5000toMax++;
		}
		System.out.println("followers0to100="+followers0to100);
		System.out.println("followers100to500="+followers100to500);
		System.out.println("followers500to2000="+followers500to2000);
		System.out.println("followers2000to5000="+followers2000to5000);
		System.out.println("followers5000toMax="+followers5000toMax);	
	}
	
	
//	private static List<Long> filterByFollowersCount(TwitterCache twitterCache, List<Long> users, int followersCount)
//	{
//		List<Long> goodUsers = new ArrayList<Long>();
//		for (Long userId : users)
//		{
//			try
//			{
//				if (twitterCache.getFollowersCount(userId)>=followersCount)
//					goodUsers.add(userId);
//			}
//			catch (TwitterException e)
//			{
//				System.out.println("Problem with user with id "+userId);
//				continue;
//			}
//		}
//		return goodUsers;
//	}
	
	
	private static void printInfo(List<RankedUser> users)
	{
		System.out.println();
		System.out.println();
		System.out.println("== RANKED USERS ==");
		System.out.println();
		for (RankedUser rankedUser : users)
			System.out.println(rankedUser.toString());
		
		System.out.println();
		System.out.println();
		System.out.println("== CSV USERS ==");
		System.out.println("screenName,followersCount,originalTweets,topicTweetsCount,topicTweetsRatio,meanRetweetsCount");
		System.out.println();
		for (RankedUser rankedUser : users)
			System.out.printf("%s,%d,%d,%.5f,%.5f,%.5f\n", 
							  rankedUser.screenName,
							  rankedUser.followersCount,
							  rankedUser.originalTweets,
							  rankedUser.topicTweetsCount,
							  rankedUser.topicTweetsRatio,
							  rankedUser.meanRetweetsCount);
//			System.out.println(rankedUser.toCSV());
	}
	
	
	private static TopicScorer getDictionaryTopicScorer()
	{
		List<String> dictionary = new ArrayList<String>();
		dictionary.add("fashion");
		dictionary.add("nyfw");
		dictionary.add("collection");
		dictionary.add("style");
		dictionary.add("shoes");
		dictionary.add("beauty");
		dictionary.add("superdry");
		dictionary.add("fashionweek");
		dictionary.add("designers");
		dictionary.add("dress");
		dictionary.add("designer");
		dictionary.add("magazine");
		dictionary.add("shopping");
		dictionary.add("glamour");
		dictionary.add("carpet");
		dictionary.add("couture");
		dictionary.add("hair");
		dictionary.add("wear");
		dictionary.add("chic");
		dictionary.add("dresses");
		dictionary.add("wearing");
		dictionary.add("vogue");
		dictionary.add("backstage");
		dictionary.add("models");
		dictionary.add("clothing");
		dictionary.add("milanfashionweek");
		dictionary.add("boutique");
		dictionary.add("vintage");
		dictionary.add("model");
		dictionary.add("accessories");
		dictionary.add("haute");
		dictionary.add("moda");
		dictionary.add("luxury");
		dictionary.add("outfit");
		dictionary.add("bag");
		dictionary.add("boots");
		dictionary.add("makeup");
		dictionary.add("shoe");
		dictionary.add("eyelashes");
		dictionary.add("leather");
		dictionary.add("design");
		
		return new DictionaryTopicScorer(dictionary);
				
	}
	
	
//	private static TopicScorer getLuceneTweetToTopicDistanceCalculator(List<Long> users, TwitterCache twitterCache)
//	{	
//		List<Long> seedUsers = users.subList(0, 50);
//		
//		String luceneTempDirPath = "/home/godzy/Desktop/temp";
////		logger.info("Building lucene indexes - START");		
////		List<Directory> indexes = TweetsIndexCreator.createSingleDocumentIndexesForUsers(twitterCache, luceneTempDirPath, seedUsers);		
//		List<Directory> indexes = new ArrayList<Directory>();
//		indexes.add( TweetsIndexCreator.createSingleIndexForUsers(twitterCache, luceneTempDirPath, seedUsers) );		
////		logger.info("Building lucene indexes - FINISHED");
//		return new LuceneTweetToTopicDistanceCalculatorOLD(indexes);
//	}
	
}
