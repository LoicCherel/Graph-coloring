package graphcoloring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 *
 * @author p1401687
 */
public class Vertex implements Serializable, Comparator<Vertex> {

    private List<Vertex> _lNeighbour;

    private int _color;
    private int _name;

    //Variables Welsh&Powell
    /**
     * Etiquette relative à ce sommet et visible à l'utilisateur
     */
    private String _label;
    /**
     * Rang qu'occupe ce sommet après le tri par rapport au nombre de sommets
     * adjacents
     */

    private int _rang;

    public Vertex(int name) {
        _lNeighbour = new ArrayList<Vertex>();
        _color = -1;
        _name = name;
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

    /**
     * Est le point d'entrée du graphe ?
     */
    // private boolean source = false;
    /**
     * Est ce sommet selectionné ?
     */
    private boolean actif = false;

    /**
     * @description : permet de savoir le degré du sommet
     * @return
     */
    public int getDegre() {
        return this._lNeighbour.size();
    }

    public int getRang() {
        return _rang;
    }

    public void setRang(int _rang) {
        this._rang = _rang;
    }

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

    public List<Vertex> getNeighbours() {
        return _lNeighbour;
    }

    void addNeighbour(Vertex b) {
        if (!_lNeighbour.contains(b)) {
            this._lNeighbour.add(b);
        }
    }

    boolean isNeighbour(int nameVertex) {
        for (int i = 0; i < this._lNeighbour.size(); i++) {
            if (nameVertex == this._lNeighbour.get(i).getName()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String vertex = this._name + "," + this._color + ",";
        for (int i = 0; i < _lNeighbour.size(); i++) {
            vertex += _lNeighbour.get(i)._name;
            if (i < _lNeighbour.size() - 1) {
                vertex += "|";
            }
        }
        vertex += "\n";
        return vertex;
    }

    @Override
    public int compare(Vertex v1, Vertex v2) {
        return v1._lNeighbour.size() - v2._lNeighbour.size();
    }

}
