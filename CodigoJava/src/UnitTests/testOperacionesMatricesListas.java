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

import Monitor.OperacionesMatricesListas;


public class testOperacionesMatricesListas {
	int[] list1Test=new int[4];
	int[] list2Test=new int[4];
	int[][] a = { { 1, 2, -3 }, { 4, 0, -2 } };
    int[][] b = { { 2, 2, 3 }, { 1, -1, -6 } };
    
    int[][] x = { { 1, 2, -3 }, { 4, 0, -2 } };
    int[][] z = { { 3, 1 }, { 2, 4 }, { -1, 5 } };
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
		list1Test[0]=1;
		list1Test[1]=0;
		list1Test[2]=0;
		list1Test[3]=1;
		
		list2Test[0]=1;
		list2Test[1]=0;
		list2Test[2]=0;
		list2Test[3]=1;

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	

	/**
	 * Test method for {@link Monitor.OperacionesMatricesListas#andVector(java.util.List, java.util.List)}.
	 */
	@Test
	public void testAndVector() {
		for(int i=0;i<4;i++){
			assertEquals(OperacionesMatricesListas.andVector(list1Test, list1Test)[i],list1Test[i]); //compruebo que 1001 and 1001 da 1001 
			list2Test[0]=0; //seteo un 0 en la posicion 0, quedando 0001
			assertEquals(OperacionesMatricesListas.andVector(list1Test, list2Test)[i],list2Test[i]); //compruebo que 1001 and 0001 da 0001
			list2Test[3]=0; //seteo un 0 en la posicion 0, quedando 0000
			list1Test[0]=1; //seteo un 1 en la posicion 1, quedando 1101
			list1Test[2]=0; //seteo un 1 en la posicion 1, quedando 1111
			assertEquals(OperacionesMatricesListas.andVector(list1Test, list2Test)[i],list2Test[i]); //compruebo que 1111 and 0000 da 0000
	
		}
	}	
	
	@Test (expected=IndexOutOfBoundsException.class) public void testAndVectorException() {
		int[] list1Test=new int[5];
		assertEquals(OperacionesMatricesListas.andVector(list1Test, list2Test),list2Test); // deberia arrojar IndexOutOfBoundsException
																	      //porque list1Test tiene un elemento de mas
	}

	/**
	 * Test method for {@link Monitor.OperacionesMatricesListas#isNotAllZeros(java.util.List)}.
	 */
	@Test
	public void testIsNotAllZeros() {
		assertEquals(OperacionesMatricesListas.isNotAllZeros(list1Test), true); // pruebo que no todos los elementos de list1Test sean 0
		int[] list3Test = new int[4];
		list3Test[0]=0;
		list3Test[1]=0;
		list3Test[2]=0;
		assertTrue(OperacionesMatricesListas.isNotAllZeros(list3Test)==false); //en list3Test, todos los elementos son 0, debe dar false
		list3Test[3]=1;
		assertTrue(OperacionesMatricesListas.isNotAllZeros(list3Test)==true); //ahora debe dar true
	}

	/**
	 * Test method for {@link Monitor.OperacionesMatricesListas#sumaMatrices(int[][], int[][])}.
	 */
	@Test
	public void testSumaMatrices() {
		int[][] c = { { 3, 4, 0 }, { 5, -1, -8 } };
		assertEquals(OperacionesMatricesListas.sumaMatrices(a, b),c);
		assertEquals(OperacionesMatricesListas.sumaMatrices(b, a),c);
	}
	
	/**
	 * Test method for {@link Monitor.OperacionesMatricesListas#sumaMatrices(int[][], int[][])}.
	 */
	@Test
	public void testRestaMatrices() {
		int[][] c = { { -1,0,-6 }, { 3,1,4 } };
		assertEquals(OperacionesMatricesListas.restaMatrices(a, b),c);
		assertNotEquals(OperacionesMatricesListas.restaMatrices(b, a),c);
	}

	
	/**
	 * Test method for {@link Monitor.OperacionesMatricesListas#productoMatrices(int[][], int[][])}.
	 */
	@Test
	public void testProductoMatrices() {
		int[][] c = { { 10,-6 }, { 14,-6 } };
		assertEquals(OperacionesMatricesListas.productoMatrices(x, z),c);
	}
	
	/**
	 * Test method for {@link Monitor.OperacionesMatricesListas#transpuesta(int[][])}.
	 */
	@Test
	public void testTranspuesta() {
		int[][] c = { { 1,4},{2, 0 }, {-3,-2 } };
		assertEquals(OperacionesMatricesListas.transpuesta(a),c);
		
	}

}
