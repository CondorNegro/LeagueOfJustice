package clasesPrincipales;

/*
 Trabajo Práctico Integrador Programación Concurrente 2017. (Ferrocarril).
 Autores: Casabella Martín, Kleiner Matías, López Gastón.
 */

public class Main {

	public static void main(String[] args) {
		//Creo el monitor y seteo matriz. (falta).
		
		//Creación de hilos. (Falta ponerle objetos Runnables).
	 	Thread manejoTren = new Thread();
        Thread manejoAutos1 = new Thread();
        Thread manejoAutos2 = new Thread();

        Thread generadorPersonas = new Thread();
        Thread generadorAutos = new Thread();
       
        Thread handlerVagon=new Thread();
        Thread handlerMaquina=new Thread();
            
		
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
