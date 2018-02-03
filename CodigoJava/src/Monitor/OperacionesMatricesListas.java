/**
 * 
 */
package Monitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OperacionesMatricesListas {
	 public  OperacionesMatricesListas(){
		
	 }
	
	 public static synchronized  List<Integer> andVector(List<Integer> lista1, List<Integer> lista2) throws IndexOutOfBoundsException{
			if (lista1.size()==lista2.size()) {
				ArrayList<Integer> resultado = new ArrayList<>();
				Iterator<Integer> iterador1=lista1.iterator();
				Iterator<Integer> iterador2=lista2.iterator();
	
				while(iterador1.hasNext()&iterador2.hasNext()) {
		    		resultado.add(new Integer(iterador1.next().intValue()&iterador2.next().intValue()));
				}   
	
				return resultado;
			}
	
			else{
				throw new IndexOutOfBoundsException("Listas de diferentes tamanios");
			}
	
		}
	 
	 
	//Devuelve true si al menos un elemento de la lista es distinto de cero, de lo contrario devuelve false
	public static synchronized boolean isNotAllZeros(List<Integer> lista){
		Iterator<Integer> iterador=lista.iterator();
		boolean var=false;
		while(iterador.hasNext()) {
			if(iterador.next().intValue()!=0) {
				var=true;
			}
		} 
		return var;
	}
	 
	
	 public static synchronized int[][] sumaMatrices(int[][] a, int[][] b){
    	 int[][] deltaDisparo = new int[1][1];
    	 return deltaDisparo;
    }
    
    public static synchronized int[][] productoMatrices(int[][] a, int[][] b){
    	 int[][] deltaDisparo = new int[1][1];
    	 return deltaDisparo;
    }
	 
}
