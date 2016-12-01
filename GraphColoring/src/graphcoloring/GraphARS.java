package graphcoloring;

import static graphcoloring.Graph.calcMeanCI;
import java.io.IOException;
import static java.lang.Math.exp;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * Classe de graphe compatible avec l'utilisation de l'algorithme du recuit simulé (ARS)
 */
public class GraphARS extends Graph{
    
    private int[] _colors;
    //Tableau contenant toutes les couleurs du graphe en indice, et leur
    //occurence dans la case du tableau à l'indice de la couleur
    private int _nbColors;
    //Nombre de couleurs avec une occurence supérieure à zéro (couleurs existantes)
    private double[] _energy;
    private GraphARS _backUp;
    private GraphARS _graphWithMinNbColors;
    //Copies du graphe
    
    ////PARAMETRES DE L'ALGORITHME////
    private int _nbTimesBeforeUsingMinNumberColorsGraph;
    private int _nbOfNeighboursToChangeMax;
    
    ////ATTRIBUTS STATIQUES ET COMPTEURS
    private int _nbTimesBeforeUsingMinNumberColorsGraphCounter;
    private int _nbOfNeighboursToChangeCounter;

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

    public static GraphARS toGraphARS(Graph g){
        GraphARS gra = new GraphARS(g.getlVertices().size());
        for (int i = 0; i < g._lVertices.size(); i++) {
            gra._lVertices.get(i).setColor(i);
            gra._colors[i] = 1;
            gra._lVertices.get(i).setName(g._lVertices.get(i).getName());
            gra._lVertices.get(i).getNeighbours().clear();
        }
        for (int i = 0; i < g._lVertices.size(); i++) {
            for (int j = 0; j < g._lVertices.get(i).getNeighbours().size(); j++) {
                int nameNeighbour = g._lVertices.get(i).getNeighbours().get(j).getName();
                gra._lVertices.get(i).addNeighbour(gra.findVertex(nameNeighbour));
            }
        }
        return gra;
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
     * @param ecriture boolean
     * @param temperatureMax
     * Paramètre donnant le nombre d'itérations à effectuer sur l'ARS
     * @param nbTimesBeforeUsingMinNumberColorsGraph
     * Paramètre informant du nombre de fois que l'on va essayer de faire des
     * changements sur le graphe. Si après avoir atteint ce nombre, on n'obtient
     * pas un graphe avec moins de couleurs que le graphe où on a atteint le
     * minimum de couleurs, nous allons reprendre comme base de travail ce graphe
     * sauvegardé (_graphWithMinNbColors)
     * @param nbOfNeighboursToChangeMax
     * Paramètre indiquant le nombre de fois que l'on va chercher à modifier
     * la couleur des voisins d'un sommet pour obtenir un graphe coloré correct
     * avant d'abandonner le changement
     */
    public void applySimulatedAnnealingAlgorithm(boolean ecriture, int temperatureMax,
            int nbTimesBeforeUsingMinNumberColorsGraph, int nbOfNeighboursToChangeMax) {
        //Initialisation de la température, de l'énergie et des autres paramètres de l'algorithme
        int temperature = temperatureMax;
        this._nbTimesBeforeUsingMinNumberColorsGraph = nbTimesBeforeUsingMinNumberColorsGraph;
        this._nbOfNeighboursToChangeMax = nbOfNeighboursToChangeMax;
        //Initialisation des compteurs
        this._nbTimesBeforeUsingMinNumberColorsGraphCounter = 0;
        this._nbOfNeighboursToChangeCounter = 0;
        double energy = this.getEnergy();
        double oldEnergy = energy;
        Vertex A;
        int color;
        this._backUp = new GraphARS(this._lVertices.size());
        this._graphWithMinNbColors = new GraphARS(this._lVertices.size());
        this.setGraphWithMinNbColors();
        this.prepareBackUp();
        int step =  temperatureMax / 100;
        this._energy = new double[101];
        
        /*Chaque passage dans cette boucle va faire cette série d'in100structions:
        - on prend un sommet et une couleur au hasard
        - on change la couleur du sommet avec la nouvelle couleur choisie
        - si ce changement a fait varier l'énergie vers le haut, on garde ce
        changement avec une certaine probabilité
        - sinon, on annule le changement en récupérant une copie du graphe qui
        n'a pas eu ce changement.
         */
        //if(ecriture) prepareShowVariables(energy, temperature);
        if(ecriture && (temperature % step == 0)) this._energy[temperature / step] = energy;
        while (temperature > 0) {
            oldEnergy = energy;
            A = this.getRandomVertex();
            color = this.getRandomColor("allColors", A);
            if (color == -1) { //Cas où il n'existe pas d'autre couleur existante que celle de A
                return;
            }
            //On fait une copie du graphe avant les changements
            this.prepareBackUp();
            //On met le compteur pour le changement des voisins à 0
            this._nbOfNeighboursToChangeCounter = 0;
            /*Si le changement de couleur a échoué dû à un trop grand nombre d'appels
            récursifs à adaptNeighbours, on charge une sauvegarde du graphe avant
            le changement*/
            if(this.changeColor(A, color) == -1){
                this.chargeBackUp();
            }
            energy = this.getEnergy();
            //En fonction de la variation de l'énergie, nous allons soit garder
            //ou soit annuler le changement
            if(this.keepChange(oldEnergy, energy, temperature) == false){
                this.chargeBackUp();
            }
            temperature--;
            //On stocke l'énergie et la température du graphe pour évaluer
            //l'efficacité de l'algorithme
            if(ecriture && (temperature % step == 0)) this._energy[temperature / step] = energy;
            /*Si nous obtenons un graphe avec le minimum de couleurs jamais obtenu,
            on le stocke dans this._graphWithMinNbColors.
            Autrement, on va remplacer le graphe par le graphe avec le moins de couleurs
            obtenues en fonction de la valeur du compteur
            _nbTimesBeforeUsingMinNumberColorsGraphCounter*/
            if (this.getNumberOfColors() < this._graphWithMinNbColors.getNumberOfColors()){
                this._graphWithMinNbColors.clone(this);
                this._nbTimesBeforeUsingMinNumberColorsGraphCounter = 0;
            }
            else{
                if(this._nbTimesBeforeUsingMinNumberColorsGraphCounter == this._nbTimesBeforeUsingMinNumberColorsGraph){
                    this.clone(this._graphWithMinNbColors);
                    this._nbTimesBeforeUsingMinNumberColorsGraphCounter = 0;
                }
                else{
                    this._nbTimesBeforeUsingMinNumberColorsGraphCounter++;
                }
            }
        }
        if(ecriture) this.showVariables(temperatureMax);
        if (this.getNumberOfColors() > this._graphWithMinNbColors.getNumberOfColors()){
            this.clone(this._graphWithMinNbColors);
        }
    }
    
    public boolean keepChange(double oldEnergy, double newEnergy, double temperature){
        Random rn = new Random();
        double energyVariation = oldEnergy - newEnergy;
        //Si l'énergie a augmenté, on rentre dans cette condition
        if (energyVariation > 0) {
            //Calcul de la probabilité de garder le changement
            double prob = exp((-1 * energyVariation) / (temperature * 0.1));
            //System.out.println("Energy: " + energy + ", temperature: "
            //        + temperature + ", var(Energy):" + energyVariation + ", prob: " + prob);
            //System.out.println("The energy has increased: prob = " + prob + ", temperature = " + temperature);
            if (rn.nextDouble() > prob) {
                //Changement annulé. On retourne false
                return false;
                
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

        }
        return true;
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
        //On lui applique la nouvelle couleur et on incrémente l'occurence de la couleur
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
 
            return -1;
        }
        return 1;
    }

    public int adaptNeighbours(Vertex A) {
        for (Vertex neighbour : A.getNeighbours()) {
            //Si A a la même couleur qu'un de ses voisins
            if (A.getColor() == neighbour.getColor()) {
                //On arrête l'appel récursif de adaptNeighbours si on l'a appelé
                //trop de fois
                this._nbOfNeighboursToChangeCounter++;
                if (this._nbOfNeighboursToChangeCounter++ >= this._nbOfNeighboursToChangeMax) {
                    this._nbOfNeighboursToChangeCounter = 0;
                    return -1;
                }
                int color = this.getRandomColor("existingColors", A);
                //On arrête l'appel récursif pour changer de couleur les voisins
                //si la valeur de retour de changeColor est -1
                int continueColorChanges = this.changeColor(neighbour, color);
                if (continueColorChanges == -1) {
                    return -1;
                }
            }
        }
        //On arrive ici si tous les voisins ont pu être changés de couleur
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

    public void setGraphWithMinNbColors(){
        this._graphWithMinNbColors.clone(this);
    }

    public void prepareBackUp() {
        this._backUp.clone(this);
    }

    public void chargeBackUp() {
        this.clone(this.getBackUp());
    }
    
    @Override
    public void charger(String nomFic){
        super.charger(nomFic);
        _colors = Arrays.copyOf(this._colors, this._lVertices.size());
        this._nbColors = 0;
        for(Vertex ver : _lVertices){
            this._colors[ver.getColor()] = 1;
        }
        for(int i = 0; i < this._lVertices.size(); i++){
            if(this._colors[i] == 1){
                this._nbColors++;
            }
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
    
    public void clone(GraphARS graph) {
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

    /**
     *
     * @return the value of the energy
     */
    public double getEnergy() {
        //return 100.0 * (double) this.getNumberOfColors() + 99.0 * ((double) this.getLeastUsedColor() / (double) this.getMostUsedColor());
        return this.getNumberOfColors();
    }

    public GraphARS getBackUp() {
        return this._backUp;
    }
    
    @Override
    public int getNumberOfColors() {
        return this._nbColors;
    }

    /**
     * @param ecriture
     * @return 
     *
     */
    @Override
    public int launchAlgorithm(boolean ecriture){
        long startTime = System.nanoTime();
        this.applySimulatedAnnealingAlgorithm(ecriture, 1000 * this._lVertices.size(), 20, 10 * this._lVertices.size());
        long endTime = System.nanoTime();
        long duration = (endTime - startTime); 
        System.out.println("Execution Time : " + (duration/1000000) + " milliseconds");
        if (verifProperties()) {
            System.out.println("Graphe Correct");
        } else {
            System.out.println("Graphe Erroné");
        }
        return this.getNumberOfColors();
    }
    
    public void showVariables(int temperatureMax){
        String outputFile = "data.js";
        int temp;
        String string = "";
        temp = 101;
        try {
            Files.deleteIfExists(Paths.get(outputFile));
        } catch (IOException ex) {
            Logger.getLogger(GraphARS.class.getName()).log(Level.SEVERE, null, ex);
        }
        string += "Morris.Line({\n  element: 'line-example',\n  data: [ \n    { y: '"+ temp + "', a: " + this._energy[this._energy.length - 1] + "}";
        for(int i = this._energy.length - 1; i >= 0; i--){
            temp = this._energy.length - i + 101;
            string += ",\n    { y: '"+ temp + "', a: " + this._energy[i] + "}";
        }
        try {
            string += "  ],\n  xkey: 'y',\n  ykeys: ['a'],\n  labels: ['Energy']\n});";
            Files.write(Paths.get("data.js"), (string).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException ex) {
            Logger.getLogger(GraphARS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String[] testAlgorithm(int nbTests, Graph graph, int[] argumentsARS){
        long[] computingTimes = new long[nbTests];
        int[] nbColorsObtained = new int[nbTests];
        long startTime;
        long endTime;
        String[] confidenceIntervals = new String[4];
        SummaryStatistics statsComputingTimes = new SummaryStatistics();
        SummaryStatistics statsNbColorsObtained = new SummaryStatistics();
        for(int i = 0; i < nbTests; i++){
            this.clone(GraphARS.toGraphARS(graph));
            startTime = System.currentTimeMillis();
            this.applySimulatedAnnealingAlgorithm(false, argumentsARS[0], argumentsARS[1], argumentsARS[2]);
            endTime = System.currentTimeMillis();
            computingTimes[i] = endTime - startTime; 
            nbColorsObtained[i] = this.getNumberOfColors();
        }
        for (long val : computingTimes) {
            statsComputingTimes.addValue(val);
        }
        for (int val : nbColorsObtained) {
            statsNbColorsObtained.addValue(val);
        }

        // Calculer l'intervalle de confiance à 95% pour le temps de calcul
        double ci = calcMeanCI(statsComputingTimes, 0.95);
        BigDecimal mean = new BigDecimal(statsComputingTimes.getMean());
        BigDecimal lower = new BigDecimal(statsComputingTimes.getMean() - ci);
        BigDecimal upper = new BigDecimal(statsComputingTimes.getMean() + ci);
        lower = lower.setScale(3, RoundingMode.HALF_UP);
        upper = upper.setScale(3, RoundingMode.HALF_UP);
        mean = mean.setScale(2, RoundingMode.HALF_UP);
        confidenceIntervals[0] = "L'intervalle de confiance à 95% du temps de calcul est entre " + lower.toString() + " et " + upper.toString() + " millisecondes";
        confidenceIntervals[1] = "Moyenne du temps de calcul : " + mean;
        
        // Calculer l'intervalle de confiance à 95% pour le nombre de couleus
        double ciNbColors = Graph.calcMeanCI(statsNbColorsObtained, 0.95);
        mean = new BigDecimal(statsNbColorsObtained.getMean());
        lower = new BigDecimal(statsNbColorsObtained.getMean() - ciNbColors);
        upper = new BigDecimal(statsNbColorsObtained.getMean() + ciNbColors);
        lower = lower.setScale(0, RoundingMode.DOWN);
        upper = upper.setScale(0, RoundingMode.UP);
        mean = mean.setScale(2, RoundingMode.HALF_UP);
        confidenceIntervals[2] = "L'intervalle de confiance à 95% du nombre de couleurs est entre " + lower.toString() + " et " + upper.toString();
         confidenceIntervals[3] = "Moyenne du nombre de couleurs : " + mean;
        
        return confidenceIntervals;
    }
}