/**
 * 
 */
package UnitTests;

import static org.junit.Assert.fail;

public class HiloTransicionesUno implements Runnable {
private Monitor.Monitor monitor;
	
	public HiloTransicionesUno(Monitor.Monitor monitor){
		this.monitor=monitor;
	}
	public void run() {
		
			
				try{
					Thread.sleep(3);
				}
				catch(InterruptedException e){
					fail("Se gener� error por interrupci�n de thread");
				}
			
			
			this.monitor.dispararTransicion(1);
		}
}


