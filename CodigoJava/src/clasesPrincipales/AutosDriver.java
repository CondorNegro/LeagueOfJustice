package clasesPrincipales;

import Monitor.Monitor;


public class AutosDriver implements Runnable {
    private int transicion_autos_cruzando;
    private Monitor monitor;
   

    /**
     *
     * @param transicion_autos_cruzando transicion que hace cruzar la barrera a los autos cuando el
     * tren se encuentra a una distancia superior a los 30 metros
     */
    public AutosDriver(int transicion_autos_cruzando, Monitor monitor) {
        this.transicion_autos_cruzando = transicion_autos_cruzando;
        this.monitor = monitor;
        
    }

    @Override
    public void run() {
    	while(Main.ejecutar_hilos==1){
    		monitor.dispararTransicion(transicion_autos_cruzando);
    	}
    }

}

