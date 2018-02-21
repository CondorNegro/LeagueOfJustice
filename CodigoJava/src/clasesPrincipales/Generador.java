package clasesPrincipales;

import Monitor.Monitor;


public class Generador implements Runnable {
    private int transicion_generadora;
    private Monitor monitor;
   

    /**
     *
     * @param transicion_generadora transicion del generador a disparar
     */
    public Generador(int transicion_generadora,Monitor monitor) {
        this.transicion_generadora = transicion_generadora;
        this.monitor = monitor;
        
    }

    @Override
    public void run() {
        
    	while(true){
    		monitor.dispararTransicion(transicion_generadora);
    	}
    }

}

