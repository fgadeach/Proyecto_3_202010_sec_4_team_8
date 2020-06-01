package implementaciones_extras;

import model.data_structures.Arco;

import model.data_structures.Graph;
import model.data_structures.Minheap;
import model.data_structures.Pila;

public class Dijkstra <K extends Comparable<K>, V, A>
{

	
	private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Arco<Integer,A>[] edgeTo; 
    private Minheap<Double> pq;    // priority queue of vertices

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every
     * other vertex in the edge-weighted graph {@code G}.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public Dijkstra(Graph<Integer,V,A> G, int s) 
    {
        for (Arco<Integer,A> e : G.arcos()) {
            if ((double)e.darInfo() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new Arco[G.V()];


        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new Minheap<Double>(G.V());
        pq.insert(s, distTo[s]);
        
        while (!pq.isEmpty()) 
        {
            int v = pq.delMin();
            
            for (int v2 : G.adj(v))
            {
            		Arco<Integer, A> e= G.getArc(v, v2);
            		relax(e, v, v2);
            }
                
        }
    }

    // relax edge e and update pq if changed
    private void relax(Arco<Integer,A> e, int v, int v2) {
        if (distTo[v2] > distTo[v] + (double)e.darInfo()) 
        {
            distTo[v2] = distTo[v] + (double)e.darInfo();
            edgeTo[v2] = e;
            if (pq.contains(v2)) pq.decreaseKey(v2, distTo[v2]);
            else                pq.insert(v2, distTo[v2]);
        }
    }

    /**
     * Returns the length of a shortest path between the source vertex {@code s} and
     * vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return the length of a shortest path between the source vertex {@code s} and
     *         the vertex {@code v}; {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distTo(int v) 
    {
        return distTo[v];
    }

    /**
     * Returns true if there is a path between the source vertex {@code s} and
     * vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path between the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int v) 
    {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path between the source vertex {@code s} and vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path between the source vertex {@code s} and vertex {@code v};
     *         {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Arco<Integer,A>> pathTo(int v) 
    {
        if (!hasPathTo(v)) return null;
        Pila<Arco<Integer,A>> path = new Pila<Arco<Integer,A>>();
        int x = v;
        for (Arco<Integer,A> e = edgeTo[v]; e != null; e = edgeTo[x]) 
        {
            path.push(e);
            x = e.darOtro(x);
        }
        return path;
    }
	
}




