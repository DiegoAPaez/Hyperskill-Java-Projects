package chucknorris;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static String userInput;
    static String binaryString;
    static String result;

    public static void main(String[] args) {
        runCipher();
    }

    public static void runCipher() {
        boolean run = true;
        while (run) {
            System.out.println("Please input operation (encode/decode/exit):");
            String op = scanner.nextLine();
            switch (op) {
                case "exit" -> {
                    System.out.println("Bye!");
                    run = false;
                    continue;
                }
                case "encode" -> {
                    setInput("Input string:");
                    parseInput();
                    chuckNorrisEncode();
                }
                case "decode" -> {
                    setInput("Input encoded string:");
                    if (validInput(userInput)) {
                        chuckNorrisDecode();
                    } else {
                        System.out.println("Encoded string is not valid.");
                        continue;
                    }
                }
                default -> {
                    System.out.println("There is no '" + op + "' operation");
                    continue;
                }
            }
            printResult("encode".equals(op) ? "Encoded" : "Decoded");
        }
    }

    public static void printResult(String message){
        System.out.println(message + " string:");
        System.out.println(result);
        System.out.println();
    }

    public static void setInput(String message) {
        System.out.println(message);
        userInput = scanner.nextLine();
    }

    public static void parseInput() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < userInput.length(); i++) {
            String parsedChar = Integer.toBinaryString(userInput.charAt(i));
            builder.append(parsedChar.length() < 7 ? "0" + parsedChar : parsedChar);
        }
        binaryString = builder.toString();
    }

    public static void chuckNorrisEncode() {
        StringBuilder encoded = new StringBuilder();
        char currentChar = binaryString.charAt(0);
        int count = 0;
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == currentChar) {
                count++;
            } else {
                appendEncodedBlock(encoded, currentChar, count);
                currentChar = binaryString.charAt(i);
                count = 1;
            }
        }
        appendEncodedBlock(encoded, currentChar, count);
        result = encoded.toString().trim();
    }

    private static void appendEncodedBlock(StringBuilder encoded, char bit, int count) {
        if (bit == '1') {
            encoded.append("0 ");
        } else {
            encoded.append("00 ");
        }
        encoded.append("0".repeat(Math.max(0, count)));
        encoded.append(" ");
    }

    public static void chuckNorrisDecode() {
        StringBuilder decodedBinary = new StringBuilder();
        String[] blocks = userInput.split(" ");

        for (int i = 0; i < blocks.length; i += 2) {
            String prefix = blocks[i];
            String zeros = blocks[i + 1];
            char bit = prefix.equals("0") ? '1' : '0';
            decodedBinary.append(String.valueOf(bit).repeat(zeros.length()));
        }

        StringBuilder decodedMessage = new StringBuilder();
        for (int i = 0; i < decodedBinary.length(); i += 7) {
            String binaryChar = decodedBinary.substring(i, Math.min(i + 7, decodedBinary.length()));
            int charCode = Integer.parseInt(binaryChar, 2);
            decodedMessage.append((char) charCode);
        }
        result = decodedMessage.toString();
    }

    public static boolean validInput(String input) {
        if (!input.matches("[0 ]+")) {
            return false;
        }
        String[] blocks = input.split(" ");
        if (blocks.length % 2 != 0) {
            return false;
        }
        for (int i = 0; i < blocks.length; i += 2) {
            if (!blocks[i].equals("0") && !blocks[i].equals("00")) {
                return false;
            }
        }
        StringBuilder decodedBinary = new StringBuilder();
        for (int i = 0; i < blocks.length; i += 2) {
            String prefix = blocks[i];
            String zeros = blocks[i + 1];
            char bit = prefix.equals("0") ? '1' : '0';
            decodedBinary.append(String.valueOf(bit).repeat(zeros.length()));
        }
        return decodedBinary.length() % 7 == 0;
    }
}