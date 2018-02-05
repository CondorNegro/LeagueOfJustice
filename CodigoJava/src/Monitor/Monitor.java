package Monitor;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
//Se aplica SINGLETON. 

public class Monitor { 
	//Elementos del monitor.
	private Politica politica;
	private int cantTransiciones; //Igual a cantidad de colas.
    private Cola colas[];
    private RedDePetri rdp;
    private Semaphore mutex;
    
    //Aplicación de Singleton.
    private static final Monitor instance = new Monitor();
	 private Monitor(){
		 //Semaforo binario a la entrada del monitor.
		 //Fairness true: FIFO en cola de hilos bloqueados.
	       mutex=new Semaphore(1,true);
	       setNumeroTransiciones(0);
	       //La red de petri y las transiciones se configuran posteriormente.
	  }

	
	public static Monitor getInstance(){return instance;}
	
	
	private void setNumeroTransiciones(int n){
		this.cantTransiciones=n;
	}

	private int getNumeroTransiciones(){
		return this.cantTransiciones;
	}
	
	private int getPolitica(){
		return this.politica.getModo();
	}
	
	private Cola getColaCero(){
		return colas[0];
	}
	
	public void configRdp(String path){
		try {
	            mutex.acquire();
	    }
		catch(InterruptedException e){
			e.printStackTrace();
		}
		this.rdp=new RedDePetri(path);
		this.setNumeroTransiciones(rdp.getCantTransiciones());
		colas= new Cola[this.getNumeroTransiciones()];
        for(int i=0;i<this.getNumeroTransiciones();i++){ 
            colas[i]=new Cola(); //Inicialización de colas.
        }
		mutex.release();
	}
	
	public void setPolitica(int Modo){
		try{
			mutex.acquire(); //Adquiero acceso al monitor.
		}
		catch(InterruptedException e){
			e.printStackTrace();
			return;
		}
		this.politica=new Politica(Modo);
		mutex.release();
	}
	
	
	private List<Integer> quienesEstanEnColas() {
		ArrayList<Integer> Vc = new ArrayList<>();
        for(int i=0;i<this.getNumeroTransiciones();i++){
        	if (colas[i].isEmpty()==true) {
        		Vc.add(0);
        	}
        	else {
        		Vc.add(1);
        	}
        }
		return Vc;
	}
	



	
	
	
	//Metodos basados en diagrama de secuencia.
	public void dispararTransicion(int transicion) {
		List<Integer> m;
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
				try{
					m= OperacionesMatricesListas.andVector(Vs, Vc);
				}
				catch(IndexOutOfBoundsException e){
					e.printStackTrace();
					return;
				}	
				if(OperacionesMatricesListas.isNotAllZeros(m)){
					try{
						int transicionADisparar=politica.cualDisparar(m);
						colas[transicionADisparar].resume(); //Sale de una cola de condicion.
					}
					catch(IndexOutOfBoundsException e){e.printStackTrace();}
					
					mutex.release();
	                return;
				}
				else{
					k=false;
				}
			}
			else{
				mutex.release();
				try{
					colas[transicion].delay(); //Se encola en una cola de condicion.
				}
				catch(Exception e){ //Puede haber mas de un tipo de excepcion. (Por interrupci�n o por exceder los l�mites).
					e.printStackTrace();
				}
			
                return;
			}
		}
		
		mutex.release(); //Libero al monitor.
		return;
	}
	
	
	
}