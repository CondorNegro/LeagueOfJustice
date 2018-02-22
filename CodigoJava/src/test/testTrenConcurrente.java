package test;


import Monitor.Monitor;



import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import Monitor.RedDePetri;


public class testTrenConcurrente {
    Monitor monitor = Monitor.getInstance();
    private String redExcel="./RedesParaTest/TestTren/excelTren.xls"; //Path para Linux.
    private int Politica=2;
    ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(26);  //creo un ThreadPoolExecutor de tamaño maximo 26 hilos
    private int[] transiciones_tren=new int[14];
    private int tren_en_A=0;
    private int tren_en_B=1;
    private int tren_en_C=0;
    private int tren_en_D=0;
    private int vueltas_tren=0;
    
    @org.junit.Before
    public void setUp() throws Exception {
    	if((System.getProperty("os.name")).equals("Windows 10")){	
			 if(System.getProperty("user.name").equals("kzAx")){
				 redExcel="..\\src\\RedesParaTest\\TestTren\\excelTren.xls";
			 }
			 else{
				 redExcel="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TestTren\\excelTren.xls"; //Path para Windows.
			 }
		}
        monitor.configRdp(redExcel);
        monitor.setPolitica(this.Politica); //modo aleatorio

        
		transiciones_tren[0]=36; //temporal
		transiciones_tren[1]=35;
		transiciones_tren[2]=34; //temporal
		transiciones_tren[3]=18;
		transiciones_tren[4]=21; //temporal
		transiciones_tren[5]=21; //temporal
		transiciones_tren[6]=30;
		transiciones_tren[7]=19; //temporal
		transiciones_tren[8]=8;
		transiciones_tren[9]=4; //temporal
		transiciones_tren[10]=14;
		transiciones_tren[11]=16; //temporal
		transiciones_tren[12]=16; //temporal
		transiciones_tren[13]=5;
    }

    @org.junit.After
    public void tearDown() throws Exception {
        int[][] m= monitor.getMarcado();
        System.out.println("");
        //assert (m[0][0]==1||m[1][0]==1||m[2][0]==1);  //control del marcado final

    }

    @org.junit.Test
    public void dispararTransicionTest() throws Exception {
    	executor.execute(new TrenDriverModificado(this.transiciones_tren,this.monitor));
  
	    //Creacion de hilos generadores - 6 hilos
    	executor.execute(new GeneradorModificado(0,monitor));
    	executor.execute(new GeneradorModificado(1,monitor));
    	executor.execute(new GeneradorModificado(2,monitor));
    	executor.execute(new GeneradorModificado(3,monitor));
    	executor.execute(new GeneradorModificado(15,monitor));
    	executor.execute(new GeneradorModificado(20,monitor));
     
		
		//Creacion de hilo control de bajada - 1 hilos
    	executor.execute(new ControlBajadaModificado(24,monitor));
		
		//Creacion de hilos circulacion de autos por barrera - 2 hilos
    	executor.execute(new AutosDriverModificado(22,monitor));
    	executor.execute(new AutosDriverModificado(17,monitor));
		
		//Creacion de hilos pasajeros subiendo al tren/vagon - 8 hilos
    	executor.execute(new SubidaPasajerosEstacionModificado(10,monitor));
    	executor.execute(new SubidaPasajerosEstacionModificado(7,monitor));
    	executor.execute(new SubidaPasajerosEstacionModificado(9,monitor));
    	executor.execute(new SubidaPasajerosEstacionModificado(13,monitor));
    	executor.execute(new SubidaPasajerosEstacionModificado(23,monitor));
    	executor.execute(new SubidaPasajerosEstacionModificado(12,monitor));
    	executor.execute(new SubidaPasajerosEstacionModificado(6,monitor));
    	executor.execute(new SubidaPasajerosEstacionModificado(11,monitor));
		
		//Creacion de hilos pasajeros bajando al tren/vagon - 8 hilos
    	executor.execute(new BajadasPasajerosEstacionModificado(29,monitor));
    	executor.execute(new BajadasPasajerosEstacionModificado(31,monitor));
    	executor.execute(new BajadasPasajerosEstacionModificado(32,monitor));
    	executor.execute(new BajadasPasajerosEstacionModificado(33,monitor));
    	executor.execute(new BajadasPasajerosEstacionModificado(25,monitor));
    	executor.execute(new BajadasPasajerosEstacionModificado(26,monitor));
    	executor.execute(new BajadasPasajerosEstacionModificado(27,monitor));
    	executor.execute(new BajadasPasajerosEstacionModificado(28,monitor));
		
		
		
       
		
    	while(executor.getCompletedTaskCount()!=(long)24) {
    		
    	}
		
		
        
        try{
        	Thread.sleep(1000);
        	executor.shutdown();
           
        }
        catch(InterruptedException e){
        	//e.printStackTrace();
        }
        finally{
        	int[][] marca_final = monitor.getMarcado();
        	assertEquals(marca_final[4][0],(int)0); //Nadie viajando en maquina
        	assertEquals(marca_final[5][0],(int)0); //Nadie viajando en vagon
        	
        	assertEquals(marca_final[15][0],(int)30); //Cruzaron 30 autos en la barrera de arriba
        	assertEquals(marca_final[19][0],(int)30); //Cruzaron 30 autos en la barrera de abajo
        	
        	assert(vueltas_tren>=1); 
        	assert(tren_en_A>=1); 
        	assert(tren_en_B>=1); 
        	assert(tren_en_C>=1); 
        	assert(tren_en_D>=1);
        	Thread.sleep(1000);
        	
        }
    }
    

   
    class TrenDriverModificado implements Runnable{
   	 
