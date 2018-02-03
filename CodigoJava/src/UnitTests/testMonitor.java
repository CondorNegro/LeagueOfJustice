/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;


import java.util.ArrayList;


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
	 * Test method for {@link Monitor.Monitor#dispararTransicion(int)}.
	 */
	@Test
	public void testDispararTransicion() {
		fail("Not yet implemented");
	}

}
