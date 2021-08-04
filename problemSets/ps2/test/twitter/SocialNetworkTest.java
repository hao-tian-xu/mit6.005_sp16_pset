package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.*;

import org.junit.Test;


/**
 * guessFollowsGraph
 *  Partitions:
 *      number of tweets: 0, 1, >=2
 *      number of @-mentions per tweet: 0, >=1
 *
 * influencers
 *  Partitions:
 *      number of graph keys: 0, 1, >=2
 *      if multi people have same influences
 *
 */
public class SocialNetworkTest {

    /** assertion enabled */
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /* init test data */
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2012-02-17T11:00:00Z");
    private static final Instant d4 = Instant.parse("2012-02-19T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much? @whoami", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "hi@hotmail.com rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "stupidity", "@hidldidl hi didl", d3);
    private static final Tweet tweet4 = new Tweet(4, "stupidity", "@Hidldidl Hi didl", d4);

    /* guessFollowsGraph */
    /** None tweet */
    @Test
    public void testGuessFollowsGraphNoneTweet() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    /** Single tweet and none mention */
    @Test
    public void testGuessFollowsGraphSingleTweetNoneMention() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2));

        assertTrue("expected empty graph or key with empty value", followsGraph.isEmpty() || followsGraph.get("bbitdiddle").isEmpty());
    }

    /** Multi tweets and multi mentions */
    @Test
    public void testGuessFollowsGraphMultiTweetMultiMention() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4));

        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertEquals(1, followsGraph.get("alyssa").size());
        assertTrue(followsGraph.get("alyssa").contains("whoami"));
        assertEquals(1, followsGraph.get("alyssa").size());
        assertTrue(followsGraph.get("stupidity").contains("hidldidl"));
        assertTrue(!followsGraph.containsKey("bbitdiddle") || followsGraph.get("bbitdiddle").isEmpty());
    }

    /* influencers */

    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
