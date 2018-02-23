package railSystem;

/*
 Trabajo Practico Integrador Programacion Concurrente 2017. (Ferrocarril).
 Autores: Casabella Martin, Kleiner Matias, Lopez Gaston.
 */



import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import monitor.Cronometro;
import monitor.Monitor;


public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		
		/*
		 * tiempo_transcurrido variable Cronometro para imprimir tiempo transcurrido desde el inicio del programa.
		 */
		Cronometro tiempo_transcurrido=new Cronometro();
		tiempo_transcurrido.setNuevoTimeStamp();
		
		/*
		 * Se setea el path donde se encuentra el excel de la red en cuestion, segun el SO y alumno que se tratase.
		 */
		String name_file="./RedesParaTest/TestTren/excelTren.xls";
		boolean flag_prueba_prioridades=true;
		
		if(!flag_prueba_prioridades){
			if((System.getProperty("os.name")).equals("Windows 10")){	
				 if(System.getProperty("user.name").equals("kzAx")){
					 name_file="..\\src\\RedesParaTest\\TestTren\\excelTren.xls";
				 }
				 else{
					 name_file="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TestTren\\excelTren.xls"; //Path para Windows.
				 }
			}
		}
		else{
			name_file="./RedesParaTest/TestTren/excelTrenPrioridades.xls";
			if((System.getProperty("os.name")).equals("Windows 10")){	
				 if(System.getProperty("user.name").equals("kzAx")){
					 name_file="..\\src\\RedesParaTest\\TestTren\\excelTrenPrioridades.xls";
				 }
				 else{
					 name_file="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TestTren\\excelTrenPrioridades.xls"; //Path para Windows.
				 }
			}
		}
				
		
		
		Monitor monitor=Monitor.getInstance(); //Patron Singleton
		monitor.configRdp(name_file); //Configuro la red de petri para el monitor segun el path.
		
		
		/*
		 * La politica puede ser:
		 *	0: aleatoria.
		 *	1: primero los que suben.
		 *	2: primero los que bajan.
		*/
		monitor.setPolitica(2);
		
		
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(26);  //creo un ThreadPoolExecutor de tamaño maximo 26 hilos
		
		
		//Inicio generadores - 6 hilos
		executor.execute(new FireSingleTransition(0,monitor));
		executor.execute(new FireSingleTransition(1,monitor));
		executor.execute(new FireSingleTransition(2,monitor));
		executor.execute(new FireSingleTransition(3,monitor));
		executor.execute(new FireSingleTransition(15,monitor));
		executor.execute(new FireSingleTransition(20,monitor));
		
		//Inicio control de bajada - 1 hilos
		executor.execute(new FireSingleTransition(24,monitor));
		
		//Inicio circulacion de autos por barrera - 2 hilos
		executor.execute(new FireSingleTransition(22,monitor));
		executor.execute(new FireSingleTransition(17,monitor));
		
		//Inicio pasajeros subiendo al tren/vagon - 8 hilos
		executor.execute(new FireSingleTransition(10,monitor));  // "Estacion A","Maquina"
		executor.execute(new FireSingleTransition(7,monitor));	 // "Estacion A","Vagon"
		executor.execute(new FireSingleTransition(9,monitor));	 // "Estacion B","Maquina"
		executor.execute(new FireSingleTransition(13,monitor));	 // "Estacion B","Vagon"
		executor.execute(new FireSingleTransition(23,monitor));  // "Estacion C","Maquina"
		executor.execute(new FireSingleTransition(12,monitor));  // "Estacion C","Vagon"
		executor.execute(new FireSingleTransition(6,monitor));   // "Estacion D","Maquina"
		executor.execute(new FireSingleTransition(11,monitor));  // "Estacion D","Vagon"
		
		//Inicio pasajeros bajando al tren/vagon - 8 hilos
		executor.execute(new FireSingleTransition(29,monitor));	 // "Estacion A","Maquina"
		executor.execute(new FireSingleTransition(31,monitor));  // "Estacion A","Vagon"
		executor.execute(new FireSingleTransition(32,monitor));  // "Estacion B","Maquina"
		executor.execute(new FireSingleTransition(33,monitor));  // "Estacion B","Vagon"
		executor.execute(new FireSingleTransition(25,monitor));  // "Estacion C","Maquina"
		executor.execute(new FireSingleTransition(26,monitor));  // "Estacion C","Vagon"
		executor.execute(new FireSingleTransition(27,monitor));  // "Estacion D","Maquina"
		executor.execute(new FireSingleTransition(28,monitor));  // "Estacion D","Vagon"

		//Inicio tren driver - 1 hilos
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
		executor.execute(new FireMultipleTransition(transiciones_tren,monitor));
		
        executor.shutdown();// no se van a aceptar mas tareas, pero espera finalizarse las que se encuentran en ejecucion    
		

		
        /*
         * 		TimeUnit.MINUTES     -		TimeUnit.SECONDS
         * 		Especifica el tiempo que espera el hilo (main) antes de continuar con la siguiente instruccion.				
         */
		executor.awaitTermination(35, TimeUnit.SECONDS);
		//executor.shutdownNow();
        
        monitor.writeLogFiles();
        
        System.out.format("Finalizo la ejecucion del simulador Tren Concurrente 2017 en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        System.exit(0);
	}

}