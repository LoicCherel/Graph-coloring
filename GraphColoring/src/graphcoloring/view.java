/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphcoloring;

import java.awt.BorderLayout;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Loïc
 */
public class view extends javax.swing.JFrame {
    
    Graph graph;
    
    JFXPanel javafxPanel;
    WebView webComponent;
    
    GraphARS graphARS;
    WelshPowell welsh;
    
    /**
     * Creates new form view
     */
    public view() {
        initComponents();
        ImageIcon img = new ImageIcon("logo.png");
        this.setIconImage(img.getImage());
        javafxPanel = new JFXPanel();
        jPanelDisplayGraph.setLayout(new BorderLayout());
        jPanelDisplayGraph.add(javafxPanel, BorderLayout.CENTER);
        loadJavaFXScene(new File("blank.html"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser = new javax.swing.JFileChooser();
        buttonGroupChooseAlgo = new javax.swing.ButtonGroup();
        jTextFieldNumberOfVertices = new javax.swing.JTextField();
        jLabelNumberOfColors = new javax.swing.JLabel();
        jLabelNumberOfColorsChangeable = new javax.swing.JLabel();
        jButtonLaunchAlgorithm = new javax.swing.JButton();
        jButtonGenerateGraph = new javax.swing.JButton();
        jLabelEndOfNumbersOfVertices = new javax.swing.JLabel();
        jButtonDisplayGraph = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jRadioButtonARS = new javax.swing.JRadioButton();
        jRadioButtonWelsh = new javax.swing.JRadioButton();
        jPanelDisplayGraph = new javax.swing.JPanel();
        jButtonDisplayLineChart = new javax.swing.JButton();
        jCheckBoxWrite = new javax.swing.JCheckBox();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemTestAlgorithms = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Project Graph Coloring v1.1");

        jTextFieldNumberOfVertices.setText("5");

        jLabelNumberOfColors.setText("Nombre de couleurs dans le graphe : ");

        jButtonLaunchAlgorithm.setText("Lancer l'algorithme");
        jButtonLaunchAlgorithm.setEnabled(false);
        jButtonLaunchAlgorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLaunchAlgorithmActionPerformed(evt);
            }
        });

