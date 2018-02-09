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
	private String redExcel1="./RedesParaTest/RedTest1/testExcel.xls"; //Path para Linux.
	private String redExcel2="./RedesParaTest/RedTest2/testExcel5.xls"; //Path para Linux.
	private  Workbook archivoExcelMatrices;
	private Workbook archivoExcelMatrices1;
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
				this.redExcel1="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTestIntervalosAlfaBeta\\RedTest1.xls";
				this.redExcel2="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTestIntervalosAlfaBeta\\RedTest2.xls";
				
		}
		File file1 = new File(this.redExcel1);
		File file2= new File(this.redExcel2);
	   
	    archivoExcelMatrices = Workbook.getWorkbook(file1);
	    archivoExcelMatrices1 = Workbook.getWorkbook(file2);
		 
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
		
		Method getCantTransiciones;
		Object cantidad=null;
		try{
			getCantTransiciones= LogicaTemporal.class.getDeclaredMethod("getCantTransiciones", null);
			getCantTransiciones.setAccessible(true);
			cantidad= getCantTransiciones.invoke(time);
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
		time.setVectorIntervalosFromExcel(archivoExcelMatrices);
		assertEquals(time.getVectorDeIntervalos()[0][0], 0);
		assertEquals(time.getVectorDeIntervalos()[0][1], -1);
		assertEquals(time.getVectorDeIntervalos()[9][0], 9);
		assertEquals(time.getVectorDeIntervalos()[9][1], -1);
		
	}
	
	

	

}
