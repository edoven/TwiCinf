package it.cybion.influence.cache.services;

import com.google.inject.Inject;
import it.cybion.commons.web.responses.ResponseStatus;
import it.cybion.commons.web.responses.StringResponse;
import it.cybion.commons.web.services.base.JsonService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
@Path("rest/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusService extends JsonService {

    private static final Logger logger = Logger.getLogger(StatusService.class);

    @Inject
    public StatusService() {
    }

    @GET
    @Path("/now")
    public Response getStatus() {
        final DateTime now = new DateTime();
        logger.info("checking services status at " + now.toString());
        final Response.ResponseBuilder rb = Response.ok();
            rb.entity(new StringResponse(ResponseStatus.OK,
                    "services up and running as of '" + now.toString() + "'"));
        return rb.build();
    }
}
