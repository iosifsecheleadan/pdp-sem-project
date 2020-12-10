package com.example.puzzle;

import com.example.puzzle.domain.Puzzle;
import com.example.puzzle.domain.PuzzleException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Solver {
  private final List<Puzzle> solution = new ArrayList<>();
  private final int FOUND = -1;
  private final ExecutorService executorService;
  private final int INFINITY = Integer.MAX_VALUE;

  public Solver(int numberOfThreads) {
    this.executorService = Executors.newFixedThreadPool(numberOfThreads);
  }

  public List<Puzzle> findSolution(Puzzle root) throws PuzzleException, ExecutionException, InterruptedException {
    int bound = root.getHeuristics();
    solution.add(root);
    boolean found = false;
    AbstractMap.SimpleEntry<Integer, Puzzle> searchAnswer;
    while (!found) {
      searchAnswer = search(0, bound);
      if (searchAnswer.getKey() == FOUND) {
        found = true;
      }
      if (searchAnswer.getKey() == -2) {
        throw new PuzzleException();
      }
      bound = searchAnswer.getKey();
    }
    return solution;
  }

  private AbstractMap.SimpleEntry<Integer, Puzzle> search(int currentCost, int bound) throws ExecutionException, InterruptedException {
    Puzzle node = solution.get(solution.size() - 1);
    int totalCost = currentCost + node.getHeuristics();
    if (totalCost > bound) {
      return totalCost;
    }
    if (node.isSolution()) {
      return FOUND;
    }
    int min = INFINITY;

    List<Puzzle> successors = sanitizeSuccessors(node);
    List<Future<Integer>> tasks = new ArrayList<>();
    for (Puzzle puzzle : successors) {
      Future<Integer> future = executorService.submit(() -> search(currentCost + 1, bound));
      tasks.add(future);
    }
    for (Future<Integer> future : tasks) {
      int result = future.get();
      if (result == )
    }
  }

  private List<Puzzle> sanitizeSuccessors(Puzzle node) {
    List<Puzzle> successors = node.successors();
    if (solution.size() > 1) {
      successors =
          successors.stream()
              .filter(s -> !s.equals(solution.get(solution.size() - 2)))
              .collect(Collectors.toList());
    }
    return successors;
  }
}
