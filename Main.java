/*
Leo Wang
06 / 11 / 26

Interacts with user and controls game loop
*/

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Solver keeps track of the remaining possible Wordle answers
        Solver solver = new Solver();

        boolean playing = true;

        // Display instructions for the user
        System.out.println("WORDLE SOLVER");
        System.out.println();
        System.out.println("G = Green");
        System.out.println("Y = Yellow");
        System.out.println("B = Black");
        System.out.println();


        // Keep asking for guesses and results until the Wordle is solved
        while (playing) {

            System.out.print("Enter your guess: ");
            String word = scanner.nextLine().toLowerCase();

            // Make sure the guess contains exactly 5 letters
            while (word.length() != 5) {

                System.out.println("The guess must be 5 letters.");
                System.out.print("Enter your guess: ");

                word = scanner.nextLine().toLowerCase();
            }

            System.out.print("Enter the result: ");
            String result = scanner.nextLine().toUpperCase();

            // Make sure the feedback contains exactly 5 characters
            while (result.length() != 5) {

                System.out.println("The result must have 5 letters.");
                System.out.print("Enter the result: ");

                result = scanner.nextLine().toUpperCase();
            }


            // Remove all answers that could not have produced the result entered by the user
            solver.update(word, result);

            System.out.println();

            // Show how many possible answers are still left after filtering the answer list
            System.out.println("Possible answers remaining: " + solver.getNumberOfAnswers());

            // Ask the Solver to choose the best next guess using average information gain (entropy)
            String nextGuess = solver.getBestGuess();

            System.out.println("Recommended next guess: " + nextGuess);


            System.out.println();

            // If every letter is green, the correct word was found
            if (result.equals("GGGGG")) {
                
                System.out.println("Wordle solved!");
                // Stop playing
                playing = false;
            }
        }

        scanner.close();
    }
}