import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Clique {

	public List<Integer> pa;       // possible additions (nodes) to make a clique
	public List<Integer> clique;   // nodes of current clique
	public Graph graph;

	public Clique(int firstVertex, Graph graph) {
        pa = new ArrayList<Integer>();
        clique = new ArrayList<Integer>();
        clique.add(firstVertex);
        this.graph = graph;

        // Iterate through all vertices in graph instance and check if
        // they have connection with 'firstVertex'
        for (int i = 0; i < MaxClique.NUMBER_NODES; i++) {
            if(i == firstVertex)
                continue;

            if(graph.adjMatrix[i][firstVertex]) {
                pa.add(i);
            }
        }
    }

    // Add vertex to the current clique and update (remove) possible additions
    public void addVertex(int vertex) {
        clique.add(vertex);
        pa.remove((Integer) vertex);

        // Removes the node in pa if it does not connect to the newly added vertex 
        for (Iterator<Integer> it = pa.iterator(); it.hasNext(); ) {
            int paVertex = it.next();
            if (!graph.adjMatrix[paVertex][vertex]) {
                it.remove();
            }
        }
    }

    // Remove a vertex from the clique and update (add) possible additions
    public void removeVertex(int vertex) {
        clique.remove((Integer)vertex);

        // For every node i, check if it has a connection with all vertices
        // of the current clique
        for(int i = 0; i < MaxClique.NUMBER_NODES; i++) {
            if(clique.contains(i))
                continue;

            Iterator<Integer> it = clique.iterator();
            boolean flag = true;
            while(it.hasNext()) {
                int ver = it.next();
                if(!graph.adjMatrix[i][ver]) {
                    flag = false;
                    break;
                }
            }

            if(flag)
                pa.add(i);
        }
    }

    // Sort the possible additions list according to decreasing order of connections between them
    public List<SortedListNode> sortPA() {
        List<SortedListNode> sortedList = new ArrayList<SortedListNode>();

    	Iterator<Integer> it = pa.iterator();
    	while(it.hasNext()) {
    		int node1 = it.next();
            int reach = 0;
            Iterator<Integer> itt = pa.iterator();
            while (itt.hasNext()) {
                int node2 = itt.next();

                if (graph.adjMatrix[node1][node2])
                    reach++;
            }

            SortedListNode sortedNode = new SortedListNode(node1, reach);
            sortedList.add(sortedNode);
    	}

        Collections.sort(sortedList);
        return sortedList;
    }
}