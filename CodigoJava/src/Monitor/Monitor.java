package Monitor;

import java.util.concurrent.Semaphore;

import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
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
	       //La red de petri y las transiciones se configuran posteriormente.
	  }

	
	public static Monitor getInstance(){return instance;}
	
	
	
	/**
	 * Metodo getPolitica. Utilizado para testing.
	 * @return int Modo de la politica utilizada
	 */
	private int getPolitica(){
		try{
			mutex.acquire(); //Adquiero acceso al monitor.
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		mutex.release();
		return this.politica.getModo();
	}
	
	
	
	/**
	 * Metodo getColaCero. Utilizado para testing. 
	 * @return Cola Elemento 0 del atributo colas[] .
	 */
	private Cola getColaCero(){
		try{
			mutex.acquire(); //Adquiero acceso al monitor.
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		mutex.release();
		return colas[0];
	}
	
	
	
	/**
	 * Metodo getRDP.
	 * @return RedDePetri Devuelve la red de petri con la que se configuro este monitor.
	 */
	private RedDePetri getRDP(){
		try{
			mutex.acquire(); //Adquiero acceso al monitor.
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		mutex.release();
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
		this.cantTransiciones=rdp.getCantTransiciones();
		colas= new Cola[this.cantTransiciones];
        for(int i=0;i<this.cantTransiciones;i++){ 
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
		this.politica.setPrioridades(rdp.getPrioridadesSubida(), rdp.getPrioridadesBajada());
		mutex.release();
	}
	
	
	
	/**
	 * Metodo quienesEstanEnColas.
	 * @return List<Integer> lista con enteros 1 y 0, indicando si las transiciones correspondientes poseen hilos esperando en sus colas o no, respectivamente.
	 */
	private int[] quienesEstanEnColas() {
		int[] Vc = new int[this.cantTransiciones];
        for(int i=0;i<this.cantTransiciones;i++){
        	if (colas[i].isEmpty()==true) {
        		Vc[i]=0;
        	}
        	else {
        		Vc[i]=1;
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
		int[] m;
		while(true){
			try{
				mutex.acquire(); //Adquiero acceso al monitor.
			}
			catch(InterruptedException e){
				e.printStackTrace();
				return;
			}
			boolean k=true; //Variable booleana de control.  
		
		
		
			k=rdp.disparar(transicion); //Disparo red de petri. //Si se logra disparar se pone en true.
			//System.out.println(k);
			//System.out.println(transicion);
			if(k){ //K=true verifica el estado de la red.
				int[] Vs=rdp.getSensibilizadasExtendido(); //get transiciones sensibilizadas
				int[] Vc=quienesEstanEnColas(); //get Quienes estan en colas
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
						//System.out.println("transicion"+transicionADisparar);
						colas[transicionADisparar].resume(); //Sale un hilo de una cola de condicion. 
						//Despierta un hilo que estaba bloqueado en la cola correspondiente
					}
					catch(IndexOutOfBoundsException e){e.printStackTrace();}
					
					mutex.release();
	                return;
				}
				else{ //No hay posibilidad de disparar una transicion.
					k=false;
					System.out.println("K false");
					mutex.release();
					return;
				}
			}
			else{
				mutex.release();
				try{
					if(this.rdp.getVectorTransicionesInmediatas()[transicion]==1 ){
						colas[transicion].delay(); //Se encola en una cola de condicion. 
					}
					else{ //No es transicion inmediata
						if(!colas[transicion].isEmpty()){
							colas[transicion].delay();
						}
						else{
							long timeout=this.rdp.getlogicaTemporal().getTiempoFaltanteParaAlfa(transicion);
							colas[transicion].delay((timeout*1000)+2000); //Por problemas de redondeo.
						}
											
						
					}
					
				}
				
				catch(Exception e){ //Puede haber mas de un tipo de excepcion. (Por interrupcion o por exceder los limites).
					e.printStackTrace();
				}
				
                //return;
			}
		
		
			//mutex.release(); //Libero al monitor.
			//return;
		}
	}
	
    public int[][] getMarcado(){
        try {
            mutex.acquire();
        }catch(Exception e){e.printStackTrace();}
        int[][] m= rdp.getMatrizM().clone();
        mutex.release();
        return m;
    }
    
    
	

	
}