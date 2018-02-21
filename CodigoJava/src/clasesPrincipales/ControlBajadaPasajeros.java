package clasesPrincipales;

import Monitor.Monitor;


public class ControlBajadaPasajeros implements Runnable {
    private int transicion_control_bajada;
    private Monitor monitor;
   

    /**
     *
     * @param transicion_control_bajada transicion que controla que la gente que se sube en una 
     * estacion X no se baje en la misma estacion X, sino que lo haga en alguna B
     */
    public ControlBajadaPasajeros(int transicion_control_bajada, Monitor monitor) {
        this.transicion_control_bajada = transicion_control_bajada;
        this.monitor = monitor;
        
    }

    @Override
    public void run() {
        
    	while(true){
    		monitor.dispararTransicion(transicion_control_bajada);
    	}
    }

}

