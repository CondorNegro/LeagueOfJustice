package clasesPrincipales;


import Monitor.Monitor;

public class AutosDriver implements Runnable{
	private Monitor monitor;
	private int indiceLog;
	
	public AutosDriver(Monitor monitor, int indiceLog){
		this.monitor=monitor;
		this.indiceLog=indiceLog;
	}
	public void run(){
		//Realizar disparos.
	}
	
}
