package com.example.puzzle.domain;

import java.util.*;

public class Puzzle {
    /** puzzle board */
    private final int[][] board;
    /** size (i.e. board width / height) and heuristics */
    private final int size;
    private final int heuristics;

    /** location of empty tile */
    private int emptyI;
    private int emptyJ;

    /** Puzzle Shift directions */
    public enum Shift {UP, DOWN, LEFT, RIGHT}

    /**
     * Random Puzzle of given size
     * @param boardWidth
     */
    public Puzzle(int boardWidth, boolean isSolution) {
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

        if (! isSolution) {
            // Randomize
            Random random = new Random();
            for (int i = 0; i < size; i += 1) {
                for (int j = 0; j < size; j += 1) {
                    try { swap(i, j, random.nextInt(size), random.nextInt(size));
                    } catch (PuzzleException e) { e.printStackTrace(); }
                }
            }
            heuristics = manhattanDistance(new Puzzle(size, true));
        } else { heuristics = 0; }
    }

    /**
     * Random puzzle of given size
     * @param boardWidth
     */
    public Puzzle(int boardWidth){ this(boardWidth, false); }

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
    public boolean isSolution() { return this.heuristics == 0; }

    /**
     * Shift this puzzle
     * @param direction direction to shift in
     * @throws PuzzleException if at the edge and can't shift
     */
    public void shift(Puzzle.Shift direction) throws PuzzleException {
        switch (direction) {
            case UP -> swap(emptyI, emptyJ, emptyI - 1, emptyJ);
            case DOWN -> swap(emptyI, emptyJ, emptyI + 1, emptyJ);
            case LEFT -> swap(emptyI, emptyJ, emptyI, emptyJ - 1);
            case RIGHT -> swap(emptyI, emptyJ, emptyI, emptyJ + 1);
        }
    }

    /**
     * Get a shifted Puzzle
     * @param direction direction to shift in
     * @return shifted copy of this puzzle
     * @throws PuzzleException if at the edge and can't shift
     */
    public Puzzle shifted(Puzzle.Shift direction) throws PuzzleException {
        Puzzle puzzle = new Puzzle(this);
        puzzle.shifted(direction);
        return puzzle;
    }

    /**
     * Get row where value is found in Puzzle
     * @param value
     * @return
     * @throws IllegalArgumentException if value is not found
     */
    public int xOf(int value) throws IllegalArgumentException {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == value) return i;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Get column where value is found in Puzzle
     * @param value
     * @return
     * @throws IllegalArgumentException if value is not found
     */
    public int yOf(int value) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == value) return j;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Get successors of this puzzle
     * @return List of copies of this, shifted once in all directions
     */
    public List<Puzzle> successors() {
        List<Puzzle> successorsList = new ArrayList<>();
        for (Shift direction : Shift.values()) {
            try { successorsList.add(shifted(direction));
            } catch (PuzzleException e) { e.printStackTrace(); }
        }
        return successorsList;
    }

    /**
     * Manhattan Distance between this and that
     * @param that
     * @return
     */
    public int manhattanDistance(Puzzle that) {
        int distance = 0;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                int current = board[i][j];
                distance += Math.abs(i - that.xOf(current)) +
                        Math.abs(j - that.yOf(current));
            }
        }
        return distance;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Puzzle puzzle = (Puzzle) o;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != puzzle.board[i][j]) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size, heuristics, emptyI, emptyJ);
        result = 31 * result + Arrays.hashCode(board);
        return result;
    }

    /**
     * Swap values at positions [i, j] and [withI, withJ] in puzzle
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
}
