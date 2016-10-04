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
    private List<Integer> _existingColors;
    private List<Integer> _deletedColors;
    private Graph _backUp;
    
    private static int _colorsChanged = 0;

    public Graph() {
        _lVertices = new ArrayList<Vertex>();
        _existingColors = new ArrayList<Integer>();
        _deletedColors = new ArrayList<Integer>();
        for(int i=0; i< 6 ; i++) {
            _lVertices.add(new Vertex(i));
        }
    }
    
    public Graph(int numberOfVertices) {
        Random rn = new Random();
        _lVertices = new ArrayList<Vertex>();
        _existingColors = new ArrayList<Integer>();
        _deletedColors = new ArrayList<Integer>();;
        int a;
        float threshold = ((float)numberOfVertices / (float)(numberOfVertices + 5 ));
        float prop;
        //System.out.println(threshold);
        for(int i=0; i<numberOfVertices; i++) {
            _lVertices.add(new Vertex(i));
        }
            
        int count = 0;
        for (Vertex ver : _lVertices){
            count++;
            for(int i=count; i<numberOfVertices; i++) {
                a = rn.nextInt(numberOfVertices+1);
                prop = (float)a / (float)numberOfVertices;
                //System.out.println(prop);
                if (prop >= threshold){
                    addEdge(ver,_lVertices.get(i));
                }
            }
        }
    }
    
    public void applySimulatedAnnealingAlgorithm(){
        
    }

    /**
     *changeColor() a 3 chances sur 4 de diminuer le nombre de couleurs du graphe,
     *et une chance sur 4 d'augmenter le nombre de couleurs
     */
    public void changeColor() {
        Random rn = new Random();
        int colorChoice = rn.nextInt(100);
        if (colorChoice < 75){
            //Si la diminution de couleurs crée des problemes (voisins avec la même couleur), il nous faut
            //utiliser une copie du graphe pour revenir à l'état où le graphe fonctionne
            this.prepareBackUp();
            this.decreaseNumberOfColors();
        }
        else{
            this.increaseNumberOfColors();
        }
        //Une fois que nous avons adapté la couleur des voisins avec la couleur du sommet modifié, nous
        //remettons l'itérateur qui compte le nombre de fois que des couleurs ont changé dans le graphe
        //à zéro
        _colorsChanged = 0;
    }
    
    /**
     *
     */
    public void decreaseNumberOfColors(){
        this.prepareBackUp();
        Vertex A = this.getRandomVertex();
        int colorChosen = this.getRandomExistingColor(A);
        if (colorChosen == -1){
            System.out.println("Error: the color of the vertex cannot be changed: only one color exists in the graph");
            return;
        }
        this.changeColor(A, colorChosen);
    }
    
    public void increaseNumberOfColors(){
        Vertex A = this.getRandomVertex();
        int colorChosen = this.getRandomDeletedColor();
        if (colorChosen == -1){
            System.out.println("Error: no color has been deleted in the graph");
            return;
        }
        for(int i = 0; i < _deletedColors.size(); i++){
            if (_deletedColors.get(i) == colorChosen){
                _deletedColors.remove(i);
            }
        }
        _existingColors.add(colorChosen);
        this.changeColor(A, colorChosen);
    }
    
    public int changeColor(Vertex A, int color) {
        this.eraseVertexColor(A);
        A.setColor(color);
        int continueColorChanges = this.adaptNeighbours(A);
        if(continueColorChanges == -1){
            System.out.println("Error: the colors cannot be changed anymore");
            this.chargeBackUp();
            return -1;
        }
        else if(continueColorChanges == -2){
            return -1;
        }
        return 1;
    }    
    
    public int adaptNeighbours(Vertex A){
        System.out.println(".");
        for(Vertex neighbour : A.getNeighbours()){
            if (A.getColor() == neighbour.getColor()){
                _colorsChanged++;
                if(_colorsChanged >= 20){
                    _colorsChanged = 0;
                    return -1;
                }
                int color = this.getRandomExistingColor(A);
                if (color == -1){
                    System.out.println("Error: the color of the vertex cannot be changed: only one color exists in the graph");
                    this.chargeBackUp();
                    return -2;
                }
                int continueColorChanges = this.changeColor(neighbour, color);
                if (continueColorChanges == -1){
                    return -2;
                }
            }
        }
        return 1;
    }
    
    public void prepareBackUp(){
        this._backUp = new Graph(this._lVertices.size());
        this._backUp.equalsTo(this);
    }

    public void chargeBackUp(){
        this.equalsTo(this.getBackUp());
    }
    
    public void equalsTo(Graph graph){
        for(int i = 0; i < graph._lVertices.size(); i++){
            this._lVertices.get(i).setColor(graph._lVertices.get(i).getColor());
            this._lVertices.get(i).setName(graph._lVertices.get(i).getName());
            this._lVertices.get(i).getNeighbours().clear();
        }
        for(int i = 0; i < graph._lVertices.size(); i++){
            for(int j = 0; j < graph._lVertices.get(i).getNeighbours().size(); j++){
                int nameNeighbour = graph._lVertices.get(i).getNeighbours().get(j).getName();
                this._lVertices.get(i).addNeighbour(this.findVertex(nameNeighbour));
            }
        }
        this._deletedColors.clear();
        this._existingColors.clear();
        for(int i = 0; i < graph._existingColors.size(); i++){
            this._existingColors.add((int)graph._existingColors.get(i));
        }
        for(int i = 0; i < graph._deletedColors.size(); i++){
            this._deletedColors.add((int)graph._deletedColors.get(i));
        }
    }
    
    public void colorGraph(){
        Integer count = 0;
        for(Vertex ver : _lVertices){
            ver.setColor(count);
            _existingColors.add(count);
            count++;
        }
    }
    
    private int getRandomExistingColor(Vertex a){
        if (_existingColors.size() <= 1){
            return -1;
        }
        Random rn = new Random();
        int index = rn.nextInt(_existingColors.size());
        while (a.getColor() == _existingColors.get(index)){
            index = rn.nextInt(_existingColors.size());
        }
        return _existingColors.get(index);
    }
    
    private int getRandomDeletedColor(){
        if (_deletedColors.size() < 1){
            return -1;
        }
        Random rn = new Random();
        int index = rn.nextInt(_deletedColors.size());
        return _deletedColors.get(index);
    }
    
    private void eraseVertexColor(Vertex a){
        int deletedColor = a.getColor();
        a.setColor(-1);
        boolean deletedColorExists = false;
        for(Vertex ver : _lVertices){
            if (ver.getColor() == deletedColor){
                deletedColorExists = true;
            }
        }
        if (!deletedColorExists){
            for(int i = 0; i < _existingColors.size(); i++){
                if (_existingColors.get(i) == deletedColor){
                    _existingColors.remove(i);
                }
            }
            _deletedColors.add(deletedColor);
        }
    }
    
    private void addEdge(Vertex a, Vertex b) {
        a.addNeighbour(b);
        b.addNeighbour(a);
    }
    
    private Vertex findVertex(int nameVertex){
        for(int i = 0; i < this._lVertices.size(); i++){
            if (this._lVertices.get(i).getName() == nameVertex){
                return this._lVertices.get(i);
            }
        }
        return null;
    }

    public Vertex getRandomVertex() {
        Random randomVertex = new Random();
        int vertexIndex = randomVertex.nextInt(this._lVertices.size());
        return this._lVertices.get(vertexIndex);
    }

    public int getNumberOfColors() {
        return _existingColors.size();
    }
    
    public Graph getBackUp(){
        return this._backUp;
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
            for(Vertex ver : _lVertices){
                _existingColors.add(ver.getColor());
            }
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
    
    public void randomColor(){
        
    }
}
