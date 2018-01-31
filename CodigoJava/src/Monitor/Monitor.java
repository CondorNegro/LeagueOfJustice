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
	
	
	public void configRdp(String path){
		try {
	            mutex.acquire();
	    }
		catch(InterruptedException e){
			e.printStackTrace();
		}
		this.rdp=new RedDePetri(path);
		this.setNumeroTransiciones(rdp.getNumeroTransiciones());
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
	
	
	public List<Integer> quienesEstanEnColas() {
		ArrayList<Integer> Vc = new ArrayList<>();
		return Vc;
	}
	


public List<Integer> andVector(List<Integer> lista1, List<Integer> lista2) throws IndexOutOfBoundsException{
	if (lista1.size()==lista2.size()) {
		ArrayList<Integer> resultado = new ArrayList<>();
		Iterator<Integer> iterador1=lista1.iterator();
		Iterator<Integer> iterador2=lista2.iterator();

		while(iterador1.hasNext()&iterador2.hasNext()) {
    		resultado.add(new Integer(iterador1.next().intValue()&iterador2.next().intValue()));
		}   

		return resultado;
	}

	else{
		throw new IndexOutOfBoundsException("Listas de diferentes tamaños");
	}

}


	
	//Devuelve true si al menos un elemento de la lista es distinto de cero, de lo contrario devuelve false
	public boolean isNotAllZeros(List<Integer> lista){
		Iterator<Integer> iterador=lista.iterator();
		boolean var=false;
		while(iterador.hasNext()) {
			if(iterador.next().intValue()!=0) {
				var=true;
			}
		} 
		return var;
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
					m= andVector(Vs, Vc);
				}
				catch(IndexOutOfBoundsException e){
					e.printStackTrace();
					return;
				}	
				if(isNotAllZeros(m)){
					
					int transicionADisparar=politica.cualDisparar(m);
					try{
						colas[transicionADisparar].resume(); //Sale de una cola de condición.
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