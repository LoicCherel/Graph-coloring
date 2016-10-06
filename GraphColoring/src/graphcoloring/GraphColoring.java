package graphcoloring;

/**
 *
 * @author p1401687
 */
public class GraphColoring {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Graph graph = new Graph(100);
        graph.colorGraph();
        graph.applySimulatedAnnealingAlgorithm();
        System.out.println(graph);
        System.out.println("Number of colors: " + graph.getNumberOfColors());
        
        System.out.println(graph.getLeastUsedColor());
        
        /*System.out.println("Decreasing the number of colors...\n");
        for (int i = 0; i < 10; i++){
            graph.decreaseNumberOfColors();
            //System.out.println(graph);
            //System.out.println("Number of colors: " + graph.getNumberOfColors() + "\n");
        }
        System.out.println("Number of colors: " + graph.getNumberOfColors() + "\n");
        System.out.println("Increasing the number of colors...\n");
        for (int i = 0; i < 30; i++){
            graph.increaseNumberOfColors();
            System.out.println(graph);
            System.out.println("Number of colors: " + graph.getNumberOfColors() + "\n");
        }*/
    }
}
