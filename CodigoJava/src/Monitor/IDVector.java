package Monitor;
import java.util.*;


public class IDVector {
	
	private int cantidadTransiciones;	
	private static List<Long> vectorID;
		
	
	/**
	 * 
	 * Constructor: se crea un arreglo cuyo tamano es igual a la cantidad de transiciones de la red
	 * @param cantTransiciones
	 */
	public IDVector(int cantTransiciones){
		this.cantidadTransiciones = cantTransiciones;
		vectorID = new ArrayList<Long>(cantTransiciones);
		
	}
		
	
	
	/**
	 * Metodo IDreset: resetea a 0 el ID del correspondiente hilo en el vector
	 * @return void
	 */
	public void IDreset(long threadID) {
		
		//
		if(vectorID.contains(threadID)){
		
			vectorID.add(vectorID.indexOf(threadID),(long) 0);
		}
		else{
				
			System.out.println("El hilo no existe en la lista de Hilos esperando");
			
		}
	}
	
	
	
	/**
	 * Metodo setEsperando: agrega el ID del hilo que arribo antes de alfa y pasa a estado waiting
	 * @return void
	 */
	public synchronized void setEsperando(long threadID){
		//Devuelve un long, por eso se efectua la conversión
					
		if(!vectorID.contains(threadID)){
			
			vectorID.add(threadID);
		
		} else{
			
			System.out.println("El hilo ya se encuentra en el vector");
			
		}
		
	}
	

	
	
}