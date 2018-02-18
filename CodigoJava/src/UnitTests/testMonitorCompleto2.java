package UnitTests;


import Monitor.Monitor;



import static org.junit.Assert.*;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class testMonitorCompleto2 {
    Monitor monitor = Monitor.getInstance();
    private String redExcel="./RedesParaTest/RedPrioridades/lectorEscritor.xls"; //Path para Linux.
    int flagPolitica=2;
    int cuentas1=0;
    int cuentas2=0;
    Logger logger = Logger.getLogger("MyLog");  
    FileHandler fh;  
    int[][] marca;
    
    @org.junit.Before
    public void setUp() throws Exception {
    	if((System.getProperty("os.name")).equals("Windows 10")){	
    		if(System.getProperty("user.name").equals("kzAx")){
				this.redExcel="..\\src\\RedesParaTest\\RedPrioridades\\lectorEscritor.xls";
				
			}
			 else{
				 this.redExcel="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedPrioridades\\lectorEscritor.xls"; //Path para Windows.
			 }
    	}
        monitor.configRdp(redExcel);
        
        monitor.setPolitica(flagPolitica);	 // 0-modo aleatorio
							     // 1-prioridad al proceso 1
							     // 2-prioridad al proceso 2
        String nombreArchivo="./MyLogFile.log";
        if((System.getProperty("os.name")).equals("Windows 10")){	
        	nombreArchivo=".\\MyLogFile.log";
        }
        this.fh=new FileHandler(nombreArchivo);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  
        
    }

    

    @org.junit.Test
    public void dispararTransicionTest() throws Exception {
        Thread hilo1 = new Thread(new HiloGenerador());
        Thread hilo2= new Thread(new HiloProceso1());
        Thread hilo3 = new Thread(new HiloProceso2());
        Thread hilo4= new Thread(new HiloControlProceso1());
        Thread hilo5= new Thread(new HiloControlProceso2());
        Thread hilo6= new Thread(new HiloFinalizo());
       
        hilo6.start();
        hilo3.start();
        hilo4.start();
        hilo1.start();
        hilo2.start();
        hilo5.start();
       
        

        
        hilo1.join();
        
        try{
        	Thread.sleep(10);
        	hilo2.stop();
            hilo3.stop();
            hilo4.stop();
            hilo5.stop();
            hilo6.stop();
           
        }
        catch(InterruptedException e){
        	e.printStackTrace();
        }
        
        finally{
        	
        	assert(monitor.getMarcado()[6][0]>=24); //Se generaron todos los recursos
        	if(flagPolitica==1) {
        		assert(cuentas1>=24); //Se generaron todos los recursos
        	}
        	if(flagPolitica==2) {
        		assert(cuentas2>=24); //Se generaron todos los recursos
        	}
        	if(flagPolitica==0) {
        		assert(cuentas2!=0&&cuentas1!=0); //Se generaron todos los recursos
        	}

        }
        
        
        
    }

    class HiloGenerador implements Runnable{

        @Override
        public void run() {
        	int cuentas=0;
        	marca=monitor.getMarcado();
        	while(marca[6][0]<24){
        		monitor.dispararTransicion(0);
        		//System.out.println("Genere un recurso, cuenta: "+ cuentas);
        		
        		/*logger.info(""
        				+"\n	**********************************************************************"
        				+"\n 	Finalizando Proceso 2, cuenta: "+ cuentas
        				+"\n	**********************************************************************"
        				+"\nMarcado P0: " + monitor.getMarcado()[0][0]);
				*/
        		cuentas=cuentas+1;
        		marca=monitor.getMarcado();
        	}
        }
    }

    
    class HiloProceso1 implements Runnable{
    	@Override
        public void run() {
    		marca=monitor.getMarcado();
    		while(marca[6][0]<24){
        		monitor.dispararTransicion(1);
        		System.out.println("Inicio Proceso 1, cuenta: "+cuentas1);
        		cuentas1=cuentas1+1;
        		marca=monitor.getMarcado();
        	}
    }
    	}
    	
    	class HiloProceso2 implements Runnable{
        	@Override
            public void run() {
        		marca=monitor.getMarcado();
        		while(marca[6][0]<24){
            		monitor.dispararTransicion(2);
            		System.out.println("Inicio Proceso 2, cuenta: "+cuentas2);
            		cuentas2=cuentas2+1;
            		marca=monitor.getMarcado();
            	}
        }
    	}
        	
        	

        class HiloControlProceso1 implements Runnable{
            @Override
            public void run() {
            	int cuentas=0;
            	marca=monitor.getMarcado();
            	while(marca[6][0]<24){
            		if(marca[1][0]!=0||marca[3][0]!=0) {
            			fail("Hay tokens en el proceso 2 mientras se ejecutaba proceso 1");
            		}
            		monitor.dispararTransicion(3);
            		monitor.dispararTransicion(5);
            		marca=monitor.getMarcado();
            		System.out.println("Finalizo Proceso 1, cuenta: "+cuentas);
            		cuentas=cuentas+1;
            	}
            }
        }
        
        class HiloControlProceso2 implements Runnable{
            @Override
            public void run() {
            	int cuentas=0;
            	marca=monitor.getMarcado();
            	while(marca[6][0]<24){
            		if(marca[2][0]!=0||marca[4][0]!=0) {
            			fail("Hay tokens en el proceso 1 mientras se ejecutaba proceso 2");
            		}
            		monitor.dispararTransicion(4);
            		monitor.dispararTransicion(6);
            		marca=monitor.getMarcado();
            		System.out.println("Finalizo Proceso 2, cuenta: "+cuentas);
            		cuentas=cuentas+1;
            	}
            }
        }
        
        class HiloFinalizo implements Runnable{
            @Override
            public void run() {
            	int cuentas=0;
            	marca=monitor.getMarcado();
            	while(marca[6][0]<24){
            		if(marca[5][0]!=0&&(marca[1][0]!=0||marca[2][0]!=0||marca[3][0]!=0||marca[4][0]!=0)) {
            			fail("Hay tokens en la plaza 1 2 3 o 4, cuando hay un token en la plaza 5");
            		}
            		monitor.dispararTransicion(7);
            		marca=monitor.getMarcado();
            		System.out.println("Libero recurso, cuenta: "+cuentas);
            		cuentas=cuentas+1;
            	}
            }
        }
    
    
    }

