/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import monitor.LogicaTemporal;


public class testLogicaTemporal {
	/**
	 * Atributos privados del unit test LogicaTemporal
	 */
	private LogicaTemporal time;
	private String redExcel1="./RedesParaTest/RedTestIntervalosAlfaBeta/RedTest1.xls"; //Path para Linux.
	
	
	/**
	 * Antes de cualquier prueba, se setean los paths de la red en cuestion, segun cada usuario y sistema operativo.
	 */
	@Before
	public void setUp() throws Exception {
		 time= new LogicaTemporal(10);
		 if((System.getProperty("os.name")).equals("Windows 10")){
			 if(System.getProperty("user.name").equals("kzAx")){this.redExcel1="..\\src\\RedesParaTest\\RedTestIntervalosAlfaBeta\\RedTest1.xls";}	
			 else{this.redExcel1="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedTestIntervalosAlfaBeta\\RedTest1.xls";}	
		} 
	}


	/**
	 * Test method for {@link monitor.LogicaTemporal#LogicaTemporal(int)}.
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testLogicaTemporal() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		//Como el atributo cantidad_de_transiciones es un atributo privado de la clase LogicaTemporal
		//obtengo el mismo a traves de la tecnica conocida como "Reflection"
		Field f2 = time.getClass().getDeclaredField("cantidad_de_transiciones");
		f2.setAccessible(true);
		int testPrivateReflection2 = (int)f2.get(time);
		
		//Pruebo de que la red a testear efectivamente contenga las 10 transiciones.
		assertEquals(10,testPrivateReflection2);
	}

	/**
	 * Test method for {@link monitor.LogicaTemporal#setVectorIntervalosFromExcel(jxl.Workbook)}.
	 */
	@Test
	public void testSetVectorIntervalosFromExcel() {
		//Verificamos que se carguen correctamente los intervalos desde el metodo setVectorIntervalosFromExcel
		time.setVectorIntervalosFromExcel(this.redExcel1);
		assertEquals(time.getVectorDeIntervalos()[0][0], 0);  //la transicion 0 debe ser inmediata
		assertEquals(time.getVectorDeIntervalos()[0][1], -1); //se comprueba que alfa=0 y beta=-1
		assertEquals(time.getVectorDeIntervalos()[9][0], 9);  //la transicion 9 debe ser temporal
		assertEquals(time.getVectorDeIntervalos()[9][1], -1); //se comprueba que alfa=9 y beta=-1
		
		assertEquals(time.getVectorDeIntervalos().length,10); //se comprueba ademas, que el vector de intervalos tenga una longitud igual a 10 (por cantidad de transiciones)
		
	}
	
	
	/**
	 * Test method for {@link monitor.LogicaTemporal#construirVectorTransicionesInmediatas}.
	 */
	@Test
	public void testConstruirVectorTransicionesInmediatas() {
		//Probamos el metodo contruir vector de transiciones inmediatas,
		//el cual devuelve una lista donde sus elementos pueden ser 1 si la transicion en cuestion es inmediata, sino 0.
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