    	private int[] transiciones_viaje;
	    private Monitor monitor;
    	public TrenDriverModificado(int[] transiciones_tren, Monitor monitor) {
			this.transiciones_viaje=transiciones_tren;
			this.monitor=monitor;
		}

		@Override
        public void run() {
            
        	while(true) {
        		for(int i=0; i<this.transiciones_viaje.length; i++) {
        			monitor.dispararTransicion(this.transiciones_viaje[i]);
        			if(transiciones_viaje[i]==5) {
        				tren_en_B=tren_en_B+1;
                		System.out.println("El tren se encuentra en la estacion B, cantidad de veces en B: "+tren_en_B);
        			}
        			if(transiciones_viaje[i]==35) {
        				tren_en_C=tren_en_C+1;
        				System.out.println("El tren se encuentra en la estacion C, cantidad de veces en C: "+tren_en_C);
        			}
        			if(transiciones_viaje[i]==30) {
        				tren_en_D=tren_en_D+1;
        				System.out.println("El tren se encuentra en la estacion D, cantidad de veces en D: "+tren_en_D);
        			}
        			if(transiciones_viaje[i]==8) {
        				tren_en_A=tren_en_A+1;
        				System.out.println("El tren se encuentra en la estacion A, cantidad de veces en A: "+tren_en_A);
        			}

        			
        		}
        		vueltas_tren=vueltas_tren+1;
        		System.out.println("El tren dio una vuelta completa, cantidad de vueltas totales: "+vueltas_tren);
        	}
        }
    }
    
    class SubidaPasajerosEstacionModificado implements Runnable{
      	 
    	private int transicion_subida;
        private Monitor monitor;
    	public SubidaPasajerosEstacionModificado(int transicion_subida, Monitor monitor) {
			this.transicion_subida=transicion_subida;
			this.monitor=monitor;
		}

		@Override
        public void run() {
    		for(int i=0; i<15; i++) {
    			monitor.dispararTransicion(this.transicion_subida);
    			
        	}
        }
    }
    
    class BajadasPasajerosEstacionModificado implements Runnable{
     	 
    	private int transicion_bajada;
        private Monitor monitor;
    	public BajadasPasajerosEstacionModificado(int transicion_bajada, Monitor monitor) {
			this.transicion_bajada=transicion_bajada;
			this.monitor=monitor;
		}

		@Override
        public void run() {
    		for(int i=0; i<15; i++) {
    			monitor.dispararTransicion(this.transicion_bajada);
        	}
        }
    }
    
    class GeneradorModificado implements Runnable{
    	 
    	private int transicion_generadora;
        private Monitor monitor;
    	public GeneradorModificado(int transicion_generadora, Monitor monitor) {
			this.transicion_generadora=transicion_generadora;
			this.monitor=monitor;
		}

		@Override
        public void run() {
    		for(int i=0; i<30; i++) {
    			monitor.dispararTransicion(this.transicion_generadora);
        	}
        }
    }
    
    class ControlBajadaModificado implements Runnable{
   	 
    	private int transicion_control;
        private Monitor monitor;
    	public ControlBajadaModificado(int transicion_control, Monitor monitor) {
			this.transicion_control=transicion_control;
			this.monitor=monitor;
		}

		@Override
        public void run() {
    		while(true) {
    			monitor.dispararTransicion(this.transicion_control);
        	}
        }
    }
    
    class AutosDriverModificado implements Runnable{
   	 
    	private int transicion_autos_cruzando;
        private Monitor monitor;
    	public AutosDriverModificado(int transicion_autos_cruzando, Monitor monitor) {
			this.transicion_autos_cruzando=transicion_autos_cruzando;
			this.monitor=monitor;
		}

		@Override
        public void run() {
    		for(int i=0; i<30; i++) {
    			
    			monitor.dispararTransicion(this.transicion_autos_cruzando);
        	}
        }
    }


}

  

