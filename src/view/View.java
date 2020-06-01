package view;

import model.logic.Modelo;
import model.data_structures.*;

public class View 
{
	/**
	 * Metodo constructor
	 */
	public View()
	{

	}

	public void printMenu()
	{
		System.out.println("0. Cargar datos prueba.");
		System.out.println("1. Cargar Grafo.");
		System.out.println("2. Red de comunicacion (comparendos mayor gravedad).");

	}

	public void printMessage(String mensaje) {

		System.out.println(mensaje);
	}		

	public void printModelo(Modelo modelo)
	{
		// TODO implementar

	}
}
