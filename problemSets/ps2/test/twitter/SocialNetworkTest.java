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
 *      number of graph entries: 0, 1, >=2
 *      if empty entry exists
 *      if multi people have same influences
 *
 */
public class SocialNetworkTest {

    /** assertion enabled */
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*********************
     * guessFollowsGraph *
     *********************/

    /* init test data */
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2012-02-17T11:00:00Z");
    private static final Instant d4 = Instant.parse("2012-02-19T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much? @whoami", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "hi@hotmail.com rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "stupidity", "@hidldidl hi didl", d3);
    private static final Tweet tweet4 = new Tweet(4, "stupidity", "@Hidldidl Hi didl", d4);

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

    /***************
     * influencers *
     ***************/

    /** init influencers test data */
    final Map<String, Set<String>> followsGraph1 = new HashMap<>();
    final Map<String, Set<String>> followsGraph2 = new HashMap<>();
    final Set<String> follows1 = new HashSet<>();
    final Set<String> follows2 = new HashSet<>();
    final Set<String> follows3 = new HashSet<>();

    private void initInfluencersTestData() {
        follows1.add("whoami");
        follows1.add("hidldidl");
        follows2.add("hidldidl");
        follows3.add("bbitdiddle");
        follows3.add("whoami");
        followsGraph1.put("alyssa", follows1);
        followsGraph1.put("whoami", follows2 );
        followsGraph2.putAll(followsGraph1);
        followsGraph2.put("hidldidl", follows3);
    }


    /** None entry */
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    /** Single entry and empty entry */
    @Test
    public void testInfluencersSingleEntryEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alyssa", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);

        assertEquals(Arrays.asList("alyssa"), influencers);
    }

    /** Multi entries and no same influences */
    @Test
    public void testInfluencersMultiEntriesNoSame() {
        initInfluencersTestData();
        List<String> influencers = SocialNetwork.influencers(followsGraph1);

        assertEquals(Arrays.asList("hidldidl", "whoami", "alyssa"), influencers);
    }

    /** Multi entries and same influences */
    @Test
    public void testInfluencersMultiEntriesMultiMax() {
        initInfluencersTestData();
        List<String> influencers = SocialNetwork.influencers(followsGraph2);

        assertTrue(Objects.equals(Arrays.asList("hidldidl", "whoami", "bbitdiddle", "alyssa"), influencers)
            || Objects.equals(Arrays.asList("whoami", "hidldidl", "bbitdiddle", "alyssa"), influencers));
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
