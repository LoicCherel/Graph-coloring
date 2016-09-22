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
        Graph graph = new Graph(6);
        graph.colorGraph();

        System.out.println(graph);
    }
    
}
