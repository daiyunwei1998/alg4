/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {
    private int numberOfTeams;
    private HashMap<String, Integer> index;
    private HashMap<Integer, String> indexToTeam;
    private HashMap<String, Integer> wins;
    private HashMap<String, Integer> losses;
    private HashMap<String, Integer> left;
    private int[][] g;
    private HashMap<String, HashSet<String>> certificates;
    private HashMap<String, HashMap<String, Integer>> allVerticesMap;

    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        In file = new In(filename);
        this.numberOfTeams = Integer.parseInt(file.readLine());
        this.index = new HashMap<>();
        this.indexToTeam = new HashMap<>();
        this.wins = new HashMap<>();
        this.losses = new HashMap<>();
        this.left = new HashMap<>();
        this.g = new int[this.numberOfTeams][this.numberOfTeams];
        this.certificates = new HashMap<>();
        this.allVerticesMap = new HashMap<>();
        int index = 0;
        while (file.hasNextLine()) {
            String[] lineElements = file.readLine().split("\\s+");
            String team = lineElements[0];
            this.index.put(team, index);
            this.indexToTeam.put(index, team);
            this.wins.put(team, Integer.parseInt(lineElements[1]));
            this.losses.put(team, Integer.parseInt(lineElements[2]));
            this.left.put(team, Integer.parseInt(lineElements[3]));
            int[] gArray = new int[this.numberOfTeams];
            for (int i = 0; i < this.numberOfTeams; i++) {
                gArray[i] = Integer.parseInt(lineElements[4 + i]);
            }
            this.g[index] = gArray;
            index += 1;
        }

    }

    public int numberOfTeams() {
        // number of teams
        return this.numberOfTeams;
    }

    public Iterable<String> teams() {
        // all teams
        return this.wins.keySet();
    }

    public int wins(String team) {
        // number of wins for given team
        return this.wins.get(team);
    }

    public int losses(String team) {
        // number of losses for given team
        return this.losses.get(team);
    }

    public int remaining(String team) {
        // number of remaining games for given team
        return this.left.get(team);
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        return this.g[this.index.get(team1)][this.index.get(team2)];
    }


    public boolean isEliminated(String team) {
        // is given team eliminated?
        // Trivial elimination
        for (String otherTeam : this.teams()) {
            if (this.wins(team) + this.remaining(team) < this.wins(otherTeam)) {
                return true;
            }
        }

        // Nontrivial elimination.
        FlowNetwork flow = formulationGraph(team);
        FordFulkerson ff = new FordFulkerson(flow, 0, flow.V() - 1);


        // check if all edges are saturated
        for (FlowEdge edge : flow.adj(0)) {

            if (edge.capacity() > edge.flow()) {
                // System.out.println(edge.from() + "->" + edge.to());
                HashSet<String> output = new HashSet<>();
                for (String t : this.teams()) {
                    if (t != team) {
                        HashMap<String, Integer> verticesMap = this.allVerticesMap.get(team);
                        int v = verticesMap.get(Integer.toString(this.index.get(t)));
                        if (ff.inCut(v)) {
                            output.add(t);
                        }
                    }

                }
                this.certificates.put(team, output);
                return true; // any edge not saturated
            }
        }
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        return this.certificates.get(team);
    }

    public FlowNetwork formulationGraph(String team) {
        // Setting up vertices label to index mapping
        HashMap<String, Integer> verticesMap = new HashMap<>();
        int currCount = 0;
        verticesMap.put("s", currCount++);
        // int numberOfGames = (1 + Combination.combination(this.numberOfTeams - 1, 2) + this.numberOfTeams) / 2;
        // game vertices
        for (int i = 0; i < this.numberOfTeams; i++) {
            for (int j = i + 1; j < this.numberOfTeams; j++) {
                if (i != this.index.get(team) & j != this.index.get(team)) {
                    verticesMap.put(Integer.toString(i) + "-" + Integer.toString(j), currCount++);
                }
            }
        }
        // team vertices
        for (int i = 0; i < this.numberOfTeams; i++) {
            if (i != this.index.get(team)) {
                verticesMap.put(Integer.toString(i), currCount++);
            }
        }
        // t vertex
        verticesMap.put("t", currCount);
        this.allVerticesMap.put(team, verticesMap);

       /* for (String key : verticesMap.keySet()) {
            System.out.println(key + " => " + Integer.toString(verticesMap.get(key)));

        }*/

        // creating the weighted DAG
        FlowNetwork graph = new FlowNetwork(verticesMap.size());

        // weight g12 = games left between 1 and 2
        for (int i = 0; i < this.numberOfTeams - 1; i++) {
            for (int j = i + 1; j < this.numberOfTeams - 1; j++) {
                if (i != this.index.get(team) & j != this.index.get(team)) {
                    int weight = this.g[i][j];
                    FlowEdge e = new FlowEdge(0, verticesMap.get(
                            Integer.toString(i) + "-" + Integer.toString(j)), weight);
                    graph.addEdge(e);
                }
            }
        }

        // weighted edge betwwen game vertices and team vertices
        for (int i = 0; i < this.numberOfTeams - 1; i++) {
            for (int j = i + 1; j < this.numberOfTeams - 1; j++) {
                if (i != this.index.get(team) & j != this.index.get(team)) {
                    FlowEdge e1 = new FlowEdge(
                            verticesMap.get(Integer.toString(i) + "-" + Integer.toString(j)),
                            verticesMap.get(Integer.toString(i)), Double.POSITIVE_INFINITY);
                    graph.addEdge(e1);
                    FlowEdge e2 = new FlowEdge(
                            verticesMap.get(Integer.toString(i) + "-" + Integer.toString(j)),
                            verticesMap.get(Integer.toString(j)), Double.POSITIVE_INFINITY);
                    graph.addEdge(e2);
                }
            }
        }

        // weighted edge between team and t
        for (int i = 0; i < this.numberOfTeams - 1; i++) {
            if (i != this.index.get(team)) {
                int weight = this.wins(team) + this.remaining(team) - this.wins(
                        this.indexToTeam.get(i));
                FlowEdge e = new FlowEdge(
                        verticesMap.get(Integer.toString(i)),
                        verticesMap.get("t"), weight);
                graph.addEdge(e);
            }

        }
        // System.out.println(graph.toString());
        return graph;
    }


    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams5.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }
}
