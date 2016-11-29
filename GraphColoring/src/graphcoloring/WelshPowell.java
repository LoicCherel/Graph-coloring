/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcoloring;

import graphcoloring.Graph;
import graphcoloring.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
        System.out.println("Return null");
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
                break;
            }
            actualColor.clear();
            numberOfColor++;
        }
        return numberOfColor;
    }
    
    
    @Override
    public int launchAlgorithm(boolean ecriture) {
        long startTime = System.nanoTime();
        _nbColors = algorithmWelshPowel();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime); 
        System.out.println("Execution Time : " + (duration/1000000) + " milliseconds");
        if (verifProperties()) {
            System.out.println("Graphe Correct");
        } else {
            System.out.println("Graphe Erroné");
        }
        return _nbColors;
    }

}
