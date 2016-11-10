/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcoloring;

import java.io.IOException;
import static java.lang.Math.exp;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

/**
 *
 * @author Loïc
 */
public class GraphARS extends Graph{
    
    //Copie du graphe
    private GraphARS _backUp;
    private GraphARS _graphWithMinNbColors;
    private double _temperature;
    //Tableau contenant toutes les couleurs du graphe en indice, et leur
    //occurence dans la case du tableau à l'indice de la couleur
    private int[] _colors;
    //Nombre de couleurs avec une occurence supérieure à zéro (couleurs existantes)
    private int _nbColors;

    //Compteur permettant de donner fin à la récursion de adaptNeighbours() si
    //la fonction est appelée plus de 20 fois
    private static int _colorsChanged = 0;
    private static boolean _file = false;
    private static final int TEMPERATUREMAX = 1000;
    private static final int X = 15;

    public GraphARS() {
        _colors = new int[5];
        _nbColors = 0;
    }

    public GraphARS(int numberOfVertices) {
        super(numberOfVertices);
        _colors = new int[numberOfVertices];
        _nbColors = 0;
        this.colorGraph();
    }

    public void colorGraph() {
        int color = 0;
        for (Vertex ver : _lVertices) {
            ver.setColor(color);
            this._colors[color] = 1;
            color++;
            this._nbColors++;
        }
    }
    
