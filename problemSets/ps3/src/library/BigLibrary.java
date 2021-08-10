package library;


import java.util.*;

/**
 * BigLibrary represents a large collection of books that might be held by a city or
 * university library system -- millions of books.
 * 
 * In particular, every operation needs to run faster than linear time (as a function of the number of books
 * in the library).
 */
public class BigLibrary implements Library {

    // rep
    private final Map<Book, Set<BookCopy>> inLibrary;
    private final Map<Book, Set<BookCopy>> checkedOut;

    private final Map<String, Set<Book>> authorBook;
    private final Map<String, Set<Book>> titleBook;
    
    // rep invariant
    //      the intersection of BookCopy Sets of same Book in inLibrary and checkedOut is empty,
    //      all Books in checkedOut, authorBook, and titleBook are in inLibrary
    // abstraction function
    //      AF(inLibrary, checkedOut) =
    //      the collection of books inLibrary union checkedOut,
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out

    // safety from rep exposure argument
    //      all fields are private
    //      inLibrary and checkedOut are mutable but never passed in or returned

    // constructor
    public BigLibrary() {
        inLibrary = new HashMap<>();
        checkedOut = new HashMap<>();

        authorBook = new HashMap<>();
        titleBook = new HashMap<>();
    }
    
    // assert the rep invariant
    private void checkRep() {
        Set<Book> inLibraryKey = inLibrary.keySet();
        Set<Book> checkedOutKey = checkedOut.keySet();

        Set<Book> union = new HashSet<>(checkedOutKey);
        for (Set<Book> books : authorBook.values()) union.addAll(books);
        for (Set<Book> books : titleBook.values()) union.addAll(books);

        for (Book book : union) assert inLibraryKey.contains(book);
        for (Book book : inLibraryKey) {
            if (checkedOutKey.contains(book)) {
                Set<BookCopy> intersection = new HashSet<>(inLibrary.get(book));
                intersection.retainAll(checkedOut.get(book));
                assert intersection.equals(Collections.emptySet());
            }
        }
    }

    @Override
    public BookCopy buy(Book book) {
        BookCopy copy = new BookCopy(book);
        addCopy(book, copy, inLibrary);
        addBook(book.getTitle(), book, titleBook);
        for (String author : book.getAuthors()) addBook(author, book, authorBook);

        checkRep();
        return copy;
    }

    @Override
    public void checkout(BookCopy copy) {
        check(copy, inLibrary, checkedOut);
        checkRep();
    }

    @Override
    public void checkin(BookCopy copy) {
        check(copy, checkedOut, inLibrary);
        checkRep();
    }

    @Override
    public Set<BookCopy> allCopies(Book book) {
        Set<BookCopy> allCopies = new HashSet<>();
        if (inLibrary.containsKey(book)) allCopies.addAll(inLibrary.get(book));
        if (checkedOut.containsKey(book)) allCopies.addAll(checkedOut.get(book));
        return allCopies;
    }

    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> availableCopies = new HashSet<>();
        if (inLibrary.containsKey(book)) availableCopies.addAll(inLibrary.get(book));
        return availableCopies;
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        Book book = copy.getBook();
        return isInMap(book, copy, inLibrary);
    }

    @Override
    public List<Book> find(String query) {
        Set<Book> findSet = new HashSet<>();

        if (titleBook.containsKey(query)) findSet.addAll(titleBook.get(query));
        if (authorBook.containsKey(query)) findSet.addAll(authorBook.get(query));

        List<Book> find = new ArrayList<>(findSet);
        find.sort((book1, book2) -> book2.getYear() - book1.getYear());

        checkRep();
        return find;
    }

//    @Override
//    public List<Book> find(String query) {
//        Set<Book> findSet = new HashSet<>();
//
//        for (Book book : inLibrary.keySet())
//            if (book.getAuthors().contains(query) || book.getTitle().contains(query))
//                findSet.add(book);
//
//        List<Book> find = new ArrayList<>(findSet);
//        find.sort((book1, book2) -> book2.getYear() - book1.getYear());
//
//        checkRep();
//        return find;
//    }
    
    @Override
    public void lose(BookCopy copy) {
        Book book = copy.getBook();
        if (isInMap(book, copy, checkedOut)) checkedOut.get(book).remove(copy);
        if (isAvailable(copy)) inLibrary.get(book).remove(copy);
        removeBook(book);
        checkRep();
    }

    /** remove a book if no copy in both maps */
    private void removeBook(Book book) {
        String title = book.getTitle();
        List<String> authors = book.getAuthors();

        Set<BookCopy> copies = new HashSet<>();
        if (checkedOut.containsKey(book)) copies.addAll(checkedOut.get(book));
        if (inLibrary.containsKey(book)) copies.addAll(inLibrary.get(book));

        if (copies.equals(Collections.emptySet())) {    // no copy in library, remove from 4 rep maps
            checkedOut.remove(book);
            inLibrary.remove(book);
            if (titleBook.containsKey(title)) titleBook.get(title).remove(book);
            for (String author : authors)
                if (authorBook.containsKey(author)) authorBook.get(author).remove(book);
        }
    }

    /** add copy of book to map */
    private void addCopy(Book book, BookCopy copy, Map<Book, Set<BookCopy>> map) {
        if (map.containsKey(book)) map.get(book).add(copy);
        else map.put(book, new HashSet<>(Arrays.asList(copy)));
    }

    /** add book of string(title or author) to map */
    private void addBook(String string, Book book, Map<String, Set<Book>> map) {
        if (map.containsKey(string)) map.get(string).add(book);
        else map.put(string, new HashSet<>(Arrays.asList(book)));
    }

    /** move a copy from from to to */
    private void check(BookCopy copy, Map<Book, Set<BookCopy>> from, Map<Book, Set<BookCopy>> to) {
        Book book = copy.getBook();
        if (isInMap(book, copy, from)) {
            from.get(book).remove(copy);
            addCopy(book, copy, to);
        }
    }

    /** if copy of book in map */
    private boolean isInMap(Book book, BookCopy copy, Map<Book, Set<BookCopy>> map) {
        return map.containsKey(book) && map.get(book).contains(copy);
    }



    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    // @Override
    // public boolean equals(Object that) {
    //     throw new RuntimeException("not implemented yet");
    // }
    // 
    // @Override
    // public int hashCode() {
    //     throw new RuntimeException("not implemented yet");
    // }



    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
