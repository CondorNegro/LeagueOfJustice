package clasesPrincipales;

import java.io.PrintStream;
import Monitor.Monitor;

public class GeneradorAutos implements Runnable{

	private Monitor monitor;
	private PrintStream printLog;
	
	public GeneradorAutos(Monitor monitor, PrintStream printstream){
		this.monitor=monitor;
		printLog=printstream;
	}
	public void run(){
		//Realizar disparos.
	}

}
