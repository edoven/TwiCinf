package servlets;

import it.cybion.influencers.cache.TwitterCache;
import it.cybion.influencers.cache.persistance.PersistenceFacade;
import it.cybion.influencers.cache.utils.CalendarManager;
import it.cybion.influencers.cache.web.Token;
import it.cybion.influencers.cache.web.WebFacade;
import it.cybion.influencers.ranking.RankedUser;
import it.cybion.influencers.ranking.RankingCalculator;
import it.cybion.influencers.ranking.topic.TopicScorer;
import it.cybion.influencers.ranking.topic.knn.KnnTopicScorer;
import org.apache.log4j.Logger;
import utils.HomePathGetter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;


/**
 * Servlet implementation class ScoresCalculator
 */
public class ScoresCalculationLauncher extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ScoresCalculationLauncher.class);

    public ScoresCalculationLauncher() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usersListFilePath = request.getParameter("usersListFilePath");
        LOGGER.info("usersListFilePath=" + usersListFilePath);
        List<Long> usersToRank = getUsersIdsFromFile(usersListFilePath);

        Date fromDate = getFromDate(request);
        LOGGER.info("fromDate=" + fromDate);

        Date toDate = getToDate(request);
        LOGGER.info("toDate=" + toDate);

        List<String> topicTweets = getTopicTweets(request);
        LOGGER.info("In Topic Tweets:");
        int count = 0;
        for (String tweet : topicTweets) {
            int i = count++;
            LOGGER.info(i + ") " + tweet);
        }

        List<String> outOfTopicTweets = getOutOfTopicTweets(request);
        LOGGER.info("Out of Topic Tweets:");
        count = 0;
        for (String tweet : outOfTopicTweets) {
            int i = count++;
            LOGGER.info(i + ") " + tweet);
        }

        int tweetsPerDocument = getTweetsPerDocument(request);
        LOGGER.info("tweetsPerDocument=" + tweetsPerDocument);

        int k = getK(request);
        LOGGER.info("k=" + k);

        String CRAWNKER_HOME = HomePathGetter.getInstance().getHomePath();
        String generalConfigFilePath = CRAWNKER_HOME + "general.config";
        Properties twitterCacheProperties = getPropertiesFromFile(generalConfigFilePath);
        TwitterCache twitterCache = getTwitterCacheFromProperties(twitterCacheProperties);

        TopicScorer topicScorer = getKnnTopicScorer(topicTweets, outOfTopicTweets, k);
        RankingCalculator rankingCalculator = new RankingCalculator(twitterCache, topicScorer);
        List<RankedUser> rankedUsers = rankingCalculator.getRankedUsersWithoutUrlsResolution(
                usersToRank, fromDate, toDate);
        for (RankedUser rankedUser : rankedUsers) {
            LOGGER.info(rankedUser.toCSV());
        }
        request.setAttribute("rankedUsers", rankedUsers);
        request.getRequestDispatcher("ranking-result.jsp").forward(request, response);
    }

    private List<String> getOutOfTopicTweets(HttpServletRequest request)
	{

        String topicFile = request.getParameter("topicFile");
        //		String CRAWNKER_HOME = HomePathGetter.getInstance().getHomePath();
        List<String> listFiles = new ArrayList<String>();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(topicFile));
        } catch (FileNotFoundException e) {
            LOGGER.error("missing file: '" + topicFile + "'" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("can't read file: '" + topicFile + "'" + e.getMessage());
            e.printStackTrace();
        }

        for (Entry<Object, Object> propertyEntry : properties.entrySet())
		{
			String key = (String)propertyEntry.getKey();

            if (key.startsWith("outOfTopic")) {
				listFiles.add((String)propertyEntry.getValue());
            }
		}

		List<String> tweets = new ArrayList<String>();
		for (String filtFile : listFiles) {
			tweets.addAll(getTweetsFromFile(filtFile));
        }
		return tweets;
	}


	private List<String> getTopicTweets(HttpServletRequest request)
	{
        //		String CRAWNKER_HOME = HomePathGetter.getInstance().getHomePath();
        String topicFile = request.getParameter("topicFile");
        String fullFilePath = topicFile;
        List<String> listFiles = new ArrayList<String>();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(fullFilePath));
        } catch (FileNotFoundException e) {
            LOGGER.error("missing file: '" + fullFilePath + "'" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("can't read file: '" + fullFilePath + "'" + e.getMessage());
            e.printStackTrace();
        }
        for (Entry<Object, Object> propertyEntry : properties.entrySet()) {
            String key = (String) propertyEntry.getKey();
            if (key.startsWith("inTopic")) {
                listFiles.add((String) propertyEntry.getValue());
            }
        }

        List<String> tweets = new ArrayList<String>();
		for (String filtFile : listFiles) {
			tweets.addAll(getTweetsFromFile(filtFile));
        }
		return tweets;
	}

    private KnnTopicScorer getKnnTopicScorer(List<String> topicTweets,
                                             List<String> outOfTopicTweets, int k) {

        KnnTopicScorer topicScorer = new KnnTopicScorer(topicTweets, outOfTopicTweets, k);
        return topicScorer;
    }

    private TwitterCache getTwitterCacheFromProperties(Properties properties) throws UnknownHostException
	{
		
		String mongodbHost = properties.getProperty("mongodb_host");
		String mongodbTwitterDb = properties.getProperty("mongodb_db");
		PersistenceFacade persistenceFacade = PersistenceFacade.getInstance(mongodbHost,
                mongodbTwitterDb);

		String applicationTokenPath = properties.getProperty("application_token_path");
		Token applicationToken = new Token(applicationTokenPath);	
		List<Token> userTokens = new ArrayList<Token>();	
		int i=0;
		String userTokenPath;
		while ((userTokenPath =  properties.getProperty("user_token_"+i+"_path")) != null)
		{
			userTokens.add(new Token(userTokenPath));
			i++;
		}
		WebFacade webFacade = WebFacade.getInstance(applicationToken, userTokens);
			
		return TwitterCache.getInstance(webFacade, persistenceFacade);
	}
	
	private Properties getPropertiesFromFile(String filePath)
	{
		InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(filePath));
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (FileNotFoundException e) {
            LOGGER.error("missing file: '" + filePath + "' " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("can't read file: '" + filePath + "' " + e.getMessage());
            e.printStackTrace();
        }
        return null;
	}
	
