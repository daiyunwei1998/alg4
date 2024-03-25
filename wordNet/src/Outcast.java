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
        WordNet wordnet = new WordNet("testCase/synsets.txt","testCase/hypernyms.txt");
        String nounA = "Ingmar_Bergman";
        String nounB = "polydactyly";
        System.out.println(wordnet.sap(nounA, nounB));

        //System.out.println(wordnet.distance(nounA, nounB));


    }
}
