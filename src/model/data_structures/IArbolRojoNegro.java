package model.data_structures;

public interface IArbolRojoNegro <K extends Comparable<K>, V extends Comparable<V>> extends Iterable<K>
{
	public int size();
	public boolean isEmpty();
	public int altura();
	public boolean contiene(K key);
	public K min();
	public K max();
	public void deleteMin();
	public void deleteMax();
	public void delete(K key);
	public void put(K key, V value);
	
}