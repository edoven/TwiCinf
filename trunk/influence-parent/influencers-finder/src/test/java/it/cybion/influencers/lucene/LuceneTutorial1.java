//package it.cybion.influencers.lucene;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.StringField;
//import org.apache.lucene.document.TextField;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.queryparser.classic.ParseException;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TopScoreDocCollector;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.RAMDirectory;
//import org.apache.lucene.util.Version;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.store.SimpleFSDirectory;
//
//
//public class LuceneTutorial1 {
//
//	/**
//	 * @param args
//	 * @throws IOException 
//	 * @throws ParseException 
//	 */
//	
//	public static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
//	
//	public static void main(String[] args) throws IOException, ParseException {
//		//createIndex();
//		executeQuery();
//		//index.close();
//				
//	}
//
//	private static void createIndex() throws IOException {
//		Directory index = new SimpleFSDirectory(new File("/home/godzy/lucene/index1"));
//		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
//		IndexWriter w = new IndexWriter(index, config);
//		addDoc(w, "Lucene in Action", "193398817");
//		addDoc(w, "Lucene for Dummies", "55320055Z");
//		addDoc(w, "Managing Gigabytes", "55063554A");
//		addDoc(w, "The Art of Computer Science", "9900333X");
//		addDoc(w, "The Art of Computer Science", "9900333X");
//		w.close();
//		
//	}
//	
//	private static void addDoc(IndexWriter indexWriter, String title, String isbn) throws IOException {
//		  Document doc = new Document();
//		  doc.add(new TextField("title", title, Field.Store.YES));
//		  doc.add(new StringField("isbn", isbn, Field.Store.YES)); 
//		  indexWriter.addDocument(doc);
//	}
//	
//	private static void executeQuery() throws ParseException, IOException {
//		Directory index = new SimpleFSDirectory(new File("/home/godzy/lucene/index1"));
//		String querystr = "lucene dummies";
//		QueryParser parser = new QueryParser(Version.LUCENE_40, "title", analyzer);
//		Query q = parser.parse(querystr);
//		
//		int hitsPerPage = 10;
//		DirectoryReader reader = DirectoryReader.open(index);
//		IndexSearcher searcher = new IndexSearcher(reader);
//		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
//		searcher.search(q, collector);
//		ScoreDoc[] hits = collector.topDocs().scoreDocs;
//		System.out.println("Found " + hits.length + " hits.");
//		for(int i=0;i<hits.length;++i) {
//		    int docId = hits[i].doc;	
//		    float score = hits[i].score;
//		    Document d = searcher.doc(docId);
//		    System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title") + "\t" + score);
//		}
//	}
//}
