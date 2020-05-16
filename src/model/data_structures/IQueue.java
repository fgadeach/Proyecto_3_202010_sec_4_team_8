package model.data_structures;

public interface IQueue <T> extends Iterable<T> {
	public Nodo<T> darPrimero();
	
	public Nodo<T> darUltimo();
	
	public boolean isEmpty();
	
	public int tamano();
	
	public void enqueue(T t);
	
	public T dequeue();
}
