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
        Graph graph = new Graph(24);
        graph.colorGraph();

        System.out.println(graph);
        graph.sauvegarder("test.graph");

        System.out.println("Chargement");
        Graph graph2 = new Graph();
        graph.charger("test.graph");
        System.out.println(graph2 + " Apres");
        System.out.println("Fin Chargement");

    }
}
