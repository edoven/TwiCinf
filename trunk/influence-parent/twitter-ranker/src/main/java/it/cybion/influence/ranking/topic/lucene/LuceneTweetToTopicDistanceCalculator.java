package it.cybion.influence.ranking.topic.lucene;


import it.cybion.influence.ranking.topic.TweetToTopicDistanceCalculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;


/**
 * 
 * TweetToTopicSimilarityCalculator uses a list of Lucene indexes to calculate a
 * tweet similarity to a topic. Each index is based on a single document created
 * by the concatenation of all tweets of a single user, than one index for every
 * user.
 * 
 */

public class LuceneTweetToTopicDistanceCalculator implements TweetToTopicDistanceCalculator
{
	

	private static final String LUCENE_ESCAPE_CHARS = "[\\\\+\\-\\!\\(\\)\\:\\^\\]\\{\\}\\~\\*\\?]";
	private static final Pattern LUCENE_PATTERN = Pattern.compile(LUCENE_ESCAPE_CHARS);
	private static final String REPLACEMENT_STRING = "\\\\$0";

	private static final Logger logger = Logger.getLogger(LuceneTweetToTopicDistanceCalculator.class);

	private List<IndexReader> indexesReaders;
	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);

	public LuceneTweetToTopicDistanceCalculator(List<Directory> indexes)
	{	
		indexesReaders = new ArrayList<IndexReader>();
		for (Directory index : indexes)
		{
			IndexReader indexReader = null;
			try
			{
				indexReader = IndexReader.open(index);
			} catch (IOException e)
			{
				e.printStackTrace();
				logger.info("Problem with index: " + index.toString());
				System.exit(0);
			}
			indexesReaders.add(indexReader);
		}
	}

	@Override
	public float getTweetToTopicDistance(String tweetText)
	{
		float accumulator = 0;
		for (IndexReader indexReader : indexesReaders)
		{
			accumulator = accumulator + getTweetToTopicDistanceFromOneIndex(indexReader, tweetText);
		}
		float totalScore = accumulator / indexesReaders.size();
//		logger.info("score="+totalScore+" - tweet="+tweet);
		return totalScore;
	}

	private float getTweetToTopicDistanceFromOneIndex(IndexReader indexReader, String tweetText)
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
			return 0;
		}
		int hitsPerPage = 1;
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
		float score;
		if (hits.length == 0)
			score = 0;
		else
			score = hits[0].score;
		return score;

	}

	private String getCleanedTweetText(String originalTweetText)
	{
		String cleanedTweet = LUCENE_PATTERN.matcher(originalTweetText).replaceAll(REPLACEMENT_STRING);
		cleanedTweet = QueryParser.escape(cleanedTweet);
		return cleanedTweet;
	}

	

}
