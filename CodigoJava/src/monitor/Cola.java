package monitor;

//Existe una cola por transicion

public class Cola { 
	private int cantidad_de_hilos_en_cola;
	
	public Cola(){
		this.cantidad_de_hilos_en_cola=0;
	}
	
	
	/**
	 * Metodo getcantidad_de_hilos_en_cola
	 * @return int cantidad_de_hilos_en_cola
	 */
	public int getcantidad_de_hilos_en_cola(){
		return this.cantidad_de_hilos_en_cola;
	}
	
	
	/**
	 * Metodo resume. Libera un hilo bloqueado en esa cola.
	 * Ejecuta metodo notify()
	 */
	public synchronized void resume(){
		super.notify(); //El hilo sale de la cola
	}
	
	
	/**
	 * Metodo delay. El hilo se bloquea y entra a la cola.
	 * @throws InterruptedException
	 */
	public synchronized void delay() throws InterruptedException{
		this.cantidad_de_hilos_en_cola++;
		try{
			super.wait(); //El hilo entra a la cola, sumando la cantidad de hilos en cola
		}catch(InterruptedException e){
			//e.printStackTrace();
		}
		this.cantidad_de_hilos_en_cola--; //Cuando sale, resta la cantidad de hilos
	}
	
	
	/**
	 * Metodo delay. El hilo se bloquea y entra a la cola.
	 * @throws InterruptedException
	 */
	public synchronized void delay(long timeout) throws InterruptedException{
		this.cantidad_de_hilos_en_cola++;
		 try{
			if(timeout>0){ //Si esta vacia es el primer hilo
		            super.wait(timeout);
		     }
		     else{
		      
		            super.wait();
		     }
			
			
		 }
		 catch(InterruptedException e){
			
		 }
		this.cantidad_de_hilos_en_cola--; //Cuando sale, resta la cantidad de hilos
	}
	
	/**
	 * Metodo isEmpty. 
	 * @return boolean true si no hay hilos bloqueados en la cola.
	 */
	public boolean isEmpty(){ //Devuelve true si no hay hilos bloqueados en la cola
		if(cantidad_de_hilos_en_cola==0){
			return true;
		}
		else{
			return false;
		}
	}
}
