/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Monitor.LogicaTemporal;
import Monitor.Monitor;
import jxl.Workbook;

public class testLogicaTemporal {
	private LogicaTemporal time;
	private String redExcel1="./RedesParaTest/RedTestIntervalosAlfaBeta/RedTest1.xls"; //Path para Linux.
	private String redExcel2="./RedesParaTest/RedTestIntervalosAlfaBeta/RedTest2.xls"; //Path para Linux.
	
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
		 if((System.getProperty("os.name")).equals("Windows 10")){
			 if(System.getProperty("user.name").equals("kzAx")){
				 this.redExcel1="..\\src\\RedesParaTest\\RedTestIntervalosAlfaBeta\\RedTest1.xls";
				 this.redExcel2="..\\src\\RedesParaTest\\RedTestIntervalosAlfaBeta\\RedTest2.xls";
		     }	
			 else{
				this.redExcel1="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTestIntervalosAlfaBeta\\RedTest1.xls";
				this.redExcel2="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTestIntervalosAlfaBeta\\RedTest2.xls";
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
	 * Test method for {@link Monitor.LogicaTemporal#LogicaTemporal(int)}.
	 */
	@Test
	public void testLogicaTemporal() {
		
		Method getCantidadDeTransiciones;
		Object cantidad=null;
		try{
			getCantidadDeTransiciones= LogicaTemporal.class.getDeclaredMethod("getCantidadDeTransiciones", null);
			getCantidadDeTransiciones.setAccessible(true);
			cantidad= getCantidadDeTransiciones.invoke(time);
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
		time.setVectorIntervalosFromExcel(this.redExcel1);
		assertEquals(time.getVectorDeIntervalos()[0][0], 0);
		assertEquals(time.getVectorDeIntervalos()[0][1], -1);
		assertEquals(time.getVectorDeIntervalos()[9][0], 9);
		assertEquals(time.getVectorDeIntervalos()[9][1], -1);
		assertEquals(time.getVectorDeIntervalos().length,10);
		
	}
	
	
	/**
	 * Test method for {@link Monitor.LogicaTemporal#construirVectorTransicionesInmediatas}.
	 */
	@Test
	public void testConstruirVectorTransicionesInmediatas() {
		time.setVectorIntervalosFromExcel(this.redExcel1);
		for(int i=0; i<10; i++){
			
			if(i!=3 & i!=0){
				if(time.construirVectorTransicionesInmediatas()[i] != 0){
					
					System.out.println(i);
					System.out.println(time.construirVectorTransicionesInmediatas()[i]);
					assertEquals(time.construirVectorTransicionesInmediatas()[i], 0);
				}
				
			}
			else{
				if(time.construirVectorTransicionesInmediatas()[i] != 1){
					System.out.println(i);
					System.out.println(time.construirVectorTransicionesInmediatas()[i]);
					assertEquals(time.construirVectorTransicionesInmediatas()[i], 1);
				}
				
			}
			
			
		}
		
	
		
	}
	

	

}
