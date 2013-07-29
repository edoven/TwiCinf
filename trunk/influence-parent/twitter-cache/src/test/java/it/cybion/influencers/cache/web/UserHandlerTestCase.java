package it.cybion.influencers.cache.web;

import it.cybion.influencers.cache.exceptions.LimitExceededException;
import it.cybion.influencers.cache.web.exceptions.ProtectedUserException;
import it.cybion.influencers.cache.web.exceptions.UserHandlerException;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotNull;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class UserHandlerTestCase {

    private static final Logger LOGGER = Logger.getLogger(UserHandlerTestCase.class);

    @Test (enabled = false)
    public void shouldBeGreen() throws TwitterException {

        Token userToken = new Token("962689441-yrFTbTzI3nAQ9sIMLnxLexyLWGAfZzhXCosTwuWp",
                "elPwBu9NeAoGXunIl1wyPJDsSYgLWlFQXbXR8C2KQc");
        Token appToken = new Token("Bam9RwjprVxWJd8TdhwQOg",
                "q1wzu5hn9HjYhEvYvPPBSIHWcfHIYJZoGnRlnD14D0");

        UserHandler userHandler = new UserHandler(appToken, userToken);
        assertNotNull(userHandler);

        while (true) {
            List<String> tweets = new ArrayList<String>();
            List<String> tweetsWithMaxId = null;
            try {
                tweetsWithMaxId = userHandler.getTweetsWithMaxId(8102582L, -1L);
            } catch (ProtectedUserException e) {
                LOGGER.error("protected user: " + e.getMessage());
            } catch (LimitExceededException e) {
                LOGGER.error("limit exceeded: " + e.getMessage());
            } catch (UserHandlerException e) {
                LOGGER.error("user handler exc: " + e.getMessage());
            }
            tweets.addAll(tweetsWithMaxId);
        }
    }

}
