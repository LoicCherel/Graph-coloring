package graphcoloring;

import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author Thomas Raynaud
 */
public class TestAlgorithmsView extends javax.swing.JFrame {
    
    Graph graph;
    //Graphe utilisé pour les tests effectués dans cette fenêtre.

    public TestAlgorithmsView() {
        initComponents();
        ImageIcon img = new ImageIcon("logo.png");
        this.setIconImage(img.getImage());
        this.jLabelError.setText("");
        this.jLabelConfidenceInterval1.setText("");
        this.jLabelConfidenceInterval2.setText("");
        this.jLabelMean1.setText("");
        this.jLabelMean2.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupAlgorithm = new javax.swing.ButtonGroup();
        jFileChooser = new javax.swing.JFileChooser();
        jLabelNbVariables = new javax.swing.JLabel();
        jLabelSelectGraph1 = new javax.swing.JLabel();
        jButtonOpenGraphFile = new javax.swing.JButton();
        jTextFieldNbTests = new javax.swing.JTextField();
        jLabelSelectGraph2 = new javax.swing.JLabel();
        jButtonGenerateGraph = new javax.swing.JButton();
        jLabelSelectGraph3 = new javax.swing.JLabel();
        jTextFieldNbVertices = new javax.swing.JTextField();
        jLabelSelectGraph4 = new javax.swing.JLabel();
        jLabelConfidenceInterval1 = new javax.swing.JLabel();
        jLabelConfidenceInterval2 = new javax.swing.JLabel();
        jButtonFindConfidenceInterval = new javax.swing.JButton();
        jLabelAlgorithmChoice = new javax.swing.JLabel();
        jLabelError = new javax.swing.JLabel();
        jLabelMean1 = new javax.swing.JLabel();
        jLabelMean2 = new javax.swing.JLabel();
        jRadioButtonARS = new javax.swing.JRadioButton();
        jRadioButtonWelsh = new javax.swing.JRadioButton();
        jLabelTemperatureMax = new javax.swing.JLabel();
        jLabelnbTimesBeforeUsingMinNumberColorsGraph = new javax.swing.JLabel();
        jLabelnbOfNeighboursToChangeMax = new javax.swing.JLabel();
        jTextFieldTemperatureMax = new javax.swing.JTextField();
        jTextFieldNbTimesBeforeUsingMinNumberColorsGraph = new javax.swing.JTextField();
        jTextFieldNbOfNeighboursToChangeMax = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tester les algorithmes");

        jLabelNbVariables.setText("Nombre de tests pour les algorithmes (entre 5 et 30 fois) :");

        jLabelSelectGraph1.setText("Sélectionner un graphe à tester :");

        jButtonOpenGraphFile.setText("Ouvrir...");
        jButtonOpenGraphFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenGraphFileActionPerformed(evt);
            }
        });

        jLabelSelectGraph2.setText("ou");

        jButtonGenerateGraph.setText("générer un graphe");
        jButtonGenerateGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateGraphActionPerformed(evt);
            }
        });

        jLabelSelectGraph3.setText("avec");

        jLabelSelectGraph4.setText("sommets");

        jLabelConfidenceInterval1.setText("jLabelIntervalConfidence1");

        jLabelConfidenceInterval2.setText("jLabelIntervalConfidence2");

        jButtonFindConfidenceInterval.setText("Calculer l'intervalle de confiance");
        jButtonFindConfidenceInterval.setEnabled(false);
        jButtonFindConfidenceInterval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFindConfidenceIntervalActionPerformed(evt);
            }
        });

        jLabelAlgorithmChoice.setText("Sélectionner le ou les algorithmes à tester :");

        jLabelError.setForeground(new java.awt.Color(255, 0, 0));
        jLabelError.setText("jLabelError");

        jLabelMean1.setText("jLabelMean1");

        jLabelMean2.setText("jLabelMean2");

        buttonGroupAlgorithm.add(jRadioButtonARS);
        jRadioButtonARS.setText("Algorithme du recuit simulé");

        buttonGroupAlgorithm.add(jRadioButtonWelsh);
        jRadioButtonWelsh.setText("Algorithme de Welsh & Powell");

        jLabelTemperatureMax.setText("Température maximum");

        jLabelnbTimesBeforeUsingMinNumberColorsGraph.setText("Nombre d'itérations avant d'utiliser le graphe avec le moins de couleurs");

        jLabelnbOfNeighboursToChangeMax.setText("Nombre de voisins à changer au maximum");

        jTextFieldTemperatureMax.setText("1000");

        jTextFieldNbTimesBeforeUsingMinNumberColorsGraph.setText("30");

        jTextFieldNbOfNeighboursToChangeMax.setText("30");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelSelectGraph1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOpenGraphFile)
                        .addGap(12, 12, 12)
                        .addComponent(jLabelSelectGraph2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonGenerateGraph)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelSelectGraph3)
                        .addGap(12, 12, 12)
                        .addComponent(jTextFieldNbVertices, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelSelectGraph4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelAlgorithmChoice)
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonARS)
                            .addComponent(jRadioButtonWelsh)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelTemperatureMax)
                                    .addComponent(jLabelnbTimesBeforeUsingMinNumberColorsGraph)
                                    .addComponent(jLabelnbOfNeighboursToChangeMax))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldTemperatureMax, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                    .addComponent(jTextFieldNbOfNeighboursToChangeMax)
                                    .addComponent(jTextFieldNbTimesBeforeUsingMinNumberColorsGraph)))))
                    .addComponent(jLabelMean2)
                    .addComponent(jLabelConfidenceInterval1)
                    .addComponent(jLabelMean1)
                    .addComponent(jLabelError)
                    .addComponent(jLabelConfidenceInterval2)
                    .addComponent(jButtonFindConfidenceInterval)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelNbVariables)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldNbTests, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(297, 297, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNbVariables)
                    .addComponent(jTextFieldNbTests, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSelectGraph1)
                    .addComponent(jButtonOpenGraphFile)
                    .addComponent(jLabelSelectGraph2)
                    .addComponent(jButtonGenerateGraph)
                    .addComponent(jLabelSelectGraph3)
                    .addComponent(jTextFieldNbVertices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSelectGraph4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelAlgorithmChoice)
                    .addComponent(jRadioButtonARS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTemperatureMax)
                    .addComponent(jTextFieldTemperatureMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelnbTimesBeforeUsingMinNumberColorsGraph)
                    .addComponent(jTextFieldNbTimesBeforeUsingMinNumberColorsGraph, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelnbOfNeighboursToChangeMax)
                    .addComponent(jTextFieldNbOfNeighboursToChangeMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButtonWelsh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jLabelError)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonFindConfidenceInterval)
                .addGap(18, 18, 18)
                .addComponent(jLabelConfidenceInterval1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMean1)
                .addGap(10, 10, 10)
                .addComponent(jLabelConfidenceInterval2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMean2)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFindConfidenceIntervalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFindConfidenceIntervalActionPerformed
        int nbOfTests;
        GraphARS graphARS;
        WelshPowell welsh;
        this.jLabelError.setText("");
        if(this.graph != null){
            if(this.jTextFieldNbTests.getText() == ""){
                this.jLabelError.setText("Erreur :  Le nombre de tests n'est pas indiqué");
                return;
            }
            nbOfTests = Integer.parseInt(this.jTextFieldNbTests.getText());
            if(nbOfTests < 5 || nbOfTests > 30){
                this.jLabelError.setText("Erreur :  Le nombre de tests entré est incorrect");
                return;
            }
            String confidenceIntervals[] = new String[4];
            if(buttonGroupAlgorithm.isSelected(jRadioButtonARS.getModel())){
                int argumentsARS[] = new int[3];
                if(this.jTextFieldTemperatureMax.getText() == "" || this.jTextFieldNbTimesBeforeUsingMinNumberColorsGraph.getText() == ""
                        || this.jTextFieldNbOfNeighboursToChangeMax.getText() == ""){
                    this.jLabelError.setText("Erreur :  les paramètres de l'ARS ne sont pas indiqués");
                    return;
                }
                argumentsARS[0] = Integer.parseInt(this.jTextFieldTemperatureMax.getText());
                argumentsARS[1] = Integer.parseInt(this.jTextFieldNbTimesBeforeUsingMinNumberColorsGraph.getText());
                argumentsARS[2] = Integer.parseInt(this.jTextFieldNbOfNeighboursToChangeMax.getText());
                graphARS = GraphARS.toGraphARS(graph);
                confidenceIntervals = graphARS.testAlgorithm(Integer.parseInt(this.jTextFieldNbVertices.getText()), graph, argumentsARS);
            }else if (buttonGroupAlgorithm.isSelected(jRadioButtonWelsh.getModel())){
                welsh = WelshPowell.toWelshPowell(graph);
                confidenceIntervals = graph.testAlgorithm(Integer.parseInt(this.jTextFieldNbVertices.getText()), graph, null);
                //welsh.launchAlgorithm(false);
            }else{
                this.jLabelError.setText("Erreur : Pas d'algorithme sélectionné");
            }
            this.jLabelConfidenceInterval1.setText(confidenceIntervals[0]);
            this.jLabelMean1.setText(confidenceIntervals[1]);
            this.jLabelConfidenceInterval2.setText(confidenceIntervals[2]);
            this.jLabelMean2.setText(confidenceIntervals[3]);
        }
        else
        {
            this.jLabelError.setText("Erreur : aucun graphe sélectionné");
        }
    }//GEN-LAST:event_jButtonFindConfidenceIntervalActionPerformed

    private void jButtonOpenGraphFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenGraphFileActionPerformed
        int returnVal = jFileChooser.showOpenDialog(this);
        if (returnVal == jFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            System.out.println(file.getName());
            graph = new Graph();
            graph.charger(file.getName());
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_jButtonOpenGraphFileActionPerformed

    private void jButtonGenerateGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateGraphActionPerformed
        if(this.jTextFieldNbVertices.getText().equals("")){
            this.jLabelError.setText("Erreur : veuillez entrer le nombre de sommets du graphe");
        }
        else{
            this.jLabelError.setText("");
            this.graph = new Graph(Integer.parseInt(this.jTextFieldNbVertices.getText()));
            this.graph.sauvegarder("testAlgorithms");
            this.jButtonFindConfidenceInterval.setEnabled(true);
        }
    }//GEN-LAST:event_jButtonGenerateGraphActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestAlgorithmsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestAlgorithmsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestAlgorithmsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestAlgorithmsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestAlgorithmsView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupAlgorithm;
    private javax.swing.JButton jButtonFindConfidenceInterval;
    private javax.swing.JButton jButtonGenerateGraph;
    private javax.swing.JButton jButtonOpenGraphFile;
    private javax.swing.JFileChooser jFileChooser;
    private javax.swing.JLabel jLabelAlgorithmChoice;
    private javax.swing.JLabel jLabelConfidenceInterval1;
    private javax.swing.JLabel jLabelConfidenceInterval2;
    private javax.swing.JLabel jLabelError;
    private javax.swing.JLabel jLabelMean1;
    private javax.swing.JLabel jLabelMean2;
    private javax.swing.JLabel jLabelNbVariables;
    private javax.swing.JLabel jLabelSelectGraph1;
    private javax.swing.JLabel jLabelSelectGraph2;
    private javax.swing.JLabel jLabelSelectGraph3;
    private javax.swing.JLabel jLabelSelectGraph4;
    private javax.swing.JLabel jLabelTemperatureMax;
    private javax.swing.JLabel jLabelnbOfNeighboursToChangeMax;
    private javax.swing.JLabel jLabelnbTimesBeforeUsingMinNumberColorsGraph;
    private javax.swing.JRadioButton jRadioButtonARS;
    private javax.swing.JRadioButton jRadioButtonWelsh;
    private javax.swing.JTextField jTextFieldNbOfNeighboursToChangeMax;
    private javax.swing.JTextField jTextFieldNbTests;
    private javax.swing.JTextField jTextFieldNbTimesBeforeUsingMinNumberColorsGraph;
    private javax.swing.JTextField jTextFieldNbVertices;
    private javax.swing.JTextField jTextFieldTemperatureMax;
    // End of variables declaration//GEN-END:variables
}
