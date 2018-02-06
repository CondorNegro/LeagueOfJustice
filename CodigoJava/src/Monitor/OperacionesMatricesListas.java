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
	
	 /**
	  * 
	  * 
	  * @param lista1 lista con enteros 1 y 0. 
	  * @param lista2 lista con enteros 1 y 0.
	  * @return List<Integer> Conteniendo el resultado de aplicar la operacion AND elemento a elemento entre lista1 y lista2
	  * @throws IndexOutOfBoundsException En caso de tener listas de diferentes tamanios
	  */
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
	 
	
	 public static synchronized int[][] productoMatrices(int[][] a, int[][] b) throws IllegalArgumentException {
		    int[][] c = new int[a.length][b[0].length]; //inicializo c
		    //se comprueba si las matrices se pueden multiplicar
		    if (a[0].length == b.length) {
		        for (int i = 0; i < a.length; i++) {
		            for (int j = 0; j < b[0].length; j++) {
		                for (int k = 0; k < a[0].length; k++) {
		                    //se multiplica la matriz
		                    c[i][j] += a[i][k] * b[k][j];
		                }
		            }
		        }
		    }
		    else { 
		    	 throw new IllegalArgumentException("Matrices diferentes tamanios"); //si no se cumple la condición tira IllegalArgumentException
		    }
		    return c;
	 }
	 
    public static synchronized int[][] sumaMatrices(int[][] a, int[][] b) throws IllegalArgumentException {
    	int[][] c = new int[a.length][a[0].length]; //inicializo c con mismos tamanios
    	if ((a[0].length == b[0].length)&&(a.length == b.length)) { //compruebo que a y b sean del mismo tamanio
		    for (int x=0; x < a.length; x++) { //recorro en un for y sumo los elementos de las matrices
		        for (int y=0; y < a[x].length; y++) {				
		          c[x][y]=a[x][y]+b[x][y];								
		        }
		    }
    	}
    	else {
    		throw new IllegalArgumentException("Matrices diferentes tamanios"); //si no se cumple la condición tira IllegalArgumentException
    	}
    	return c;
    }
    
   
	 
}
