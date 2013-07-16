package utils;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import java.io.*;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class PropertiesLoader {

    private static final Logger LOGGER = Logger.getLogger(PropertiesLoader.class);

    public static final String GENERAL_CONFIG_FILENAME = "general.config";

    public static final String CRAWLING_DIRECTORY = "crawling/";

    public static final String RANKING_DIRECTORY = "ranking/";

    private final String crawnkerHome;

    private final String rankedUsersResultsDirectory;

    private final String influencersResultsDirectory;

    private final String tokensDirectory;

    private String crawlingConfigDirectory;

    private String crawlingOutputDirectory;
    private String topicDirectory;
    private String rankingTopicListDirectory;

    public PropertiesLoader() {
        this.crawnkerHome = HomePathGetter.getInstance().getHomePath();
        this.rankedUsersResultsDirectory = this.crawnkerHome + "results/";
        this.influencersResultsDirectory = this.crawnkerHome + "influencers/";
        this.crawlingConfigDirectory = this.crawnkerHome + CRAWLING_DIRECTORY + "config/";
        this.crawlingOutputDirectory = this.crawnkerHome + CRAWLING_DIRECTORY + "output/";
        this.topicDirectory = this.crawnkerHome + RANKING_DIRECTORY + "topic/";
        this.rankingTopicListDirectory = this.topicDirectory + "lists/";
        this.tokensDirectory = this.crawnkerHome + "tokens/";
    }

    public Properties loadGeneralProperties() throws ServletException {
        final String generalConfigFilePath = this.crawnkerHome + GENERAL_CONFIG_FILENAME;
        return loadPropertiesFromFile(generalConfigFilePath);
    }

    //TODO use FileHelper
    private Properties loadPropertiesFromFile(String filePath) throws ServletException {

        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(filePath));
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (FileNotFoundException e) {
            final String message = "missing file: '" + filePath + "' " + e.getMessage();
            LOGGER.error(message);
            throw new ServletException(message, e);
        } catch (IOException e) {
            final String message = "can't read file: '" + filePath + "' " + e.getMessage();
            LOGGER.error(message);
            throw new ServletException(message);
        }
    }

    public String getCrawnkerHome() {

        return crawnkerHome;
    }

    public String getRankedUsersResultsDirectory() {

        return rankedUsersResultsDirectory;
    }

    public String getInfluencersResultsDirectory() {

        return influencersResultsDirectory;
    }

    public String getCrawlingConfigDirectory() {

        return crawlingConfigDirectory;
    }

    public String getCrawlingOutputDirectory() {

        return crawlingOutputDirectory;
    }

    public String getTopicDirectory() {

        return topicDirectory;
    }

    public String getRankingTopicListDirectory() {

        return rankingTopicListDirectory;
    }

    public String getTokensDirectory() {

        return tokensDirectory;
    }
}
