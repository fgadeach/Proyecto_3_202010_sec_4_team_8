package model.data_structures;

import java.util.Iterator;

public class Array <T extends Comparable<T>> implements IArray<T>
{

	private Object[] array;
	private int size;
	public static final int DEFAULT_SIZE = 100;
	
	public Array()
	{
		array =  new Object [DEFAULT_SIZE];
	}

	public boolean isEmpty()
	{
		boolean respuesta = false;
		if (size ==0)
		{
			respuesta = true;
		}
		
		return respuesta;
		
	}
	
	
	public void expandArray()
	{
		Object[] expandido = new Object[array.length + size];

		for(int i =0; i<array.length;i++)
		{
			expandido[i] = array[i];
		}

		array = null;
		array = expandido;
	}


	@Override
	public void append(T item) 
	{
		if (size == array.length)
		{
			expandArray();
		}
		array [size] = item;
		size++;

	}

	@Override
	public void addFirst(T item) {
		// TODO Auto-generated method stub

		if (size == array.length)
		{
			expandArray();
		}
		for (int i=size; i>0; i--) 
		{
			array[i] = array[i-1];
		}
		array[0] = item;
		size++;

	}

	@Override
	public void remove(int pos) {
		// TODO Auto-generated method stub
		if (pos < 0 || pos > size)
			throw new ArrayIndexOutOfBoundsException(pos);
		if (pos == size) 
		{
			array[pos] = null;
		}
		else 
		{
			for (int i=pos; pos< size-1; i++) 
			{
				array[i] = array[i+1];
			}
			array[size-1] = null;
		}
		size--;
	}

	@Override
	public T get(int pos) 
	{
		// TODO Auto-generated method stub

		if (pos<0||pos>size)
		{
			throw new ArrayIndexOutOfBoundsException(pos);
		}

		return (T) array[pos];
	}

	public void set(int pos, T nuevo)
	{
		if (pos<0||pos>size)
		{
			throw new ArrayIndexOutOfBoundsException(pos);
		}

		array[pos] = nuevo ;
	}

	@Override
	public int size() 
	{
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public Iterator<T> iterator() 
	{
		return new Iterator<T>() {
			T act = null;
			int pos = -1;
			
			@Override
			public boolean hasNext() {
				if (size == 0) {
					return false;
				}
				
				if (act == null) {
					return true;
				}
				
				return pos + 1 <size;
			}

			@Override
			public T next() 
			{
				act = (T) array [++pos];
				return act;
			}
		};
	}

}
