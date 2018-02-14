package UnitTests;


import Monitor.Monitor;



import static org.junit.Assert.*;


public class testMonitorCompleto {
    Monitor monitor = Monitor.getInstance();
    private String redExcel="./RedesParaTest/lectorEscritor/lectorEscritor.xls"; //Path para Linux.
    private boolean flagGeneracion;
    private boolean flagLector;
    private boolean flagEscritor;
    
    @org.junit.Before
    public void setUp() throws Exception {
    	if((System.getProperty("os.name")).equals("Windows 10")){	
			this.redExcel="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\lectorEscritor\\lectorEscritor.xls"; //Path para Windows.
		}
        monitor.configRdp(redExcel);
        monitor.setPolitica(0); //modo aleatorio
        flagEscritor=false;
        flagLector=false;
        flagGeneracion=false;
        
    }

    @org.junit.After
    public void tearDown() throws Exception {
        int[][] m= monitor.getMarcado();
        System.out.println("");
        //assert (m[0][0]==1||m[1][0]==1||m[2][0]==1);  //control del marcado final

    }

    @org.junit.Test
    public void dispararTransicionTest() throws Exception {
        Thread hilo1 = new Thread(new HiloGenerador());
        Thread hilo2= new Thread(new HiloEscritor());
        Thread hilo3 = new Thread(new HiloLector());
        Thread hilo4= new Thread(new HiloAcumuladorEscritores());
        Thread hilo5= new Thread(new HiloRetiradorLectores());
       
        hilo3.start();
        hilo4.start();
        hilo1.start();
        hilo2.start();
        hilo5.start();
       
        
        //while(hilo1.getState()!= Thread.State.TERMINATED && hilo2.getState()!= Thread.State.TERMINATED && hilo3.getState()!= Thread.State.TERMINATED) {

       // }
        hilo1.join();
        
        try{
        	Thread.sleep(1000);
        	hilo2.interrupt();
            hilo3.interrupt();
            hilo4.interrupt();
           
        }
        catch(InterruptedException e){
        	e.printStackTrace();
        }
        finally{
        	assert(flagGeneracion);
            assert(flagLector);
            assert(flagEscritor);
        }
        
        
        
    }

    /*
     @org.junit.Test
     public void intentarDispararTransicion() throws Exception {
    	 Thread hilo1 = new Thread(new HiloGenerador());
         Thread hilo2= new Thread(new HiloEscritor());
         Thread hilo3 = new Thread(new HiloLector());
         hilo1.start();
         hilo2.start();
         hilo3.start();

         hilo1.join();
         try{
         	Thread.sleep(1000);
         }
         catch(InterruptedException e){
         	e.printStackTrace();
         }
         hilo2.interrupt();
         hilo3.interrupt();
         
     }*/


    class HiloAcumuladorEscritores implements Runnable{
    	@Override
        public void run() {
        	
        	
            for(int i=0;i<50;i++){
            		
            	System.out.print("Escritor:");
          	    System.out.println(i);
            	
            	
                 monitor.dispararTransicion(1);
                   
                              

            }
        }
    }
    

    class HiloGenerador implements Runnable{

        @Override
        public void run() {
        	
        	
            for(int i=0;i<50;i++){
            		if(i==49){
            			flagGeneracion=true;
            		}
            	   System.out.print("Generador:");
            	   System.out.println(i);
                   monitor.dispararTransicion(0);
                   
                   System.out.println("Genero un escritor");
                   int[][] marca=monitor.getMarcado();
                   
                   if(marca[0][0]>0) {
                	   assertEquals(marca[3][0],0);
                	   if(marca[3][0]!=0){  //control de inhibidores) {
                	   		                	   		
                		   fail("En P2 hay un token y en P6 tambien");
                	   }
                	   if(marca[1][0]!=0) {
                		   fail("En P2 hay un token y en P3 tambien");
                	   }
                   
                   if(marca[0][0]>0) {
                       assert (marca[1][0]==0);  //control de inhibidores
                       assert (marca[0][0]==1); //siempre solo 1 escritor
                   }
                  
                 

            }
        }
    }
   }

    class HiloEscritor implements Runnable{

        @Override
        public void run() {
            for(int i=0;i<50;i++){
            	if(i==49){
        			flagEscritor=true;
        		}
            	
            	System.out.println("Escribiendo: " + i);
            	
            	monitor.dispararTransicion(2);
            	System.out.println("Dejo de escribir");

            }
        }
    }

    class HiloLector implements Runnable{
        @Override
        public void run() {
            for (int i=0;i<50;i++) {
            	if(i==49){
        			flagLector=true;
        		}
            	System.out.print("Lector:");
          	    System.out.println(i);
            	monitor.dispararTransicion(4);
            	
            	
            	
            }
        }
    }
    
    class HiloRetiradorLectores implements Runnable{
    	@Override
        public void run() {
            for (int i=0;i<50;i++) {
            	
            
            	System.out.println("Leyendo: "+ i);
            	monitor.dispararTransicion(3);
            	System.out.println("Dejo de leer");
            	
            	
            }
        }
    }
}

