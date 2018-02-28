package test;


import static org.junit.Assert.*;

import org.junit.After;

import monitor.Monitor;

public class testMonitorCompleto2 {
    private Monitor monitor;
    private String redExcel="./RedesParaTest/RedPrioridades/lectorEscritor.xls"; //Path para Linux.
    private int flagPolitica=0;
    private int cuentas1=0;
    private int cuentas2=0;
    private int[][] marca;
    
    @org.junit.Before
    public void setUp() throws Exception {
    	monitor = Monitor.getInstance();
    	/*
    	 * configuracion del path para el excel de la red a testear, segun el SO y el usuario de cada alumno.
    	 */
    	if((System.getProperty("os.name")).equals("Windows 10")){	
    		if(System.getProperty("user.name").equals("kzAx")){
				this.redExcel="..\\src\\RedesParaTest\\RedPrioridades\\lectorEscritor.xls";
			}
			 else{
				 this.redExcel="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\RedPrioridades\\lectorEscritor.xls"; //Path para Windows.
			 }
    	}
    	
        monitor.configRdp(redExcel);        
        monitor.setPolitica(flagPolitica);}	// 0-modo aleatorio
							     			// 1-prioridad al proceso 1
        									// 2-prioridad al proceso 2

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		monitor=null;
	}
	
    @org.junit.Test
    public void testCompleto2() throws Exception {
        
    	/*
    	 * Creo los hilos que operan la red
    	 */
    	Thread hilo1 = new Thread(new HiloGenerador());
        Thread hilo2= new Thread(new HiloProceso1());
        Thread hilo3 = new Thread(new HiloProceso2());
        Thread hilo4= new Thread(new HiloControlProceso1());
        Thread hilo5= new Thread(new HiloControlProceso2());
        Thread hilo6= new Thread(new HiloFinalizo());
       
        /*
         * Inicio las tareas a cada hilo
         */
        hilo6.start();
        hilo3.start();
        hilo4.start();
        hilo1.start();
        hilo2.start();
        hilo5.start();
       
        /*
         * El hilo generador finaliza y hace un join con el hilo del main
         */
        hilo1.join();
        
         // Al finalizar la tarea del hilo generador, se detienen la ejecucion de los demas hilos
        try{
        	hilo2.stop();
        	hilo3.stop();
        	hilo4.stop();
        	hilo5.stop();
        	hilo6.stop();
        	Thread.sleep(100);
        }
        catch(InterruptedException e){
        	System.out.println("Error en testMonitorCompleto2, testCompleto2");
        	e.printStackTrace();
        }
        
        finally{
        	this.marca=monitor.getMarcado(); // se obtiene el marcado de la red
        	
        	/*
        	 * Se prueba que realmente el hilo generador haya podido llegar al estado local P7=0, lo que implica
        	 * que se disparo correctamente la transicion asignada al hilo generador.
        	 */
        	assert(this.marca[7][0]==0); 
        	
        	/*
        	 * Se prueba que, la plaza trampa P6 tenga al menos 10 tareas finalizadas
        	 */
        	assert(this.marca[6][0]>=10);
        	
        	/*
        	 * Por ultimo, dependiendo de la politica se prueba:
        	 * 	Si es 0: que la cuentas de proceso 1 cuentas1 y la cuenta de procesos 2 cuentas2 sean ambas distintas de 0 (se realizo al menos un proceso en ambos casos)
        	 * 	Si es 1: Unicamente se deberian haber realizadas las tareas 1
        	 * 	Si es 2: Unicamente se deberian haber realizadas las tareas 2
        	 */
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
    

    /*
     * El hilo "generador" tiene la tarea de disparar la transicion 0 50 veces.
     */
    class HiloGenerador implements Runnable{
        @Override
        public void run() {
        	for(int i=0; i<50; i++){monitor.dispararTransicion(0);}
        }
    }

    
    /*
     * El hilo del proceso1 trata de dar inicio al proceso 1, disparando la transicion 1 (25 veces)
     */
    class HiloProceso1 implements Runnable{
    	@Override
        public void run() {
    		for(int i=0; i<25; i++){
        		monitor.dispararTransicion(1);
        		System.out.println("Inicio Proceso 1, cuenta: "+cuentas1);
        		cuentas1=cuentas1+1; //se lleva una cuenta de cuantas veces se disparo la transicion 1
        	}
    	}
	}
    	
    
    /*
     * El hilo del proceso2 trata de dar inicio al proceso 2, disparando la transicion 2 (25 veces)
     */
	class HiloProceso2 implements Runnable{
    	@Override
        public void run() {
    		for(int i=0; i<25; i++){
            		monitor.dispararTransicion(2);
            		System.out.println("Inicio Proceso 2, cuenta: "+cuentas2);
            		cuentas2=cuentas2+1; //se lleva una cuenta de cuantas veces se disparo la transicion 2
            		
        	}
        }
	}
     
	
	/*
	 * El hilo control proceso 1 dispara las transiciones que corresponden a la tarea 1 (simulando que ese proceso lleva ciertas actividades)
	 */
    class HiloControlProceso1 implements Runnable{
        @Override
        public void run() {
        	int cuentas=0;
        	for(int i=0; i<25; i++){
        		monitor.dispararTransicion(3);
        		int [][] marca_hilo=monitor.getMarcado();
        		/*
        		 * aqui se compara que, como hay brazos inhibidores y ademas una plaza que representan los "recursos"
        		 * para realizar la tarea, sea imposible que mientras se este ejecutando el proceso1 se ejecute el proceso2 simultaneamente
        		 */
        		if(marca_hilo[1][0]!=0||marca_hilo[3][0]!=0) {
        			fail("Hay tokens en el proceso 2 mientras se ejecutaba proceso 1");
        		}
        		monitor.dispararTransicion(5);
        		System.out.println("Finalizo Proceso 1, cuenta: "+cuentas);
        		cuentas=cuentas+1;
        	}
        }
    }
    
    /*
	 * El hilo control proceso 2 dispara las transiciones que corresponden a la tarea 2 (simulando que ese proceso lleva ciertas actividades)
	 */ 
    class HiloControlProceso2 implements Runnable{
        @Override
        public void run() {
        	int cuentas=0;
        	for(int i=0; i<25; i++){
        		monitor.dispararTransicion(4);
        		int [][] marca_hilo=monitor.getMarcado();
        		/*
        		 * aqui se compara que, como hay brazos inhibidores y ademas una plaza que representan los "recursos"
        		 * para realizar la tarea, sea imposible que mientras se este ejecutando el proceso2 se ejecute el proceso1 simultaneamente
        		 */
        		if(marca_hilo[2][0]!=0||marca_hilo[4][0]!=0) {
        			fail("Hay tokens en el proceso 1 mientras se ejecutaba proceso 2");
        		}
        		monitor.dispararTransicion(6);
        		System.out.println("Finalizo Proceso 2, cuenta: "+cuentas);
        		cuentas=cuentas+1;
        	}
        }
    }
     
    /*
     * Hilo que tiene la finalidad de disparar la transicion 7, simulando que el recurso esta ahora libre y puede inicializarse otra tarea (1 o 2)
     */
    class HiloFinalizo implements Runnable{
        @Override
        public void run() {
        	int cuentas=0;
        	for(int i=0; i<25; i++){
        		int [][] marca_hilo=monitor.getMarcado();
        		/*
        		 * Aqui se compara que funcionen los arcos inhibidores, si mientras exista al menos un token en la plaza 5, las demas plazas no pueden
        		 * contener un marcado distinto de cero.
        		 */
        		if(marca_hilo[5][0]!=0&&(marca_hilo[1][0]!=0||marca_hilo[2][0]!=0||marca_hilo[3][0]!=0||marca_hilo[4][0]!=0)) {
        			fail("Hay tokens en la plaza 1 2 3 o 4, cuando hay un token en la plaza 5");
        		}
        		monitor.dispararTransicion(7);
        		System.out.println("Libero recurso, cuenta: "+cuentas);
        		cuentas=cuentas+1;
        	}
        }
    }
    
    
}
