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
        long startTime = System.nanoTime();
        GraphARS graph = new GraphARS(1000);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime); 
        System.out.println("Execution Time : " + (duration/1000000) + " milliseconds");
        graph.launchAlgorithm(false);
        endTime = System.nanoTime();
        duration = (endTime - startTime); 
        System.out.println("Execution Time : " + (duration/1000000) + " milliseconds");
       // System.out.println(graph);
        graph.toJSON();
    }
}
