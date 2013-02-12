package it.cybion.influencers.filtering.language;


import it.cybion.influencers.filtering.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;



public class LanguageDetectionFilter implements Filter
{

	private static final Logger logger = Logger.getLogger(LanguageDetectionFilter.class);

	private Map<Long, List<String>> user2tweets;
	private String profilesDir;

	public LanguageDetectionFilter(Map<Long, List<String>> user2tweets, String profilesDir)
	{
		this.user2tweets = user2tweets;
		this.profilesDir = profilesDir;
	}

	@Override
	public List<Long> filter()
	{
		List<Long> goodUsers = new ArrayList<Long>();
		try
		{
			DetectorFactory.loadProfile(profilesDir);
		} catch (LangDetectException e1)
		{
			logger.info("Error with language profile directory. Exiting.");
			System.exit(0);
		}
		int userCount = 1;
		for (Long userId : user2tweets.keySet())
		{
			logger.info("Analyzig language for user " + (userCount++) + "/" + user2tweets.size());
			Map<String, Integer> languages2TweetsCount = new HashMap<String, Integer>();
			List<String> tweets = user2tweets.get(userId);
			for (String tweet : tweets)
			{
				Detector detector = null;
				try
				{
					detector = DetectorFactory.create();
				} catch (LangDetectException e1)
				{
					logger.info("Error with  DetectorFactory.create(). Exiting.");
					System.exit(0);
				}
				detector.append(tweet);
				String language = "";
				try
				{
					language = detector.detect();
				} catch (LangDetectException e)
				{
					continue;
				}
				if (!languages2TweetsCount.containsKey(language))
					languages2TweetsCount.put(language, 1);
				else
					languages2TweetsCount.put(language, (languages2TweetsCount.get(language) + 1));
			}
			if (languages2TweetsCount.containsKey("it") && languages2TweetsCount.get("it") > 5)
				goodUsers.add(userId);
		}
		return goodUsers;
	}

}
