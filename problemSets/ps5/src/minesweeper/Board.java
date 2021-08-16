/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.*;

/**
 * TODO: Specification
 */
public class Board {

    // rep
    final private int sizeX;
    final private int sizeY;
    private char[][] board;
    final private Set<List<Integer>> bombSet;
        // constants
        final private double BOMB_PROBABILITY = 0.25;
        final private char UNREVEALED = '-';

    // TODO
    // abstraction function
    //      AF()
    // rep invariant
    // rep exposure
    // thread safety
    
    // TODO: Specify, test, and implement in problem 2

    /**
     * construct a Board from file
     * TODO
     */
    public Board(File file) {
        // TODO
        throw new NotImplementedException();
    }

    /**
     * construct a Board from size
     * TODO
     */
    public Board(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.board = new char[sizeX][sizeY];
        this.bombSet = new HashSet<>();
        Random random = new Random(0);
        for (int x=0; x<sizeX; x++) {
            for (int y=0; y<sizeY; y++) {
                if (random.nextDouble() <= BOMB_PROBABILITY) bombSet.add(Arrays.asList(x ,y));
                board[x][y] = UNREVEALED;
            }
        }
    }

    /**
     * dig a square
     * TODO
     */
    public boolean dig(int x, int y) {
        // TODO
        throw new NotImplementedException();
    }

    /**
     * clear a square if dug with no bomb
     * TODO
     */
    public void clear(int x, int y) {
        // TODO
        throw new NotImplementedException();
    }

    /**
     * flag a square
     * TODO
     */
    public void flag(int x, int y) {
        // TODO
        throw new NotImplementedException();
    }

    /**
     * deflag a square
     * TODO
     */
    public void deflag(int x, int y) {
        // TODO
        throw new NotImplementedException();
    }

    /**
     * return board size
     * TODO
     */
    public List<Integer> size() {
        // TODO
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        // TODO
        throw new NotImplementedException();
    }




}
