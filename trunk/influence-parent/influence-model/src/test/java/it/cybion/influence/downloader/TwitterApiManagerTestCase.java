package it.cybion.influence.downloader;

import it.cybion.influence.metrics.MetricsResultPrinter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TwitterApiManagerTestCase {
	
	private static final Logger logger = Logger.getLogger(TwitterApiManagerTestCase.class);

	TwitterApiManager twitterApiManager = null;
	
	@BeforeClass
	public void setup() {
		List<String> userTokenFilePaths = new ArrayList<String>();
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		userTokenFilePaths.add("/home/godzy/tokens/token1.txt");
		String consumerTokenFilePath = ("/home/godzy/tokens/consumerToken.txt");
		twitterApiManager = new TwitterApiManager(consumerTokenFilePath, userTokenFilePaths);
	}
	
	@AfterClass
	public void shutdown() {
		twitterApiManager = null;
	}
	
	@Test
	public void printResultForOneUser() {
		List<String> friendsIds = twitterApiManager.getFriends("edoventurini");
		for (String friendId : friendsIds)
			logger.info(friendId);
		logger.info("friends number: "+friendsIds.size());
	}
}
