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
	public static boolean ejecutar_hilos=true;

	public static void main(String[] args) throws InterruptedException {
		
		Monitor monitor=Monitor.getInstance(); //Patron Singleton
		monitor.configRdp(""); //Configuro la red de petri para el monitor segun el path.
		
		//Politica 0: aleatoria.
		//Politica 1: primero los que suben.
		//Politica 2: primero los que bajan.
		monitor.setPolitica(0);
		
		
		//LogDeEventos log=new LogDeEventos(8);
		
		
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
		int[] transiciones_tren=new int[5];
		transiciones_tren[0]=0;
		transiciones_tren[1]=0;
		transiciones_tren[2]=0;
		transiciones_tren[3]=0;
		transiciones_tren[4]=0;
		transiciones_tren[5]=0;
		Thread tren_driver = new Thread(new TrenDriver(transiciones_tren,monitor));
            
		
		//Start hilos.
		generador_personasA.start();
		generador_personasB.start();
		generador_personasC.start();
		generador_personasD.start();
		generador_autos1.start();
		generador_autos2.start();
		
		control_bajada.start();
		
		autos_driver1.start();
		autos_driver2.start();
		
		subiendo_maquina_estacionA.start();
		subiendo_vagon_estacionA.start();
		subiendo_maquina_estacionB.start();
		subiendo_vagon_estacionB.start();
		subiendo_maquina_estacionC.start();
		subiendo_vagon_estacionC.start();
		subiendo_maquina_estacionD.start();
		subiendo_vagon_estacionD.start();
		
		bajando_maquina_estacionA.start();
		bajando_vagon_estacionA.start();
		bajando_maquina_estacionB.start();
		bajando_vagon_estacionB.start();
		bajando_maquina_estacionC.start();
		bajando_vagon_estacionC.start();
		bajando_maquina_estacionD.start();
		bajando_vagon_estacionD.start();
		
		tren_driver.start();

        
        
        tren_driver.join();
        Main.ejecutar_hilos=false;
        System.out.println("Finalizo la ejecucion del simulador Tren Concurrente 2017 en: ");
        
	}

}
