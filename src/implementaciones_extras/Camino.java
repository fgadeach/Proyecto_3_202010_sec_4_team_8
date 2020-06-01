package implementaciones_extras;

import model.data_structures.Vertice;

public class Camino <T>
{
	private Iterable<T> pasos;
	private double distancia;
	private Vertice verticeOrigen;
	private Vertice verticeDestino;
	
	/**
	 * @param vertices
	 * @param distancia
	 * @param verticeOrigen
	 * @param verticeDestino
	 */
	public Camino(Iterable<T> pasos, double distancia, Vertice verticeOrigen, Vertice verticeDestino) {
		this.pasos = pasos;
		this.distancia = distancia;
		this.verticeOrigen = verticeOrigen;
		this.verticeDestino = verticeDestino;
	}

	/**
	 * @return the vertices
	 */
	public Iterable<T> getPasos() 
	{
		return pasos;
	}

	/**
	 * @return the distancia
	 */
	public double getDistancia() 
	{
		return distancia;
	}

	/**
	 * @return the verticeOrigen
	 */
	public Vertice getverticeOrigen() {
		return verticeOrigen;
	}

	/**
	 * @return the verticeDestino
	 */
	public Vertice getverticeDestino() {
		return verticeDestino;
	}

}
