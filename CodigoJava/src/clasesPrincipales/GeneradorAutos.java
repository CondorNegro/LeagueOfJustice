package clasesPrincipales;


import Monitor.Monitor;

public class GeneradorAutos implements Runnable{

	private Monitor monitor;
	private int indiceLog;
	
	public GeneradorAutos(Monitor monitor, int indiceLog){
		this.monitor=monitor;
		this.indiceLog=indiceLog;
	}
	public void run(){
		//Realizar disparos.
	}
	

}
