package model.data_structures;

public class Arco<K extends Comparable<K>, A> implements Comparable<Arco<K,A>>
{
	private K primerVertice;
	private K segundoVertice;
	private A info;
	
	public Arco(K primerVert,K segundoVert, A info)
	{
		primerVertice = primerVert;
		segundoVertice = segundoVert;
		this.info = info;
	}
	
	public K darPrimerVertice()
	{
		return primerVertice;
	}
	
	public K darSegundoVertice()
	{
		return segundoVertice;
	}
	
	public A darInfo()
	{
		return info;
	}
	
	public void setInfo(A newInfo)
	{
		info = newInfo;
	}

	@Override
	public int compareTo(Arco<K, A> o) {
		if((double) o.darInfo() < (double) this.darInfo()) {
			return 1;
		} else if((double) o.darInfo() > (double) this.darInfo()) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public K darOtro(K vertice)
	{
		if(vertice.equals(primerVertice))
		{
			return segundoVertice;
		}
		
		else 
		{
			return primerVertice;
		}
	}
	
	   // Returns either endpoint of this edge.
    public int either() 
    { 
    	return (int) primerVertice; 
    }
    

    // Returns the endpoint of the edge different from the given vertex.
    public int other(int vertex) {
        if      (vertex == (int) primerVertice) return (int)segundoVertice;
        else                  return (int)primerVertice;
    }
	
}
