package clasesPrincipales;

/*
 Trabajo Práctico Integrador Programación Concurrente 2017. (Ferrocarril).
 Autores: Casabella Martín, Kleiner Matías, López Gastón.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import Monitor.Monitor;

public class Main {

	public static void main(String[] args) {
		
		Monitor monitor=Monitor.getInstance();
		monitor.configRdp("");
		
		//Política 0: aleatoria.
		//Política 1: primero los que suben.
		//Política 2: primero los que bajan.
		monitor.setPolitica(0);
		
		
		
		PrintStream salidaLog;
        try {
            salidaLog=new PrintStream(new File("./log.txt"));
        }
        catch(FileNotFoundException e){
        	e.printStackTrace();
        	return;
        }

		
		
		
		//Creación de hilos. (Falta ponerle objetos Runnables).
	 	Thread manejoTren = new Thread(new TrenDriver(monitor,salidaLog));
        Thread manejoAutos1 = new Thread(new AutosDriver(monitor, salidaLog));
        Thread manejoAutos2 = new Thread(new AutosDriver(monitor, salidaLog));

        Thread generadorPersonas = new Thread(new GeneradorPersonas(monitor, salidaLog));
        Thread generadorAutos = new Thread(new GeneradorAutos(monitor,salidaLog));
       
        Thread handlerVagon=new Thread(new SubeBajaVagon(monitor,salidaLog));
        Thread handlerMaquina=new Thread(new SubeBajaMaquina(monitor,salidaLog));
            
		
		//Start hilos.
        
        manejoTren.start();
        manejoAutos1.start();
        manejoAutos2.start();
        generadorPersonas.start();
        generadorAutos.start();
        handlerVagon.start();
        handlerMaquina.start();
        
	}

}
