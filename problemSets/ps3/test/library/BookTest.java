package library;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for Book ADT.
 */
public class BookTest {
    /** assertion enabled */
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * Testing strategy
     * ==================
     *  title:
     *      no partition
     *  authors:
     *      number: 1, >=2
     *  year:
     *      0, >=1
     */

    /** one author and year 0 CE */
    @Test
    public void testBookOneAuthorYearZero() {
        Book book = new Book("MyBook", Arrays.asList("Alexander"), 0);
        assertEquals("MyBook", book.getTitle());
        assertEquals(0, book.getYear());

        assertEquals(1, book.getAuthors().size());
        assertTrue(book.getAuthors().contains("Alexander"));

        String rep = book.toString();
        assertTrue(rep.contains("MyBook") && rep.contains("Alexander") && rep.contains("0"));
    }

    /** multiple authors */
    @Test
    public void testBookMultiAuthors() {
        Book book = new Book("NeverLand", Arrays.asList("Armstrong", "Louis"), 2022);
        assertEquals("NeverLand", book.getTitle());
        assertEquals(2022, book.getYear());

        assertEquals(2, book.getAuthors().size());
        assertTrue(book.getAuthors().containsAll(Arrays.asList("Armstrong", "Louis")));

        String rep = book.toString();
        assertTrue(rep.contains("NeverLand") && rep.contains("2022")
                && rep.contains("Armstrong") && rep.contains("Louis"));
    }
    


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
