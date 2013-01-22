package it.cybion.influencers;

import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.Twitter4jWebFacade;
import it.cybion.influencers.twitter.web.TwitterWebFacade;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import twitter4j.TwitterException;

public class Food46experiment1 {


	public static void main(String[] args) throws UnknownHostException, TwitterException {
		List<Long> usersIds = getUsersIds();
		TwitterFacade twitterFacade = getTwitterFacade();
		int usersCount = 1;
		for (Long userdId : usersIds) {
			String description = twitterFacade.getDescription(userdId).replace('\n', ' ').replace('\r',' ');
			System.out.printf("%3d%20s \t %6d \t %6d \t %s\n",
					usersCount, 
					twitterFacade.getScreenName(userdId),
					twitterFacade.getFollowersCount(userdId),
					twitterFacade.getFriendsCount(userdId),
					description);
			usersCount++;
		}

	}
	
	private static TwitterFacade getTwitterFacade() throws UnknownHostException {
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
		PersistanceFacade persistanceFacade = new MongodbPersistanceFacade("localhost", "users", "users");
		TwitterFacade twitterFacade = new TwitterFacade(twitterWebFacade, persistanceFacade);
		return twitterFacade;
	}
	
	private static List<Long> getUsersIds() {
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add( 193611265L);
		usersIds.add( 500824680L);
		usersIds.add( 39941540L);
		usersIds.add( 48111246L);
		usersIds.add( 96738439L);
		usersIds.add( 40619979L);
		usersIds.add( 151556101L);
		usersIds.add( 131156047L);
		usersIds.add( 142354631L);
		usersIds.add( 245694460L);
		usersIds.add( 34270698L);
		usersIds.add( 339541519L);
		usersIds.add( 102996503L);
		usersIds.add( 487456640L);
		usersIds.add( 87666638L);
		usersIds.add( 81079701L);
		usersIds.add( 802588746L);
		usersIds.add( 98648536L);
		usersIds.add( 6832662L);
		usersIds.add( 331037397L);
		usersIds.add( 149253977L);
		usersIds.add( 221067255L);
		usersIds.add( 173871410L);
		usersIds.add( 191365206L);
		usersIds.add( 89406293L);
		usersIds.add( 626541094L);
		usersIds.add( 812700228L);
		usersIds.add( 26196278L);
		usersIds.add( 21777780L);
		usersIds.add( 572701804L);
		usersIds.add( 90461830L);
		usersIds.add( 299521579L);
		usersIds.add( 158138547L);
		usersIds.add( 108040534L);
		usersIds.add( 357549379L);
		usersIds.add( 286190863L);
		usersIds.add( 51166715L);
		usersIds.add( 121830670L);
		usersIds.add( 17421288L);
		usersIds.add( 377001680L);
		usersIds.add( 136225752L);
		usersIds.add( 359926178L);
		usersIds.add( 36639580L);
		usersIds.add( 46118391L);
		usersIds.add( 272022405L);
		usersIds.add( 211829335L);
		usersIds.add( 7527942L);
		usersIds.add( 793502809L);
		usersIds.add( 22598920L);
		usersIds.add( 126594828L);
		usersIds.add( 113361100L);
		usersIds.add( 36063499L);
		usersIds.add( 342677624L);
		usersIds.add( 778548776L);
		usersIds.add( 343323611L);
		usersIds.add( 9762312L);
		usersIds.add( 57163636L);
		usersIds.add( 207512402L);
		usersIds.add( 66560224L);
		usersIds.add( 103868230L);
		usersIds.add( 76680404L);
		usersIds.add( 82880906L);
		usersIds.add( 223978952L);
		usersIds.add( 390333869L);
		usersIds.add( 319108160L);
		usersIds.add( 14467351L);
		usersIds.add( 484596854L);
		usersIds.add( 555458011L);
		usersIds.add( 228381779L);
		usersIds.add( 423452319L);
		usersIds.add( 437350792L);
		usersIds.add( 389342719L);
		usersIds.add( 402570404L);
		usersIds.add( 348665127L);
		usersIds.add( 225450784L);
		usersIds.add( 527605554L);
		usersIds.add( 211806408L);
		usersIds.add( 135436730L);
		usersIds.add( 183706059L);
		usersIds.add( 128564404L);
		usersIds.add( 279197519L);
		usersIds.add( 201899679L);
		usersIds.add( 145666162L);
		usersIds.add( 720089600L);
		usersIds.add( 171089369L);
		usersIds.add( 48155558L);
		usersIds.add( 31704566L);
		usersIds.add( 843711092L);
		usersIds.add( 168596160L);
		usersIds.add( 614322431L);
		usersIds.add( 357536147L);
		usersIds.add( 50243493L);
		usersIds.add( 45552464L);
		usersIds.add( 240978459L);
		usersIds.add( 214818082L);
		usersIds.add( 321628442L);
		usersIds.add( 198459077L);
		usersIds.add( 26516085L);
		usersIds.add( 592592317L);
		usersIds.add( 219597357L);
		usersIds.add( 444073336L);
		usersIds.add( 96384661L);
		usersIds.add( 465844367L);
		usersIds.add( 355560337L);
		usersIds.add( 16127199L);
		usersIds.add( 370079196L);
		usersIds.add( 96087561L);
		usersIds.add( 54157380L);
		usersIds.add( 322450170L);
		usersIds.add( 414831878L);
		usersIds.add( 562214871L);
		usersIds.add( 78360773L);
		usersIds.add( 420303510L);
		usersIds.add( 245723016L);
		usersIds.add( 74391551L);
		usersIds.add( 85260473L);
		usersIds.add( 41570239L);
		usersIds.add( 206121739L);
		usersIds.add( 540782177L);
		usersIds.add( 261114131L);
		usersIds.add( 98582802L);
		usersIds.add( 52695733L);
		usersIds.add( 366032981L);
		usersIds.add( 38836483L);
		usersIds.add( 266001803L);
		usersIds.add( 134791730L);
		usersIds.add( 321866289L);
		usersIds.add( 483098039L);
		usersIds.add( 275075115L);
		usersIds.add( 266630581L);
		usersIds.add( 447125557L);
		usersIds.add( 543466922L);
		usersIds.add( 804747362L);
		usersIds.add( 454453288L);
		usersIds.add( 46344347L);
		usersIds.add( 17007757L);
		usersIds.add( 545198161L);
		usersIds.add( 118207942L);
		usersIds.add( 287686936L);
		usersIds.add( 7614322L);
		usersIds.add( 38914814L);
		usersIds.add( 318616724L);
		usersIds.add( 250416435L);
		usersIds.add( 537132337L);
		usersIds.add( 14684382L);
		usersIds.add( 834035106L);
		usersIds.add( 18938453L);
		usersIds.add( 17033125L);
		usersIds.add( 57283474L);
		usersIds.add( 293483941L);
		usersIds.add( 140012915L);
		usersIds.add( 165369107L);
		usersIds.add( 45415583L);
		usersIds.add( 535531417L);
		usersIds.add( 22773908L);
		usersIds.add( 77262923L);
		usersIds.add( 324511499L);
		usersIds.add( 42564613L);
		usersIds.add( 72844777L);
		usersIds.add( 130209798L);
		usersIds.add( 139800392L);
		usersIds.add( 298380836L);
		usersIds.add( 372009540L);
		usersIds.add( 514213189L);
		usersIds.add( 373837441L);
		usersIds.add( 138387593L);
		usersIds.add( 515588610L);
		usersIds.add( 454891576L);
		usersIds.add( 13727692L);
		usersIds.add( 215932863L);
		usersIds.add( 321508350L);
		usersIds.add( 376646050L);
		usersIds.add( 429086332L);
		usersIds.add( 622110209L);
		usersIds.add( 410706231L);
		usersIds.add( 201663185L);
		usersIds.add( 3801141L);
		usersIds.add( 480551945L);
		usersIds.add( 587426447L);
		usersIds.add( 211281577L);
		usersIds.add( 254177293L);
		usersIds.add( 61763915L);
		usersIds.add( 124152888L);
		usersIds.add( 194576983L);
		usersIds.add( 75086891L);
		usersIds.add( 67278706L);
		usersIds.add( 49944496L);
		usersIds.add( 16466474L);
		usersIds.add( 122407831L);
		usersIds.add( 374448711L);
		usersIds.add( 251660615L);
		usersIds.add( 482135987L);
		usersIds.add( 424538170L);
		usersIds.add( 438242339L);
		usersIds.add( 89029069L);
		usersIds.add( 105734420L);
		usersIds.add( 955883234L);
		usersIds.add( 106658261L);
		return usersIds;
	}

}
