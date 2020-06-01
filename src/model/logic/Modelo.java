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
import implementaciones_extras.Camino;
import implementaciones_extras.Conexion;
import implementaciones_extras.Dijkstra;
import implementaciones_extras.Haversine;
import implementaciones_extras.Interseccion;
import implementaciones_extras.Prim;
import implementaciones_extras.Indicador;


/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {



	private SeparateChainingHashST<Integer, Vertice> vertices = new SeparateChainingHashST<>(300);
	private Minheap heap = new Minheap(100000);

	private Graph<Integer, Indicador , Double> Graph = new Graph<Integer,Indicador , Double>(1);

	private Graph<Integer, Indicador , Double> GraphComparendos = new Graph<Integer,Indicador , Double>(0);
	private ArbolRojoNegro<String,Comparendos> listaComparendos = new ArbolRojoNegro<>();
	private ArbolRojoNegro<String,Comparendos> listaComparendosMayorGravedad = new ArbolRojoNegro<>();


	private int numeroVerticesvertice =0;
	private int numeroArcosMixtos = 0;

	public void loadData() throws NumberFormatException, Exception
	{


		this.loadIntersecciones(Controller.RUTA_NODOS);
		this.loadMallaVial(Controller.RUTA_ARCOS);
		this.loadverts(Controller.POLICIA);

		this.guardarGraph();

		System.out.println("Total vertices cargados: " + Graph.E());
		System.out.println("Numero de vertices vertice: " + numeroVerticesvertice);
		System.out.println("Total arcos cargados: " + Graph.V());
		System.out.println("Numero de arcos mixtos: " + numeroArcosMixtos);
	}

	private Minheap Minheap(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Loads the trips of the file into a queue and a stack
	 * @param tripsFile the route of the file with the data that will be loaded
	 */

	private void loadverts(String verticepolicia)
	{
		JSONParser parser = new JSONParser();
		try {     
			Object obj = parser.parse(new FileReader(verticepolicia));

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

				Policia vert = new Policia (Integer.parseInt(String.valueOf(comp.get("id"))),latitud,longitud);

				Graph.addVertex(Graph.V(), vert);

				numeroVerticesvertice++;

				double menorDistancia = Double.MAX_VALUE;

				Interseccion interseccionAConectar=null;

				Iterator<Integer> iter = Graph.iterarVertices();
				while(iter.hasNext())
				{	
					Integer act = iter.next();

					if(Graph.getInfoVertex(act) instanceof Interseccion)
					{
						Interseccion actual =  (Interseccion) Graph.getInfoVertex(act);

						double distanciaActual = Haversine.distance(vert.darLatitud(), vert.darLongitud(), actual.darLatitud(), actual.darLongitud());

						if(menorDistancia>distanciaActual)
						{
							menorDistancia = distanciaActual;
							interseccionAConectar=actual;
						}
					}
				}

				Graph.addEdge(Graph.V() - 1, interseccionAConectar.darId(), menorDistancia);
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
				Vertice vert = new Vertice (id, interseccion);

				Graph.addVertex(id, interseccion);

				vertices.put(id, vert);
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

					Double conexion = Haversine.distance(int1.darLatitud(), int1.darLongitud(), int2.darLatitud(), int2.darLongitud());

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
		JSONArray verticees = new JSONArray();
		JSONArray arcos = new JSONArray();

		Iterator<Integer> iter = Graph.iterarVertices();
		while(iter.hasNext()) {
			int id = iter.next();
			Indicador nodo = Graph.getInfoVertex(id);

			if(nodo instanceof Interseccion) {
				intersecciones.add(nodo.toString());
			} else {
				verticees.add(id + ";" + nodo.toString());
			}
		}

		Iterator<Arco<Integer, Double>> iterCalles = Graph.arcos().iterator();

		while(iterCalles.hasNext()) 
		{
			Arco<Integer, Double> arco = iterCalles.next();
			String infoArco = String.valueOf(arco.darPrimerVertice() + ";" + arco.darSegundoVertice() + ";"
					+ arco.darInfo().toString());

			arcos.add(infoArco);
		}


		obj.put("Intersecciones", intersecciones);
		obj.put("verticees", verticees);
		obj.put("Arcos", arcos);

		FileWriter fw = new FileWriter(new File("./data/MallaVial.geojson"), false);
		fw.write(obj.toJSONString());
		fw.close();

	}

	public void cargarGraph() throws NumberFormatException, Exception 
	{
		Graph = new Graph<Integer, Indicador, Double>(0);
		int numArcos = 0;
		int numVertices = 0;

		JSONParser parser = new JSONParser();

		JSONObject obj = (JSONObject) parser.parse(new FileReader("./data/MallaVial.geojson"));

		JSONArray array = (JSONArray) obj.get("verticees");
		for(Object o: array) {
			String object = String.valueOf(o);
			String[] params = object.split(";");

			Policia vertice = new Policia(Integer.valueOf(params[1]), Double.valueOf(params[2]), Double.valueOf(params[3]));
			//System.out.println(params[0]);
			Graph.addVertex(Integer.valueOf(params[0]), vertice);
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

			Double calle = Double.valueOf(params[2]);
			Graph.addEdge(Integer.valueOf(params[0]), Integer.valueOf(params[1]), calle);
			Indicador nodo1 = Graph.getInfoVertex(Integer.valueOf(params[0]));
			Indicador nodo2 = Graph.getInfoVertex(Integer.valueOf(params[1]));

			if(nodo1 instanceof Policia||nodo2 instanceof Policia ) 
			{
				numArcos ++;
			}
		}

		System.out.println("Número vertices (Interseccion): " + (Graph.V() - numVertices));
		System.out.println("Número vertices (vertice): " + numVertices);
		System.out.println("Número arcos (Interseccion): " + (Graph.E() - numArcos));
		System.out.println("Número arcos (Mixtos): " + numArcos);
	}

	public void dibujarGraph() throws IOException
	{
		int contador = 10000;
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

			pr.println("var flightPlanCoordinates = [\r\n" + 
					"    {lat:"+ sup1.darLatitud() +", lng:"+ sup1.darLongitud()+"},\r\n" +  
					"    {lat:"+ sup2.darLatitud()+", lng:"+ sup2.darLongitud()+"}\r\n" + 
					"  ];");
			pr.println("var flightPath = new google.maps.Polyline({\r\n" + 
					"    path: flightPlanCoordinates,\r\n" + 
					"    geodesic: true,\r\n" + 
					"    strokeColor: '#FF0000',\r\n" + 
					"    strokeOpacity: 1.0,\r\n" + 					
					"    strokeWeight: 1.0\r\n" + 
					"  });\r\n" + 
					"\r\n" + 
					"  flightPath.setMap(map);");

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

	public void loadComparendos (String comparendosFile)
	{
		JSONParser parser = new JSONParser();

		try {     
			Object obj = parser.parse(new FileReader(comparendosFile));

			JSONObject jsonObject =  (JSONObject) obj;
			JSONArray jsArray = (JSONArray) jsonObject.get("features");

			for(Object o: jsArray) {
				JSONObject comp = (JSONObject) o;	
				JSONObject properties =  (JSONObject) comp.get("properties");
				JSONObject geometry =  (JSONObject) comp.get("geometry");
				JSONArray coordinates = (JSONArray) geometry.get("coordinates");
				Comparendos comparendo = new Comparendos(String.valueOf(comp.get("type")), Integer.parseInt(String.valueOf(properties.get("OBJECTID"))), String.valueOf(properties.get("FECHA_HORA")), String.valueOf((properties).get("MEDIO_DETECCION")),String.valueOf(properties.get("CLASE_VEHI")), String.valueOf(properties.get("TIPO_SERVI")), String.valueOf(properties.get("INFRACCION")), String.valueOf(properties.get("DES_INFRAC")), String.valueOf(properties.get("LOCALIDAD")), String.valueOf(properties.get("MUNICIPIO")),String.valueOf(geometry.get("type")), String.valueOf(coordinates));
				String objectId = Integer.toString(comparendo.getOBJECTID());
				listaComparendos.put(objectId, comparendo);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}


	public void comparendosMayorGravedad(int m) 
	{

		Iterator<String> iter = listaComparendos.keys();
		int numero = -20;

		while(iter.hasNext()) 
		{	
			String llave = iter.next();
			Comparendos comparendo = listaComparendos.get(llave);

			if(comparendo.getTIPO_SERVI().equals("Público")) 
			{
				numero = 3;
			}
			else if(comparendo.getTIPO_SERVI().equals("Oficial")) 
			{
				numero = 2;
			}
			else if(comparendo.getTIPO_SERVI().equals("Particular")) 
			{
				numero = 1;
			}

			String objectId = numero+comparendo.getINFRACCION();
			listaComparendosMayorGravedad.put(objectId, comparendo);

		}
		Iterator<String> iter2 = listaComparendosMayorGravedad.keys();
		while(iter2.hasNext() && m>0) 
		{	

			String llave = iter2.next();
			Comparendos comparendo = listaComparendosMayorGravedad.get(listaComparendosMayorGravedad.max());

			listaComparendosMayorGravedad.deleteMax();
			m--;
		}
	}

	public void cargarComparendosMayorGravedadVertice(int m) 
	{
		comparendosMayorGravedad(m);

		Iterator<String> iter = listaComparendosMayorGravedad.keys();

		while(iter.hasNext() && m>0) 
		{	

			String llave = iter.next();
			Comparendos comparendo = listaComparendosMayorGravedad.get(listaComparendosMayorGravedad.max());
			comparendo.setLatitudyLongitud();
			listaComparendosMayorGravedad.deleteMax();

			Iterator<Integer> iter2 = Graph.iterarVertices();

			Indicador inicio = Graph.getInfoVertex(0);
			int idMasCerca = 0;
			while(iter2.hasNext())
			{	
				int id = iter2.next();
				Indicador actual = Graph.getInfoVertex(id);


				double distanciaInicio = Haversine.distance(comparendo.darLatitud(), comparendo.darLongitud(), inicio.darLatitud(), inicio.darLongitud());
				double distanciaComp = Haversine.distance(comparendo.darLatitud(), comparendo.darLongitud(), actual.darLatitud(), actual.darLongitud());

				if(distanciaComp<distanciaInicio) 
				{
					inicio = actual;
					idMasCerca = actual.darId();
				}
				if(iter2.hasNext()!=true) 
				{
					Graph.vertices.get(idMasCerca).agregarComparendos(comparendo);
					break;
				}
			}
			m--;
		}
	}




	public Camino<Arco<Integer,Double>> caminoCostoMinimo(double latitudIni, double longitudIni, double latitudFin, double longitudFin) throws Exception
	{
		Camino <Arco<Integer,Double>> masCorto = null;

		Vertice verticeMasCercanaInicio=null;
		Vertice verticeMasCercanaFin=null;

		double distanciaMinimaverticeInicio = Double.MAX_VALUE;
		double distanciaMinimaverticeFin = Double.MAX_VALUE;


		Iterator<Integer> iter = vertices.keys();
		while(iter.hasNext())
		{
			Vertice station = vertices.get(iter.next());

			double distanciaInicio = Haversine.distance(latitudIni, longitudIni, station.darLatitud(), station.darLongitud());
			double distanciaFin = Haversine.distance(latitudFin, longitudFin, station.darLatitud(), station.darLongitud());

			if(distanciaInicio<distanciaMinimaverticeInicio)
			{
				distanciaMinimaverticeInicio = distanciaInicio;
				verticeMasCercanaInicio = station;
			}

			if(distanciaFin<distanciaMinimaverticeFin)
			{
				distanciaMinimaverticeFin = distanciaFin;
				verticeMasCercanaFin = station;
			}
		}

		Dijkstra<Integer, Indicador, Double> d = new Dijkstra<Integer, Indicador, Double>(Graph, (int) verticeMasCercanaInicio.darKey());

		masCorto = new Camino<Arco<Integer,Double>>(d.pathTo((int)verticeMasCercanaFin.darKey()), d.distTo((int)verticeMasCercanaFin.darKey()), 
				verticeMasCercanaInicio, verticeMasCercanaFin);	

		return masCorto;
	}

	public void redComunicacion(int m) throws Exception
	{
		Camino <Arco<Integer,Double>> masCorto = null;

		cargarComparendosMayorGravedadVertice(m);


		double pesoTotal = 0;
		int not = -1000;
		Iterator<Integer> iter = Graph.iterarVertices();
		int other = -1000;
		while(iter.hasNext()) 
		{
			int id = iter.next();
			Vertice vert = Graph.vertices.get(id);
			if(vert.hasComparendos()) 
			{	
				not = id;
			}
		}

		Dijkstra<Integer, Indicador, Double> d = new Dijkstra<Integer, Indicador, Double>(Graph, not);

		Vertice verticeIni = Graph.vertices.get(not);
		File file = new File("1B.html");

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
		
		
		Iterator<Integer> iter2 = Graph.iterarVertices();
		while(iter2.hasNext()) 
		{
			int id = iter2.next();
	
			Vertice vert = Graph.vertices.get(id);
		
			
			if(vert.hasComparendos()&&id!=not) 
			{
				masCorto = new Camino<Arco<Integer,Double>>(d.pathTo((int)vert.darKey()), d.distTo((int)vert.darKey()), verticeIni, vert);

				pr.println(
						"          var cityCircle = new google.maps.Circle({\r\n" + 
								"            strokeColor: '#FF0000',\r\n" + 
								"            strokeOpacity: 0.8,\r\n" + 
								"            strokeWeight: 2,\r\n" + 
								"            fillColor: '#FF0000',\r\n" + 
								"            fillOpacity: 0.35,\r\n" + 
								"            map: map,\r\n" + 
								"            center: {lat:"+Graph.getInfoVertex(id).darLatitud()+",lng:"+Graph.getInfoVertex(id).darLongitud()+"},\r\n" + 
								"            radius:"+ vert.darRadio() +"\r\n" + 
								"          });\r\n"+
						"cityCircle.setMap(map);");
			}	
		}
		pr.println("}\n");
		pr.println("  </script>\n" +"<script async defer     src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyB4IQ2SrwpoZ_6fs9jOngVXxHNU1CtVK3g&callback=initMap\">");
		pr.println("</script>");
		pr.println("</body>");
		pr.println("</html>");
		pr.close();
		java.awt.Desktop.getDesktop().browse(file.toURI());
		
		if(pesoTotal!=0) 
		{
			System.out.println("Peso Total: " + pesoTotal + " Costo Total: " + pesoTotal*10000+"$");
		}
		else 
		{
			System.out.println("No hay conexion entre el vertice origen: " + verticeIni.darValue() + " Y los demas vertices");
		}
	}

}



