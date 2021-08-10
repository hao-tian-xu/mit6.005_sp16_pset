package library;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test suite for BookCopy ADT.
 */
public class BookCopyTest {
    /** assertion enabled */
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * Testing strategy
     * ==================
     *  getCondition:
     *      this copy: GOOD, DAMAGED
     *  setCondition:
     *      GOOD, DAMAGED
     *  toString:
     *      good, damaged
     */

    @Test
    public void testBookCopySingleGood() {
        Book book = new Book("NeverLand", Arrays.asList("Armstrong", "Louis"), 2022);
        BookCopy copy = new BookCopy(book);
        copy.setCondition(BookCopy.Condition.GOOD);

        assertEquals(book, copy.getBook());
        assertEquals(BookCopy.Condition.GOOD, copy.getCondition());

        String rep = copy.toString();
        assertTrue(rep.contains(copy.getBook().toString()) && rep.contains("good"));
    }

    @Test
    public void testBookCopyMultiDamaged() {
        Book book = new Book("NeverLand", Arrays.asList("Armstrong", "Louis"), 2022);
        BookCopy copy = new BookCopy(book);
        BookCopy copy2 = new BookCopy(book);
        copy2.setCondition(BookCopy.Condition.DAMAGED);

        assertEquals(book, copy.getBook());
        assertEquals(book, copy2.getBook());
        assertEquals(BookCopy.Condition.GOOD, copy.getCondition());
        assertEquals(BookCopy.Condition.DAMAGED, copy2.getCondition());

        String rep = copy.toString();
        String rep2 = copy2.toString();
        assertTrue(rep.contains(copy.getBook().toString()) && rep.contains("good"));
        assertTrue(rep2.contains(copy2.getBook().toString()) && rep2.contains("damaged"));
    }
    



    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
