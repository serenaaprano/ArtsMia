package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		
		
		dao=new ArtsmiaDAO();
		idMap=new HashMap<Integer, ArtObject>();
		
		
		
	}
	
	public void creaGrafo() {
		
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungere i vertici
		
		//se ci sono dei filtri(ovvero non prendo tutti gli oggetti ma solo di un tipo, il metodo riceve qualcosa)
		//in questo caso no filtri, tutti gli oggetti
		//1. recupero tutti gli artobject dal db
		//2. li inserisco come vertici
		
		//primo metodo
		/*List<ArtObject> vertici = dao.listObjects();
		Graphs.addAllVertices(grafo,vertici);*/
		//secondo metodo(concetto di identity map)
		dao.listObjects(idMap);//gli chiedo di riempire idMap
		Graphs.addAllVertices(grafo,idMap.values());
		
		//aggiungere gli archi
		//APPROCCIO LUNGO, PER DATABASE SNELLI E POCHI VERTICI(FINO A 50 VERTICI VA BENE)
		//Doppio ciclo for sui vertici
		//Dati due vertici, controllo se sono collegati
		/*for(ArtObject a1: this.grafo.vertexSet()) {
			
			for(ArtObject a2: this.grafo.vertexSet()) {
				
				if(!a1.equals(a2) && this.grafo.containsEdge(a1, a2)) {
					
					int peso=dao.getPeso(a1, a2);
					if(peso>0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
					
					
					
				}
				
			}
		}*/
		
		
	/*	System.out.println("grafo creato");
		System.out.println("numero di vertici: "+grafo.vertexSet().size());
		System.out.println("mumero di archi: "+grafo.edgeSet().size());*/
		
		//SECONO APPROCCIO, BLOCCO UN SOLO OGGETTO NELLA QUERY SQL, piu breve dell'altro ma ancora lungo
		
		//TERZO APPROCCIO
		for(Adiacenza a:dao.getAdiacenze()) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, idMap.get(a.getId1()) , idMap.get(a.getId2()), a.getPeso());
			}
		}
		
		System.out.println("grafo creato");
		System.out.println("numero di vertici: "+grafo.vertexSet().size());
		System.out.println("mumero di archi: "+grafo.edgeSet().size());
		
		
		
	}
	
	
	public int getNVertici() {
		
		int vertici=0;
		
		vertici=this.grafo.vertexSet().size();
		
		
		return vertici;
		
	}
	
public int getNArchi() {
		
		int archi=0;
		
		archi=this.grafo.edgeSet().size();
		
		
		return archi;
		
	}
	

}
