// Adjacency Matrix representation in Java

public class Graph {

	public int maxNode; // Node with highest degree
	public boolean adjMatrix[][];

	private int numVertices;
	private int[] degrees;

	// Initialize the matrix
	public Graph(int numVertices) {
		this.numVertices = numVertices;
		adjMatrix = new boolean[numVertices][numVertices];
		degrees = new int[numVertices];
		maxNode = 0;
	}

	// Add edges
	public void addEdge(int i, int j) {
		adjMatrix[i][j] = true;
		adjMatrix[j][i] = true;
		degrees[i]++;
		degrees[j]++;

		if (degrees[i] > degrees[maxNode])
			maxNode = i;
		if (degrees[j] > degrees[maxNode])
			maxNode = j;
	}
}