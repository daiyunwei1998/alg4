import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.HashSet;
import java.util.List;

public class SAP {
    private Digraph graph;
    private int[] distToV;
    private int[] distToW;
    //private int[] edgeToV;
    //private int[] edgeToW;
    private int[] marked;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("null Graph");
        }
        this.graph = G;
        this.distToV = new int[this.graph.V()];
        this.distToW = new int[this.graph.V()];
        //this.edgeToV = new int[this.graph.V()];
        //this.edgeToW = new int[this.graph.V()];

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int ancestor = ancestor(v, w);
        if (ancestor == -1) {
            return -1;
        }
        return this.distToV[ancestor] + this.distToW[ancestor];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        HashSet<Integer> vMarked = new HashSet<>();
        HashSet<Integer> wMarked = new HashSet<>();

        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();

        distToV[v] = 0;
        distToW[w] = 0;
        qV.enqueue(v);
        qW.enqueue(w);

        int vDist = 0;
        int wDist = 0;

        while(!qV.isEmpty() || !qW.isEmpty()) {
            if (!qV.isEmpty()) {
                int curr = qV.dequeue();
                distToV[curr] = vDist++;
                vMarked.add(curr);
                if (wMarked.contains(curr)) {
                    return curr;
                }

                for (int neighbour:this.graph.adj(curr)) {
                    qV.enqueue(neighbour);
                }

            }
            if (!qW.isEmpty()) {
                int curr = qW.dequeue();
                distToW[curr]= wDist++;
                wMarked.add(curr);
                if (vMarked.contains(curr)) {
                    return curr;
                }

                for (int neighbour:this.graph.adj(curr)) {
                    qW.enqueue(neighbour);
                }

            }
        }
        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // check corner cases
        for (Integer i:v) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer i:w) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }

        int ancestor = ancestor(v,w);
        if (ancestor == -1) {return -1;}
        //debug(); //TODO delete
        return  distToV[ancestor] + distToW[ancestor];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // check corner cases
        for (Integer i:v) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer i:w) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }

        Integer minDist = null;
        int ancestor = -1;
        HashSet<Integer> vMarked = new HashSet<>();
        HashSet<Integer> wMarked = new HashSet<>();

        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();

        for (int sourceV:v) {
            distToV[sourceV] = 0;
            //edgeToV[sourceV] = -1;
            vMarked.add(sourceV);
            for (int neighbourV: this.graph.adj(sourceV)) {

                //edgeToV[neighbourV] = sourceV;
                vMarked.add(neighbourV);
                distToV[neighbourV] = distToV[sourceV] + 1;
                qV.enqueue(neighbourV);
                if (wMarked.contains(neighbourV)) {
                    // first time meeting at this point
                    int pathDist = distToV[neighbourV] + distToW[neighbourV];
                    if (minDist != null && pathDist < minDist) {
                        ancestor = neighbourV;
                        minDist = pathDist;
                    } else if (minDist == null) {
                        ancestor = neighbourV;
                        minDist = pathDist;
                    }
                }
            }
        }
        for (int sourceW:w) {
            distToW[sourceW] = 0;
            //edgeToW[sourceW] = -1;
            wMarked.add(sourceW);
            for (int neighbourW: this.graph.adj(sourceW)) {
                qW.enqueue(neighbourW);
                //edgeToV[neighbourW] = sourceW;
                wMarked.add(neighbourW);
                distToW[neighbourW] = distToW[sourceW] + 1;
                if (vMarked.contains(neighbourW)) {
                    // first time meeting at this point
                    int pathDist = distToV[neighbourW] + distToW[neighbourW];
                    if (minDist != null && pathDist < minDist) {
                        ancestor = neighbourW;
                        minDist = pathDist;
                    } else if (minDist == null) {
                        ancestor = neighbourW;
                        minDist = pathDist;
                    }
                }
            }

        }

        while(!qV.isEmpty() || !qW.isEmpty()) {
            if (!qV.isEmpty()) {
                int curr = qV.dequeue();
                for (int neighbour : this.graph.adj(curr)) {
                    if (!vMarked.contains(neighbour)) {
                        vMarked.add(neighbour);
                        distToV[neighbour] = distToV[curr] + 1;
                        if (distToV[neighbour] < minDist) {
                            qV.enqueue(neighbour);
                        }
                        if (wMarked.contains(neighbour)) {
                            // first time meeting at this point
                            int pathDist = distToV[neighbour] + distToW[neighbour];
                            if (minDist != null && pathDist < minDist) {
                                ancestor = neighbour;
                                minDist = pathDist;
                            } else if (minDist == null) {
                                ancestor = neighbour;
                                minDist = pathDist;
                            }
                        }
                    }

                }
                if (!qW.isEmpty()) {
                    curr = qW.dequeue();
                    for (int neighbour : this.graph.adj(curr)) {
                        if (!wMarked.contains(neighbour)) {
                            wMarked.add(neighbour);
                            distToW[neighbour] = distToW[curr] + 1;
                            if (distToW[neighbour] < minDist) {
                                qW.enqueue(neighbour);
                            }
                            if (vMarked.contains(neighbour)) {
                                // first time meeting at this point
                                int pathDist = distToV[neighbour] + distToW[neighbour];
                                if (minDist != null && pathDist < minDist) {
                                    ancestor = neighbour;
                                    minDist = pathDist;
                                } else if (minDist == null) {
                                    ancestor = neighbour;
                                    minDist = pathDist;
                                }
                            }
                        }
                    }
                }
            }
        } // end of while loop

        // will return -1 if nothing found
        return ancestor;
    }
    private void debug() {
        for (int i = 0; i < distToV.length; i++) {
            System.out.println("i = "+i+" distToV: "+distToV[i]+" distToW: "+distToW[i]);
        }

    }
    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("testCase/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        Integer[] v = new Integer[2];
        Integer[] w = new Integer[2];
        v[0] = 2;
        v[1] = 11;
        w[0] = 5;
        w[1] = 12;

        int length   = sap.length(List.of(v), List.of(w));
        int ancestor = sap.ancestor(List.of(v), List.of(w));
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

}