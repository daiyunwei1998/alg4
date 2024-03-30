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
        this.graph = new Digraph(G);
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
    private void validateInput(Integer vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("null input vertex");
        }
        if (vertex < 0 || vertex >this.graph.V() - 1) {
            throw new IllegalArgumentException();
        }

    }
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateInput(v);
        validateInput(w);
        Integer minDist = null;
        int ancestor = -1;
        HashSet<Integer> vMarked = new HashSet<>();
        HashSet<Integer> wMarked = new HashSet<>();

        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();

        distToV[v] = 0;
        qV.enqueue(v);
        vMarked.add(v);

        distToW[w] = 0;
        if (vMarked.contains(w)) {
            return w;
        }
        qW.enqueue(w);
        wMarked.add(w);


        while(!qV.isEmpty() || !qW.isEmpty()) {
            if (!qV.isEmpty()) {
                int curr = qV.dequeue();
                for (int neighbour:this.graph.adj(curr)) {
                    if (!vMarked.contains(neighbour)) {
                        distToV[neighbour] = distToV[curr] + 1;
                        if (wMarked.contains(neighbour)) {
                            int pathDist = distToV[neighbour] + distToW[neighbour];
                            if (minDist != null && pathDist < minDist) {
                                ancestor = neighbour;
                                minDist = pathDist;
                            } else if (minDist == null) {
                                ancestor = neighbour;
                                minDist = pathDist;
                            }
                        }
                        qV.enqueue(neighbour);
                        vMarked.add(neighbour);
                    }
                }

            }
            if (!qW.isEmpty()) {
                int curr = qW.dequeue();
                for (int neighbour:this.graph.adj(curr)) {
                    if (!wMarked.contains(neighbour)) {
                        distToW[neighbour] = distToW[curr] + 1;
                        if (vMarked.contains(neighbour)) {
                            int pathDist = distToV[neighbour] + distToW[neighbour];
                            if (minDist != null && pathDist < minDist) {
                                ancestor = neighbour;
                                minDist = pathDist;
                            } else if (minDist == null) {
                                ancestor = neighbour;
                                minDist = pathDist;
                            }
                        }
                        qW.enqueue(neighbour);
                        wMarked.add(neighbour);
                    }
                }

            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int ancestor = ancestor(v,w);
        if (ancestor == -1) {return -1;}
        return  distToV[ancestor] + distToW[ancestor];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v==null || w == null) {
            throw new IllegalArgumentException();
        }
        // check corner cases
        for (Integer i:v) {
            validateInput(i);
        }
        for (Integer i:w) {
            validateInput(i);
        }

        Integer minDist = null;
        int ancestor = -1;
        HashSet<Integer> vMarked = new HashSet<>();
        HashSet<Integer> wMarked = new HashSet<>();

        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();

        for (int sourceV:v) {
            distToV[sourceV] = 0;
            vMarked.add(sourceV);
            qV.enqueue(sourceV);
        }

        for (int sourceW:w) {
            distToW[sourceW] = 0;
            if (vMarked.contains(sourceW)) {
                return sourceW;
            }
            wMarked.add(sourceW);
            qW.enqueue(sourceW);
        }

        while(!qV.isEmpty() || !qW.isEmpty()) {
            if (!qV.isEmpty()) {
                int curr = qV.dequeue();
                for (int neighbour : this.graph.adj(curr)) {
                    if (!vMarked.contains(neighbour)) {
                        distToV[neighbour] = distToV[curr] + 1;
                        if (minDist == null || distToV[neighbour] < minDist) {
                            qV.enqueue(neighbour);
                            vMarked.add(neighbour);
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

            }
            if (!qW.isEmpty()) {
                int curr = qW.dequeue();
                for (int neighbour : this.graph.adj(curr)) {
                    if (!wMarked.contains(neighbour)) {
                        distToW[neighbour] = distToW[curr] + 1;
                        if (minDist == null || distToW[neighbour] < minDist) {
                            qW.enqueue(neighbour);
                            wMarked.add(neighbour);
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

        // Create HashSet v and populate it with the given values
        HashSet<Integer> v = new HashSet<>();
        v.add(0);
        v.add(7);
        v.add(9);
        v.add(12);

        // Create HashSet w and populate it with the given values
        HashSet<Integer> w = new HashSet<>();
        w.add(1);
        w.add(2);
        w.add(4);
        w.add(5);
        w.add(12);
        w.add(10);


        int length   = sap.length(v,w);
        //int ancestor = sap.ancestor(v,w);
        int ancestor = 0;
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

}