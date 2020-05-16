package model.data_structures;

import java.util.Iterator;

public interface IGraph<K,V,A> 
{
	public int V();
	public int E();
	public void addVertex(K id, V infoVertex);
	public void addEdge(K idVertIni , K idVertFin,A infoArc) throws Exception;
	public V getInfoVertex(K idVertex); 
	public void setInfoVertex(K idVertex, V infoVertex);
	public A getInfoArc(K idVertIni, K idVertFin);
	public void setInfoArc(K idVertIni, K idVertFin, A infoArc);
	public Iterable<K> adj(K idVertex);
	public Iterator<K> iterarVertices();
}
