package it.cybion.influencers.lucene;



import it.cybion.influence.ranking.topic.lucene.TweetToTopicSimilarityCalculator;

import java.io.IOException;
import java.util.ArrayList;
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



public class TweetToTopicRankerTEST
{

	@Test
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
		TweetToTopicSimilarityCalculator ranker = new TweetToTopicSimilarityCalculator(indexes);
		String tweet1 = "un tweet sulla pasta, il cibo migliore che esista";
		String tweet2 = "la ricetta della torta fragole, peperoni e salmone";
		String tweet3 = "testo senza senso";
		Assert.assertTrue(ranker.getTweetRank(tweet1) > 0);
		Assert.assertTrue(ranker.getTweetRank(tweet2) > 0);
		Assert.assertTrue(ranker.getTweetRank(tweet3) == 0);
	}
}
