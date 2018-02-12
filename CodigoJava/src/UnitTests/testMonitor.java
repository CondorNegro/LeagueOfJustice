/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import Monitor.RedDePetri;



public class testMonitor {
	private Monitor monitor1=Monitor.getInstance();
	private Monitor monitor2=Monitor.getInstance();
	int[] list1Test;
	int[] list2Test;
	
	private HiloDelay hiloDelay;
	private HiloResume hiloResume;
	private Thread threadDelay;
	private Thread threadResume;
	
	private String redExcel1="./RedesParaTest/RedTest1/testExcel.xls"; //Path para Linux.
	private String redExcel2="./RedesParaTest/RedTest2/testExcel5.xls"; //Path para Linux.
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
		
		
		this.list1Test=new int[4];
		this.list2Test=new int[4];
		this.list1Test[0]=1;
		this.list1Test[1]=0;
		this.list1Test[2]=0;
		this.list1Test[3]=1;
		this.list2Test[0]=1;
		this.list2Test[1]=0;
		this.list2Test[2]=0;
		this.list2Test[3]=1;
		if((System.getProperty("os.name")).equals("Windows 10")){
			this.redExcel1="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTest1\\testExcel.xls";
			this.redExcel2="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTest2\\testExcel5.xls";
			
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
		Method getRDP = Monitor.class.getDeclaredMethod("getRDP", null);
		getRDP.setAccessible(true);
		//Object[] parameters = new Object[1];
		//parameters[0] = "A String parameter";
		RedDePetri cantidadTransiciones = (RedDePetri) getRDP.invoke(monitor1);
		assertEquals(cantidadTransiciones.getCantTransiciones(), 4); //monitor1 y monitor2 en realidad
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
				TimeUnit.MILLISECONDS.sleep(20);
			}
			catch(InterruptedException e){
				fail("Se genero error por interrupcion de thread");
			}
			int[] Lista1 = (int[]) quienesEstanEnColas.invoke(monitor1);
			assertEquals((int)Lista1[0],1);
			
			threadResume.start();
			threadResume.join();
			try{
				TimeUnit.MILLISECONDS.sleep(20);
			}
			catch(InterruptedException e){
				fail("Se genero error por interrupcion de thread");
			}
			int[] Lista2 = (int[]) quienesEstanEnColas.invoke(monitor1);
			assertEquals((int)Lista2[0],0);
			
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
		this.monitor1.setPolitica(0);
		this.monitor1.configRdp(this.redExcel1);
		
		HiloTransicionesCero hiloCero=new HiloTransicionesCero(this.monitor1);
		HiloTransicionesUno hiloUno=new HiloTransicionesUno(this.monitor1);
		Thread threadTCero=new Thread(hiloCero);
		Thread threadTUno=new Thread(hiloUno);
		
		try {
			Method getRDP;
		
			getRDP = Monitor.class.getDeclaredMethod("getRDP", null);
			getRDP.setAccessible(true);
			RedDePetri rdp = (RedDePetri) getRDP.invoke(monitor1);		
		
			int[][] m0={{2},{0},{0},{0}};
			int[][] m1={{0},{2},{0},{0}};
			int[][] m2={{0},{0},{2},{0}};
			
			threadTUno.start();
			try{
				Thread.sleep(3);
			}
			catch(InterruptedException e){
				fail("Se genero error por interrupcion de thread");
			}
			
			
			
			assertEquals(rdp.getMatrizM(),m0);
			threadTCero.start();
			threadTCero.join();
			assertEquals(rdp.getMatrizM(),m1);
			threadTUno.join();
			
			try{
				Thread.sleep(3);
			}
			catch(InterruptedException e){
				fail("Se gener� error por interrupci�n de thread");
			}
			RedDePetri rdp1 = (RedDePetri) getRDP.invoke(monitor1);	
			assertEquals(rdp1.getMatrizM(),m2);
			
		} 
		catch (Exception e){
			
				e.printStackTrace();
				System.out.println("Error en testDispararTransicion");
				fail("Error en testDispararTransicion");
		}
		
	}
	
	@Test
	public void itWorksRepeatably() {
	    for (int i = 0; i < 20; i++) {
	    	testQuienesEstanEnColas();
	    	testDispararTransicion();
	    }
	}

}
