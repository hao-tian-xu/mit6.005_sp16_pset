package twitter;

//import com.sun.tools.corba.se.idl.constExpr.Times;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.time.Instant;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        Instant start;
        Instant end;
        if (tweets.size() == 0) start = Instant.now();
        else start = tweets.get(0).getTimestamp();
        end = start;
        for (Tweet tweet : tweets) {
            Instant current = tweet.getTimestamp();
            if (current.isBefore(start)) start = current;
            if (current.isAfter(end)) end = current;
        }
        return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        final Set<String> mentionedUsers = new HashSet<>();
        for (Tweet tweet : tweets) {
            mentionedUsers.addAll(getUsers(tweet));
        }
        return mentionedUsers;
    }

    private static Set<String> getUsers(Tweet tweet) {
        // init temp var
        final String notInitStamp = "$";
        char prevChar = ' ';
        final StringBuilder userName = new StringBuilder(notInitStamp);

        final Set<String> users = new HashSet<>();

        final String text = tweet.getText().toLowerCase();
        for (int i = 0; i < text.length(); i++) {
            char curChar = text.charAt(i);
            // userName start
            if (curChar == '@' && !isValid(prevChar))
                userName.replace(0, userName.length(), "");
            // single @ character
            else if (!userName.toString().contains(notInitStamp) && prevChar == '@'
                && !isValid(curChar))
                userName.replace(0, userName.length(), notInitStamp);
            // userName cont.
            else if (!userName.toString().contains(notInitStamp) && isValid(curChar))
                userName.append(curChar);
            // userName end
            else if (!userName.toString().contains(notInitStamp) && !isValid(curChar)) {
                users.add(userName.toString());
                userName.replace(0, userName.length(), notInitStamp);
            }
            prevChar = curChar;
        }
        if (!userName.toString().contains(notInitStamp) && userName.length() != 0)
            users.add(userName.toString());
        return users;
    }

    private static boolean isValid(char c) {
        return  (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c >= '0' && c <= '9') ||
                 c == '_' || c == '-';
    }

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public GitHub repository.
     */
}
