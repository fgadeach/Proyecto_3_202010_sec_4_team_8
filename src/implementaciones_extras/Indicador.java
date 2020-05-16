package implementaciones_extras;

public abstract class Indicador {

	protected int id;
	protected double longitud;
	protected double latitud;
	
	public Indicador(int id, double longitud, double latitud)
	{
		this.id=id;
		this.latitud=latitud;
		this.longitud=longitud;
	}
	
	public int darId()
	{
		return id;
	}
	
	public double darLatitud()
	{
		return latitud;
	}
	
	public double darLongitud()
	{
		return longitud;
	}
	
	public abstract String toString();
}
