package lastpencil;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static int pencils = 0;
    static Player[] players = new Player[2];

    public static void main(String[] args) {
        prepareGame();
        setPlayers();
        playGame();
    }

    public static void prepareGame() {
        System.out.println("How many pencils would you like to use: ");
        while (true) {
            String input = scanner.nextLine();
            try {
                pencils = Integer.parseInt(input);
                if (pencils <= 0) {
                    System.out.println("The number of pencils should be positive");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("The number of pencils should be numeric");
            }
        }
    }

    public static void setPlayers() {
        players[0] = new Player("John", false);
        players[1] = new Player("Jack", false);
    }

    public static void playGame() {
        System.out.println("Who will be the first (John, Jack):");
        int plays = selectPlayer();

        while (pencils > 0) {
            printPencils(pencils);
            if (plays == 0) {
                System.out.println(players[0].name + "'s turn!");
                pencils -= takePencils();
                plays = 1;
            } else {
                System.out.println(players[1].name + "'s turn:");
                checkWinningPosition(players[1]);
                int botTakes = botMove(players[1]);
                System.out.println(botTakes);
                pencils -= botTakes;
                plays = 0;
            }
        }
        System.out.println(players[plays].name + " won!");
    }

    public static int takePencils() {
        while (true) {
            try {
                int take = Integer.parseInt(scanner.next());
                if (take < 1 || take > 3) {
                    System.out.println("Possible values: '1', '2', '3'");
                } else if (take > pencils) {
                    System.out.println("Too many pencils were taken");
                } else {
                    return take;
                }
            } catch (NumberFormatException e) {
                System.out.println("Possible values: '1', '2', '3'");
            }
        }
    }

    public static void checkWinningPosition(Player player) {
        player.winningPosition = (pencils % 4 != 1);
    }

    public static int botMove(Player player) {
        Random random = new Random();
        if (!player.winningPosition) {
            return Math.min(pencils, random.nextInt(3) + 1);
        } else {
            return (pencils - 1) % 4;
        }
    }

    public static int selectPlayer() {
        while (true) {
            String starts = scanner.next();
            if ("John".equals(starts)) {
                players[0].startsFirst = true;
                return 0;
            } else if ("Jack".equals(starts)) {
                players[1].startsFirst = true;
                return 1;
            } else {
                System.out.println("Choose between 'John' and 'Jack'");
            }
        }
    }

    public static void printPencils(int pencils) {
        System.out.println("|".repeat(pencils));
    }
}
