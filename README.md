# Smart Expense Splitter

A Java application for session-based expense splitting.

Team Members 

Sobigasri

Subiksha

Tamil Arase 

## Features

- Session mode for outings
- Record expenses during the session
- Automatic settlement at the end of the session

## How to Run

1. Compile: `javac src/main/java/com/example/*.java`
2. Run: `java -cp src/main/java com.example.ExpenseSplitter`

## Example Output

For friends A, B, C with expenses:
- A pays ₹50
- B pays ₹100
- C pays ₹300

Total: ₹450, Each: ₹150

Balances:
- A: -100
- B: -50
- C: +150

Settlements:
- A pays ₹100 to C
- B pays ₹50 to C