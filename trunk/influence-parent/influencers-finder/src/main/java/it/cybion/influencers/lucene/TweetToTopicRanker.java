package it.cybion.influencers.lucene;

import it.cybion.influencers.graph.Neo4jGraphFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;


public class TweetToTopicRanker {
	
	private static final Logger logger = Logger.getLogger(Neo4jGraphFacade.class);

	private List<IndexReader> indexesReaders = new ArrayList<IndexReader>();
	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
	
	public TweetToTopicRanker(List<Directory> indexes) {
		for (Directory index : indexes) {
			IndexReader indexReader = null;
			try {
				indexReader = DirectoryReader.open(index);
			} catch (IOException e) {
				e.printStackTrace();
				logger.info("Problem with index: " + index.toString());
				System.exit(0);
			}
			indexesReaders.add(indexReader);
		} 
	}
	
	public float getTweetRank(String tweet) {
		float accumulator = 0;
		for (IndexReader indexReader : indexesReaders) {
			accumulator = accumulator + getTweetRankFromOneIndex(indexReader, tweet);
		}
		return ( accumulator / indexesReaders.size());
	}
	
	private float getTweetRankFromOneIndex(IndexReader indexReader , String tweet) {
		Query query = null;
		try {
			query = new QueryParser(Version.LUCENE_40, "content", analyzer).parse(QueryParser.escape(tweet));
		} catch (ParseException e) {
			e.printStackTrace();
			logger.info("Problem in Lucene in parsing tweet: "+tweet);
			System.exit(0);
		}
		int hitsPerPage = 1;	    
	    IndexSearcher searcher = new IndexSearcher(indexReader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    try {
			searcher.search(query, collector);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Problem with searcher.search(query, collector). Query="+query);
		}
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    if ( hits.length == 0)
	    	return 0;
	    else
	    	return hits[0].score;
	}
	
}
