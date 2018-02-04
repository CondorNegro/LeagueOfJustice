/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.Arrays;



import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Monitor.OperacionesMatricesListas;
import Monitor.RedDePetri;


public class testRedDePetri {
	private RedDePetri redTest;
	private ArrayList<Integer> transicionesSensibilizadasTest = new ArrayList<>();
	private String redExcel1="./RedesParaTest/testExcel.xls"; //Path para Linux.
	private String redExcel2="./RedesParaTest/testExcel5.xls"; //Path para Linux.
	
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
		if((System.getProperty("os.name")).equals("Windows 10")){
			redExcel1="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\testExcel.xls";
			redExcel2="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\testExcel5.xls";
			
		}
		transicionesSensibilizadasTest.add(1);
		transicionesSensibilizadasTest.add(0);
		transicionesSensibilizadasTest.add(0);
		transicionesSensibilizadasTest.add(0);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link Monitor.RedDePetri#RedDePetri(java.lang.String)}.
	 */
	@Test
	public void testRedDePetri() {
		redTest= new RedDePetri(this.redExcel1);
		assertEquals(redTest.getCantTransiciones(), 4);
	}

	/**
	 * Test method for {@link Monitor.RedDePetri#getCantTransiciones()}.
	 */
	@Test
	public void testGetCantTransiciones() {
		redTest= new RedDePetri(this.redExcel1);
		assertEquals(redTest.getCantTransiciones(), 4);
		redTest= new RedDePetri(this.redExcel2);
		assertTrue(redTest.getCantTransiciones()==5);
		assertFalse(redTest.getCantTransiciones()==4);
	}

	/**
	 * Test method for {@link Monitor.RedDePetri#getSensibilizadas()}.
	 */
	@Test
	public void testGetSensibilizadas() {
		redTest= new RedDePetri(this.redExcel1);
		assertEquals(redTest.getSensibilizadas(), transicionesSensibilizadasTest);
	}

	/**
	 * Test method for {@link Monitor.RedDePetri#disparar(int)}.
	 */
	@Test
	public void testDisparar() {
		redTest= new RedDePetri(this.redExcel1);
		//System.out.println(redTest.esDisparoValido(redTest.getMarcadoSiguiente(0)));
		assertEquals(redTest.getSensibilizadas(),transicionesSensibilizadasTest);
		redTest.disparar(0);
		transicionesSensibilizadasTest.set(0, 0);
		transicionesSensibilizadasTest.set(1, 1);
		assertEquals(redTest.getSensibilizadas(),transicionesSensibilizadasTest);
	}

	/**
	 * Test method for {@link Monitor.RedDePetri#getMarcadoSiguiente(int)}.
	 */
	@Test
	public void testGetMarcadoSiguiente() {
		int[][] a = { {0}, {2}, {0}, {0} };
		int[][] b = { {2}, {0}, {0}, {-2} };
		redTest= new RedDePetri(this.redExcel1);
		assertEquals(redTest.getMarcadoSiguiente(0), a);
		redTest= new RedDePetri(this.redExcel2);
		//System.out.println(Arrays.deepToString(redTest.getMarcadoSiguiente(4)));
		assertEquals(redTest.getMarcadoSiguiente(4),b);	
	}
	
	@Test (expected=IllegalArgumentException.class) public void testGetMarcadoSiguienteException() {
		int[][] b = { {2}, {0}, {0}, {-2} };
		redTest= new RedDePetri(this.redExcel1);
		assertEquals(redTest.getMarcadoSiguiente(4),b);
	}

	/**
	 * Test method for {@link Monitor.RedDePetri#esDisparoValido(int[][])}.
	 */
	@Test
	public void testEsDisparoValido() {
		redTest= new RedDePetri(this.redExcel1);
		//System.out.println(redTest.esDisparoValido(redTest.getMarcadoSiguiente(0)));
		assertEquals(redTest.esDisparoValido(redTest.getMarcadoSiguiente(0)),true);
		assertEquals(redTest.esDisparoValido(redTest.getMarcadoSiguiente(1)),false);
		assertEquals(redTest.esDisparoValido(redTest.getMarcadoSiguiente(2)),false);
		assertEquals(redTest.esDisparoValido(redTest.getMarcadoSiguiente(3)),false);
		redTest.disparar(0);
		assertEquals(redTest.esDisparoValido(redTest.getMarcadoSiguiente(0)),false);
		assertEquals(redTest.esDisparoValido(redTest.getMarcadoSiguiente(1)),true);
		assertEquals(redTest.esDisparoValido(redTest.getMarcadoSiguiente(2)),false);
		assertEquals(redTest.esDisparoValido(redTest.getMarcadoSiguiente(3)),false);
	}
	
	@Test (expected=NullPointerException.class) public void testEsDisparoValidoException() {
		int[][] b = { {2}, {0}, {0}, {-2} };
		redTest= new RedDePetri(this.redExcel1);
		assertEquals(redTest.esDisparoValido(null),b);
	}

}
