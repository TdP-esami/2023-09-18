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
	private List<RetailersExt> cammino;
	private List<Arco> camminoArchi;
	private int bestPesoCammino;
	
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
	
	
	public int getPesoCamminoChiuso() {
		return this.bestPesoCammino;
	}
	
	public List<Arco> calcolaCamminoChiuso(int N){
		this.cammino = new ArrayList<RetailersExt>();
		this.camminoArchi = new ArrayList<Arco>();
		this.bestPesoCammino = 0;
		
		for (RetailersExt R : this.retailersRappresentativi) {
			List<RetailersExt> parziale = new ArrayList<RetailersExt>();
			parziale.add(R);
			doRicorsione2(parziale, N, new ArrayList<Arco>());
		}
		return this.camminoArchi;
	}
	
	private int calcolaPesoCammino(List<Arco> cammino) {
		int res = 0;
		for (Arco a : cammino) {
			res += a.getPeso();
		}
		return res;
	}
	
	private Arco arcoFromRetailers(RetailersExt R1, RetailersExt R2) {
		DefaultWeightedEdge arco = this.grafo.getEdge(R1, R2);
		if (arco==null) {
			return null;
		}else {
			return new Arco(R1, R2, (int)this.grafo.getEdgeWeight(arco));
		}
	}
	
	private void doRicorsione2(List<RetailersExt> parziale, int N, List<Arco> parzialeArchi) {		
		RetailersExt last = parziale.get(parziale.size()-1);
		RetailersExt first = parziale.get(0);
		// caso terminale
		if (parzialeArchi.size()==(N-1)) {
			if(this.grafo.getEdge(last, first)!=null) {
				parzialeArchi.add(arcoFromRetailers(last, first));
				parziale.add(first);
				int pesoCammino = this.calcolaPesoCammino(parzialeArchi);
				if (pesoCammino>this.bestPesoCammino) {
					this.bestPesoCammino = pesoCammino;
					this.cammino = new ArrayList<RetailersExt>(parziale);
					this.camminoArchi = new ArrayList<Arco>(parzialeArchi);
				}
			}
			return;
		}

		// caso normale
		List<RetailersExt> vicini = Graphs.neighborListOf(this.grafo, last);
//		vicini.retainAll(this.retailersRappresentativi);
		vicini.removeAll(parziale);
		for (RetailersExt rext : vicini) {
			//aggiorna parziale
			parzialeArchi.add(arcoFromRetailers(last, rext));
			parziale.add(rext);
				
			//fai un'altro step della ricorsione
			doRicorsione2(parziale, N, parzialeArchi);
				
			//backtracking
			parziale.remove(parziale.size()-1);
			parzialeArchi.remove(parzialeArchi.size()-1);
			
		}
		
	}
	
	
//	public List<RetailersExt> calcolaRappresentativi(){
//		this.retailersRappresentativi = new ArrayList<RetailersExt>();
//		List<RetailersExt> rimanenti = new ArrayList<RetailersExt>();
//		for (RetailersExt rext : this.grafo.vertexSet()) {
//			if (rext.getVolume()>0) {
//				rimanenti.add(rext);
//			}
//		}
//		doRicorsione(new ArrayList<RetailersExt>(), rimanenti, new ArrayList<String>());
//		Collections.sort(this.retailersRappresentativi);
//		return this.retailersRappresentativi;
//	}
//	
//	private void doRicorsione(List<RetailersExt> parziale, List<RetailersExt> rimanenti, List<String> parzialeTipi) {
//		// caso terminale
//		if (rimanenti.isEmpty()) {
//			if (volumeRappresentativi(parziale) > volumeRappresentativi(this.retailersRappresentativi))
//				this.retailersRappresentativi = new ArrayList<RetailersExt>(parziale);
//			return;
//		}
//		
//		// caso normale
//		for (RetailersExt rext : rimanenti) {
//			//aggiorna parziale
//			parziale.add(rext);
////			parzialeTipi.add(rext.getType());
//			//aggiorna rimanenti
//			List<RetailersExt> nuoviRimanenti = new ArrayList<RetailersExt>(rimanenti);
//			nuoviRimanenti.removeAll(Graphs.neighborListOf(this.grafo, rext));
//			nuoviRimanenti.remove(rext);
////			List<RetailersExt> nuoviRimanenti2 =  new ArrayList<RetailersExt>(nuoviRimanenti);
////			for (RetailersExt r : nuoviRimanenti) {
////				if (parzialeTipi.contains(r.getType())) {
////					nuoviRimanenti2.remove(r);
////				}
////			}
//			//fai un'altro step della ricorsione
//			doRicorsione(parziale, nuoviRimanenti, parzialeTipi);
//			//backtracking
//			parziale.remove(parziale.size()-1);
////			parzialeTipi.remove(parzialeTipi.size()-1);
//		}
//		
//	}
	
	
	public int volumeRappresentativi(List<RetailersExt> retailers) {
		int volume = 0;
		for (RetailersExt rext : retailers) {
			volume += rext.getVolume();
		}
		return volume;
	}
	
	public List<RetailersExt> calcolaVolume(){
		this.retailersRappresentativi = new ArrayList<RetailersExt>();
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
		for(RetailersExt R: verticiVolume) {
			if(R.getVolume()>0) {
				retailersRappresentativi.add(R);
			} else {
				break;
			}
		}
		
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
