package com.example.puzzle;

import com.example.puzzle.domain.Puzzle;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
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
  private final Stack<Puzzle> solution;

  public Solver(int numberOfThreads) {
    this.executorService = Executors.newFixedThreadPool(numberOfThreads);
    solution = new Stack<>();
  }

  public Stack<Puzzle> findSolution(Puzzle root) throws ExecutionException, InterruptedException {
    int minBound = root.getHeuristics();
    solution.add(root);
    AbstractMap.SimpleEntry<Integer, Stack<Puzzle>> answer = null;
    while (true) {
      answer = searchParallel(solution, 0, minBound, 20);
      if (answer.getKey() == -1) {
        executorService.shutdown();
        return answer.getValue();
      }
      System.out.println(minBound);
      minBound = answer.getKey();
    }
  }

  public AbstractMap.SimpleEntry<Integer, Stack<Puzzle>> searchParallel(
      Stack<Puzzle> stack, int numSteps, int bound, int nrThreads)
      throws ExecutionException, InterruptedException {
    if (nrThreads <= 1) {
      return search(stack, numSteps, bound);
    }
    System.out.println(bound);
    Puzzle current = stack.peek();
    int estimation = numSteps + current.getHeuristics();
    if (estimation > bound) {
      return new AbstractMap.SimpleEntry<>(estimation, stack);
    }
    if (estimation > 80) {
      return new AbstractMap.SimpleEntry<>(estimation, stack);
    }
    if (current.getHeuristics() == 0) {
      return new AbstractMap.SimpleEntry<>(FOUND, stack);
    }
    int min = INFINITY;
    List<Puzzle> moves =
        current.successors().stream()
            .filter(m -> !stack.contains(m))
            .sorted(Comparator.comparingInt(Puzzle::getHeuristics))
            .collect(Collectors.toList());
    List<Future<AbstractMap.SimpleEntry<Integer, Stack<Puzzle>>>> futures = new ArrayList<>();
    for (Puzzle next : moves) {
      Stack<Puzzle> copy = (Stack<Puzzle>) stack.clone();
      copy.push(next);
      Future<AbstractMap.SimpleEntry<Integer, Stack<Puzzle>>> f =
          executorService.submit(
              () -> searchParallel(copy, numSteps + 1, bound, nrThreads / moves.size()));
      futures.add(f);
    }
    for (Future<AbstractMap.SimpleEntry<Integer, Stack<Puzzle>>> f : futures) {
      int t = f.get().getKey();
      if (t == -1) {
        return new AbstractMap.SimpleEntry<>(FOUND, f.get().getValue());
      }
      if (t < min) {
        min = t;
      }
    }
    return new AbstractMap.SimpleEntry<>(min, stack);
  }

  public AbstractMap.SimpleEntry<Integer, Stack<Puzzle>> search(
      Stack<Puzzle> stack, int numSteps, int bound) {
    Puzzle current = stack.peek();
    int estimation = numSteps + current.getHeuristics();
    if (estimation > bound) {
      return new AbstractMap.SimpleEntry<>(estimation, stack);
    }
    if (estimation > 80) {
      return new AbstractMap.SimpleEntry<>(estimation, stack);
    }
    if (current.getHeuristics() == 0) {
      return new AbstractMap.SimpleEntry<>(FOUND, stack);
    }
    int min = INFINITY;
    for (Puzzle next : current.successors()) {
      if (!solution.contains(next)) {
        stack.push(next);
        int t = search(stack, numSteps + 1, bound).getKey();
        if (t == -1) {
          return new AbstractMap.SimpleEntry<>(FOUND, stack);
        }
        if (t < min) {
          min = t;
        }
        stack.pop();
      }
    }
    return new AbstractMap.SimpleEntry<>(min, stack);
  }
}
