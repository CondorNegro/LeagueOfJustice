/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Monitor.LogicaTemporal;
import Monitor.Monitor;

public class testLogicaTemporal {
	private LogicaTemporal time;
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
		 time= new LogicaTemporal(10);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link Monitor.LogicaTemporal#LogicaTemporal(int)}.
	 */
	@Test
	public void testLogicaTemporal() {
		
		Method getCantTransiciones;
		Object cantidad=null;
		try{
			getCantTransiciones= LogicaTemporal.class.getDeclaredMethod("getCantTransiciones", null);
			getCantTransiciones.setAccessible(true);
			cantidad= getCantTransiciones.invoke(time);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		assertEquals(10,cantidad );
	}

	/**
	 * Test method for {@link Monitor.LogicaTemporal#setVectorIntervalosFromExcel(jxl.Workbook)}.
	 */
	@Test
	public void testSetVectorIntervalosFromExcel() {
		
	}

	/**
	 * Test method for {@link Monitor.LogicaTemporal#getVectorDeIntervalos()}.
	 */
	@Test
	public void testGetVectorDeIntervalos() {
		
		
	}

}
