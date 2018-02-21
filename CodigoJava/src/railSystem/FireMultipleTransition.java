package railSystem;

import Monitor.Monitor;


public class FireMultipleTransition implements Runnable {
    private int[] transitions_to_fire;
    private Monitor monitor;

/**
 * 
 * @param transicion_subida transicion que hace subir a un pasajero al tren o vagon
 * @param monitor
 * @param identificador_estacion  identifica si se trata del control de subida en la estacion A, B, C o D
 * @param identificador_tren_vagon identifica si se trata de la maquina o del vagon
 */
    public FireMultipleTransition(
    		int[] transiciones_viaje, 
    		Monitor monitor
    		) {
        this.transitions_to_fire = transiciones_viaje;
        this.monitor = monitor;
        
    }

    @Override
    public void run() {
        int vueltas=0;
    	while(true) {
    		for(int i=0; i<this.transitions_to_fire.length; i++) {
    			monitor.dispararTransicion(this.transitions_to_fire[i]);
    	        System.out.println("El tren se movio, dispare la transicion: "+this.transitions_to_fire[i]+" - Vuelta: "+vueltas);
    	        if(i==transitions_to_fire.length-1) {
    	        	vueltas=vueltas+1;
    	        }

    		}
    	}
    }

}

