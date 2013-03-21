package it.cybion.influence.ranking.topic.lucene;


import it.cybion.influence.ranking.topic.lucene.enriching.UrlsExapandedTweetsTextExtractor;
import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
	private static final Logger logger = Logger.getLogger(TweetsIndexCreator.class);
	
	public static List<Directory> createSingleDocumentIndexesForUsers(TwitterCache twitterFacade, String indexesRootDirPath, List<Long> usersIds)
	{
		List<Directory> indexes = new ArrayList<Directory>();
		int indexesCount = 0;
		for (Long userId : usersIds)
		{
			logger.info("creating index "+(indexesCount++)+"/"+usersIds.size());
			String indexDir = indexesRootDirPath + "/" + userId;
			try
			{
				indexes.add(createSingleDocumentIndexForUser(twitterFacade, indexDir, userId));
			}
			catch (ProtectedUserException e)
			{
				logger.info("User protected. Skipped.");
			}
		}
		return indexes;
	}

	public static Directory createSingleDocumentIndexForUser(TwitterCache twitterFacade, String indexPath, long userId) throws ProtectedUserException
	{
		List<String> tweetsJsons = null;
		try
		{
			tweetsJsons = twitterFacade.getLast200Tweets(userId);
			logger.info("creating index for user with id "+userId+" with "+tweetsJsons.size()+" tweets");
		} catch (TwitterException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		List<String> tweetsTexts = new ArrayList<String>(UrlsExapandedTweetsTextExtractor.getUrlsExpandedTextFromTexts(tweetsJsons).values());
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
