package graphcoloring;

import java.io.IOException;
import static java.lang.Math.exp;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe de graphes compatible avec l'utilisation de l'algorithme du recuit simulé (ARS).
 * @author: Loïc Cherel, Thomas Raynaud
 */
public class GraphARS extends Graph{
    
    private int[] _colors;
    //Tableau contenant toutes les couleurs du graphe en indice, et leur
    //occurence dans la case du tableau à l'indice de la couleur.
    private double[] _energy;
    //Tableau contenant la progression des valeurs de l'énergie durant le déroulement
    //de l'ARS.
    private GraphARS _backUp;
    private GraphARS _graphWithMinNbColors;
    //Copies profondes du graphe, utilisées pour retourner à une configuration précédente
    //de la coloration du graphe.
    
    ////PARAMETRES DE L'ALGORITHME////
    private int _nbTimesBeforeUsingMinNumberColorsGraph;
    //Variable fixant le nombre maximum de fois que l'on va modifier la configuration
    //des couleurs du graphe avant de retourner à un graphe avec le plus petit nombre
    //de couleurs trouvé jusqu'alors.
    private int _nbOfNeighboursToChangeMax;
    //Pour chaque modification de la couleur d'un sommet, ce paramètre est le nombre
    //de fois maximum que l'ARS va chercher à adapter les voisins de ce sommet avant
    //d'abandonner le changement.
    
    ////COMPTEURS////
    private int _nbTimesBeforeUsingMinNumberColorsGraphCounter;
    //Compteur associé au paramètre _nbTimesBeforeUsingMinNumberColorsGraph.
    private int _nbOfNeighboursToChangeCounter;
    //Compteur associé au paramètre _nbOfNeighboursToChangeMax.
    

    /**
     * Constructeur par défaut de la classe.
     */
    public GraphARS() {
        super();
        _colors = new int[5];
        _nbColors = 0;
    }

    /**
     * Constructeur de la classe spécifiant le nombre de sommets du graphe. Le graphe
     * sera colorié avant de pouvoir y travailler dessus.
     * @param numberOfVertices Nombre de sommets que contient le graphe.
     */
    public GraphARS(int numberOfVertices) {
        super(numberOfVertices);
        _colors = new int[numberOfVertices];
        _nbColors = 0;
        this.colorGraph();
    }

    /**
     * Conversion d'un graphe de type générique en un graphe compatible avec l'ARS.
     * @param g Instance de la classe Graph.
     * @return Un graphe ayant les mêmes sommets et arêtes que g et instance de la
     *  classe GraphARS.
     */
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
    
