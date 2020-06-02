package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;

import implementaciones_extras.BFS;
import implementaciones_extras.Prim;



public class Graph <K extends Comparable<K>, V, A> implements IGraph<K,V,A>
{
	public static final int DIRIGIDO = 0;
	public static final int NODIRIGIDO = 1;
	private int tipo;

	private int V;
	private int E;
	public SeparateChainingHashST<K, Vertice<K,V>> vertices;
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

	public int darTipo()
	{
		return tipo;
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
		if(tipo == NODIRIGIDO)
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

		else if(tipo==DIRIGIDO)
		{
			if(!vertices.contiene(idVertIni)||!vertices.contiene(idVertFin))
			{
				throw new Exception("No existe alguno de los vertices en el grafo");
			}

			Arco<K, A> nuevoArco = new Arco<K,A>(idVertIni, idVertFin, infoArc);
			String key = idVertIni+"-"+idVertFin;

			if(arcos.contiene(key))
			{

			}

			else
			{
				arcos.put(key, nuevoArco);
				E++;
				Vertice<K,V> verticeIni = vertices.get(idVertIni);

				verticeIni.agregarVerticeAdj(idVertFin);
			}
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
		if(tipo==NODIRIGIDO)
		{
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
		}

		else if(tipo==DIRIGIDO)
		{
			String key = idVertIni+"-"+idVertFin;

			if(arcos.contiene(key))
			{
				Arco<K,A> arc = arcos.get(key);
				info = arc.darInfo();
			}

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

	public Iterable<Arco<K,A>> arcos() 
	{
		Queue<Arco<K,A>> lista = new Queue<>();

		Iterator<String> iter = arcos.keys();
		while(iter.hasNext()) 
		{
			Arco<K,A> arco = arcos.get(iter.next());

			lista.enqueue(arco);
		}
		return lista;
	}

	public void eliminarVertice(K key) 
	{
		vertices.delete(key);
		V--;
	}

	public boolean existeVertice(K key) 
	{
		if(vertices.get(key)!=null) {
			return true;
		}
		else {return false;
		}
	}

	public Arco<K,A> getArc(K idVertIni, K idVertFin)
	{
		Arco<K,A> arco = null;
		if(tipo==NODIRIGIDO)
		{
			String key = idVertIni+"-"+idVertFin;
			String keyInv = idVertFin+"-"+idVertIni;

			if(arcos.contiene(key))
			{
				arco = arcos.get(key);
			}

			else if(arcos.contiene(keyInv))
			{
				arco = arcos.get(keyInv);

			}
		}

		else if(tipo==DIRIGIDO)
		{
			String key = idVertIni+"-"+idVertFin;

			if(arcos.contiene(key))
			{
				arco = arcos.get(key);
			}
		}

		return arco;
	}

	public Graph<K,V,A> reves() 
	{
		Graph<K,V,A> reverse = new Graph<K,V,A>(0);

		Iterator<String > iter = iterarArcos();
		while(iter.hasNext())
		{
			Arco<K,A> arc = this.arcos.get(iter.next());

			try 
			{
				K v1 = arc.darPrimerVertice();
				K v2 = arc.darSegundoVertice();

				if(!reverse.vertices.contiene(v1))
				{
					reverse.addVertex(v1, this.getInfoVertex(v1));
				}

				if(!reverse.vertices.contiene(v2))
				{
					reverse.addVertex(v2, this.getInfoVertex(v2));
				}

				reverse.addEdge(arc.darSegundoVertice(), arc.darPrimerVertice(), arc.darInfo());
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		System.out.println(reverse.E);
		System.out.println(this.E);
		return reverse;
	}

	public BFS<K,V,A> bfs(K s)
	{
		return new BFS<K, V, A>(this, s);
	}

	public int Grado(K v)
	{
		int grado=0;

		Iterator<String> iter = arcos.keys();
		while(iter.hasNext())
		{
			String key = iter.next();
			if(key.contains(String.valueOf(v)))
			{
				grado++;
			}
		}


		return grado;
	}
	

}
