package Monitor;

//Existe una cola por transicion

public class Cola { 
	private int cantHilosEnCola;
	
	public Cola(){
		this.cantHilosEnCola=0;
	}
	
	public int getCantHilosEnCola(){
		return this.cantHilosEnCola;
	}
	
	public synchronized void resume(){
		notify(); //El hilo sale de la cola
	}
	
	
	public synchronized void delay() throws InterruptedException{
		this.cantHilosEnCola++;
		super.wait(); //El hilo entra a la cola
		this.cantHilosEnCola--; //Cuando sale, resta la cantidad de hilos
	}
	
	public boolean isEmpty(){ //Devuelve true si no hay hilos bloqueados en la cola
		if(cantHilosEnCola==0){
			return true;
		}
		else{
			return false;
		}
	}
}
