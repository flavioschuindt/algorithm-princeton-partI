import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	
	private final int n;
	private final int trials;
	private double[] percolationThresholds;
	private Double mean = null;
	private Double stddev = null;
	
	public PercolationStats(int n, int trials) {
		this.n = n;
		this.trials = trials;
		
		if (n <= 0 || trials <= 0) {
			throw new java.lang.IllegalArgumentException();
		}
		
		percolationThresholds = new double[trials];
		this.simulate();
	}
	
	public double mean() {
		if (mean == null)
			mean = StdStats.mean(percolationThresholds);
		return mean;
	}
	
	public double stddev() {
		if (stddev == null)
			stddev = StdStats.stddev(percolationThresholds);
		return stddev;
	}
	
	public double confidenceLo() {
		return this.mean() - (1.96 * stddev() / Math.sqrt(trials));
	}
	
	public double confidenceHi() {
		return this.mean() + (1.96 * stddev() / Math.sqrt(trials));
	}
	
	private void simulate() {
		
		for (int i = 0; i < trials; i++) {
			// Start experiment T
			Percolation percolation = new Percolation(n);
			while (!percolation.percolates())
			{
				int randomRow = StdRandom.uniform(1, n + 1);
				int randomCol = StdRandom.uniform(1, n + 1);
				percolation.open(randomRow, randomCol);
			}
			
			percolationThresholds[i] = (double)percolation.numberOfOpenSites() / (n*n);
		}
		
	}
	
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int t = Integer.parseInt(args[1]);
		
		PercolationStats ps = new PercolationStats(n, t);
		System.out.println("mean                    = " + ps.mean());
		System.out.println("stddev                  = " + ps.stddev());
		System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
	}
}
