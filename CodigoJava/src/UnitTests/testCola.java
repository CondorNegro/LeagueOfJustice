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

/**
 * @author Gastón
 *
 */
public class testCola {
	private Monitor.Cola cola;
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
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link Monitor.Cola#Cola()}.
	 */
	@Test
	public void testCola() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link Monitor.Cola#resume()}.
	 */
	@Test
	public void testResume() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link Monitor.Cola#delay()}.
	 */
	@Test
	public void testDelay() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link Monitor.Cola#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		assert(cola.isEmpty());
	}

}
