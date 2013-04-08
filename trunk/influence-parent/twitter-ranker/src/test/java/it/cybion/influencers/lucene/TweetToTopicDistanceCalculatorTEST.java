//package it.cybion.influencers.lucene;
//
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.List;
//
//import it.cybion.influence.ranking.utils.urlsexpansion.UrlsExapandedTweetsTextExtractor;
//import it.cybion.influencers.cache.TwitterCache;
//import it.cybion.influencers.cache.TwitterFacadeFactory;
//import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
//
//import org.testng.annotations.Test;
//
//import twitter4j.TwitterException;
//
//
//
//public class TweetToTopicDistanceCalculatorTEST
//{	
//	@Test
//	public void test1() throws UnknownHostException, TwitterException, ProtectedUserException
//	{
//		TwitterCache twitterCache = TwitterFacadeFactory.getTwitterFacade();
//		List<String> inTopicTweets = new ArrayList<String>();
//		inTopicTweets.addAll( twitterCache.getLast200Tweets(27844479L) ); // BritishVogue
//		inTopicTweets.addAll( twitterCache.getLast200Tweets(153474021L) ); // MichaelKors
//		inTopicTweets.addAll( twitterCache.getLast200Tweets(14934818L) ); // InStyle		
//		inTopicTweets.addAll( twitterCache.getLast200Tweets(17133807L) ); // Grazia_Live		
//		inTopicTweets.addAll( twitterCache.getLast200Tweets(34258758L) ); // GlamourMagUK
//		
//		List<String> tweetsPolitics = new ArrayList<String>();
//
//	}
//	
//	private List<String> getPoliticsTweets()
//	{
//		List<String> screenNamesPolitics = new ArrayList<String>();
//		screenNamesPolitics.add("BarackObama");
//		screenNamesPolitics.add("msnbc");
//		screenNamesPolitics.add("chrislhayes");
//		screenNamesPolitics.add("huffingtonpost");
//		screenNamesPolitics.add("andersoncooper");
//		
//		List<Long> usersIdsPolitics = twitterCache.getUserIds(screenNamesPolitics);
//		List<String> tweetsPolitics = new ArrayList<String>();
//		for (Long userId : usersIdsPolitics)
//			tweetsPolitics.addAll(twitterCache.getLast200Tweets(userId));	
//		return tweetsPolitics;
//	}
//}