//	private boolean getUrlsResolution(HttpServletRequest request)
//	{
//		String urlsResolution = request.getParameter("urlsResolution");
//		if ( urlsResolution.equals("true") )
//			return true;
//		else
//			return false;
//	}
	
	private int getTweetsPerDocument(HttpServletRequest request)
	{
		String tweetsPerDocumentString = request.getParameter("tweetsPerDocument");
		return Integer.parseInt(tweetsPerDocumentString);
	}
	
	private int getK(HttpServletRequest request)
	{
		String kString = request.getParameter("k");
		return Integer.parseInt(kString);
	}
	
	
	private Date getFromDate(HttpServletRequest request)
	{
		String fromDateyearString = request.getParameter("fromDateYear");
		String fromDateMonthString = request.getParameter("fromDateMonth");
		String fromDateDayString = request.getParameter("fromDateDay");
		int year = Integer.parseInt(fromDateyearString);
		int month = Integer.parseInt(fromDateMonthString);
		int day = Integer.parseInt(fromDateDayString);		
		Date fromDate   = CalendarManager.getDate(year, month, day);		
		return fromDate;
	}
	
	private Date getToDate(HttpServletRequest request)
	{
		String toDateYearString = request.getParameter("toDateYear");
		String toDateMonthString = request.getParameter("toDateMonth");
		String toDateDayString = request.getParameter("toDateDay");
		int year = Integer.parseInt(toDateYearString);
		int month = Integer.parseInt(toDateMonthString);
		int day = Integer.parseInt(toDateDayString);		
		Date toDate   = CalendarManager.getDate(year, month, day);
		return toDate;
	}
	
	
	private List<Long> getUsersIdsFromFile(String listFilePath)
	{
		List<Long> ids = new ArrayList<Long>();
		try
		{
			BufferedReader fileReader = new BufferedReader(new FileReader(listFilePath));
			String currentLine =  fileReader.readLine();
			while (currentLine != null) 
			{
				LOGGER.debug(currentLine);
				long id = Long.parseLong(currentLine);
				ids.add(id);
				currentLine =  fileReader.readLine();
			}
			fileReader.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return ids;
	}
	
	
	private List<String> getTweetsFromFile(String listFilePath)
	{
		List<String> tweets = new ArrayList<String>();
		try
		{
			BufferedReader fileReader = new BufferedReader(new FileReader(listFilePath));
			String currentLine =  fileReader.readLine();
			while (currentLine != null) 
			{			
				tweets.add(currentLine);
				currentLine =  fileReader.readLine();
			}
			fileReader.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return tweets;
	}

}
