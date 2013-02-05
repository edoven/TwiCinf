package lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class Test1 {
	
	
	public static void main(String[] args) throws IOException, ParseException {
		List<String> docs = new ArrayList<String>();
		
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(92403540L); //lapinella
		usersIds.add(24499591L); //filippala
//		usersIds.add(40090727L); //ChiaraFerragni
//		usersIds.add(37491839L); //VeronicaFerraro
//		usersIds.add(132888646L); //elenabarolo		
//		usersIds.add(236857407L); //chiarabiasi		
//		usersIds.add(46164460L); //Eleonoracarisi
		
		docs.add("Lucene for Dummies");
		docs.add("Lucene in Action");
		docs.add("Managing Gigabytes");
		docs.add("The Art of Computer Science");
		getScores("Lucene in Action Lucene for Dummies Managing Gigabytes The Art of Computer Science", docs);
		
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
	    
	    for (String doc : docs) {
	    	String querystr = doc;
	    	Query query = new QueryParser(Version.LUCENE_40, "content", analyzer).parse(querystr);
	    	
	    	// 3. search
		    int hitsPerPage = 10;
		    IndexReader reader = DirectoryReader.open(index);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(query, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		   
		    // 4. display results
		    for(int i=0;i<hits.length;++i) {
		      float score = hits[i].score;
		      System.out.println(score+" - " + doc);
		    }		    
		    reader.close();
	    }
	    
  }
}