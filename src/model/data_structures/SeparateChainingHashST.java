package model.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SeparateChainingHashST <K extends Comparable<K>, V> implements ISeparateChainingHashST<K , V>
{
	private int size; // the number of key-value pairs in the symbol table
	private int m; // the number of size of separate chaining table
	private Node<K, V>[] table; // array of linked-list symbol tables



	@SuppressWarnings("hiding")
	private class Node<K, V> 
	{
		private K key;
		private V value;
		private Node<K,V> next;

		public Node()
		{

		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Node(K key, V value, Node next) 
		{
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SeparateChainingHashST(int capacity) 
	{
		this.m = capacity;
		this.size = 0;
		table = (Node<K, V>[]) new Node[capacity];
		for (int i = 0; i < m; i++) {
			table[i] = (Node<K, V>) new Node();
		}
	}

	public int size() 
	{
		return size;
	}

	public boolean contiene(K key) 
	{
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	} 

	public boolean isEmpty() 
	{
		return size == 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public V get(K key) 
	{
		if (key == null)
			throw new IllegalArgumentException("called get() with key is null.");
		int i = hash(key);
		Node x = table[i];
		while (x != null) 
		{
			if (key.equals(x.key))
				return (V) x.value;
			x = x.next;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void put(K key, V value) 
	{
		if (key == null)
			throw new IllegalArgumentException("called put() with key is null.");

		if (value == null) 
		{
			delete(key);
			return;
		}

		// double table size if average length of list >= 10
		if (size >= 10 * m)
			rehash(2 * m);
		int i = hash(key);
		Node x = table[i];
		Node p = null;
		while (x != null) 
		{
			if (key.equals(x.key)) 
			{
				x.value = value;
				return;
			}

			p = x;
			x = x.next;
		}
		if (p == null) 
		{
			table[i] = new Node(key, value, null);
			size++;
		} 
		else 
		{
			p.next = new Node(key, value, null);
			size++;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public V delete(K key) 
	{
		if (isEmpty())
			throw new NoSuchElementException("called remove() with empty symbol table.");

		if (get(key)== null)
			return null;
		int i = hash(key);

		Node x = table[i];
		Node p = null;
		V oldValue = null;
		while (x != null) 
		{
			if (key.equals(x.key)) {
				oldValue = (V) x.value;
				if (p == null) {
					table[i] = x.next;
				} else {
					p.next = x.next;
				}
				size--;
				break;
			}
			p = x;
			x = x.next;
		}

		if (m >  5 && size <= 2 * m)
			rehash(m / 2);
		return oldValue;
	}

	@SuppressWarnings("unchecked")
	public Iterator<K> keys() 
	{
		Array<K> list = new Array<K>();
		for (int i = 0; i < m; i++) 
		{
			Node<K, V> x = table[i];
			while (x != null) 
			{
				if (x.key != null)
					list.append(x.key);
				x = x.next;
			}
		}
		return list.iterator();
	}

	private int hash(K key) 
	{
		return ((key.hashCode()) & 0x7fffffff) % m;
	}


	private void rehash(int capacity) 
	{
		SeparateChainingHashST<K, V> temp = new SeparateChainingHashST<K, V>(capacity);
		for (int i = 0; i < m; i++) {
			Node<K, V> x = table[i];
			while (x != null) {
				K key = x.key;
				if (key != null)
					temp.put(key, this.get(key));
				x = x.next;
			}
		}
		this.m = temp.m;
		this.size = temp.size;
		this.table = temp.table;
	}

	public Node<K,V> darRaiz(int i)
	{
		return table[i];
	}

}


