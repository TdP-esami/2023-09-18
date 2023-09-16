package it.polito.tdp.gosales.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.gosales.dao.GOsalesDAO;

public class Model {
	
	private GOsalesDAO dao;
	private SimpleWeightedGraph<RetailersExt,DefaultWeightedEdge> grafo;
	private Map<Integer, RetailersExt> idMap;
	private List<RetailersExt> retailersRappresentativi;
	
	public Model() {
		this.dao = new GOsalesDAO();
		idMap = this.dao.getAllRetailers();
	}
	
	
	public List<String> getCountries(){
		return this.dao.getAllCountries();
	}
	
	public List<Integer> getYears(){
		return this.dao.getYears();
	}
	
	
	public List<RetailersExt> calcolaRappresentativi(){
		this.retailersRappresentativi = new ArrayList<RetailersExt>();
		List<RetailersExt> rimanenti = new ArrayList<RetailersExt>();
		for (RetailersExt rext : this.grafo.vertexSet()) {
			if (rext.getVolume()>0) {
				rimanenti.add(rext);
			}
		}
		doRicorsione(new ArrayList<RetailersExt>(), rimanenti);
		Collections.sort(this.retailersRappresentativi);
		return this.retailersRappresentativi;
	}
	
	private void doRicorsione(List<RetailersExt> parziale, List<RetailersExt> rimanenti) {
		// caso terminale
		if (rimanenti.isEmpty()) {
			if (volumeRappresentativi(parziale) > volumeRappresentativi(this.retailersRappresentativi))
				this.retailersRappresentativi = new ArrayList<RetailersExt>(parziale);
		}
		
		// caso normale
		for (RetailersExt rext : rimanenti) {
			//aggiorna parziale
			parziale.add(rext);
			//aggiorna rimanenti
			List<RetailersExt> nuoviRimanenti = new ArrayList<RetailersExt>(rimanenti);
			nuoviRimanenti.removeAll(Graphs.neighborListOf(this.grafo, rext));
			nuoviRimanenti.remove(rext);
			//fai un'altro step della ricorsione
			doRicorsione(parziale, nuoviRimanenti);
			//backtracking
			parziale.remove(parziale.size()-1);
		}
		
	}
	
	
	public int volumeRappresentativi(List<RetailersExt> retailers) {
		int volume = 0;
		for (RetailersExt rext : retailers) {
			volume += rext.getVolume();
		}
		return volume;
	}
	
	public List<RetailersExt> calcolaVolume(){
		for (RetailersExt ret : this.grafo.vertexSet()) {
			int volume = 0;
			Set<DefaultWeightedEdge> incoming = this.grafo.incomingEdgesOf(ret);
			for (DefaultWeightedEdge edge : incoming) {
				volume += this.grafo.getEdgeWeight(edge);
			}
			ret.setVolume(volume);
		}
		List<RetailersExt> verticiVolume =  new ArrayList<RetailersExt>(this.grafo.vertexSet());
		Collections.sort(verticiVolume);
		return verticiVolume;
	}
	
	
	public void creaGrafo(String nazione, int anno) {
		this.grafo = new SimpleWeightedGraph<RetailersExt, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Vertici
		List<RetailersExt> vertici = this.dao.getRetailersCountries(nazione);
		Graphs.addAllVertices(this.grafo, vertici);
		
		//Archi
		List<Arco> archi = this.dao.getArchi(nazione, anno, idMap);
		for(Arco a : archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getRetailer1(), a.getRetailer2(), a.getPeso());

		}
	}
	
	public Set<RetailersExt> getVertici() {
		return this.grafo.vertexSet();
	}
	
	public List<Arco> getArchi() {
		Set<DefaultWeightedEdge> edges = this.grafo.edgeSet();
		List<Arco> archi = new ArrayList<Arco>();
		for (DefaultWeightedEdge e : edges) {
			RetailersExt R1 = this.grafo.getEdgeSource(e);
			RetailersExt R2 = this.grafo.getEdgeTarget(e);
			int peso = (int) this.grafo.getEdgeWeight(e);
			archi.add(new Arco(R1, R2, peso));
		}
		Collections.sort(archi);
		return archi;
	}
	
}
