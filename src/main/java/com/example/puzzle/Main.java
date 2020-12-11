package com.example.puzzle;

import com.example.puzzle.domain.Puzzle;
import com.example.puzzle.domain.PuzzleException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
  public static void main(String[] args) {
    play(4);
    System.out.println("Bye!");
  }

  private static void play(int size) {
//    Puzzle puzzle = new Puzzle(size, true);
    //        System.out.println(
    //                "Try to get to the result using commands \"up\" | \"down\" | \"left\" |
    // \"right\" " +
    //                "The first letter of either word will suffice (u, d, l, r)" +
    //                "Type \"exit\" to quit" +
    //                "Here's what that should look like:\n" +
    //                new Puzzle(size, true).toString());
    //        Scanner input = new Scanner(System.in);
    //
    //        while (! puzzle.isSolution()) {
    //            System.out.printf("Your puzzle\n%s\n>", puzzle.toString());
    //            String line = input.nextLine();
    //            char command = line.charAt(0);
    //            try {
    //                switch (command) {
    //                    case 'u' -> puzzle.shift(Puzzle.Shift.UP);
    //                    case 'd' -> puzzle.shift(Puzzle.Shift.DOWN);
    //                    case 'l' -> puzzle.shift(Puzzle.Shift.LEFT);
    //                    case 'r' -> puzzle.shift(Puzzle.Shift.RIGHT);
    //                    case 'e' -> { return; }
    //                    default -> System.out.printf("Wrong input: \"%s\" \n", line);
    //                }
    //            } catch (PuzzleException e) { System.out.printf("Can't shift %s \n", line); } }
    //        System.out.printf("You did it! Good Job! \n%s", puzzle.toString());
//    int board[][] = new int[][] {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
    Puzzle puzzle = new Puzzle(4);
    puzzle.setParent(null);
    Solver solver = new Solver(4);
//    System.out.println("Starting from:");
//    System.out.println(puzzle.toString());
    try {
      Puzzle solution = solver.findSolution(puzzle);
      System.out.println(solution.toString());
    } catch (PuzzleException | ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
