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

    private final String crawnkerHome;

    private final String rankedUsersResultsDirectory;

    public String getInfluencersResultsDirectory() {

        return influencersResultsDirectory;
    }

    private final String influencersResultsDirectory;

    public PropertiesLoader() {
        this.crawnkerHome = HomePathGetter.getInstance().getHomePath();
        this.rankedUsersResultsDirectory = this.crawnkerHome + "results/";
        this.influencersResultsDirectory = this.crawnkerHome + "influencers/";

    }

    public Properties loadGeneralProperties() throws ServletException {
        String generalConfigFilePath = this.crawnkerHome + GENERAL_CONFIG_FILENAME;
        return getPropertiesFromFile(generalConfigFilePath);
    }

    //TODO use FileHelper
    private Properties getPropertiesFromFile(String filePath) throws ServletException {

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
}
