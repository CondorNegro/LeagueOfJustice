package monitor;

//Clase dedicada a los timestamps
public class Cronometro{
	public static final long MILLIS_TO_SECONDS = 1000;
	public static final long MILLIS_TO_MINUTES = 60000;
    public static final long MILLIS_TO_HOURS = 3600000;
    private long contador; //Variable contador
    private long millis;
   
    
    
    public Cronometro(){
    	this.contador=0;
    }
   
    /**
    * Metodo setNuevoTimeStamp. 
    * Setea al atributo contador la cantidad de tiempo transcurrido desde que comenzo a ejecutarse el programa.
    * (Estampa de tiempo)
    */

    public void setNuevoTimeStamp() {
    	contador = System.currentTimeMillis();
    }
    
    
    /**
     * Metodo resetearContador. (Transicion disparada o desensibilizada)
     */
    public void resetearContador() {
    	contador = -1;
    }
    
    
    /**
     * Metodo getMillis.
     * @return long Diferencia de tiempo entre el tiempo actual y el atributo contador en milisegundos. 
     * Devuelve -1 en caso de que contador se encuentre reseteado
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
     * Metodo getSeconds. Idem a metodo getMillis pero el tiempo es devuelto en segundos
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
     * @return long Atributo contador
     */
    public long getContador() {
        return this.contador;
    }

        

}
