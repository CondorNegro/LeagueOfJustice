/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Monitor.Cronometro;


public class testCronometro {
	private Cronometro chronos;
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
		chronos=new Cronometro();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link Monitor.Cronometro#Cronometro()}.
	 */
	@Test
	public void testCronometro() {
		assertEquals(chronos.getContador(), 0);
	}

	/**
	 * Test method for {@link Monitor.Cronometro#setNuevoTimeStamp()}.
	 */
	@Test
	public void testSetNuevoTimeStamp() {
		chronos.setNuevoTimeStamp();
		assertFalse(chronos.getContador()==0);
	}

	/**
	 * Test method for {@link Monitor.Cronometro#resetearContador()}.
	 */
	@Test
	public void testResetearContador() {
		chronos.resetearContador();
		assertEquals(chronos.getContador(),-1);
	}

	/**
	 * Test method for {@link Monitor.Cronometro#getMillis()}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testGetMillis() throws InterruptedException {
		chronos.setNuevoTimeStamp();
		TimeUnit.SECONDS.sleep(3);
		assertFalse(chronos.getMillis()==0);
		assertEquals(chronos.getSeconds(),3);
	}

	/**
	 * Test method for {@link Monitor.Cronometro#getSeconds()}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testGetSeconds() throws InterruptedException {
		chronos.setNuevoTimeStamp();
		TimeUnit.SECONDS.sleep(2);
		assertEquals(chronos.getSeconds(),2);
	}

	/**
	 * Test method for {@link Monitor.Cronometro#getContador()}.
	 */
	@Test
	public void testGetContador() {
		chronos.setNuevoTimeStamp();
		assertFalse(chronos.getContador()==0);
		chronos.resetearContador();
		assertEquals(chronos.getContador(),-1);
	}

}
