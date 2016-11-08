///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
///*package graphcoloring;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//
//
///**
// *
// * @author Wilians Rodulfo P1610880
// * 
// * In this case the vertices are sorted from highest to lowest grade is based on the number of adjacent vertices
// * Dans ce Algot, les sommets sont classés par ordre décroissant de qualité est basé sur le nombre de sommets adjacents
// */
//public class WelshPowell extends Graph{
//    
//
// /** Les sommets du graphe */
//	private List<Vertex> vertexs = new ArrayList<Vertex>();
//	private List<String> messages = new ArrayList<String>();
//	private static int _labelGen = 1;
//
//	public WelshPowell() {
//		super();
//		initialiser();
//	}
//
//	
//	/**
//	 * @description : ajoute le nouveau sommet au graphe
//	 * @param vertex
//	 */
//	public void addVertex(Vertex vertex) {
//		vertexs.add(vertex);
//	}
//
//	public void addVertex() {
//		Vertex vertex = new Vertex();
//		vertex.setLabel("e" + getLabelGen());
//		addVertex(vertex);
//	}
//
//	/**
//	 * @description : supprime le sommet du graphe
//	 * @param sommet
//	 */
//	public void removeVertex(Vertex vertex) {
//		vertexs.remove(vertex);
//	}
//
//	/**
//	 * @description : initialise le graphe
//	 */
//	public void initialiser() {
//		vertexs.clear();
//		_labelGen = 1;
//		Vertex vertex = new Vertex();
//		vertex.setLabel("e" + _labelGen);
//		addVertex(vertex);
//	}
//
//	/**
//	 * @description : permet de générér la prochaine etiquette
//	 * @return
//	 */
//	public static int getLabelGen() {
//		return ++_labelGen;
//	}
//
//	/**
//	 * @description : permet de connaître le nombre de sommets du graphe
//	 * @return
//	 */
//	public int getNombreVertex() {
//		return vertexs.size();
//	}
//
//	/**
//	 * @description : permet de récupérer les sommets du graphe
//	 * @return
//	 */
//	public List<Vertex> getVertexs() {
//		return vertexs;
//	}
//
//        
//        private boolean isAdjacentsNonColores(Vertex vertex, int couleur) {
//		List<Vertex> adj = vertex.getNeighbours();
//		for (Iterator<Vertex> iter = adj.iterator(); iter.hasNext();) {
//		return iter.next().getColor() != couleur;
//	}
//                return false;
//        }
//	public int getNombreChromatique() {
//		int nombreChromatique = 0;
//
//		Object[] s = vertexs.toArray();
//		/** Tri décroissant des sommets en fonction de leur degré */
//		for (int i = 0; i < s.length - 1; i++) {
//			for (int j = i + 1; j < s.length; j++) {
//				Vertex si = (Vertex) s[i];
//				Vertex sj = (Vertex) s[j];
//				if (sj.getDegre() > si.getDegre()) {
//					Object temp = s[i];
//					s[i] = s[j];
//					s[j] = temp;
//				}
//			}
//		}
//		messages.clear();
//		for (int i = 0; i < s.length; i++) {
//			Vertex vertex = (Vertex) s[i];
//			messages.add(vertex.getLabel() + " => degré : "
//					+ vertex.getDegre());
//			vertex.setColor(0); /* Initialiser : aucune couleur a tous les */
//			vertex.setRang(i);
//		}
//
//		Vertex sommetNonColore = null;
//		int i = 0;
//		boolean boucler = true;
//		while (i < s.length && boucler) {
//			/** recherche d'un sommet non coloré */
//			do {
//				sommetNonColore = (Vertex) s[i++];
//			} while (i < s.length && sommetNonColore.getColor() != 0);
//
//			/**
//			 * attribuant une couleur non encore utilisée, au premier sommet non
//			 * encore coloré
//			 */
//			int nouvelleCouleur = 0;
//			if (sommetNonColore != null) {
//				if (sommetNonColore.getColor() == 0) {
//					nouvelleCouleur = ++nombreChromatique;
//					sommetNonColore.setColor(nouvelleCouleur);
//				} else {
//					boucler = false;
//				}
//			}
//
//			/**
//			 * Attribuer cette même couleur à chaque sommet non encore coloré et
//			 * non adjacent à un sommet de cette couleur
//			 */
//			if (i < s.length && boucler) {
//				for (int j = 0; j < s.length; j++) {
//					Vertex autreSommet = (Vertex) s[j];
//					if (autreSommet != sommetNonColore
//							&& autreSommet.getColor() == 0
//							&& !sommetNonColore.isAdjacent(autreSommet)
//							&& isAdjacentsNonColores(autreSommet,
//									nouvelleCouleur)) {
//						autreSommet.setColor(nouvelleCouleur);
//					}
//				}
//			}
//		}
//		return nombreChromatique;
//	}
//
//	public List<String> getMessages() {
//		return messages;
//   }  
//        
//	}
//
//	
//   
// 
//
