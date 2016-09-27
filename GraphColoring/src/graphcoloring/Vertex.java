package graphcoloring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p1401687
 */
public class Vertex implements Serializable{
    
    private List<Vertex> _lNeighbour;
    private int _color;
    private int _name;

    public Vertex(int name) {
        _lNeighbour = new ArrayList<Vertex>();
        _color = -1;
        _name = name;
    }
    
    public int getName()
    {
        return _name;
    }
    
    public void setName(int name){
        _name = name;
    }

    public int getColor() {
        return _color;
    }

    public void setColor(int color) {
        this._color = color;
    }
    
    public List<Vertex> getNeighbours(){
        return _lNeighbour;
    }

    void addNeighbour(Vertex b) {
        this._lNeighbour.add(b);
    }
    
    boolean isNeighbour(int nameVertex){
        for(int i = 0; i < this._lNeighbour.size(); i++)
        {
            if (nameVertex == this._lNeighbour.get(i).getName())
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        String vertex = this._name + ", color = " + this._color + ", Neighbours = ";
        for (int i = 0; i < _lNeighbour.size(); i++) {
            vertex += "==> " + _lNeighbour.get(i)._name + ", Color = " + _lNeighbour.get(i)._color;
            if (i < _lNeighbour.size() - 1) {
                vertex += " | ";
            }
        }
        vertex += "\n";
        return vertex;
    }
}
