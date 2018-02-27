package railSystem;

import acciones.Accion;
import monitor.Monitor;


public class FireSingleTransition implements Runnable {
    private int transition_to_fire;
    private Monitor monitor;
    private Accion accion;
   

    /**
     *
     * @param transition_to_fire transicion que disparara el hilo, mientras este sensibilizada.
     */
    public FireSingleTransition(int transition_to_fire,  Monitor monitor, Accion accion) {
        this.transition_to_fire = transition_to_fire;
        this.monitor = monitor; 
        this.accion=accion;
    }

    
    @Override
    public void run() {
    	while(monitor.getCondicion()){
    		monitor.dispararTransicion(transition_to_fire);
    		accion.ejecutarAccion();
    	}
    }
}
