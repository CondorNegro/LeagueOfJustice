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
	
	public int getModo(){
		return this.modoPolitica;
	}
	
	public void setModo(int modo){
		this.modoPolitica=modo;
	}
	
	
	private int politicaAleatoria(List<Integer> listaM){
	
			int indice=0;
			boolean flag=true;
			while(flag){
				int numeroAleatorio = (int) (Math.random()*100);
				indice=numeroAleatorio % listaM.size(); //Defino una posicion aleatoria.
				if(listaM.get(indice)!=0){ //Verifico si el numero aleatorio no coincide con una transicion que no se puede disparar
					flag=false;
				}
			}
			return indice;
		
	}
	
	
	private int politicaPrimeroSuben(List<Integer> listaM){
		
			
			for(int i=0;i<transicionesPrioritariasSubida.length;i++){
		         if(listaM.get(transicionesPrioritariasSubida[i])==1){ //El 1 indica que se puede disparar
		             return transicionesPrioritariasSubida[i];
		         }
		     }
		     return this.politicaAleatoria(listaM); //Si no est� definida la prioridad, se utiliza la aleatoriedad.
			
	
	}
	
	private int politicaPrimeroBajan(List<Integer> listaM){
		
			
			for(int i=0;i<transicionesPrioritariasBajada.length;i++){
		         if(listaM.get(transicionesPrioritariasBajada[i])==1){ //El 1 indica que se puede disparar
		             return transicionesPrioritariasBajada[i];
		         }
		     }
		     return this.politicaAleatoria(listaM); //Si no est� definida la prioridad, se utiliza la aleatoriedad.
			
	
	}
	
	
	
	//Metodo que indica cual transicion se dispara segun el modo de politica elegido.
	public int cualDisparar(List<Integer> listaM) throws IndexOutOfBoundsException{
		if(listaM.size()>0){
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
