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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.jsoup.Jsoup;

import twitter4j.TwitterException;

import com.google.gson.Gson;


public class LuceneTweetsAnalyzerSingleDocument {
	
	
	public static void main(String[] args) throws IOException, ParseException, TwitterException {

		TwitterFacade twitterFacade = getTwitterFacade();
		
		List<Long> indexedUsers = new ArrayList<Long>();
		indexedUsers.add(200557647L); //cuocopersonale
		indexedUsers.add(490182956L); //DarioBressanini
//		indexedUsers.add(6832662L); //burde
		
		List<Long> testUsers = new ArrayList<Long>();
		testUsers.add(46118391L); //Fiordifrolla
//		testUsers.add(272022405L); //Davide_Oltolini
//		testUsers.add(9762312L); //toccodizenzero

		Set<String> urls = new HashSet<String>();
		
		for (Long userId : indexedUsers) {
			Gson gson = new Gson();	
			List<String> userTweets = twitterFacade.getUpTo200Tweets(userId);
			for (String tweetJson : userTweets) {
				Tweet tweet = gson.fromJson(tweetJson, Tweet.class);		
				for (Tweet.Url url : tweet.entities.urls)
					urls.add(url.expanded_url);
			}
		}
		
		for (Long userId : testUsers) {
			Gson gson = new Gson();	
			List<String> userTweets = twitterFacade.getUpTo200Tweets(userId);
			for (String tweetJson : userTweets) {
				Tweet tweet = gson.fromJson(tweetJson, Tweet.class);		
				for (Tweet.Url url : tweet.entities.urls)
					urls.add(url.expanded_url);
			}
		}
		
		Map<String,String> urls2Titles = MultithreadUrlsTitleExtractor.getTitles(new ArrayList<String>(urls));
		for (String url : urls2Titles.keySet())
			System.out.println(url + " - " + urls2Titles.get(url));
		
		

		
		String tweetsString = "";
		
		int userCount = 1;
		for (Long userId : indexedUsers) {
			List<String> userTweets = twitterFacade.getUpTo200Tweets(userId);
			int tweetsCount = 1;
			for (String tweetJson : userTweets) {
				System.out.println("("+userCount+indexedUsers.size() + ")" +tweetsCount + "/" +userTweets.size());
				String tweetText = getUrlExpandedTweet(tweetJson);
				tweetsString = tweetsString + tweetText + " ";
				tweetsCount++;
			}
			userCount++;
		}
		
		System.out.println("Index created.");
		
		List<String> tweets = new ArrayList<String>();
		
		for (Long userId : testUsers) {
			List<String> userTweets = twitterFacade.getUpTo200Tweets(userId);
			for (String tweetJson : userTweets) {
				String tweetText = getUrlExpandedTweet(tweetJson);
				tweetText = tweetText.replace("AND", "and");
				tweets.add(tweetText);
			}
		}
		getScores(tweetsString, tweets);	
		
		System.exit(0);
	}
	
	
	private static String getUrlExpandedTweet(String tweetJson) {
		Gson gson = new Gson();	
		Tweet tweet = gson.fromJson(tweetJson, Tweet.class);		
		String tweetText = tweet.text.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
		for (Tweet.Url url : tweet.entities.urls)
			tweetText = tweetText + " " + getTitleFromUrl(url.expanded_url);
		return tweetText;	
	}
	
	private static String getTitleFromUrl(String urlString) {		
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(urlString).get();
			return doc.getElementsByTag("title").text();
		} catch (IOException e) {
			return "";
		}		
	}
	
	private class Tweet {		
		public String text;
		public Entities entities;	
		
		public class Url { String expanded_url;}
		
		public class Entities {
			List<Url> urls;
		}			
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
			if (this.score>=toCompare.score)
				return -1;
			else
				return 1;
		}
	}

	public static void getScores(String toIndexDoc, List<String> docs) throws IOException, ParseException {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

	    // 1. create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

	    IndexWriter w = new IndexWriter(index, config);
	    Document document = new Document();
	    document.add(new TextField("content", toIndexDoc, Field.Store.YES));
	    w.addDocument(document);
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
		   
		    
		    // 4. display results
		    for(int i=0;i<hits.length;++i)
		    	rankedDocs.add(new LuceneTweetsAnalyzerSingleDocument().new MyDoc(doc,  hits[i].score));   
		    
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