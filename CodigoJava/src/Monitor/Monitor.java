package Monitor;

import java.util.concurrent.Semaphore;
import java.util.List;
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
	
	
	//Metodos basados en diagrama de secuencia.
	public void dispararTransicion(int transicion) throws InterruptedException{
		
		try{
			mutex.acquire(); //Adquiero acceso al monitor.
		}
		catch(InterruptedException e){
			e.printStackTrace();
			return;
		}
		boolean k=true;
		
		while(k){
			k=rdp.disparar(transicion); //Disparo red de petri.
			if(k){ //K=true verifica el estado de la red.
				List<Integer> Vs=rdp.getSensibilizadas(); //get transiciones sensibilizadas
				List<Integer> Vc=quienesEstanEnColas(); //get transiciones sensibilizadas
				List<Integer> m= andVector(Vs, Vc);
				if(m!=0){
					int transicionADisparar=politica.cualDisparar(m);
					mutex.release();
	                return;
				}
				else{
					k=false;
				}
			}
			else{
				mutex.release();
				acquireColaDeCondicion();
                return;
			}
		}
		
		mutex.release(); //Libero al monitor.
		return;
	}
	
	
}
