package clasesPrincipales;


import Monitor.Monitor;

public class GeneradorPersonas implements Runnable{

	private Monitor monitor;
	private int indiceLog;
	
	public GeneradorPersonas(Monitor monitor, int indiceLog){
		this.monitor=monitor;
		this.indiceLog=indiceLog;
	}
	public void run(){
		//Realizar disparos.
	}
	

}


