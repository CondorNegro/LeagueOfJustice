package clasesPrincipales;

/*
 Trabajo Practico Integrador Programacion Concurrente 2017. (Ferrocarril).
 Autores: Casabella Martin, Kleiner Matias, Lopez Gaston.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream; //Para logueo de eventos
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import Monitor.Cronometro;
import Monitor.Monitor; //Clase Monitor.
import Logueo.LogDeEventos;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		Cronometro tiempo_transcurrido=new Cronometro();
		tiempo_transcurrido.setNuevoTimeStamp();
		String name_file="./RedesParaTest/TestTren/excelTren.xls";
		if((System.getProperty("os.name")).equals("Windows 10")){	
			 if(System.getProperty("user.name").equals("kzAx")){
				 name_file="..\\src\\RedesParaTest\\TestTren\\excelTren.xls";
			 }
			 else{
				 name_file="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TestTren\\excelTren.xls"; //Path para Windows.
			 }
		}
		Monitor monitor=Monitor.getInstance(); //Patron Singleton
		monitor.configRdp(name_file); //Configuro la red de petri para el monitor segun el path.
		
		//Politica 0: aleatoria.
		//Politica 1: primero los que suben.
		//Politica 2: primero los que bajan.
		monitor.setPolitica(0);
		
		
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(26);  //creo un ThreadPoolExecutor de tamaño maximo 26 hilos
		
		
		//Creacion de hilos generadores - 6 hilos
		executor.execute(new Generador(0,monitor));
		executor.execute(new Generador(1,monitor));
		executor.execute(new Generador(2,monitor));
		executor.execute(new Generador(3,monitor));
		executor.execute(new Generador(15,monitor));
		executor.execute(new Generador(20,monitor));
		
		//Creacion de hilo control de bajada - 1 hilos
		executor.execute(new ControlBajadaPasajeros(24,monitor));
		
		//Creacion de hilos circulacion de autos por barrera - 2 hilos
		executor.execute(new AutosDriver(22,monitor));
		executor.execute(new AutosDriver(17,monitor));
		
		//Creacion de hilos pasajeros subiendo al tren/vagon - 8 hilos
		executor.execute(new SubidaPasajerosEstacion(10,monitor,"Estacion A","Maquina"));
		executor.execute(new SubidaPasajerosEstacion(7,monitor,"Estacion A","Vagon"));
		executor.execute(new SubidaPasajerosEstacion(9,monitor,"Estacion B","Maquina"));
		executor.execute(new SubidaPasajerosEstacion(13,monitor,"Estacion B","Vagon"));
		executor.execute(new SubidaPasajerosEstacion(23,monitor,"Estacion C","Maquina"));
		executor.execute(new SubidaPasajerosEstacion(12,monitor,"Estacion C","Vagon"));
		executor.execute(new SubidaPasajerosEstacion(6,monitor,"Estacion D","Maquina"));
		executor.execute(new SubidaPasajerosEstacion(11,monitor,"Estacion D","Vagon"));
		
		//Creacion de hilos pasajeros bajando al tren/vagon - 8 hilos
		executor.execute(new BajadaPasajerosEstacion(29,monitor,"Estacion A","Maquina"));
		executor.execute(new BajadaPasajerosEstacion(31,monitor,"Estacion A","Vagon"));
		executor.execute(new BajadaPasajerosEstacion(32,monitor,"Estacion B","Maquina"));
		executor.execute(new BajadaPasajerosEstacion(33,monitor,"Estacion B","Vagon"));
		executor.execute(new BajadaPasajerosEstacion(25,monitor,"Estacion C","Maquina"));
		executor.execute(new BajadaPasajerosEstacion(26,monitor,"Estacion C","Vagon"));
		executor.execute(new BajadaPasajerosEstacion(27,monitor,"Estacion D","Maquina"));
		executor.execute(new BajadaPasajerosEstacion(28,monitor,"Estacion D","Vagon"));

		//Creacion de hilo tren driver - 1 hilos
		int[] transiciones_tren=new int[14];
		transiciones_tren[0]=36; //temporal
		transiciones_tren[1]=35;
		transiciones_tren[2]=34; //temporal
		transiciones_tren[3]=18;
		transiciones_tren[4]=21; //temporal
		transiciones_tren[5]=21; //temporal
		transiciones_tren[6]=30;
		transiciones_tren[7]=19; //temporal
		transiciones_tren[8]=8;
		transiciones_tren[9]=4; //temporal
		transiciones_tren[10]=14;
		transiciones_tren[11]=16; //temporal
		transiciones_tren[12]=16; //temporal
		transiciones_tren[13]=5;
		executor.execute(new TrenDriver(transiciones_tren,monitor));
            
		

       
        
		executor.awaitTermination(2, TimeUnit.MINUTES); //finalizo la ejecucion del executor al terminar X minutos
        
        
        monitor.writeLogFiles();
        
        System.out.format("Finalizo la ejecucion del simulador Tren Concurrente 2017 en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        System.exit(0);
	}

}