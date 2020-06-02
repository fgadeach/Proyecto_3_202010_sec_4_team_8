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
		System.out.println("1.  costo mínimo entre dos ubicaciones geográficas por distancia.");
		System.out.println("2. Red de comunicacion (comparendos mayor gravedad).");
		System.out.println("3. costo mínimo entre dos ubicaciones geográficas por número de comparendos.");
		System.out.println("4. Red de comunicacion (comparendos mayor  mayor número).");
		System.out.println("5. caminos más cortos para que los policías puedan atender los M comparendos más graves.");
	}

	public void printMessage(String mensaje) {

		System.out.println(mensaje);
	}		

	public void printModelo(Modelo modelo)
	{
		// TODO implementar

	}
}
