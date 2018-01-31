package Monitor;

import java.util.List;

public class Politica { 
	private int modoPolitica;
	
	public Politica(int modo){
		modoPolitica=modo;
		
	}
	
	
	
	private int politicaAleatoria(List<Integer> listaM){
		if(listaM.size()>0){
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
		else{
			return 0;
		}
	}
	
	
	private int politicaPrimeroSuben(List<Integer> listaM){
		return 0;
	}
	
	private int politicaPrimeroBajan(List<Integer> listaM){
		return 0;
	}
	
	
	
	
	public int cualDisparar(List<Integer> listaM){
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
	
	
}
