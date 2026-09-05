

/*
Leo Wang
06 / 11 / 26

Checks whether a possible answer matches the g/y/b result entered

*/
public class Guess {

    private String word;
    private String result;

    public Guess(String word, String result) {

        this.word = word;
        this.result = result;
    }

    
    public boolean isValid(String possibleAnswer) {

        // Pretend possibleAnswer is the real Wordle answer and calculate what 
        // result our guess would have produced
        String testResult = getResult(word, possibleAnswer);

        // If the simulated result matches the actual result 
        // that Wordle gave us, this word could be the answer
        if (testResult.equals(result)) {
            return true;
        }

        // If the results do not match, then this word cannot be the real answer
        return false;
    }


    // You test every single possible word as the right answer, and you eliminate it if it doesn't
    // create the same result as the given result from wordle.
    // Because any word that is truly the answer must produce that exact same result when compared with your guess. 
    // If a word produces a different result, then it cannot be the true answer, 
    // because Wordle would have shown you something different
    public static String getResult(String guess, String answer) {

        char[] resultLetters = {'B', 'B', 'B', 'B', 'B'};

        // Use this boolean to make sure you do not mark a repeat letter as yellow
        boolean[] used = new boolean[5];

        // Check green letters first
        for (int i = 0; i < 5; i++) {

            if (guess.charAt(i) == answer.charAt(i)) {

                resultLetters[i] = 'G';
                used[i] = true;
            }
        }

        // Check yellow letters
        for (int i = 0; i < 5; i++) {   

            if (resultLetters[i] != 'G') {

                for (int j = 0; j < 5; j++) {

                    if (guess.charAt(i) == answer.charAt(j) && used[j] == false) {

                        resultLetters[i] = 'Y';
                        used[j] = true;

                        break;
                    }
                }
            }
        }

        return new String(resultLetters);
    }
}
