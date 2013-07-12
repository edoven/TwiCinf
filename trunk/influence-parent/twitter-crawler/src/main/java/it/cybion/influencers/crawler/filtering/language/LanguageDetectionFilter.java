package it.cybion.influencers.crawler.filtering.language;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import it.cybion.influencers.crawler.filtering.Filter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class LanguageDetectionFilter implements Filter
{

	private static final Logger LOGGER = Logger.getLogger(LanguageDetectionFilter.class);

	private Map<Long, List<String>> user2tweets;
	private String profilesDir;
	private String language; //it, en, fs, es,...
	
	private int TWEETS_THRESHOLD = 5;

	public LanguageDetectionFilter(Map<Long, List<String>> user2tweets, String profilesDir, String language)
	{
		this.user2tweets = user2tweets;
		this.profilesDir = profilesDir;
		this.language = language;
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
			LOGGER.error("Error with language profile directory. Exiting.");
			e1.printStackTrace();
		}
		int userCount = 1;
		for (Long userId : user2tweets.keySet())
		{
			LOGGER.info("Analyzig language for user " + (userCount++) + "/" + user2tweets.size());
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
					LOGGER.info("Error with  DetectorFactory.create(). Exiting.");
                    e1.printStackTrace();

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
			if (languages2TweetsCount.containsKey(language) && languages2TweetsCount.get(language) > TWEETS_THRESHOLD)
				goodUsers.add(userId);
		}
		return goodUsers;
	}

}
