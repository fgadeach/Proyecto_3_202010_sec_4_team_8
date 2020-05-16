package model.data_structures;

public interface IArray<T extends Comparable<T>> extends Iterable<T> 
{
	
	public int size();
	public void append(T item);
	
	public void addFirst(T item);
	
	public void remove(int pos);
	
	public T get(int pos);

}