import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class WordNet {
    private HashMap<String, Bag<Integer>> synsets; // key: id, value: noun
    private HashMap<Integer, String> idMap; // key: id, value: noun
    private Digraph graph;

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
        //addHypernyms(hypernyms);
    }
    private void readSynsets(String synsets) {
        this.synsets =  new HashMap<>();
        this.idMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(synsets))) {
            String line;
            while ((line = reader.readLine()) != null) {
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
                        this.idMap.put(id, syn);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addHypernyms(String hypernyms) {
        try (BufferedReader reader = new BufferedReader(new FileReader(hypernyms))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] l = line.split(",");
                int idx = Integer.parseInt(l[0]);
                for (int i = 1; i < l.length; i++) {
                    String item = l[i];
                    this.graph.addEdge(idx, Integer.parseInt(item));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        SAP s = new SAP(this.graph);
        return s.length(setA, setB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {throw new IllegalArgumentException();}
        if (!isNoun(nounA) || !isNoun(nounB)) {throw new IllegalArgumentException();}
        SAP s = new SAP(this.graph);
        Bag<Integer> setA = getSynsetsID(nounA);
        Bag<Integer> setB = getSynsetsID(nounB);
        int ancestor = s.ancestor(setA, setB);
        return idMap.get(ancestor);
    }


    // do unit testing of this class
    public static void main(String[] args){
        WordNet wordnet = new WordNet("testCase/synsets15.txt","testCase/hypernyms15Path.txt");
        String nounA = "a";
        String nounB = "a";

        System.out.println(wordnet.sap(nounA, nounB));

    }
}