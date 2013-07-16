package servlet;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cybion.commons.FileHelper;
import it.cybion.influencers.ranking.RankedUser;
import it.cybion.model.twitter.User;
import org.apache.log4j.Logger;

import org.codehaus.jackson.map.DeserializationConfig;
import servlets.model.InfluenceUser;
import twitter4j.TwitterException;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.UserFollowersExclusionStrategyC;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class SerializationTestCase {

    private static final Logger LOGGER = Logger.getLogger(SerializationTestCase.class);

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setUpJson() {

        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
    }

    @Test
    public void shouldSerializeDeserializeProperly() throws IOException, TwitterException {

        String jsonUser = "";
        File userFile = FileHelper.readFromClasspath("/twitter-user.json");
        jsonUser = FileHelper.readFile(userFile);

        LOGGER.info("deserializing user from json: " + jsonUser);

        User expectedUser = this.objectMapper.readValue(jsonUser, User.class);
        String serializedUser = this.objectMapper.writeValueAsString(expectedUser);
        User actualUser = this.objectMapper.readValue(serializedUser, User.class);
        assertEquals(actualUser, expectedUser);

        String screenName = "user";
        int followersCount = 0;
        int originalTweets = 0;
        double topicTweetsCount = 0;
        double topicTweetsRatio = 0;
        double meanRetweetsCount = 0;
        double rank = 0;

        RankedUser rankedUser = new RankedUser(screenName, followersCount, originalTweets,
                topicTweetsCount, topicTweetsRatio, meanRetweetsCount, rank);

        InfluenceUser user = new InfluenceUser(rankedUser, expectedUser);
        String serializedInfluencer = this.objectMapper.writeValueAsString(user);
        InfluenceUser deSerializedInfluencer = this.objectMapper.readValue(serializedInfluencer,
                InfluenceUser.class);

        assertEquals(deSerializedInfluencer, user);

        List<InfluenceUser> influencersList = new ArrayList<InfluenceUser>();
        int limit = 2700;
        for (int i = 0; i < limit; i++) {
            influencersList.add(user);
        }
        String influencersJson = objectMapper.writeValueAsString(influencersList);
        LOGGER.info("" + influencersJson);
    }

    @Test
    public void shouldIgnoreSomeFields() throws IOException {

        //Fri Dec 21 19:22:16 +0000 2012
        String pattern = "EEE MMM dd hh:mm:ss Z y";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        final Gson gson = new GsonBuilder().setExclusionStrategies(
                new UserFollowersExclusionStrategyC()).setFieldNamingPolicy(
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setDateFormat(
                simpleDateFormat.toPattern()).create();
        final File userJsonFile = FileHelper.readFromClasspath("/user-from-mongodb.json");
        final String userJson = FileHelper.readFile(userJsonFile);

        final User deserialized = gson.fromJson(userJson, User.class);
        assertNotNull(deserialized);
        assertEquals(deserialized.getFollowers().size(), 0);
        assertEquals(deserialized.getFriends().size(), 0);
    }
}
