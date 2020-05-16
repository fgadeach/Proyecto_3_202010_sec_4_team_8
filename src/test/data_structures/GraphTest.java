package test.data_structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import model.data_structures.*;

public class GraphTest{
	
private Graph<Integer, String, String> Graph;
	int m = 0;
	public void setUpScenario1()
	{
		Graph = new Graph<Integer,String, String>(m);
	}
	
	public void setUpScenario2() 
	{
		Graph = new Graph<Integer,String, String>(m);
		
		Graph.addVertex(0, "Cero");
		Graph.addVertex(1, "Uno");
		Graph.addVertex(2, "Dos");
		Graph.addVertex(3, "Tres");
		Graph.addVertex(4, "Cuatro");
		
		try 
		{
			Graph.addEdge(0, 1, "Arco01");
			Graph.addEdge(0, 2, "Arco02");
			Graph.addEdge(1, 3, "Arco13");
			Graph.addEdge(1, 4, "Arco14");
			Graph.addEdge(2, 3, "Arco23");
			Graph.addEdge(2, 4, "Arco24");
		} 
		catch (Exception e) 
		{
			System.out.println("No se pudo agregar el arco");
			e.printStackTrace();
		}
	}
	
	public void setUpScenario3() 
	{
		Graph = new Graph<Integer,String, String>(m);
		
		Graph.addVertex(0, "Cero");
		Graph.addVertex(1, "Uno");
		Graph.addVertex(2, "Dos");
		Graph.addVertex(3, "Tres");
		Graph.addVertex(4, "Cuatro");
		Graph.addVertex(5, "Cinco");
		
		try 
		{
			Graph.addEdge(0, 1, "Arco01");
			Graph.addEdge(0, 2, "Arco02");
			Graph.addEdge(1, 3, "Arco13");
			Graph.addEdge(1, 4, "Arco14");
			Graph.addEdge(2, 3, "Arco23");
			Graph.addEdge(2, 4, "Arco24");
			
			//Arcos que no deberia agregar por ser No Dirigido
			Graph.addEdge(0, 2, "Arco02");
			Graph.addEdge(1, 0, "Arco10");
			Graph.addEdge(3, 1, "Arco31");
			Graph.addEdge(4, 2, "Arco42");
			
		} 
		catch (Exception e) 
		{
			System.out.println("No se pudo agregar el arco");
			e.printStackTrace();
		}
	}
	
	
	//En este se pruba que los vertices se estan agregando correctae
	@Test
	public void testDarNumeroVertices() 
	{
		setUpScenario1();
		assertEquals(0, Graph.V(), "El tamaño no es correcto. No deberian haber elementos");
		
		try 
		{
			setUpScenario2();
			assertEquals(5, Graph.V(), "El numero de elementos no es correcto");
			setUpScenario3();
			assertEquals(6, Graph.V(), "El numero de elementos no es correcto");

		} 
		catch (Exception e) 
		{
			fail("No deberia mandar exception");
		}
	}

	@Test
	public void testDarNumeroArcos() 
	{
		setUpScenario1();
		assertEquals(0, Graph.E(), "El tamaño no es correcto. No deberian haber elementos");
		
		try 
		{
			setUpScenario2();
			assertEquals(6, Graph.E(), "El numero de elementos no es correcto");
			
			//Prueba que el Graph, al ser no dirigido, agrega una unica vez un arco entre los mismos dos vertices
			setUpScenario3();
			assertEquals(6, Graph.E(), "El numero de elementos no es correcto");
		} 
		catch (Exception e) 
		{
			fail("No deberia mandar exception");
		}
	}
	
	
	@Test
	public void testGetInfoVertex() 
	{
		setUpScenario2();
		
		assertEquals("Cero", Graph.getInfoVertex(0), "No retorna la informacion correcta");
		assertEquals("Uno", Graph.getInfoVertex(1), "No retorna la informacion correcta");
		assertEquals("Dos", Graph.getInfoVertex(2), "No retorna la informacion correcta");
		assertEquals("Tres", Graph.getInfoVertex(3), "No retorna la informacion correcta");
		assertEquals("Cuatro", Graph.getInfoVertex(4), "No retorna la informacion correcta");
	}
	
	@Test
	public void testGetInfoArc() 
	{
		setUpScenario3();
		
		assertEquals("Arco01", Graph.getInfoArc(0, 1), "No retorna la informacion correcta");
		assertEquals("Arco02", Graph.getInfoArc(0, 2), "No retorna la informacion correcta");
		assertEquals("Arco13", Graph.getInfoArc(1, 3), "No retorna la informacion correcta");
		assertEquals("Arco14", Graph.getInfoArc(1, 4), "No retorna la informacion correcta");
		assertEquals("Arco23", Graph.getInfoArc(2, 3), "No retorna la informacion correcta");
		assertEquals("Arco24", Graph.getInfoArc(2, 4), "No retorna la informacion correcta");
		
		//Prueba que me de la misma informacion de arco si comienza del otro vertice
		assertEquals("Arco01", Graph.getInfoArc(1, 0), "No retorna la informacion correcta");
		assertEquals("Arco02", Graph.getInfoArc(2, 0), "No retorna la informacion correcta");
		assertEquals("Arco13", Graph.getInfoArc(3, 1), "No retorna la informacion correcta");
		assertEquals("Arco14", Graph.getInfoArc(4, 1), "No retorna la informacion correcta");
		assertEquals("Arco23", Graph.getInfoArc(3, 2), "No retorna la informacion correcta");
		assertEquals("Arco24", Graph.getInfoArc(4, 2), "No retorna la informacion correcta");
	}

	@Test
	public void testSetInfoVertex() 
	{
		setUpScenario2();
		
		assertEquals("Cero", Graph.getInfoVertex(0), "No retorna la informacion correcta");
		Graph.setInfoVertex(0, "CambiandoInfoVertex");
		assertEquals("CambiandoInfoVertex", Graph.getInfoVertex(0), "No retorna la informacion correcta");
		
	}
	
	@Test
	public void testSetInfoArc() 
	{
		setUpScenario3();
		
		assertEquals("Arco01", Graph.getInfoArc(0, 1), "No retorna la informacion correcta");
		assertEquals("Arco01", Graph.getInfoArc(1, 0), "No retorna la informacion correcta");
		
		Graph.setInfoArc(0, 1, "CambiandoInfoArco");
		
		assertEquals("CambiandoInfoArco", Graph.getInfoArc(0, 1), "No retorna la informacion correcta");
		assertEquals("CambiandoInfoArco", Graph.getInfoArc(1, 0), "No retorna la informacion correcta");
	}
	
	@Test
	public void testAdj() 
	{
		setUpScenario3();
		Array<Integer> adj = new Array<Integer>();
		Iterator<Integer> iter =Graph.adj(0).iterator();
		
		while(iter.hasNext())
		{
			adj.append(iter.next());
		}
		
		assertEquals(2, adj.size(), "No hay el numero correcto de adjuntos");
		
		iter =Graph.adj(2).iterator();
		
		Array<Integer> adj2 = new Array<Integer>();
		while(iter.hasNext())
		{
			adj2.append(iter.next());
		}
		
		assertEquals(3, adj2.size(), "No hay el numero correcto de adjuntos");
		
	}
	
}