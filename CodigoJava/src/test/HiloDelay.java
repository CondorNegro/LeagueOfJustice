
package test;
import static org.junit.Assert.*;


//Hilo que se duerme en Cola.
public class HiloDelay implements Runnable {
	private monitor.Cola cola;
	private boolean flag;
	
	public HiloDelay(monitor.Cola cola){
		this.cola=cola;
		flag=false;
	}
	
	public boolean getFlag(){
		return flag;
	}
	
	@Override
	public void run(){
		try{
			cola.delay();
			//System.out.println("Sali del wait");
		}
		catch(InterruptedException e){
			fail("Se genero error por interrupcion de thread");
		}
		flag=true;	
	}

}
