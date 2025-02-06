package battleship;

import java.util.List;

public class Ship {
    String name;
    int size;
    List<int[]> coordinates;

    public Ship(String name, int size, List<int[]> coordinates) {
        this.name = name;
        this.size = size;
        this.coordinates = coordinates;
    }

    public boolean isSunk(char[][] board) {
        for (int[] coord : coordinates) {
            int row = coord[0];
            int col = coord[1];
            if (board[row][col] != 'X') {
                return false;
            }
        }
        return true;
    }
}