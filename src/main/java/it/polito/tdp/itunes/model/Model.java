package it.polito.tdp.itunes.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private SimpleDirectedWeightedGraph<Album,DefaultWeightedEdge> grafo;
	private Map<Integer, Album> idMapAlbum;
	private LinkedList<Album> camminoSemplice; //parte 2
	
	public Model() {
		dao =new ItunesDAO();
	}
	
	public void creaGrafo(int n) {
		//pulisco
		grafo = new SimpleDirectedWeightedGraph<Album, DefaultWeightedEdge>(DefaultWeightedEdge.class);	
		idMapAlbum = new HashMap<>();
		
		for(Album a: this.dao.getAllAlbums()) {
			idMapAlbum.put(a.getAlbumId(), a);
		}
		//aggiungo vertici
		Graphs.addAllVertices(grafo, this.dao.getAllAlbumsN(n));
		
		//aggiungo archi
		for(Album a: this.grafo.vertexSet()) {
			Album a1= this.idMapAlbum.get(a.getAlbumId());
			for(Album b: this.grafo.vertexSet()) {
				Album a2= this.idMapAlbum.get(b.getAlbumId());
				if(!a1.equals(a2) && (!this.grafo.containsEdge(a2, a1) || !this.grafo.containsEdge(a1, a2))) {
					int diff = a1.getNumCanzoni() - a2.getNumCanzoni();
					if(diff>0) {
						Graphs.addEdgeWithVertices(grafo, a2, a1, diff);
					}else if(diff<0) {
						Graphs.addEdgeWithVertices(grafo, a1, a2, (-1*diff));
					}
				}
			}
		}
	}

	public SimpleDirectedWeightedGraph<Album, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public void calcoloBilanci() {
		
		List<DefaultWeightedEdge> archiEntranti = new ArrayList<>();
		List<DefaultWeightedEdge> archiUscenti = new ArrayList<>();
		
		
		for(Album a: this.grafo.vertexSet()) {
			Album al = this.idMapAlbum.get(a.getAlbumId());
			int sommaE=0;
			int sommaU=0;
			archiEntranti.addAll(grafo.incomingEdgesOf(al));
			archiUscenti.addAll(grafo.outgoingEdgesOf(al));
			
			for(DefaultWeightedEdge e: archiEntranti)
				sommaE += grafo.getEdgeWeight(e); 
			
			for(DefaultWeightedEdge e: archiUscenti)
				sommaU += grafo.getEdgeWeight(e);
			
			int bilancio = sommaE - sommaU;
			al.setBilancio(bilancio);
			
			archiEntranti.clear();
			archiUscenti.clear();
			
		}
	}
	
	
	public List<Album> getSuccessoriNodoOrdineBilancioDecr(Album a1){
		calcoloBilanci();
		List<Album> albums = Graphs.successorListOf(grafo, a1);
		Collections.sort(albums, new ComparatoreBilancioDecr());
		return albums;
	}
	
	public class ComparatoreBilancioDecr implements Comparator<Album> {
		public int compare(Album a1, Album a2) {
			return - (a1.getBilancio() - a2.getBilancio());
		}
	}
	
	public List<Album> calcolaCammino(int x, Album a1, Album a2){
		camminoSemplice = new LinkedList<>();
		int maxVert= 0; //parte da zero
		
		LinkedList<Album> parziale = new LinkedList<>();
		parziale.add(a1);
		
		cerca(a2, parziale, maxVert);
		
		return camminoSemplice;
		
	}
	
	public void cerca(Album a2, LinkedList<Album> parziale, int max) {
		
		//condizione di terminazione
		if(parziale.getLast().equals(a2)) {
			
			//DALLA SOLUZIONE: controllo se questa soluzione è migliore del best 
			if ( getScore(parziale) > max) {
				max = getScore(parziale);
				this.camminoSemplice = new LinkedList<>(parziale);
			}
			return;
		}
		
		//////
		//DALLA SOLUZIONE
		
		//continuo ad aggiungere elementi in parziale
		List<Album> successors = Graphs.successorListOf(this.grafo, parziale.getLast());
		
		for (Album a : successors) {
			if( this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.getLast(), a)) >= max && !parziale.contains(a)) {
				parziale.add(a);
				cerca(a2, parziale, max);
				parziale.remove(a); //backtracking
			}
		}
	}
	
	///DALLA SOLUZIONE
	private int getScore(List<Album> parziale) {
		int score = 0;
		Album source = parziale.get(0);
		
		for (Album a : parziale.subList(1, parziale.size()-1)) {
			if (this.idMapAlbum.get(a).getBilancio() > this.idMapAlbum.get(source).getBilancio())
				score += 1;
		}
		
		return score;
		
	}

}
