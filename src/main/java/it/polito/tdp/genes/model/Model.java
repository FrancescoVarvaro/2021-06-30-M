package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	private List<Integer> migliore;
	private int lunghezzaMax;
	
	public Model() {
		dao = new GenesDao();
	}
	
	public List<Integer> calcolaPercorso(double S){
		List<Integer> parziale = new ArrayList<>();
		this.migliore = new ArrayList<>();
		this.lunghezzaMax = 0;
		
		for(Integer cromosoma : this.grafo.vertexSet()) {
			if(parziale.size()==0) {
				parziale.add(cromosoma);
				ricorsione(parziale, 1, S);
			}
		}
		
		return migliore;
	}
	
	private void ricorsione(List<Integer> parziale, int i, double s) {
		int pesoCammino = CalcolaCammino(parziale);
		if(pesoCammino>lunghezzaMax) {
			lunghezzaMax = pesoCammino;
			migliore = new LinkedList<>(parziale);
		}
		for(Integer cromosoma1 : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(cromosoma1) && valida(parziale, cromosoma1, s)) { // evito di creare dei cicli
				parziale.add(cromosoma1);
				ricorsione(parziale, i+1, s);
				parziale.remove(parziale.size()-1);
				}
			}
	}

	private int CalcolaCammino(List<Integer> parziale) {
		// TODO Auto-generated method stub
		int somma = 0;
		for(DefaultWeightedEdge d : this.grafo.edgeSet()) {
			somma += this.grafo.getEdgeWeight(d);
		}
		return somma;
	}

	private boolean valida(List<Integer> parziale, Integer cromosoma1, double s) {
		// TODO Auto-generated method stub
		if(this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), cromosoma1))>s) {
			return true;
		}
		return false;
	}

	public void creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getCromosomi());
		List<Adiacenza> lista = dao.getArco();
		for(Adiacenza a : lista) {
			Graphs.addEdgeWithVertices(this.grafo, a.getC1(), a.getC2(), a.getPeso());
		}
	}
	
	public String getMaggioriMinoriS(double S) {
		int contMaggiori = 0;
		int contMinori = 0;
		for(DefaultWeightedEdge d : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(d)<S)
				contMinori++;
			if(this.grafo.getEdgeWeight(d)>S)
				contMaggiori++;
		}
		return "Maggiori: "+contMaggiori+" Minori: "+contMinori;
	}
	
	public String getPesoMinMax(){
		double max = 0;
		double min = 10000;
		List <Double> maxMin = new ArrayList<>();
		for(DefaultWeightedEdge d : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(d)>max)
				max =this.grafo.getEdgeWeight(d); 
		}
		
		for(DefaultWeightedEdge d : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(d)<min)
				min =this.grafo.getEdgeWeight(d); 
		}
		maxMin.add(min);
		maxMin.add(max);
		return "peso min: "+min+" peso max: "+max;
	}
	
	public int nVertici(){
		return this.grafo.vertexSet().size();
	}
	
	public int nEdge(){
		return this.grafo.edgeSet().size();
	}
}