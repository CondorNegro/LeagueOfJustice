package UnitTests;


import Monitor.Monitor;
import org.junit.Test;


import static org.junit.Assert.*;


public class testMonitorCompleto {
    Monitor monitor = Monitor.getInstance();
    private String redExcel5="./RedesParaTest/redTemporal/testExcel.xls"; //Path para Linux.
    
    @org.junit.Before
    public void setUp() throws Exception {
    	if((System.getProperty("os.name")).equals("Windows 10")){	
			this.redExcel5="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\redTemporal\\testExcel.xls"; //Path para Linux.
		}
        monitor.configRdp(redExcel5);
        monitor.setPolitica(0);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        int[][] m= monitor.getMarcado();
        System.out.println("");
        assert (m[0][0]==1||m[1][0]==1||m[2][0]==1);  //control del marcado final

    }

    @org.junit.Test
    public void dispararTransicionTest() throws Exception {
        Thread hilo1 = new Thread(new HiloUno());
        Thread hilo2= new Thread(new HiloDos());
        Thread hilo3 = new Thread(new HiloTres());
        Thread hilo4 = new Thread(new HiloTres());
        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();
        //while(hilo1.getState()!= Thread.State.TERMINATED && hilo2.getState()!= Thread.State.TERMINATED && hilo3.getState()!= Thread.State.TERMINATED) {

       // }
        hilo1.join();
        try{
        	Thread.sleep(1000);
        }
        catch(InterruptedException e){
        	e.printStackTrace();
        }
        hilo2.interrupt();
        hilo3.interrupt();
        hilo4.interrupt();

    }

     @org.junit.Test
     public void intentarDispararTransicion() throws Exception {
    	 Thread hilo1 = new Thread(new HiloUno());
         Thread hilo2= new Thread(new HiloDos());
         Thread hilo3 = new Thread(new HiloTres());
         hilo1.start();
         hilo2.start();
         hilo3.start();
         //while(hilo1.getState()!= Thread.State.TERMINATED && hilo2.getState()!= Thread.State.TERMINATED && hilo3.getState()!= Thread.State.TERMINATED) {
                     //}
         hilo1.join();
         try{
         	Thread.sleep(1000);
         }
         catch(InterruptedException e){
         	e.printStackTrace();
         }
         hilo2.interrupt();
         hilo3.interrupt();
         
     }




    class HiloUno implements Runnable{

        @Override
        public void run() {
            for(int i=0;i<20;i++){
                try{
                	Thread.sleep(2000);
           
                }
                catch(InterruptedException e){
                	e.printStackTrace();
                }
            	 //System.out.println("Dispara t2");
                   monitor.intentardispararTransicion(2);
                  
                  // System.out.println("Dispara t3");
                   monitor.intentardispararTransicion(3);
                  
                   //System.out.println("Dispara t0");
                   monitor.intentardispararTransicion(0);
                  

                   monitor.intentardispararTransicion(1);
                 

            }
        }
    }

    class HiloDos implements Runnable{

        @Override
        public void run() {
            for(int i=0;i<3;i++){
            	//System.out.println("Dispara t4");
            	monitor.dispararTransicion(4);
            	//System.out.println("Dispara t4");

            }
        }
    }

    class HiloTres implements Runnable{
        @Override
        public void run() {
            for (int i=0;i<3;i++) {
            	//System.out.println("Dispara t5");
            	monitor.dispararTransicion(5);
            	
            	
            }
        }
    }

}