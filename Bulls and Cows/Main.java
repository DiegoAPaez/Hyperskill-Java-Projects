package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static int numLen;
    static int symbols;
    static char[] secretNumber;
    static char[] userGuess;
    static int bulls = 0;
    static int cows = 0;

    public static void main(String[] args) {
        setNumLenAndSymbols();
        if (numLen <= symbols) {
            generateUniqueCode();
            printSecretCodeInfo();
            playGame();
        }
    }

    public static void setNumLenAndSymbols() {
        System.out.println("Input the length of the secret code:");
        String userInput = scanner.next();
        try {
            numLen = Integer.parseInt(userInput);
            System.out.println("Input the number of possible symbols in the code:");
            symbols = scanner.nextInt();
            if (numLen > symbols) {
                System.out.printf("Error: it's not possible to generate a code with a length %d with %d unique symbols.\n", numLen, symbols);
                System.exit(0);
            } else if (symbols > 36) {
                System.out.println("Error: the maximum number of possible symbols is 36 (0-9, a-z).");
                System.exit(0);
            } else {
                secretNumber = new char[numLen];
                userGuess = new char[numLen];
            }
        } catch (Exception e) {
            System.out.printf("\"%s\" isn't a valid number.\n", userInput);
        }
    }

    public static void generateUniqueCode() {
        boolean[] usedSymbols = new boolean[36];
        for (int i = 0; i < numLen; i++) {
            int symbol;
            do {
                symbol = random.nextInt(symbols);
            } while (usedSymbols[symbol]);
            usedSymbols[symbol] = true;
            secretNumber[i] = symbolToChar(symbol);
        }
    }

    public static char symbolToChar(int symbol) {
        if (symbol < 10) {
            return (char) ('0' + symbol);
        } else {
            return (char) ('a' + symbol - 10);
        }
    }

    public static void printSecretCodeInfo() {
        StringBuilder range = new StringBuilder();
        for (int i = 0; i < symbols; i++) {
            range.append(symbolToChar(i));
        }
        System.out.printf("The secret is prepared: %s (%s).\n", "*".repeat(numLen), range);
    }

    public static void inputToArr(String string) {
        for (int i = 0; i < string.length(); i++) {
            userGuess[i] = string.charAt(i);
        }
    }

    public static void compareNumbers() {
        bulls = 0;
        cows = 0;
        boolean[] secretUsed = new boolean[numLen];
        boolean[] guessUsed = new boolean[numLen];

        for (int i = 0; i < numLen; i++) {
            if (userGuess[i] == secretNumber[i]) {
                bulls++;
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        for (int i = 0; i < numLen; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < numLen; j++) {
                    if (!secretUsed[j] && userGuess[i] == secretNumber[j]) {
                        cows++;
                        secretUsed[j] = true;
                        break;
                    }
                }
            }
        }
    }

    public static void printResults() {
        StringBuilder result = new StringBuilder("Grade: ");

        if (bulls > 0 || cows > 0) {
            if (bulls > 0) {
                result.append(bulls).append(bulls > 1 ? " bulls" : " bull");
            }
            if (cows > 0) {
                if (bulls > 0) result.append(" and ");
                result.append(cows).append(cows > 1 ? " cows" : " cow");
            }
        } else {
            result.append("None.");
        }
        System.out.println(result);
    }

    public static void playGame() {
        int turn = 1;

        while (bulls < numLen) {
            System.out.printf("Turn %d:\n", turn++);
            inputToArr(scanner.next());
            compareNumbers();
            printResults();
        }
        System.out.println("Congratulations! You guessed the secret code.");
    }
}