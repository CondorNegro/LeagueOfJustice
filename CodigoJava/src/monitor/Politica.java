package monitor;


public class Politica { 
	//Vector indicando la prioridad de las transiciones.
	//{2,5,4} -> indica que la transicion 2 es la de mayor prioridad. La t4 es la de menor prioridad.
	private int[] transiciones_prioritarias_subida; //Corregir
	private int[] transiciones_prioritarias_bajada; //Corregir
	
	
	private int modo_politica;
	//Modo 0: aleatoria.
    //Modo 1: primero los que suben.
    //Modo 2: primero los que bajan.
	
	
	
	public Politica(int modo){
		setModo(modo);
			
	}
	
	public void setPrioridades(int[] subida, int[] bajada){
		this.transiciones_prioritarias_bajada=bajada;
		this.transiciones_prioritarias_subida=subida;
	}
	
	/**
	 * Metodo getModo.
	 * @return int atributo modo_politica
	 */
	public int getModo(){
		return this.modo_politica;
	}
	
	/**
	 * Metodo setModo
	 * @param modo Modo con el que va a decidir la politica
	 */
	public void setModo(int modo){
		this.modo_politica=modo;
	}
	
	/**
	 * Metodo politicaAleatoria. Implementa la decision de cual disparar en base a la aleatoriedad.
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @param flagInmediatas boolean que indica si existen transiciones inmediatas con posibilidad de dispararse. 
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaAleatoria(int[] lista_m){
	
			int indice=0;
			boolean flag=true;
			while(flag){
				int numero_aleatorio = (int) (Math.random()*100);
				indice=numero_aleatorio % lista_m.length; //Defino una posicion aleatoria.
				
				if(lista_m[indice]!=0){ //Verifico si el numero aleatorio no coincide con una transicion que no se puede disparar
					
						flag=false;
					
				}
				
			}
			return indice;
		
	}
	
	/**
	 * Metodo politicaPrimeroSuben. Implementa la decision de cual disparar en base a darle mayor prioridad a la gente que tiene que subir al ferrocarril.
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaPrimeroSuben(int[] lista_m){
		
			int lista_aux[]=new int[lista_m.length];
			boolean flag_hay_prioritarias=false;
			for(int i=0;i<lista_m.length;i++){
				lista_aux[i]=0;
			}
			for(int i=0;i<transiciones_prioritarias_subida.length;i++){
		         if(lista_m[transiciones_prioritarias_subida[i]]==1){ //El 1 indica que se puede disparar
		             lista_aux[transiciones_prioritarias_subida[i]]=1;
		             flag_hay_prioritarias=true;
		         }
		         
		     }
			
			if(flag_hay_prioritarias){
				 return this.politicaAleatoria(lista_aux);
			}

			
		    return this.politicaAleatoria(lista_m); //Si no esta definida la prioridad, se utiliza la aleatoriedad.
			
	
	}
	
	/**
	 * Metodo politicaPrimeroBajan. Implementa la decision de cual disparar en base a darle mayor prioridad a la gente que tiene que bajar del ferrocarril.
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 */
	private int politicaPrimeroBajan(int[] lista_m){
		
		int lista_aux[]=new int[lista_m.length];
		boolean flag_hay_prioritarias=false;
		for(int i=0;i<lista_m.length;i++){
			lista_aux[i]=0;
		}
		for(int i=0;i<transiciones_prioritarias_bajada.length;i++){
	         if(lista_m[transiciones_prioritarias_bajada[i]]==1){ //El 1 indica que se puede disparar
	             lista_aux[transiciones_prioritarias_bajada[i]]=1;
	             flag_hay_prioritarias=true;
	         }
	         
	     }
		
		if(flag_hay_prioritarias){
			 return this.politicaAleatoria(lista_aux);
		}

			
		     return this.politicaAleatoria(lista_m); //Si no esta definida la prioridad, se utiliza la aleatoriedad.
			
	
	}
	
	
	
	/**
	 * Metodo cualDisparar. Metodo que indica cual transicion se dispara segun el modo de politica elegido.
	 * @param lista_m lista que contiene los enteros 1 y 0, representando con el 1 las transiciones que se pueden disparar.
	 * @return int indice que representa a la transicion a disparar del vector de transiciones
	 * @throws IndexOutOfBoundsException en caso de que lista_m sea una lista vacia.
	 */
	public int cualDisparar(int[] lista_m) throws IndexOutOfBoundsException{
		if(lista_m.length>0){
			if(this.modo_politica==0){ //Politica aleatoria.
				return politicaAleatoria(lista_m);
			}
			else if(this.modo_politica==1){ //Politica primero suben.
				return politicaPrimeroSuben(lista_m);
			}
			else if(this.modo_politica==2){ //Politica primero bajan.
				return politicaPrimeroBajan(lista_m);
			}
			else{
				return 0;
			}
		}
		else{
			throw new IndexOutOfBoundsException("Lista M vacia.");
		}
		
		
		
	}
	
	
	
	
}
