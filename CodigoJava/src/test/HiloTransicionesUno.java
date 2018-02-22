
package test;

import static org.junit.Assert.fail;
//Objeto Runnable que dispara T1
public class HiloTransicionesUno implements Runnable {
private monitor.Monitor monitor;
	
	public HiloTransicionesUno(monitor.Monitor monitor){
		this.monitor=monitor;
	}
	public void run() {
		
			
				try{
					Thread.sleep(3);
				}
				catch(InterruptedException e){
					fail("Se genero error por interrupcion de thread");
				}
			
			
			this.monitor.dispararTransicion(1);
		}
}


