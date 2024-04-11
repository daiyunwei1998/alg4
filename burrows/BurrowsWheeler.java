/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        // read from input
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }

        // create circularSuffixArray
        String input = sb.toString();
        CircularSuffixArray ca = new CircularSuffixArray(input);

        int[] output = new int[ca.length()];
        for (int i = 0; i < ca.length(); i++) {
            int id = ca.index(i);
            if (id == 0) {
                BinaryStdOut.write(i);
            }
            output[i] = id;
        }
        for (int i = 0; i < ca.length(); i++) {
            BinaryStdOut.write(input.charAt(ca.index(i)));
        }
        BinaryStdOut.close();
    }

    private static int[] sort(String s) {
        int N = s.length();
        int R = 256;
        int[] count = new int[R + 1];
        int[] indexArray = new int[N];
        char[] aux = new char[N];
        // Count frequencies of each element
        for (int i = 0; i < N; i++)
            count[s.charAt(i) + 1]++;

        // Compute cumulative counts
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        // Move elements to auxiliary array according to counts
        for (int i = 0; i < N; i++) {
            int id = count[s.charAt(i)]++;
            aux[id] = s.charAt(i);
            indexArray[id] = i;
        }

        return indexArray;

    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // int first = BinaryStdIn.readInt();
        // String s = BinaryStdIn.readString();
        String s = "ARD!RCAAAABB";
        int first = 3;
        // counting sort
        int N = s.length();
        int R = 256;
        int[] count = new int[R + 1];
        int[] next = new int[N];
        char[] aux = new char[N];
        char[] a = new char[N];

        // Count frequencies of each element
        for (int i = 0; i < N; i++)
            count[s.charAt(i) + 1]++;

        // Compute cumulative counts
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        // Move elements to auxiliary array according to counts
        for (int i = 0; i < N; i++) {
            int id = count[s.charAt(i)]++;
            aux[id] = s.charAt(i);
            next[id] = i;
        }

        // Copy back to the original array
        for (int i = 0; i < N; i++) {
            a[i] = aux[i];
        }


        // output the original message in correct order
        // BinaryStdOut.write(a[first]);
        System.out.println(a[first]);
        for (int i = 0; i < N - 1; i++) {
            BinaryStdOut.write(a[next[first]]);
            first = next[first];
        }

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {


        if (args[0].equals("-")) {
            transform();
        }
        if (args[0].equals("+")) {
            inverseTransform();
        }


    }
}
