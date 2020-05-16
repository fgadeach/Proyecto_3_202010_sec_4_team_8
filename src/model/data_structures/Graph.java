package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;



public class Graph <K extends Comparable<K>, V, A> implements IGraph<K,V,A>
{
	private int V;
	private int E;
	private SeparateChainingHashST<K, Vertice<K,V>> vertices;
	private SeparateChainingHashST<String, Arco<K,A>> arcos;

	public Graph(int v)
	{
		this.V = v;
		this.E = 0;
		this.vertices = new SeparateChainingHashST<>(10000);
		this.arcos = new SeparateChainingHashST<>(10000);
	}

	@Override
	public int V() 
	{
		return V;
	}

	@Override
	public int E() 
	{
		return E;
	}

	@Override
	public void addVertex(K id, V infoVertex) 
	{
		Vertice<K,V> vertice = new Vertice<>(id, infoVertex);

		vertices.put(id, vertice);
		V++;
	}

	public void addEdge(K idVertIni, K idVertFin, A infoArc) throws Exception 
	{
		if(!vertices.contiene(idVertIni)||!vertices.contiene(idVertFin))
		{
			throw new Exception("No existe alguno de los vertices en el grafo");
		}

		Arco<K, A> nuevoArco = new Arco<K,A>(idVertIni, idVertFin, infoArc);
		String key = idVertIni+"-"+idVertFin;
		String keyInv = idVertFin+"-"+idVertIni;

		if(arcos.contiene(key)||arcos.contiene(keyInv))
		{

		}

		else
		{
			arcos.put(key, nuevoArco);
			E++;
			Vertice<K,V> verticeIni = vertices.get(idVertIni);
			Vertice<K,V> verticeFin = vertices.get(idVertFin);

			verticeIni.agregarVerticeAdj(idVertFin);
			verticeFin.agregarVerticeAdj(idVertIni);
		}
	}
	@Override
	public V getInfoVertex(K idVertex) 
	{	
		Vertice<K,V> vertice = vertices.get(idVertex);

		return vertice.darValue();
	}

	@Override
	public void setInfoVertex(K idVertex, V infoVertex) 
	{
		Vertice<K,V> vertice = vertices.get(idVertex);
		vertice.setValue(infoVertex);
	}

	@Override
	public A getInfoArc(K idVertIni, K idVertFin) 
	{
		A info = null;
		String key = idVertIni+"-"+idVertFin;
		String keyInv = idVertFin+"-"+idVertIni;

		if(arcos.contiene(key))
		{
			Arco<K,A> arc = arcos.get(key);
			info = arc.darInfo();
		}

		else if(arcos.contiene(keyInv))
		{
			Arco<K,A> arc = arcos.get(keyInv);
			info = arc.darInfo();
		}

		return info;
	}

	@Override
	public void setInfoArc(K idVertIni, K idVertFin, A infoArc) 
	{
		String key = idVertIni+"-"+idVertFin;
		Arco<K,A> arc = arcos.get(key);
		arc.setInfo(infoArc);
	}

	@Override
	public Iterable<K> adj(K idVertex) 
	{
		Vertice<K,V> vertice = vertices.get(idVertex);
		return vertice.darAdj();
	}

	@Override
	public Iterator<K> iterarVertices() 
	{
		// TODO Auto-generated method stub
		return vertices.keys();
	}


	public Iterator<String> iterarArcos() 
	{
		return arcos.keys();
	}

	public Iterable<Arco<K,A>> arcos() {
		Queue<Arco<K,A>> lista = new Queue<>();

		Iterator<String> iter = arcos.keys();
		while(iter.hasNext()) 
		{
			Arco<K,A> arco = arcos.get(iter.next());

			lista.enqueue(arco);
		}
		return lista;
	}
}
