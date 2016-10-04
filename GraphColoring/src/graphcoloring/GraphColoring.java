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
        boolean ser = true;
        Graph graph = new Graph(500);
        graph.colorGraph();
        graph.sauvegarder("graph500.ser");
    }
}
