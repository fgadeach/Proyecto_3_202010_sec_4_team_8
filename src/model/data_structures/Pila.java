package model.data_structures;

import java.util.EmptyStackException;
import java.util.Iterator;

public class Pila <T extends Comparable<T>> implements IPila <T>
{
	private Nodo<T> topStack;
	private int size = 0;
	
	public Pila()
	{
		topStack = null;
		size =0;
	}
	
	public Pila(T item)
	{
		topStack = new Nodo<T>(item);
		size =1;
	}

	@Override
	public Iterator<T> iterator() 
	{
		return new Iterator<T>() {
			Nodo<T> act = null;

			@Override
			public boolean hasNext() 
			{
				if (size == 0) {
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
					act = topStack;
				} else {
					act = act.darSiguiente();
				}
				
				return act.darItem();
			}
		};
	}

	@Override
	public boolean isEmpty() 
	{
		boolean b;
		if (topStack == null)
		{
			b = true;
		}
		
		else
		{
			b = false;
		}
		
		return b;
	}

	@Override
	public int size() 
	{
		return size;
	}

	@Override
	public void push(T t) 
	{
		Nodo<T> nuevo = new Nodo<T>(t);
		if(topStack == null)
		{
			topStack = nuevo;
		}

		else
		{
			nuevo.cambiarSiguiente(topStack);
			topStack = nuevo;
		}
		size++;

	}

	@Override
	public T pop() 
	{
		if(topStack == null)
		{
			throw new EmptyStackException();
		}
		
		T elemento = topStack.darItem();
		Nodo<T> nuevoTopStack = topStack.darSiguiente();
		topStack.cambiarSiguiente(null);
		topStack = nuevoTopStack;
		size--;
		
		return elemento;
	}

}

