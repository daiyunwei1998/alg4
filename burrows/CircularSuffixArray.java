/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class CircularSuffixArray {
    private int length;
    private int[] index;
    private CircularSuffix[] suffixes;

    // nested helper class
    private class CircularSuffix {
        private int startIndex;
        private String inputString;
        private int length;

        public CircularSuffix(int startIndex, String inputString) {
            this.startIndex = startIndex;
            this.inputString = inputString;
            this.length = inputString.length();
        }

        public char get(int i) {
            // adjust index according to startIndex
            int realIndex = (i + this.startIndex) % this.inputString.length();
            return this.inputString.charAt(realIndex);
        }

        public int length() {
            return this.length;
        }

    }

    // MSD sort
    private void sort(CircularSuffix[] a) {
        CircularSuffix[] aux = new CircularSuffix[a.length];
        int[] indexArrayAux = new int[this.index.length];
        sort(a, aux, 0, a.length - 1, 0, this.index, indexArrayAux);
    }

    private static int charAt(CircularSuffix suffix, int d) {
        if (d == suffix.length()) return -1;
        return suffix.get(d);

    }

    private void sort(CircularSuffix[] a, CircularSuffix[] aux, int lo, int hi,
                      int d, int[] indexArray, int[] indexArrayAux) {
        int R = 256;
        if (hi <= lo)
            return;

        // key-indexed counting
        int[] count = new int[R + 2];
        // count how many time each character appears
        for (int i = lo; i <= hi; i++)
            count[charAt(a[i], d) + 2]++;
        // make it accumulated count
        for (int r = 0; r < R + 1; r++)
            count[r + 1] += count[r];
        // System.out.println(Arrays.toString(count));
        // put into aux array with a new sorted order
        for (int i = lo; i <= hi; i++) {
            int id = count[charAt(a[i], d) + 1]++;
            aux[id] = a[i];
            indexArrayAux[id] = indexArray[i];
        }

        // put it back to original arrays
        for (int i = lo; i <= hi; i++) {
            a[i] = aux[i - lo]; // key-indexed counting
            indexArray[i] = indexArrayAux[i - lo];
        }

        // sort r subarrays recursively
        for (int r = 0; r < R; r++)
            sort(a, aux, lo + count[r], lo + count[r + 1] - 1,
                 d + 1, indexArray, indexArrayAux); // sort R subarrays recursively
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        this.length = s.length();
        this.suffixes = new CircularSuffix[length];
        this.index = new int[s.length()];
        for (int i = 0; i < length; i++) {
            suffixes[i] = new CircularSuffix(i, s);
            this.index[i] = i;
        }

        sort(suffixes);
       /* for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                System.out.print(suffixes[i].get(j));
            }
            System.out.println();
        }*/
    }

    // length of s
    public int length() {
        return this.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length() - 1) {
            throw new IllegalArgumentException();
        }
        return this.index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "couscous";
        CircularSuffixArray ca = new CircularSuffixArray(s);

        for (int i = 0; i < ca.length(); i++) {
            System.out.println(ca.index(i));
        }

    }
}
