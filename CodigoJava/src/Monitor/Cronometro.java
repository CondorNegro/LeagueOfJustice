package Monitor;


public class Cronometro{
	public static final long MILLIS_TO_SECONDS = 1000;
	public static final long MILLIS_TO_MINUTES = 60000;
    public static final long MILLIS_TO_HOURS = 3600000;
    private long contador;
    private long millis;
    private boolean stop;

    public void setNuevoTimeStamp() {
    	contador = System.currentTimeMillis();

    }

    public void resetearContador() {
    	contador = 0;
    }
    
    public long getMillis() {
    	this.millis = System.currentTimeMillis() - contador;
        return this.millis;
    }
    
    public long getSeconds() {
    	this.millis = System.currentTimeMillis() - contador;
        return this.millis/MILLIS_TO_SECONDS;
    }
    
    public long getStartTime() {
        return this.contador;
    }

        

}
