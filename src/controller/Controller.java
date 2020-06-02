package controller;

import java.io.FileReader;



import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import model.logic.Modelo;
import view.View;

public class Controller {

	/*
	 * 
	 *
	/* Instancia del Modelo*/
	private Modelo modelo;

	/* Instancia de la Vista*/
	private View view;

	public static final String RUTA_ARCOS="./data/bogota_arcos.txt";
	public static final String RUTA_COMPARENDOS="./data/Comparendos_DEI_2018_Bogotá_D.C_small_50000_sorted.geojson";
	public static final String RUTA_NODOS="./data/bogota_vertices.txt";
	public static final String jsonAV="./data/grafo.geojson";
	public static final String POLICIA="./data/estacionpolicia.geojson";
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller()
	{

		view = new View();
		modelo = new Modelo();
	}

	@SuppressWarnings("null")
	public void run() 
	{
		int s =0;
		double li = 0;
		double loi = 0;
		double lf = 0;
		double lof = 0;
		long tiempoI=0;

		Scanner lector = new Scanner(System.in);
		boolean fin = false;

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){

			case 0:
				modelo = new Modelo(); 
				try {
					tiempoI = System.nanoTime();
					modelo.loadData();
					modelo.cargarGraph();
					long tiempoF = System.nanoTime();
					double demora = (tiempoF - tiempoI)/ 1e6;
					System.out.println("Tiempo de demora: "+ demora);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("no carga");
				}

			case 1:
				try {
					tiempoI = System.nanoTime();
					System.out.println("--------- \nm Latitud inicial: ");
					li = lector.nextInt();
					System.out.println("--------- \nm longitud inicial: ");
					loi = lector.nextInt();
					System.out.println("--------- \nm Latitud final: ");
					lf = lector.nextInt();
					System.out.println("--------- \nm longitud final: ");
					lof = lector.nextInt();

					modelo.caminoCostoMinimo(li, loi, lf, lof);
					long tiempoF = System.nanoTime();
					double demora = (tiempoF - tiempoI)/ 1e6;
					System.out.println("Tiempo de demora: "+ demora);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("no calcula el costo");
				}
				break;

			case 2:
				try {
					tiempoI = System.nanoTime();
					modelo.loadComparendos(RUTA_COMPARENDOS);
					System.out.println("--------- \nm Comparendos: ");
					s = lector.nextInt();
					modelo.redComunicacion(s);
					long tiempoF = System.nanoTime();
					double demora = (tiempoF - tiempoI)/ 1e6;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("no carga el grafo");
				}
				break;

			case 3:
				try {
					tiempoI = System.nanoTime();
					System.out.println("--------- \nm Latitud inicial: ");
					li = lector.nextInt();
					System.out.println("--------- \nm longitud inicial: ");
					loi = lector.nextInt();
					System.out.println("--------- \nm Latitud final: ");
					lf = lector.nextInt();
					System.out.println("--------- \nm longitud final: ");
					lof = lector.nextInt();

					modelo.caminoCostoMinimoPorComparendos(li, loi, lf, lof);
					long tiempoF = System.nanoTime();
					double demora = (tiempoF - tiempoI)/ 1e6;
					System.out.println("Tiempo de demora: "+ demora);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("no calcula el costo");
				}
				break;

			case 4:

				try {
					tiempoI = System.nanoTime();
					modelo.loadComparendos(RUTA_COMPARENDOS);
					System.out.println("--------- \nm Comparendos: ");
					s = lector.nextInt();
					modelo.caminosCortosPolicia(s);
					long tiempoF = System.nanoTime();
					double demora = (tiempoF - tiempoI)/ 1e6;

					System.out.println("Tiempo de demora: "+ demora);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("no carga el grafo");
				}
				break;

			default: 
				System.out.println("--------- \n Opcion Invalida !! \n---------");
				break;
			}
		}
	}
}	