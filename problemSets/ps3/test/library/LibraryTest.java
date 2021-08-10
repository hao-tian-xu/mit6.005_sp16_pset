package library;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

/**
 * Test suite for Library ADT.
 */
@RunWith(Parameterized.class)
public class LibraryTest {

    /** assertion enabled */
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * Note: all the tests you write here must be runnable against any
     * Library class that follows the spec.  JUnit will automatically
     * run these tests against both SmallLibrary and BigLibrary.
     */

    /**
     * Implementation classes for the Library ADT.
     * JUnit runs this test suite once for each class name in the returned array.
     * @return array of Java class names, including their full package prefix
     */
    @Parameters(name="{0}")
    public static Object[] allImplementationClassNames() {
        return new Object[] { 
            "library.SmallLibrary", 
            "library.BigLibrary"
        }; 
    }

    /**
     * Implementation class being tested on this run of the test suite.
     * JUnit sets this variable automatically as it iterates through the array returned
     * by allImplementationClassNames.
     */
    @Parameter
    public String implementationClassName;    

    /**
     * @return a fresh instance of a Library, constructed from the implementation class specified
     * by implementationClassName.
     */
    public Library makeLibrary() {
        try {
            Class<?> cls = Class.forName(implementationClassName);
            return (Library) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /*
     * Testing strategy
     * ==================
     *  Library
     *      number of Book: 0, 1, >=2
     *      available copies: 1, >=2
     *
     *  buy()
     *      book: existing, new
     *
     *  isAvailable()
     *      copy: available, none, checked out, lost
     *
     *  allCopies()
     *      number of copies: 0, 1, >=2
     *      copy: available, none, checked out, lost
     *
     *  availableCopies()
     *      number of copies: 0, 1, >=2
     *      copy condition: available, checked out, lost
     *
     *  find()
     *      query: title, author, other
     *      number of matches: 0, 1, >=2
     *      if identical with different year
     */

    /* test data */
    private final String title1 = "NeverLand";
    private final String title2 = "MyBook";

    private final String author1 = "Armstrong";
    private final String author2 = "Louis";

    private final Book book1 = new Book(title1, Arrays.asList(author1, author2), 2022);
    private final Book book2 = new Book(title2, Arrays.asList(author1), 0);
    private final Book book3 = new Book(title2, Arrays.asList(author1), 2022);

    private final BookCopy copy = new BookCopy(book1);

    /** empty library */
    @Test
    public void testLibraryEmpty() {
        Library library = makeLibrary();

        assertEquals(Collections.emptySet(), library.availableCopies(book1));
        assertEquals(Collections.emptySet(), library.allCopies(book2));
        assertFalse(library.isAvailable(copy));
        assertEquals(Arrays.asList(), library.find(title1));
    }

    /** library buy one copy of a new book, however, lost */
    @Test
    public void testLibrarySingleBookSingleCopyLost() {
        Library library = makeLibrary();
        BookCopy copy1 = library.buy(book1);

        assertEquals(1, library.availableCopies(book1).size());

        library.lose(copy1);

        assertEquals(Collections.emptySet(), library.availableCopies(book1));
        assertEquals(Collections.emptySet(), library.allCopies(book1));
        assertFalse(library.isAvailable(copy1));
        assertEquals(Arrays.asList(), library.find(title1));
    }

    /** library buy multi copies of multi books, some checked out, some checked in,
        find author and find title
        find matches: 1, 2 */
    @Test
    public void testLibraryMultiBooksMultiCopiesCheckoutCheckin() {
        Library library = makeLibrary();
        BookCopy copy1_1 = library.buy(book1);
        BookCopy copy1_2 = library.buy(book1);
        BookCopy copy2 = library.buy(book2);

        library.checkout(copy1_1);
        library.checkout(copy2);
        library.checkin(copy2);

        assertEquals(new HashSet<>(Arrays.asList(copy1_1, copy1_2)), library.allCopies(book1));
        assertEquals(new HashSet<>(Arrays.asList(copy1_2)), library.availableCopies(book1));
        assertEquals(new HashSet<>(Arrays.asList(copy2)), library.allCopies(book2));
        assertEquals(new HashSet<>(Arrays.asList(copy2)), library.availableCopies(book2));

        assertTrue(library.find(author1).containsAll(Arrays.asList(book1, book2)));
        assertEquals(Arrays.asList(book1), library.find(title1));
    }

    /** library two identical books with different publication year */
    @Test
    public void testLibraryFindIdentical() {
        Library library = makeLibrary();
        library.buy(book2);
        library.buy(book3);

        assertEquals(Arrays.asList(book3, book2), library.find(title2));
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