        jButtonGenerateGraph.setText("Générer un graphe avec");
        jButtonGenerateGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateGraphActionPerformed(evt);
            }
        });

        jLabelEndOfNumbersOfVertices.setText("sommets.");

        jButtonDisplayGraph.setText("Afficher le Graphe");
        jButtonDisplayGraph.setEnabled(false);
        jButtonDisplayGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplayGraphActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane.setViewportView(jTextArea1);

        buttonGroupChooseAlgo.add(jRadioButtonARS);
        jRadioButtonARS.setText("ARS");

        buttonGroupChooseAlgo.add(jRadioButtonWelsh);
        jRadioButtonWelsh.setText("Welsh");

        jPanelDisplayGraph.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanelDisplayGraphLayout = new javax.swing.GroupLayout(jPanelDisplayGraph);
        jPanelDisplayGraph.setLayout(jPanelDisplayGraphLayout);
        jPanelDisplayGraphLayout.setHorizontalGroup(
            jPanelDisplayGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelDisplayGraphLayout.setVerticalGroup(
            jPanelDisplayGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButtonDisplayLineChart.setText("Afficher l'évolution de l'énergie");
        jButtonDisplayLineChart.setEnabled(false);
        jButtonDisplayLineChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplayLineChartActionPerformed(evt);
            }
        });

        jCheckBoxWrite.setText("Ecriture");

        jMenu1.setText("Fichier");

        jCheckBoxMenuItem2.setSelected(true);
        jCheckBoxMenuItem2.setText("Utiliser un graphe courbé");
        jMenu1.add(jCheckBoxMenuItem2);
        jMenu1.add(jSeparator1);

        jMenuItemTestAlgorithms.setText("Tester les algorithmes");
        jMenuItemTestAlgorithms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTestAlgorithmsActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemTestAlgorithms);
        jMenu1.add(jSeparator2);

        jMenuItem1.setText("Ouvrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Sauvegarder");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Fermer");
        jMenu1.add(jMenuItem3);

        jMenuBar.add(jMenu1);

        jMenu2.setText("?");
        jMenuBar.add(jMenu2);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDisplayGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonGenerateGraph)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNumberOfVertices, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelEndOfNumbersOfVertices)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                        .addComponent(jLabelNumberOfColors)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelNumberOfColorsChangeable, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(106, 106, 106))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonLaunchAlgorithm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxWrite)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonARS)
                            .addComponent(jRadioButtonWelsh))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDisplayLineChart)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDisplayGraph)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelNumberOfColorsChangeable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelNumberOfColors, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGenerateGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabelEndOfNumbersOfVertices, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldNumberOfVertices))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                    .addComponent(jPanelDisplayGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonLaunchAlgorithm)
                        .addComponent(jButtonDisplayGraph)
                        .addComponent(jButtonDisplayLineChart)
                        .addComponent(jCheckBoxWrite))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButtonARS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonWelsh)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGenerateGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateGraphActionPerformed
            graph = new Graph((Integer.parseInt(jTextFieldNumberOfVertices.getText())));
            unlockButtons();
            graph.toJSON();
            update(graph);
    }//GEN-LAST:event_jButtonGenerateGraphActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        
        int returnVal = jFileChooser.showOpenDialog(this);
        if (returnVal == jFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            System.out.println(file.getName());
            graph = new Graph();
            graph.charger(file.getName());
            unlockButtons();
            update(graph);
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButtonDisplayGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayGraphActionPerformed
        File htmlFile;
        if (jCheckBoxMenuItem2.getState())
            htmlFile = new File("index-curved.html");
        else 
            htmlFile = new File("index.html");
        try {  
            //Desktop.getDesktop().browse(htmlFile.toURI());
            loadJavaFXScene(htmlFile);
        } catch (Exception ex) {
            Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDisplayGraphActionPerformed

    private void jButtonLaunchAlgorithmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLaunchAlgorithmActionPerformed
        if(buttonGroupChooseAlgo.isSelected(jRadioButtonARS.getModel())){
            graphARS = GraphARS.toGraphARS(graph);
            graphARS.launchAlgorithm(jCheckBoxWrite.isSelected());
            update(graphARS);
            if (jCheckBoxWrite.isSelected())jButtonDisplayLineChart.setEnabled(true);
        }else if (buttonGroupChooseAlgo.isSelected(jRadioButtonWelsh.getModel())){
            welsh = WelshPowell.toWelshPowell(graph); 
            welsh.launchAlgorithm(jCheckBoxWrite.isSelected());
            update(welsh);
            jButtonDisplayLineChart.setEnabled(false);
        }else{
            System.out.println("Pas d'algorithme sélectionné");
        }
                
    }//GEN-LAST:event_jButtonLaunchAlgorithmActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if(graph == null){
            JOptionPane.showMessageDialog(null, "Aucun graphe ne peut être enregistré");
        }
        else{
            Object result = JOptionPane.showInputDialog(this, "Entrer le nom du fichier :");
            graph.sauvegarder(result.toString() + ".ser");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItemTestAlgorithmsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTestAlgorithmsActionPerformed
        TestAlgorithmsView tav = new TestAlgorithmsView();
        tav.setVisible(true);
    }//GEN-LAST:event_jMenuItemTestAlgorithmsActionPerformed

    private void jButtonDisplayLineChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayLineChartActionPerformed
        
        File f = new File("data.js");
        if(f.exists() && !f.isDirectory()){
            try {
                File htmlFile;
                htmlFile = new File("courb.html");
                loadJavaFXScene(htmlFile);
            } catch (Exception ex) {
                Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonDisplayLineChartActionPerformed

    private void update(Graph graph){
        jTextArea1.setText(graph.toString());
        
        jLabelNumberOfColorsChangeable.setText(Integer.toString(graph.getNumberOfColors()));
        File htmlFile;
        graph.toJSON();
        if (jCheckBoxMenuItem2.getState())
            htmlFile = new File("index-curved.html");
        else 
            htmlFile = new File("index.html");
        try {  
            //Desktop.getDesktop().browse(htmlFile.toURI());
            loadJavaFXScene(htmlFile);
        } catch (Exception ex) {
            Logger.getLogger(view.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void unlockButtons(){
        jButtonLaunchAlgorithm.setEnabled(true);
        jButtonDisplayGraph.setEnabled(true);
    }
    
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
            java.util.logging.Logger.getLogger(view.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(view.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(view.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(view.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new view().setVisible(true);
            }
        });
    }
    
    private void loadJavaFXScene(File myFile){
        Platform.runLater(new Runnable() {
        @Override
        public void run() {

            BorderPane borderPane = new BorderPane();
            webComponent = new WebView();

            webComponent.getEngine().load("file:///"+  myFile.getAbsolutePath());

            borderPane.setCenter(webComponent);
            Scene scene = new Scene(borderPane,450,450);
            javafxPanel.setScene(scene);

      }
    });
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupChooseAlgo;
    private javax.swing.JButton jButtonDisplayGraph;
    private javax.swing.JButton jButtonDisplayLineChart;
    private javax.swing.JButton jButtonGenerateGraph;
    private javax.swing.JButton jButtonLaunchAlgorithm;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JCheckBox jCheckBoxWrite;
    private javax.swing.JFileChooser jFileChooser;
    private javax.swing.JLabel jLabelEndOfNumbersOfVertices;
    private javax.swing.JLabel jLabelNumberOfColors;
    private javax.swing.JLabel jLabelNumberOfColorsChangeable;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItemTestAlgorithms;
    private javax.swing.JPanel jPanelDisplayGraph;
    private javax.swing.JRadioButton jRadioButtonARS;
    private javax.swing.JRadioButton jRadioButtonWelsh;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextFieldNumberOfVertices;
    // End of variables declaration//GEN-END:variables
}
