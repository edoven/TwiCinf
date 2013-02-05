package lucene;

import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.Twitter4jWebFacade;
import it.cybion.influencers.twitter.web.TwitterWebFacade;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import twitter4j.TwitterException;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class LuceneTweetsAnalyzerMultiIndexes {
	
	
	public static void main(String[] args) throws IOException, ParseException, TwitterException {

		TwitterFacade twitterFacade = getTwitterFacade();
		
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(200557647L); //cuocopersonale
		usersIds.add(490182956L); //DarioBressanini
		usersIds.add(6832662L); //burde
//		usersIds.add(45168679L); //Ricette20
//		usersIds.add(76901354L); //spylong
//		usersIds.add(138387593L); //giuliagraglia
//		usersIds.add(136681167L); //ele_cozzella
//		usersIds.add(416427021L); //FilLaMantia
//		usersIds.add(444712353L); //ChiaraMaci
//		usersIds.add(472363994L); //LucaVissani
//		usersIds.add(7077572L); //maghetta
		
		List<String> usersDocuments = new ArrayList<String>();
		String userDocument = "";
		
		for (Long userId : usersIds) {
			List<String> userTweets = twitterFacade.getUpTo200Tweets(userId);
			for (String tweetJson : userTweets) {
				DBObject tweet = (DBObject) JSON.parse(tweetJson);
				String tweetText = (String)tweet.get("text");
				userDocument = userDocument + tweetText + " ";
			}
			usersDocuments.add(userDocument);
		}
		
		
		System.out.println("Index created.");
		
		List<String> tweets = new ArrayList<String>();
		usersIds = new ArrayList<Long>();
		usersIds.add(46118391L); //Fiordifrolla
		usersIds.add(272022405L); //Davide_Oltolini
		usersIds.add(9762312L); //toccodizenzero
		usersIds.add(96738439L); //oloapmarchi
		usersIds.add(57163636L); //fooders
		usersIds.add(57283474L); //GialloZafferano
		usersIds.add(191365206L); //giornaledelcibo
		for (Long userId : usersIds) {
			List<String> userTweets = twitterFacade.getUpTo200Tweets(userId);
			for (String tweetJson : userTweets) {
				DBObject tweet = (DBObject) JSON.parse(tweetJson);
				String tweetText = (String)tweet.get("text");
				tweetText = tweetText.replace("AND", "and");
				tweets.add(tweetText);
			}
		}
		
		
		getScores(usersDocuments, tweets);	
		
		System.exit(0);
	}
	
	
	private class MyDoc implements Comparable<MyDoc> {
		public String text;
		public float score;
		
		public MyDoc(String text, float score) {
			super();
			this.text = text;
			this.score = score;
		}
		
		@Override
		public int compareTo(MyDoc toCompare) {
			return Math.round( this.score - toCompare.score);

		}
	}

	public static void getScores(List<String> toIndexDocs, List<String> docs) throws IOException, ParseException {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

	    // 1. create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

	    IndexWriter w = new IndexWriter(index, config);
	    for (String toIndexDoc : toIndexDocs) {
		    Document document = new Document();
		    document.add(new TextField("content", toIndexDoc, Field.Store.YES));
		    w.addDocument(document);
	    }
	    w.close();
	    
	    List<MyDoc> rankedDocs = new ArrayList<MyDoc>();
	    
	    IndexReader reader = DirectoryReader.open(index);
	    for (String doc : docs) {
	    	String querystr = doc;
	    	Query query = new QueryParser(Version.LUCENE_40, "content", analyzer).parse(QueryParser.escape(querystr));
    	
	    	// 3. search
		    int hitsPerPage = 10;
		    
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(query, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		   
		    float totalRank = 0;
		    // 4. display results
		    for(int i=0;i<hits.length;++i)
		    	totalRank = totalRank + hits[i].score;
		    totalRank = totalRank / hits.length; //Avg
		    
		    rankedDocs.add(new LuceneTweetsAnalyzerMultiIndexes().new MyDoc(doc,  totalRank));   
		    
	    }
	    reader.close();
	    
	    Collections.sort(rankedDocs);
	    for (MyDoc rankedDoc : rankedDocs)
	    	System.out.println(rankedDoc.score+" - "+rankedDoc.text);
	    
	  }
	
	private static TwitterFacade getTwitterFacade() throws UnknownHostException {
		Token applicationToken = new Token("tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		Token userToken1 = new Token("tokens/token1.txt"); 
		userTokens.add(userToken1);
		Token userToken2 = new Token("tokens/token2.txt");
		userTokens.add(userToken2);
		Token userToken3 = new Token("tokens/token3.txt");
		userTokens.add(userToken3);
		Token userToken4 = new Token("tokens/token4.txt");
		userTokens.add(userToken4);
		Token userToken5 = new Token("tokens/token5.txt");
		userTokens.add(userToken5);
		Token userToken6 = new Token("tokens/token6.txt");
		userTokens.add(userToken6);
		
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "twitter");
		TwitterFacade twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}
  	
  
}