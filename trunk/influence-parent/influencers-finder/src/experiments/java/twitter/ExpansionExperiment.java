package twitter;

import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.persistance.MongodbPersistanceFacade;
import it.cybion.influencers.twitter.persistance.PersistanceFacade;
import it.cybion.influencers.twitter.web.Token;
import it.cybion.influencers.twitter.web.Twitter4jWebFacade;
import it.cybion.influencers.twitter.web.TwitterWebFacade;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twitter4j.TwitterException;

public class ExpansionExperiment {
	
	
	private static TwitterFacade twitterFacade;
	
		
	class User {
		public long id;
		public int followersCount;
		public int friendsCount;
		
		public User(long id, int followersCount, int friendsCount) {
			this.id = id;
			this.followersCount = followersCount;
			this.friendsCount = friendsCount;
		}
	};
	
	class ComparatorByFollowers implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			User u1 = (User)o1;
			User u2 = (User)o2;
			return u2.followersCount - u1.followersCount;
		}			
	};
	
	class ComparatorByFriends implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			User u1 = (User)o1;
			User u2 = (User)o2;
			return u2.friendsCount - u1.friendsCount;
		}			
	};
	
	public static void main(String[] args) throws TwitterException, UnknownHostException {
		twitterFacade = getTwitterFacade();
		
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(200557647L);
		usersIds.add(490182956L);
		usersIds.add(6832662L);
		usersIds.add(45168679L);
		usersIds.add(76901354L);
		usersIds.add(138387593L);
		usersIds.add(136681167L);
		usersIds.add(416427021L);
		usersIds.add(444712353L);
		usersIds.add(472363994L);
		usersIds.add(7077572L);
		usersIds.add(16694823L);
		usersIds.add(991704536L);
		usersIds.add(46118391L);
		usersIds.add(272022405L);
		usersIds.add(9762312L);
		usersIds.add(96738439L);
		usersIds.add(57163636L);
		usersIds.add(57283474L);
		usersIds.add(191365206L);
		usersIds.add(416478534L);
		usersIds.add(70918724L);
		usersIds.add(323154299L);
		usersIds.add(342813082L);
		usersIds.add(31935994L);
		usersIds.add(28414979L);
		usersIds.add(96384661L);
		usersIds.add(86961660L);
		usersIds.add(167406951L);
		usersIds.add(368991338L);
		usersIds.add(17007757L);
		usersIds.add(135436730L);
		usersIds.add(23306444L);
		usersIds.add(128564404L);
		usersIds.add(20696734L);
		usersIds.add(7171022L);
		usersIds.add(22147020L);
		usersIds.add(426206087L);
		usersIds.add(54157380L);
		usersIds.add(222491618L);
		usersIds.add(339541519L);
		usersIds.add(130209798L);
		usersIds.add(41074932L);
		usersIds.add(342677624L);
		usersIds.add(81079701L);
		usersIds.add(455651350L);
		usersIds.add(153835703L);
		usersIds.add(167874623L);
		usersIds.add(488864885L);
		usersIds.add(396495123L);
		usersIds.add(282465252L);
		usersIds.add(815714022L);
		usersIds.add(566594290L);
		usersIds.add(522142798L);
		usersIds.add(116035984L);
		usersIds.add(550171914L);
		usersIds.add(354558264L);
		usersIds.add(91641939L);
		usersIds.add(379722184L);
		usersIds.add(323335959L);
		usersIds.add(376151691L);
		usersIds.add(317912030L);
		usersIds.add(479819134L);
		usersIds.add(302195691L);
		usersIds.add(418614287L);
		usersIds.add(582102544L);
		usersIds.add(508745926L);
		usersIds.add(424926342L);
		usersIds.add(203285503L);
		usersIds.add(127568291L);
		usersIds.add(42013583L);
		usersIds.add(633657340L);
		usersIds.add(340196933L);
		usersIds.add(408196064L);
		usersIds.add(563952509L);
		usersIds.add(112840433L);
		usersIds.add(473859930L);
		usersIds.add(493569863L);
		usersIds.add(246183028L);
		usersIds.add(387723456L);
		usersIds.add(501929729L);
		usersIds.add(145219498L);
		usersIds.add(224362274L);
		usersIds.add(32347613L);
		usersIds.add(497035616L);
		usersIds.add(51115363L);
		usersIds.add(731040823L);
		usersIds.add(52811632L);
		usersIds.add(445629424L);
		usersIds.add(413000921L);
		usersIds.add(416459268L);
		usersIds.add(528752504L);
		usersIds.add(127050479L);
		usersIds.add(465632019L);
		usersIds.add(540873L);
		usersIds.add(167322351L);
		usersIds.add(203438938L);
		usersIds.add(821146950L);
		usersIds.add(625670417L);
		usersIds.add(433019693L);
		usersIds.add(118352409L);
		usersIds.add(321271223L);
		usersIds.add(447884472L);
		usersIds.add(379257140L);
		usersIds.add(376489518L);
		usersIds.add(190625515L);
		usersIds.add(728597227L);
		usersIds.add(505734159L);
		usersIds.add(231466126L);
		usersIds.add(118459500L);
		usersIds.add(50358694L);
		usersIds.add(284147978L);
		usersIds.add(353186683L);
		usersIds.add(338924586L);
		usersIds.add(543774984L);
		usersIds.add(533055536L);
		usersIds.add(702835705L);
		usersIds.add(92798800L);
		usersIds.add(196388759L);
		usersIds.add(234371743L);
		usersIds.add(461995661L);
		usersIds.add(42008251L);
		usersIds.add(428226051L);
		usersIds.add(17789524L);
		usersIds.add(207414538L);
		usersIds.add(54932082L);
		usersIds.add(337709748L);
		usersIds.add(47678136L);
		usersIds.add(199357039L);
		usersIds.add(377004643L);
		usersIds.add(19048014L);
		usersIds.add(271398203L);
		usersIds.add(222759682L);
		usersIds.add(112403678L);
		usersIds.add(73404332L);
		usersIds.add(50985244L);
		usersIds.add(396845110L);
		usersIds.add(226904580L);
		usersIds.add(55201968L);
		usersIds.add(31370548L);
		usersIds.add(272634064L);
		usersIds.add(95065399L);
		usersIds.add(983370686L);
		usersIds.add(534667257L);
		usersIds.add(7856222L);
		usersIds.add(158861650L);
		usersIds.add(628524838L);
		usersIds.add(325459258L);
		usersIds.add(47188403L);
		usersIds.add(224736736L);
		usersIds.add(423536940L);
		usersIds.add(56397924L);
		usersIds.add(523642610L);
		usersIds.add(247890026L);
		usersIds.add(219340924L);
		usersIds.add(319646224L);
		usersIds.add(53885203L);
		usersIds.add(139444047L);
		usersIds.add(48652501L);
		usersIds.add(245714279L);
		usersIds.add(36910791L);
		usersIds.add(243202158L);
		usersIds.add(357013045L);
		usersIds.add(184139451L);
		usersIds.add(478765101L);
		usersIds.add(297378499L);
		usersIds.add(152605574L);
		usersIds.add(293420717L);
		usersIds.add(15446162L);
		usersIds.add(292933943L);
		usersIds.add(198492254L);
		usersIds.add(50986343L);
		usersIds.add(478532228L);
		usersIds.add(216247431L);
		usersIds.add(120380619L);
		usersIds.add(119156069L);
		usersIds.add(567920774L);
		usersIds.add(158067396L);
		usersIds.add(92726058L);
		usersIds.add(258774289L);
		usersIds.add(445473207L);
		usersIds.add(138357622L);
		usersIds.add(604612272L);
		usersIds.add(138405382L);
		usersIds.add(246959550L);
		usersIds.add(365545538L);
		usersIds.add(44297230L);
		usersIds.add(464959844L);
		usersIds.add(504213777L);
		usersIds.add(28352472L);
		usersIds.add(168501948L);
		usersIds.add(132681126L);
		usersIds.add(260678396L);
		usersIds.add(35480285L);
		usersIds.add(380816026L);
		usersIds.add(272667342L);
		usersIds.add(231055267L);
		usersIds.add(175085220L);
		usersIds.add(24562353L);
		usersIds.add(406865793L);
		usersIds.add(422577508L);
		usersIds.add(331666825L);
		usersIds.add(245403656L);
		usersIds.add(71026979L);
		usersIds.add(201207305L);
		usersIds.add(94063500L);
		usersIds.add(300510501L);
		usersIds.add(37650384L);
		usersIds.add(35003378L);
		usersIds.add(281987950L);
		usersIds.add(117701249L);
		usersIds.add(107554109L);
		usersIds.add(329712103L);
		usersIds.add(573376838L);
		usersIds.add(385888951L);
		usersIds.add(321988106L);
		usersIds.add(536701698L);
		usersIds.add(217792901L);
		usersIds.add(469460774L);
		usersIds.add(399504686L);
		usersIds.add(471207656L);
		usersIds.add(810634080L);
		usersIds.add(215050728L);
		usersIds.add(52874601L);
		usersIds.add(105929685L);
		usersIds.add(311987336L);
		usersIds.add(130743539L);
		usersIds.add(454772659L);
		usersIds.add(325174492L);
		usersIds.add(45515152L);
		usersIds.add(373311691L);
		usersIds.add(968066173L);
		usersIds.add(229092881L);
		usersIds.add(400120423L);
		usersIds.add(71564326L);
		usersIds.add(262170287L);
		usersIds.add(104944098L);
		usersIds.add(174235448L);
		usersIds.add(532202923L);
		usersIds.add(493075369L);
		usersIds.add(429756228L);
		usersIds.add(502254699L);
		usersIds.add(717849745L);
		usersIds.add(104197803L);
		usersIds.add(42592638L);
		usersIds.add(333535020L);
		usersIds.add(508091069L);
		usersIds.add(532357004L);
		usersIds.add(242364755L);
		usersIds.add(415743L);
		usersIds.add(17786680L);
		usersIds.add(407762156L);
		usersIds.add(625446484L);
		usersIds.add(501162078L);
		usersIds.add(60562876L);
		usersIds.add(8641682L);
		usersIds.add(400659732L);
		usersIds.add(92294198L);
		usersIds.add(253956088L);
		usersIds.add(426724668L);
		usersIds.add(541768154L);
		usersIds.add(470237508L);
		usersIds.add(52110205L);
		usersIds.add(140776259L);
		usersIds.add(577297221L);
		usersIds.add(359231506L);
		usersIds.add(205715015L);
		usersIds.add(518392799L);
		usersIds.add(56814502L);
		usersIds.add(108411822L);
		usersIds.add(12993022L);
		usersIds.add(36359277L);
		usersIds.add(376991861L);
		usersIds.add(198818462L);
		usersIds.add(54321798L);
		usersIds.add(195164635L);
		usersIds.add(410109893L);
		usersIds.add(138029340L);
		usersIds.add(330227615L);
		usersIds.add(106455042L);
		usersIds.add(292319000L);
		usersIds.add(573987754L);
		usersIds.add(227272869L);
		usersIds.add(385910975L);
		usersIds.add(204358279L);
		usersIds.add(394010458L);
		usersIds.add(702720528L);
		usersIds.add(127632449L);
		usersIds.add(226092023L);
		usersIds.add(105169554L);
		usersIds.add(70731786L);
		usersIds.add(246361193L);
		usersIds.add(93065477L);
		usersIds.add(18749668L);
		usersIds.add(34717311L);
		usersIds.add(388129232L);
		usersIds.add(300164030L);
		usersIds.add(15164194L);
		usersIds.add(283033025L);
		usersIds.add(189908238L);
		usersIds.add(359955419L);
		usersIds.add(24028644L);
		usersIds.add(197393212L);
		usersIds.add(36142154L);
		usersIds.add(468316108L);
		usersIds.add(9407212L);
		usersIds.add(247348938L);
		usersIds.add(276877879L);
		usersIds.add(614308979L);
		usersIds.add(74458253L);
		usersIds.add(250195430L);
		usersIds.add(499131541L);
		usersIds.add(130077416L);
		usersIds.add(37908680L);
		usersIds.add(475648031L);
		usersIds.add(603891853L);
		usersIds.add(338686158L);
		usersIds.add(80397413L);
		usersIds.add(21885975L);
		usersIds.add(291710084L);
		usersIds.add(15423900L);
		usersIds.add(106195223L);
		usersIds.add(414704682L);
		usersIds.add(202187051L);
		usersIds.add(182095745L);
		usersIds.add(312557231L);
		usersIds.add(373342103L);
		usersIds.add(318003165L);
		usersIds.add(221772714L);
		usersIds.add(123603520L);
		usersIds.add(24063959L);
		usersIds.add(430745136L);
		usersIds.add(28131595L);
		usersIds.add(97694361L);
		usersIds.add(94526140L);
		usersIds.add(249417811L);
		usersIds.add(8102942L);
		usersIds.add(69189488L);
		usersIds.add(146593792L);
		usersIds.add(134131744L);
		usersIds.add(429035754L);
		usersIds.add(215031217L);
		usersIds.add(17347916L);
		usersIds.add(18835073L);
		usersIds.add(205716991L);
		usersIds.add(58411120L);
		usersIds.add(463846774L);
		usersIds.add(250284309L);
		usersIds.add(193220955L);
		usersIds.add(82593795L);
		usersIds.add(492113524L);
		usersIds.add(71850983L);
		usersIds.add(524511008L);
		usersIds.add(27621454L);
		usersIds.add(62500641L);
		usersIds.add(30184354L);
		usersIds.add(139643718L);
		usersIds.add(172040568L);
		usersIds.add(475686787L);
		usersIds.add(16959085L);
		usersIds.add(605430641L);
		usersIds.add(868551L);
		usersIds.add(490798068L);
		usersIds.add(73585910L);
		usersIds.add(545951041L);
		usersIds.add(211453853L);
		usersIds.add(35425518L);
		usersIds.add(273932585L);
		usersIds.add(62975550L);
		usersIds.add(414352009L);
		usersIds.add(14282445L);
		usersIds.add(41328400L);
		usersIds.add(303744923L);
		usersIds.add(384186966L);
		usersIds.add(425774841L);
		usersIds.add(589843940L);
		usersIds.add(75642164L);
		usersIds.add(198791049L);
		usersIds.add(104444731L);
		usersIds.add(144527623L);
		usersIds.add(833009336L);
		usersIds.add(255662125L);
		usersIds.add(156242571L);
		usersIds.add(41109810L);
		usersIds.add(143113151L);
		usersIds.add(290549107L);
		usersIds.add(121998958L);
		usersIds.add(419544330L);
		usersIds.add(46705624L);
		usersIds.add(401609741L);
		usersIds.add(603736139L);
		usersIds.add(99914766L);
		usersIds.add(287857598L);
		usersIds.add(90388588L);
		usersIds.add(143475048L);
		usersIds.add(412431451L);
		usersIds.add(283916542L);
		usersIds.add(238144871L);
		usersIds.add(197759677L);
		usersIds.add(632947412L);

		usersIds = new ArrayList<Long>(new HashSet<Long>(usersIds));
		System.out.println("users="+usersIds.size());
		
		List<User> users = new ArrayList<User>();
		Set<Long> followersAndFriends = new HashSet<Long>();
		for (int i=0; i<usersIds.size(); i++) {
			
			long userId = usersIds.get(i);		
			System.out.println((i) +" - "+userId);
			List<Long> followers = twitterFacade.getFollowers(userId);
			List<Long> friends = twitterFacade.getFriends(userId);
			followersAndFriends.addAll(followers);
			followersAndFriends.addAll(friends);
			User user = new ExpansionExperiment(). new User(userId, followers.size(), friends.size());	
			users.add(user);
		}
		//Collections.sort(users,new ComparatorByFollowers());
		
		int totFollowersAndFriends = followersAndFriends.size();
		followersAndFriends = new HashSet<Long>();
	
		
		
		/*
		 * politica 1 : scarico tutti e due (followers o friends) solo se la differenza
		 * è maggiore del 30%, altrimenti prendo quello (followers o friends) piu numeroso
		 */
//		int both=0;
//		int onlyFollowers=0;
//		int onlyFriends=0;
//		for (int i=0; i<users.size(); i++) {
//			//for (int i=0; i<37; i++) {
//				User user = users.get(i);
//				List<Long> followers = twitterFacade.getFollowers(user.id);
//				List<Long> friends = twitterFacade.getFriends(user.id);
//				if (maxPosition(followers.size(),friends.size()).equals("FIRST")) {
//					followersAndFriends.addAll(followers);
//					if (abs(followers.size()-friends.size()) > (float)followers.size()*0.3) {
//						followersAndFriends.addAll(friends);
//						System.out.println("Added both");
//						both++;
//					}
//					else {
//						System.out.println("Added only followers");
//						onlyFollowers++;
//					}
//				}
//				else {
//					followersAndFriends.addAll(friends);
//					if (abs(followers.size()-friends.size()) > (float)friends.size()*0.3) {
//						followersAndFriends.addAll(followers);
//						System.out.println("Added both");
//						both++;
//					}
//					else {
//						System.out.println("Added only friends");
//						onlyFriends++;
//					}
//				}		
//				System.out.println((i+1) +" - "+followersAndFriends.size()+" - "+100*(float)followersAndFriends.size()/totFollowersAndFriends+"%");
//			}	
//			System.out.println("both="+both+", onlyFriends="+onlyFriends+", onlyFollowers="+onlyFollowers);

		
		/*
		 * Politica 2 : una volta prendo i follower, una volta prendo i friend
		 */
//		String followersOrFriends = "followers";
//		for (int i=0; i<users.size(); i++) {
//			User user = users.get(i);
//			List<Long> followers = twitterFacade.getFollowers(user.id);
//			List<Long> friends = twitterFacade.getFriends(user.id);
//			if (followersOrFriends.equals("followers")) {
//				followersAndFriends.addAll(followers);
//				followersOrFriends = "friends";
//			}
//			else {
//				followersAndFriends.addAll(friends);
//				followersOrFriends = "followers";
//			}
//			
//			System.out.println((i+1) +" - "+followersAndFriends.size()+" - "+100*(float)followersAndFriends.size()/totFollowersAndFriends+"%");
//		}	
		
		
		
		
		
		
		
		Collections.sort(users, new ExpansionExperiment().new ComparatorByFollowers());
		int totFollowersRequests = 0;		
		List<Long> topFollowers = new ArrayList<Long>();
		for (int i=0; i<20; i++) {
			User user = users.get(i);
			topFollowers.add(user.id);
			int followersCount = user.followersCount;
			int userFollowerRequests = upperBound((float)followersCount/5000);
			System.out.println("followersCount="+followersCount+
								", followersCount/5000="+followersCount/5000+
								", userFollowerRequests="+userFollowerRequests);
			totFollowersRequests = totFollowersRequests + userFollowerRequests;
		}

		Collections.sort(users,  new ExpansionExperiment().new ComparatorByFriends());
		int totFriendsRequests = 0;
		List<Long> topFriends = new ArrayList<Long>();
		for (int i=0; i<users.size() && totFriendsRequests<totFollowersRequests; i++) {
			User user = users.get(i);
		//	if (!top50followers.contains(users.get(i).id)) {
				topFriends.add(users.get(i).id);
				int friendsCount = user.friendsCount;
				int userFriendsRequests = upperBound((float)friendsCount/5000);
				System.out.println("friendsCount="+friendsCount+
									", friendsCount/5000="+friendsCount/5000+
									", userFollowerRequests="+userFriendsRequests);
				totFriendsRequests = totFriendsRequests + userFriendsRequests;
		//	}			
		}
		System.out.println("top100friends.size()="+topFriends.size());
		
		
		for (Long userId : topFollowers) {
			List<Long> followers = twitterFacade.getFollowers(userId);	
			followersAndFriends.addAll(followers);
			System.out.println(followersAndFriends.size()+" - "+100*(float)followersAndFriends.size()/totFollowersAndFriends+"%");
		}
		
		for (Long userId : topFriends) {
			List<Long> friends = twitterFacade.getFriends(userId);	
			followersAndFriends.addAll(friends);
			System.out.println(followersAndFriends.size()+" - "+100*(float)followersAndFriends.size()/totFollowersAndFriends+"%");
		}
		
		System.out.println("topFollowers.sez()="+topFollowers.size());
		System.out.println("topFriends.size()="+topFriends.size());
		System.out.println("totFollowersRequests="+totFollowersRequests);
		System.out.println("totFriendsRequests="+totFriendsRequests);
		
		
		
		System.exit(0);
	}
	
	public static int upperBound(float f) {
		int arrotondamento = Math.round(f);
		if (f>arrotondamento)
			arrotondamento++;
		return arrotondamento;
	}

	
	public static int abs(int i) {
		if (i>=0)
			return i;
		else
			return -i;
	}
	
	public static int max(int a, int b) {
		if (a>=b)
			return a;
		else
			return b;
	}
	
	public static String maxPosition(int a, int b) {
		if (a>=b)
			return "FIRST";
		else
			return "SECOND";
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
	
	
}
