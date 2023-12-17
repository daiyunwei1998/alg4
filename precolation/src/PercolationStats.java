
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.util.ArrayList;
import java.util.List;

public class PercolationStats {
    double[] record;
    int NOTrials;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        NOTrials = trials;
        record = new double[n];
        for (int i = 0; i < trials; i++){
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row,col);
            }
            double threshold = (double) p.numberOfOpenSites() /(n * n) ;
            record[i] = threshold;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(record);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(record);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(NOTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(NOTrials);
    }

    // test client (see below)
    public static void main(String[] args){
        int n =  Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats experiment = new PercolationStats(n,t);

        System.out.printf("mean                    = %.16f\n", experiment.mean());
        System.out.printf("stddev                  = %.16f\n", experiment.stddev());
        System.out.printf("95%% confidence interval = [%.16f, %.16f]\n", experiment.confidenceLo(), experiment.confidenceHi());
    }

}
