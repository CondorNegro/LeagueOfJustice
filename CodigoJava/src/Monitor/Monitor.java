package Monitor;

import java.util.concurrent.Semaphore;
//Se aplicó SINGLETON. 

public class Monitor { 
	//Elementos del monitor.
	private Politica politica;
    private Transiciones transiciones[];
    private RedDePetri rdp;
    private Semaphore mutex;
    
    //Aplicación de Singleton.
    private static final Monitor instance = new Monitor();
	 private Monitor(){
		//Semáforo binario a la entrada del monitor.
		 //Fairness true: FIFO en cola de hilos bloqueados.
	       mutex=new Semaphore(1,true);
	       politica=new Politica(0); //Inicialmente, la politica es al azar.
	       //La red de petri y las transiciones se configuran posteriormente.
	  }

	

	public static Monitor getInstance(){return instance;}

}
