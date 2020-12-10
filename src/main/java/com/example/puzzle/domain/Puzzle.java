package com.example.puzzle.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Puzzle {

  public static final Puzzle SOLUTION_4X4 =
      new Puzzle(new int[][] {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}});
  // board
  private int[][] board;
  // size for quick access
  private int size;
  private int heuristics;


  public Puzzle(int size) {
    this.size = size;
    List<Integer> pool = new ArrayList<>();
    for (int i = 0; i < size * size; i++) {
      pool.add(i);
    }
    Collections.shuffle(pool);
    board = new int[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        board[i][j] = pool.get(0);
        pool.remove(0);
      }
    }
    this.heuristics = getManhattanDistance(Puzzle.SOLUTION_4X4);
  }

  public Puzzle(int[][] board) {
    this.size = board.length;
    this.board = new int[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        this.board[i][j] = board[i][j];
      }
    }
    this.heuristics = getManhattanDistance(Puzzle.SOLUTION_4X4);
  }

  public int getSize() {
    return size;
  }

  public int heuristics() {
    return heuristics;
  }

  public int getManhattanDistance(Puzzle goal) {
    int distance = 0;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        int currentValue = board[i][j];
        distance += Math.abs(i - goal.xOf(currentValue)) + Math.abs(j - goal.yOf(currentValue));
      }
    }
    return distance;
  }

  public int xOf(int value) {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (board[i][j] == value) return i;
      }
    }
    throw new IllegalArgumentException();
  }

  public int yOf(int value) {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (board[i][j] == value) return j;
      }
    }
    throw new IllegalArgumentException();
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

  private Puzzle shiftedUp() throws PuzzleException {
    Puzzle puzzle = new Puzzle(this.board);
    puzzle.shiftUp();
    return puzzle;
  }

  private Puzzle shiftedDown() throws PuzzleException {
    Puzzle puzzle = new Puzzle(this.board);
    puzzle.shiftDown();
    return puzzle;
  }

  private Puzzle shiftedLeft() throws PuzzleException {
    Puzzle puzzle = new Puzzle(this.board);
    puzzle.shiftLeft();
    return puzzle;
  }

  private Puzzle shiftedRight() throws PuzzleException {
    Puzzle puzzle = new Puzzle(this.board);
    puzzle.shiftRight();
    return puzzle;
  }

  public List<Puzzle> successors() {
    List<Puzzle> successorsList = new ArrayList<>();
    Puzzle temp;
    try {
      temp = shiftedUp();
      successorsList.add(temp);
    } catch (PuzzleException ignored) {
    }
    try {
      temp = shiftedDown();
      successorsList.add(temp);
    } catch (PuzzleException e) {
    }
    try {
      temp = shiftedLeft();
      successorsList.add(temp);
    } catch (PuzzleException e) {
    }
    try {
      temp = shiftedRight();
      successorsList.add(temp);
    } catch (PuzzleException e) {
    }
    return successorsList;
  }

  public boolean isSolution() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (board[i][j] != Puzzle.SOLUTION_4X4.board[i][j]) return false;
      }
    }
    return true;
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
}
