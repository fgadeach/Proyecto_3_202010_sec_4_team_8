package model.data_structures;

public class Arco<K extends Comparable<K>, A>
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
	
}
