package com.example.puzzle;

import com.example.puzzle.domain.Puzzle;
import com.example.puzzle.domain.PuzzleException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        play(3);
        System.out.println("Bye!");
    }

    private static void play(int size) {
        Puzzle puzzle = new Puzzle(size, true);
        System.out.println("""
                Try to get to the result using commands "up" | "down" | "left" | "right"
                The first letter of either word will suffice (u, d, l, r)
                Type "exit" to quit
                Here's what that should look like:\s
                """ + new Puzzle(size).toString());
        Scanner input = new Scanner(System.in);

        while (puzzle.getHeuristics() != 0) {
            System.out.printf("Your puzzle\n%s\n>", puzzle.toString());
            String line = input.nextLine();
            char command = line.charAt(0);
            try {
                switch (command) {
                    case 'u' -> puzzle.shiftUp();
                    case 'd' -> puzzle.shiftDown();
                    case 'l' -> puzzle.shiftLeft();
                    case 'r' -> puzzle.shiftRight();
                    case 'e' -> { return; }
                    default -> System.out.printf("Wrong input: \"%s\" \n", line);
                }
            } catch (PuzzleException e) { System.out.printf("Can't shift %s \n", line); } }
        System.out.printf("You did it! Good Job! \n %s", puzzle.toString());
    }
}
