package servlet;

import it.cybion.commons.FileHelper;
import it.cybion.influencers.ranking.RankedUser;
import it.cybion.model.twitter.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;

import org.codehaus.jackson.map.DeserializationConfig;
import twitter4j.TwitterException;
import twitter4j.internal.json.DataObjectFactoryUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static com.mongodb.util.MyAsserts.assertTrue;
import static org.testng.Assert.assertEquals;

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
    }
}
