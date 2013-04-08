package it.cybion.influencers.lucene;



import it.cybion.influence.ranking.topic.TweetToTopicDistanceCalculator;
import it.cybion.influence.ranking.topic.dictionary.DictionaryTweetToTopicDistanceCalculator;
import it.cybion.influence.ranking.topic.lucene.LuceneTweetToTopicDistanceCalculatorOLD;
import it.cybion.influence.ranking.utils.ListFileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.testng.Assert;
import org.testng.annotations.Test;



public class TweetToTopicRankerOLD_TEST
{

	@Test(enabled=false)
	public void test1() throws IOException
	{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		// index 1
		Directory index1 = new RAMDirectory();
		IndexWriter indexWriter1 = new IndexWriter(index1, new IndexWriterConfig(Version.LUCENE_36, analyzer));
		Document document = new Document();
		document.add(new Field("content", "un bellissimo tweet sul cibo e sulla cucina", Field.Store.YES, Field.Index.ANALYZED));
		indexWriter1.addDocument(document);
		indexWriter1.close();
		// index 2
		Directory index2 = new RAMDirectory();
		IndexWriter indexWriter2 = new IndexWriter(index2, new IndexWriterConfig(Version.LUCENE_36, analyzer));
		document = new Document();
		document.add(new Field("content", "la nuovissima ricetta della pasta con pere, pecorino e ananas", Field.Store.YES, Field.Index.ANALYZED));
		indexWriter2.addDocument(document);
		indexWriter2.close();

		// TweetToTopicRanker
		List<Directory> indexes = new ArrayList<Directory>();
		indexes.add(index1);
		indexes.add(index2);
		LuceneTweetToTopicDistanceCalculatorOLD ranker = new LuceneTweetToTopicDistanceCalculatorOLD(indexes);
		String tweet1 = "un tweet sulla pasta, il cibo migliore che esista";
		String tweet2 = "la ricetta della torta fragole, peperoni e salmone";
		String tweet3 = "testo senza senso";
		Assert.assertTrue(ranker.getTweetToTopicDistance(tweet1) > 0);
		Assert.assertTrue(ranker.getTweetToTopicDistance(tweet2) > 0);
		Assert.assertTrue(ranker.getTweetToTopicDistance(tweet3) == 0);
	}
	
