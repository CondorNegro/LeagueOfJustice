package clasesPrincipales;

import java.io.PrintStream;
import Monitor.Monitor;


public class TrenDriver implements Runnable {
	private Monitor monitor;
	private PrintStream printLog;
	
	public TrenDriver(Monitor monitor, PrintStream printstream){
		this.monitor=monitor;
		printLog=printstream;
	}
	public void run(){
		//Realizar disparos.
	}

}
