package model.data_structures;

import java.util.Iterator;

public class Queue <T> implements IQueue<T>{
	//ATRIBUTOS
	private int tamano;
	private Nodo<T> primerNodo;
	private Nodo<T> ultimoNodo;
	
	//CONSTRUCTORES
	public Queue()
	{
		primerNodo = null;
		ultimoNodo = null;
		tamano = 0;
	}
	
	public Queue(T item)
	{
		primerNodo = new Nodo<T>(item);
		ultimoNodo = new Nodo<T>(item);
		tamano =1;
	}
	
	//METODOS
	public Nodo<T> darPrimero()
	{
		return primerNodo;
	}
	
	public Nodo<T> darUltimo()
	{
		return ultimoNodo;
	}
	
	public boolean isEmpty() 
	{
		boolean respuesta = false;	
		if (tamano >= 1)
			respuesta =false;
		else
			respuesta = true;
		return respuesta;
	}

	@Override
	public int tamano() 
	{
		return tamano;
	}
	
	@Override
	public void enqueue (T t) 
	{
		Nodo <T> contenedor = new Nodo <T>(t);
		if (tamano == 0)
		{
			primerNodo = contenedor;
			ultimoNodo = contenedor;
		}
		else
		{
			Nodo <T> ultimo = ultimoNodo;
			ultimo.cambiarSiguiente(contenedor);
			ultimoNodo = contenedor;
		}
		
		tamano++;
	}

	@Override
	public T dequeue() 
	{
		T sacado = null;
		
		if (tamano ==0)
		{
			System.out.println("No hay elementos para quitar de la fila");
		}
		
		else
		{
			Nodo <T> primero = primerNodo;
			sacado = primerNodo.darItem();
			primerNodo = primero.darSiguiente();
			primero.cambiarSiguiente(null);
			tamano --;
		}
		
		return sacado;
	}

	@Override
	public Iterator<T> iterator() 
	{
		return new Iterator<T>() {
			Nodo<T> act = null;

			@Override
			public boolean hasNext() {
				if (tamano == 0) {
					return false;
				}
				
				if (act == null) {
					return true;
				}
				
				return act.darSiguiente() != null; 
			}

			@Override
			public T next() {
				if (act == null) {
					act = primerNodo;
				} else {
					act = act.darSiguiente();
				}
				
				return act.darItem();
			}
		};
	}
}
