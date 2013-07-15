package servlet;

import com.google.gson.Gson;
import it.cybion.commons.FileHelper;
import it.cybion.influencers.cache.persistance.PersistenceFacade;
import it.cybion.influencers.cache.persistance.exceptions.PersistenceFacadeException;
import it.cybion.influencers.cache.persistance.exceptions.UserNotPresentException;
import it.cybion.influencers.ranking.RankedUser;
import it.cybion.model.twitter.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class PerfTestCase {

    private static final Logger LOGGER = Logger.getLogger(PerfTestCase.class);

    private PersistenceFacade persistenceFacade;

    @BeforeClass
    public void init() throws PersistenceFacadeException {
        this.persistenceFacade = PersistenceFacade.getInstance("localhost", "twitter");
    }

    @Test(enabled = false)
    public void shouldLoadLotsOfTimesTheSameUser() throws IOException {

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Gson gson = new Gson();

        File lot = FileHelper.readFromClasspath("/lots-of-users.json");
        String usersJson = FileHelper.readFile(lot);

        List<RankedUser> rankedUsers = new ArrayList<RankedUser>();
        try {
            rankedUsers = om.readValue(usersJson, new TypeReference<List<RankedUser>>() {});
        } catch (IOException e) {
            String esmg = "cant read json from file, having this content: " + usersJson;
            LOGGER.error(esmg);
        }

        LOGGER.info(rankedUsers.size() + " on file");

        List<User> users = new ArrayList<User>(rankedUsers.size());

        for (RankedUser user: rankedUsers) {

            String screenName = user.getScreenName();

            try {
                String json = persistenceFacade.getUser(screenName);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                User completeProfile = null;

                try {
                    completeProfile = om.readValue(json, User.class);
                } catch (IOException e) {
                    LOGGER.error("cant deserialize " + e.getMessage() + " " + json );
                }

//                try {
//                    completeProfile = gson.fromJson(json, User.class);
//                } catch (Exception e) {
//                    LOGGER.error("gson err: "+  e.getMessage() + " content: " + json);
//                }

                users.add(completeProfile);
            } catch (UserNotPresentException e) {
                LOGGER.error("user '" + screenName + "' not present: " + e.getMessage());
            }
        }

        String influencersAsJson = "";

        LOGGER.info("free memory: " + Runtime.getRuntime().freeMemory());
        LOGGER.info("non empty profiles: " + users.size());

        try {
            influencersAsJson = om.writeValueAsString(users);
        } catch (IOException e) {
            LOGGER.error("error " + e.getMessage());
        } catch (OutOfMemoryError e) {
            LOGGER.error("");
        }
        LOGGER.info("serialized: " + influencersAsJson);
        assertTrue(true);
    }

    @Test (enabled = false)
    public void jsonPerfTest() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        int iterations = 100000;
        for (int i = 0; i < iterations; i++) {
            String jsonUser = "{\n" +
                              "            \"id\": " + i +",\n" +
                              "            \"createdAt\": null,\n" +
                              "            \"defaultProfile\": false,\n" +
                              "            \"defaultProfileImage\": false,\n" +
                              "            \"description\": null,\n" +
                              "            \"entities\": null,\n" +
                              "            \"favouritesCount\": 0,\n" +
                              "            \"followers\": null,\n" +
                              "            \"followersCount\": 0,\n" +
                              "            \"friends\": null,\n" +
                              "            \"friendsCount\": 0,\n" +
                              "            \"lang\": null,\n" +
                              "            \"listedCount\": 0,\n" +
                              "            \"location\": null,\n" +
                              "            \"name\": null,\n" +
                              "            \"screenName\": \"fakeName\",\n" +
                              "            \"statusesCount\": 0,\n" +
                              "            \"timeZone\": null,\n" +
                              "            \"token\": null,\n" +
                              "            \"tokenSecret\": null,\n" +
                              "            \"url\": null,\n" +
                              "            \"utcOffset\": 0,\n" +
                              "            \"verified\": false,\n" +
                              "            \"protected\": false,\n" +
                              "            \"contributorsEnabled\": false,\n" +
                              "            \"geoEnabled\": false\n" +
                              "        }";
            try {
                User user = objectMapper.readValue(jsonUser, User.class);
                LOGGER.info(user.getScreenName());

            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

}
