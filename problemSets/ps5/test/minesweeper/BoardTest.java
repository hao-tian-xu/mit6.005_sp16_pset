/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import org.junit.Test;

/**
 * TODO: Description
 */
public class BoardTest {
    
    // TODO: Testing strategy
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO: Tests

    @Test
    public void testBoard() {
        Board board = new Board(10, 10);
        System.out.println(board);
        board.dig(0, 0);
        System.out.println(board);
        board.flag(3, 1);
        System.out.println(board);
    }
}
