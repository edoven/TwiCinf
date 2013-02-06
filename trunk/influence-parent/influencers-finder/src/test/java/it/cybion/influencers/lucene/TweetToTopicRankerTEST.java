package it.cybion.influencers.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TweetToTopicRankerTEST {

	@Test
	public void test1() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		// index 1
	    Directory index1 = new RAMDirectory();	    
	    IndexWriter indexWriter = new IndexWriter(index1, config);
	    Document document = new Document();
	    document.add(new TextField("content", "un bellissimo tweet sul cibo e sulla cucina", Field.Store.YES));
	    indexWriter.addDocument(document);
	    indexWriter.close();
	    // index 2
	    Directory index2 = new RAMDirectory();
	    indexWriter = new IndexWriter(index2, config);
	    document = new Document();
	    document.add(new TextField("content", "la nuovissima ricetta della pasta con pere, pecorino e ananas", Field.Store.YES));
	    indexWriter.addDocument(document);
	    indexWriter.close();
	    
	    //TweetToTopicRanker
	    List<Directory> indexes = new ArrayList<Directory>();
	    indexes.add(index1);
	    indexes.add(index2);
	    TweetToTopicRanker ranker = new TweetToTopicRanker(indexes);
	    String tweet1 = "un tweet sulla pasta, il cibo migliore che esista";
	    String tweet2 = "la ricetta della torta fragole, peperoni e salmone";
	    String tweet3 = "testo senza senso";
	    Assert.assertTrue(ranker.getTweetRank(tweet1)>0);
	    Assert.assertTrue(ranker.getTweetRank(tweet2)>0);
	    Assert.assertTrue(ranker.getTweetRank(tweet3)==0);
	}
}
