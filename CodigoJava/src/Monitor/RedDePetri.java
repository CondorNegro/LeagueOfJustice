package Monitor;

import java.util.ArrayList;
import java.util.List;

public class RedDePetri{
	private String path;
	private int cantTransiciones;
	
	
	public RedDePetri(String path){
		this.path=path;
		cantTransiciones=0; //Corregir
	}
	
	public int getNumeroTransiciones(){ //Esto es lo unico que esta OK.
		return cantTransiciones;
	}
	
	public List<Integer> getSensibilizadas() {
		ArrayList<Integer> transicionesSensibilizadas = new ArrayList<>();
		return transicionesSensibilizadas;
	}
	
	public boolean disparar(int transicion){
		return true;
	}
	
}