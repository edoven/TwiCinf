package it.cybion.influence.cache.services;

import it.cybion.commons.web.http.CybionHttpClient;
import it.cybion.commons.web.responses.ExternalStringResponse;
import it.cybion.commons.web.services.exceptions.CybionHttpException;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

import javax.ws.rs.core.MediaType;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class StatusServiceTestCase extends AbstractJerseyRestTestCase {

    private static final Logger logger = Logger.getLogger(StatusServiceTestCase.class);

    public StatusServiceTestCase() {
        super(AbstractJerseyRestTestCase.PORT);
    }

    @Test
    public void shouldSeeThatStatusIsOk() throws CybionHttpException {
        final String query = "status/now";
        String clusteringPath = base_uri + query;

        ExternalStringResponse stringResponse = null;

        final Map<String, String> requestHeaderMap = Maps.newHashMap();
        requestHeaderMap.put("Accept", MediaType.APPLICATION_JSON);
        stringResponse = CybionHttpClient.performGet(clusteringPath, requestHeaderMap);
        assertEquals(stringResponse.getCode(), 200);
        assertNotNull(stringResponse.getObject());
        //TODO assert json object contains what we expect
        logger.info("received response: \n" + stringResponse.getObject());
    }
}
