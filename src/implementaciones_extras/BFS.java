package implementaciones_extras;

import java.util.Iterator;

import model.data_structures.Graph;
import model.data_structures.Pila;
import model.data_structures.Queue;
import model.data_structures.SeparateChainingHashST;

public class BFS <K extends Comparable<K>, V, A>
{
	private static final int INFINITY = Integer.MAX_VALUE;

	private SeparateChainingHashST<K, Boolean> marked;
	private SeparateChainingHashST<K, K> edgeTo;
	private SeparateChainingHashST<K, Integer> distTo;


	public BFS(Graph<K,V,A> graph, K s)
	{
		marked=new SeparateChainingHashST<>(graph.V());
		
		Iterator<K> iter = graph.iterarVertices();
		while(iter.hasNext())
		{
			marked.put(iter.next(), false);
		}
		
		edgeTo = new SeparateChainingHashST<>(graph.V());
		distTo = new SeparateChainingHashST<>(graph.V());

		bfs(graph,s);
	}

	private void bfs(Graph<K,V,A> graph , K s) {
		Queue<K> queue = new Queue<K>();

		Iterator<K> keys = graph.iterarVertices();

		while(keys.hasNext())
		{
			K k= keys.next();
			distTo.put(k, INFINITY);
		}

		distTo.put(s, 0);
		marked.put(s, true);
		queue.enqueue(s);

		while (!queue.isEmpty()) 
		{
			K v = queue.dequeue();
			for (K w : graph.adj(v)) 
			{
				if (!marked.get(w)) 
				{
					edgeTo.put(w, v);
					distTo.put(w, distTo.get(v)+1);
					marked.put(w, true);
					queue.enqueue(w);
				}
			}
		}
	}

	public boolean hasPathTo(K v) 
	{
		return marked.get(v);
	}

	
	public int distTo(K v) {

		return distTo.get(v);
	}

	public Iterable<K> pathTo(K v) {

		if (hasPathTo(v)) 
		{
			Pila<K> path = new Pila<K>();
			K x;
			for (x = v; distTo.get(x) != 0; x = edgeTo.get(x))
				path.push(x);
			path.push(x);
			return path;
		}

		return null;

	}

}

