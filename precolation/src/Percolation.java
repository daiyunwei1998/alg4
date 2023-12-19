import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int noOfOpenSite;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufNoBottom;
    private boolean[][] openRecord;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            // Throw IllegalArgumentException if the argument is invalid
            throw new IllegalArgumentException("n should be greater than 0");
        }

        this.size = n;
        this.noOfOpenSite = 0;
        openRecord = new boolean[n][n];
        // n * n + top and bottom node
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufNoBottom = new WeightedQuickUnionUF(n * n + 1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Assign values to each element
                openRecord[i][j] = false;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        if (row <= 0 || row > size || col <= 0 || col > size) {
            // Throw IllegalArgumentException if the argument is invalid
            throw new IllegalArgumentException("the row and column indices are integers between 1 and n");
        }

        row = row - 1;
        col = col - 1;

        if (openRecord[row][col]) {
            return;
        }

        openRecord[row][col] = true;
        noOfOpenSite += 1;

        // check top
        if (row - 1 >= 0 && openRecord[row - 1][col]) {
            int currIndex = row * size + col;
            int topIndex = (row - 1) * size + col;
            uf.union(currIndex, topIndex);
            ufNoBottom.union(currIndex, topIndex);
        } else if (row == 0) { // first row
            int currIndex = col;
            int topIndex = size * size; // virtual top node
            uf.union(currIndex, topIndex);
            ufNoBottom.union(currIndex, topIndex);
        }
        // check left
        if (col - 1 >= 0 && openRecord[row][col - 1]) {
            int currIndex = row * size + col;
            int leftIndex = row * size + col - 1;
            uf.union(currIndex, leftIndex);
            ufNoBottom.union(currIndex, leftIndex);
        }
        // check right
        if (col + 1 < size && openRecord[row][col + 1]) {
            int currIndex = row * size + col;
            int rightIndex = row * size + col + 1;
            uf.union(currIndex, rightIndex);
            ufNoBottom.union(currIndex, rightIndex);
        }
        // check bottom
        if (row + 1 < size && openRecord[row + 1][col]) {
            int currIndex = row * size + col;
            int bottomIndex = (row + 1) * size + col;
            uf.union(currIndex, bottomIndex);
            ufNoBottom.union(currIndex, bottomIndex);
        } else if (row == size - 1) { // bottom row
            int currIndex = row * size + col;
            int bottomIndex = size * size + 1; // virtual bottom node
            uf.union(currIndex, bottomIndex);

        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            // Throw IllegalArgumentException if the argument is invalid
            throw new IllegalArgumentException("the row and column indices are integers between 1 and n");
        }

        row = row - 1;
        col = col - 1;

        return openRecord[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            // Throw IllegalArgumentException if the argument is invalid
            throw new IllegalArgumentException("the row and column indices are integers between 1 and n");
        }

        row = row - 1;
        col = col - 1;

        // check if connected to top node
        return ufNoBottom.find(size * row + col) == ufNoBottom.find(size * size);

    }

    private boolean isConnectedToBottom(int row, int col) {
        row = row - 1;
        col = col - 1;
        if (row < 0 || row > size || col < 0 || col > size) {
            // Throw IllegalArgumentException if the argument is invalid
            throw new IllegalArgumentException("the row and column indices are integers between 1 and n");
        }
        // check if connected to bottom node
        return uf.find(size * row + col) == uf.find(size * size + 1);

    }

    private boolean isConnected(int row1, int col1, int row2, int col2) {
        row1 = row1 - 1;
        col1 = col1 - 1;
        row2 = row2 - 1;
        col2 = col2 - 1;

        // check if connected to bottom node
        return uf.find(size * row1 + col1) == uf.find(size * row2 + col2);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return noOfOpenSite;
    }

    // does the system percolate?
    public boolean percolates() {
        // return true if top and bottom node is connected
        return uf.find(size * size) == uf.find(size * size + 1);
    }

    private void printInternal() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (openRecord[i][j]) {
                    System.out.print(1 + " ");
                } else {
                    System.out.print(0 + " ");
                }
            }
            System.out.println();
        }

    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(10);
        p.open(1, 1);
        p.percolates();
        p.isConnected(1, 1, 1, 2);
        p.isConnectedToBottom(1, 1);
        p.printInternal();
    }
}
