package graphcoloring;

/**
 *
 * @author Loïc Cherel, Thomas Raynaud, Wilians Rodulfo
 */
public class GraphColoring {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Graph graph = new Graph(100);
        graph.applySimulatedAnnealingAlgorithm();
        System.out.println(graph);
        graph.toJSON();
    }
}
