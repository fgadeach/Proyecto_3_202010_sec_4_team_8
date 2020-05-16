package implementaciones_extras;

public class Conexion {

	private double distancia;

	public Conexion(double distancia)
	{	
		this.distancia = distancia;
	}


	public double getDistancia() 
	{
		return distancia;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(distancia);
	}	
}
