package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Battleship {
    private static final int SIZE = 10;
    private static final char WATER = '~';
    private static final char SHIP = 'O';
    private static final char HIT = 'X';
    private static final char MISS = 'M';

    private final char[][] board;
    private final List<Ship> ships;

    public Battleship() {
        board = new char[SIZE][SIZE];
        for (char[] row : board) {
            Arrays.fill(row, WATER);
        }

        ships = new ArrayList<>();
    }

    public void printBoard() {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        char rowLabel = 'A';
        for (int i = 0; i < SIZE; i++) {
            System.out.print(rowLabel++ + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printFogOfWarBoard() {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        char rowLabel = 'A';
        for (int i = 0; i < SIZE; i++) {
            System.out.print(rowLabel++ + " ");
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == HIT || board[i][j] == MISS) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print(WATER + " ");
                }
            }
            System.out.println();
        }
    }

    public void placeAllShips() {
        Scanner scanner = new Scanner(System.in);
        String[] shipNames = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        int[] shipSizes = {5, 4, 3, 3, 2};

        for (int i = 0; i < shipNames.length; i++) {
            printBoard();
            System.out.println("Enter the coordinates of the " + shipNames[i] + " (" + shipSizes[i] + " cells):");
            while (true) {
                String start = scanner.next();
                String end = scanner.next();
                if (placeShip(shipNames[i], shipSizes[i], start, end)) break;
            }
        }
        printBoard();
    }

    private boolean placeShip(String name, int size, String start, String end) {
        int[] startCoords = convertCoords(start);
        int[] endCoords = convertCoords(end);

        if (startCoords == null || endCoords == null) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }

        int row1 = startCoords[0], col1 = startCoords[1];
        int row2 = endCoords[0], col2 = endCoords[1];

        if (row1 != row2 && col1 != col2) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }

        int length = Math.abs(row2 - row1) + Math.abs(col2 - col1) + 1;
        if (length != size) {
            System.out.println("Error! Wrong length of the " + name + "! Try again:");
            return false;
        }

        List<int[]> coordinates = new ArrayList<>();
        for (int i = Math.min(row1, row2); i <= Math.max(row1, row2); i++) {
            for (int j = Math.min(col1, col2); j <= Math.max(col1, col2); j++) {
                if (board[i][j] == SHIP || isTooClose(i, j)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
                coordinates.add(new int[]{i, j});
            }
        }

        for (int[] coord : coordinates) {
            board[coord[0]][coord[1]] = SHIP;
        }

        ships.add(new Ship(name, size, coordinates));
        return true;
    }

    public void takeShot(String shot) {
        int[] coords = convertCoords(shot);
        if (coords == null) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return;
        }

        int row = coords[0];
        int col = coords[1];

        if (board[row][col] == SHIP) {
            board[row][col] = HIT;
            printFogOfWarBoard(); // Show fog-of-war board
            Ship hitShip = getHitShip(row, col);
            if (hitShip != null && hitShip.isSunk(board)) {
                System.out.println("You sank a ship! Press Enter and pass the move to another player");
            } else {
                System.out.println("You hit a ship! Press Enter and pass the move to another player");
            }
        } else if (board[row][col] == WATER) {
            board[row][col] = MISS;
            printFogOfWarBoard(); // Show fog-of-war board after a miss
            System.out.println("You missed! Press Enter and pass the move to another player");
        } else {
            System.out.println("You already shot here. Try again:");
        }
    }

    private boolean isTooClose(int row, int col) {
        int[] dx = {-1, 0, 1, 0, -1, -1, 1, 1};
        int[] dy = {0, -1, 0, 1, -1, 1, -1, 1};

        for (int i = 0; i < dx.length; i++) {
            int newRow = row + dx[i], newCol = col + dy[i];
            if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && board[newRow][newCol] == SHIP) {
                return true;
            }
        }
        return false;
    }

    private int[] convertCoords(String coord) {
        if (coord.length() < 2 || coord.length() > 3) return null;

        char row = coord.charAt(0);
        int col;
        try {
            col = Integer.parseInt(coord.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return null;
        }

        if (row < 'A' || row > 'J' || col < 0 || col >= SIZE) return null;
        return new int[]{row - 'A', col};
    }

    private Ship getHitShip(int row, int col) {
        for (Ship ship : ships) {
            for (int[] coord : ship.coordinates) {
                if (coord[0] == row && coord[1] == col) {
                    return ship;
                }
            }
        }
        return null;
    }

    public boolean areAllShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk(board)) {
                return false;
            }
        }
        return true;
    }
}