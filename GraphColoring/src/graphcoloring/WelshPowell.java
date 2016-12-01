/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcoloring;

import graphcoloring.Graph;
import graphcoloring.Vertex;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Wilians Rodulfo P1610880
 *
 * In this case the vertices are sorted from highest to lowest grade is based on
 * the number of adjacent vertices Dans ce Algot, les sommets sont classés par
 * ordre décroissant de qualité est basé sur le nombre de sommets adjacents
 */
public class WelshPowell extends Graph {

    /**
     * Les sommets du graphe
     */
    private List<Vertex> vertexs = new ArrayList<Vertex>();
    private List<String> messages = new ArrayList<String>();

    private static int _labelGen = 1;

    private int[] _colors;

    private int _nbColors;
    Vertex A;

    //Construteur
    public WelshPowell() {
        _colors = new int[5];
        _nbColors = 0;
    }

    public WelshPowell(int numb) {
        super(numb);
        _colors = new int[numb];
        _nbColors = 0;
    }

    public static WelshPowell toWelshPowell(Graph g) {
        WelshPowell gra = new WelshPowell(g.getlVertices().size());
        for (int i = 0; i < g._lVertices.size(); i++) {
            gra._lVertices.get(i).setColor(g.getlVertices().get(i).getColor());
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
    
    public void clone(WelshPowell g) {
        for (int i = 0; i < g._lVertices.size(); i++) {
            this._lVertices.get(i).setColor(g.getlVertices().get(i).getColor());
            this._lVertices.get(i).setName(g._lVertices.get(i).getName());
            this._lVertices.get(i).getNeighbours().clear();
        }
        for (int i = 0; i < g._lVertices.size(); i++) {
            for (int j = 0; j < g._lVertices.get(i).getNeighbours().size(); j++) {
                int nameNeighbour = g._lVertices.get(i).getNeighbours().get(j).getName();
                this._lVertices.get(i).addNeighbour(this.findVertex(nameNeighbour));
            }
        }
        this._nbColors = 0;
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

    @Override
    public int getNumberOfColors() {
        return _nbColors;
    }

    
    
    private boolean isAdjacentsNonColores(Vertex vertex, int color) {
        List<Vertex> adj = vertex.getNeighbours();
        for (Iterator<Vertex> iter = adj.iterator(); iter.hasNext();) {
            return iter.next().getColor() != color;
        }
        return false;
    }

    public int getNombreChromatique(Vertex A) {
        int nombreChromatique = 0;

        Object[] s = vertexs.toArray();
        /**
         * Tri décroissant des sommets en fonction de leur degré
         */
        for (Vertex NeibourWP : A.getNeighbours()) {

            for (int i = 0; i < s.length - 1; i++) {
                for (int j = i + 1; j < s.length; j++) {
                    Vertex si = (Vertex) s[i];
                    Vertex sj = (Vertex) s[j];
                    if (sj.getDegre() > si.getDegre()) {
                        Object temp = s[i];
                        s[i] = s[j];
                        s[j] = temp;
                    }
                }
            }
            messages.clear();
            for (int i = 0; i < s.length; i++) {
//		Vertex vertex = (Vertex) s[i]; + vertex.getDegre());
//			vertex.setColor(0); /* Initialiser : aucune couleur a tous les */
//			vertex.setRang(i);
            }

            Vertex sommetNonColore = null;
            int i = 0;
            boolean boucler = true;
            while (i < s.length && boucler) {
                /**
                 * recherche d'un sommet non coloré
                 */
                do {
                    sommetNonColore = (Vertex) s[i++];
                } while (i < s.length && sommetNonColore.getColor() != 0);

                /**
                 * attribuant une couleur non encore utilisée, au premier sommet
                 * non encore coloré
                 */
                int newColor = 0;
                if (sommetNonColore != null) {
                    if (sommetNonColore.getColor() == 0) {
                        newColor = ++nombreChromatique;
                        sommetNonColore.setColor(newColor);
                    } else {
                        boucler = false;
                    }
                }

                /**
                 * Attribuer cette même couleur à chaque sommet non encore
                 * coloré et non adjacent à un sommet de cette couleur
                 */
                if (i < s.length && boucler) {
                    for (int j = 0; j < s.length; j++) {
                        Vertex autreSommet = (Vertex) s[j];

                        if (autreSommet != sommetNonColore
                                && autreSommet.getColor() == 0) /* Probleme de toString àfaire */ // && !sommetNonColore.isNeighbour(autreSommet)
                        //  && isAdjacentsNonColores(autreSommet, newColor)) {
                        {
                            autreSommet.setColor(newColor);
                        }
                    }
                }
            }
        }
        return nombreChromatique;

    }

    private void orderLVertices() {
        List temp = new ArrayList();
        Vertex max;
        while ( _lVertices.size() != 0) {
            max = _lVertices.get(0);
            for (Vertex ver : _lVertices) {
                if (max.getDegre() < ver.getDegre() && temp.indexOf(ver) == -1) {
                    max = ver;
                }
            }
            _lVertices.remove(max);
            temp.add(max);
        }
        _lVertices = temp;
    }

    private Vertex nextWithOutColor(){
        for(Vertex ver : _lVertices){
            if (ver.getColor() == -1 ) return ver;
        }
        //System.out.println("Return null");
        return null;
    }
    
    public int algorithmWelshPowel(){
        orderLVertices();
        int verticesWithOutColor = _lVertices.size();
        int numberOfColor = 0;
        Vertex ver;
        List<Vertex> actualColor = new ArrayList();
        while(verticesWithOutColor > 0){
            if((ver = nextWithOutColor()) != null){
                //System.out.println("Ver Not NULL");
                ver.setColor(numberOfColor);
                actualColor.add(ver);
                verticesWithOutColor--;
                for(int i = _lVertices.indexOf(ver); i < _lVertices.size(); i++){
                    if (_lVertices.get(i).getColor() == -1){
                        boolean b = true;
                        for(Vertex v : actualColor){
                            if(_lVertices.get(i).getNeighbours().indexOf(v) != -1) b = false;
                        }
                        if(b){
                            _lVertices.get(i).setColor(numberOfColor);
                            actualColor.add(_lVertices.get(i));
                            verticesWithOutColor--;
                        }                    
                    }
                }
            }
            else{
                //System.out.println("verticesWithOutColor = " + verticesWithOutColor);
                break;
            }
            actualColor.clear();
            numberOfColor++;
        }
        return numberOfColor;
    }
    
    
    @Override
    public int launchAlgorithm(boolean ecriture) {
        _nbColors = algorithmWelshPowel();
        return _nbColors;
    }

    public String[] testAlgorithm(int nbTests, Graph graph){
        long[] computingTimes = new long[nbTests];
        int[] nbColorsObtained = new int[nbTests];
        long startTime;
        long endTime;
        String[] confidenceIntervals = new String[4];
        SummaryStatistics statsComputingTimes = new SummaryStatistics();
        SummaryStatistics statsNbColorsObtained = new SummaryStatistics();
        for(int i = 0; i < nbTests; i++){
            this.clone(WelshPowell.toWelshPowell(graph));
            System.out.println(this);
            startTime = System.currentTimeMillis();
            nbColorsObtained[i] = this.launchAlgorithm(false);
            System.out.println(nbColorsObtained[i]);
                        System.out.println(this);
            endTime = System.currentTimeMillis();
            computingTimes[i] = endTime - startTime; 
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
