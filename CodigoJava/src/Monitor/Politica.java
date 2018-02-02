package Monitor;

import java.util.List;

public class Politica { 
	private static final int[] transicionesPrioritariasSubida={1,2,3,4}; //Corregir
	private static final int[] transicionesPrioritariasBajada={1,2,3,4}; //Corregir
	private int modoPolitica;
	
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
				indice=numeroAleatorio % listaM.size();
				if(listaM.get(indice)!=0){
					flag=false;
				}
			}
			return indice;
		
	}
	
	
	private int politicaPrimeroSuben(List<Integer> listaM){
		
			
			for(int i=0;i<transicionesPrioritariasSubida.length;i++){
		         if(listaM.get(transicionesPrioritariasSubida[i])==1){
		             return transicionesPrioritariasSubida[i];
		         }
		     }
		     return this.politicaAleatoria(listaM);
			
	
	}
	
	private int politicaPrimeroBajan(List<Integer> listaM){
		
			
			for(int i=0;i<transicionesPrioritariasBajada.length;i++){
		         if(listaM.get(transicionesPrioritariasBajada[i])==1){
		             return transicionesPrioritariasBajada[i];
		         }
		     }
		     return this.politicaAleatoria(listaM);
			
	
	}
	
	
	
	
	public int cualDisparar(List<Integer> listaM) throws IndexOutOfBoundsException{
		if(listaM.size()>0){
			if(this.modoPolitica==0){ //Politica aleatoria.
				return politicaAleatoria(listaM);
			}
			else if(this.modoPolitica==1){ //Política primero suben.
				return politicaPrimeroSuben(listaM);
			}
			else if(this.modoPolitica==2){ //Política primero bajan.
				return politicaPrimeroBajan(listaM);
			}
			else{
				return 0;
			}
		}
		else{
			throw new IndexOutOfBoundsException("Lista M vacía.");
		}
		
		
		
	}
	
	
}
