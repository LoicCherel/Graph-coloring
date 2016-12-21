package graphcoloring;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Loïc Cherel, Wilians Rodulfo
 *
 * In this case the vertices are sorted from highest to lowest grade is based on
 * the number of adjacent vertices Dans ce Algot, les sommets sont classés par
 * ordre décroissant de qualité est basé sur le nombre de sommets adjacents
 */
public class WelshPowell extends Graph {

    //Construteur
    public WelshPowell() {
        _nbColors = 0;
    }

    public WelshPowell(int numb) {
        super(numb);
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
            this._lVertices.get(i).setColor(-1);
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

    @Override
    public int getNumberOfColors() {
        return _nbColors;
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
