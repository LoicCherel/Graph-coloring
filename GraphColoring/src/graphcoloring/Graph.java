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
        float threshold = 0.95f;
        
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
    
    private void addEdge(Vertex a, Vertex b) {
        a.addNeightbour(b);
    }
    
    public void colorGraph(){
        Integer count = 0;
        for(Vertex ver : _lVertices){
            ver.setColor(count.toString());
            count++;
        }
    }
    
    @Override
    public String toString(){
        return _lVertices.toString();
    }
    
}
