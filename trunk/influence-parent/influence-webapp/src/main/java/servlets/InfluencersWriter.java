package servlets;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import it.cybion.commons.FileHelper;
import it.cybion.influencers.cache.persistance.PersistenceFacade;
import it.cybion.influencers.cache.persistance.exceptions.PersistenceFacadeException;
import it.cybion.influencers.ranking.RankedUser;
import it.cybion.model.twitter.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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

    private Properties properties;

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {

        this.pl = new PropertiesLoader();
        this.properties = this.pl.loadGeneralProperties();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

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

        List<RankedUser> rankedUserList = this.objectMapper.readValue(rankedUsersContentJson,
                new TypeReference<List<RankedUser>>() {});

        List<InfluenceUser> influenceUsers = new ArrayList<InfluenceUser>();

        for (RankedUser currentUser : rankedUserList) {
            //TODO load user from persistence
            final User fromPersistence = new User.Users(999L).withScreenName("fakeName").build();
            final InfluenceUser influencer = new InfluenceUser(currentUser, fromPersistence);
            influenceUsers.add(influencer);
        }

        final String influencersAsJson = this.objectMapper.writeValueAsString(influenceUsers);

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

}
