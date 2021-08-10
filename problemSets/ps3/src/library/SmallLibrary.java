package library;

import java.util.*;

/** 
 * SmallLibrary represents a small collection of books, like a single person's home collection.
 */
public class SmallLibrary implements Library {

    // This rep is required! 
    // Do not change the types of inLibrary or checkedOut, 
    // and don't add or remove any other fields.
    // (BigLibrary is where you can create your own rep for
    // a Library implementation.)

    // rep
    private final Set<BookCopy> inLibrary;
    private final Set<BookCopy> checkedOut;
    
    // rep invariant:
    //      the intersection of inLibrary and checkedOut is the empty set
    //
    // abstraction function:
    //      represents the collection of books inLibrary union checkedOut,
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out

    // safety from rep exposure argument
    //      all fields are private
    //      inLibrary and checkedOut are mutable but never passed in or returned
    
    public SmallLibrary() {
        this.inLibrary = new HashSet<>();
        this.checkedOut = new HashSet<>();
    }
    
    // assert the rep invariant
    private void checkRep() {
        Set<BookCopy> intersection = new HashSet<>(inLibrary);
        intersection.retainAll(checkedOut);
        assert intersection.equals(Collections.emptySet());
    }

    @Override
    public BookCopy buy(Book book) {
        BookCopy copy = new BookCopy(book);
        inLibrary.add(copy);
        checkRep();
        return copy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
        if (isAvailable(copy)) {
            inLibrary.remove(copy);
            checkedOut.add(copy);
        }
        checkRep();
    }
    
    @Override
    public void checkin(BookCopy copy) {
        if (checkedOut.contains(copy)) {
            checkedOut.remove(copy);
            inLibrary.add(copy);
        }
        checkRep();
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        return inLibrary.contains(copy);
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        Set<BookCopy> allCopies = new HashSet<>();
        for (BookCopy copy : inLibrary)
            if (copy.getBook().equals(book)) allCopies.add(copy);
        for (BookCopy copy : checkedOut)
            if (copy.getBook().equals(book)) allCopies.add(copy);
        return allCopies;
    }
    
    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> availableCopies = new HashSet<>();
        for (BookCopy copy : inLibrary)
            if (copy.getBook().equals(book)) availableCopies.add(copy);
        return availableCopies;
    }

    @Override
    public List<Book> find(String query) {
        Set<Book> findSet = new HashSet<>();

        Set<BookCopy> union = new HashSet<>(inLibrary);
        union.addAll(checkedOut);

        for (BookCopy copy : union) {
            Book book = copy.getBook();
            if (book.getAuthors().contains(query) || book.getTitle().contains(query)) {
                findSet.add(book);
            }
        }

        List<Book> find = new ArrayList<>(findSet);
        find.sort((book1, book2) -> book2.getYear() - book1.getYear());

        checkRep();
        return find;
    }
    
    @Override
    public void lose(BookCopy copy) {
        inLibrary.remove(copy);
        checkedOut.remove(copy);
        checkRep();
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
