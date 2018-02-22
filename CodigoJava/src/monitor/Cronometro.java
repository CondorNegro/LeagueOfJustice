package monitor;


public class Cronometro{
	public static final long MILLIS_TO_SECONDS = 1000;
	public static final long MILLIS_TO_MINUTES = 60000;
    public static final long MILLIS_TO_HOURS = 3600000;
    private long contador;
    private long millis;
   
    
    
    public Cronometro(){
    	this.contador=0;
    }
   /**
    * Metodo setNuevoTimeStamp. 
    */

    public void setNuevoTimeStamp() {
    	contador = System.currentTimeMillis();
    }
    
    
    /**
     * Metodo resetearContador.
     */
    public void resetearContador() {
    	contador = -1;
    }
    
    
    /**
     * Metodo getMillis.
     * @return long
     */
    public long getMillis() {
    	if(this.contador==-1) {
    		return (long)-1;
    	}
    	else {
    		this.millis = System.currentTimeMillis() - contador;
        	return this.millis;
    	}
    }
    

    /**
     * Metodo getSeconds.
     * @return long 
     */
    public long getSeconds() {
    	if(this.contador==-1) {
    		return (long)-1;
    	}
    	else {
    		this.millis = System.currentTimeMillis() - contador;
    		return this.millis/MILLIS_TO_SECONDS;
    	}
    }
    

    /**
     * Metodo getContador.
     * @return long
     */
    public long getContador() {
        return this.contador;
    }

        

}
