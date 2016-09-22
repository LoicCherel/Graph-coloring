package graphcoloring;

import java.util.List;

/**
 *
 * @author p1401687
 */
public class Vertex {
    
    private List<Vertex> _lNeighbour;
    private int _color;
    private int _name;

    public Vertex() {
        
    }

    public int getColor() {
        return _color;
    }

    public void setColor(int color) {
        this._color = color;
    }

    void addNeighbour(Vertex b) {
        
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
