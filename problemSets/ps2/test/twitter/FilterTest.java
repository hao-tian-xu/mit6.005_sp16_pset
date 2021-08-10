package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * writtenBy
 *  Partitions:
 *      number of tweets: 0, 1, >=2
 *      number of tweets containing the username: 0, 1, >=2
 *      case-insensitive
 *
 * inTimespan
 *  Partitions:
 *      number of tweets: 0, 1, >=2
 *      timespan: 0, >0
 *      number of tweets in the timespan: 0, 1, >=2
 *      if tweet at the start or end of the timespan exists
 *
 * containing
 *  Partitions:
 *      number of tweets: 0, 1, >=2
 *      number of words: 0, 1, >=2
 *      number of tweets contain certain words: 0, 1, >=2
 *      if sub-word
 *      case-insensitive
 *
 */
public class FilterTest {

    /* assertion enabled */
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /* test data */
    private static final Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
    private static final Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d3 = Instant.parse("2017-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "rivest talking in 30 minutes #hype", d3);
    
    /* writtenBy */
    /** None tweet */
    @Test
    public void testWrittenByNoneTweet() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "alyssa");

        assertEquals("expected empty list", 0, writtenBy.size());
    }

    /** Single tweet and none result */
    @Test
    public void testWrittenBySingleTweetNoneResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2), "alyssa");

        assertEquals("expected empty list", 0, writtenBy.size());
    }

    /** Multi tweets and single result */
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    /** Multi tweets and multi results */
    @Test
    public void testWrittenByMultipleTweetsMultipleResultsCase() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");

        assertEquals("expected double list", 2, writtenBy.size());
        assertTrue("expected list to contain tweets", writtenBy.containsAll(Arrays.asList(tweet1, tweet3)));
    }

    /* inTimespan */
    /** None tweet */
    @Test
    public void  testInTimespanNoneTweet() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(testStart, testEnd));

        assertEquals("expected empty list", 0, inTimespan.size());
    }

    /** Single tweet, 0 timespan, and none result */
    @Test
    public void testInTimespanSingleTweetZeroTimespanNoneResult() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(testStart, testStart));

        assertEquals("expected empty list", 0, inTimespan.size());
    }

    /** Multi tweets and single result */
    @Test
    public void testInTimespanMultiTweetsSingleResult() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet3), new Timespan(testStart, testEnd));

        assertEquals("expected singleton list", 1, inTimespan.size());
        assertTrue("expected list to contain tweet", inTimespan.contains(tweet1));
    }

    /** Multi tweets, multi results, and tweet at the end of timespan */
    @Test
    public void testInTimespanMultipleTweetsTweetAtEndMultipleResults() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    /* containing */
    /** None tweet */
    @Test
    public void testContainingNoneTweet() {
        List<Tweet> containing = Filter.containing(Arrays.asList(), Arrays.asList("talk"));

        assertEquals("expected empty list", 0, containing.size());
    }

    /** Single tweet, none word and none result */
    @Test
    public void testContainingSingleTweetNoneWordNoneResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1), Arrays.asList());

        assertEquals("expected empty list", 0, containing.size());
    }

    /** Multi tweet, single word, sub-word and single result */
    @Test
    public void testContainingMultiTweetsSingleWordSubWordSingleResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet3), Arrays.asList("talk"));

        assertEquals("expected singleton list", 1, containing.size());
        assertTrue("expected list to contain tweet", containing.contains(tweet1));
    }

    /** Multi tweets, multi words and multi results */
    @Test
    public void testContainingMultiTweetsMultiWordsMultiResultsCase() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("talk", "Talking"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2, tweet3)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
        assertEquals("expected same order", 1, containing.indexOf(tweet2));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
