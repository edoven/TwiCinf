package it.cybion.influencers.ranking.topic.lucene;


import it.cybion.influencers.graph.Neo4jGraphFacade;

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

public class TweetToTopicSimilarityCalculator
{

	private static final String LUCENE_ESCAPE_CHARS = "[\\\\+\\-\\!\\(\\)\\:\\^\\]\\{\\}\\~\\*\\?]";
	private static final Pattern LUCENE_PATTERN = Pattern.compile(LUCENE_ESCAPE_CHARS);
	private static final String REPLACEMENT_STRING = "\\\\$0";

	private static final Logger logger = Logger.getLogger(Neo4jGraphFacade.class);

	private List<IndexReader> indexesReaders = new ArrayList<IndexReader>();
	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);

	public TweetToTopicSimilarityCalculator(List<Directory> indexes)
	{
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

	public float getTweetRank(String tweet)
	{
		float accumulator = 0;
		for (IndexReader indexReader : indexesReaders)
		{
			accumulator = accumulator + getTweetRankFromOneIndex(indexReader, tweet);
		}
		float totalScore = accumulator / indexesReaders.size();
		logger.info("total=" + totalScore);
		return totalScore;
	}

	private float getTweetRankFromOneIndex(IndexReader indexReader, String tweet)
	{
		Query query = null;
		QueryParser queryParser = new QueryParser(Version.LUCENE_36, "content", analyzer);
		try
		{
			query = queryParser.parse(getCleanedTweetText(tweet));
		} catch (ParseException e1)
		{
			e1.printStackTrace();
			System.exit(0);
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
		logger.info(score);
		return score;

	}

	private String getCleanedTweetText(String originalTweetText)
	{
		String cleanedTweet = LUCENE_PATTERN.matcher(originalTweetText).replaceAll(REPLACEMENT_STRING);
		cleanedTweet = QueryParser.escape(cleanedTweet);
		return cleanedTweet;
	}

}