    /**
     * Appliquer l'algorithme du recuit simulé sur le graphe. On itère sur cet
     * algorithme jusqu'à ce que la température du "système" soit égale à zéro.
     * La température diminue si l'énergie du système a diminuée
     */
    public void applySimulatedAnnealingAlgorithm() {
        //Initialisation de la température et de l'énergie
        this._temperature = TEMPERATUREMAX;
        boolean temperatureHasChanged = true;
        double energy = this.getEnergy();
        double oldEnergy = energy;
        double energyVariation;
        double prob;
        Vertex A;
        int color;
        int countReachMinColors = this._lVertices.size() * (int)this._temperature * X;
        int step = 0, stepMax = 10;
        
        this._backUp = new GraphARS(this._lVertices.size());
        this._graphWithMinNbColors = new GraphARS(this._lVertices.size());
        this.setGraphWithMinNbColors();
        this.prepareBackUp();
        //_colorsChanged = 0;
        /*Chaque passage dans cette boucle va faire cette série d'instructions:
        - on prend un sommet et une couleur au hasard
        - on change la couleur du sommet avec la nouvelle couleur choisie
        - si ce changement a fait varier l'énergie vers le haut, on garde ce
        changement avec une certaine probabilité
        - sinon, on annule le changement en récupérant une copie du graphe qui
        n'a pas eu ce changement.
         */
        storeVariables(energy, this._temperature);
        while (this._temperature > 0) {
            //for(int k = 0; k < 500; k++){
            Random rn = new Random();
            A = this.getRandomVertex();
            color = this.getRandomColor("allColors", A);
            if (color == -1) {
                continue;
            }
            //On fait une copie du graphe avant les changements
            this.prepareBackUp();
            this.changeColor(A, color);
            energy = this.getEnergy();
            energyVariation = oldEnergy - energy;
            //Si l'énergie a augmenté, on rentre dans cette condition
            if (energyVariation > 0) {
                //Calcul de la probabilité de garder le changement
                prob = exp((-1 * energyVariation) / this._temperature);
                //System.out.println("Energy: " + energy + ", temperature: "
                //        + temperature + ", var(Energy):" + energyVariation + ", prob: " + prob);
                //System.out.println("The energy has increased: prob = " + prob + ", temperature = " + temperature);
                if (rn.nextDouble() > prob) {
                    //Changement annulé. On reprend la copie du graphe
                    this.chargeBackUp();
                    //System.out.println("Changes not saved... nbOfColors = " + this.getNumberOfColors());
                } else {
                    //System.out.println("Changes SAVED.");
                }
            } else if (energyVariation < 0) {
                //System.out.println("The energy has decreased");
            } else {
                //System.out.println("The energy has not changed");
                /*int nbOfTries = 5;
                while((this.changeColor(A, this.getRandomColor("existingColors", A)) == - 1) && (nbOfTries > 0)) nbOfTries--;*/
                this._temperature -= 1;
                temperatureHasChanged = true;
            }
            
            oldEnergy = energy;
            //On stocke l'énergie et la température du graphe pour évaluer
            //l'efficacité de l'algorithme
            if(temperatureHasChanged){
                storeVariables(energy, this._temperature);
                step = 0;
                temperatureHasChanged = false;
            }
            //System.out.println(energyVariation); 
            if (this.getNumberOfColors() < this._graphWithMinNbColors.getNumberOfColors()){
                this._graphWithMinNbColors.equalsTo(this);
                countReachMinColors = this._lVertices.size() * (int)this._temperature * X;
            }
            if(countReachMinColors == 0){
                countReachMinColors = this._lVertices.size() * (int)this._temperature * X;
            }
            else{
                countReachMinColors--;
            }
        }
        if (this.getNumberOfColors() > this._graphWithMinNbColors.getNumberOfColors()){
            this.equalsTo(this._graphWithMinNbColors);
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
        if (this._colors[color] == 1) {
            this._nbColors++;
        }
        //Il faut adapter les voisins du sommet A au changement de couleur.
        int continueColorChanges = this.adaptNeighbours(A);
        if (continueColorChanges == -1) {
            //System.out.println("Error: the colors cannot be changed anymore");
            this.chargeBackUp();
            return -1;
        } else if (continueColorChanges == -2) {
            return -1;
        }
        //System.out.println(this);
        //this.displayColors();
        return 1;
    }

    public int adaptNeighbours(Vertex A) {
        for (Vertex neighbour : A.getNeighbours()) {
            //Si A a la même couleur qu'un de ses 
            if (A.getColor() == neighbour.getColor()) {
                _colorsChanged++;
                if (_colorsChanged >= this._lVertices.size() * 10) {
                    _colorsChanged = 0;
                    return -1;
                }
                int color = this.getRandomColor("allColors", A);
                //Cas dans lequel il n'y a plus qu'une couleur dans le graphe.
                //La couleur ne peut alors plus être changée
                if (color == -1) {
                    this.chargeBackUp();
                    return -2;
                }
                //On arrête de changer de couleurs
                int continueColorChanges = this.changeColor(neighbour, color);
                if (continueColorChanges == -1) {
                    return -2;
                }
            }
        }
        return 1;
    }
    
    private void eraseVertexColor(Vertex a) {
        int deletedColor = a.getColor();
        a.setColor(-1);
        this._colors[deletedColor]--;
        if (this._colors[deletedColor] == 0) {
            this._nbColors--;
        }
    }
    
    private void storeVariables(double energy, double temperature) {
        String outputFile = "../energy.csv";
        if (!_file) {
            System.out.println("Creation/Vidage du fichier");
            try {
                Files.deleteIfExists(Paths.get(outputFile));
                Files.write(Paths.get(outputFile), "".getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
            _file = true;
        } else {
            String string = (double)Math.round(energy * 1000) / 1000 + ";" + (int)(temperature) + ";\n";
            String str = string.replaceAll("\\.",",");
            try {
                Files.write(Paths.get(outputFile), string.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }

    public void setGraphWithMinNbColors(){
        this._graphWithMinNbColors.equalsTo(this);
    }

    public void prepareBackUp() {
        this._backUp.equalsTo(this);
    }

    public void chargeBackUp() {
        this.equalsTo(this.getBackUp());
    }
    
    public void charger(String nomFic){
        super.charger(nomFic);
        for(Vertex ver : _lVertices){
            _colors[ver.getColor()]++;
        }
    }
    
    public void displayColors() {
        int existingColors[] = this.getGroupOfColors("allColors", null);
        if (existingColors == null) {
            System.out.println("ERROR");
        }
        int arrayIndex = 0;
        for (int i = 0; i < this._colors.length; i++) {
            if (i == existingColors[arrayIndex]) {
                System.out.println("Color = " + i + ", frequency = " + this._colors[i]);
                arrayIndex++;
            }
        }
    }
    
    public void equalsTo(GraphARS graph) {
        for (int i = 0; i < graph._lVertices.size(); i++) {
            this._lVertices.get(i).setColor(graph._lVertices.get(i).getColor());
            this._lVertices.get(i).setName(graph._lVertices.get(i).getName());
            this._lVertices.get(i).getNeighbours().clear();
        }
        for (int i = 0; i < graph._lVertices.size(); i++) {
            for (int j = 0; j < graph._lVertices.get(i).getNeighbours().size(); j++) {
                int nameNeighbour = graph._lVertices.get(i).getNeighbours().get(j).getName();
                this._lVertices.get(i).addNeighbour(this.findVertex(nameNeighbour));
            }
        }
        for (int i = 0; i < graph._colors.length; i++) {
            this._colors[i] = graph._colors[i];
        }
        this._nbColors = graph._nbColors;
    }
    
    /**
     *
     */
    private int getRandomColor(String groupOfColorsDescription, Vertex A) {

        //On crée tout d'abord un tableau temporaire contenant que les couleurs
        //souhaitées. Cela évite de chercher plusieurs fois une couleur au
        //hasard dans toutes les couleurs jusqu'à ce que l'on tombe sur une
        //des couleurs souhaitées (toutes les couleurs, celles existantes
        //ou supprimées
        int[] groupOfColors = this.getGroupOfColors(groupOfColorsDescription, A);
        Random rn = new Random();
        //S'il n'y a pas de couleur supprimée, on arrête la fonction
        if (groupOfColors == null) {
            return -1;
        }
        //On retourne un élément du tableau au hasard
        return groupOfColors[rn.nextInt(groupOfColors.length)];
    }

    public int[] getGroupOfColors(String specifiedGroup, Vertex A) {
        int arraySize;
        int arrayIndex = 0;
        int vertexColor = -1;
        if (specifiedGroup == "existingColors") {
            if (A != null) {
                vertexColor = A.getColor();
                arraySize = this.getNumberOfColors() - 1;
            } else {
                arraySize = this.getNumberOfColors();
            }
        } else if (specifiedGroup == "deletedColors") {
            arraySize = this._colors.length - this.getNumberOfColors();
        } else if (specifiedGroup == "allColors") {
            if (A == null) {
                vertexColor = -1;
                arraySize = this._colors.length;
            } else {
                vertexColor = A.getColor();
                arraySize = this._colors.length - 1;
            }
        } else {
            return null;
        }
        if (arraySize == 0) {
            return null;
        }
        int[] groupOfColors = new int[arraySize];
        for (int i = 0; i < this._colors.length; i++) {
            if (arrayIndex < groupOfColors.length && ((specifiedGroup == "existingColors" && this._colors[i] > 0 && i != vertexColor) || (specifiedGroup == "deletedColors" && this._colors[i] == 0) || (specifiedGroup == "allColors" && i != vertexColor))) {
                groupOfColors[arrayIndex] = i;
                arrayIndex++;
            }
        }
        return groupOfColors;
    }

    public int getLeastUsedColor() {
        int[] existingColors = this.getGroupOfColors("existingColors", null);
        if (existingColors == null) {
            return -1;
        }
        int leastUsedColor = this._colors[existingColors[0]];
        for (int i = 0; i < existingColors.length; i++) {
            if (this._colors[existingColors[i]] < leastUsedColor && this._colors[existingColors[i]] > 0) {
                leastUsedColor = i;
            }
        }
        return leastUsedColor;
    }

    public int getMostUsedColor() {
        int[] existingColors = this.getGroupOfColors("existingColors", null);
        if (existingColors == null) {
            return -1;
        }
        int mostUsedColor = this._colors[existingColors[0]];
        for (int i = 0; i < existingColors.length; i++) {
            if (this._colors[existingColors[i]] > mostUsedColor && this._colors[existingColors[i]] > 0) {
                mostUsedColor = i;
            }
        }
        return mostUsedColor;
    }

    public double getEnergy() {
        //return 100.0 * (double) this.getNumberOfColors() + 99.0 * ((double) this.getLeastUsedColor() / (double) this.getMostUsedColor());
        return this.getNumberOfColors();
    }

    public int getNumberOfColors() {
        return this._nbColors;
    }

    public GraphARS getBackUp() {
        return this._backUp;
    }

    /**
     *
     */
    @Override
    public void launchAlgorithm(){
        applySimulatedAnnealingAlgorithm();
    }
}
