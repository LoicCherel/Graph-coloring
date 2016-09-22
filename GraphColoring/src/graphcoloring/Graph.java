package graphcoloring;

import java.util.List;
import java.util.Random;

/**
 *
 * @author p1401687
 */
public class Graph {
    
    private List<Vertex> _lVertices;

    public Graph() {
        
    }
    
    public Graph(int numberOfVertices) {
        Random rn = new Random();
        int a;
        float threshold = 1 - (numberOfVertices / (numberOfVertices + 5 ));
        
        for(int i=0; i<numberOfVertices; i++) {
            _lVertices.add(new Vertex());
        }
            
        int count = 0;
        for (Vertex ver : _lVertices){
            count++;
            for(int i=count; i<numberOfVertices; i++) {
                a = rn.nextInt(numberOfVertices);
                if (a/numberOfVertices > threshold){
                    addEdge(ver,_lVertices.get(count));
                }
            }
        }
    }
    
    public void colorGraph(){
        Integer count = 0;
        for(Vertex ver : _lVertices){
            ver.setColor(count);
            count++;
        }
    }
    
    private void addEdge(Vertex a, Vertex b) {
        a.addNeighbour(b);
        b.addNeighbour(a);
    }

    public Vertex getRandomVertex() {
        Random randomVertex = new Random();
        int vertexIndex = randomVertex.nextInt(this._lVertices.size());
        return this._lVertices.get(vertexIndex);
    }

    public int getNumberOfColors() {
        int[] colors = new int[500];
        for (int i = 0; i < this._lVertices.size(); i++) {
            int tempColor = this._lVertices.get(i).getColor();
            int j = 0;
            boolean colorAdded = false;
            while (colors[j] != 0) {
                if (colors[j] == tempColor) {
                    colorAdded = true;
                    break;
                }
                j++;
            }
            if (!colorAdded) {
                colors[j] = tempColor;
            }
        }
        int j = 0;
        while (colors[j] != 0) j++;
        return j;
    }
    
    @Override
    public String toString() {
        String graph = "";
        for (int i = 0; i < this._lVertices.size(); i++) {
            graph += this._lVertices.get(i).toString();
        }
        return graph;
    }
    
}