	@Test
	public void speedTest()
	{
		List<String> dictionary = new ArrayList<String>();
		dictionary.add("fashion");
		dictionary.add("clothes");
		dictionary.add("boots");
		dictionary.add("jacket");
		dictionary.add("hello");
		dictionary.add("buzzword1");
		dictionary.add("buzzword2");
		dictionary.add("buzzword3");
		dictionary.add("buzzword4");
		dictionary.add("buzzword5");
		dictionary.add("buzzword6");
		TweetToTopicDistanceCalculator topicDistanceCalculator = new DictionaryTweetToTopicDistanceCalculator(dictionary);
		List<String> tweets = new ArrayList<String>();
		tweets.add("I just backed The Veronica Mars Movie Project on @Kickstarter  The Veronica Mars Movie Project by Rob Thomas — Kickstarter");
		tweets.add("PLEDGE PEOPLE!! The Veronica Mars Movie Project by Rob Thomas — Kickstarter  via @kickstarter The Veronica Mars Movie Project by Rob Thomas — Kickstarter");
		tweets.add("Anyone got any good sunburn remedys? Had a bit if a sunscreen fail yesterday :(");
		tweets.add("RT @tarazzhq: Beauty lovers out there please meet our new partner retailer - Sephora! Shop here  Sephora - Tarazz");
		tweets.add("My life is now complete cause snowflakes are back @punkologist   My life is now complete cause snowflakes are back @punkologist");
		tweets.add("Skirts over trousers: it's happening people! #joseph #love  Skirts over trousers: it's happening people! #joseph #love");
		tweets.add("Getting excited for @thegigcompany an evening with @Suggsmcpherson &amp; friends for @pancreaticcanuk 14/3/2013 #joinus #musicnews #joolsholland");
		tweets.add("empire state #mandatory #insta #nyc  empire state #mandatory #insta #nyc");
		tweets.add("Parker loves the water. Matches his eyes ;) #parkie #2of3  Parker loves the water. Matches his eyes ;) #parkie #2of3");
		tweets.add("Future electrician??? Children's Museum. Aka. Anxiety attack.  Future electrician??? Children's Museum. Aka. Anxiety attack.");
		tweets.add("The before. (Angel face) 13.2 #crossfit #open #wod  The before. (Angel face) 13.2 #crossfit #open #wod");
		tweets.add("Thanks a lot @alexgrav - my DiggIn is tainted. #sonyerds  Thanks a lot @alexgrav - my DiggIn is tainted. #sonyerds");
		tweets.add("Yesssss! more things i can borrowRT @LindsayKordik: I just ordered a skirt and a girly dress. What's happening to my brand? (@alwaysinstyle)");
		tweets.add("Just call me Ivanka. #furban #fur #turban  Just call me Ivanka. #furban #fur #turban");
		tweets.add("A mixed leather jacket just came down the runway I nearly peed my pants for. @VivienneTam");
		tweets.add("Google Introduces Tool to Calculate Mobile ROI  Google Introduces Tool to Calculate Mobile ROI");
		tweets.add("Foursquare Replacing Check-in Data With Data From APIs, Crowley Indicates  Foursquare Replacing Check-in Data With Data From APIs, Crowley Indicates - SocialTimes");
		tweets.add("8 Ways Your Content Strategy Should Change With the New Facebook News Feed  8 Ways Your Content Strategy Should Change With the New Facebook News Feed");
		tweets.add("I just backed The Veronica Mars Movie Project on @Kickstarter  The Veronica Mars Movie Project by Rob Thomas — Kickstarter");
		tweets.add("PLEDGE PEOPLE!! The Veronica Mars Movie Project by Rob Thomas — Kickstarter  via @kickstarter The Veronica Mars Movie Project by Rob Thomas — Kickstarter");
		tweets.add("Anyone got any good sunburn remedys? Had a bit if a sunscreen fail yesterday :(");
		tweets.add("RT @tarazzhq: Beauty lovers out there please meet our new partner retailer - Sephora! Shop here  Sephora - Tarazz");
		tweets.add("My life is now complete cause snowflakes are back @punkologist   My life is now complete cause snowflakes are back @punkologist");
		tweets.add("Skirts over trousers: it's happening people! #joseph #love  Skirts over trousers: it's happening people! #joseph #love");
		tweets.add("Getting excited for @thegigcompany an evening with @Suggsmcpherson &amp; friends for @pancreaticcanuk 14/3/2013 #joinus #musicnews #joolsholland");
		tweets.add("empire state #mandatory #insta #nyc  empire state #mandatory #insta #nyc");
		tweets.add("Parker loves the water. Matches his eyes ;) #parkie #2of3  Parker loves the water. Matches his eyes ;) #parkie #2of3");
		tweets.add("Future electrician??? Children's Museum. Aka. Anxiety attack.  Future electrician??? Children's Museum. Aka. Anxiety attack.");
		tweets.add("The before. (Angel face) 13.2 #crossfit #open #wod  The before. (Angel face) 13.2 #crossfit #open #wod");
		tweets.add("Thanks a lot @alexgrav - my DiggIn is tainted. #sonyerds  Thanks a lot @alexgrav - my DiggIn is tainted. #sonyerds");
		tweets.add("Yesssss! more things i can borrowRT @LindsayKordik: I just ordered a skirt and a girly dress. What's happening to my brand? (@alwaysinstyle)");
		tweets.add("Just call me Ivanka. #furban #fur #turban  Just call me Ivanka. #furban #fur #turban");
		tweets.add("A mixed leather jacket just came down the runway I nearly peed my pants for. @VivienneTam");
		tweets.add("Google Introduces Tool to Calculate Mobile ROI  Google Introduces Tool to Calculate Mobile ROI");
		tweets.add("Foursquare Replacing Check-in Data With Data From APIs, Crowley Indicates  Foursquare Replacing Check-in Data With Data From APIs, Crowley Indicates - SocialTimes");
		tweets.add("8 Ways Your Content Strategy Should Change With the New Facebook News Feed  8 Ways Your Content Strategy Should Change With the New Facebook News Feed");
		tweets.add("I just backed The Veronica Mars Movie Project on @Kickstarter  The Veronica Mars Movie Project by Rob Thomas — Kickstarter");
		tweets.add("PLEDGE PEOPLE!! The Veronica Mars Movie Project by Rob Thomas — Kickstarter  via @kickstarter The Veronica Mars Movie Project by Rob Thomas — Kickstarter");
		tweets.add("Anyone got any good sunburn remedys? Had a bit if a sunscreen fail yesterday :(");
		tweets.add("RT @tarazzhq: Beauty lovers out there please meet our new partner retailer - Sephora! Shop here  Sephora - Tarazz");
		tweets.add("My life is now complete cause snowflakes are back @punkologist   My life is now complete cause snowflakes are back @punkologist");
		tweets.add("Skirts over trousers: it's happening people! #joseph #love  Skirts over trousers: it's happening people! #joseph #love");
		tweets.add("Getting excited for @thegigcompany an evening with @Suggsmcpherson &amp; friends for @pancreaticcanuk 14/3/2013 #joinus #musicnews #joolsholland");
		tweets.add("empire state #mandatory #insta #nyc  empire state #mandatory #insta #nyc");
		tweets.add("Parker loves the water. Matches his eyes ;) #parkie #2of3  Parker loves the water. Matches his eyes ;) #parkie #2of3");
		tweets.add("Future electrician??? Children's Museum. Aka. Anxiety attack.  Future electrician??? Children's Museum. Aka. Anxiety attack.");
		tweets.add("The before. (Angel face) 13.2 #crossfit #open #wod  The before. (Angel face) 13.2 #crossfit #open #wod");
		tweets.add("Thanks a lot @alexgrav - my DiggIn is tainted. #sonyerds  Thanks a lot @alexgrav - my DiggIn is tainted. #sonyerds");
		tweets.add("Yesssss! more things i can borrowRT @LindsayKordik: I just ordered a skirt and a girly dress. What's happening to my brand? (@alwaysinstyle)");
		tweets.add("Just call me Ivanka. #furban #fur #turban  Just call me Ivanka. #furban #fur #turban");
		tweets.add("A mixed leather jacket just came down the runway I nearly peed my pants for. @VivienneTam");
		tweets.add("Google Introduces Tool to Calculate Mobile ROI  Google Introduces Tool to Calculate Mobile ROI");
		tweets.add("Foursquare Replacing Check-in Data With Data From APIs, Crowley Indicates  Foursquare Replacing Check-in Data With Data From APIs, Crowley Indicates - SocialTimes");
		tweets.add("8 Ways Your Content Strategy Should Change With the New Facebook News Feed  8 Ways Your Content Strategy Should Change With the New Facebook News Feed");
		tweets.add("I just backed The Veronica Mars Movie Project on @Kickstarter  The Veronica Mars Movie Project by Rob Thomas — Kickstarter");
		tweets.add("PLEDGE PEOPLE!! The Veronica Mars Movie Project by Rob Thomas — Kickstarter  via @kickstarter The Veronica Mars Movie Project by Rob Thomas — Kickstarter");
		tweets.add("Anyone got any good sunburn remedys? Had a bit if a sunscreen fail yesterday :(");
		tweets.add("RT @tarazzhq: Beauty lovers out there please meet our new partner retailer - Sephora! Shop here  Sephora - Tarazz");
		tweets.add("My life is now complete cause snowflakes are back @punkologist   My life is now complete cause snowflakes are back @punkologist");
		tweets.add("Skirts over trousers: it's happening people! #joseph #love  Skirts over trousers: it's happening people! #joseph #love");
		tweets.add("Getting excited for @thegigcompany an evening with @Suggsmcpherson &amp; friends for @pancreaticcanuk 14/3/2013 #joinus #musicnews #joolsholland");
		tweets.add("empire state #mandatory #insta #nyc  empire state #mandatory #insta #nyc");
		tweets.add("Parker loves the water. Matches his eyes ;) #parkie #2of3  Parker loves the water. Matches his eyes ;) #parkie #2of3");
		tweets.add("Future electrician??? Children's Museum. Aka. Anxiety attack.  Future electrician??? Children's Museum. Aka. Anxiety attack.");
		tweets.add("The before. (Angel face) 13.2 #crossfit #open #wod  The before. (Angel face) 13.2 #crossfit #open #wod");
		tweets.add("Thanks a lot @alexgrav - my DiggIn is tainted. #sonyerds  Thanks a lot @alexgrav - my DiggIn is tainted. #sonyerds");
		tweets.add("Yesssss! more things i can borrowRT @LindsayKordik: I just ordered a skirt and a girly dress. What's happening to my brand? (@alwaysinstyle)");
		tweets.add("Just call me Ivanka. #furban #fur #turban  Just call me Ivanka. #furban #fur #turban");
		tweets.add("A mixed leather jacket just came down the runway I nearly peed my pants for. @VivienneTam");
		tweets.add("Google Introduces Tool to Calculate Mobile ROI  Google Introduces Tool to Calculate Mobile ROI");
	
		long start = System.currentTimeMillis();
		for (String string : tweets)
		{
			topicDistanceCalculator.getTweetToTopicDistance(string);
		}
		long end = System.currentTimeMillis();
		System.out.println("Time="+(end-start));
		
		
	}
	
	
	
}
