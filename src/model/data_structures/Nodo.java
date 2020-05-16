package model.data_structures;

public class Nodo <T>{
	
	//ATRIBUTOS
	private Nodo<T> siguiente;
	private Nodo<T> anterior;
	private T objeto;
	
	//CONSTRUCTOR
	public Nodo (T objeto)
	{
		siguiente = null;
		anterior = null;
		this.objeto = objeto;
	}
	
	//METODOS
	public Nodo<T> darSiguiente()
	{
		return siguiente;
	}
	public Nodo<T> darAnterior() 
	{
		return anterior;
	}
	
	public void cambiarSiguiente(Nodo<T> siguiente)
	{
		this.siguiente = siguiente;
	}
	
	public void cambiarAnterior(Nodo<T> anterior)
	{
		this.anterior = anterior;
	}
	
	public T darItem()
	{
		return objeto;
	}
	
	public void cambiarItem(T objeto)
	{
		this.objeto = objeto;
	}
}
