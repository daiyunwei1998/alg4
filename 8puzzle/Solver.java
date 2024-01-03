import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {


        MinPQ<SearchNode> PQ = new MinPQ<>(new SearchNodeComparator());
        PQ.insert(new SearchNode(initial, 0, null));
        SearchNode curr = PQ.delMin();

        int step = 0;
        while (!curr.board.isGoal() && step < 5) {
            step += 1;
            for (Board neighbour : curr.board.neighbors()) {
                SearchNode newNode = new SearchNode(neighbour, curr.moves + 1, curr);
                if (curr.prev != null && !curr.prev.board.equals(neighbour)) {
                    PQ.insert(newNode);
                }
                else if (curr.prev == null) {
                    PQ.insert(newNode);
                }
            }

            System.out.println("Step:" + step);
            for (SearchNode n : PQ) {
                // System.out.println(n.board.debugString());
                n.printInfo();
            }
            System.out.println("Dequed is....");
            curr = PQ.delMin();
            curr.printInfo();

        }

    }

    class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode s1, SearchNode s2) {
            return s1.compareTo(s2);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return null;
    }


    public class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves; // the number of moves made to reach the board
        private SearchNode prev;
        private int priority;

        SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = ManhattanPriority();
        }

        private int HammingPriority() {
            return board.hamming() + moves;
        }

        private int ManhattanPriority() {
            return board.manhattan() + moves;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(this.getPriority(), o.getPriority());
        }

        public void printInfo() {

            System.out.println("Priority:" + this.getPriority());
            System.out.println("Manhattam:" + this.board.manhattan());
            System.out.println(this.board.debugString());
        }
    }


    public static void main(String[] args) {


        String filename = "puzzle04.txt";
        // read in the board specified in the filename
        In in = new In(filename);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // solve the slider puzzle
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        StdOut.println(filename + ": " + solver.moves());
    }


}
