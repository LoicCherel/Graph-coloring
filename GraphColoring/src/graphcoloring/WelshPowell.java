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
    
    public WelshPowell(int numb){
        super(numb);
        _colors = new int[numb];
        _nbColors = 0;
    }

    public static WelshPowell toWelshPowell(Graph g){
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
        gra.colorGraph();
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
                                && autreSommet.getColor() == 0)
                                
                           /* Probleme de toString àfaire */
                               // && !sommetNonColore.isNeighbour(autreSommet)
                              //  && isAdjacentsNonColores(autreSommet, newColor)) {
                            autreSommet.setColor(newColor);
                        }
                    }
                }
            }
            return nombreChromatique;

        }

    @Override
    public int launchAlgorithm(boolean ecriture) {
        return super.launchAlgorithm(ecriture);
    }

    }
