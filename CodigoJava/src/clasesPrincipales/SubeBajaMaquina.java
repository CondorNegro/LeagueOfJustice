package clasesPrincipales;

import java.io.PrintStream;
import Monitor.Monitor;

public class SubeBajaMaquina implements Runnable{

	private Monitor monitor;
	private PrintStream printLog;
	
	public SubeBajaMaquina(Monitor monitor, PrintStream printstream){
		this.monitor=monitor;
		printLog=printstream;
	}
	public void run(){
		//Realizar disparos.
	}

}
 
