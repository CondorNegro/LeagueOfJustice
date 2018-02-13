package clasesPrincipales;


import Monitor.Monitor;

public class SubeBajaVagon implements Runnable{

	private Monitor monitor;
	private int indiceLog;
	
	public SubeBajaVagon(Monitor monitor, int indiceLog){
		this.monitor=monitor;
		this.indiceLog=indiceLog;
	}
	public void run(){
		//Realizar disparos.
	}
	
}
