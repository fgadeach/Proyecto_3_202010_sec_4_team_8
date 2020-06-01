package implementaciones_extras;

import java.util.Iterator;

import model.data_structures.Graph;
import model.data_structures.Pila;
import model.data_structures.Queue;
import model.data_structures.SeparateChainingHashST;

public class Kosajatu <K extends Comparable<K>, V, A>
{
	
	private SeparateChainingHashST<K, Boolean> marked;
	private SeparateChainingHashST<K, Integer> id;
	private int count=0;
	
	public Kosajatu(Graph<K,V,A> grafo)
	{
		marked = new SeparateChainingHashST<>(grafo.V());
		
		Iterator<K> iter = grafo.iterarVertices();
		while(iter.hasNext())
		{
			marked.put(iter.next(), false);
		}
		
		id = new SeparateChainingHashST<>(grafo.V());
		
		DepthFirstOrder<K,V,A> dfs = new DepthFirstOrder<K, V, A>(grafo.reves());
		
		for(K v: dfs.reversePost())
		{	
			if(!marked.get(v))
			{
				dfs(grafo, v);
				count ++;	
				System.out.println("V: " + v + " - Count: " + count + " - Id: " + id.get(v));
			}
		}
	}
	
	private void dfs(Graph<K,V,A> grafo, K v)
	{
		marked.put(v, true);
		id.put(v, count);
		
		
		for(K v2: grafo.adj(v))
		{	
			if(!marked.get(v2))
			{
				dfs(grafo, v2);
			}
		}
	}
	
	public int count()
	{
		return count;
	}
	
	public boolean stronglyConnected(K v1, K v2)
	{
		return id.get(v1).equals(id.get(v2));
	}
	
	public int id(K v)
	{
		return id.get(v);
	}
	
	public SeparateChainingHashST<K, Integer> darIds()
	{
		return id;
	}
	
}
	

class DepthFirstOrder <K extends Comparable<K>, V, A>
{
	private SeparateChainingHashST<K, Boolean> marked;
	private SeparateChainingHashST<K, Integer> pre;
	private SeparateChainingHashST<K, Integer> post;
	
	private Queue<K> preorder;
    private Queue<K> postorder;
    
    private int preCounter;            
    private int postCounter;
    
    public DepthFirstOrder(Graph<K,V,A> grafo)
    {
    		marked = new SeparateChainingHashST<>(grafo.V());
    		
    		Iterator<K> iterator = grafo.iterarVertices();
    		while(iterator.hasNext())
    		{
    			marked.put(iterator.next(), false);
    		}
    		
    		pre = new SeparateChainingHashST<>(grafo.V());
    		post = new SeparateChainingHashST<>(grafo.V());
    		preorder = new Queue<K>();
    		postorder = new Queue<K>();
    		
    		Iterator<K> iter = grafo.iterarVertices();
    		while(iter.hasNext())
    		{
    			K vertice = iter.next();
    			
    			if(!marked.get(vertice))
    			{
    				dfs(grafo,vertice);
    			}
    		}
    }
    
    private void dfs(Graph<K,V,A> grafo, K v)
    {
    		marked.put(v, true);
    		pre.put(v, preCounter++);
    		
    		preorder.enqueue(v);
    		
    		for(K v2 : grafo.adj(v))
    		{
    			if(!marked.get(v2))
    			{
    				dfs(grafo, v2);
    			}
    		}
    		
    		postorder.enqueue(v);
    		post.put(v, postCounter++);
    }
    
    public int pre(K v) 
    {
        return pre.get(v);
    }

    
    public int post(K v) 
    {
        return post.get(v);
    }

   
    public Iterable<K> post() 
    {
        return postorder;
    }

    public Iterable<K> pre() 
    {
        return preorder;
    }

    public Iterable<K> reversePost() 
    {
        Pila<K> reverse = new Pila<K>();
        for (K v : postorder)
        {
        		reverse.push(v);
        }
        return reverse;
    }
}