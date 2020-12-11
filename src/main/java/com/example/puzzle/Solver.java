package com.example.puzzle;

import com.example.puzzle.domain.Puzzle;
import com.example.puzzle.domain.PuzzleException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Solver {
  private final int FOUND = -1;
  private final ExecutorService executorService;
  private final int INFINITY = Integer.MAX_VALUE;

  public Solver(int numberOfThreads) {
    this.executorService = Executors.newFixedThreadPool(numberOfThreads);
  }

  public Puzzle findSolution(Puzzle root) throws PuzzleException, ExecutionException, InterruptedException {
    int bound = root.getHeuristics();
    boolean found = false;
    AbstractMap.SimpleEntry<Integer, Puzzle> searchAnswer = null;
    while (!found) {
      searchAnswer = search(root, 0, bound);
      if (searchAnswer.getKey() == FOUND) {
        found = true;
      }
      if (searchAnswer.getKey() == INFINITY) {
        throw new PuzzleException();
      }
      bound = searchAnswer.getKey();
    }
    executorService.shutdown();
    return searchAnswer.getValue();
  }

  private AbstractMap.SimpleEntry<Integer, Puzzle> search(Puzzle current, int currentCost, int bound) throws ExecutionException, InterruptedException {
    int totalCost = currentCost + current.getHeuristics();
    if (totalCost > bound) {
      return new AbstractMap.SimpleEntry<>(totalCost, current);
    }
    if (current.isSolution()) {
      return new AbstractMap.SimpleEntry<>(FOUND, current);
    }
    int min = INFINITY;

    List<Puzzle> successors = successorsSanitized(current);
    List<Future<AbstractMap.SimpleEntry<Integer, Puzzle>>> tasks = new ArrayList<>();
    for(int i = 0; i < successors.size(); i++) {
      Puzzle temp = successors.get(i);
      Future<AbstractMap.SimpleEntry<Integer, Puzzle>> future = executorService.submit(() -> search(temp, currentCost + 1, bound));
      tasks.add(future);
    }

    for (int i = 0; i < tasks.size(); i++) {
      AbstractMap.SimpleEntry<Integer, Puzzle> result = tasks.get(i).get();
      if (result.getKey() == FOUND) {
        return new AbstractMap.SimpleEntry<>(FOUND, result.getValue());
      }
      if (result.getKey() < min) {
        min = result.getKey();
      }
    }
    return new AbstractMap.SimpleEntry<>(min, current);
  }

  private List<Puzzle> successorsSanitized(Puzzle node) {
    List<Puzzle> successors = node.successors();
    successors.forEach(s -> s.setParent(node));
//    if (solution.size() > 1) {
//      successors =
//          successors.stream()
//              .filter(s -> !s.equals(solution.get(solution.size() - 2)))
//              .collect(Collectors.toList());
//    }

    return successors;
  }
}
