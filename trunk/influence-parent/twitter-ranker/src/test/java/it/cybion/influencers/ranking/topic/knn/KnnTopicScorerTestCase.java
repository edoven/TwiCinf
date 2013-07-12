package it.cybion.influencers.ranking.topic.knn;

import it.cybion.influencers.ranking.topic.TopicScorer;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class KnnTopicScorerTestCase {

    @Test (enabled = false)
    public void testName() {

        List<String> topicTweets = buildInTopic(100);
        List<String> outOfTopicTweets = buildOffTopic(100);
        int k = 10;
        TopicScorer t = new KnnTopicScorer(topicTweets,outOfTopicTweets,  k);




    }

    private List<String> buildInTopic(int i) {

        List<String> toReturn = new ArrayList<String>();

        int counter = 0;
        while (counter < i) {
            toReturn.add("this is a tweet in topic");
            counter++;
        }

        return toReturn;
    }

    private List<String> buildOffTopic(int i) {

        List<String> toReturn = new ArrayList<String>();

        int counter = 0;
        while (counter < i) {
            toReturn.add("this is a completely different tweet");
            counter++;
        }
        return toReturn;
    }
}
