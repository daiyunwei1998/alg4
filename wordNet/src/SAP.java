import edu.princeton.cs.algs4.*;

import java.util.HashSet;

public class SAP {
    Digraph graph;
    private int[][] dictTo;
    private int[] marked;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.graph = G;
        this.dictTo = new int[this.graph.V()][this.graph.V()];

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int ancestor = ancestor(v, w);
        if (ancestor == -1) {
            return -1;
        }
        return this.dictTo[v][ancestor] + this.dictTo[w][ancestor];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        HashSet<Integer> vMarked = new HashSet<>();
        HashSet<Integer> wMarked = new HashSet<>();

        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();

        dictTo[v][v] = 0;
        dictTo[w][w] = 0;
        qV.enqueue(v);
        qW.enqueue(w);

        int vDist = 0;
        int wDist = 0;

        while(!qV.isEmpty() || !qW.isEmpty()) {
            if (!qV.isEmpty()) {
                int curr = qV.dequeue();
                dictTo[v][curr] = vDist++;
                vMarked.add(curr);
                if (wMarked.contains(curr)) {
                    return curr;
                }
                for (int other: this.graph.adj(curr)) {
                    for (int neighbour:this.graph.adj(curr)) {
                        qV.enqueue(neighbour);
                    }
                }
            }
            if (!qW.isEmpty()) {
                int curr = qW.dequeue();
                dictTo[w][curr]= wDist++;
                wMarked.add(curr);
                if (vMarked.contains(curr)) {
                    return curr;
                }
                for (int other: this.graph.adj(curr)) {
                    for (int neighbour:this.graph.adj(curr)) {
                        qW.enqueue(neighbour);
                    }
                }
            }
        }
        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return  0;


    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Integer minDist = null;
        HashSet<Integer> vMarked = new HashSet<>();
        HashSet<Integer> wMarked = new HashSet<>();

        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();

        for (int sourceV:v) {
            dictTo[sourceV][sourceV] = 0;
            vMarked.add(sourceV);
            for (int neighbourV: this.graph.adj(sourceV)) {
                qV.enqueue(neighbourV);
            }
        }
        for (int sourceW:w) {
            dictTo[sourceW][sourceW] = 0;
            vMarked.add(sourceW);
            for (int neighbourW: this.graph.adj(sourceW)) {
                qV.enqueue(neighbourW);
            }
        }
        while(!qV.isEmpty() || !qW.isEmpty()) {
            if (!qV.isEmpty()) {
                int curr = qV.dequeue();
                dictTo[v][curr] = vDict++;
                vMarked.add(curr);
                if (wMarked.contains(curr)) {
                    return curr;
                }
                for (int other: this.graph.adj(curr)) {
                    for (int neighbour:this.graph.adj(curr)) {
                        qV.enqueue(neighbour);
                    }
                }
            }
            if (!qW.isEmpty()) {
                int curr = qW.dequeue();
                dictTo[w][curr]= wDict++;
                wMarked.add(curr);
                if (vMarked.contains(curr)) {
                    return curr;
                }
                for (int other: this.graph.adj(curr)) {
                    for (int neighbour:this.graph.adj(curr)) {
                        qW.enqueue(neighbour);
                    }
                }
            }
        }
        return -1;


    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("testCase/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}