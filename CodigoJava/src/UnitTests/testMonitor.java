/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import Monitor.Cola;
import Monitor.Monitor;



public class testMonitor {
	private Monitor monitor1=Monitor.getInstance();
	private Monitor monitor2=Monitor.getInstance();
	ArrayList<Integer> list1Test = new ArrayList<>();
	ArrayList<Integer> list2Test = new ArrayList<>();
	
	private HiloDelay hiloDelay;
	private HiloResume hiloResume;
	private Thread threadDelay;
	private Thread threadResume;
	
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
		
		
		
		this.list1Test.add(1);
		this.list1Test.add(0);
		this.list1Test.add(0);
		this.list1Test.add(1);
		this.list2Test.add(1);
		this.list2Test.add(0);
		this.list2Test.add(0);
		this.list2Test.add(1);
		if((System.getProperty("os.name")).equals("Windows 10")){
			this.redExcel1="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\testExcel.xls";
			this.redExcel2="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\testExcel5.xls";
			
		}

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link Monitor.Monitor#getInstance()}.
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	@Test
	public void testGetInstance() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this.monitor1.setPolitica(1); //creo dos instancias de monitor y seteo politicas diferentes
		this.monitor2.setPolitica(2); //quiero probar que se crea un solo monitor, con la ultima politica definida
		Method getPolitica = Monitor.class.getDeclaredMethod("getPolitica", null);
		getPolitica.setAccessible(true);
		Object Politica = getPolitica.invoke(monitor1);
		assertFalse((int)Politica==1);
		assertTrue((int)Politica==2);   //monitor1 y monitor2 en realidad
								  		// son las mismas intancias
	}

	/**
	 * Test method for {@link Monitor.Monitor#configRdp(java.lang.String)}.
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@Test
	public void testConfigRdp() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.monitor1.configRdp(this.redExcel1);
		Method getNumeroTransiciones = Monitor.class.getDeclaredMethod("getNumeroTransiciones", null);
		getNumeroTransiciones.setAccessible(true);
		//Object[] parameters = new Object[1];
		//parameters[0] = "A String parameter";
		Object cantidadTransiciones = getNumeroTransiciones.invoke(monitor1);
		assertEquals(cantidadTransiciones, 4); //monitor1 y monitor2 en realidad
	}

	/**
	 * Test method for {@link Monitor.Monitor#setPolitica(int)}.
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	@Test
	public void testSetPolitica() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this.monitor1.setPolitica(1);
		Method getPolitica = Monitor.class.getDeclaredMethod("getPolitica", null);
		getPolitica.setAccessible(true);
		Object Politica = getPolitica.invoke(monitor1);
		assertFalse((int)Politica==2);
		assertTrue((int)Politica==1);
		this.monitor1.setPolitica(2);
		Politica = getPolitica.invoke(monitor1);
		assertEquals((int)Politica, 2);
	}

	/**
	 * Test method for {@link Monitor.Monitor#quienesEstanEnColas()}.
	 */
	@Test
	public void testQuienesEstanEnColas() {
		this.monitor1.configRdp(this.redExcel1);
		
		
		try {
			Method quienesEstanEnColas;
		
			quienesEstanEnColas = Monitor.class.getDeclaredMethod("quienesEstanEnColas", null);
			quienesEstanEnColas.setAccessible(true);
					
		
			Method getColaCero;
		
			getColaCero = Monitor.class.getDeclaredMethod("getColaCero", null);
			getColaCero.setAccessible(true);
			Object cola = getColaCero.invoke(monitor1);
			
			hiloDelay=new HiloDelay((Cola) cola);
			hiloResume=new HiloResume((Cola) cola);
			threadDelay=new Thread(hiloDelay);
			threadResume=new Thread(hiloResume);
			
			
			threadDelay.start();
			try{
				Thread.sleep(3);
			}
			catch(InterruptedException e){
				fail("Se generó error por interrupción de thread");
			}
			List<Integer> Lista1 = (List<Integer>) quienesEstanEnColas.invoke(monitor1);
			assertEquals((int)Lista1.get(0),1);
			
			threadResume.start();
			threadResume.join();
			List<Integer> Lista2 = (List<Integer>) quienesEstanEnColas.invoke(monitor1);
			assertEquals((int)Lista2.get(0),0);
			
			} 
		catch (Exception e){
			
				e.printStackTrace();
				System.out.println("Error en testQuienesEstanEnColas");
				fail("Error en testQuienesEstanEnColas");
		}
		
		
		
		
		
		
		
		
		
		
		
	}

	
	/**
	 * Test method for {@link Monitor.Monitor#dispararTransicion(int)}.
	 */
	@Test
	public void testDispararTransicion() {
		fail("Not yet implemented");
	}

}
