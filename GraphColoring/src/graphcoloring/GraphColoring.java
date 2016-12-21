package graphcoloring;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Loïc Cherel, Thomas Raynaud, Wilians Rodulfo
 */
public class GraphColoring {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //S = Nombre de sommets
        //I = Nombre d'itérations avant de prendre le graphe avec le moins de couleurs
        //C = Nombre d'appels récursifs pour adapter les voisins
        //T = Température maximum
        
        //POUR I
        String outputFile_I = "dataARS_I.csv";
        String string = "";
        GraphARS graphARS = new GraphARS();
        for(int S = 10; S < 50; S += 5){
            System.out.println(S);
            Graph graph = new Graph(S);
            for(int I = 30; I > 3; I -= 5){
                System.out.println("  " + I);
                double[] tab_C = new double[10];
                int compteur_C = 0;
                for(int C = 50; C > 3; C -= 5){
                    double[] tab_T = new double[20];
                    int compteur_T = 0;
                    for(int T = 2000; T < 10000; T += 400){
                        graphARS = GraphARS.toGraphARS(graph);
                        graphARS.applySimulatedAnnealingAlgorithm(false, T, I, C);
                        tab_T[compteur_T] = graphARS.getNumberOfColors();
                        compteur_T++;
                    }
                    SummaryStatistics statsT = new SummaryStatistics();
                    for (double val : tab_T) {
                        statsT.addValue(val);
                    }
                    BigDecimal meanT = new BigDecimal(statsT.getMean());
                    meanT = meanT.setScale(3, RoundingMode.HALF_UP);
                    tab_C[compteur_C] = meanT.doubleValue();
                    compteur_C++;
                }
                SummaryStatistics statsI = new SummaryStatistics();
                for (double val : tab_C) {
                    statsI.addValue(val);
                }
                BigDecimal meanC = new BigDecimal(statsI.getMean());
                meanC = meanC.setScale(3, RoundingMode.HALF_UP);
                string += S + "; " + I + "; " + meanC + ";\n";
            }
        }
        Files.write(Paths.get(outputFile_I), (string).getBytes(), StandardOpenOption.CREATE);
    }
}
