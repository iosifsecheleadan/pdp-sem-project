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
  private final Stack<Puzzle> solution = new Stack<>();
  private final int FOUND = -1;
  private final ExecutorService executorService;
  private final int INFINITY = Integer.MAX_VALUE;

  public Solver(int numberOfThreads) {
    this.executorService = Executors.newFixedThreadPool(numberOfThreads);
  }

  public List<Puzzle> findSolution(Puzzle root) throws PuzzleException, ExecutionException, InterruptedException {
    int bound = root.getHeuristics();
    solution.push(root);
    boolean found = false;
    AbstractMap.SimpleEntry<Integer, Puzzle> searchAnswer;
    while (!found) {
      searchAnswer = search(root, 0, bound);
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

  private AbstractMap.SimpleEntry<Integer, Puzzle> search(Puzzle current, int currentCost, int bound) throws ExecutionException, InterruptedException {
    Puzzle node = solution.get(solution.size() - 1);
    int totalCost = currentCost + node.getHeuristics();
    if (totalCost > bound) {
      return new AbstractMap.SimpleEntry<>(totalCost, current);
    }
    if (node.isSolution()) {
      return new AbstractMap.SimpleEntry<>(FOUND, current);
    }
    int min = INFINITY;

    List<Puzzle> successors = successorsSanitized(node);
    List<Future<AbstractMap.SimpleEntry<Integer, Puzzle>>> tasks = new ArrayList<>();
    for (Puzzle next : successors) {
      Future<AbstractMap.SimpleEntry<Integer, Puzzle>> future = executorService.submit(() -> search(next, currentCost + 1, bound));
      tasks.add(future);
    }
    for (Future<AbstractMap.SimpleEntry<Integer, Puzzle>> future : tasks) {
      AbstractMap.SimpleEntry<Integer, Puzzle> result = future.get();
      if (solution.search(result.getValue()) != -1) {
        solution.push(result.getValue());
        if (result.getKey() == FOUND) {
          return new AbstractMap.SimpleEntry<>(totalCost, current);
        }
        if (result.getKey() < min) {
          min = result.getKey();
        }
        solution.pop();
      }

    }
    return new AbstractMap.SimpleEntry<>(min, current);
  }

  private List<Puzzle> successorsSanitized(Puzzle node) {
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
