package graphcoloring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @authors: Loïc Cherel, Thomas Raynaud, Wilians Rodulfo
 */
public class Graph implements Serializable {

    //Liste des sommets du graphe
    List<Vertex> _lVertices;

    public Graph() {
        _lVertices = new ArrayList<Vertex>();
        for (int i = 0; i < 6; i++) {
            _lVertices.add(new Vertex(i));
        }
    }

    public Graph(int numberOfVertices) {
        Random rn = new Random();
        _lVertices = new ArrayList<Vertex>();
        int a;
        float threshold = ((float)numberOfVertices / (float)(numberOfVertices + 4 ));
        float prop;
        for (int i = 0; i < numberOfVertices; i++) {
            _lVertices.add(new Vertex(i));
        }
        int count = 0;
        for (Vertex ver : _lVertices) {
            count++;
            for (int i = count; i < numberOfVertices; i++) {
                a = rn.nextInt(numberOfVertices + 1);
                prop = (float) a / (float) numberOfVertices;
                //System.out.println(prop);
                if (prop >= threshold) {
                    addEdge(ver, _lVertices.get(i));
                }
            }
        }
    }

    public void addEdge(Vertex a, Vertex b) {
        a.addNeighbour(b);
        b.addNeighbour(a);
    }

    public Vertex findVertex(int nameVertex) {
        for (int i = 0; i < this._lVertices.size(); i++) {
            if (this._lVertices.get(i).getName() == nameVertex) {
                return this._lVertices.get(i);
            }
        }
        return null;
    }

    public void charger(String nomFic){
        FileInputStream f = null;
        try {
            File entree = new File(nomFic);
            f = new FileInputStream(entree);
            ObjectInputStream in = new ObjectInputStream(f);
            _lVertices =(List<Vertex>) in.readObject();
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
        }
        //else System.out.println("Valeurs nulles");
    }

    public void displayGraph() {
        for (Vertex ver : _lVertices) {
            System.out.println("{id :" + ver.getName() + "},");
        }

        for (Vertex ver : _lVertices) {
            for (Vertex v : ver.getNeighbours()) {
                if (v.getName() > ver.getName()) {
                    System.out.println("{source : " + ver.getName() + ", target : " + v.getName() + "},");
                }
            }
        }
    }

    @Override
    public String toString() {
        String graph = "";
        for (int i = 0; i < this._lVertices.size(); i++) {
            graph += this._lVertices.get(i).toString() + "\n";
        }
        return graph;
    }

    public Vertex getRandomVertex() {
        Random randomVertex = new Random();
        int vertexIndex = randomVertex.nextInt(this._lVertices.size());
        return this._lVertices.get(vertexIndex);
    }

    public void loadText(String myFile) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(myFile));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Vertex ver = new Vertex(Integer.parseInt(fields[0]));
                ver.setColor(Integer.parseInt(fields[0]));
                _lVertices.add(ver);
                String[] neighbours = fields[2].split("|");
                for (String t : neighbours) {
                    if (Integer.parseInt(t) < i) {
                        addEdge(ver, _lVertices.get(Integer.parseInt(t)));
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void toJSON(){
        String outputFile = "output.json";
        String output = "{\n  \"nodes\": [\n";
        boolean first = true;
        for(Vertex ver : _lVertices){
            if(first){
                output += "\n";
                first = false;
            }
            else output += ",\n";
            
            output += ("   {\"id\" :\"" + ver.getName() +"\", \"group\": " + ver.getColor() + "}");
        }
        output += "],\n  \"links\": [";
        first = true;
        for(Vertex ver : _lVertices){
            for(Vertex v : ver.getNeighbours()){
                if(v.getName()>ver.getName()){
                    if(first){
                        output += "\n";
                        first = false;
                    }
                    else output += ",\n";
                    output += "    {\"source\" : " + ver.getName() + ", \"target\" : " + v.getName() +"}";
                }
            }
        }
        output += "  ]\n}";
        try {
            Files.deleteIfExists(Paths.get(outputFile));
            Files.write(Paths.get(outputFile), output.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("ERREUR");
        }
    }
}
