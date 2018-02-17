/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Monitor.Monitor;
import Monitor.RedDePetri;
import UnitTests.testMonitorCompleto.HiloEscritor;
import UnitTests.testMonitorCompleto.HiloLector;

public class testTinvariantes {
	 Monitor monitor = Monitor.getInstance();
	 private String redExcel="./RedesParaTest/TinvariantesTest/testExcel.xls"; //Path para Linux.
	 private boolean flagImpresion=false;
	   
	    
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
			 if(System.getProperty("user.name").equals("kzAx")){
				 this.redExcel="D:\\Concurrent repository\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TinvariantesTest\\testExcel.xls";
			 }
			 else{
				 this.redExcel="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TinvariantesTest\\testExcel.xls"; //Path para Windows.
			 }
		}
        monitor.configRdp(redExcel);
        
        monitor.setPolitica(0);	 
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testVerificarTInvariantes() {
		 Thread hilo1 = new Thread(new HiloUno());
	     Thread hilo2 = new Thread(new HiloDos());
	     Thread hilo3 = new Thread(new HiloTres());
	       
	        hilo1.start();
	       
	        hilo2.start();
	     
	        hilo3.start();
	        
	       
	        
	        try{
	        	hilo1.join();
	        	Thread.sleep(1000);
	        	hilo2.interrupt();
	            hilo3.interrupt();
	           
	        }
	        catch(InterruptedException e){
	        	e.printStackTrace();
	        }
	       
	        
	    }
	    
	   

	    
	    

	    

	   
	    class HiloUno implements Runnable{
	    	 
	   	  
	        @Override
	        public void run() {
	        	for(int i=0; i<50;i++){
	        		monitor.dispararTransicion(0);
	        		if(flagImpresion)System.out.println("Disparo T0");
	        	}
	            	
	        }
	    }

	    class HiloDos implements Runnable{
	        @Override
	        public void run() {
	        	
	        	for(int i=0; i<25;i++){
	        		
	            	monitor.dispararTransicion(2);
	            	if(flagImpresion)System.out.println("Disparo T2");
	            	monitor.dispararTransicion(3);
	            	if(flagImpresion)System.out.println("Disparo T3");
	        	}
	            	
	        }
	        
	    }
	    
	    
	    class HiloTres implements Runnable{
	        @Override
	        public void run() {
	        	
	        	for(int i=0; i<25;i++){  
	            	monitor.dispararTransicion(1);
	            	if(flagImpresion)System.out.println("Disparo T1");
	        	}           	
	            	
	        }
	        
	    }
	        	

}
