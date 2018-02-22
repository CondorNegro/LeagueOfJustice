
package test;

//Objeto Runnable que dispara T0
public class HiloTransicionesCero implements Runnable {
	private monitor.Monitor monitor;
	
	public HiloTransicionesCero(monitor.Monitor monitor){
		this.monitor=monitor;
	}
	public void run() {
		this.monitor.dispararTransicion(0);

	}

}
