package it.cybion.influence.ranking.topic.knn;

import it.cybion.influence.ranking.topic.TopicScorer;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;


public class KnnTopicScorer implements TopicScorer
{	
	private static final Logger logger = Logger.getLogger(KnnTopicScorer.class);
	
	private static final String LUCENE_ESCAPE_CHARS = "[\\\\+\\-\\!\\(\\)\\:\\^\\]\\{\\}\\~\\*\\?]";
	private static final Pattern LUCENE_PATTERN = Pattern.compile(LUCENE_ESCAPE_CHARS);
	private static final String REPLACEMENT_STRING = "\\\\$0";
	
	private Directory index;
	private IndexReader indexReader;
	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	private int k;
	
	public KnnTopicScorer(List<String> topicTweets,
											 List<String> outOfTopicTweets,
											 int k)
	{	
		index = createIndex(topicTweets, outOfTopicTweets);
		this.k = k;
		try
		{
			indexReader = IndexReader.open(index);
		}
		catch (CorruptIndexException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	
	public void printKnn(String tweetText)
	{
//		logger.info("#### printKnn for tweet: "+tweetText);
		Query query = null;
		QueryParser queryParser = new QueryParser(Version.LUCENE_36, "content", analyzer);
		String cleanedTweetText = getCleanedTweetText(tweetText);
		try
		{
			query = queryParser.parse(cleanedTweetText);
		} catch (ParseException e1)
		{
			logger.info("Parsing error! Can't parse: "+cleanedTweetText);
			return;
		}
		int hitsPerPage = k;
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		try
		{
			searcher.search(query, collector);
		} catch (IOException e)
		{
			e.printStackTrace();
			logger.info("Problem with searcher.search(query, collector). Query=" + query);
		}
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		try
		{
			searcher.close();
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		if (hits.length == 0)
			logger.info("no results for this query");
		else
		{
			int inTopicCount = 0,
				outOfTopicCount = 0;
			
			for (int i=0; i<hits.length; i++)
			{
				ScoreDoc scoreDoc = hits[i];
				int docId = scoreDoc.doc;
				Document document = null;
				try
				{
					document = indexReader.document(docId);
					String inTopicString = document.get("inTopic");
//					logger.info(inTopicString+" - "+document.get("content"));
					if (inTopicString.equals("true"))
						inTopicCount++;
					else
						outOfTopicCount++;
				}
				catch (CorruptIndexException e)
				{
					e.printStackTrace();
					System.exit(0);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			}
			logger.info("("+inTopicCount+"-"+outOfTopicCount+") "+tweetText);
//			logger.info("inTopicCount="+inTopicCount);
//			logger.info("outOfTopicCount="+outOfTopicCount);
		}
	}
	

	
	private static Directory createIndex(List<String> topicTweets, 
										 List<String> outOfTopicTweets)
	{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		Directory index = null;
		index = new RAMDirectory(); 
		IndexWriter indexWriter = null;
		try
		{
			indexWriter = new IndexWriter(index, config);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		for (String topicTweet : topicTweets)
			addInTopicDocument(indexWriter, topicTweet);
		for (String outOfTopicTweet : outOfTopicTweets)
			addOutOfTopicDocument(indexWriter, outOfTopicTweet);
		try
		{
			indexWriter.close();
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		return index;
	}

	private static void addInTopicDocument(IndexWriter indexWriter, String tweet)
	{
		Document document = new Document();
		document.add(new Field("content", tweet, Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("inTopic", "true", Field.Store.YES, Field.Index.NOT_ANALYZED));
		try
		{
			indexWriter.addDocument(document);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static void addOutOfTopicDocument(IndexWriter indexWriter, String tweet)
	{
		Document document = new Document();
		document.add(new Field("content", tweet, Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("inTopic", "false", Field.Store.YES, Field.Index.NOT_ANALYZED));
		try
		{
			indexWriter.addDocument(document);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	private String getCleanedTweetText(String originalTweetText)
	{
		String cleanedTweet = LUCENE_PATTERN.matcher(originalTweetText).replaceAll(REPLACEMENT_STRING);
		cleanedTweet = QueryParser.escape(cleanedTweet);
		return cleanedTweet;
	}



	@Override
	public float getTweetToTopicDistance(String tweetText)
	{
		ScoreDoc[] hits = getRetrievedDocs(tweetText);		
		if (hits.length == 0)
			return 0;
		int inTopicCount = 0;		
		for (int i=0; i<hits.length; i++)
		{
			ScoreDoc scoreDoc = hits[i];
			int docId = scoreDoc.doc;
			Document document = null;
			try
			{
				document = indexReader.document(docId);
				String inTopicString = document.get("inTopic");
				if (inTopicString.equals("true"))
					inTopicCount++;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return 0;
			}
		}
		if (0==inTopicCount)
			return 0;
		else
			return inTopicCount/hits.length;
	}
	
	private ScoreDoc[] getRetrievedDocs(String tweetText)
	{
		Query query = null;
		QueryParser queryParser = new QueryParser(Version.LUCENE_36, "content", analyzer);
		String cleanedTweetText = getCleanedTweetText(tweetText);
		try
		{
			query = queryParser.parse(cleanedTweetText);
		} catch (ParseException e1)
		{
			logger.info("Parsing error! Can't parse: "+cleanedTweetText);
			return new ScoreDoc[0];
		}
		int hitsPerPage = k;
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		try
		{
			searcher.search(query, collector);
		} catch (IOException e)
		{
			e.printStackTrace();
			logger.info("Problem with searcher.search(query, collector). Query=" + query);
		}
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		try
		{
			searcher.close();
		} catch (IOException e)
		{
			e.printStackTrace();;
		}
		return hits;
	}
}
