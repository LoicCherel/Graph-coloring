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


/**
 *
 * @authors: Loïc Cherel, Thomas Raynaud, Wilians Rodulfo
 */
public class Graph implements Serializable{
    //Liste des sommets du graphe
    private List<Vertex> _lVertices;
    //Copie du graphe
    private Graph _backUp;
    //Tableau contenant toutes les couleurs du graphe en indice, et leur
    //occurence dans la case du tableau à l'indice de la couleur
    private int[] _colors;
    //Nombre de couleurs avec une occurence supérieure à zéro (couleurs existantes)
    private int _nbColors;
    
    //Compteur permettant de donner fin à la récursion de adaptNeighbours() si
    //la fonction est appelée plus de 20 fois
    private static int _colorsChanged = 0;
    private static boolean _file = false;
    private static boolean _fileTemp = false;

    public Graph() {
        _lVertices = new ArrayList<Vertex>();
        _colors = new int[5];
        _nbColors = 0;
        for(int i=0; i< 6 ; i++) {
            _lVertices.add(new Vertex(i));
        }
        this.colorGraph();
    }
    
    public Graph(int numberOfVertices) {
        Random rn = new Random();
        _lVertices = new ArrayList<Vertex>();
        _colors = new int[numberOfVertices];
        _nbColors = 0;
        int a;
        float threshold = ((float)numberOfVertices / (float)(numberOfVertices + 5 ));
        float prop;
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
        this.colorGraph();
    }
    
    /**
     * Appliquer l'algorithme du recuit simulé sur le graphe.
     * On itère sur cet algorithme jusqu'à ce que la température du "système"
     * soit égale à zéro. La température diminue si l'énergie du système a
     * diminuée
     */
    public void applySimulatedAnnealingAlgorithm(){
        //Initialisation de la température et de l'énergie
        double temperature = 100;
        double energy = this.getEnergy();
        double oldEnergy = energy;
        double energyVariation;
        double prob;
        Vertex A;
        int color;
        _colorsChanged = 0;
        /*Chaque passage dans cette boucle va faire cette série d'instructions:
        - on prend un sommet et une couleur au hasard
        - on change la couleur du sommet avec la nouvelle couleur choisie
        - si ce changement a fait varier l'énergie vers le haut, on garde ce
        changement avec une certaine probabilité
        - sinon, on annule le changement en récupérant une copie du graphe qui
        n'a pas eu ce changement.
        */
        //while(temperature > 0){
        for(int k = 0; k < 500; k++){
            Random rn = new Random();
            A = this.getRandomVertex();
            color = this.getRandomColor("allColors", A);
            if (color == -1){
                continue;
            }
            //On fait une copie du graphe avant les changements
            this.prepareBackUp();
            this.changeColor(A, color);
            energy = this.getEnergy();
            energyVariation = oldEnergy - energy;
            //Si l'énergie a augmenté, on rentre dans cette condition
            if (energyVariation > 0) {
                    //Calcul de la probabilité de carder le changement
                    prob = exp((-1 * energyVariation) / temperature);
                    //System.out.println("The energy has increased: prob = " + prob + ", temperature = " + temperature);
                    if (rn.nextDouble() > prob ){
                        //Changement annulé. On reprend la copie du graphe
                        this.chargeBackUp();
                        //System.out.println("Changes not saved... energy = " + this.getNumberOfColors());
                    }
                    else{
                        //System.out.println("Changes SAVED");
                    }
            }
            else if (energyVariation < 0){
                    //System.out.println("The energy has decreased");
            }
            else{
                //System.out.println("The energy has not changed");
                //temperature -= 0.1;
            }
            oldEnergy = energy;
            //On stocke l'énergie et la température du graphe pour évaluer
            //l'efficacité de l'algorithme
            checkEnergy(energy);
            checkTemperature(temperature);
        }
    }
    
