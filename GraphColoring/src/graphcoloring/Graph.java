package graphcoloring;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author p1401687
 */
public class Graph {
    
    private List<Vertex> _lVertices;

    public Graph() {
        _lVertices = new ArrayList<Vertex>();
        for(int i=0; i< 6 ; i++) {
            _lVertices.add(new Vertex(i));
        }
    }
    
    public Graph(int numberOfVertices) {
        Random rn = new Random();
        _lVertices = new ArrayList<Vertex>();
        int a;
        float threshold = ((float)numberOfVertices / (float)(numberOfVertices + 5 ));
        float prop;
        System.out.println(threshold);
        for(int i=0; i<numberOfVertices; i++) {
            _lVertices.add(new Vertex(i));
        }
            
        int count = 0;
        for (Vertex ver : _lVertices){
            count++;
            for(int i=count; i<numberOfVertices; i++) {
                a = rn.nextInt(numberOfVertices+1);
                prop = (float)a / (float)numberOfVertices;
                System.out.println(prop);
                if (prop >= threshold){
                    addEdge(ver,_lVertices.get(i));
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
        return j+1;
    }
    
    @Override
    public String toString() {
        String graph = "";
        for (int i = 0; i < this._lVertices.size(); i++) {
            graph += this._lVertices.get(i).toString()+ "\n";
        }
        return graph;
    }
    
}
