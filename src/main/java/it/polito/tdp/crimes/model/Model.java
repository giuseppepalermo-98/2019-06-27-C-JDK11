package it.polito.tdp.crimes.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.Adiacenza;
import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private List<String> tipi;
	private Graph <String, DefaultWeightedEdge> grafo;
	private List<Adiacenza> adiacenti;
	private List<Adiacenza> ammissibili;
	
	private List<String> best;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public List<String> getCategorie(){
		return dao.getAllCategory();
	}
	
	public List<LocalDate> getDay(){
		return dao.getAllDay();
	}
	
	public void creaGarfo(String categoria, LocalDate giorno) {
		tipi = dao.getTipo(categoria, giorno);
		
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.tipi);
		
	
		adiacenti = dao.getAdiacenze(categoria, giorno);
		
		for(Adiacenza a: adiacenti) {
			if(grafo.containsVertex(a.getTipo1()) && grafo.containsVertex(a.getTipo2()))
				Graphs.addEdgeWithVertices(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
		}
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
	}
	
	public List<Adiacenza> getAmmissibili(){
		Double mediano=0.0;
		Integer max=-1;
		Integer min=100000;
		
		ammissibili=new ArrayList<>();
		
		for(Adiacenza a: adiacenti) {
			Integer peso = (int) grafo.getEdgeWeight(grafo.getEdge(a.getTipo1(), a.getTipo2()));
			if(peso>max)
				max=peso;
			if(peso<min)
				min=peso;
		}
		
		mediano = (double) (max+min)/2;
		
		for(Adiacenza a: adiacenti) {
			if(grafo.getEdgeWeight(grafo.getEdge(a.getTipo1(), a.getTipo2()))<mediano)
				ammissibili.add(a);
		}
	return ammissibili;	
	}
	
	public List<Adiacenza> getArchi(){
		return this.adiacenti;
	}
	
	public List<String> calcolaPercorso(Adiacenza estremi){
		best = new ArrayList<>();
		List<String> parziale = new ArrayList<>();
		
		parziale.add(estremi.getTipo1());
		
		calcola(parziale, estremi);
		
		return best;
	}

	private void calcola(List<String> parziale, Adiacenza estremi) {
		
		
		List<String> vicini = null;
		vicini = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
		
		if(vicini != null) {
		for(String possibile: vicini) {
			if(!parziale.contains(possibile)) {
				parziale.add(possibile);
				calcola(parziale, estremi);
				parziale.remove(possibile);
				}
			}
		}
		if(calcolaPeso(parziale)>calcolaPeso(best) && parziale.get(parziale.size()-1).equals(estremi.getTipo2()))
			best=new ArrayList<>(parziale);
		
	}

	private int calcolaPeso(List<String> vertici) {
		Integer peso=0;
		
		if(vertici.size()>1) {
		for(int i=1; i<vertici.size(); i++) {
			peso = (int) grafo.getEdgeWeight(grafo.getEdge(vertici.get(i-1), vertici.get(i)));
		}
		}
		return peso;
	}
}
