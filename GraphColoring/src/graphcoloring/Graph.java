package graphcoloring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author p1401687
 */
public class Graph implements Serializable{
    
    private List<Vertex> _lVertices;

    public Graph() {
        _lVertices = new ArrayList<Vertex>();
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
    
    public void charger(String nomFic){
        FileInputStream f = null;
        try {
            File entree = new File(nomFic);
            f = new FileInputStream(entree);
            ObjectInputStream in = new ObjectInputStream(f);
            _lVertices =(List<Vertex>) in.readObject();
            System.out.println("Apres Chargement");
            in.close();
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f.close();
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sauvegarder(String nomFic){
        if (_lVertices.isEmpty()==false){
            FileOutputStream f = null;
            try {
                File sortie = new File(nomFic);
                f = new FileOutputStream(sortie);
                ObjectOutputStream o = new ObjectOutputStream(f);
                o.writeObject(_lVertices);
                o.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    f.close();
                } catch (IOException ex) {
                    Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else System.out.println("Valeurs nulles");
    }
    
    public void increaseNumberOfColors(){
        
    }

    public void decreaseNumberOfColors(){
        
    }
    
    public void randomColor(){
        
    }
}
