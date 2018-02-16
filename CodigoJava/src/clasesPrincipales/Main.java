package clasesPrincipales;

/*
 Trabajo Practico Integrador Programacion Concurrente 2017. (Ferrocarril).
 Autores: Casabella Martin, Kleiner Matias, Lopez Gaston.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream; //Para logueo de eventos
import Monitor.Monitor; //Clase Monitor.
import Logueo.LogDeEventos;

public class Main {

	public static void main(String[] args) {
		
		Monitor monitor=Monitor.getInstance(); //Patron Singleton
		monitor.configRdp(""); //Configuro la red de petri para el monitor segun el path.
		
		//Pol�tica 0: aleatoria.
		//Pol�tica 1: primero los que suben.
		//Pol�tica 2: primero los que bajan.
		monitor.setPolitica(0);
		
		
		LogDeEventos log=new LogDeEventos(8);
		

		
		
		
		//Creacion de hilos. (Falta ponerle objetos Runnables).
	 	Thread manejoTren = new Thread(new TrenDriver(monitor,log,0));
        //Thread manejoAutos1 = new Thread(new AutosDriver(monitor,log, 1));
        //Thread manejoAutos2 = new Thread(new AutosDriver(monitor,log, 2));

       // Thread generadorPersonas = new Thread(new GeneradorPersonas(monitor,log, 3));
        //Thread generadorAutos = new Thread(new GeneradorAutos(monitor,log,4));
       
        Thread handlerVagon=new Thread(new SubeBajaVagon(monitor,log,5));
        Thread handlerMaquina=new Thread(new SubeBajaMaquina(monitor,log,6));
            
		
		//Start hilos.
        
        manejoTren.start();
      //  manejoAutos1.start();
        //manejoAutos2.start();
        //generadorPersonas.start();
       // generadorAutos.start();
        handlerVagon.start();
        handlerMaquina.start();
        
	}

}
