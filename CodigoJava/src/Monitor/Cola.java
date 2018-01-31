package Monitor;


public class Cola { 
	private int cantHilosEnCola;
	
	public Cola(){
		this.cantHilosEnCola=0;
	}
	
	public synchronized void resume(){
		notify();
	}
	
	
	public synchronized void delay() throws InterruptedException{
		this.cantHilosEnCola++;
		super.wait();
		this.cantHilosEnCola--;
	}
	
	public boolean isEmpty(){
		if(cantHilosEnCola==0){
			return true;
		}
		else{
			return false;
		}
	}
}
