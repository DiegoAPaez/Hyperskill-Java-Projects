package cinema;

import java.util.Scanner;

public class Cinema {
    static char[][] room;
    static Scanner scanner = new Scanner(System.in);
    static int rows;
    static int seats;
    static int totalSeats;
    static int maxIncome;

    public static void main(String[] args) {
        setRoom();
        printRoom();
        buyTicket();
    }

    public static void setRoom(){
        System.out.println("Enter the number of rows:");
        rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        seats = scanner.nextInt();

        room = new char[rows][seats];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                room[i][j] = 'S';
            }
        }
        totalSeats = rows * seats;
        setMaxIncome();
    }

    public static void buyTicket() {
        System.out.println("\nEnter a row number:");
        int row = scanner.nextInt();
        System.out.println("Enter a seat number in that row:");
        int seat = scanner.nextInt();

        if (row > rows || seat > seats) {
            System.out.println("\nWrong input!");
            buyTicket();
        } else if (room[row - 1][seat - 1] == 'B') {
            System.out.println("\nThat ticket has already been purchased!");
            buyTicket();
        } else {
            getTicketPrice(row);
            room[row - 1][seat - 1] = 'B';
            printRoom();
        }
    }

    public static void printRoom() {
        System.out.println("\nCinema:");
        System.out.print("  ");
        for (int i = 1; i <= seats; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < seats; j++) {
                System.out.print(room[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void getTicketPrice(int row) {
        if (totalSeats <= 60) {
            System.out.println("Ticket price: $10");
        } else {
            int frontHalf = rows / 2;
            if (row <= frontHalf) {
                System.out.println("Ticket price: $10");
            } else {
                System.out.println("Ticket price: $8");
            }
        }
    }

    public static void setMaxIncome(){
        if (totalSeats <= 60) {
            maxIncome = totalSeats * 10;
        } else {
            int frontHalf = rows / 2;
            int backHalf = rows - frontHalf;
            maxIncome = (frontHalf * seats * 10) + (backHalf * seats * 8);
        }
    }
}