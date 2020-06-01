package model.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArbolRojoNegro <K extends Comparable<K>,V extends Comparable<V>> implements IArbolRojoNegro<K, V>
{
	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private Node raiz;

	public class Node
	{
		K key;
		V value;
		Node right, left;
		int size;
		boolean color;

		Node(K key, V value, int size, boolean color)
		{
			this.key = key;
			this.value = value;
			this.size = size;
			this.color = color;
		}

		public V darValue()
		{
			return value;
		}
	}

	public ArbolRojoNegro() 
	{
	}

	public Node darRaiz()
	{
		return raiz;
	}

	private boolean isRed(Node nodo)
	{
		if(nodo==null)
		{
			return false;
		}

		return nodo.color == RED;
	}

	private int size(Node nodo) 
	{
		if (nodo == null) return 0;
		return nodo.size;
	} 

	public int size() 
	{
		return size(raiz);
	}

	public boolean isEmpty() 
	{
		return raiz == null;
	}

	public int altura() 
	{
		return altura(raiz);
	}

	private int altura(Node nodo) 
	{
		if (nodo == null) return -1;
		return 1 + Math.max(altura(nodo.left), altura(nodo.right));
	}

	public int darAltura(K key)
	{
		Node nodo = getNode(key);

		return altura(nodo);
	}

	public Node getNode(K key) 
	{
		if (key == null) throw new IllegalArgumentException("La llave buscada en nula");
		return getNode(raiz, key);
	}

	private Node getNode(Node nodo, K key) 
	{
		while (nodo != null) 
		{
			int compare = key.compareTo(nodo.key);
			if (compare < 0)
			{
				nodo = nodo.left;
			}
			else if (compare > 0) 
			{
				nodo = nodo.right;
			}
			else  
			{
				return nodo;
			}
		}
		return null;
	}


	public V get(K key) 
	{
		if (key == null) throw new IllegalArgumentException("La llave buscada en nula");
		return get(raiz, key);
	}

	private V get(Node nodo, K key) 
	{
		while (nodo != null)  
		{
			int compare = key.compareTo(nodo.key);
			if (compare < 0)
			{
				nodo = nodo.left;
			}
			else if (compare > 0) 
			{
				nodo = nodo.right;
			}
			else  
			{
				return nodo.value;
			}
		}
		return null;
	}

	public boolean contiene(K key) 
	{
		return get(key) != null;
	}

	private Node rotateRight(Node nodo) 
	{
		Node x = nodo.left;
		nodo.left = x.right;
		x.right = nodo;
		x.color = x.right.color;
		x.right.color = RED;
		x.size = nodo.size;
		nodo.size = size(nodo.left) + size(nodo.right) + 1;
		return x;
	}

	private Node rotateLeft(Node nodo) 
	{
		Node x = nodo.right;
		nodo.right = x.left;
		x.left = nodo;
		x.color = x.left.color;
		x.left.color = RED;
		x.size = nodo.size;
		nodo.size = size(nodo.left) + size(nodo.right) + 1;
		return x;
	}

	private void flipColors(Node nodo) 
	{
		nodo.color = !nodo.color;
		nodo.left.color = !nodo.left.color;
		nodo.right.color = !nodo.right.color;
	}

	private Node moveRedLeft(Node nodo) 
	{
		flipColors(nodo);
		if (isRed(nodo.right.left)) 
		{ 
			nodo.right = rotateRight(nodo.right);
			nodo = rotateLeft(nodo);
			flipColors(nodo);
		}
		return nodo;
	}

	private Node moveRedRight(Node nodo) 
	{
		flipColors(nodo);
		if (isRed(nodo.left.left)) 
		{ 
			nodo = rotateRight(nodo);
			flipColors(nodo);
		}
		return nodo;
	}

	private Node balance(Node nodo) 
	{
		if (isRed(nodo.right))
		{
			nodo = rotateLeft(nodo);
		}

		if (isRed(nodo.left) && isRed(nodo.left.left))
		{
			nodo = rotateRight(nodo);
		}
		if (isRed(nodo.left) && isRed(nodo.right))
		{
			flipColors(nodo);
		}

		nodo.size = size(nodo.left) + size(nodo.right) + 1;
		return nodo;
	}

	public K min() 
	{
		if (isEmpty()) throw new NoSuchElementException("El arbol esta vacio");
		return min(raiz).key;
	} 

	private Node min(Node nodo) 
	{ 
		if (nodo.left == null)
		{
			return nodo; 
		}
		else
		{
			return min(nodo.left); 
		}
	} 

	/**
	 * Returns the largest key in the symbol table.
	 * @return the largest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public K max() {
		if (isEmpty()) throw new NoSuchElementException("El arbol esta vacio");
		return max(raiz).key;
	} 

	private Node max(Node nodo) 
	{ 
		if (nodo.right == null) 
		{
			return nodo; 
		}

		else
		{
			return max(nodo.right); 
		}
	} 

	public void deleteMin() {
		if (isEmpty()) throw new NoSuchElementException("El arbol esta vacio. No se puede eliminar nada");

		if (!isRed(raiz.left) && !isRed(raiz.right))
		{
			raiz.color = RED;
		}

		raiz = deleteMin(raiz);
		if (!isEmpty()) raiz.color = BLACK;
	}

	private Node deleteMin(Node nodo) 
	{ 
		if (nodo.left == null)
		{
			return null;
		}

		if (!isRed(nodo.left) && !isRed(nodo.left.left))
		{
			nodo = moveRedLeft(nodo);
		}

		nodo.left = deleteMin(nodo.left);
		return balance(nodo);
	}


	public void deleteMax() 
	{
		if (isEmpty()) throw new NoSuchElementException("El arbol esta vacio. No se puede eliminar nada");

		if (!isRed(raiz.left) && !isRed(raiz.right))
		{
			raiz.color = RED;
		}
		raiz = deleteMax(raiz);
		if (!isEmpty())
		{
			raiz.color = BLACK;
		}
	}

	private Node deleteMax(Node nodo) 
	{ 
		if (isRed(nodo.left))
		{
			nodo = rotateRight(nodo);
		}

		if (nodo.right == null)
		{
			return null;
		}

		if (!isRed(nodo.right) && !isRed(nodo.right.left))
		{
			nodo = moveRedRight(nodo);
		}

		nodo.right = deleteMax(nodo.right);

		return balance(nodo);
	}

	public void delete(K key) { 
		if (key == null) throw new IllegalArgumentException("La llave ingresada es nula");
		if (!contiene(key)) return;

		if (!isRed(raiz.left) && !isRed(raiz.right))
		{
			raiz.color = RED;
		}

		raiz = delete(raiz, key);
		if (!isEmpty())
		{
			raiz.color = BLACK;
		}
	}

	private Node delete(Node nodo, K key) 
	{ 
		if (key.compareTo(nodo.key) < 0)  
		{
			if (!isRed(nodo.left) && !isRed(nodo.left.left))
			{
				nodo = moveRedLeft(nodo);
			}

			nodo.left = delete(nodo.left, key);
		}

		else 
		{
			if (isRed(nodo.left))
			{
				nodo = rotateRight(nodo);
			}
			if (key.compareTo(nodo.key) == 0 && (nodo.right == null))
			{
				return null;
			}
			if (!isRed(nodo.right) && !isRed(nodo.right.left))
			{
				nodo = moveRedRight(nodo);
			}
			if (key.compareTo(nodo.key) == 0) 
			{
				Node x = min(nodo.right);
				nodo.key = x.key;
				nodo.value = x.value;
				nodo.right = deleteMin(nodo.right);
			}
			else
			{
				nodo.right = delete(nodo.right, key);
			}		
		}
		return balance(nodo);
	}

	private Node put(Node nodo, K key, V val) 
	{ 
		if (nodo == null) return new Node(key, val, 1,RED);

		int compare = key.compareTo(nodo.key);
		if (compare < 0) 
		{
			nodo.left  = put(nodo.left,  key, val); 
		}

		else if (compare > 0)
		{
			nodo.right = put(nodo.right, key, val); 
		}

		else
		{
			nodo.value   = val;
		}

		if (isRed(nodo.right) && !isRed(nodo.left)) 
		{
			nodo = rotateLeft(nodo);
		}
		if (isRed(nodo.left)  &&  isRed(nodo.left.left))
		{
			nodo = rotateRight(nodo);
		}
		if (isRed(nodo.left)  &&  isRed(nodo.right))  
		{
			flipColors(nodo);
		}

		nodo.size = size(nodo.left) + size(nodo.right) + 1;

		return nodo;
	}

	public void put(K key, V val) 
	{
		if (key == null)
		{
			throw new IllegalArgumentException("La llave es nula");
		}
		if (val == null) 
		{
			delete(key);
			return;
		}

		raiz = put(raiz, key, val);
		raiz.color = BLACK;
	}

	public int rank(K key) {
		if (key == null) throw new IllegalArgumentException("argument to rank() is null");
		return rank(key, raiz);
	} 

	private int rank(K key, Node x) 
	{
		if (x == null) return 0; 
		int cmp = key.compareTo(x.key); 
		if      (cmp < 0) return rank(key, x.left); 
		else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right); 
		else              return size(x.left); 
	} 

	public K floor(K key) 
	{
		if (key == null) throw new IllegalArgumentException("argument to floor() is null");
		if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
		Node x = floor(raiz, key);
		if (x == null) return null;
		else           return x.key;
	}    

	private Node floor(Node x, K key) 
	{
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0) return x;
		if (cmp < 0)  return floor(x.left, key);
		Node t = floor(x.right, key);
		if (t != null) return t; 
		else           return x;
	}

	public K ceiling(K key) 
	{
		if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
		if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
		Node x = ceiling(raiz, key);
		if (x == null) return null;
		else           return x.key;  
	}

	private Node ceiling(Node x, K key) 
	{  
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0) return x;
		if (cmp > 0)  return ceiling(x.right, key);
		Node t = ceiling(x.left, key);
		if (t != null) return t; 
		else           return x;
	}

	public K select(int k) 
	{
		if (k < 0 || k >= size()) {
			throw new IllegalArgumentException("argument to select() is invalid: " + k);
		}
		Node x = select(raiz, k);
		return x.key;
	}


	private Node select(Node x, int k) 
	{
		int t = size(x.left); 
		if      (t > k) return select(x.left,  k); 
		else if (t < k) return select(x.right, k-t-1); 
		else            return x; 
	} 

	public boolean check() 
	{
		if (!isBST())            System.out.println("Not in symmetric order");
		if (!isSizeConsistent())  System.out.println("Subtree counts not consistent");
		if (!isRankConsistent()) System.out.println("Ranks not consistent");
		if (!is23())             System.out.println("Not a 2-3 tree");
		if (!isBalanced())       System.out.println("Not balanced");
		return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
	}

	private boolean isBST() 
	{
		return isBST(raiz, null, null);
	}

	private boolean isBST(Node x, K min, K max) 
	{
		if (x == null) return true;
		if (min != null && x.key.compareTo(min) <= 0) return false;
		if (max != null && x.key.compareTo(max) >= 0) return false;
		return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
	} 

	private boolean isSizeConsistent() { return isSizeConsistent(raiz); }

	private boolean isSizeConsistent(Node x) 
	{
		if (x == null) return true;
		if (x.size != size(x.left) + size(x.right) + 1) return false;
		return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	} 

	private boolean isRankConsistent() 
	{
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i))) return false;

		Iterator<K> iter = keys();

		while(iter.hasNext())
		{
			K key = iter.next();
			if (key.compareTo(select(rank(key))) != 0) return false;
		}
		return true;
	}

	private boolean is23() { return is23(raiz); }

	private boolean is23(Node x) 
	{
		if (x == null) return true;
		if (isRed(x.right)) return false;
		if (x != raiz && isRed(x) && isRed(x.left))
			return false;
		return is23(x.left) && is23(x.right);
	} 

	private boolean isBalanced() 
	{ 
		int black = 0;   
		Node x = raiz;
		while (x != null) {
			if (!isRed(x)) black++;
			x = x.left;
		}
		return isBalanced(raiz, black);
	}

	private boolean isBalanced(Node x, int black) 
	{
		if (x == null) return black == 0;
		if (!isRed(x)) black--;
		return isBalanced(x.left, black) && isBalanced(x.right, black);
	}

	public Iterator<K> keys() 
	{
		if (isEmpty())
		{
			return new Queue<K>().iterator();
		}
		return keysInRange(min(), max());
	}

	public Iterator<K> keysInRange(K lo, K hi) 
	{
		if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
		if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

		Queue<K> queue = new Queue<K>();

		keys(raiz, queue, lo, hi);
		return queue.iterator();
	} 

	private void keys(Node x, Queue<K> queue, K lo, K hi) 
	{ 
		if (x == null) return; 
		int cmplo = lo.compareTo(x.key); 
		int cmphi = hi.compareTo(x.key); 
		if (cmplo < 0) keys(x.left, queue, lo, hi); 
		if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key); 
		if (cmphi > 0) keys(x.right, queue, lo, hi); 
	} 

	public Iterator<V> valuesInRange(K lo,K hi)
	{
		Iterator<K> iter = this.keysInRange(lo, hi);

		Queue<V> values = new Queue<V>();

		while(iter.hasNext())
		{
			V value = get(iter.next());

			values.enqueue(value);
		}

		return values.iterator();
	}

	public Iterator<K> iterator() {
		// TODO Auto-generated method stub
		return null;
	} 

}