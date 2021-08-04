package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

/**
 ** getTimespan Testing Strategy:
 ** Partition:
 *      List.length(): 1, 2, >=3
 *      duplicate tweets
 *
 ** getMentionedUsers Testing Strategy:
 ** Partition:
 *      no mentioned
 *      valid character before @
 *      case-insensitive
 *      duplicate username
 *
 ** Cover each part.
 *
 */
public class ExtractTest {

    /* assertions enabled */
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
    private static final Tweet tweet4 = new Tweet(3, "stupidity", "@Hidldidl Hi didl", d4);

    /* getTimespan Test */
    /** single tweet */
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    /** two tweets */
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    /** multi tweets */
    @Test
    public void testGetTimespanMultiTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));

        assertEquals("expected start", d3, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    /** duplicate tweets */
    @Test
    public void testGetTimespanDuplicateTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet1));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    /* getMentionedUsers Test */
    /** no mentioned */
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet2));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    /**
     * valid character before @
     * case-insensitive
     * duplicate username
     */
    @Test
    public void testGetMentionedUsersMulti() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2, tweet3, tweet4));

        Set<String> expected = new HashSet<>();
        Set<String> result = new HashSet<>();
        expected.add("whoami");
        expected.add("hidldidl");
        for (String mentionedUser : mentionedUsers) {
            result.add(mentionedUser.toLowerCase(Locale.ROOT));
        }
        assertEquals(expected, result);
    }


    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public GitHub repository.
     */

}
