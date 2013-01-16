package it.cybion.influencers.lucene;

import it.cybion.influencers.filtering.FilterManager;
import it.cybion.influencers.filtering.contentbased.DescriptionDictionaryFilterManager;
import it.cybion.influencers.filtering.topologybased.InAndOutDegreeFilterManager;
import it.cybion.influencers.graph.GraphFacade;
import it.cybion.influencers.graph.Neo4jGraphFacade;
import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.Twitter4jWebFacade;
import it.cybion.influencers.twitter.web.TwitterWebFacade;
import it.cybion.influencers.utils.FilesDeleter;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import twitter4j.TwitterException;

public class Food46descriptions {
	
	private static final Logger logger = Logger.getLogger(Food46descriptions.class);
	
	public static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

	
	public static void main(String[] args) throws IOException, TwitterException, ParseException {	
				
//		createIndex();		
		Directory index = new SimpleFSDirectory(new File("/home/godzy/lucene/descriptions"));
		IndexReader indexReader = DirectoryReader.open(index);
		int docIndex = 0;
		Document doc = indexReader.document(docIndex);
		logger.info(doc);
		Fields fields = indexReader.getTermVectors(docIndex);
		logger.info(fields==null);

		
	}
	
	
	private static void createIndex() throws IOException, TwitterException {
		TwitterFacade twitterFacade = getTwitterFacade();

		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(200557647L); //cuocopersonale
		usersIds.add(490182956L); //DarioBressanini
		usersIds.add(6832662L); //burde
		usersIds.add(45168679L); //Ricette20
		usersIds.add(76901354L); //spylong
		usersIds.add(138387593L); //giuliagraglia
		usersIds.add(136681167L); //ele_cozzella
		usersIds.add(416427021L); //FilLaMantia
		usersIds.add(444712353L); //ChiaraMaci
		usersIds.add(472363994L); //LucaVissani
		usersIds.add(7077572L); //maghetta
		usersIds.add(16694823L); //paperogiallo
		usersIds.add(991704536L); //craccocarlo
		usersIds.add(46118391L); //Fiordifrolla
		usersIds.add(272022405L); //Davide_Oltolini
		usersIds.add(9762312L); //toccodizenzero
		usersIds.add(96738439L); //oloapmarchi
		usersIds.add(57163636L); //fooders
		usersIds.add(57283474L); //GialloZafferano
		usersIds.add(191365206L); //giornaledelcibo
		usersIds.add(75086891L); //carlo_spinelli
		usersIds.add(416478534L); //CarloOttaviano
		usersIds.add(70918724L); //slow_food_italy
		usersIds.add(323154299L); //massimobottura
		usersIds.add(342813082L); //barbierichef
		usersIds.add(31935994L); //puntarellarossa
		usersIds.add(28414979L); //morenocedroni
		usersIds.add(96384661L); //soniaperonaci
		usersIds.add(86961660L); //scattidigusto
		usersIds.add(167406951L); //WineNewsIt
		usersIds.add(368991338L); //TheBreakfastRev
		usersIds.add(17007757L); //ci_polla
		usersIds.add(135436730L); //ilgastronauta
		usersIds.add(23306444L); //dissapore
		usersIds.add(128564404L); //gianlucamorino
		usersIds.add(20696734L); //cookaround
		usersIds.add(7171022L); //cavoletto
		usersIds.add(22147020L); //Cucina_Italiana
		usersIds.add(426206087L); //DavideScabin0
		usersIds.add(54157380L); //italiasquisita
		usersIds.add(222491618L); //LaCuochina
		usersIds.add(339541519L); //SingerFood
		usersIds.add(130209798L); //GigiPadovani
		usersIds.add(41074932L); //FeudiDSGregorio
		usersIds.add(342677624L); //MartaTovaglieri
		usersIds.add(81079701L); //elisiamenduni

		List<String> descriptions = new ArrayList<String>();
		for (Long userId : usersIds) {
			String description = twitterFacade.getDescription(userId);
			logger.info(description);
			descriptions.add(description);
		}
		
		
		Directory index = new SimpleFSDirectory(new File("/home/godzy/lucene/descriptions"));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		IndexWriter indexWriter = new IndexWriter(index, config);
		String uniqueDescription = "";
		for (String description : descriptions) 
			uniqueDescription = uniqueDescription+" "+description;
		Document doc = new Document();
		doc.add(new TextField("descriptions", uniqueDescription, Field.Store.YES)); 
		indexWriter.addDocument(doc);
		indexWriter.close();		
	}

	
	private static TwitterFacade getTwitterFacade() throws UnknownHostException {
		Token applicationToken = new Token("/home/godzy/tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		Token userToken1 = new Token("/home/godzy/tokens/token1.txt"); 
		userTokens.add(userToken1);
		
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "users", "users");
		TwitterFacade twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}	
}
