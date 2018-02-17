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
		monitor.configRdp("./excelTren.xls"); //Configuro la red de petri para el monitor segun el path.
		
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
		Thread generador_autos1 = new Thread(new Generador(15,monitor));
		Thread generador_autos2 = new Thread(new Generador(20,monitor));
		
		//Creacion de hilo control de bajada - 1 hilos
		Thread control_bajada = new Thread(new ControlBajadaPasajeros(24,monitor));
		
		//Creacion de hilos circulacion de autos por barrera - 2 hilos
		Thread autos_driver1 = new Thread(new AutosDriver(22,monitor));
		Thread autos_driver2 = new Thread(new AutosDriver(17,monitor));
		
		//Creacion de hilos pasajeros subiendo al tren/vagon - 8 hilos
		Thread subiendo_maquina_estacionA = new Thread(new SubidaPasajerosEstacion(10,monitor,"Estacion A","Maquina"));
		Thread subiendo_vagon_estacionA = new Thread(new SubidaPasajerosEstacion(17,monitor,"Estacion A","Vagon"));
		Thread subiendo_maquina_estacionB = new Thread(new SubidaPasajerosEstacion(19,monitor,"Estacion B","Maquina"));
		Thread subiendo_vagon_estacionB = new Thread(new SubidaPasajerosEstacion(13,monitor,"Estacion B","Vagon"));
		Thread subiendo_maquina_estacionC = new Thread(new SubidaPasajerosEstacion(23,monitor,"Estacion C","Maquina"));
		Thread subiendo_vagon_estacionC = new Thread(new SubidaPasajerosEstacion(12,monitor,"Estacion C","Vagon"));
		Thread subiendo_maquina_estacionD = new Thread(new SubidaPasajerosEstacion(6,monitor,"Estacion D","Maquina"));
		Thread subiendo_vagon_estacionD = new Thread(new SubidaPasajerosEstacion(11,monitor,"Estacion D","Vagon"));
		
		//Creacion de hilos pasajeros bajando al tren/vagon - 8 hilos
		Thread bajando_maquina_estacionA = new Thread(new BajadaPasajerosEstacion(29,monitor,"Estacion A","Maquina"));
		Thread bajando_vagon_estacionA = new Thread(new BajadaPasajerosEstacion(31,monitor,"Estacion A","Vagon"));
		Thread bajando_maquina_estacionB = new Thread(new BajadaPasajerosEstacion(32,monitor,"Estacion B","Maquina"));
		Thread bajando_vagon_estacionB = new Thread(new BajadaPasajerosEstacion(33,monitor,"Estacion B","Vagon"));
		Thread bajando_maquina_estacionC = new Thread(new BajadaPasajerosEstacion(25,monitor,"Estacion C","Maquina"));
		Thread bajando_vagon_estacionC = new Thread(new BajadaPasajerosEstacion(26,monitor,"Estacion C","Vagon"));
		Thread bajando_maquina_estacionD = new Thread(new BajadaPasajerosEstacion(27,monitor,"Estacion D","Maquina"));
		Thread bajando_vagon_estacionD = new Thread(new BajadaPasajerosEstacion(28,monitor,"Estacion D","Vagon"));

		//Creacion de hilo tren driver - 1 hilos
		int[] transiciones_tren=new int[14];
		transiciones_tren[0]=36;
		transiciones_tren[1]=35;
		transiciones_tren[2]=34;
		transiciones_tren[3]=18;
		transiciones_tren[4]=21;
		transiciones_tren[5]=21;
		transiciones_tren[6]=30;
		transiciones_tren[7]=19;
		transiciones_tren[8]=8;
		transiciones_tren[9]=4;
		transiciones_tren[10]=14;
		transiciones_tren[11]=16;
		transiciones_tren[12]=16;
		transiciones_tren[13]=5;
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
