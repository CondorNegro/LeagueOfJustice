
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class testCola {
	private monitor.Cola cola;
	private HiloDelay hiloDelay;
	private HiloResume hiloResume;
	private Thread threadDelay;
	private Thread threadResume;
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
		cola=new monitor.Cola();
		hiloDelay=new HiloDelay(cola);
		hiloResume=new HiloResume(cola);
		threadDelay=new Thread(hiloDelay);
		threadResume=new Thread(hiloResume);
		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	
	/**
	 * Test method for {@link monitor.Cola#delay()} and for {@link monitor.Cola#resume()}.
	 */
	@Test
	public void testDelayAndTestResume() {
		assert(cola.isEmpty());
		threadDelay.start(); //El hilo se duerme
		try{
			Thread.sleep(3);
		}
		catch(InterruptedException e){
			fail("Se genero error por interrupcion de thread");
		}
		assert(!cola.isEmpty()); //Cola debe tener un hilo dormido
		assert(!hiloDelay.getFlag());
		assert(!hiloResume.getFlag());
		threadResume.start(); //Envio un hilo a despertarlo
		
		try{
			threadResume.join();
			threadDelay.join();
		}
		catch(InterruptedException e){
			fail("Se genero error por interrupci�n de thread");
		}
		
		assert(hiloDelay.getFlag());
		assert(hiloResume.getFlag());
		assert(cola.isEmpty()); //La cola debe quedar sin ningun hilo
		threadDelay.interrupt();
		threadResume.interrupt();
	}

	

	/**
	 * Test method for {@link monitor.Cola#isEmpty()}.
	 * 
	 */
	@Test
	public void testIsEmpty() {
		assert(cola.isEmpty());
	}

}
