package clasesPrincipales;

import Monitor.Monitor;


public class BajadaPasajerosEstacion implements Runnable {
    private int transicion_bajada;
    private Monitor monitor;
    private String identificador_estacion;
    private String identificador_tren_vagon;

/**
 * 
 * @param transicion_bajada transicion que hace bajar a un pasajero del tren/vagon
 * @param monitor
 * @param identificador_estacion  identifica si se trata del control de bajada en la estacion A, B, C o D
 * @param identificador_tren_vagon identifica si se trata de la maquina o del vagon
 */
    public BajadaPasajerosEstacion(
    		int transicion_bajada, 
    		Monitor monitor,
    		String identificador_estacion,
    		String identificador_tren_vagon
    		) {
        this.transicion_bajada = transicion_bajada;
        this.monitor = monitor;
        this.identificador_estacion = identificador_estacion;
        this.identificador_tren_vagon = identificador_tren_vagon;
        
    }

    @Override
    public void run() {
        
    	while(Main.ejecutar_hilos){
    		monitor.dispararTransicion(transicion_bajada);
    	}
    }

}

