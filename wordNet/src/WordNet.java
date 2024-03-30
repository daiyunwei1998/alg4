import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;
import java.util.HashMap;


public class WordNet {
    private HashMap<String, Bag<Integer>> synsets; // key: id, value: noun
    private HashMap<Integer, String> idMap; // key: id, value: noun
    private Digraph graph;
    private SAP s;

    private Bag<Integer> getSynsetsID(String noun) {
        return this.synsets.get(noun);
    }
    private String getSynset(int id) {
        return this.idMap.get(id);
    }
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("null input");
        }
        readSynsets(synsets);
        this.graph = new Digraph(this.idMap.size());
        addHypernyms(hypernyms);
        checkGraph();
        this.s = new SAP(this.graph);
    }
    private void checkGraph() {
        // check if graph is a rooted DAG
        Topological t = new Topological(this.graph);
        if (!t.hasOrder()) {
            throw new IllegalArgumentException("not a DAG");
        }
        int count = 0;
        for (int v = 0; v < this.graph.V(); v++) {
            if (this.graph.outdegree(v) == 0) {
                count += 1;
            }
            if (count > 1) {
                throw new IllegalArgumentException("more than one root");
            }
        }


    }
    private void readSynsets(String synsets) {
        this.synsets =  new HashMap<>();
        this.idMap = new HashMap<>();

        In synsetsIn = new In(synsets);

        String line;
        while ((line = synsetsIn.readLine()) != null) {
            String[] l = line.split(",");
            // if not already contains this word
            int id = Integer.parseInt(l[0]);
            String synset = l[1];
           for (String syn:synset.split(" ")) {
                if (!this.synsets.containsKey(syn)) {
                    this.synsets.put(syn, new Bag<>());
                    this.synsets.get(syn).add(id);
                } else {
                    this.synsets.get(syn).add(id);
                }
                if (!this.idMap.containsKey(id)) {
                    this.idMap.put(id, synset);
                }
            }


        }
    }

    private void addHypernyms(String hypernyms) {
        In hypernymsIn = new In(hypernyms);

        String line;
        while ((line = hypernymsIn.readLine()) != null) {
            String[] l = line.split(",");
            int idx = Integer.parseInt(l[0]);
            for (int i = 1; i < l.length; i++) {
                String item = l[i];
                this.graph.addEdge(idx, Integer.parseInt(item));
            }
        }

    }





    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.synsets.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {throw new IllegalArgumentException();}
        return this.synsets.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {throw new IllegalArgumentException();}
        if (!isNoun(nounA) || !isNoun(nounB)) {throw new IllegalArgumentException();}
        Bag<Integer> setA = getSynsetsID(nounA);
        Bag<Integer> setB = getSynsetsID(nounB);

        return this.s.length(setA, setB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {throw new IllegalArgumentException();}
        if (!isNoun(nounA) || !isNoun(nounB)) {throw new IllegalArgumentException();}
        Bag<Integer> setA = getSynsetsID(nounA);
        Bag<Integer> setB = getSynsetsID(nounB);
        int ancestor = s.ancestor(setA, setB);
        return idMap.get(ancestor);
    }


    // do unit testing of this class
    public static void main(String[] args){
        WordNet wn = new WordNet("testCase/synsets3.txt","testCase/hypernyms3InvalidTwoRoots.txt");


    }
}