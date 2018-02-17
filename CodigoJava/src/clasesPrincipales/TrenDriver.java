package clasesPrincipales;

import Monitor.Monitor;


public class TrenDriver implements Runnable {
    private int[] transiciones_viaje;
    private Monitor monitor;

/**
 * 
 * @param transicion_subida transicion que hace subir a un pasajero al tren o vagon
 * @param monitor
 * @param identificador_estacion  identifica si se trata del control de subida en la estacion A, B, C o D
 * @param identificador_tren_vagon identifica si se trata de la maquina o del vagon
 */
    public TrenDriver(
    		int[] transiciones_viaje, 
    		Monitor monitor
    		) {
        this.transiciones_viaje = transiciones_viaje;
        this.monitor = monitor;
        
    }

    @Override
    public void run() {
        
    	for(int vueltas=0; vueltas<500; vueltas++) {
    		for(int i=0; i<this.transiciones_viaje.length; i++) {
    			monitor.dispararTransicion(this.transiciones_viaje[i]);
    		}
    	}
    }

}

