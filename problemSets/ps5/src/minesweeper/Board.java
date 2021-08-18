/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.*;

/**
 * TODO: Specification
 */
public class Board {

    // rep
    private final int sizeX;
    private final int sizeY;
    private final char[][] board;
    private final Set<List<Integer>> bombSet;
        // static constants
        private static final double BOMB_PROBABILITY = 0.125;
        private static final char UNTOUCHED = '-';
        private static final char DUG = ' ';
        private static final char FLAGGED = 'F';

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
    public Board(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine();
        if (!line.matches("[0-9]+ [0-9]+")) throw new RuntimeException();
        String[] size = line.split(" ");
        this.sizeX = Integer.parseInt(size[0]);
        this.sizeY = Integer.parseInt(size[1]);

        this.board = new char[sizeY][sizeX];
        this.bombSet = new HashSet<>();
        for (int y=0; y<sizeY; y++) {
            line = br.readLine();
            if (!line.matches("((0|1) )*(0|1)")) throw new RuntimeException();
            String[] squares = line.split(" ");
            if (squares.length != sizeX) throw new RuntimeException();
            for (int x=0; x<sizeX; x++) {
                if (Objects.equals(squares[x], "1")) bombSet.add(Arrays.asList(x, y));
                board[y][x] = UNTOUCHED;
            }
        }
    }

    /**
     * construct a Board from size
     * @param sizeX board width
     * @param sizeY board height
     * TODO
     */
    public Board(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.board = new char[sizeY][sizeX];
        this.bombSet = new HashSet<>();
        Random random = new Random(0);
        for (int y=0; y<sizeY; y++) {
            for (int x=0; x<sizeX; x++) {
                if (random.nextDouble() <= BOMB_PROBABILITY) bombSet.add(Arrays.asList(x, y));
                board[y][x] = UNTOUCHED;
            }
        }
    }

    /**
     * Dig a square to check if it's not a bomb and clear it;
     * If square contains a bomb, change it so that it contains no bomb, and update the board and bombSet;
     * Clear a square if dug with no bomb, If the square has no neighbor squares with bombs,
     * then for each untouched neighbor squares, change said square to dug and repeat this step;
     * @param x x coordinate
     * @param y y coordinate
     * @return false if dug a bomb, else true
     */
    public synchronized boolean dig(int x, int y) {
        // If either x or y is less than 0, or either x or y is equal to or greater than the board size,
        // or square x,y is not in the untouched state
        if (notOnBoard(x, y) || board[y][x] != UNTOUCHED) return true;
        // If square x,y is a bomb
        if (bombSet.contains(Arrays.asList(x, y))) {
            bombSet.remove(Arrays.asList(x, y));
            int numberOfBombNeighbors = numberOfBombNeighbors(x, y);
            if (numberOfBombNeighbors != 0)
                board[y][x] = Character.forDigit(numberOfBombNeighbors, 10);
            else board[y][x] = DUG;
            for (List<Integer> neighbor : neighbors(x, y))
                updateDugBombNeighbor(neighbor.get(0), neighbor.get(1));
            return false;
        }
        // untouched and not a bomb
        else {
            clear(x, y);
            return true;
        }
    }

    private void updateDugBombNeighbor(int x, int y) {
        if (Character.isDigit(board[y][x])) board[y][x] -= 1;
        if (board[y][x] == '0') {
            board[y][x] = DUG;
            for (List<Integer> neighbor : neighbors(x, y)) clear(neighbor.get(0), neighbor.get(1));
        }
    }

    private void clear(int x, int y) {
        // not on board or dug
        if (notOnBoard(x, y) || board[y][x] != UNTOUCHED) return;
        // number of bomb neighbors
        int numberOfBombNeighbors = numberOfBombNeighbors(x, y);
        // has bomb neighbors
        if (numberOfBombNeighbors != 0) {
            board[y][x] = Character.forDigit(numberOfBombNeighbors, 10);
            return;
        }
        // no bomb neighbor
        board[y][x] = DUG;
        for (List<Integer> neighbor : neighbors(x, y)) clear(neighbor.get(0), neighbor.get(1));
    }

    private boolean notOnBoard(int x, int y) {
        return x < 0 || y < 0 || x >= sizeX || y >= sizeY;
    }

    private int numberOfBombNeighbors(int x, int y) {
        int numberOfBombNeighbors = 0;
        for (List<Integer> neighbor : neighbors(x, y))
            if (bombSet.contains(neighbor)) numberOfBombNeighbors++;
        return numberOfBombNeighbors;
    }

    private Set<List<Integer>> neighbors(int x, int y) {
        Set<List<Integer>> neighbors = new HashSet<>();
        for (int i=-1; i<=1; i++)
            for (int j=-1; j<=1; j++) {
                if (i == 0 && j == 0) continue;
                neighbors.add(Arrays.asList(x + i, y + j));
            }
        return neighbors;
    }

    /**
     * flag a square
     * @param x x coordinate
     * @param y y coordinate
     */
    public synchronized void flag(int x, int y) {
        // not on board
        if (notOnBoard(x, y) || board[y][x] != UNTOUCHED) return;
        board[y][x] = FLAGGED;
    }

    /**
     * deflag a square
     * @param x x coordinate
     * @param y y coordinate
     */
    public synchronized void deflag(int x, int y) {
        // not on board
        if (notOnBoard(x, y) || board[y][x] != FLAGGED) return;
        board[y][x] = UNTOUCHED;
    }

    /**
     * @return board size array {width, height}
     */
    public synchronized int[] size() {
        return new int[]{sizeX, sizeY};
    }

    @Override
    public synchronized String toString() {
        StringBuilder toString = new StringBuilder();
        for (char[] line : board) {
            for (char square : line) {
                toString.append(square).append(" ");
            }
            toString.append("\n");
        }
        String out = toString.toString().replaceAll(" \n", "\n");
        return out.substring(0, out.length()-1);
    }
}
