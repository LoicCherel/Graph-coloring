package graphcoloring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author: Loïc Cherel, Thomas Raynaud, Wilians Rodulfo
 */
public class Graph implements Serializable {

    //Liste des sommets du graphe
    List<Vertex> _lVertices;

    public Graph() {
        _lVertices = new ArrayList<Vertex>();
    }

    /**
     *
     * @param numberOfVertices 
     */
    public Graph(int numberOfVertices) {
        Random rn = new Random();
        _lVertices = new ArrayList<Vertex>();
        int a;
        float threshold = ((float)numberOfVertices / (float)(numberOfVertices + 4 ));
        float prop;
        for (int i = 0; i < numberOfVertices; i++) {
            _lVertices.add(new Vertex(i));
        }
        int count = 0;
        for (Vertex ver : _lVertices) {
            count++;
            for (int i = count; i < numberOfVertices; i++) {
                a = rn.nextInt(numberOfVertices + 1);
                prop = (float) a / (float) numberOfVertices;
                //System.out.println(prop);
                if (prop >= threshold) {
                    addEdge(ver, _lVertices.get(i));
                }
            }
        }
    }

    public List<Vertex> getlVertices() {
        return _lVertices;
    }

    public void addEdge(Vertex a, Vertex b) {
        a.addNeighbour(b);
        b.addNeighbour(a);
    }

    public Vertex findVertex(int nameVertex) {
        for (int i = 0; i < this._lVertices.size(); i++) {
            if (this._lVertices.get(i).getName() == nameVertex) {
                return this._lVertices.get(i);
            }
        }
        return null;
    }

    public void charger(String nomFic){
        FileInputStream f = null;
        try {
            File entree = new File(nomFic);
            f = new FileInputStream(entree);
            ObjectInputStream in = new ObjectInputStream(f);
            _lVertices =(List<Vertex>) in.readObject();
            in.close();
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f.close();
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        
    public void sauvegarder(String nomFic){
        if (_lVertices.isEmpty()==false){
            FileOutputStream f = null;
            try {
                File sortie = new File(nomFic);
                f = new FileOutputStream(sortie);
                ObjectOutputStream o = new ObjectOutputStream(f);
                o.writeObject(_lVertices);
                o.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    f.close();
                } catch (IOException ex) {
                    Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     *
     */
    public void displayGraph() {
        for (Vertex ver : _lVertices) {
            System.out.println("{id :" + ver.getName() + "},");
        }

        for (Vertex ver : _lVertices) {
            for (Vertex v : ver.getNeighbours()) {
                if (v.getName() > ver.getName()) {
                    System.out.println("{source : " + ver.getName() + ", target : " + v.getName() + "},");
                }
            }
        }
    }

    @Override
    public String toString() {
        String graph = "";
        for (Vertex ver : _lVertices) {
            graph += ver.toString() + "\n";
        }
        return graph;
    }

    public Vertex getRandomVertex() {
        Random randomVertex = new Random();
        int vertexIndex = randomVertex.nextInt(this._lVertices.size());
        return this._lVertices.get(vertexIndex);
    }

    public void loadText(String myFile) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(myFile));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Vertex ver = new Vertex(Integer.parseInt(fields[0]));
                ver.setColor(Integer.parseInt(fields[0]));
                _lVertices.add(ver);
                String[] neighbours = fields[2].split("|");
                for (String t : neighbours) {
                    if (Integer.parseInt(t) < i) {
                        addEdge(ver, _lVertices.get(Integer.parseInt(t)));
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void toJSON(){
        String outputFile = "output.json";
        String output = "{\n  \"nodes\": [\n";
        boolean first = true;
        for(Vertex ver : _lVertices){
            if(first){
                output += "\n";
                first = false;
            }
            else output += ",\n";
            
            output += ("   {\"id\" :\"" + ver.getName() +"\", \"group\": " + ver.getColor() + "}");
        }
        output += "],\n  \"links\": [";
        first = true;
        for(Vertex ver : _lVertices){
            for(Vertex v : ver.getNeighbours()){
                if(v.getName()>ver.getName()){
                    if(first){
                        output += "\n";
                        first = false;
                    }
                    else output += ",\n";
                    output += "    {\"source\" : " + ver.getName() + ", \"target\" : " + v.getName() +"}";
                }
            }
        }
        output += "  ]\n}";
        try {
            Files.deleteIfExists(Paths.get(outputFile));
            Files.write(Paths.get(outputFile), output.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("ERREUR");
        }
    }
        
    public String[] testAlgorithm(int nbTests, String nameFile){
        long[] computingTimes = new long[nbTests];
        int[] nbColorsObtained = new int[nbTests];
        long startTime;
        long endTime;
        String[] confidenceIntervals = new String[4];
        SummaryStatistics statsComputingTimes = new SummaryStatistics();
        SummaryStatistics statsNbColorsObtained = new SummaryStatistics();
        for(int i = 0; i < nbTests; i++){
            this.charger(nameFile);
            startTime = System.currentTimeMillis();
            this.launchAlgorithm(false);
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
        double ciNbColors = calcMeanCI(statsNbColorsObtained, 0.95);
        mean = new BigDecimal(statsNbColorsObtained.getMean());
        lower = new BigDecimal(statsNbColorsObtained.getMean() - ciNbColors);
        upper = new BigDecimal(statsNbColorsObtained.getMean() + ciNbColors);
        lower = lower.setScale(0, RoundingMode.HALF_UP);
        upper = upper.setScale(0, RoundingMode.HALF_UP);
        mean = mean.setScale(2, RoundingMode.HALF_UP);
        confidenceIntervals[2] = "L'intervalle de confiance à 95% du nombre de couleurs est entre " + lower.toString() + " et " + upper.toString();
         confidenceIntervals[3] = "Moyenne du nombre de couleurs : " + mean;
        
        return confidenceIntervals;
    }

    private static double calcMeanCI(SummaryStatistics stats, double level) {
        try {
            // Create T Distribution with N-1 degrees of freedom
            TDistribution tDist = new TDistribution(stats.getN() - 1);
            // Calculate critical value
            double critVal = tDist.inverseCumulativeProbability(1.0 - (1 - level) / 2);
            // Calculate confidence interval
            return critVal * stats.getStandardDeviation() / Math.sqrt(stats.getN());
        } catch (MathIllegalArgumentException e) {
            return Double.NaN;
        }
    }
    /**
     *
     * @param ecriture
     * @return 
     */
    public int launchAlgorithm(boolean ecriture){
        System.out.println("No algoritm selected");
        return this.getNumberOfColors();
    }
    
    public int getNumberOfColors() {
        return _lVertices.size();
    }
    
    public boolean verifProperties(){
        for (Vertex ver : _lVertices){
            int color = ver.getColor();
            for (Vertex v : ver.getNeighbours()){
                if (color == v.getColor()){
                    return false;
                }
            }
        }
        return true;
    }
}
