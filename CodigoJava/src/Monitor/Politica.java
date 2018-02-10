package Monitor;

import java.util.List;

public class Politica { 
	//Vector indicando la prioridad de las transiciones.
	//{2,5,4} -> indica que la transici�n 2 es la de mayor prioridad. La t4 es la de menor prioridad.
	private static final int[] transicionesPrioritariasSubida={1,2,3,4}; //Corregir
	private static final int[] transicionesPrioritariasBajada={1,2,3,4}; //Corregir
	
	private int modoPolitica;
	//Modo 0: aleatoria.
    //Modo 1: primero los que suben.
    //Modo 2: primero los que bajan.
	
	public Politica(int modo){
		setModo(modo);
	}
	
	
	/**
	 * Metodo getModo.
	 * @return int atributo modoPolitica
	 */
	public int getModo(){
		return this.modoPolitica;
	}
	
	/**
	 * Metodo setModo
	 * @param modo Modo con el que va a decidir la politica
	 */
	public void setModo(int modo){
		this.modoPolitica=modo;
	}
	
	/**
	 * M�todo politicaAleatoria. Implementa la decision de cual disparar en base a la aleatoriedad.
	 * @param listaM lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaAleatoria(int[] listaM){
	
			int indice=0;
			boolean flag=true;
			while(flag){
				int numeroAleatorio = (int) (Math.random()*100);
				indice=numeroAleatorio % listaM.length; //Defino una posicion aleatoria.
				if(listaM[indice]!=0){ //Verifico si el numero aleatorio no coincide con una transicion que no se puede disparar
					flag=false;
				}
			}
			return indice;
		
	}
	
	/**
	 * M�todo politicaPrimeroSuben. Implementa la decision de cual disparar en base a darle mayor prioridad a la gente que tiene que subir al ferrocarril.
	 * @param listaM lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaPrimeroSuben(int[] listaM){
		
			
			for(int i=0;i<transicionesPrioritariasSubida.length;i++){
		         if(listaM[transicionesPrioritariasSubida[i]]==1){ //El 1 indica que se puede disparar
		             return transicionesPrioritariasSubida[i];
		         }
		     }
		     return this.politicaAleatoria(listaM); //Si no est� definida la prioridad, se utiliza la aleatoriedad.
			
	
	}
	
	/**
	 * M�todo politicaPrimeroBajan. Implementa la decision de cual disparar en base a darle mayor prioridad a la gente que tiene que bajar del ferrocarril.
	 * @param listaM lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaPrimeroBajan(int[] listaM){
		
			
			for(int i=0;i<transicionesPrioritariasBajada.length;i++){
		         if(listaM[transicionesPrioritariasBajada[i]]==1){ //El 1 indica que se puede disparar
		             return transicionesPrioritariasBajada[i];
		         }
		     }
		     return this.politicaAleatoria(listaM); //Si no est� definida la prioridad, se utiliza la aleatoriedad.
			
	
	}
	
	
	
	/**
	 * Metodo cualDisparar. Metodo que indica cual transicion se dispara segun el modo de politica elegido.
	 * @param listaM lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 * @throws IndexOutOfBoundsException en caso de que listaM sea una lista vacia.
	 */
	public int cualDisparar(int[] listaM) throws IndexOutOfBoundsException{
		if(listaM.length>0){
			if(this.modoPolitica==0){ //Politica aleatoria.
				return politicaAleatoria(listaM);
			}
			else if(this.modoPolitica==1){ //Pol�tica primero suben.
				return politicaPrimeroSuben(listaM);
			}
			else if(this.modoPolitica==2){ //Pol�tica primero bajan.
				return politicaPrimeroBajan(listaM);
			}
			else{
				return 0;
			}
		}
		else{
			throw new IndexOutOfBoundsException("Lista M vac�a.");
		}
		
		
		
	}
	
	
}
