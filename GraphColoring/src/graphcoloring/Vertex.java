package graphcoloring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Classe de sommets. C'est une agrégation de la classe de graphes : un graphe
 * va contenir une liste de sommets.
 * @author Loïc Cherel, Thomas Raynaud
 */
public class Vertex implements Serializable, Comparator<Vertex> {

    private List<Vertex> _lNeighbour;
    //Liste des voisins du sommet.
    private int _color;
    //Couleur du sommet.
    private int _name;
    //Nom du sommet. Chaque sommet a un nom unique.
    

    /**
     * Constructeur de sommets.
     * @param name Nom attribué au sommet construit.
     */
    public Vertex(int name) {
        _lNeighbour = new ArrayList<>();
        _color = -1;
        _name = name;
    }

    /**
     * Ajouter une arête entre le sommet sur lequel on est et le sommet b. Méthode
     * utilisée lors de la génération du graphe.
     * @param b Le voisin que l'on souhaite relier.
     */
    void addNeighbour(Vertex b) {
        if (!_lNeighbour.contains(b)) {
            this._lNeighbour.add(b);
        }
    }

    /**
     * Méthode qui analyse si deux sommets sont voisins ou non.
     * @param nameVertex Nom du sommet pour lequel on vérifie le lien de voisinnage.
     * @return Vrai si les deux sommets sont voisins, faux sinon.
     */
    boolean isNeighbour(int nameVertex) {
        for (int i = 0; i < this._lNeighbour.size(); i++) {
            if (nameVertex == this._lNeighbour.get(i).getName()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Vérifier que deux sommets sont égaux.
     * @param obj Le sommet à comparer.
     * @return Vrai si les deux sommets sont égaux, faux sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Vertex other = (Vertex) obj;
        if (_name != other.getName()) {
            return false;
        }
        return true;
    }
    
    /**
     * Afficher le sommet avec ses voisins.
     * @return L'affichage su sommet.
     */
    @Override
    public String toString() {
        String vertex = "";
        if (_name < 10){
                vertex += "      " + _name + ",";
            }else if(_name < 100){
                vertex += "    " + _name + ",";
            }else if(_name < 1000){
                vertex += "  " + _name + ",";
            }else if(_name < 10000){
                vertex += _name + ",";
            }
        if (_color < 10){
                vertex += "      " + _color + ",";
            }else if(_color < 100){
                vertex += "    " + _color + ",";
            }else if(_color < 1000){
                vertex += "  " + _color + ",";
            }else if(_color < 10000){
                vertex += _color + ",";
            }
        
        for (int i = 0; i < _lNeighbour.size(); i++) {
            int name = _lNeighbour.get(i)._name;
            if (name < 10){
                vertex += "      " + name;
            }else if(name < 100){
                vertex += "    " + name;
            }else if(name < 1000){
                vertex += "  " + name;
            }else if(name < 10000){
                vertex += name;
            }
            if (i < _lNeighbour.size() - 1) {
                vertex += "|";
            }
        }
        vertex += "\n";
        return vertex;
    }

    /**
     * Comparer deux sommets en fonction du nombre de leurs voisins.
     * @param v1 Le premier sommet à comparer.
     * @param v2 Le deuxième sommet à comparer.
     * @return 0 si les deux sommets on le même nombre de voisins, un nombre négatif
     *  si v1 a moins de voisins que v2, un nombre positif sinon.
     */
    @Override
    public int compare(Vertex v1, Vertex v2) {
        return v1._lNeighbour.size() - v2._lNeighbour.size();
    }
    
    /**
     * Permet de connaître le degré du sommet.
     * @return le degré su sommet.
     */
    public int getDegre() {
        return this._lNeighbour.size();
    }
    
    public List<Vertex> getNeighbours() {
        return _lNeighbour;
    }
    
    public int getName() {
        return _name;
    }

    public void setName(int name) {
        _name = name;
    }

    public int getColor() {
        return _color;
    }

    public void setColor(int color) {
        this._color = color;
    }
}
