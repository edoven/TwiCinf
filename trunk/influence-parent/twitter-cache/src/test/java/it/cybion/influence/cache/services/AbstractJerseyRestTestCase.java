package it.cybion.influence.cache.services;

import com.google.inject.servlet.GuiceFilter;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public abstract class AbstractJerseyRestTestCase {

    protected static final int PORT = 9995;

    private static final Logger logger = Logger.getLogger(AbstractJerseyRestTestCase.class);

    //some shared variable to be accessed from extending classes
    protected static final String root_dir = "";

    private static final String base_uri_str = "http://localhost:%d/rest/";

    protected final URI base_uri;

    private GrizzlyWebServer server;

    private int port;

    protected AbstractJerseyRestTestCase(int port) {
        try {
            base_uri = new URI(String.format(base_uri_str, port));
        } catch (URISyntaxException urise) {
            throw new RuntimeException(urise);
        }
        this.port = port;
    }

    @BeforeClass
    public void setUpAbstract() throws Exception {
        startFrontendService();
        logger.info("Grizzly started");
    }

    protected void startFrontendService() throws IOException {
        //new solution from https://github.com/dpalmisano/NoTube-Beancounter-2.0/blob/master/platform/src/test/java/tv/notube/platform/AbstractJerseyTestCase.java
        server = new GrizzlyWebServer(port);
        ServletAdapter ga = new ServletAdapter();
        ga.addServletListener(TestServiceConfig.class.getName());
        ga.setServletPath("/");
        ga.addFilter(new GuiceFilter(), "filter", null);
        server.addGrizzlyAdapter(ga, null);
        server.start();
    }

    @AfterClass
    public void tearDownAbstract() {
        server.stop();
        logger.info("Grizzly stopped");
    }

}
