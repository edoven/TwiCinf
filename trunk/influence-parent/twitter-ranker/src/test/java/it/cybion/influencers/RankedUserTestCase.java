package it.cybion.influencers;

import it.cybion.influencers.ranking.RankedUser;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class RankedUserTestCase {

    private static final Logger LOGGER = Logger.getLogger(RankedUserTestCase.class);

    @Test
    public void shouldRankUsers() {

        RankedUser first = new RankedUser("a", 1, 2, 3, 4, 5.0D, 0.0D);
        RankedUser second = new RankedUser("b", 1, 2, 3, 4, 5.0D, 2.0D);
        RankedUser nullUser = null;

        List<RankedUser> unsorted = new ArrayList<RankedUser>();
        unsorted.add(second);
        unsorted.add(nullUser);
        LOGGER.info("unsorted");

        for (RankedUser user : unsorted) {
            LOGGER.info("user: " + user);
        }

        Collections.sort(unsorted);
        List<RankedUser> sorted = unsorted;
        LOGGER.info("sorted");



    }

}
