package it.cybion.influencers.ranking.topic.lucene;


import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.TwitterFacadeFactory;
import it.cybion.influencers.utils.text_extractor.TweetsTextExtractor;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import twitter4j.TwitterException;



public class TweetsIndexCreator
{

	public static List<Directory> createSingleDocumentIndexesForUsers(String indexesRootDirPath, List<Long> usersIds)
	{
		List<Directory> indexes = new ArrayList<Directory>();
		for (Long userId : usersIds)
		{
			String indexDir = indexesRootDirPath + "/" + userId;
			indexes.add(createSingleDocumentIndexForUser(indexDir, userId));
		}
		return indexes;
	}

	public static List<Directory> createSingleDocumentIndexesForUsers(TwitterFacade twitterFacade, String indexesRootDirPath, List<Long> usersIds)
	{
		List<Directory> indexes = new ArrayList<Directory>();
		for (Long userId : usersIds)
		{
			String indexDir = indexesRootDirPath + "/" + userId;
			indexes.add(createSingleDocumentIndexForUser(twitterFacade, indexDir, userId));
		}
		return indexes;
	}

	public static Directory createSingleDocumentIndexForUser(String indexPath, long userId)
	{
		TwitterFacade twitterFacade = null;
		try
		{
			twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		List<String> tweetsJsons = null;
		try
		{
			tweetsJsons = twitterFacade.getUpTo200Tweets(userId);
		} catch (TwitterException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		List<String> tweetsTexts = TweetsTextExtractor.getUrlsExpandedText(tweetsJsons);
		return createSingleDocumentIndex(indexPath, tweetsTexts);
	}

	public static Directory createSingleDocumentIndexForUser(TwitterFacade twitterFacade, String indexPath, long userId)
	{
		List<String> tweetsJsons = null;
		try
		{
			tweetsJsons = twitterFacade.getUpTo200Tweets(userId);
		} catch (TwitterException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		List<String> tweetsTexts = TweetsTextExtractor.getUrlsExpandedText(tweetsJsons);
		return createSingleDocumentIndex(indexPath, tweetsTexts);
	}

	public static Directory createSingleDocumentIndex(String indexPath, List<String> tweets)
	{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		Directory index = null;
		try
		{
			index = new SimpleFSDirectory(new File(indexPath));
		} catch (IOException e1)
		{
			e1.printStackTrace();
			System.exit(0);
		}
		IndexWriter indexWriter = null;
		try
		{
			indexWriter = new IndexWriter(index, config);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		for (String tweet : tweets)
		{
			addDocument(indexWriter, tweet);
		}
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

	private static void addDocument(IndexWriter indexWriter, String tweet)
	{
		Document document = new Document();
		document.add(new Field("content", tweet, Field.Store.YES, Field.Index.ANALYZED));
		try
		{
			indexWriter.addDocument(document);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

}
