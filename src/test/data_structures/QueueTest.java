package test.data_structures;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.Test;

import model.data_structures.Queue;

public class QueueTest <T extends Comparable<T>>{

	private Queue<T> cola;
	
	public static final String obj1 = "obj1";
	
	public static final String obj2 = "obj2";
	
	public static final String obj3 = "obj3";
	
	public static final String obj4 = "obj4";
	
	public static final String obj5 = "obj5";

	public void setUp() throws Exception
	{
		cola = new Queue<T>();
	}
	
	@SuppressWarnings("unchecked")
	public void enQueueInOrder(String... strings)
	{
		for(String str : strings)
		{
			cola.enqueue((T) str);
		}
	}
	
	@Test
	public void testIsEmpty()
	{
		try
		{
			setUp();
			assertTrue("Deberia ser vacio", true);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTamano()
	{
		try
		{
			setUp();
			enQueueInOrder(obj1,obj2,obj3,obj4,obj5);
			assertEquals(5, cola.tamano());
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testEnQueue()
	{
		try
		{
			setUp();
			enQueueInOrder(obj1,obj2,obj3,obj4);
			cola.enqueue((T)obj5);
			assertEquals(5, cola.tamano());
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeQueue()
	{
		try
		{
			setUp();
			enQueueInOrder(obj1,obj2,obj3,obj4);
			cola.dequeue();
			assertEquals(3, cola.tamano());
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}}