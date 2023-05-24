import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.StringTokenizer;

public class GraphReader {

    public static Graph readGraph(String file) throws FileNotFoundException, IOException {
		FileReader fileReader = new FileReader(new File(file));
    	BufferedReader reader = new BufferedReader(fileReader);
    	String line = reader.readLine();

        // Read line until we get to the line where the number of nodes is found
        while(line.charAt(0) == 'c') {
            line = reader.readLine();
        }

        StringTokenizer token = new StringTokenizer(line, " ");
        token.nextToken();
        token.nextToken();
    	MaxClique.NUMBER_NODES = Integer.parseInt(token.nextToken().trim());
		Graph graph = new Graph(MaxClique.NUMBER_NODES);

        // Start reading the edges of the graph
		line = reader.readLine();
    	while(line != null) {
            token = new StringTokenizer(line, " ");
            token.nextToken();
            int sv = Integer.parseInt(token.nextToken().trim());
            int ev = Integer.parseInt(token.nextToken().trim());
            graph.addEdge(--sv, --ev);
            line = reader.readLine();
        }

		return graph;
    }
}