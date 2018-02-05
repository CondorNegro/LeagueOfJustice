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
		for(int i=0; i<2; i++){
			if(i==1){
				try{
					Thread.sleep(3);
				}
				catch(InterruptedException e){
					fail("Se generó error por interrupción de thread");
				}
			}
			
			this.monitor.dispararTransicion(1);
		}
	}

}
