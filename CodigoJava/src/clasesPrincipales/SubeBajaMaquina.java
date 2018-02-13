package clasesPrincipales;


import Monitor.Monitor;

public class SubeBajaMaquina implements Runnable{

	private Monitor monitor;
	private int indiceLog;
	
	public SubeBajaMaquina(Monitor monitor, int indiceLog){
		this.monitor=monitor;
		this.indiceLog=indiceLog;
	}
	public void run(){
		//Realizar disparos.
	}
	
}
 
