package clasesPrincipales;

import java.io.PrintStream;
import Monitor.Monitor;

public class SubeBajaVagon implements Runnable{

	private Monitor monitor;
	private PrintStream printLog;
	
	public SubeBajaVagon(Monitor monitor, PrintStream printstream){
		this.monitor=monitor;
		printLog=printstream;
	}
	public void run(){
		//Realizar disparos.
	}

}
