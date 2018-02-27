package railSystem;

import acciones.Accion;
import monitor.Monitor;


public class FireMultipleTransition implements Runnable {
    private int[] transitions_to_fire;
    private Monitor monitor;
    private Accion[] acciones;

    
/**
 * 
 * @param transiciones_viaje arreglo de enteros que representan las transiciones que disparara el hilo  (viajes en el tren)
 * @param monitor
 */
    public FireMultipleTransition(
    		int[] transiciones_viaje, 
    		Monitor monitor,
    		Accion[] acciones
    		) {
        this.transitions_to_fire = transiciones_viaje;
        this.monitor = monitor;
        this.acciones=acciones;
        
    }

    @Override
    public void run() {
        int vueltas=0;
    	while(monitor.getCondicion()) {
    		for(int i=0; i<this.transitions_to_fire.length; i++) {
    			monitor.dispararTransicion(this.transitions_to_fire[i]);
    			acciones[i].ejecutarAccion();    			
    	       
    		}
    	}
    }

}

