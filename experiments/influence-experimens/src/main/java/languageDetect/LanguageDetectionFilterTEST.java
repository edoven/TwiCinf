package languageDetect;

import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.persistance.PersistanceFacade;
import it.cybion.influencers.cache.persistance.mongodb.MongodbPersistanceFacade;
import it.cybion.influencers.cache.web.Token;
import it.cybion.influencers.cache.web.Twitter4jWebFacade;
import it.cybion.influencers.cache.web.TwitterWebFacade;
import it.cybion.influencers.filtering.language.LanguageDetectionFilterManager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class LanguageDetectionFilterTEST {
	
	private static final Logger logger = Logger.getLogger(LanguageDetectionFilterTEST.class);
	
	public static void main(String[] args) throws UnknownHostException {
		LanguageDetectionFilterManager filter = new LanguageDetectionFilterManager("/home/godzy/Dropbox/universita/tesi/lib/langdetect-09-13-2011/profiles.sm");
		filter.setTwitterFacade(getTwitterFacade());
		filter.setSeedUsers( getUsersIds());
		logger.info(getUsersIds().size());
		logger.info(filter.filter().size());
		System.exit(0);
	}
	
	
	private static List<Long> getUsersIds() {
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(200557647L); //cuocopersonale
		usersIds.add(490182956L); //DarioBressanini
		usersIds.add(6832662L); //burde
		usersIds.add(40090727L); //ChiaraFerragni
		usersIds.add(37491839L); //VeronicaFerraro
		usersIds.add(132888646L); //elenabarolo		
		usersIds.add(236857407L); //chiarabiasi		
		usersIds.add(46164460L); //Eleonoracarisi
		usersIds.add(190625515L); 
		usersIds.add(186430183L); 
		usersIds.add(50593920L);  
		usersIds.add(326028296L); 
		usersIds.add(493117328L); 
		usersIds.add(31718476L); 
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
		return usersIds;
	}

	
	private static TwitterCache getTwitterFacade() throws UnknownHostException {
		Token applicationToken = new Token("tokens/consumerToken.txt");
		List<Token> userTokens = new ArrayList<Token>();
		Token userToken1 = new Token("tokens/token1.txt"); 
		userTokens.add(userToken1);
		Token userToken2 = new Token("tokens/token2.txt");
		userTokens.add(userToken2);
		Token userToken3 = new Token("tokens/token3.txt");
		userTokens.add(userToken3);
		Token userToken4 = new Token("tokens/token4.txt");
		userTokens.add(userToken4);
		Token userToken5 = new Token("tokens/token5.txt");
		userTokens.add(userToken5);
		Token userToken6 = new Token("tokens/token6.txt");
		userTokens.add(userToken6);
		
		TwitterWebFacade twitterWebFacade = new Twitter4jWebFacade(applicationToken, userTokens);
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "twitter");
		TwitterCache twitterFacade = new TwitterCache(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}
}
