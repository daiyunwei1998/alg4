import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wn;
    public Outcast(WordNet wordnet)     {
        this.wn = wordnet;
        // constructor takes a WordNet object
    }
    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        String outcast ="";
        int maxDist = 0;
        for (String n:nouns) {
            int totalDist = 0;
            for (String other:nouns) {
                totalDist += wn.distance(n,other);
            }
            if (totalDist > maxDist) {
                outcast = n;
                maxDist = totalDist;
            }
        }
        return outcast;
    }
    public static void main(String[] args) {
        // see test client below
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
