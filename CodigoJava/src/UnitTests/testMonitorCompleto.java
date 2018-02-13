package UnitTests;


import Monitor.Monitor;
import org.junit.Test;


import static org.junit.Assert.*;


public class testMonitorCompleto {
    Monitor monitor = Monitor.getInstance();
    private String redExcel="./RedesParaTest/lectorEscritor/lectorEscritor.xls"; //Path para Linux.
    
    @org.junit.Before
    public void setUp() throws Exception {
    	if((System.getProperty("os.name")).equals("Windows 10")){	
			this.redExcel="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\lectorEscritor\\lectorEscritor.xls"; //Path para Windows.
		}
        monitor.configRdp(redExcel);
        monitor.setPolitica(0); //modo aleatorio
        
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
       
        hilo1.start();
        hilo2.start();
        hilo3.start();
        
        //while(hilo1.getState()!= Thread.State.TERMINATED && hilo2.getState()!= Thread.State.TERMINATED && hilo3.getState()!= Thread.State.TERMINATED) {

       // }
        hilo1.join();
        hilo2.join();
        try{
        	Thread.sleep(1000);
        }
        catch(InterruptedException e){
        	e.printStackTrace();
        }
        hilo2.interrupt();
        hilo3.interrupt();
        
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




    class HiloGenerador implements Runnable{

        @Override
        public void run() {
        	monitor.dispararTransicion(3);
        	try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	monitor.dispararTransicion(3);
            for(int i=0;i<50;i++){
                
            	   
                   //monitor.dispararTransicion(3);
                  // System.out.println("Genero un escritor");
                   int[][] marca=monitor.getMarcado();
                   if(marca[1][0]>0) {
                       assert (marca[0][0]==0);  //control de inhibidores
                       assert (marca[1][0]<6); //nunca mas de 5 lectores
                       System.out.println("jode");
                   }
                   
                   if(marca[0][0]>0) {
                       assert (marca[1][0]==0);  //control de inhibidores
                       assert (marca[0][0]==1); //siempre solo 1 escritor
                   }
                  
                 

            }
        }
    }

    class HiloEscritor implements Runnable{

        @Override
        public void run() {
            for(int i=0;i<50;i++){
            	
            	//monitor.dispararTransicion(0);
            	//System.out.println("Escribiennnnnnnnnnnnnnnnnnnnnnnndo");
            	//monitor.dispararTransicion(4);
            	//System.out.println("Dejo de escribir");

            }
        }
    }

    class HiloLector implements Runnable{
        @Override
        public void run() {
            for (int i=0;i<50;i++) {
            	//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            	//monitor.dispararTransicion(2);
            	//System.out.println("Leyennnnnnnnnnnnnnnnnnnnnnnnnnndo");
            	//monitor.dispararTransicion(1);
            	//System.out.println("Dejo de leer");
            	
            	
            }
        }
    }

}