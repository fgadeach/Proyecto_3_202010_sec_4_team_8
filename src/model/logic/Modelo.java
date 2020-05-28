package model.logic;

import model.data_structures.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.opencsv.CSVReader;

import controller.Controller;
import implementaciones_extras.Conexion;
import implementaciones_extras.Haversine;
import implementaciones_extras.Interseccion;
import implementaciones_extras.Indicador;


/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {

	private Graph<Integer, Indicador , Conexion> Graph = new Graph<Integer,Indicador , Conexion>(0);

	private int numeroVerticesEstacion =0;
	private int numeroArcosMixtos = 0;

	public void loadData() throws NumberFormatException, Exception
	{


		this.loadIntersecciones(Controller.RUTA_NODOS);
		this.loadMallaVial(Controller.RUTA_ARCOS);
		this.loadStations(Controller.POLICIA);

		this.guardarGraph();

		System.out.println("Total vertices cargados: " + Graph.E());
		System.out.println("Numero de vertices estacion: " + numeroVerticesEstacion);
		System.out.println("Total arcos cargados: " + Graph.V());
		System.out.println("Numero de arcos mixtos: " + numeroArcosMixtos);
	}

	/**
	 * Loads the trips of the file into a queue and a stack
	 * @param tripsFile the route of the file with the data that will be loaded
	 */

	private void loadStations(String estacionpolicia)
	{
		JSONParser parser = new JSONParser();
		try {     
			Object obj = parser.parse(new FileReader(estacionpolicia));

			JSONObject jsonObject =  (JSONObject) obj;
			JSONArray jsArray = (JSONArray) jsonObject.get("features");

			for(Object o: jsArray) 
			{
				JSONObject comp = (JSONObject) o;	
				JSONObject properties =  (JSONObject) comp.get("features");
				JSONObject geometry =  (JSONObject) comp.get("geometry");	
				String coordenadas = String.valueOf(geometry.get("coordinates"));
				coordenadas = coordenadas.replaceAll("\\[","");
				coordenadas = coordenadas.replaceAll("\\]","");

				String [] coord = coordenadas.split(",");

				Double latitud = Double.parseDouble(coord[1]);
				Double longitud = Double.parseDouble(coord[0]);

				Policia station = new Policia (Integer.parseInt(String.valueOf(comp.get("id"))),latitud,longitud);

				Graph.addVertex(Graph.V(), station);
				numeroVerticesEstacion++;
				double menorDistancia = Double.MAX_VALUE;
				Interseccion interseccionAConectar=null;

				Iterator<Integer> iter = Graph.iterarVertices();
				while(iter.hasNext())
				{	
					Integer act = iter.next();

					if(Graph.getInfoVertex(act) instanceof Interseccion)
					{
						Interseccion actual =  (Interseccion) Graph.getInfoVertex(act);
						double distanciaActual = Haversine.distance(station.darLatitud(), station.darLongitud(), actual.darLatitud(), actual.darLongitud());
						if(menorDistancia>distanciaActual)
						{
							menorDistancia = distanciaActual;
							interseccionAConectar=actual;
						}
					}
				}

				Conexion conexion = new Conexion(menorDistancia);
				Graph.addEdge(Graph.V() - 1, interseccionAConectar.darId(), conexion);
				numeroArcosMixtos++;

			}

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("Se produjo un error. No se pudieron cargar los datos");
		}

	}

	private void loadIntersecciones(String interseccionesFile) throws IOException
	{
		File archivo = new File(interseccionesFile);
		try 
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo));
			String linea;
			String datos[];

			while((linea = bufferedReader.readLine())!=null)
			{
				datos = linea.split(",");
				Integer id = Integer.parseInt(datos[0]);

				double longitud = Double.parseDouble(datos[1]);
				double latitud = Double.parseDouble(datos[2]);
				Interseccion interseccion = new Interseccion(id, latitud, longitud);

				Graph.addVertex(id, interseccion);
			}

			bufferedReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadMallaVial(String mallaVialFile) throws NumberFormatException, Exception
	{
		File archivo = new File(mallaVialFile);

		try 
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo));
			String linea;
			String datos[];

			while((linea = bufferedReader.readLine())!=null)
			{
				datos = linea.split(" ");

				for(int i =1; i<datos.length;i++)
				{
					Interseccion int1 = (Interseccion) Graph.getInfoVertex(Integer.parseInt(datos[0]));
					Interseccion int2 = (Interseccion) Graph.getInfoVertex(Integer.parseInt(datos[i]));

					Conexion conexion = new Conexion(Haversine.distance(int1.darLatitud(), int1.darLongitud(), int2.darLatitud(), int2.darLongitud()));

					Graph.addEdge(Integer.parseInt(datos[0]), Integer.parseInt(datos[i]), conexion);
				}
			}

			bufferedReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void guardarGraph() throws IOException 
	{ 

		JSONObject obj = new JSONObject();
		JSONArray intersecciones = new JSONArray();
		JSONArray estaciones = new JSONArray();
		JSONArray arcos = new JSONArray();

		Iterator<Integer> iter = Graph.iterarVertices();
		while(iter.hasNext()) {
			int id = iter.next();
			Indicador nodo = Graph.getInfoVertex(id);

			if(nodo instanceof Interseccion) {
				intersecciones.add(nodo.toString());
			} else {
				estaciones.add(id + ";" + nodo.toString());
			}
		}

		Iterator<Arco<Integer, Conexion>> iterCalles = Graph.arcos().iterator();
		while(iterCalles.hasNext()) 
		{
			Arco<Integer, Conexion> arco = iterCalles.next();
			String infoArco = String.valueOf(arco.darPrimerVertice() + ";" + arco.darSegundoVertice() + ";"
					+ arco.darInfo().toString());

			arcos.add(infoArco);
		}


		obj.put("Intersecciones", intersecciones);
		obj.put("Estaciones", estaciones);
		obj.put("Arcos", arcos);

		FileWriter fw = new FileWriter(new File("./data/MallaVial.geojson"), false);
		fw.write(obj.toJSONString());
		fw.close();

	}

	public void cargarGraph() throws NumberFormatException, Exception {
		Graph = new Graph<Integer, Indicador, Conexion>(0);
		int numArcos = 0;
		int numVertices = 0;

		JSONParser parser = new JSONParser();

		JSONObject obj = (JSONObject) parser.parse(new FileReader("./data/MallaVial.geojson"));

		JSONArray array = (JSONArray) obj.get("Estaciones");
		for(Object o: array) {
			String object = String.valueOf(o);
			String[] params = object.split(";");

			Policia estacion = new Policia(Integer.valueOf(params[1]), Double.valueOf(params[2]), Double.valueOf(params[3]));
			//System.out.println(params[0]);
			Graph.addVertex(Integer.valueOf(params[0]), estacion);
			numVertices ++;
		}

		array = (JSONArray) obj.get("Intersecciones");
		for(Object o: array) {
			String object = String.valueOf(o);
			String[] params = object.split(";");


			Interseccion interseccion = new Interseccion(Integer.valueOf(params[0]), Double.valueOf(params[1]), 
					Double.valueOf(params[2]));
			Graph.addVertex(interseccion.darId(), interseccion);
		}

		array = (JSONArray) obj.get("Arcos");
		for(Object o: array) {
			String object = String.valueOf(o);
			String[] params = object.split(";");

			Conexion calle = new Conexion(Double.valueOf(params[2]));
			Graph.addEdge(Integer.valueOf(params[0]), Integer.valueOf(params[1]), calle);
			Indicador nodo1 = Graph.getInfoVertex(Integer.valueOf(params[0]));
			Indicador nodo2 = Graph.getInfoVertex(Integer.valueOf(params[1]));

			if(nodo1 instanceof Policia||nodo2 instanceof Policia ) 
			{
				numArcos ++;
			}
		}

		System.out.println("Número vertices (Interseccion): " + (Graph.V() - numVertices));
		System.out.println("Número vertices (Estacion): " + numVertices);
		System.out.println("Número arcos (Interseccion): " + (Graph.E() - numArcos));
		System.out.println("Número arcos (Mixtos): " + numArcos);
	}

	public void dibujarGraph() throws IOException
	{
		File file = new File("hola.html");

		PrintWriter pr = new PrintWriter(file);
		pr.println(" <!DOCTYPE html>\n" +
		"<html>\n" +
		"  <head>\n" +
		"    <title>Mapa</title>\n" +
		"    <meta name=\"viewport\" content=\"initial-scale=1.0\">\n" +
		"    <meta charset=\"utf-8\">\n" +
		"    <style>\n" +
		"      /* Always set the map height explicitly to define the size of the div\n" +
		"       * element that contains the map. */\n" +
		"      #map {\n" +
		"        height: 100%;\n" +
		"      }\n" +
		"      /* Optional: Makes the sample page fill the window. */\n" +
		"      html, body \n" +
		"      {\n" +
		"        height: 100%;\n" +
		"        margin: 0;\n" +
		"        padding: 0;\n" +
		"      }\n" +
		"    </style> \n" +
		"  </head> \n" +
		"  <body> \n" +
		"    <div id=\"map\"></div> \n" +
		"    <script> \n");	
		pr.println(" function initMap() {\n"+
				"    var map = new google.maps.Map(document.getElementById('map'), {\n"+
				"      zoom: 12,\n"+
				"      center: {lat: 4.647956, lng: -74.083781},\n"+
				"      mapTypeId: 'terrain'\n"+
				"    });\n");
		Iterator<String> iter = Graph.iterarArcos();

		while(iter.hasNext() && contador>0)
		{	
			

			String act = iter.next();
			String info[] = act.split("-");

			Indicador sup1 = Graph.getInfoVertex(Integer.parseInt(info[0]));
			Indicador sup2 = Graph.getInfoVertex(Integer.parseInt(info[1]));
			
//			pr.println("var flightPlanCoordinates = [\r\n" + 
//				"    {lat:"+ sup1.darLatitud() +", lng:"+ sup1.darLongitud()+"},\r\n" +  
//				"    {lat:"+ sup2.darLatitud()+", lng:"+ sup2.darLongitud()+"}\r\n" + 
//					"  ];");
//			pr.println("var flightPath = new google.maps.Polyline({\r\n" + 
//					"    path: flightPlanCoordinates,\r\n" + 
//					"    geodesic: true,\r\n" + 
//					"    strokeColor: '#FF0000',\r\n" + 
//					"    strokeOpacity: 1.0,\r\n" + 
//					"    strokeWeight: 1.0\r\n" + 
//					"  });\r\n" + 
//					"\r\n" + 
//					"  flightPath.setMap(map);");
			
			pr.println(
					"          var cityCircle = new google.maps.Circle({\r\n" + 
					"            strokeColor: '#FF0000',\r\n" + 
					"            strokeOpacity: 0.8,\r\n" + 
					"            strokeWeight: 2,\r\n" + 
					"            fillColor: '#FF0000',\r\n" + 
					"            fillOpacity: 0.35,\r\n" + 
					"            map: map,\r\n" + 
					"            center: {lat:"+sup1.darLatitud()+",lng:"+sup1.darLongitud()+"},\r\n" + 
					"            radius: 10\r\n" + 
					"          });\r\n"+
					"cityCircle.setMap(map);");
			contador--;
		}
		
		pr.println("}\n");
		pr.println("  </script>\n" +"<script async defer     src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyB4IQ2SrwpoZ_6fs9jOngVXxHNU1CtVK3g&callback=initMap\">");
		pr.println("</script>");
		pr.println("</body>");
		pr.println("</html>");
		pr.close();
		java.awt.Desktop.getDesktop().browse(file.toURI());
	}
}


