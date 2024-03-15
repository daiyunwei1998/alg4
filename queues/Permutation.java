/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String value = StdIn.readString();
            queue.enqueue(value);
        }
        int k = Integer.parseInt(args[0]);
        for (int i = 0; i < k; i++) {
            System.out.println(queue.dequeue());
        }
    }
}
