package servlets;

import it.cybion.influencers.cache.persistance.PersistenceFacade;
import it.cybion.influencers.cache.persistance.exceptions.PersistenceFacadeException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import utils.PropertiesLoader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private PersistenceFacade persistenceFacade;

    private Properties properties;

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {

        final PropertiesLoader pl = new PropertiesLoader();
        this.properties = pl.loadGeneralProperties();

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
        String fileName = request.getParameter("fileName");
        LOGGER.info("got parameter value: " + fileName);

        //get input param from params

        //get output param

        String outputFilePath = fileName + " fake! TODO";
        request.setAttribute("outputFilePath", outputFilePath);
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(
                "influencers-result.jsp");
        requestDispatcher.forward(request, response);
    }

}
