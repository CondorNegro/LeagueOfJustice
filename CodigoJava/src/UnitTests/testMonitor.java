/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Monitor.Monitor;


public class testMonitor {
	private Monitor monitor1=Monitor.getInstance();
	private Monitor monitor2=Monitor.getInstance();
	ArrayList<Integer> list1Test = new ArrayList<>();
	ArrayList<Integer> list2Test = new ArrayList<>();
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		monitor1.setPolitica(1); //creo dos instancias de monitor y seteo politicas diferentes
		monitor2.setPolitica(2); //quiero probar que se crea un solo monitor, con la ultima politica definida
		
		list1Test.add(1);
		list1Test.add(0);
		list1Test.add(0);
		list1Test.add(1);
		list2Test.add(1);
		list2Test.add(0);
		list2Test.add(0);
		list2Test.add(1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link Monitor.Monitor#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		assertFalse(monitor2.getPolitica()==1);
		assertTrue(monitor2.getPolitica()==2);
		assertEquals(monitor2.getPolitica(), monitor1.getPolitica()); //monitor1 y monitor2 en realidad
																	  // son las mismas intancias
	}

	/**
	 * Test method for {@link Monitor.Monitor#configRdp(java.lang.String)}.
	 */
	@Test
	public void testConfigRdp() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link Monitor.Monitor#setPolitica(int)}.
	 */
	@Test
	public void testSetPolitica() {
		monitor1.setPolitica(1);
		assertFalse(monitor1.getPolitica()==2);
		assertTrue(monitor1.getPolitica()==1);
		monitor1.setPolitica(2);
		assertEquals(monitor1.getPolitica(), 2);
	}

	/**
	 * Test method for {@link Monitor.Monitor#quienesEstanEnColas()}.
	 */
	@Test
	public void testQuienesEstanEnColas() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link Monitor.Monitor#andVector(java.util.List, java.util.List)}.
	 */
	@Test
	public void testAndVector() {
		//assertTrue(list1Test==list2Test);
		assertEquals(monitor1.andVector(list1Test, list2Test), list1Test); //compruebo que 1001 and 1001 da 1001 
		list2Test.set(0, 0); //seteo un 0 en la posicion 0, quedando 0001
		assertEquals(monitor1.andVector(list1Test, list2Test),list2Test); //compruebo que 1001 and 0001 da 0001
		list2Test.set(3, 0); //seteo un 0 en la posicion 0, quedando 0000
		list1Test.set(1, 0); //seteo un 1 en la posicion 1, quedando 1101
		list1Test.set(2, 0); //seteo un 1 en la posicion 1, quedando 1111
		assertEquals(monitor1.andVector(list1Test, list2Test),list2Test); //compruebo que 1111 and 0000 da 0000
	}

	@Test (expected=IndexOutOfBoundsException.class) public void testAndVectorException() {
		list1Test.add(1);
		assertEquals(monitor1.andVector(list1Test, list2Test),list2Test); // deberia arrojar IndexOutOfBoundsException
																	      //porque list1Test tiene un elemento de mas
	}
	
	
	/**
	 * Test method for {@link Monitor.Monitor#isNotAllZeros(java.util.List)}.
	 */
	@Test
	public void testIsNotAllZeros() {
		assertEquals(monitor1.isNotAllZeros(list1Test), true); // pruebo que no todos los elementos de list1Test sean 0
		ArrayList<Integer> list3Test = new ArrayList<>();
		list3Test.add(0);
		list3Test.add(0);
		list3Test.add(0);
		assertTrue(monitor1.isNotAllZeros(list3Test)==false); //en list3Test, todos los elementos son 0, debe dar false
		list3Test.add(1);
		assertTrue(monitor1.isNotAllZeros(list3Test)==true); //ahora debe dar true
	}

	/**
	 * Test method for {@link Monitor.Monitor#dispararTransicion(int)}.
	 */
	@Test
	public void testDispararTransicion() {
		fail("Not yet implemented");
	}

}
