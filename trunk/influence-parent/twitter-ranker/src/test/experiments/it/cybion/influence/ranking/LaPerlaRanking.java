package it.cybion.influence.ranking;

import it.cybion.influence.ranking.utils.ListFileReader;
import it.cybion.influencers.cache.TwitterCache;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

public class LaPerlaRanking
{
	public static void main(String[] args) throws UnknownHostException
	{
		RankingCalculator rankingCalculator = new RankingCalculator();
		int fromYear = 2013, 
			fromMonth = 2, 
			fromDay = 1; 
		int toYear = 2013, 
			toMonth= 2, 
			toDay = 20;
		String luceneTempDirPath = "/home/godzy/Desktop/temp";
		TwitterCache twitterFacade = TwitterFacadeFactory.getTwitterFacade();
		List<Long> laPerla2800UserIds = ListFileReader.readLongListFile("/home/godzy/Desktop/laPerla2800UsersIds.txt");
		Collections.shuffle(laPerla2800UserIds);
		List<Long> seedUsers = laPerla2800UserIds.subList(0, 10);
		List<Long> usersToRank = laPerla2800UserIds.subList(20, 40);
		
		
		List<RankedUser> users = rankingCalculator.printRanks(seedUsers, usersToRank, twitterFacade, luceneTempDirPath, 
									fromYear, fromMonth, fromDay, 
									toYear, toMonth, toDay);
		
		
		System.out.println();
		System.out.println();
		System.out.println("== RANKED USERS ==");
		System.out.println();
		for (RankedUser rankedUser : users)
			System.out.println(rankedUser.toString());
		System.exit(0);
	}
	
	
}
