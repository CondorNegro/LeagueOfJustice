package clasesPrincipales;

/*
 Trabajo Pr�ctico Integrador Programaci�n Concurrente 2017. (Ferrocarril).
 Autores: Casabella Mart�n, Kleiner Mat�as, L�pez Gast�n.
 */

public class Main {

	public static void main(String[] args) {
		//Creo el monitor y seteo matriz. (falta).
		
		//Creaci�n de hilos. (Falta ponerle objetos Runnables).
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
