/**
 * 
 */
package test;


public class HiloTransicionesCero implements Runnable {
	private Monitor.Monitor monitor;
	
	public HiloTransicionesCero(Monitor.Monitor monitor){
		this.monitor=monitor;
	}
	public void run() {
		this.monitor.dispararTransicion(0);

	}

}
