/*
Leo Wang
06 / 11 / 26

Keeps track of which Wordle answers are still possible and uses
average information gain / entropy to calculate the best
possible guess
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Solver {

    // Stores all words that could still be the correct Wordle answer
    private ArrayList<String> answers = new ArrayList<>();

    // Loads all possible Wordle answers when a Solver object is created
    public Solver() {

        // File opening
        try {

            File file = new File("wordle-nyt-answers-alphabetical.txt");

            Scanner fileScanner = new Scanner(file);

            // Read every word from the file and add it to answers
            while (fileScanner.hasNextLine()) {

                String word = fileScanner.nextLine();

                answers.add(word);
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {

            System.out.println("Answer file not found.");
        }
    }


    // Removes all answers that are inconsistent with the guess and G/Y/B result entered
    public void update(String word, String result) {

        // Create a Guess object using the word the user played 
        // and the actual G/Y/B result returned by Wordle
        Guess guess = new Guess(word, result);

        // Go backwards so removing words does not skip indexes
        for (int i = answers.size() - 1; i >= 0; i--) {

            // Pretend this word is the actual Wordle answer
            String possibleAnswer = answers.get(i);

            // Check whether this possible answer would have produced 
            // the same G/Y/B result that the user actually received
            if (!guess.isValid(possibleAnswer)) {

                // If it would not produce the same result, it cannot be the real answer
                answers.remove(i);
            }
        }
    }


    // Finds the remaining answer with the highest entropy
    public String getBestGuess() {

        if (answers.size() == 0) {

            return "No possible answers";
        }


        String bestGuess = "";
        double maxEntropy = -1;


        // Test every remaining answer as a possible next guess
        for (int i = 0; i < answers.size(); i++) {

            String word = answers.get(i);

            // Calculate how much information this guess is expected to give us
            double entropy = calculateEntropy(word);
       
            // Find the max
            if (entropy > maxEntropy) {

                maxEntropy = entropy;
                bestGuess = word;
            }
        }

        return bestGuess;
    }


    // Returns how many possible answers are still remaining
    public int getNumberOfAnswers() {

        return answers.size();
    }


    // Calculates the expected information gain for one possible guess
    private double calculateEntropy(String word) {

        HashMap<String, Integer> patternCounts = findPatterns(word);

        double entropy = 0.0;

        for (int count : patternCounts.values()) {

            // solving for p(x) (probability)
            double probability = (double) count / answers.size();

            // The formula for bits of information: log2(1 / p(x)) 
            double information = Math.log(1.0 / probability) / Math.log(2);

            // Finding weighted average
            entropy += probability * information;
        }

        return entropy;
}

    // Finds every possible G/Y/B pattern that a guess could produce and counts how many times it occurs
    private HashMap<String, Integer> findPatterns(String word) {

        HashMap<String, Integer> patternCounts = new HashMap<String, Integer>();


        // Pretend every remaining answer is the real answer.
        for (int i = 0; i < answers.size(); i++) {

            String possibleAnswer = answers.get(i);

            String pattern = Guess.getResult(word, possibleAnswer);


            // Get how many times this pattern has appeared so far.
            int oldCount = patternCounts.getOrDefault(pattern, 0);

            // Increase it by one.
            patternCounts.put(pattern, oldCount + 1);
        }

        return patternCounts;
    }
}
