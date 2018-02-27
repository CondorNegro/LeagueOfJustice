package railSystem;

import java.util.HashMap;

/*
 Trabajo Practico Integrador Programacion Concurrente 2017. (Ferrocarril).
 Autores: Casabella Martin, Kleiner Matias, Lopez Gaston.
 */



import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import acciones.Accion;
import acciones.ConsolePrinter;
import monitor.Cronometro;
import monitor.Monitor;


public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		final int cantidad_de_hilos=26;
		
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
		monitor.setPolitica(1);
		
		if(monitor.getCantTransiciones()==0){
			System.out.println("Error en getTransiciones del monitor");
			System.exit(1);
		}
		
		int cant_transiciones=monitor.getCantTransiciones();
		
		//Diccionario.
		HashMap<Integer,String> diccionary=new HashMap<Integer,String>();
		
		//Escritura del diccionario
		//diccionary.put(0,"");
		
		
		//Acciones.
		
		HashMap<Integer,Accion> acciones=new HashMap<Integer,Accion>();
		for(int transicion=0; transicion<cant_transiciones;transicion++){
			diccionary.put(transicion,"h");
			acciones.put(transicion, new ConsolePrinter(diccionary.get(transicion)));
		} 
		
		
		//ThreadPoolExecutor.
		ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(cantidad_de_hilos);  //creo un ThreadPoolExecutor de tamaño maximo 26 hilos
		
		
		//Inicio generadores - 6 hilos
		executor.execute(new FireSingleTransition(0,monitor, acciones.get(0)));
		executor.execute(new FireSingleTransition(1,monitor, acciones.get(1)));
		executor.execute(new FireSingleTransition(2,monitor, acciones.get(2)));
		executor.execute(new FireSingleTransition(3,monitor, acciones.get(3)));
		executor.execute(new FireSingleTransition(15,monitor, acciones.get(15)));
		executor.execute(new FireSingleTransition(20,monitor, acciones.get(20)));
		
		//Inicio control de bajada - 1 hilos
		executor.execute(new FireSingleTransition(24,monitor, acciones.get(24)));
		
		//Inicio circulacion de autos por barrera - 2 hilos
		executor.execute(new FireSingleTransition(22,monitor, acciones.get(22)));
		executor.execute(new FireSingleTransition(17,monitor, acciones.get(17)));
		
		//Inicio pasajeros subiendo al tren/vagon - 8 hilos
		executor.execute(new FireSingleTransition(10,monitor, acciones.get(10)));  // "Estacion A","Maquina"
		executor.execute(new FireSingleTransition(7,monitor, acciones.get(7)));	 // "Estacion A","Vagon"
		executor.execute(new FireSingleTransition(9,monitor, acciones.get(9)));	 // "Estacion B","Maquina"
		executor.execute(new FireSingleTransition(13,monitor, acciones.get(13)));	 // "Estacion B","Vagon"
		executor.execute(new FireSingleTransition(23,monitor, acciones.get(23)));  // "Estacion C","Maquina"
		executor.execute(new FireSingleTransition(12,monitor, acciones.get(12)));  // "Estacion C","Vagon"
		executor.execute(new FireSingleTransition(6,monitor, acciones.get(6)));   // "Estacion D","Maquina"
		executor.execute(new FireSingleTransition(11,monitor, acciones.get(11)));  // "Estacion D","Vagon"
		
		//Inicio pasajeros bajando al tren/vagon - 8 hilos
		executor.execute(new FireSingleTransition(29,monitor, acciones.get(29)));	 // "Estacion A","Maquina"
		executor.execute(new FireSingleTransition(31,monitor, acciones.get(31)));  // "Estacion A","Vagon"
		executor.execute(new FireSingleTransition(32,monitor, acciones.get(32)));  // "Estacion B","Maquina"
		executor.execute(new FireSingleTransition(33,monitor, acciones.get(33)));  // "Estacion B","Vagon"
		executor.execute(new FireSingleTransition(25,monitor, acciones.get(25)));  // "Estacion C","Maquina"
		executor.execute(new FireSingleTransition(26,monitor, acciones.get(26)));  // "Estacion C","Vagon"
		executor.execute(new FireSingleTransition(27,monitor, acciones.get(27)));  // "Estacion D","Maquina"
		executor.execute(new FireSingleTransition(28,monitor, acciones.get(28)));  // "Estacion D","Vagon"

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
		
		
		Accion[] acciones_tren=new Accion[14];
		for(int j=0; j<transiciones_tren.length;j++){
			acciones_tren[j]=acciones.get(transiciones_tren[j]);
		}
		executor.execute(new FireMultipleTransition(transiciones_tren,monitor, acciones_tren));
		
        executor.shutdown();// no se van a aceptar mas tareas, pero espera finalizarse las que se encuentran en ejecucion    
		

		
        /*
         * 		TimeUnit.MINUTES     -		TimeUnit.SECONDS
         * 		Especifica el tiempo que espera el hilo (main) antes de continuar con la siguiente instruccion.				
         */
		executor.awaitTermination(600, TimeUnit.SECONDS);
		
		monitor.setCondicion(false);
		
		while(!executor.isTerminated()) {
			
		}
		
		executor.shutdownNow();
        
        monitor.writeLogFiles();
        
        System.out.format("Finalizo la ejecucion del simulador Tren Concurrente 2017 en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        System.exit(0);
	}

}