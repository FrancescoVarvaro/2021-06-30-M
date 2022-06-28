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
	private double lunghezzaMax;
	
	public Model() {
		dao = new GenesDao();
	}
	
	public List<Integer> calcolaPercorso(double S){
		List<Integer> parziale = new ArrayList<>();
		this.migliore = new ArrayList<>();
		this.lunghezzaMax = 0;
//		List<DefaultWeightedEdge> archi = new ArrayList<>(this.grafo.edgeSet());
		//dalla lista di archi mi prendo un vertice qualsiasi
		for(Integer cromosoma : this.grafo.vertexSet()) {
			parziale.add(cromosoma);
			ricorsione(parziale, 1, S);
			}
		return migliore;
	}
	
	private void ricorsione(List<Integer> parziale, int i, double s) {
		double pesoCammino = CalcolaCammino(parziale);
		if(pesoCammino>lunghezzaMax) {
			lunghezzaMax = pesoCammino;
			migliore = new LinkedList<>(parziale);
		}
		if(Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1)).size()!=0) {
			
			for(Integer cromosoma1 : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
				if(!parziale.contains(cromosoma1) && this.grafo.getEdge(parziale.get(parziale.size()-1), cromosoma1)!=null &&
						this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), cromosoma1)) > s) { 
					parziale.add(cromosoma1);
//					if(valida(parziale, cromosoma1, s))
					ricorsione(parziale, i+1, s);
					parziale.remove(parziale.size()-1);
					}
				}
		}
		
	}

	private double CalcolaCammino(List<Integer> parziale) {
		// TODO Auto-generated method stub
		if(parziale.size()>1) {
			double somma = 0;
			for(int i=0; i<parziale.size()-1;i++) {
				int p = parziale.get(i);//vertice
				int a = parziale.get(i+1);// il successivo
				double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(p, a));
				somma += peso;
			}
			return somma;
		}else 
			return -1;
	}

	private boolean valida(List<Integer> parziale, Integer cromosoma1, double s) {
		// TODO Auto-generated method stub
		if(this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), cromosoma1)) > s) {
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