package test.data_structures;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.Test;

import model.data_structures.SeparateChainingHashST;

public class SeparateChainingHashSTTest 
{
	private SeparateChainingHashST<Integer,String> hash;

	public void setUpScenario1()
	{
		hash = new SeparateChainingHashST<Integer,String>(5);
	}

	public void setUpScenario2() 
	{
		hash = new SeparateChainingHashST<Integer,String>(5);

		try
		{
			hash.put(0,"Primero");
			hash.put(1,"Segundo");
			hash.put(2,"Tercero");
			hash.put(3,"Cuarto");
			hash.put(4,"Quinto");

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}


	@Test
	public void testSize() 
	{
		setUpScenario1();
		assertEquals(0, hash.size(), "El tamaño no es correcto. No deberian haber elementos");

		try 
		{
			setUpScenario2();
			assertEquals(5, hash.size(), "El numero de elementos no es correcto");
		} 
		catch (Exception e) 
		{
			fail("No deberia mandar exception");
		}
	}

	@Test
	public void testGet() 
	{
		try 
		{
			setUpScenario2();
			assertEquals("Primero", hash.get(0), "El numero de elementos no es correcto");
			assertEquals("Segundo", hash.get(1), "El numero de elementos no es correcto");
			assertEquals("Tercero", hash.get(2), "El numero de elementos no es correcto");
			assertEquals("Cuarto", hash.get(3), "El numero de elementos no es correcto");
			assertEquals("Quinto", hash.get(4), "El numero de elementos no es correcto");
		} 
		catch (Exception e) 
		{
			fail("No deberia mandar exception");
		}
	}


	@Test
	public void testPut() 
	{
		try 
		{
			setUpScenario1();
			hash.put(0, "Primero");
			assertEquals(1, hash.size() , "No se agrego correctamente");
			assertEquals("Primero", hash.get(0), "No se agrego correctamente");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testDelete() 
	{
		try 
		{
			setUpScenario2();
			ArrayList<String> list = new ArrayList<>();
			String temp = null;

			for(int i = 0; i< 5;i++)
			{
				temp = hash.delete(i);
				list.add(temp);
				System.out.println(list.get(i));
			}

			assertEquals(5, list.size(), "No se estan sacando los elementos");

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void testIsEmpty()
	{
		setUpScenario1();
		assertTrue(hash.isEmpty(), "La lista esta vacia");

		try 
		{
			setUpScenario2();
			assertFalse(hash.isEmpty(), "La lista no esta vacia");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}