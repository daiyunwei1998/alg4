import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {

    private ArrayList<Board> log;
    private Stack<Board> pathToGoal;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial board is null");
        }

        // create a list to store each dequeued board
        log = new ArrayList<>();
        // create the PQ
        MinPQ<SearchNode> PQ = new MinPQ<>(new SearchNodeComparator());

        // step 0
        PQ.insert(new SearchNode(initial, 0, null));
        SearchNode curr = PQ.delMin();
        log.add(curr.getBoard());

        int step = 0;
        while (!curr.getBoard().isGoal()) {

            for (Board neighbour : curr.getBoard().neighbors()) {
                SearchNode newNode = new SearchNode(neighbour, curr.getMoves() + 1, curr);
                if (curr.getPrev() != null && !curr.getPrev().getBoard().equals(neighbour)) {
                    PQ.insert(newNode);
                }
                else if (curr.getPrev() == null) {
                    PQ.insert(newNode);
                }
            }


            // System.out.println("Step:" + step);

            // System.out.println("PQ for step" + step);
            // printPQ(PQ);
            // System.out.println("Dequed is....");
            curr = PQ.delMin();
            step += 1;
            // System.out.println(curr.getBoard().debugString());


            log.add(curr.getBoard());
        }

        this.pathToGoal = new Stack<>();

        while (curr.prev != null) {
            this.pathToGoal.push(curr.getBoard());
            curr = curr.prev;
        }

    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode s1, SearchNode s2) {
            int priorityCompare = s1.compareTo(s2);
            if (priorityCompare == 0) {
                int manhattanCompare = Integer.compare(s1.board.manhattan(), s2.board.manhattan());
                return manhattanCompare;
            }
            return priorityCompare;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // todo
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return pathToGoal.size();
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }


        return pathToGoal;

    }


    private class SearchNode implements Comparable<SearchNode> {
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

        public int getMoves() {
            return moves;
        }

        public SearchNode getPrev() {
            return prev;
        }

        public Board getBoard() {
            return board;
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(this.getPriority(), o.getPriority());
        }

        private void printInfo() {
            System.out.println("Priority:" + this.getPriority());
            System.out.println("moves:" + this.moves);
            System.out.println("Manhattam:" + this.board.manhattan());
            // System.out.println(this.board.debugString());
        }
    }


    // public void printPQ(MinPQ<SearchNode> PQ) {
    //     for (SearchNode item : PQ) {
    //         System.out.printf("Priority: %2d\t", item.getPriority());
    //     }
    //     System.out.println(); // Move to the next line
    //
    //     for (SearchNode item : PQ) {
    //         System.out.printf("Moves: %6d\t", item.getMoves());
    //     }
    //     System.out.println(); // Move to the next line
    //
    //     for (SearchNode item : PQ) {
    //         System.out.printf("Manhattan: %2d\t", item.getBoard().manhattan());
    //     }
    //     System.out.println(); // Move to the next line
    //
    //
    //     int nRow = 0;
    //     while (nRow < 3) {
    //         StringBuilder row = new StringBuilder();
    //         for (SearchNode item : PQ) {
    //             for (int col = 0; col < item.board.dimension(); col++) {
    //                 row.append(item.board.tiles[nRow][col]);
    //                 row.append(" ");
    //             }
    //             row.append("\t\t\t");
    //         }
    //         System.out.println(row.toString());
    //         nRow += 1;
    //     }
    // }

    public static void main(String[] args) {


        String filename = "puzzle39.txt";
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

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }

        }
    }


}
