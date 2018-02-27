package railSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
	private static String name_file_console="";
	private static String name_file="";
	private static final int cantidad_de_hilos=26;
	private static final int tiempo=35;
	private static final TimeUnit unidad=TimeUnit.SECONDS;
	private static final boolean flag_prueba_prioridades=true;
	/*
	 * La politica puede ser:
	 *	0: aleatoria.
	 *	1: primero los que suben.
	 *	2: primero los que bajan.
	*/
	private static final int politica=1;
	
	public static void main(String[] args) throws InterruptedException {
		
		setPath(name_file,name_file_console,flag_prueba_prioridades); //seteo los paths de los diferentes archivos a utilizar
		
		/*
		 * Seteo el log de acciones al directorio donde indique name_file_console
		 */
		PrintStream fileStream = null;
		try {
			fileStream=new PrintStream(new File(name_file_console));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		/*
		 * tiempo_transcurrido variable Cronometro para imprimir tiempo transcurrido desde el inicio del programa.
		 */
		Cronometro tiempo_transcurrido=new Cronometro();
		tiempo_transcurrido.setNuevoTimeStamp();
		
		
		Monitor monitor=Monitor.getInstance(); //Patron Singleton
		monitor.configRdp(name_file); //Configuro la red de petri para el monitor segun el path.
		
		
		
		monitor.setPolitica(politica);
		
		
		int cant_transiciones=monitor.getCantTransiciones();
		if(cant_transiciones==0){
			System.out.println("Error en getTransiciones del monitor");
			System.exit(1);
		}
		
		
		//Diccionario.
		HashMap<Integer,String> dictionary=new HashMap<Integer,String>();
		
		//Se escribe la referencia de transicion/accion en el diccionario
		writeDictionary(dictionary);
		
		//Acciones.
		HashMap<Integer,Accion> acciones=new HashMap<Integer,Accion>();
		for(int transicion=0; transicion<cant_transiciones;transicion++){
			acciones.put(transicion, new ConsolePrinter(dictionary.get(transicion),fileStream));
		} 
		
		
		//Inicializo ThreadPoolExecutor, maximo de cantidad_de_hilos.
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
		executor.awaitTermination(tiempo, unidad);
		
		monitor.setCondicion(false);
		
		/*
		 * Mientras no terminen todas las tareas, no sale del while. Evita que algun hilo se quede con el semaforo.
		 */
		while(!executor.isTerminated()) {
			
		}
		
		/*
		 * Finalizo completamente el executor
		 */
		executor.shutdownNow();
        
		/*
		 * Escribo todos los logs del sistema.
		 */
        monitor.writeLogFiles();
        
        
        
        /*
         * Fin del programa
         */
        fileStream.format("Finalizo la ejecucion del simulador Tren Concurrente 2017 en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        System.out.format("Finalizo la ejecucion del simulador Tren Concurrente 2017 en: %f minutos.",(double)tiempo_transcurrido.getSeconds()/(double)60);
        
        fileStream.format("\nQuedaron %d tareas por finalizar.",executor.getActiveCount());
        System.out.format("\nQuedaron %d tareas por finalizar.",executor.getActiveCount());
        System.exit(0);
	}
	
	
	
	
	
	
	
	
	public static void writeDictionary(HashMap<Integer,String> diccionario) {
		diccionario.put(0,"Nueva persona en estacion A");
		diccionario.put(1,"Nueva persona en estacion B");
		diccionario.put(2,"Nueva persona en estacion C");
		diccionario.put(3,"Nueva persona en estacion D");
		diccionario.put(4,"El tren partio de la estacion A");
		diccionario.put(5,"El tren llego a la estacion B");
		diccionario.put(6,"Pasajero subio a la maquina en estacion D");
		diccionario.put(7,"Pasajero subio al vagon en estacion A");
		diccionario.put(8,"El tren llego a la estacion A");
		diccionario.put(9,"Pasajero subio a la maquina en estacion B");
		diccionario.put(10,"Pasajero subio a la maquina en estacion A");
		diccionario.put(11,"Pasajero subio al vagon en estacion D");
		diccionario.put(12,"Pasajero subio al vagon en estacion C");
		diccionario.put(13,"Pasajero subio al vagon en estacion B");
		diccionario.put(14,"--TREN A 30 METROS DE LA BARRERA A-B--");
		diccionario.put(15,"Nuevo auto queriendo cruzar la barrera en A-B");
		diccionario.put(16,"--EL TREN SE ALEJO 20 METROS DE LA BARRERA A-B--");
		diccionario.put(17,"Cruza un auto en barrera A-B");
		diccionario.put(18,"--TREN A 30 METROS DE LA BARRERA C-D--");
		diccionario.put(19,"El tren partio de la estacion D");
		diccionario.put(20,"Nuevo auto queriendo cruzar la barrera en C-D");
		diccionario.put(21,"--EL TREN SE ALEJO 20 METROS DE LA BARRERA C-D--");
		diccionario.put(22,"Cruza un auto en barrera C-D");
		diccionario.put(23,"Pasajero subio a la maquina en estacion C");
		diccionario.put(24,"Autorizacion de bajada de pasajero");
		diccionario.put(25,"Pasajero se baja de la maquina en estacion C");
		diccionario.put(26,"Pasajero se baja del vagon en estacion C");
		diccionario.put(27,"Pasajero se baja de la maquina en estacion D");
		diccionario.put(28,"Pasajero se baja del vagon en estacion D");
		diccionario.put(29,"Pasajero se baja de la maquina en estacion A");
		diccionario.put(30,"El tren llego a la estacion D");
		diccionario.put(31,"Pasajero se baja del vagon en estacion A");
		diccionario.put(32,"Pasajero se baja de la maquina en estacion B");
		diccionario.put(33,"Pasajero se baja del vagon en estacion B");
		diccionario.put(34,"El tren partio de la estacion C");
		diccionario.put(35,"El tren llego a la estacion C");
		diccionario.put(36,"El tren partio de la estacion B");
	}
	
	public static void setPath(String name_fil,String name_file_consol,boolean flag_prioridad) {
		if(!flag_prioridad){
			if((System.getProperty("os.name")).equals("Windows 10")){	
				 if(System.getProperty("user.name").equals("kzAx")){
					 name_file="..\\src\\RedesParaTest\\TestTren\\excelTren.xls";
					 name_file_console="..\\src\\logueo\\logFileD.txt";
				 }
				 else{
					 name_file="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TestTren\\excelTren.xls"; //Path para Windows.
					 name_file_console="..\\..\\LeagueOfJustice\\CodigoJava\\src\\logueo\\logFileD.txt"; 
				 }
			}
			else {
				name_file="./RedesParaTest/TestTren/excelTren.xls";
				name_file_console="./logueo/logFileD.txt";
			}
		}
		else{
			if((System.getProperty("os.name")).equals("Windows 10")){	
				 if(System.getProperty("user.name").equals("kzAx")){
					 name_file="..\\src\\RedesParaTest\\TestTren\\excelTrenPrioridades.xls";
					 name_file_console="..\\src\\logueo\\logFileD.txt";
				 }
				 else{
					 name_file="..\\..\\LeagueOfJustice\\CodigoJava\\src\\RedesParaTest\\TestTren\\excelTrenPrioridades.xls"; //Path para Windows.
					 name_file_console="..\\..\\LeagueOfJustice\\CodigoJava\\src\\logueo\\logFileD.txt"; 
				 }
			}
			else {
				name_file="./RedesParaTest/TestTren/excelTrenPrioridades.xls";
				name_file_console="./logueo/logFileD.txt";
			}
		}
	}

}