package monitor;

import java.util.concurrent.Semaphore;

import logueo.Logger;

public class Monitor { 
	//Elementos del monitor.
	private Politica politica;
	private int cantidad_de_transiciones; //Igual a cantidad de colas.
    private Cola[] colas;
    private RedDePetri rdp;
    private Semaphore mutex;
    private Logger log;
    
   
    
    
    //Aplicacion de Singleton.
    private static final Monitor instance = new Monitor();
	 private Monitor(){
		 //Semaforo binario a la entrada del monitor.
		 //Fairness true: FIFO en cola de hilos bloqueados.
	       mutex=new Semaphore(1,true);
	       //La red de petri y las transiciones se configuran posteriormente.
	       this.log=new Logger(3);
	       this.log.createMessage("Transiciones disparadas: \r\n", 1);
		   
		 
		   //LogFileA: Evolucion del marcado
		   //LogFileB: Vector con contadores de transiciones disparadas
		   //LogFileC: Contador de transiciones disparadas.
	  }

	
	public static Monitor getInstance(){return instance;}
	
	
	/**
	 * Metodo writeLogFiles. 
	 * Es utilizado para escribir los archivos de log que se crean en el constructor.
	 */
	public void writeLogFiles(){
		try{
			mutex.acquire(); //Adquiero acceso al monitor.
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		for(int i=0;i<3;i++){
			log.flushBufferToFile(i);
		}
		mutex.release();
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
		this.rdp=new RedDePetri(path,this.log);
		this.cantidad_de_transiciones=rdp.getCantTransiciones();
		
		colas= new Cola[this.cantidad_de_transiciones];
        for(int i=0;i<this.cantidad_de_transiciones;i++){ 
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
	 * @return int[] lista con enteros 1 y 0, indicando si las transiciones correspondientes poseen hilos esperando en sus colas o no, respectivamente.
	 */
	private int[] quienesEstanEnColas() {
		int[] Vc = new int[this.cantidad_de_transiciones];
        for(int i=0;i<this.cantidad_de_transiciones;i++){
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
		
			this.log.addMessage("\r\nTransicion a disparar: " + transicion+ "\r\n", 0); //Log de transicion a disparar
		
			k=rdp.disparar(transicion); //Disparo red de petri. //Si se logra disparar, k se pone en true.
			
			
			
			//System.out.println(k);
			//System.out.println(transicion);
			if(k){ //K=true verifica el estado de la red.
				int[] Vs=rdp.getSensibilizadasExtendido(); //get transiciones sensibilizadas
				int[] Vc=quienesEstanEnColas(); //get Quienes estan en colas
				try{
					m= OperacionesMatricesListas.andVector(Vs, Vc); //Obtengo listaM  (Vs AND Vc)
				}
				catch(IndexOutOfBoundsException e){
					e.printStackTrace();
					return;
				}	
				if(OperacionesMatricesListas.isNotAllZeros(m)){ //Hay posibilidad de disparar una transicion.
					try{
						int transicionADisparar=politica.cualDisparar(m); //Pregunto a politica 
						//System.out.println("transicion"+transicionADisparar);
						colas[transicionADisparar].resume(); //Sale un hilo de una cola de condicion para disparara esa transicion 
						//Despierta un hilo que estaba bloqueado en la cola correspondiente
					}
					catch(IndexOutOfBoundsException e){e.printStackTrace();}
					
					mutex.release();
	                return;
				}
				else{ //No hay posibilidad de disparar una transicion.
					k=false;
					//System.out.println("K false");
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
						if(!colas[transicion].isEmpty()){ //Cola no esta vacia
							colas[transicion].delay();
						}
						else{ //No es inmediata y no hay nadie en la cola.
							long timeout=this.rdp.getLogicaTemporal().getTiempoFaltanteParaAlfa(transicion);
							colas[transicion].delay((timeout)+2); //+2 Por problemas de redondeo.
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
	
	
	
	/**
	 * Metodo getMarcado. 
	 * @return int[][] Marcado actual de la RdP
	 */
    public int[][] getMarcado(){
        try {
            mutex.acquire();
        }catch(Exception e){e.printStackTrace();}
        int[][] m= rdp.getMatrizM().clone();
        mutex.release();
        return m;
    }
	
}