/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private int[][] tiles;
    private int[][] goal;
    private int size;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = deepCopy(tiles); // create a copy, making board immutable
        this.size = tiles.length;

        int[][] goal = new int[this.size][this.size];
        int count = 1;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                goal[i][j] = count++;
                if (count == this.size * this.size + 1) {
                    goal[i][j] = 0;
                    this.goal = goal;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(this.size).append("\n");

        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                result.append(this.tiles[row][col]);

                // Append space only if it's not the last element in the row
                if (col < this.size - 1) {
                    result.append(" ");
                }
            }

            result.append("\n");
        }

        return result.toString();
    }

    private String debugString() {
        StringBuilder result = new StringBuilder();

        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                result.append(this.tiles[row][col]);

                // Append space only if it's not the last element in the row
                if (col < this.size - 1) {
                    result.append(" ");
                }
            }

            result.append("\n");
        }

        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return this.size;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int row = 0; row < this.size; row += 1) {
            for (int col = 0; col < this.size; col += 1) {
                if (this.tiles[row][col] != this.goal[row][col]) {
                    if (this.tiles[row][col] != 0) {
                        distance += 1;
                    }
                }
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        int expected_row;
        int expected_col;

        for (int row = 0; row < this.size; row += 1) {
            for (int col = 0; col < this.size; col += 1) {
                int target = this.tiles[row][col];
                if (target != 0) {
                    expected_row = (target - 1) / this.size;
                    expected_col = (target - 1) % this.size;
                    distance += Math.abs(expected_row - row) + Math.abs(expected_col - col);
                }
            }
        }
        return distance;
    }


    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object obj) {
        // Check if the object is the same reference
        if (this == obj) {
            return true;
        }

        // Check if the object is null
        if (obj == null) {
            return false;
        }

        // Check if the classes are the same
        if (getClass() != obj.getClass()) {
            return false;
        }

        // Cast the object to Board
        Board otherBoard = (Board) obj;

        // Check size
        if (this.dimension() != otherBoard.dimension()) {
            return false;
        }

        // Compare contents
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                if (this.tiles[row][col] != otherBoard.tiles[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[][] newTiles;
        ArrayList<Board> neighborsList = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (this.tiles[row][col] == 0) {

                    if (row > 0) {
                        newTiles = deepCopy(tiles);
                        int targetNumber = newTiles[row - 1][col];
                        newTiles[row][col] = targetNumber;
                        newTiles[row - 1][col] = 0;
                        neighborsList.add(new Board(newTiles));
                    }
                    if (row < size - 1) {
                        newTiles = deepCopy(tiles);
                        int targetNumber = newTiles[row + 1][col];
                        newTiles[row][col] = targetNumber;
                        newTiles[row + 1][col] = 0;
                        neighborsList.add(new Board(newTiles));
                    }
                    if (col > 0) {
                        newTiles = deepCopy(tiles);
                        int targetNumber = newTiles[row][col - 1];
                        newTiles[row][col] = targetNumber;
                        newTiles[row][col - 1] = 0;
                        neighborsList.add(new Board(newTiles));
                    }
                    if (col < size - 1) {
                        newTiles = deepCopy(tiles);
                        int targetNumber = newTiles[row][col + 1];
                        newTiles[row][col] = targetNumber;
                        newTiles[row][col + 1] = 0;
                        neighborsList.add(new Board(newTiles));
                    }
                }
            }
        }

        return neighborsList;
    }

    // Helper method to create a deep copy of a 2D array
    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] newTiles;
        ArrayList<Board> neighborsList = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (this.tiles[row][col] != 0) {

                    if (row > 0 && this.tiles[row - 1][col] != 0) {
                        newTiles = deepCopy(tiles);
                        int targetNumber = newTiles[row - 1][col];
                        newTiles[row - 1][col] = newTiles[row][col];
                        newTiles[row][col] = targetNumber;
                        return new Board(newTiles);
                    }
                    if (row < size - 1 && this.tiles[row + 1][col] != 0) {
                        newTiles = deepCopy(tiles);
                        int targetNumber = newTiles[row + 1][col];
                        newTiles[row + 1][col] = newTiles[row][col];
                        newTiles[row][col] = targetNumber;
                        return new Board(newTiles);

                    }
                    if (col > 0 && this.tiles[row][col - 1] != 0) {
                        newTiles = deepCopy(tiles);
                        int targetNumber = newTiles[row][col - 1];
                        newTiles[row][col - 1] = newTiles[row][col];
                        newTiles[row][col] = targetNumber;
                        return new Board(newTiles);
                    }
                    if (col < size - 1 && this.tiles[row][col + 1] != 0) {
                        newTiles = deepCopy(tiles);
                        int targetNumber = newTiles[row][col + 1];
                        newTiles[row][col + 1] = newTiles[row][col];
                        newTiles[row][col] = targetNumber;
                        return new Board(newTiles);
                    }
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {

        int[][] tiles = {
                { 1, 5, 2 },
                { 7, 0, 4 },
                { 8, 6, 3 }
        };

        Board b = new Board(tiles);

        System.out.println(b.manhattan());
        for (Board neighbour : b.neighbors()) {
            System.out.println(neighbour.toString());
        }

    }


}
