import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int NoOfOpenSite;
    private WeightedQuickUnionUF uf;
    private int[][] openRecord;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        this.size = n;
        this.NoOfOpenSite = 0;
        openRecord = new int[n][n];
        // n * n + top and bottom node
        uf = new WeightedQuickUnionUF(n^2 + 2 );

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Assign values to each element
                openRecord[i][j] = 0;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        if (row < 0 || row > size || col < 0 || col > size) {
            // Throw IllegalArgumentException if the argument is invalid
            throw new IllegalArgumentException("the row and column indices are integers between 1 and n");
        }
        openRecord[row][col] = 1;
        NoOfOpenSite += 1;

        //check top
        if (row - 1 > 0 && openRecord[row - 1][col] == 1) {
            int currIndex = row * size + col;
            int topIndex = (row-1) * size + col;
            uf.union(currIndex, topIndex);
        } else { //first row
            int currIndex = row * size + col;
            int topIndex = size * size + 1; //virtual top node
            uf.union(currIndex, topIndex);
        }
        //check left
        if (col - 1 > 0 && openRecord[row][col - 1] == 1) {
            int currIndex = row * size + col;
            int leftIndex = row * size + col - 1;
            uf.union(currIndex, leftIndex);
        }
        //check right
        if (col + 1 < size && openRecord[row][col + 1] == 1) {
            int currIndex = row * size + col;
            int rightIndex = row * size + col + 1;
            uf.union(currIndex, rightIndex);
        }
        //check bottom
        if (row + 1 < size && openRecord[row + 1][col] == 1) {
            int currIndex = row * size + col;
            int bottomIndex = (row + 1) * size + col;
            uf.union(currIndex, bottomIndex);
        } else { //bottom row
            int currIndex = row * size + col;
            int bottomIndex = size * size + 2; //virtual bottom node
            uf.union(currIndex, bottomIndex);
        }


    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if (row < 0 || row > size || col < 0 || col > size) {
            // Throw IllegalArgumentException if the argument is invalid
            throw new IllegalArgumentException("the row and column indices are integers between 1 and n");
        }
        return openRecord[row][col] != 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if (row < 0 || row > size || col < 0 || col > size) {
            // Throw IllegalArgumentException if the argument is invalid
            throw new IllegalArgumentException("the row and column indices are integers between 1 and n");
        }
        //heck if connected to top node
        return uf.find(size*row + col) == uf.find(size*size + 1);

    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return NoOfOpenSite;
    }

    // does the system percolate?
    public boolean percolates(){
        //return true if top and bottom node is connected
        return uf.find(size*size + 1) == uf.find(size*size + 1);
    }

    // test client (optional)
    public static void main(String[] args){

    }
}
