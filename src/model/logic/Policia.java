package model.logic;

import implementaciones_extras.Indicador;

public class Policia extends Indicador implements Comparable<Policia> 
{

	private int id;

	public Policia(int id, double latitud, double longitud) {
		
		super(id, longitud, latitud);

		this.id = id;
	}

	public int darId() 
	{
		return id;
	}

	@Override
	public int compareTo(Policia o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(super.id + ";" + super.latitud + ";" + super.longitud);
	}

}
