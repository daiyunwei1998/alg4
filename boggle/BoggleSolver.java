/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.HashSet;

public class BoggleSolver {
    private MatchaTries dict;


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dict = new MatchaTries();
        for (String word : dictionary) {
            dict.add(word);
        }

    }

    private class MatchaTries {
        // customized TrieSet
        private static final int R = 26;
        private Node root = new Node();

        private class Node {
            private Node[] next;
            private boolean isEnd;

            private Node() {
                next = new Node[R];
                isEnd = false;
            }
        }

        public MatchaTries() {
            root = new Node();
        }

        public void add(String key) {
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null)
                x = new Node();
            if (d == key.length()) {
                x.isEnd = true;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], key, d + 1);
            return x;
        }

        public boolean contains(String key) {
            Node node = get(root, key, 0);
            return node != null && node.isEnd;
        }

        public boolean startsWith(String prefix) {
            return startsWith(root, prefix, 0);
        }

        private boolean startsWith(Node x, String prefix, int d) {
            if (x == null)
                return false;
            if (d == prefix.length())
                return true;
            char c = prefix.charAt(d);
            return startsWith(x.next[c - 'A'], prefix, d + 1);
        }


        private Node get(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length())
                return x;
            char c = key.charAt(d);
            return get(x.next[c - 'A'], key, d + 1);
        }
    }

    private class DFSearch {
        private boolean[][] marked;
        private BoggleBoard graph;
        private int nRows;
        private int nCols;
        private HashSet<String> allStrings;
        private MatchaTries dict;
        private MatchaTries allStringsTrie;

        private Iterable<int[]> adj(int row, int col) {
            HashSet<int[]> output = new HashSet<>();
            HashSet<Integer> rows = new HashSet<>();
            HashSet<Integer> cols = new HashSet<>();

            if (row - 1 >= 0) {
                rows.add(row - 1);
            }
            if (row + 1 < this.nRows) {
                rows.add(row + 1);
            }
            rows.add(row);

            if (col - 1 >= 0) {
                cols.add(col - 1);
            }
            if (col + 1 < this.nCols) {
                cols.add(col + 1);
            }
            cols.add(col);
            for (int i : rows) {
                for (int j : cols) {
                    if (i == row & j == col) {
                        continue;
                    }
                    output.add(new int[] { i, j });
                }
            }
            return output;
        }

        private DFSearch(BoggleBoard board, MatchaTries dict) {
            this.graph = board;
            this.nRows = board.rows();
            this.nCols = board.cols();
            this.marked = new boolean[nRows][nCols];
            this.allStrings = new HashSet<>();
            this.allStringsTrie = new MatchaTries();
            this.dict = dict;
            for (int sRow = 0; sRow < this.nRows; sRow++) {
                for (int sCol = 0; sCol < this.nCols; sCol++) {
                    search("", sRow, sCol);
                }
            }

        }

        private void search(String currentString, int row, int col) {
            this.marked[row][col] = true;
            char newLetter = this.graph.getLetter(row, col);
            if (newLetter == 'Q') {
                currentString += "QU";
            }
            else {
                currentString += newLetter;
            }

            // System.out.println("current:" + currentString);
            if (!this.dict.startsWith(currentString)) {
                // go no further
                this.marked[row][col] = false;
                return;
            }

            for (int[] point : adj(row, col)) {
                if (!marked[point[0]][point[1]]) {
                    search(currentString, point[0], point[1]);
                }
            }
            this.marked[row][col] = false;
            if (currentString.length() > 2 && this.dict.contains(currentString)) {

                this.allStrings.add(currentString);
                this.allStringsTrie.add(currentString);
            }
        }

        private HashSet<String> getAllStrings() {
            return this.allStrings;
        }
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        DFSearch dfs = new DFSearch(board, this.dict);
        return dfs.getAllStrings();

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!this.dict.contains(word)) {
            return 0;
        }
        int length = word.length();
        if (length == 3 | length == 4) {
            return 1;
        }
        if (length == 5) {
            return 2;
        }
        if (length == 6) {
            return 3;
        }
        if (length == 7) {
            return 5;
        }
        if (length >= 8) {
            return 11;
        }
        return 0;
    }


    public static void main(String[] args) {
        In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-q.txt");
        int size = 0;
        for (String s : solver.getAllValidWords(board)) {
            System.out.println(s);
            size += 1;
        }
        System.out.println(size);


    }
}
