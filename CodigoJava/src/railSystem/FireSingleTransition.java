package railSystem;

import monitor.Monitor;


public class FireSingleTransition implements Runnable {
    private int transition_to_fire;
    private Monitor monitor;
   

    /**
     *
     * @param transition_to_fire transicion que disparara el hilo, mientras este sensibilizada.
     */
    public FireSingleTransition(int transition_to_fire, Monitor monitor) {
        this.transition_to_fire = transition_to_fire;
        this.monitor = monitor;     
    }

    
    @Override
    public void run() {
    	while(true){
    		monitor.dispararTransicion(transition_to_fire);
    	}
    }
}
