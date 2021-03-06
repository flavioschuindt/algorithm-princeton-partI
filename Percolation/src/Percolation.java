import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	
	private enum State {
		OPEN, BLOCKED;
	}
	
	private final int n;
	private State lattice[][];
	private final WeightedQuickUnionUF uf;
	private int numberOfOpenSites;
	
	public Percolation(int n) {
		if (n <= 0)
			throw new java.lang.IllegalArgumentException();
		this.n = n;
		this.lattice = new State[n][n];
		this.uf = new WeightedQuickUnionUF(n * n + 2);
		numberOfOpenSites = 0;
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				this.lattice[i][j] = State.BLOCKED;
			}
		}
	}

	private void validatePosition(int row, int col) {
		if (row < 1 || row > this.n || col < 1 || col > this.n) {
			throw new java.lang.IllegalArgumentException("Invalid position.");
		}
	}
	
	private int getIndexFromLatticePosition(int row, int col) {
		return (row - 1) * n + (col - 1);
	}
	
	public void open(int row, int col) {
		if (isOpen(row, col))
			return;
		validatePosition(row, col);
		this.lattice[row - 1][col - 1] = State.OPEN;
		numberOfOpenSites++;
		
		if (row == 1) {
			uf.union(getIndexFromLatticePosition(row,col), n * n); // union with top virtual node
		}
		if(row == this.n) {
			uf.union(getIndexFromLatticePosition(row,col), n * n + 1); // union with bottom virtual node
		}
		
		if (col < n && isOpen(row, col + 1)) {
			uf.union(getIndexFromLatticePosition(row,col), getIndexFromLatticePosition(row, col + 1));
		}
		if (col > 1 && isOpen(row, col - 1)) {
			uf.union(getIndexFromLatticePosition(row,col), getIndexFromLatticePosition(row, col - 1));
		}
		if (row < n && isOpen(row + 1, col)) {
			uf.union(getIndexFromLatticePosition(row,col), getIndexFromLatticePosition(row + 1, col));
		}
		if (row > 1 && isOpen(row - 1, col)) {
			uf.union(getIndexFromLatticePosition(row,col), getIndexFromLatticePosition(row - 1, col));
		}
		
	}
	
	public boolean isOpen(int row, int col) {
		validatePosition(row, col);
		return this.lattice[row - 1][col - 1] == State.OPEN;
	}
	
	public boolean isFull(int row, int col) {
		validatePosition(row, col);
		return uf.connected(getIndexFromLatticePosition(row,col), n * n);
	}
	
	public int numberOfOpenSites() {
		return numberOfOpenSites;
	}
	
	public boolean percolates() {
		return uf.connected(n * n, n * n+1);
	}
	
	public static void main(String[] args) {
		In in = new In(args[0]);      // input file
        int n = in.readInt();         // n-by-n percolation system

        Percolation perc = new Percolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            System.out.println(perc.percolates());
        }
	}
}
