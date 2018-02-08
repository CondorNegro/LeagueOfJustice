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
   
    
    
    //Aplicacion de Singleton.
    private static final Monitor instance = new Monitor();
	 private Monitor(){
		 //Semaforo binario a la entrada del monitor.
		 //Fairness true: FIFO en cola de hilos bloqueados.
	       mutex=new Semaphore(1,true);
	       setNumeroTransiciones(0);
	       //La red de petri y las transiciones se configuran posteriormente.
	  }

	
	public static Monitor getInstance(){return instance;}
	
	
	
	/**
	 * Metodo setNumeroTransiciones. Permite setear el numero de transiciones de la red.
	 * @param n Numero de transiciones
	 */
	private void setNumeroTransiciones(int n){
		this.cantTransiciones=n;
	}
	
	/**
	 * Metodo getNumeroTransiciones. Permite obtener el numero de transiciones de la red de petri.
	 * @return int Numero de transiciones
	 */
	private int getNumeroTransiciones(){
		return this.cantTransiciones;
	}
	
	/**
	 * Metodo getPolitica. Utilizado para testing.
	 * @return int Modo de la politica utilizada
	 */
	private int getPolitica(){
		return this.politica.getModo();
	}
	
	/**
	 * Metodo getColaCero. Utilizado para testing. 
	 * @return Cola Elemento 0 del atributo colas[] .
	 */
	private Cola getColaCero(){
		return colas[0];
	}
	
	/**
	 * Metodo getRDP.
	 * @return RedDePetri Devuelve la red de petri con la que se configuro este monitor.
	 */
	private RedDePetri getRDP(){
		return this.rdp;
	}
	
	/**
	 * Metodo configRdp. 
	 * Crea y configura la Red de Petri y sus matrices. 
	 * Setea el numero de transiciones correspondientes.
	 * Crea una cola por cada transicion de la red.
	 * @param path Direccion absoluta del archivo Excel en donde se encuentran las matrices de la red de Petri
	 */
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
	
	/**
	 * Metodo setPolitica. Permite setear la politica del monitor.
	 * @param Modo modo de la politica
	 */
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
	
	/**
	 * Metodo quienesEstanEnColas.
	 * @return List<Integer> lista con enteros 1 y 0, indicando si las transiciones correspondientes poseen hilos esperando en sus colas o no, respectivamente.
	 */
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
	



	
	/**
	 * Metodo dispararTransicion. Permite indicarle al monitor que el hilo desea disparar una determinada transicion. 
	 * (Ver diagrama de secuencia).
	 * @param transicion Transicion a disparar
	 */
	
	
	public void dispararTransicion(int transicion) {
		List<Integer> m;
		try{
			mutex.acquire(); //Adquiero acceso al monitor.
		}
		catch(InterruptedException e){
			e.printStackTrace();
			return;
		}
		boolean k=true; //Variable booleana de control.  
		
		while(k){
			k=rdp.disparar(transicion); //Disparo red de petri. //Si se logra disparar se pone en true.
			if(k){ //K=true verifica el estado de la red.
				List<Integer> Vs=rdp.getSensibilizadas(); //get transiciones sensibilizadas
				List<Integer> Vc=quienesEstanEnColas(); //get Quienes estan en colas
				try{
					m= OperacionesMatricesListas.andVector(Vs, Vc); //Obtengo listaM
				}
				catch(IndexOutOfBoundsException e){
					e.printStackTrace();
					return;
				}	
				if(OperacionesMatricesListas.isNotAllZeros(m)){ //Hay posibilidad de disparar una transicion.
					try{
						int transicionADisparar=politica.cualDisparar(m);
						colas[transicionADisparar].resume(); //Sale un hilo de una cola de condicion. 
						//Despierta un hilo que estaba bloqueado en la cola correspondiente
					}
					catch(IndexOutOfBoundsException e){e.printStackTrace();}
					
					mutex.release();
	                return;
				}
				else{ //No hay posibilidad de disparar una transicion.
					k=false;
				}
			}
			else{
				mutex.release();
				try{
					colas[transicion].delay(); //Se encola en una cola de condicion.
				}
				catch(Exception e){ //Puede haber mas de un tipo de excepcion. (Por interrupcion o por exceder los limites).
					e.printStackTrace();
				}
			
                return;
			}
		}
		
		mutex.release(); //Libero al monitor.
		return;
	}
	
	
	
}