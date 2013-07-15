package servlets;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.cybion.commons.FileHelper;
import it.cybion.influencers.cache.persistance.PersistenceFacade;
import it.cybion.influencers.cache.persistance.exceptions.PersistenceFacadeException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.ranking.RankedUser;
import it.cybion.model.twitter.User;
import org.apache.log4j.Logger;
import servlets.model.InfluenceUser;
import utils.PropertiesLoader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Given an input file with json of {@link it.cybion.influencers.ranking.RankedUser}s,
 * it parses and loads their full profiles from {@link it.cybion.influencers.cache.persistance.PersistenceFacade}.
 * <p/>
 * It writes the json output in a file with a list of {@link servlets.model.InfluenceUser}.
 *
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class InfluencersWriter extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(InfluencersWriter.class);

    private PropertiesLoader pl;

    private PersistenceFacade persistenceFacade;

    private Gson gson;

    private Properties properties;

    @Override
    public void init() throws ServletException {

        this.pl = new PropertiesLoader();
        this.properties = this.pl.loadGeneralProperties();

//        this.objectMapper = new ObjectMapper();
//        this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
//                false);

        this.gson = new Gson();


        initPersistenceFacade();
        LOGGER.info("servlet init");
    }

    private void initPersistenceFacade() throws ServletException {

        final String mongodbHost = this.properties.getProperty("mongodb_host");
        final String mongodbTwitterDb = this.properties.getProperty("mongodb_db");

        LOGGER.info("mongodb host " + mongodbHost);
        LOGGER.info("mongodb db " + mongodbTwitterDb);

        //init persistenceFacade
        try {
            this.persistenceFacade = PersistenceFacade.getInstance(mongodbHost, mongodbTwitterDb);
        } catch (PersistenceFacadeException e) {
            final String emsg =
                    "persistence facade exception - unknown host: " + mongodbHost + " - " +
                    e.getMessage() + "";
            throw new ServletException(emsg, e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.info("got request params: " + request.getParameterNames());
        final String fileName = request.getParameter("rankedUsersFileName");

        LOGGER.info("about to read file: " + fileName);
        LOGGER.info("and write output to path: '" + this.pl.getInfluencersResultsDirectory() + "'");
        final String influencersFilePath = this.pl.getInfluencersResultsDirectory() + fileName;
        LOGGER.info("full influencers filepath: '" + influencersFilePath + "'");

        String rankedUsersInputFile = this.pl.getRankedUsersResultsDirectory() + fileName;
        File rankedUsers = new File(rankedUsersInputFile);
        String rankedUsersContentJson = FileHelper.readFile(rankedUsers);

        List<RankedUser> rankedUserList = null;

        try {
            rankedUserList = this.gson.fromJson(rankedUsersContentJson,
                new TypeToken<List<RankedUser>>() {}.getType());
        } catch (JsonSyntaxException e) {
            String emsg = "failed reading json";
            throw new ServletException(emsg, e);
        }

        List<InfluenceUser> influenceUsers = new ArrayList<InfluenceUser>();

        for (RankedUser currentUser : rankedUserList) {

            final String screenName = currentUser.getScreenName();
            LOGGER.info("loading user '" + screenName + "'");
            final User fromPersistence = loadUserByScreenName(screenName);
            final InfluenceUser influencer = new InfluenceUser(currentUser, fromPersistence);
            influenceUsers.add(influencer);
        }

        String influencersAsJson = gson.toJson(influenceUsers);

        try {
            writeStringToFile(influencersFilePath, influencersAsJson);
        } catch (IOException e) {
            throw new ServletException(
                    "cant write to file '" + influencersFilePath + "' these contents: '" +
                    influencersAsJson + "'", e);
        }

        request.setAttribute("influencersFilePath", influencersFilePath);
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(
                "influencers-result.jsp");
        requestDispatcher.forward(request, response);
    }

    //TODO move to commons
    private void writeStringToFile(String filenamePath, String fileContent) throws IOException {

        final File destinationFile = new File(filenamePath);
        Files.write(fileContent, destinationFile, Charsets.UTF_8);
    }

    private User loadUserByScreenName(String screenName) {

        String userString = "";
        try {
            userString = this.persistenceFacade.getUser(screenName);
        } catch (UserNotPresentException e) {
            LOGGER.error("cant find '" + screenName + "' in local persistence: " + e.getMessage());
            //TODO find reason why we don't have the profile locally. we should have it
        }

        User user = null;
        try {
            user = this.gson.fromJson(userString, User.class);
        } catch (JsonSyntaxException e) {
            LOGGER.error("cant deserialize json to user: '" + userString + "'");
        }

        return user;
    }

}