    /**
     * Méthode permettant de colorier le graphe. Chaque sommet sera colorié avec
     * une couleur unique
     */
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
     * Exécuter l'ARS sur le graphe 
     * @param ecriture Booléen indiquant si l'ARS doit sauvegarder l'évolution des
     *  valeurs de l'énergie ou non.
     * @return Le nombre de couleurs du graphe.
     */
    @Override
    public int launchAlgorithm(boolean ecriture){
        long startTime = System.nanoTime();
        this.applySimulatedAnnealingAlgorithm(ecriture, 1000 * this._lVertices.size(), 7, 8 * this._lVertices.size());
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
    
    /**
     * Appliquer l'algorithme du recuit simulé sur le graphe. On itère sur cet
     * algorithme jusqu'à ce que la température du "système" soit égale à zéro.
     * @param ecriture Booléen indiquant si l'ARS doit sauvegarder l'évolution des
     *  valeurs de l'énergie ou non.
     * @param temperatureMax La température initiale, paramètre donnant le nombre
     *  d'itérations à effectuer sur l'ARS.
     * @param nbTimesBeforeUsingMinNumberColorsGraph Paramètre informant du nombre
     *  de fois que l'on va essayer de faire des changements sur le graphe. Si après
     *  avoir atteint ce nombre, on n'obtient pas un graphe avec moins de couleurs
     *  que le graphe où on a atteint le minimum de couleurs, nous allons reprendre
     *  comme base de travail ce graphe sauvegardé (_graphWithMinNbColors).
     * @param nbOfNeighboursToChangeMax Paramètre indiquant le nombre de fois que
     * l'on va chercher à modifier la couleur des voisins d'un sommet pour obtenir
     * un graphe coloré correct avant d'abandonner le changement
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
        
        /*Chaque passage dans cette boucle va faire cette série d'instructions:
            - On prend un sommet et une couleur au hasard.
            - On change la couleur du sommet avec la nouvelle couleur choisie.
            - Si ce changement a fait varier l'énergie vers le haut, on garde ce
              changement avec une certaine probabilité.
            - Sinon, on annule le changement en récupérant une copie du graphe qui
              n'a pas eu ce changement.
         */
        if(ecriture && (temperature % step == 0)) this._energy[temperature / step] = energy;
        while (temperature > 0) {
            oldEnergy = energy;
            A = this.getRandomVertex();
            color = this.getRandomColor("allColors", A);
            if (color == -1) { //Cas où il n'existe pas d'autre couleur existante que celle de A.
                return;
            }
            //On fait une copie du graphe avant les changements appliqués au graphe.
            this.prepareBackUp();
            //On met le compteur pour le changement des voisins à zéro.
            this._nbOfNeighboursToChangeCounter = 0;
            /*Si le changement de couleurs a échoué dû à un trop grand nombre d'appels
            récursifs avec la méthode adaptNeighbours, on charge une sauvegarde du graphe
            avant le changement.*/
            if(this.changeColor(A, color) == -1){
                this.chargeBackUp();
            }
            energy = this.getEnergy();
            //En fonction de la variation de l'énergie, nous allons soit garder
            //ou soit annuler le changement.
            if(this.keepChange(oldEnergy, energy, temperature) == false){
                this.chargeBackUp();
            }
            temperature--;
            //On stocke l'énergie et la température du graphe pour évaluer
            //l'efficacité de l'algorithme, dans le cas où ecriture est vrai.
            if(ecriture && (temperature % step == 0)) this._energy[temperature / step] = energy;
            /*Si nous obtenons un graphe avec le plus petit nombre de couleurs obtenu,
            on le stocke dans this._graphWithMinNbColors.
            Autrement, on va remplacer le graphe par le graphe avec le moins de couleurs
            obtenues en fonction de la valeur du compteur S_nbTimesBeforeUsingMinNumberColorsGraphCounter*/
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
        
        if (this.getNumberOfColors() > this._graphWithMinNbColors.getNumberOfColors()){
            this.clone(this._graphWithMinNbColors);
            this._energy[temperature / step] = this.getEnergy();
        }
        if(ecriture) this.storeVariables(temperatureMax);
    }
    
    /**
     * Méthode qui va choisir de garder le changement de couleurs ou annuler la
     * modification, à partir d'une probabilité dépendant les la variation de l'énergie
     * et de la température.
     * @param oldEnergy Valeur de l'énergie avant le changement.
     * @param newEnergy Valeur de l'énergie après le changement.
     * @param temperature Température du "système".
     * @return vrai si l'on décide de garder le changement de couleurs, faux sinon.
     */
    public boolean keepChange(double oldEnergy, double newEnergy, double temperature){
        Random rn = new Random();
        double energyVariation = oldEnergy - newEnergy;
        //Si l'énergie a augmenté, on rentre dans cette condition.
        if (energyVariation > 0) {
            //Calcul de la probabilité de garder le changement.
            double prob = exp((-1 * energyVariation) / (temperature * 0.1));
            if (rn.nextDouble() > prob) {
                //Changement annulé. On retourne faux.
                return false;
            }
        }
        return true;
    }

    /**
     * Méthode qui va changer la couleur d'un sommet.
     * @param A Le sommet auquel on change la couleur.
     * @param color La couleur donnée au sommet A.
     * @return 1 s'il n'y a eu aucun problème, -1 sinon.
     */
    public int changeColor(Vertex A, int color) {
        //On efface l'ancienne couleur de A.
        this.eraseVertexColor(A);
        //On lui applique la nouvelle couleur et on incrémente l'occurence de la couleur
        //dans _colors. Si l'occurence de la nouvelle couleur était nulle, on incrémente
        //aussi le nombre de couleurs existantes.
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

    /**
     * On va ici adapter les voisins du sommet A : entre chaque sommet et A
     * la couleur doit être différente.
     * @param A Le sommet qui vient de changer de couleur.
     * @return 1 s'il a été possible d'adapter les voisins, -1 sinon.
     */
    public int adaptNeighbours(Vertex A) {
        for (Vertex neighbour : A.getNeighbours()) {
            //Si A a la même couleur qu'un de ses voisins.
            if (A.getColor() == neighbour.getColor()) {
                //On arrête l'appel récursif de adaptNeighbours si on l'a appelé
                //trop de fois.
                this._nbOfNeighboursToChangeCounter++;
                if (this._nbOfNeighboursToChangeCounter++ >= this._nbOfNeighboursToChangeMax) {
                    this._nbOfNeighboursToChangeCounter = 0;
                    return -1;
                }
                int color = this.getRandomColor("existingColors", A);
                //On arrête l'appel récursif pour changer de couleur les voisins
                //si la valeur de retour de changeColor est -1.
                int continueColorChanges = this.changeColor(neighbour, color);
                if (continueColorChanges == -1) {
                    return -1;
                }
            }
        }
        //On arrive ici si tous les voisins ont été adaptés.
        return 1;
    }
    
    /**
     * Effacer la couleur d'un sommet.
     * @param A Le sommet qui aura sa couleur effacée.
     */
    private void eraseVertexColor(Vertex a) {
        int deletedColor = a.getColor();
        a.setColor(-1);
        this._colors[deletedColor]--;
        if (this._colors[deletedColor] == 0) {
            this._nbColors--;
        }
    }

    /**
     * Sauvegarde du graphe avec le plus petit nombre de couleurs
     */
    public void setGraphWithMinNbColors(){
        this._graphWithMinNbColors.clone(this);
    }

    /**
     * Sauvegarde du graphe actuel
     */
    public void prepareBackUp() {
        this._backUp.clone(this);
    }

    /**
     * On remplace le graphe actuel par une de ses sauvegardes.
     */
    public void chargeBackUp() {
        this.clone(this.getBackUp());
    }
    
    /**
     * Sauvegarder le graphe dans un fichier.
     * @param nomFic Le nom du fichier contenant le graphe.
     */
    @Override
    public void charger(String nameFile){
        super.charger(nameFile);
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
    
    /**
     * Affecter le graphe en paramètre à celui qui appelle cette méthode
     * @param graph
     */
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
     * Récupérer une couleur au hasard dans un groupe de couleurs spécifique et
     * différente de la couleur de A.
     * @param groupOfColorsDescription groupe de couleur dans lequel on va sélectionner
     *  une couleur au hasard. Il y a trois groupes de couleurs :
     *      - existingColors : couleurs présentes dans le graphe.
     *      - deletedColors : couleurs effacées du graphe.
     *      - allColors : couleur pouvant être dans un des deux groupes ci-dessus.
     * @param A Sommet indiquant la couleur à ne pas sélectionner.
     * @return Une couleur ou -1 si aucune couleur ne peut être sélectionnée.
     */
    private int getRandomColor(String groupOfColorsDescription, Vertex A) {
        //On crée tout d'abord un tableau temporaire contenant que les couleurs
        //souhaitées. Cela évite de chercher plusieurs fois une couleur au
        //hasard dans toutes les couleurs jusqu'à ce que l'on tombe sur une
        //des couleurs souhaitée (toutes les couleurs, celles existantes
        //ou supprimées).
        int[] groupOfColors = this.getGroupOfColors(groupOfColorsDescription, A);
        Random rn = new Random();
        //S'il n'y a pas de couleur supprimée, on arrête la fonction.
        if (groupOfColors == null) {
            return -1;
        }
        //On retourne un élément du tableau au hasard.
        return groupOfColors[rn.nextInt(groupOfColors.length)];
    }

    /**
     * Sélectionner un groupe de couleurs dans le graphe.
     * @param specifiedGroup Spécification du groupe de couleur choisi (allColors,
     *  deletedColors ou allColors).
     * @param A Sommet indiquant la couleur à ne pas sélectionner.
     * @return Un tableau de couleurs correspondant au groupe de couleurs souhaité.
     */
    public int[] getGroupOfColors(String specifiedGroup, Vertex A) {
        int arraySize;
        int arrayIndex = 0;
        int vertexColor = -1;
        switch (specifiedGroup) {
            case "existingColors":
                if (A != null) {
                    vertexColor = A.getColor();
                    arraySize = this.getNumberOfColors() - 1;
                } else {
                    arraySize = this.getNumberOfColors();
                }   break;
            case "deletedColors":
                arraySize = this._colors.length - this.getNumberOfColors();
                break;
            case "allColors":
                if (A == null) {
                    vertexColor = -1;
                    arraySize = this._colors.length;
                } else {
                    vertexColor = A.getColor();
                    arraySize = this._colors.length - 1;
                }   break;
            default:
                return null;
        }
        if (arraySize == 0) {
            return null;
        }
        int[] groupOfColors = new int[arraySize];
        for (int i = 0; i < this._colors.length; i++) {
            if (arrayIndex < groupOfColors.length &&
                    ((specifiedGroup == "existingColors" && this._colors[i] > 0 && i != vertexColor)
                        || (specifiedGroup == "deletedColors" && this._colors[i] == 0)
                        || (specifiedGroup == "allColors" && i != vertexColor))) {
                groupOfColors[arrayIndex] = i;
                arrayIndex++;
            }
        }
        return groupOfColors;
    }
    
    public void storeVariables(int temperatureMax){
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

    /**
     * Méthode renvoyant la couleur la moins présente dans le graphe.
     * @return La couleur la moins utilisée.
     */
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

    /**
     * Méthode renvoyant la couleur la plus présente dans le graphe.
     * @return La couleur la plus utilisée.
     */
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
     * @return La valeur de l'énergie.
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
}