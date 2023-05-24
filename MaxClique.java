import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MaxClique {

	public static int NUMBER_NODES;
	public static int CLIQUE_ITERATIONS;
	public static int TOLERANCE = 1000;
	public static int MAX_UNIQUE_ITERATIONS = 100;

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java MaxClique <DIMACS File Name> <Number of Iterations>");
			System.exit(0);
		}

		CLIQUE_ITERATIONS = Integer.parseInt(args[1]);

		System.out.println("Reading graph...");
		Graph graph = null;

		try {
			String file = args[0];
			graph = GraphReader.readGraph(file);
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}

		System.out.println("Computing clique...");
		long start = System.currentTimeMillis();

        // Initialize the clique
		Clique clique = new Clique(graph.maxNode, graph);
		List<SortedListNode> sortedPA = clique.sortPA();
		Iterator<SortedListNode> it = sortedPA.iterator();
		while (clique.pa.size() > 0) {
			SortedListNode node = it.next();
			if (clique.pa.contains(node.node)) {
				clique.addVertex(node.node);
			}
		}

        // Initialize variables
		List<Integer> bestClique = new ArrayList<Integer>();
		bestClique.addAll(clique.clique);
		int prevBest = clique.clique.size();
        int count = 0;
        int [] restarts = new int[NUMBER_NODES];
		Random random = new Random();

        // Randomized Heuristic algorithm
		for (int i = 0; i < CLIQUE_ITERATIONS; i++) {
			if(prevBest == bestClique.size()) {
                count++;
                if(count > TOLERANCE) {
                    randomRestart(clique, graph, restarts);
                    count = 0;
                }
            } else {
                prevBest = bestClique.size();
                count = 0;
            }

			/* Choose two vertices randomly and remove from the clique */
            int rand1 = random.nextInt(clique.clique.size());
            int vertex1 = clique.clique.get(rand1);
            int rand2 = random.nextInt(clique.clique.size());
            int vertex2 = clique.clique.get(rand2);
            while(rand1 == rand2) {
                rand2 = random.nextInt(clique.clique.size());
                vertex2 = clique.clique.get(rand2);
            }
            clique.removeVertex(vertex1);
            clique.removeVertex(vertex2);

            /* Add vertices to the clique based on the sorted possible additions TreeSet */
            if(clique.pa.size() > 0) {
                List<SortedListNode> sortedList = clique.sortPA();
                Iterator<SortedListNode> itt = sortedList.iterator();
                while(clique.pa.size() > 0) {
                    SortedListNode node = itt.next();
                    if (clique.pa.contains(node.node)) {
                    	clique.addVertex(node.node);
                    }
                }
            }

            // If the new clique is better than the current global best, replace the global best with this clique
            if(clique.clique.size() > bestClique.size()) {
                bestClique = new ArrayList<Integer>();
                bestClique.addAll(clique.clique);
            }
		}

		/*
         * Finally, try to improve upon the solution
         * considering all vertices one by one using 1-OPT moves
         */
        int maxClique = bestClique.size();
        clique.clique = bestClique;
        while(true) {
            boolean flag = false;
            for(int i = 0; i < clique.clique.size(); i++) {
                int vertex = clique.clique.get(i);
                clique.removeVertex(vertex);
                List<SortedListNode> sList = clique.sortPA();
                Iterator<SortedListNode> itt = sList.iterator();
                while(clique.pa.size() > 0) {
                    // itt should always be valid
                    SortedListNode node = itt.next();
                    if(clique.pa.contains(node.node)) {
                        clique.addVertex(node.node);
                    }
                }

                if(clique.clique.size() > maxClique) {
                    // Improvement has been made so iterate from the very start again
                    maxClique = clique.clique.size();
                    flag = true;
                    break;
                }
            }

            // If no improvement after iterating through all vertices, break out of the loop
            if(!flag)
            	break;
        }

        long end = System.currentTimeMillis();

        // Print the results
        System.out.println("Maximum Clique Size Found: " + bestClique.size());
        System.out.println("Vertices in the Clique:");
        for(Iterator<Integer> itr = bestClique.iterator(); itr.hasNext(); ) {
            int vertex = itr.next();
            System.out.print(vertex + " ");
        }

        System.out.println("\n\n" + (end-start) + " milliseconds (excluding I/O).");
	}

	/*
     * Random Restart the current search from a new random starting vertex
     */
    public static void randomRestart(Clique clique, Graph graph, int [] restarts) {
        int rand = (int)(Math.random() * (double)NUMBER_NODES);
        int count = 0;
        while(restarts[rand] == 1) {
            count++;
            if(count > MAX_UNIQUE_ITERATIONS)
                break;

            rand = (int)(Math.random() * (double)NUMBER_NODES);
        }
        restarts[rand] = 1;

        Clique clq = new Clique(rand, graph);
        List<SortedListNode> sortedList = clq.sortPA();
        Iterator<SortedListNode> it = sortedList.iterator();
        while(clq.pa.size() > 0) {
            SortedListNode node = it.next();
            if(clq.pa.contains(node.node)) {
                clq.addVertex(node.node);
            }
        }

        clique.clique = clq.clique;
        clique.pa = clq.pa;
    }
}