/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board implements Iterable<Board> {

    private int[][] tiles;
    private Integer[][] goal = {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 0 }
    };
    private int size;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.size = tiles.length;
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
                    distance += 1;
                }
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;

        for (int row = 0; row < this.size; row += 1) {
            for (int col = 0; col < this.size; col += 1) {
                int target = this.tiles[row][col];
                int expected_row = target / this.size;
                int expected_col = target % this.size;
                distance += expected_row + expected_col - row - col;
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Board y) {
        // check size
        if (this.dimension() != y.dimension()) {
            return false;
        }

        for (int row = 0; row < this.size; row += 1) {
            for (int col = 0; col < this.size; col += 1) {
                if (this.tiles[row][col] != y.tiles[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    Iterable<Board> neighbors() {
        return new NeighborsIterable();
    }

    public Iterator<Board> iterator() {
        return new NeighborsIterable().iterator();
    }

    // Define an iterator for the neighbors
    private class NeighborsIterable implements Iterable<Board>, Iterator<Board> {
        private ArrayList<int[]> neighbourList;
        private int[][] current; // current board state
        private int zeroRow;
        private int zeroCol;

        // Constructor to initialize the neighborList
        public NeighborsIterable() {
            this.neighbourList = new ArrayList<>();
            for (int row = 0; row < tiles.length; row++) {
                for (int col = 0; col < tiles.length; col++) {
                    if (tiles[row][col] == 0) {
                        this.zeroRow = row;
                        this.zeroCol = col;
                        if (row > 0) {
                            neighbourList.add(new int[] { row - 1, col });
                        }
                        if (row < tiles.length - 1) {
                            neighbourList.add(new int[] { row + 1, col });
                        }
                        if (col > 0) {
                            neighbourList.add(new int[] { row, col - 1 });
                        }
                        if (col < tiles.length - 1) {
                            neighbourList.add(new int[] { row, col + 1 });
                        }
                    }
                }
            }
        }

        // Implement the iterator() method from Iterable interface
        @Override
        public Iterator<Board> iterator() {
            // Reset any necessary iterator variables
            // (if needed based on your implementation)
            return this;
        }

        // Implement the hasNext() method from Iterator interface
        @Override
        public boolean hasNext() {
            return this.neighbourList.size() > 0;
        }

        // Implement the next() method from Iterator interface
        @Override
        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            int[][] newTiles = new int[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(tiles[i], 0, newTiles[i], 0, size);
            }

            int[] cord = this.neighbourList.remove(0);
            int targetNumber = newTiles[cord[0]][cord[1]];
            newTiles[zeroRow][zeroCol] = targetNumber;
            newTiles[cord[0]][cord[1]] = 0;

            return new Board(newTiles);
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    // public Board twin()


    public static void main(String[] args) {
        int[][] twoDArray = {
                { 1, 0, 3 },
                { 4, 5, 6 },
                { 7, 8, 2 }
        };
        Board b = new Board(twoDArray);
        for (Board neighbour : b.neighbors()) {
            System.out.println(neighbour.toString());

        }
    }


}
