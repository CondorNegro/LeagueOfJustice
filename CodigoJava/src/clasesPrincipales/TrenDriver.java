package clasesPrincipales;


import Monitor.Monitor;


public class TrenDriver implements Runnable {
	private Monitor monitor;
	private int indiceLog;
	
	public TrenDriver(Monitor monitor, int indiceLog){
		this.monitor=monitor;
		this.indiceLog=indiceLog;
	}
	public void run(){
		//Realizar disparos.
	}
	

}
