package battleship;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Battleship player1 = new Battleship();
        Battleship player2 = new Battleship();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Player 1, place your ships on the game field");
        player1.placeAllShips();
        promptEnterKey();

        System.out.println("Player 2, place your ships on the game field");
        player2.placeAllShips();
        promptEnterKey();

        while (true) {
            System.out.println("Player 1, it's your turn:");
            printBoards(player2, player1);
            String shot = scanner.next();
            player2.takeShot(shot);
            if (player2.areAllShipsSunk()) {
                System.out.println("Player 1 sank the last ship. Player 1 won. Congratulations!");
                break;
            }
            promptEnterKey();

            System.out.println("Player 2, it's your turn:");
            printBoards(player1, player2);
            shot = scanner.next();
            player1.takeShot(shot);
            if (player1.areAllShipsSunk()) {
                System.out.println("Player 2 sank the last ship. Player 2 won. Congratulations!");
                break;
            }
            promptEnterKey();
        }
    }

    private static void printBoards(Battleship opponent, Battleship player) {
        opponent.printFogOfWarBoard();
        System.out.println("---------------------");
        player.printBoard();
    }

    private static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (Exception e) {
        }
        clearScreen();
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}