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
        GraphARS graph = new GraphARS(100);
        graph.applySimulatedAnnealingAlgorithm();
        System.out.println(graph);
       // System.out.println(graph);
        graph.toJSON();
    }
}
