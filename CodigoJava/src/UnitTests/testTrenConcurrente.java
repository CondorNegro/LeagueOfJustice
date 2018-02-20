package UnitTests;


import Monitor.Monitor;



import static org.junit.Assert.*;

import java.lang.reflect.Method;
import Monitor.RedDePetri;
import clasesPrincipales.AutosDriver;
import clasesPrincipales.BajadaPasajerosEstacion;
import clasesPrincipales.ControlBajadaPasajeros;
import clasesPrincipales.Generador;
import clasesPrincipales.SubidaPasajerosEstacion;
import clasesPrincipales.TrenDriver;


public class testTrenConcurrente {
    Monitor monitor = Monitor.getInstance();
    private String redExcel="./RedesParaTest/TestTren/excelTren.xls"; //Path para Linux.
    private int Politica=2;
    
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
    	Thread tren_driver= new Thread(new TrenDriverModificado(this.transiciones_tren,this.monitor));
  
	    //Creacion de hilos generadores - 6 hilos
		Thread generador_personasA = new Thread(new GeneradorModificado(0,monitor));
		Thread generador_personasB = new Thread(new GeneradorModificado(1,monitor));
		Thread generador_personasC = new Thread(new GeneradorModificado(2,monitor));
		Thread generador_personasD = new Thread(new GeneradorModificado(3,monitor));
		Thread generador_autos1 = new Thread(new GeneradorModificado(15,monitor));
		Thread generador_autos2 = new Thread(new GeneradorModificado(20,monitor));
     
		
		//Creacion de hilo control de bajada - 1 hilos
		Thread control_bajada = new Thread(new ControlBajadaModificado(24,monitor));
		
		//Creacion de hilos circulacion de autos por barrera - 2 hilos
		Thread autos_driver1 = new Thread(new AutosDriverModificado(22,monitor));
		Thread autos_driver2 = new Thread(new AutosDriverModificado(17,monitor));
		
		//Creacion de hilos pasajeros subiendo al tren/vagon - 8 hilos
		Thread subiendo_maquina_estacionA = new Thread(new SubidaPasajerosEstacionModificado(10,monitor));
		Thread subiendo_vagon_estacionA = new Thread(new SubidaPasajerosEstacionModificado(7,monitor));
		Thread subiendo_maquina_estacionB = new Thread(new SubidaPasajerosEstacionModificado(9,monitor));
		Thread subiendo_vagon_estacionB = new Thread(new SubidaPasajerosEstacionModificado(13,monitor));
		Thread subiendo_maquina_estacionC = new Thread(new SubidaPasajerosEstacionModificado(23,monitor));
		Thread subiendo_vagon_estacionC = new Thread(new SubidaPasajerosEstacionModificado(12,monitor));
		Thread subiendo_maquina_estacionD = new Thread(new SubidaPasajerosEstacionModificado(6,monitor));
		Thread subiendo_vagon_estacionD = new Thread(new SubidaPasajerosEstacionModificado(11,monitor));
		
		//Creacion de hilos pasajeros bajando al tren/vagon - 8 hilos
		Thread bajando_maquina_estacionA = new Thread(new BajadasPasajerosEstacionModificado(29,monitor));
		Thread bajando_vagon_estacionA = new Thread(new BajadasPasajerosEstacionModificado(31,monitor));
		Thread bajando_maquina_estacionB = new Thread(new BajadasPasajerosEstacionModificado(32,monitor));
		Thread bajando_vagon_estacionB = new Thread(new BajadasPasajerosEstacionModificado(33,monitor));
		Thread bajando_maquina_estacionC = new Thread(new BajadasPasajerosEstacionModificado(25,monitor));
		Thread bajando_vagon_estacionC = new Thread(new BajadasPasajerosEstacionModificado(26,monitor));
		Thread bajando_maquina_estacionD = new Thread(new BajadasPasajerosEstacionModificado(27,monitor));
		Thread bajando_vagon_estacionD = new Thread(new BajadasPasajerosEstacionModificado(28,monitor));
		
		
		
       
		tren_driver.start();
		//Start hilos.
		generador_personasA.start();
		generador_personasB.start();
		generador_personasC.start();
		generador_personasD.start();
		generador_autos1.start();
		generador_autos2.start();
		
		control_bajada.start();
		
		autos_driver1.start();
		autos_driver2.start();
		
		subiendo_maquina_estacionA.start();
		subiendo_vagon_estacionA.start();
		subiendo_maquina_estacionB.start();
		subiendo_vagon_estacionB.start();
		subiendo_maquina_estacionC.start();
		subiendo_vagon_estacionC.start();
		subiendo_maquina_estacionD.start();
		subiendo_vagon_estacionD.start();
		
		
		bajando_maquina_estacionA.start();
		bajando_vagon_estacionA.start();
		bajando_maquina_estacionB.start();
		bajando_vagon_estacionB.start();
		bajando_maquina_estacionC.start();
		bajando_vagon_estacionC.start();
		bajando_maquina_estacionD.start();
		bajando_vagon_estacionD.start();
        
     
        

		generador_personasA.join();
		generador_personasB.join();
		generador_personasC.join();
		generador_personasD.join();
		generador_autos1.join();
		generador_autos2.join();
		autos_driver1.join();
		autos_driver2.join();
		
		subiendo_maquina_estacionA.join();
		subiendo_vagon_estacionA.join();
		subiendo_maquina_estacionB.join();
		subiendo_vagon_estacionB.join();
		subiendo_maquina_estacionC.join();
		subiendo_vagon_estacionC.join();
		subiendo_maquina_estacionD.join();
		subiendo_vagon_estacionD.join();
		
		bajando_maquina_estacionA.join();
		bajando_vagon_estacionA.join();
		bajando_maquina_estacionB.join();
		bajando_vagon_estacionB.join();
		bajando_maquina_estacionC.join();
		bajando_vagon_estacionC.join();
		bajando_maquina_estacionD.join();
		bajando_vagon_estacionD.join();
		
		
		
        
        try{
        	Thread.sleep(1000);
        	tren_driver.stop();
        	control_bajada.stop();
           
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

  

