# Wordle Solver

An entropy-based Wordle solver written in Java that determines strong guesses by measuring how much information each possible guess is expected to provide.

I built this project while learning Java and object-oriented programming. Rather than simply filtering words after each guess, the solver uses information theory to evaluate possible guesses and choose words that are expected to eliminate the largest number of possible answers.

## How It Works

After each Wordle guess, the user enters the result using:

* 🟩 — Correct letter in the correct position
* 🟨 — Correct letter in the wrong position
* 🟥 — Letter is not present in the word

For example:

```text
Guess: RAISE
Result: 🟥🟨🟩🟥🟥
```

The solver uses this information to eliminate answers that could not produce the same result pattern.

It then evaluates possible guesses using **entropy** to determine which guess is expected to provide the most information.

## Entropy-Based Guess Selection

For every possible guess, the solver compares that guess against every remaining possible Wordle answer.

Each comparison generates a five-character Wordle pattern such as:

```text
🟩🟩🟩🟩🟩
🟥🟥🟨🟩🟥
🟨🟥🟥🟩🟨
```

Because every letter can have three possible states — 🟩, 🟨, or 🟥 — there are up to:

```text
3^5 = 243
```

possible result patterns.

The solver counts how many remaining answers fall into each pattern and calculates the probability of each result.

For a pattern with probability `p`, the information gained is:

```text
I = -log2(p)
```

The expected information, or entropy, of a guess is:

```text
H = Σ p × -log2(p)
```

A guess with higher entropy tends to divide the remaining answers into smaller and more evenly distributed groups.

The solver selects the guess with the highest expected information gain.

## Example

Suppose there are 100 possible answers remaining.

One guess might divide them into groups like:

```text
80, 10, 5, 5
```

Another might divide them into:

```text
25, 25, 25, 25
```

The second guess provides more information because regardless of which pattern Wordle returns, the number of possible answers is reduced substantially.

This is why the solver does not simply choose a word that looks likely to be the answer. Sometimes the best guess is a word chosen specifically because it reveals more information.

## Running the Project

### Requirements

Install a Java Development Kit (JDK).

Check that Java is installed with:

```bash
java -version
```

### Compile

From the project directory:

```bash
javac Main.java Solver.java Guess.java
```

### Run

```bash
java Main
```

Follow the prompts in the terminal and enter the Wordle result after each guess.

## What I Learned

This project was originally a way for me to learn Java before using it in university coursework.

Before starting the project, most of my programming experience was in C. Building the solver helped me become more comfortable with:

* Java syntax
* Classes and objects
* Object-oriented programming
* `ArrayList` and `HashMap`
* File input
* Algorithm design
* Debugging larger programs

I also learned how information theory can be applied to a real problem. Instead of only asking whether a guess is likely to be correct, the solver evaluates how much information a guess can reveal about the answer.

## Future Improvements

Potential additions include:

* Automated Wordle gameplay using Selenium
* A graphical user interface
* Performance optimizations for entropy calculations
* Support for different Wordle dictionaries
* Solver statistics and benchmarking
* Caching previously calculated guess results

## Author

**Leo Wang**

UBC Engineering Physics
