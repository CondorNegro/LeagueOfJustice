package clasesPrincipales;

import Monitor.Monitor;


public class SubidaPasajerosEstacion implements Runnable {
    private int transicion_subida;
    private Monitor monitor;
    private String identificador_estacion;
    private String identificador_tren_vagon;

/**
 * 
 * @param transicion_subida transicion que hace subir a un pasajero al tren o vagon
 * @param monitor
 * @param identificador_estacion  identifica si se trata del control de subida en la estacion A, B, C o D
 * @param identificador_tren_vagon identifica si se trata de la maquina o del vagon
 */
    public SubidaPasajerosEstacion(
    		int transicion_subida, 
    		Monitor monitor,
    		String identificador_estacion,
    		String identificador_tren_vagon
    		) {
        this.transicion_subida = transicion_subida;
        this.monitor = monitor;
        this.identificador_estacion = identificador_estacion;
        this.identificador_tren_vagon = identificador_tren_vagon;
        
    }

    @Override
    public void run() {
        
    	while(true){
    		monitor.dispararTransicion(transicion_subida);
    	}
    }

}

