package Monitor;

//Existe una cola por transicion

public class Cola { 
	private int cantHilosEnCola;
	
	public Cola(){
		this.cantHilosEnCola=0;
	}
	
	
	/**
	 * Metodo getCantHilosEnCola
	 * @return int cantHilosEnCola
	 */
	public int getCantHilosEnCola(){
		return this.cantHilosEnCola;
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
		this.cantHilosEnCola++;
		try{
			super.wait(); //El hilo entra a la cola, sumando la cantidad de hilos en cola
		}catch(InterruptedException e){
			//e.printStackTrace();
		}
		this.cantHilosEnCola--; //Cuando sale, resta la cantidad de hilos
	}
	
	
	/**
	 * Metodo delay. El hilo se bloquea y entra a la cola.
	 * @throws InterruptedException
	 */
	public synchronized void delay(long timeout) throws InterruptedException{
		this.cantHilosEnCola++;
		 try{
			if(timeout>0){ //Si esta vacía es el primer hilo
		            super.wait(timeout);
		     }
		     else{
		      
		            super.wait();
		     }
			
			
		 }
		 catch(InterruptedException e){
			
		 }
		this.cantHilosEnCola--; //Cuando sale, resta la cantidad de hilos
	}
	
	/**
	 * Metodo isEmpty. 
	 * @return boolean true si no hay hilos bloqueados en la cola.
	 */
	public boolean isEmpty(){ //Devuelve true si no hay hilos bloqueados en la cola
		if(cantHilosEnCola==0){
			return true;
		}
		else{
			return false;
		}
	}
}
