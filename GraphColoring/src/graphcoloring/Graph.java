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
import static java.lang.Math.exp;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.util.Pair;


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
    private static boolean _file = false;

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
        _deletedColors = new ArrayList<Integer>();
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
        //int temperature = (this._lVertices.size() - 1) * 100;
        double temperature = 2;
        int energy, energyVariation;
        Vertex A;
        int color;
        _colorsChanged = 0;
        while(temperature > 0){
            Random rn = new Random();
            A = this.getRandomVertex();
            energy = this.getNumberOfColors();
            checkEnergy(energy);
            System.out.println("Energy: " + energy);
            this.prepareBackUp();
            if(rn.nextInt(100) <= 50){
                color = this.getRandomExistingColor(A);
                if (color == -1){
                    continue;
                }
            }
            else{
                color = this.getRandomDeletedColor();
                if (color == -1){
                    continue;
                }
                for(int i = 0; i < _deletedColors.size(); i++){
                    if (_deletedColors.get(i) == color){
                        _deletedColors.remove(i);
                    }
                }
                _existingColors.add(color);
            }
            //System.out.println("Energy of back up: " + this.getBackUp().getNumberOfColors());
            this.changeColor(A, color);
            energyVariation = this.getNumberOfColors() - energy;
            switch (energyVariation) {
                case 1:
                    double prob = exp((double)-1/temperature);
                    System.out.println("The energy has increased: prob = " + prob + ", temperature = " + temperature);
                    if (rn.nextDouble() > prob ){
                        this.chargeBackUp();
                        System.out.println("Changes not saved... energy = " + this.getNumberOfColors());
                    }
                    else{
                        System.out.println("Changes SAVED");
                    }
                    break;
                case -1:
                    System.out.println("The energy has decreased");
                    break;
                default:
                    System.out.println("The energy has not changed");
                    temperature -= 0.01;
                    break;
            }
        }
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
    public int decreaseNumberOfColors(){
        this.prepareBackUp();
        Vertex A = this.getRandomVertex();
        int colorChosen = this.getRandomExistingColor(A);
        if (colorChosen == -1){
            System.out.println("Il y a trop peu de couleurs : impossible d'en supprimer");
            return -1;
        }
        int feedback = this.changeColor(A, colorChosen);
        return feedback;
    }
    
    public int increaseNumberOfColors(){
        Vertex A = this.getRandomVertex();
        int colorChosen = this.getRandomDeletedColor();
        if (colorChosen == -1){
            System.out.println("Impossible de rajouter une couleur : il est nécessaire d'en supprimer auparavant");
            return -3;
        }
        for(int i = 0; i < _deletedColors.size(); i++){
            if (_deletedColors.get(i) == colorChosen){
                _deletedColors.remove(i);
            }
        }
        _existingColors.add(colorChosen);
        int feedback = this.changeColor(A, colorChosen);
        return feedback;
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
    
    public int getLeastUsedColor(){
        Map<Integer, Integer> colors = new HashMap<>();
        for(Integer color : _existingColors){
            colors.put(color, 0);
        }
        for(Vertex ver : this._lVertices){
            colors.replace(ver.getColor(), colors.get(ver.getColor()).intValue() + 1);
        }
        int min = colors.get(_lVertices.get(0).getColor()).intValue();
        for(Integer c : colors.keySet()){
            if (colors.get(c).intValue() < min) min = colors.get(c).intValue();
        }
        return min;
    }
    
    public int getMostUsedColor(){
        Map<Integer, Integer> colors = new TreeMap<>();
        for(Integer color : _existingColors){
            colors.put(color, 0);
        }
        for(Vertex ver : this._lVertices){
            colors.replace(ver.getColor(), colors.get(ver.getColor()).intValue() + 1);
        }
        int max = colors.get(_lVertices.get(0).getColor()).intValue();
        for(Integer c : colors.keySet()){
            if (colors.get(c).intValue() > max) max = colors.get(c).intValue();
        }
        return max;
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
    
    public void randomColor(){
        
    }

    private void checkEnergy(Integer energy) {
        if(!_file){
            System.out.println("Creation/Vidage du fichier");
        try {
            Files.deleteIfExists(Paths.get("myfile.txt"));
            Files.write(Paths.get("myfile.txt"), "".getBytes(), StandardOpenOption.CREATE);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        _file = true;
        }else{
            String string = energy.toString() + "\n";
            try {
                Files.write(Paths.get("myfile.txt"), string.getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }
}
