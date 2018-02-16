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
		
		//Politica 0: aleatoria.
		//Politica 1: primero los que suben.
		//Politica 2: primero los que bajan.
		
		monitor.setPolitica(0);
		
		
		LogDeEventos log=new LogDeEventos(8);
		

		
		
		
		//Creacion de hilos generadores - 6 hilos
		Thread generador_personasA = new Thread(new Generador(0,monitor));
		Thread generador_personasB = new Thread(new Generador(1,monitor));
		Thread generador_personasC = new Thread(new Generador(2,monitor));
		Thread generador_personasD = new Thread(new Generador(3,monitor));
		Thread generador_autos1 = new Thread(new Generador(26,monitor));
		Thread generador_autos2 = new Thread(new Generador(30,monitor));
		
		//Creacion de hilo control de bajada - 1 hilos
		Thread control_bajada = new Thread(new ControlBajadaPasajeros(34,monitor));
		
		//Creacion de hilos circulacion de autos por barrera - 2 hilos
		Thread autos_driver1 = new Thread(new AutosDriver(28,monitor));
		Thread autos_driver2 = new Thread(new AutosDriver(32,monitor));
		
		//Creacion de hilos pasajeros subiendo al tren/vagon - 8 hilos
		Thread subiendo_maquina_estacionA = new Thread(new SubidaPasajerosEstacion(21,monitor,"Estacion A","Maquina"));
		Thread subiendo_vagon_estacionA = new Thread(new SubidaPasajerosEstacion(12,monitor,"Estacion A","Vagon"));
		Thread subiendo_maquina_estacionB = new Thread(new SubidaPasajerosEstacion(20,monitor,"Estacion A","Maquina"));
		Thread subiendo_vagon_estacionB = new Thread(new SubidaPasajerosEstacion(24,monitor,"Estacion A","Vagon"));
		Thread subiendo_maquina_estacionC = new Thread(new SubidaPasajerosEstacion(33,monitor,"Estacion A","Maquina"));
		Thread subiendo_vagon_estacionC = new Thread(new SubidaPasajerosEstacion(23,monitor,"Estacion A","Vagon"));
		Thread subiendo_maquina_estacionD = new Thread(new SubidaPasajerosEstacion(11,monitor,"Estacion A","Maquina"));
		Thread subiendo_vagon_estacionD = new Thread(new SubidaPasajerosEstacion(22,monitor,"Estacion A","Vagon"));
		
		//Creacion de hilos pasajeros bajando al tren/vagon - 8 hilos
		Thread bajando_maquina_estacionA = new Thread(new BajadaPasajerosEstacion(21,monitor,"Estacion A","Maquina"));
		Thread bajando_vagon_estacionA = new Thread(new BajadaPasajerosEstacion(12,monitor,"Estacion A","Vagon"));
		Thread bajando_maquina_estacionB = new Thread(new BajadaPasajerosEstacion(20,monitor,"Estacion A","Maquina"));
		Thread bajando_vagon_estacionB = new Thread(new BajadaPasajerosEstacion(24,monitor,"Estacion A","Vagon"));
		Thread bajando_maquina_estacionC = new Thread(new BajadaPasajerosEstacion(33,monitor,"Estacion A","Maquina"));
		Thread bajando_vagon_estacionC = new Thread(new BajadaPasajerosEstacion(23,monitor,"Estacion A","Vagon"));
		Thread bajando_maquina_estacionD = new Thread(new BajadaPasajerosEstacion(11,monitor,"Estacion A","Maquina"));
		Thread bajando_vagon_estacionD = new Thread(new BajadaPasajerosEstacion(22,monitor,"Estacion A","Vagon"));

		//Creacion de hilo tren driver - 1 hilos
		Thread tren_driver = new Thread(new TrenDriver(null,monitor));
		
		
        
            
		
		//Start hilos.
        
        //manejoTren.start();
      //  manejoAutos1.start();
        //manejoAutos2.start();
        //generadorPersonas.start();
       // generadorAutos.start();
        //handlerVagon.start();
        //handlerMaquina.start();
        
	}

}
