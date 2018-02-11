package Monitor;

import java.util.List;

public class Politica { 
	//Vector indicando la prioridad de las transiciones.
	//{2,5,4} -> indica que la transicion 2 es la de mayor prioridad. La t4 es la de menor prioridad.
	private static final int[] transicionesPrioritariasSubidaInmediatas={1,2,3,4}; //Corregir
	private static final int[] transicionesPrioritariasBajadaInmediatas={1,2,3,4}; //Corregir
	private static final int[] transicionesPrioritariasSubidaTemporal={5,6}; //Corregir
	private static final int[] transicionesPrioritariasBajadaTemporal={5,6};
	
	private int modoPolitica;
	//Modo 0: aleatoria.
    //Modo 1: primero los que suben.
    //Modo 2: primero los que bajan.
	
	private int[] transicionesInmediatas; //Contiene un uno si es inmediata.
	
	public Politica(int modo, int[] transicionesInmediatas){
		setModo(modo);
		this.transicionesInmediatas=transicionesInmediatas;
		
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
	 * Metodo politicaAleatoria. Implementa la decision de cual disparar en base a la aleatoriedad.
	 * @param listaM lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @param flagInmediatas boolean que indica si existen transiciones inmediatas con posibilidad de dispararse. 
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaAleatoria(int[] listaM, boolean flagInmediatas){
	
			int indice=0;
			boolean flag=true;
			while(flag){
				int numeroAleatorio = (int) (Math.random()*100);
				indice=numeroAleatorio % listaM.length; //Defino una posicion aleatoria.
				
				if(listaM[indice]!=0){ //Verifico si el numero aleatorio no coincide con una transicion que no se puede disparar
					if(!flagInmediatas){
						flag=false;
					}
					else{
						if(listaM[indice]==1 & this.transicionesInmediatas[indice]==1){
							flag=false;
						}
					}
				}
				
			}
			return indice;
		
	}
	
	/**
	 * Metodo politicaPrimeroSuben. Implementa la decision de cual disparar en base a darle mayor prioridad a la gente que tiene que subir al ferrocarril.
	 * @param listaM lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaPrimeroSuben(int[] listaM, boolean flagInmediatas){
		
			if(flagInmediatas){
				for(int i=0;i<transicionesPrioritariasSubidaInmediatas.length;i++){
			         if(listaM[transicionesPrioritariasSubidaInmediatas[i]]==1){ //El 1 indica que se puede disparar
			             return transicionesPrioritariasSubidaInmediatas[i];
			         }
			     }
			}
			else{
				for(int i=0;i<transicionesPrioritariasSubidaTemporal.length;i++){
			         if(listaM[transicionesPrioritariasSubidaTemporal[i]]==1){ //El 1 indica que se puede disparar
			             return transicionesPrioritariasSubidaTemporal[i];
			         }
			     }
			}
			
		     return this.politicaAleatoria(listaM, flagInmediatas); //Si no esta definida la prioridad, se utiliza la aleatoriedad.
			
	
	}
	
	/**
	 * Metodo politicaPrimeroBajan. Implementa la decision de cual disparar en base a darle mayor prioridad a la gente que tiene que bajar del ferrocarril.
	 * @param listaM lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaPrimeroBajan(int[] listaM, boolean flagInmediatas){
		
			
			if(flagInmediatas){
				for(int i=0;i<transicionesPrioritariasBajadaInmediatas.length;i++){
			         if(listaM[transicionesPrioritariasBajadaInmediatas[i]]==1){ //El 1 indica que se puede disparar
			             return transicionesPrioritariasBajadaInmediatas[i];
			         }
			     }
			}
			else{
				for(int i=0;i<transicionesPrioritariasBajadaTemporal.length;i++){
			         if(listaM[transicionesPrioritariasBajadaTemporal[i]]==1){ //El 1 indica que se puede disparar
			             return transicionesPrioritariasBajadaTemporal[i];
			         }
			     }
			}
		     return this.politicaAleatoria(listaM, flagInmediatas); //Si no esta definida la prioridad, se utiliza la aleatoriedad.
			
	
	}
	
	
	
	/**
	 * Metodo cualDisparar. Metodo que indica cual transicion se dispara segun el modo de politica elegido.
	 * @param listaM lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 * @throws IndexOutOfBoundsException en caso de que listaM sea una lista vacia.
	 */
	public int cualDisparar(int[] listaM, boolean flagInmediatas) throws IndexOutOfBoundsException{
		if(listaM.length>0){
			if(this.modoPolitica==0){ //Politica aleatoria.
				return politicaAleatoria(listaM, flagInmediatas);
			}
			else if(this.modoPolitica==1){ //Politica primero suben.
				return politicaPrimeroSuben(listaM, flagInmediatas);
			}
			else if(this.modoPolitica==2){ //Politica primero bajan.
				return politicaPrimeroBajan(listaM, flagInmediatas);
			}
			else{
				return 0;
			}
		}
		else{
			throw new IndexOutOfBoundsException("Lista M vacia.");
		}
		
		
		
	}
	
	
	
	public boolean isThereTransicionInmediataSensibilizada(int[] listaM) throws IndexOutOfBoundsException{
		if(listaM.length>0){
			boolean flag=false;
			for(int i=0; i<listaM.length;i++){
				if(listaM[i]==1 & this.transicionesInmediatas[i]==1){
					flag=true;
				}
			}
			return flag;
		}
		else{
			throw new IndexOutOfBoundsException("Lista M vacia.");
		}
	}
	
	
	
	
}