    /**
     * 
     * @param A est le sommet auquel on change la couleur
     * @param color est la couleur donnée au sommet A
     * @return 1 s'il n'y a eu aucun problème, -1 sinon
     */
    public int changeColor(Vertex A, int color) {
        //On efface l'ancienne couleur de A
        this.eraseVertexColor(A);
        //On lui applique la nouvelle et on incrémente l'occurence de la couleur
        //dans _colors. Si l'occurence de la nouvelle couleur était nulle, on incrémente
        //aussi le nombre de couleurs existantes
        A.setColor(color);
        this._colors[color]++;
        if(this._colors[color] == 1){
            this._nbColors++;
        }
        //Il faut adapter les voisins du sommet A au changement de couleur.
        int continueColorChanges = this.adaptNeighbours(A);
        if(continueColorChanges == -1){
            //System.out.println("Error: the colors cannot be changed anymore");
            this.chargeBackUp();
            return -1;
        }
        else if(continueColorChanges == -2){
            return -1;
        }
        //System.out.println(this);
        //this.displayColors();
        return 1;
    }    
    
    public int adaptNeighbours(Vertex A){
        for(Vertex neighbour : A.getNeighbours()){
            if (A.getColor() == neighbour.getColor()){
                _colorsChanged++;
                if(_colorsChanged >= 30){
                    _colorsChanged = 0;
                    return -1;
                }
                int color = this.getRandomColor("allColors", A);
                if (color == -1){
                    //System.out.println("Error: the color of the vertex cannot be changed: only one color exists in the graph");
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
    
    public void colorGraph(){
        int color = 0;
        for(Vertex ver : _lVertices){
            ver.setColor(color);
            this._colors[color] = 1;
            color++;
            this._nbColors++;
        }
    }
    
    private void eraseVertexColor(Vertex a){
        int deletedColor = a.getColor();
        a.setColor(-1);
        this._colors[deletedColor]--;
        if(this._colors[deletedColor] == 0){
            this._nbColors--;
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
    
    /*public void charger(String nomFic){
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
    }*/
    
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

    private void checkEnergy(double energy) {
        if(!_file){
            System.out.println("Creation/Vidage du fichier");
        try {
            Files.deleteIfExists(Paths.get("energy.txt"));
            Files.write(Paths.get("energy.txt"), "".getBytes(), StandardOpenOption.CREATE);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        _file = true;
        }else{
            String string = energy + "\n";
            try {
                Files.write(Paths.get("energy.txt"), string.getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }
    
    private void checkTemperature(double temperature) {
        //System.out.println("Temperature: " + temperature);
        if(!_fileTemp){
            System.out.println("Creation/Vidage du fichier");
        try {
            Files.deleteIfExists(Paths.get("temperature.txt"));
            Files.write(Paths.get("temperature.txt"), "".getBytes(), StandardOpenOption.CREATE);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        _fileTemp = true;
        }else{
            String string = temperature + "\n";
            try {
                Files.write(Paths.get("temperature.txt"), string.getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
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
        for(int i = 0; i < graph._colors.length; i++){
            this._colors[i] = graph._colors[i];
        }
        this._nbColors = graph._nbColors;
    }
    
    public void displayGraph(){
        for(Vertex ver : _lVertices){
            System.out.println("{id :" + ver.getName() +"},");
        }
        
        for(Vertex ver : _lVertices){
            for(Vertex v : ver.getNeighbours()){
                if(v.getName()>ver.getName())
                    System.out.println("{source : " + ver.getName() + ", target : " + v.getName() +"},");
            }
        }
    }
    
    public void displayColors(){
        int existingColors[] = this.getGroupOfColors("allColors", null);
        if(existingColors == null){
            System.out.println("ERROR");
        }
        int arrayIndex = 0;
        for(int i = 0; i < this._colors.length; i++){
            if(i == existingColors[arrayIndex]){
                System.out.println("Color = " + i + ", frequency = " + this._colors[i]);
                arrayIndex++;
            }  
        }
    }

    @Override
    public String toString() {
        String graph = "";
        for (int i = 0; i < this._lVertices.size(); i++) {
            graph += this._lVertices.get(i).toString()+ "\n";
        }
        return graph;
    }
    
    public Vertex getRandomVertex() {
        Random randomVertex = new Random();
        int vertexIndex = randomVertex.nextInt(this._lVertices.size());
        return this._lVertices.get(vertexIndex);
    }
    
    /**
    *
    */
    private int getRandomColor(String groupOfColorsDescription, Vertex A){
        
        //On crée tout d'abord un tableau temporaire contenant que les couleurs
        //souhaitées. Cela évite de chercher plusieurs fois une couleur au
        //hasard dans toutes les couleurs jusqu'à ce que l'on tombe sur une
        //des couleurs souhaitées (toutes les couleurs, celles existantes
        //ou supprimées
        int[] groupOfColors = this.getGroupOfColors(groupOfColorsDescription, A);
        Random rn = new Random();
        //S'il n'y a pas de couleur supprimée, on arrête la fonction
        if(groupOfColors == null){
            return -1;
        }
        //On retourne un élément du tableau au hasard
        return groupOfColors[rn.nextInt(groupOfColors.length)];
    }
    
    public int[] getGroupOfColors(String specifiedGroup, Vertex A)
    {
        int arraySize;
        int arrayIndex = 0;
        int vertexColor = -1;
        if(specifiedGroup == "existingColors"){
            if(A != null){
                vertexColor = A.getColor();
                arraySize = this.getNumberOfColors() - 1;
            }
            else{
                arraySize = this.getNumberOfColors();
            }
        }
        else if(specifiedGroup == "deletedColors"){
            arraySize = this._colors.length - this.getNumberOfColors();
        }
        else if(specifiedGroup == "allColors"){
            if (A == null){
                vertexColor = -1;
                arraySize = this._colors.length;
            }
            else{
                vertexColor = A.getColor();
                arraySize = this._colors.length - 1;
            }
        }
        else return null;
        if (arraySize == 0) return null;
        int[] groupOfColors = new int[arraySize];
        for(int i = 0; i < this._colors.length; i++){
            if(arrayIndex < groupOfColors.length && ((specifiedGroup == "existingColors" && this._colors[i] > 0 && i != vertexColor) || (specifiedGroup == "deletedColors" && this._colors[i] == 0) || (specifiedGroup == "allColors" && i != vertexColor))){
                groupOfColors[arrayIndex] = i;
                arrayIndex++;
            }
        }
        return groupOfColors;
    }
    
    public int getLeastUsedColor(){
        int[] existingColors = this.getGroupOfColors("existingColors", null);
        if(existingColors == null){
            return -1;
        }
        int leastUsedColor = this._colors[existingColors[0]];
        for(int i = 0; i < existingColors.length; i++){
            if(this._colors[existingColors[i]] < leastUsedColor && this._colors[existingColors[i]] > 0){
                leastUsedColor = i;
            }
        }
        return leastUsedColor;
    }
    
    public int getMostUsedColor(){
        int[] existingColors = this.getGroupOfColors("existingColors", null);
        if(existingColors == null){
            return -1;
        }
        int mostUsedColor = this._colors[existingColors[0]];
        for(int i = 0; i < existingColors.length; i++){
            if(this._colors[existingColors[i]] > mostUsedColor && this._colors[existingColors[i]] > 0){
                mostUsedColor = i;
            }
        }
        return mostUsedColor;
    }
        
    public double getEnergy(){
        return 100.0 * (double)this.getNumberOfColors() + 99.0 * ((double)this.getLeastUsedColor()/(double)this.getMostUsedColor());
    }
    
    public int getNumberOfColors() {
        return this._nbColors;
    }
    
    public Graph getBackUp(){
        return this._backUp;
    }
}
