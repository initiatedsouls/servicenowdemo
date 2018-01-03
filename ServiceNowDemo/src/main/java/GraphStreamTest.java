import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class GraphStreamTest {
	public static void main(String[] args) {
		new GraphExplore();
	}
	
}

class GraphExplore{
	GraphExplore() {
        Graph graph = new SingleGraph("tutorial 1");

        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.display();

        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.addEdge("AD", "A", "D");
        graph.addEdge("DE", "D", "E");
        graph.addEdge("DF", "D", "F");
        graph.addEdge("EF", "E", "F");
    }
}