package com.example.puzzle.domain;

import java.util.Random;

public class Puzzle {
    // board
    private final int[][] board;
    // size and heuristics for quick access
    private final int size;
    private final int heuristics;

    // location of empty tile, for quick access
    private int emptyI;
    private int emptyJ;

    private int heuristics() {
        // todo - manhattan or hamming distance validity function

        int number = 1;
        int result = 0;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (i == size-1 && j == size-1) number = 0;
                if (board[i][j] != number) result += 1;
                number += 1;
            }
        }
        return result;
    }

    /**
     * Swap values at positions [i, j] with [withI, withJ] in puzzle
     * @param i
     * @param j
     * @param withI
     * @param withJ
     * @throws PuzzleException If any indexes are out of bounds.
     */
    private void swap(int i, int j, int withI, int withJ) throws PuzzleException {
        if (i >= size || j >= size || withI >= size || withJ >= size ||
                i < 0 || j < 0 || withI < 0 || withJ < 0) {
            throw new PuzzleException(String.format(
                    "Can't swap spaces [%d, %d] with [%d, %d]",
                    i, j, withI, withJ));
        }
        int auxiliary = board[i][j];
        board[i][j] = board[withI][withJ];
        board[withI][withJ] = auxiliary;

        if (board[i][j] == 0) { emptyI = i; emptyJ = j; }
        if (board[withI][withJ] == 0) { emptyI = withI; emptyJ = withJ; }
    }

    /**
     * Random Puzzle of given size
     * @param boardWidth
     */
    public Puzzle(int boardWidth, boolean randomize) {
        // Initialize
        size = boardWidth;
        board = new int[size][size];
        int number = 1;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (i == size-1 && j == size-1) board[i][j] = 0;
                else board[i][j] = number;
                number += 1;
            }
        }
        emptyI = size-1;
        emptyJ = size-1;

        if (randomize) {
            // Randomize
            Random random = new Random();
            for (int i = 0; i < size; i += 1) {
                for (int j = 0; j < size; j += 1) {
                    try { swap(i, j, random.nextInt(size), random.nextInt(size));
                    } catch (PuzzleException e) { e.printStackTrace(); }
                }
            }
        }

        heuristics = heuristics();
    }

    /**
     * Correct puzzle of given size
     * @param boardWidth
     */
    public Puzzle(int boardWidth){
        this(boardWidth, false);
    }

    /**
     * Copy constructor
     * @param that
     */
    public Puzzle(Puzzle that) {
        this.board = that.board.clone();
        this.size = that.size;
        this.emptyI = that.emptyI;
        this.emptyJ = that.emptyJ;
        this.heuristics = that.heuristics;
    }

    public int getSize() { return size; }
    public int getHeuristics() { return heuristics; }

    public void shiftUp() throws PuzzleException {
        swap(emptyI, emptyJ, emptyI - 1, emptyJ);
    }

    public void shiftDown() throws PuzzleException {
        swap(emptyI, emptyJ, emptyI + 1, emptyJ);
    }

    public void shiftLeft() throws PuzzleException {
        swap(emptyI, emptyJ, emptyI, emptyJ - 1);
    }

    public void shiftRight() throws PuzzleException {
        swap(emptyI, emptyJ, emptyI, emptyJ + 1);
    }

    public Puzzle shiftedUp() throws PuzzleException {
        Puzzle puzzle = new Puzzle(this);
        puzzle.shiftUp();
        return puzzle;
    }

    public Puzzle shiftedDown() throws PuzzleException {
        Puzzle puzzle = new Puzzle(this);
        puzzle.shiftDown();
        return puzzle;
    }

    public Puzzle shiftedLeft() throws PuzzleException {
        Puzzle puzzle = new Puzzle(this);
        puzzle.shiftLeft();
        return puzzle;
    }

    public Puzzle shiftedRight() throws PuzzleException {
        Puzzle puzzle = new Puzzle(this);
        puzzle.shiftRight();
        return puzzle;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                builder.append(board[i][j]).append("\t");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
