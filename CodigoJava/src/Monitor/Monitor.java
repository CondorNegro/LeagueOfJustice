package Monitor;

//Se aplicó SINGLETON. 

public class Monitor { 
	//Elementos del monitor.
	private Politica politica;
    private Transiciones transiciones[];
    private RedDePetri rdp;
    private Semaphore mutex;
    
    //Aplicación de Singleton.
    private static final Monitor instance = new Monitor();
	 private Monitor(){
	        
	  }

	  public static Monitor getInstance(){return instance;}

}
