package library;

import java.util.Collections;
import java.util.List;

/**
 * Book is an immutable type representing an edition of a book -- not the physical object, 
 * but the combination of words and pictures that make up a book.  Each book is uniquely
 * identified by its title, author list, and publication year.  Alphabetic case and author 
 * order are significant, so a book written by "Fred" is different than a book written by "FRED".
 */
public class Book {

    //  Rep
    final private String title;
    final private List<String> authors;
    final private Integer year;

    //  Rep Invariant
    //      title contains at least one non-space character
    //      authors is not empty and each element contains at least one non-space character
    //      year is a non-negative integer
    //  Abstraction Function
    //      AF(title, authors, year) = a book with title, written by author,
    //                                 published at year
    //  safety from rep exposure argument
    //      All fields are private
    //      String title and Integer year are immutable
    //      List authors is implemented as an immutable list  in Book()
    
    /**
     * Make a Book.
     * @param title Title of the book. Must contain at least one non-space character.
     * @param authors Names of the authors of the book.  Must have at least one name, and each name must contain 
     * at least one non-space character.
     * @param year Year when this edition was published in the conventional (Common Era) calendar.  Must be nonnegative. 
     */
    public Book(String title, List<String> authors, int year) {
        this.title = title;
        this.authors = Collections.unmodifiableList(authors);
        this.year = year;
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
        assert containsAtLeastOneNoneSpaceChar(title);
        assert authors.size() > 0;
        for (String author : authors)
            assert containsAtLeastOneNoneSpaceChar(author);
        assert year >= 0;
    }

    private boolean containsAtLeastOneNoneSpaceChar(String name) {
        for (int i = 0; i < name.length(); i++)
            if (name.charAt(i) != ' ') return true;
        return false;
    }

    /**
     * @return the title of this book
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @return the authors of this book
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * @return the year that this book was published
     */
    public int getYear() {
        return year;
    }

    /**
     * @return human-readable representation of this book that includes its title,
     *    authors, and publication year
     */
    public String toString() {
        checkRep();
        return "title: " + title + ", authors: " + authors + ", publication year: " + year;
    }

    /**
     * @param that another instance of Book
     * @return if two books have same title, authors and year, return true
     */
    @Override
    public boolean equals(Object that) {
        return that instanceof Book && this.sameValue((Book) that);
    }

    private boolean sameValue(Book that) {
        if (title.equals(that.getTitle()) && authors.equals(that.getAuthors()) && year.equals(that.getYear()))
            return true;
        return false;
    }

    /**
     * @return sum of hashcode of title, year and each author
     */
    @Override
    public int hashCode() {
        int hashCode = 0;
        for (String author : authors) hashCode += author.hashCode();
        hashCode += (title.hashCode() + year.hashCode());
        return hashCode;
    }



    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
