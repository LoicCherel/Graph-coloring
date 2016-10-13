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

        Graph graph = new Graph(50);
        
        graph.applySimulatedAnnealingAlgorithm();
        graph.displayColors();

        graph.toJSON();
    }
}
