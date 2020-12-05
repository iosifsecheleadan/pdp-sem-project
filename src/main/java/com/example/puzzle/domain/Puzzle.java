package com.example.puzzle.domain;

public class Puzzle {
    // board
    private int[][] board;
    // size for quick access
    private int size;

    // location of empty tile, for quick access
    private int emptyI;
    private int emptyJ;

    public Puzzle(int size) {
        // todo - new Random Puzzle of given size
    }

    public Puzzle(int[][] board) {
        // todo - new Puzzle with given board (copy constructor !!!)
    }

    public int getSize() { return size; }

    public int heuristics() {
        // todo - manhattan or hamming distance validity function
        return 0;
    }

    @Override
    public String toString() {
        // todo - pretty printing
        return "Puzzle{}";
    }

    public void shiftUp() throws PuzzleException {
        // todo
    }

    public void shiftDown() throws PuzzleException {
        // todo
    }

    public void shiftLeft() throws PuzzleException {
        // todo
    }

    public void shiftRight() throws PuzzleException {
        // todo
    }

    public Puzzle shiftedUp() throws PuzzleException {
        Puzzle puzzle = new Puzzle(this.board);
        puzzle.shiftUp();
        return puzzle;
    }

    public Puzzle shiftedDown() throws PuzzleException {
        Puzzle puzzle = new Puzzle(this.board);
        puzzle.shiftDown();
        return puzzle;
    }

    public Puzzle shiftedLeft() throws PuzzleException {
        Puzzle puzzle = new Puzzle(this.board);
        puzzle.shiftLeft();
        return puzzle;
    }

    public Puzzle shiftedRight() throws PuzzleException {
        Puzzle puzzle = new Puzzle(this.board);
        puzzle.shiftRight();
        return puzzle;
    }
}
