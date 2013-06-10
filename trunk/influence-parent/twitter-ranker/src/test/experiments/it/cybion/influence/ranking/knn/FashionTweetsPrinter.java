package it.cybion.influence.ranking.knn;

import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
import it.cybion.influencers.ranking.model.Tweet;
import it.cybion.influencers.ranking.topic.knn.KnnTopicScorer;
import it.cybion.influencers.ranking.utils.TweetsDeserializer;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.TwitterException;
import utils.TwitterFacadeFactory;

public class FashionTweetsPrinter
{
	public static void main(String[] args) throws UnknownHostException, TwitterException, ProtectedUserException
	{		
		getUsersIds();
		
		int groupSize = 100;
		
		TwitterCache twitterCache = TwitterFacadeFactory.getTwitterFacade();
		List<String> fashionTweets = getFashionTweets(twitterCache);
		List<String> fashionTweetsGroups = new ArrayList<String>();
		Collections.shuffle(fashionTweets);
		int groups = fashionTweets.size()/groupSize;
		for (int i=0; i<groups; i++)
		{
			List<String> group = fashionTweets.subList(i*groupSize, (i+1)*groupSize);
			String groupString = "";
			for (String tweet : group)
				groupString = groupString + " " + tweet;
			fashionTweetsGroups.add(groupString);
		}
		System.out.println("fashionTweetsGroups="+fashionTweetsGroups.size());
//		for (int i=0; i<fashionTweetsGroups.size(); i++)
//			System.out.println(i+"-"+fashionTweetsGroups.get(i));
		
		
		List<String> technologyTweets = getTechnologyTweets(twitterCache);
		List<String> technologyTweetsGroups = new ArrayList<String>();
		Collections.shuffle(technologyTweets);
		groups = technologyTweets.size()/groupSize;
		for (int i=0; i<groups; i++)
		{
			List<String> group = technologyTweets.subList(i*groupSize, (i+1)*groupSize);
			String groupString = "";
			for (String tweet : group)
				groupString = groupString + " " + tweet;
			technologyTweetsGroups.add(groupString);
		}
		System.out.println("technologyTweetsGroups="+technologyTweetsGroups.size());
//		for (int i=0; i<technologyTweetsGroups.size(); i++)
//			System.out.println(i+"-"+technologyTweetsGroups.get(i));
		
		KnnTopicScorer topicDistanceCalculator = new KnnTopicScorer(fashionTweetsGroups,
																										  technologyTweetsGroups,
																										  10);
		
		
//		topicDistanceCalculator.printKnn("this new computer really sucks because it has windows and not linux");
//		topicDistanceCalculator.printKnn("look at my new shirt!");
//		topicDistanceCalculator.printKnn("Racerback mini dresses with front/back zips etc. I love the yellow lace number at Alex. Would've loved to see a variety of silhouette");
//		topicDistanceCalculator.printKnn("Should sleep. Can't. What to do.");
//		topicDistanceCalculator.printKnn("My name is Nadia and I'm addicted to Instagram.");
//		
//		topicDistanceCalculator.printKnn("External Zip drive (or shall I say zipper) story at Alex Perry. cute slick short dresses but there were way too many of em. Edit please?");
//		topicDistanceCalculator.printKnn("Worked out 3 times, submitted taxes, did laundry, went grocery shopping AND planned sister's bridal shower...productive weekend!! ");
//		topicDistanceCalculator.printKnn("Totally inspired by this! RT Man works a volunteer job for every paying one");
//		topicDistanceCalculator.printKnn("We need to inflate a new bubble, finance wasn't enough? Rumor has Google close to buying WhatsApp for $1B");
//		topicDistanceCalculator.printKnn("Beware of the bubble! The Basics on Bitcoin: 11 Things to Know About This Suddenly 'Hot' Digital Currency");
//		topicDistanceCalculator.printKnn("Zuck on communication, funny how the home is there: a real way of ending your privacy. But hey, it's free and blue!");
//		topicDistanceCalculator.printKnn("Hack Skype, acquire bitcoins. This is genius!");
//		topicDistanceCalculator.printKnn("Forbes in 1998: Paul Krugman Column Perfectly Explains The Design Flaw At The Heart Of Bitcoin");
//		topicDistanceCalculator.printKnn("Please not 1999 again! Stop browser wars and hyper-paid software (30M Yahoo acquisition), we don't need bubbles!");
		
		
//		DianaMadison - 70883897
		List<String> fashionTweetsToTest = getUrlsEnrichedTweetsTexts(70883897,twitterCache);
		for (String tweet : fashionTweetsToTest)
			topicDistanceCalculator.printKnn(tweet);
		
//		TechCrunch - 816653	
		List<String> technologyTweetsToTest = getUrlsEnrichedTweetsTexts(816653,twitterCache);
		for (String tweet : technologyTweetsToTest)
			topicDistanceCalculator.printKnn(tweet);
		
//		KnowledgeBishop - 117477071
		List<String> businessTweetsToTest = getUrlsEnrichedTweetsTexts(117477071,twitterCache);
		for (String tweet : businessTweetsToTest)
			topicDistanceCalculator.printKnn(tweet);
			
		System.exit(0);
	}
	
	
	public static List<String> getFashionTweets(TwitterCache twitterCache)
	{
		List<String> fashionTweets = new ArrayList<String>();
		try
		{
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(27844479, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(153474021, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(15933910, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(19967728, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(14934818, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(18138157, twitterCache));
			
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(18423808, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(105189289, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(64822927, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(20025220, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(15170087, twitterCache));
			fashionTweets.addAll(getUrlsEnrichedTweetsTexts(14241704, twitterCache));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		return fashionTweets;
	}
	
	
	//	TECHNOLOGY
	//	davewiner - 3839
	//	benparr - 11009852
	//	bill_gross - 47539748
	//	mashable - 972651
	//	timoreilly - 2384071
	//	realdanlyons - 15481606
	//	_robin_sharma - 26244629
	//	2morrowknight - 19478383
	//	AnnTran_ - 23135992
	//	ckburgess - 25911549
	//	claychristensen - 60308942
	//	danielnewmanUV - 228918333
	public static List<String> getTechnologyTweets(TwitterCache twitterCache)
	{
		List<String> technologyTweets = new ArrayList<String>();
		try
		{
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(3839, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(11009852, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(47539748, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(972651, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(2384071, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(15481606, twitterCache));	
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(26244629, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(19478383, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(23135992, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(25911549, twitterCache));
			technologyTweets.addAll(getUrlsEnrichedTweetsTexts(60308942, twitterCache));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		return technologyTweets;
	}
	
	
	
	
	
	public static List<String> getUrlsEnrichedTweetsTexts(long usersId, TwitterCache twitterCache) throws TwitterException, ProtectedUserException
	{
		List<String> userTweets = new ArrayList<String>();
		List<String> jsonTweets = twitterCache.getLast200Tweets(usersId);
		List<Tweet> tweets = TweetsDeserializer.getTweetsObjectsFromJsons(jsonTweets);
//		tweets = UrlsExapandedTweetsTextExtractor.getUrlsExpandedTextTweets(tweets);
		for (Tweet tweet : tweets)
		{
//			String text = tweet.urlsExpandedText;
			String text = tweet.text;
			text = text.replace("#", " ");
			text = text.replace("@", " ");
			userTweets.add(text);
		}
		return userTweets;
	}
	
	
	public static void getUsersIds() throws UnknownHostException, TwitterException
	{
		TwitterCache twitterCache = TwitterFacadeFactory.getTwitterFacade();
		System.out.println("TechCrunch - "+twitterCache.getUserId("TechCrunch"));
//		System.out.println("2morrowknight - "+twitterCache.getUserId("2morrowknight"));
//		System.out.println("AnnTran_ - "+twitterCache.getUserId("AnnTran_"));
//		System.out.println("ckburgess - "+twitterCache.getUserId("ckburgess"));
//		System.out.println("claychristensen - "+twitterCache.getUserId("claychristensen"));
//		System.out.println("danielnewmanUV - "+twitterCache.getUserId("danielnewmanUV"));
	}
	
}
