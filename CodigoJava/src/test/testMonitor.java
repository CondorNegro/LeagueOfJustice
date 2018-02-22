/**
 * 
 */
package test;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import Monitor.Cola;
import Monitor.Monitor;
import Monitor.Politica;
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
			if(System.getProperty("user.name").equals("kzAx")){
				this.redExcel1="..\\src\\RedesParaTest\\RedTest1\\testExcel.xls";
				this.redExcel2="..\\src\\RedesParaTest\\RedTest2\\testExcel5.xls";
			}
			 else{
				 this.redExcel1="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTest1\\testExcel.xls";
				 this.redExcel2="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTest2\\testExcel5.xls";
			 }
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
	 * @throws NoSuchFieldException 
	 */
	@Test
	public void testGetInstance() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		this.monitor1.setPolitica(1); //creo dos instancias de monitor y seteo politicas diferentes
		this.monitor2.setPolitica(2); //quiero probar que se crea un solo monitor, con la ultima politica definida
		Field field1 = monitor1.getClass().getDeclaredField("politica");
		field1.setAccessible(true);
		Politica testPrivateReflection1 = (Politica)field1.get(monitor1);	
		
		Field field2 = monitor2.getClass().getDeclaredField("politica");
		field2.setAccessible(true);
		Politica testPrivateReflection2 = (Politica)field2.get(monitor2);	
		
		assertFalse((int)testPrivateReflection1.getModo()==1);
		assertTrue((int)testPrivateReflection2.getModo()==2);   //monitor1 y monitor2 en realidad
								  		// son las mismas intancias
	}

	/**
	 * Test method for {@link Monitor.Monitor#configRdp(java.lang.String)}.
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 */
	@Test
	public void testConfigRdp() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		this.monitor1.configRdp(this.redExcel1);
		
		
		Field f = monitor1.getClass().getDeclaredField("rdp");
		f.setAccessible(true);
		RedDePetri testPrivateReflection = (RedDePetri)f.get(monitor1);
		
		
		//Object[] parameters = new Object[1];
		//parameters[0] = "A String parameter";
		
		assertEquals(testPrivateReflection.getCantTransiciones(), 4); //monitor1 y monitor2 en realidad
	}

	/**
	 * Test method for {@link Monitor.Monitor#setPolitica(int)}.
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws NoSuchFieldException 
	 */
	@Test
	public void testSetPolitica() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		this.monitor1.setPolitica(1);
		
		Field f = monitor1.getClass().getDeclaredField("politica");
		f.setAccessible(true);
		Politica testPrivateReflection = (Politica)f.get(monitor1);		
		
		assertFalse((int)testPrivateReflection.getModo()==2);
		assertTrue((int)testPrivateReflection.getModo()==1);
		this.monitor1.setPolitica(2);
		Politica testPrivateReflection2 = (Politica)f.get(monitor1);	
		assertEquals((int)testPrivateReflection2.getModo(), 2);
	}

	/**
	 * Test method for {@link Monitor.Monitor#quienesEstanEnColas()}.
	 */
	/*
	@Test
	public void testQuienesEstanEnColas() {
		this.monitor1.configRdp(this.redExcel1);
		
		
		try {
			Method quienesEstanEnColas;
			
			quienesEstanEnColas = Monitor.class.getDeclaredMethod("quienesEstanEnColas", null);
			quienesEstanEnColas.setAccessible(true);
					
			
			
			Field f = monitor1.getClass().getDeclaredField("colas");
			f.setAccessible(true);
			Cola[] testPrivateReflection = (Cola[])f.get(monitor1);
			
			
		
			
			Object cola = testPrivateReflection[0];
			
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
*/
	
	/**
	 * Test method for {@link Monitor.Monitor#dispararTransicion(int)}.
	 */
/*
	@Test
	public void testDispararTransicion() {
		this.monitor1.setPolitica(0);
		this.monitor1.configRdp(this.redExcel1);
		
		HiloTransicionesCero hiloCero=new HiloTransicionesCero(this.monitor1);
		HiloTransicionesUno hiloUno=new HiloTransicionesUno(this.monitor1);
		Thread threadTCero=new Thread(hiloCero);
		Thread threadTUno=new Thread(hiloUno);
		
		try {
			
			Field f = monitor1.getClass().getDeclaredField("rdp");
			f.setAccessible(true);
			RedDePetri testPrivateReflection = (RedDePetri)f.get(monitor1);		
		
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
			
			
			
			assertEquals(testPrivateReflection.getMatrizM(),m0);
			
			threadTCero.start();
			threadTCero.join();
			assertEquals(testPrivateReflection.getMatrizM(),m1);
			threadTUno.join();
			
			try{
				Thread.sleep(3);
			}
			catch(InterruptedException e){
				fail("Se genero error por interrupcion de thread");
			}
			
			assertEquals(testPrivateReflection.getMatrizM(),m2);
			
		} 
		catch (Exception e){
			
				e.printStackTrace();
				System.out.println("Error en testDispararTransicion");
				fail("Error en testDispararTransicion");
		}
		
	}
	
	*/

}
