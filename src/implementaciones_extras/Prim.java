package implementaciones_extras;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.PriorityQueue;

import model.data_structures.Arco;
import model.data_structures.Graph;
import model.data_structures.Minheap;
import model.data_structures.Queue;

public class Prim <K extends Comparable<K>, V, A>
{
	private double weight;                  // total weight of MST
	private LinkedList<Arco> mst;                // Arcos in the MST
	private boolean[] marked;               // marked[v] = true if v on tree
	private PriorityQueue<Arco> pq;         // Arcos with one endpoint in tree

	// Compute a minimum spanning tree (or forest) of an Arco-weighted graph.
	public Prim(Graph G) 
	{
		mst = new LinkedList<>();
		pq = new PriorityQueue<>();
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++)     // run Prim from all vertices to
			if (!marked[v]) prim(G, v);     // get a minimum spanning forest
	}

	// run Prim's algorithm
	private void prim(Graph G, int s) {
		scan(G, s);
		while (!pq.isEmpty()) {                     // better to stop when mst has V-1 Arcos
			Arco e = pq.poll();                     // smallest Arco on pq
			int v = e.either(), w = e.other(v);     // two endpoints
			if (marked[v] && marked[w]) continue;   // lazy, both v and w already scanned
			mst.offer(e);                           // add e to MST
			weight += Double.parseDouble(e.darInfo().toString());
			if (!marked[v]) scan(G, v);             // v becomes part of tree
			if (!marked[w]) scan(G, w);             // w becomes part of tree
		}
	}

	// add all Arcos e incident to v onto pq if the other endpoint has not yet been scanned
	private void scan(Graph G, int v) 
	{
		marked[v] = true;
		for (Object e : G.adj(v))
			if (!marked[((Arco) e).other(v)]) pq.offer((Arco) e);
	}
}

