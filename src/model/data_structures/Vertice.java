package model.data_structures;

import model.logic.Comparendos;

public class Vertice <K extends Comparable<K>, V>
{
	private K key;
	private V value;
	private Array<K> adj = new Array<K>();
	private boolean hasComparendo = false;
	
	private Array<Comparendos> comp = new Array<Comparendos>();

	private double latitud;
	private double longitud;

	
	public Vertice(K pKey, V pValue)
	{
		key = pKey;
		value = pValue;
	}
	
	public K darKey()
	{
		return key;
	}
	public V darValue()
	{
		return value; 
	}
	
	public boolean hasComparendos() {
		if(comp.isEmpty()) {
			return false;
		}
		else {return true;}	
		}

	public void setValue(V newValue)
	{
		value=newValue;
	}
	
	public int darRadio() 
	{
		return comp.size()+10;
	}
	
	public Array<K> darAdj()
	{
		return adj;
	}
	
	public Array<Comparendos> darComparendos()
	{
		return comp;
	}
	
	public void agregarComparendos(Comparendos comparendo)
	{
		comp.append(comparendo);
	}
	
	public void agregarVerticeAdj(K idVertAdj)
	{
		adj.append(idVertAdj);
	}

	public double darLongitud()
	{
		return longitud; 
	}

	public double darLatitud()
	{
		return latitud; 
	}
}