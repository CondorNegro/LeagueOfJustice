package clasesPrincipales;

import java.io.PrintStream;
import Monitor.Monitor;

public class AutosDriver {
	private Monitor monitor;
	private PrintStream printLog;
	
	public AutosDriver(Monitor monitor, PrintStream printstream){
		this.monitor=monitor;
		printLog=printstream;
	}
	public void run(){
		//Realizar disparos.
	}
	
}
