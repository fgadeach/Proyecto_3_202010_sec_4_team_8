package model.data_structures;


public class Vertice <K extends Comparable<K>, V>
{
	private K key;
	private V value;
	private Array<K> adj = new Array<K>();

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

	public void setValue(V newValue)
	{
		value=newValue;
	}
	
	public Array<K> darAdj()
	{
		return adj;
	}
	
	public void agregarVerticeAdj(K idVertAdj)
	{
		adj.append(idVertAdj);
	}
	
	public void setLaYLo()
	{
		String lector = (String) value; 
		String linea[] = lector.split(",");
		
		longitud = Double.parseDouble(linea[1]);
		latitud = Double.parseDouble(linea[0]);	
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