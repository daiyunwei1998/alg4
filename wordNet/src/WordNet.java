import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

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
                if (!this.synsets.containsKey(l[1])) {
                    this.synsets.put(l[1], new Bag<>());
                } else {
                    this.synsets.get(l[1]).add(Integer.parseInt(l[0]));
                }
                if (!this.idMap.containsKey(Integer.parseInt(l[0]))) {
                    this.idMap.put(Integer.parseInt(l[0]), l[1]);
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
        WordNet w = new WordNet("testCase/synsets.txt","testCase/hypernyms.txt");
        //for (String noun:w.nouns()) {
          //  System.out.println(w.nouns());
        //}

    }
}