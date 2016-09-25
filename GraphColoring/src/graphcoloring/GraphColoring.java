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
        
        Graph graph = new Graph(3);
        graph.colorGraph();
        System.out.println(graph);
        
        for (int i = 0; i < 3; i++){
            graph.changeColor();
            System.out.println(graph);
            System.out.println("Number of colors: " + graph.getNumberOfColors());
        }
    }
    
}
