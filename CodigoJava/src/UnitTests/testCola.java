/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class testCola {
	private Monitor.Cola cola;
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
		cola=new Monitor.Cola();
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
	 * Test method for {@link Monitor.Cola#delay()} and for {@link Monitor.Cola#resume()}.
	 */
	@Test
	public void testDelayAndTestResume() {
		assert(cola.isEmpty());
		threadDelay.start();
		try{
			Thread.sleep(3);
		}
		catch(InterruptedException e){
			fail("Se generó error por interrupción de thread");
		}
		assert(!cola.isEmpty());
		assert(!hiloDelay.getFlag());
		assert(!hiloResume.getFlag());
		threadResume.start();
		
		try{
			threadResume.join();
			threadDelay.join();
		}
		catch(InterruptedException e){
			fail("Se generó error por interrupción de thread");
		}
		
		assert(hiloDelay.getFlag());
		assert(hiloResume.getFlag());
		assert(cola.isEmpty());
		threadDelay.interrupt();
		threadResume.interrupt();
	}

	

	/**
	 * Test method for {@link Monitor.Cola#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		assert(cola.isEmpty());
	}

}
