/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {


    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // initialize the sequence
        char[] sequence = new char[256];
        for (int i = 0; i < 256; i++) {
            sequence[i] = (char) i;
        }


        while (!BinaryStdIn.isEmpty()) {
            char newChar = BinaryStdIn.readChar();
            for (int i = 0; i < 256; i++) {
                if (sequence[i] == newChar) {
                    BinaryStdOut.write((char) i);
                    if (i != 0) {
                        // need to move to front
                        char elementToMove = sequence[i];
                        // Move element to the front (index 0)
                        for (int idx = i; idx > 0; idx--) {
                            sequence[idx] = sequence[idx - 1];
                        }
                        sequence[0] = elementToMove;

                    }
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // initialize the sequence
        char[] sequence = new char[256];
        for (int i = 0; i < 256; i++) {
            sequence[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int newChar = (int) BinaryStdIn.readChar();
            BinaryStdOut.write(sequence[newChar]);

            if (newChar != 0) {
                // need to move to front
                char elementToMove = sequence[newChar];
                // Move element to the front (index 0)
                for (int idx = newChar; idx > 0; idx--) {
                    sequence[idx] = sequence[idx - 1];
                }
                sequence[0] = elementToMove;
            }

        }
        BinaryStdOut.close();


    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        if (args[0].equals("+")) {
            decode();
        }
    }
}
