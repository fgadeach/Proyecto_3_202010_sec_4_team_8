package model.data_structures;

import java.util.Iterator;

public interface ISeparateChainingHashST <K extends Comparable<K>, V> 
{
	public void put(K K, V V);
	public V get(K K);
	public V delete(K K);
	public Iterator<K> keys();
	
}
