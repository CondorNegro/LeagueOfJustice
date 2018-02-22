package test;


import Monitor.Monitor;



import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import Monitor.RedDePetri;


public class testMonitorCompleto {
    Monitor monitor = Monitor.getInstance();
    private String redExcel="./RedesParaTest/lectorEscritor/lectorEscritor.xls"; //Path para Linux.
    private static boolean flagEscritoresFull; //Indica que los 50 escritores leyeron.
    private static boolean flagLectorFull;
    
   
    
    @org.junit.Before
    public void setUp() throws Exception {
    	if((System.getProperty("os.name")).equals("Windows 10")){	
    		if(System.getProperty("user.name").equals("kzAx")){
				this.redExcel="..\\src\\RedesParaTest\\lectorEscritor\\lectorEscritor.xls";
				
			}
			 else{
				 this.redExcel="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\lectorEscritor\\lectorEscritor.xls"; 
			 }
    	}
        monitor.configRdp(redExcel);
        monitor.setPolitica(0); //modo aleatorio
        flagEscritoresFull=false;
        this.flagLectorFull=false;
        
    }

    @org.junit.After
    public void tearDown() throws Exception {
        int[][] m= monitor.getMarcado();
        System.out.println("");
        //assert (m[0][0]==1||m[1][0]==1||m[2][0]==1);  //control del marcado final

    }

    @org.junit.Test
    public void dispararTransicionTest() throws Exception {
        Thread hilo1= new Thread(new HiloEscritor());
        Thread hilo2 = new Thread(new HiloLector());
     
       
        hilo1.start();
       
        hilo2.start();
     
        
        //while(hilo1.getState()!= Thread.State.TERMINATED && hilo2.getState()!= Thread.State.TERMINATED && hilo3.getState()!= Thread.State.TERMINATED) {

       // }
        hilo1.join();
        
        try{
        	Thread.sleep(1000);
        	hilo2.stop();
          
           
        }
        catch(InterruptedException e){
        	e.printStackTrace();
        }
        finally{
        	assert(flagEscritoresFull); //Se lleno plaza 9
        	assert(flagLectorFull);
            
        }
        
        
        
    }
    
    public static synchronized void setBoolean(){
    	flagEscritoresFull=true;
    	flagLectorFull=true;
    }

   

    
    

    

   
    class HiloEscritor implements Runnable{
    	 
   	  
        @Override
        public void run() {
        	int contador=0;
        	//int[][] m= monitor.getMarcado();
      	  
        	while(monitor.getMarcado()[6][0]<25 | monitor.getMarcado()[7][0]<25){
            	
            	contador++;
            	System.out.println("Escribiendo: " + contador);
            	
            	monitor.dispararTransicion(4);
            	if(monitor.getMarcado()[2][0]!=0){
            		try{
            			Field f = monitor.getClass().getDeclaredField("rdp");
            			f.setAccessible(true);
            			RedDePetri testPrivateReflection = (RedDePetri)f.get(monitor);
            			assert(testPrivateReflection.getSensibilizadasExtendido()[0]==0);
            		}
            		catch(Exception e){
            			e.printStackTrace();
            		}
            		
            	}
            	monitor.dispararTransicion(1);
            	monitor.dispararTransicion(2);
            	System.out.println("Dejo de escribir: "+ contador);

            }
        	setBoolean();
        }
    }

    class HiloLector implements Runnable{
        @Override
        public void run() {
        	int contador1=0;
          
        	while(monitor.getMarcado()[6][0]<25 | monitor.getMarcado()[7][0]<25){
        	
               
            	contador1++;
            	
            	System.out.print("Lector:");
          	    System.out.println(contador1);
            	monitor.dispararTransicion(0);
            	monitor.dispararTransicion(3);
            	
            	
            	
            }
        	setBoolean();
        	
        }
    }
}

  

