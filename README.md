# wordle solver

an entropy-based wordle solver

the idea for using entropy was inspired by 3blue1brown's video on information theory and wordle. i struggled with winning wordle sometimes and i liked the idea of using entropy to measure how much information a guess gives you, so i wanted to try implementing that idea myself in java.

## how it works

the solver starts with a list of possible wordle answers and keeps narrowing that list down after every guess.

after entering a word, each letter gets one of three results:

🟩 means the letter is correct and in the correct spot
🟨 means the letter is in the word but in the wrong spot
🟥 means the letter is not in the word

for example:

```text
guess: RAISE
result: 🟥🟨🟩🟥🟥
```

the solver compares that result against every remaining possible answer and removes any word that could not have produced the same pattern.

after filtering the list, it uses entropy to choose the next guess.

## entropy

the main idea behind the solver is that the best guess is not always the word that is most likely to be correct. sometimes it is better to choose a word that gives you more information about what the answer could be.

for every possible guess, the solver compares it against every remaining possible answer and records the result pattern it would produce.

since each of the 5 letters can have 3 possible states, there can be up to:

```text
3^5 = 243
```

different result patterns.

the solver then counts how often each pattern happens and calculates the probability of getting that result.

the information gained from a result with probability `p` is:

```text
I = -log2(p)
```

the entropy of the entire guess is:

```text
H = Σ p × -log2(p)
```

a higher entropy means the possible answers are being split into smaller and more even groups, which usually means the guess will eliminate more words.

for example, if there are 100 possible answers left, a guess that splits them into:

```text
80, 10, 5, 5
```

is usually worse than a guess that splits them into:

```text
25, 25, 25, 25
```

because the second guess gives more useful information no matter which result comes back.

## selenium automation

i also added selenium so the solver can interact directly with wordle+ infinite instead of having to manually enter every guess and result.

the `WordlePlusBrowser.java` file opens the game in chrome, presses the on-screen keyboard to enter a guess, submits it, waits for the tile animation, and reads the result from the page.

the tile results are converted back into `G`, `Y`, and `B`, which can then be sent back into the solver to generate the next guess.

this creates a loop where the solver can choose a word, selenium enters it into the browser, the browser result is read, and the solver uses that result to choose the next word.

## what i learned

this project was mainly how i learned java outside of class.

it helped me get more comfortable with classes, objects, object-oriented programming, `ArrayList`, `HashMap`, file input, and debugging larger programs.

the entropy part also helped me understand information theory more clearly by actually implementing the idea from 3blue1brown's explanation in code.

adding selenium also gave me experience connecting a java program to a real website and automatically reading information from the page.

## used

java
selenium webdriver
chrome webdriver

## what's to come

i want to make the entropy calculations faster, add more testing and solver statistics, make the selenium setup easier, and possibly add a simple gui later.
