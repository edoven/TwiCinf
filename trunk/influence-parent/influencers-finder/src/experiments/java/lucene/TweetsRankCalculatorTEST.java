package lucene;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.store.Directory;

import twitter4j.TwitterException;

import it.cybion.influencers.lucene.TweetToTopicSimilarityCalculator;
import it.cybion.influencers.lucene.TweetsIndexCreator;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.TwitterFacadeFactory;
import it.cybion.influencers.utils.TweetsTextExtractor;

public class TweetsRankCalculatorTEST {
	
	public static void main(String[] args) throws TwitterException, UnknownHostException {
		long userToIndex1 = 200557647; //cuocopersonale
		long userToIndex2 = 490182956; //DarioBressanini
		long userToIndex3 = 6832662; //burde
		
		TwitterFacade twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		
		List<String> tweetsJsonUserToIndex1 = twitterFacade.getUpTo200Tweets(userToIndex1);
		List<String> tweetsJsonUserToIndex2 = twitterFacade.getUpTo200Tweets(userToIndex2);
		List<String> tweetsJsonUserToIndex3 = twitterFacade.getUpTo200Tweets(userToIndex3);
		
		List<String> tweetsTextsUser1 = TweetsTextExtractor.getUrlsExpandedText(tweetsJsonUserToIndex1);
		List<String> tweetsTextsUser2 = TweetsTextExtractor.getUrlsExpandedText(tweetsJsonUserToIndex2);
		List<String> tweetsTextsUser3 = TweetsTextExtractor.getUrlsExpandedText(tweetsJsonUserToIndex3);
		
		Directory index1 = TweetsIndexCreator.createSingleDocumentIndex("lucene/index1", tweetsTextsUser1);
		Directory index2 = TweetsIndexCreator.createSingleDocumentIndex("lucene/index2", tweetsTextsUser2);
		Directory index3 = TweetsIndexCreator.createSingleDocumentIndex("lucene/index3", tweetsTextsUser3);
		List<Directory> indexes = new ArrayList<Directory>();
		indexes.add(index1);
		indexes.add(index2);
		indexes.add(index3);
		
		TweetToTopicSimilarityCalculator similarityCalculator = new TweetToTopicSimilarityCalculator(indexes);
		
		
		long userToCalculate1 = 200557647; //cuocopersonale		
		List<String> tweetsJsonUserToCalculate1 = twitterFacade.getUpTo200Tweets(userToCalculate1);		
		List<String> tweetsTextsUserToCalculate1 = TweetsTextExtractor.getUrlsExpandedText(tweetsJsonUserToCalculate1);
		for (String tweetText : tweetsTextsUserToCalculate1)
			System.out.println(similarityCalculator.getTweetRank(tweetText)+ " - " + tweetText);
		
		System.exit(0);
	}
	
}
