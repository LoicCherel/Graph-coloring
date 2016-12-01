package graphcoloring;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Loïc Cherel, Thomas Raynaud, Wilians Rodulfo
 */
public class GraphColoring {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GraphARS graphARS = new GraphARS();
        for(int nbSommets = 10; nbSommets < 50; nbSommets += 5){
            Graph graph = new Graph(nbSommets);
            for(int nbIterationsBeforeUsingGraphMin = 30; nbIterationsBeforeUsingGraphMin > 3; nbIterationsBeforeUsingGraphMin -= 5){
                for(int changementVoisins = 50; changementVoisins > 3; changementVoisins -= 5){
                    for(int temp = 2000; temp < 10000; temp += 200){
                        int[] nbColorsObtained = new int[5];
                        for(int nbTests = 0; nbTests < 5; nbTests++){
                            graphARS = GraphARS.toGraphARS(graph);
                            graphARS.applySimulatedAnnealingAlgorithm(false, temp, nbIterationsBeforeUsingGraphMin, changementVoisins);
                            nbColorsObtained[nbTests] = graphARS.getNumberOfColors();
                            
                        }
                        SummaryStatistics statsNbColorsObtained = new SummaryStatistics();
                        for (int val : nbColorsObtained) {
                            statsNbColorsObtained.addValue(val);
                        }
                        BigDecimal mean = new BigDecimal(statsNbColorsObtained.getMean());
                        mean = mean.setScale(3, RoundingMode.HALF_UP);
                        System.out.println("NBsommets = " + nbSommets + ", I = " + nbIterationsBeforeUsingGraphMin +
                                    ", C = " + changementVoisins + ", Temperature = " + temp + ", NBCOULEURS = " + mean.toString());
                    }
                }
            }
            
        }
    }
}
