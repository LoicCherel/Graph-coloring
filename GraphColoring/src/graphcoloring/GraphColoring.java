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
        Graph graph = new Graph(10000);
        System.out.println(graph);
        GraphARS ars =  GraphARS.toGraphARS(graph);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime); 
        System.out.println("Execution Time : " + (duration/1000000) + " milliseconds");
        ars.launchAlgorithm(false);
        endTime = System.nanoTime();
        duration = (endTime - startTime); 
        System.out.println("Execution Time : " + (duration/1000000) + " milliseconds");
        System.out.println(ars);
        graph.toJSON();
    }
}
