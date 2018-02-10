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
	  * Metodo andVector.
	  * @param lista1 lista con enteros 1 y 0. 
	  * @param lista2 lista con enteros 1 y 0.
	  * @return List<Integer> Conteniendo el resultado de aplicar la operacion AND elemento a elemento entre lista1 y lista2
	  * @throws IndexOutOfBoundsException En caso de tener listas de diferentes tama�os
	  */
	 public static synchronized  int[] andVector(int[] lista1, int[] lista2) throws IndexOutOfBoundsException{
			if (lista1.length==lista2.length) {
				int[] resultado = new int[lista1.length];
	
				for(int i = 0; i < lista1.length; i++) {
					resultado[i]=lista1[i]&lista2[i];
				}   
	
				return resultado;
			}
	
			else{
				throw new IndexOutOfBoundsException("Listas de diferentes tamanios");
			}
	
		}
	 
	 
		/**
		 * Metodo isNotAllZeros. Devuelve true si al menos un elemento de la lista es distinto de cero, de lo contrario devuelve false
		 * @param lista Lista a evaluar
		 * @return boolean true si al menos un elemento de la lista es distinto de cero
		 */
		public static synchronized boolean isNotAllZeros(int[] lista){
			boolean var=false;
			 for (int i = 0; i < lista.length; i++) {
				if(lista[i]!=0) {
					var=true;
				}
			} 
			return var;
		}
		
		/**
		 * Metodo isNotAllZeros. Devuelve true si al menos un elemento de la lista es distinto de cero, de lo contrario devuelve false
		 * @param lista Lista a evaluar
		 * @return boolean true si al menos un elemento de la lista es distinto de cero
		 */
		public static synchronized boolean isNotAllZerosInt(int[] lista){
			boolean var=false;
			for (int i = 0; i<lista.length; i++) {
				if(lista[i]!=0) {
					var=true;
				}
			}
			
			return var;
		}
	 
	/**
	 * Metodo productoMatrices. Realiza el producto entre dos matrices (a x b) de datos tipo int.
	 * @param a Primer matriz
	 * @param b Segunda matriz
	 * @return int[][] resultado de la multiplicacion
	 * @throws IllegalArgumentException En caso de tener matrices que no cumplen con las condiciones para poder efectuar la multiplicacion
		    
	 */
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
		    	 throw new IllegalArgumentException("Las matrices no cumplen con las condiciones para poder efectuar la multiplicacion"); //si no se cumple la condici�n tira IllegalArgumentException
		    }
		    return c;
	 }
	 
	 
	 /**
		 * Metodo sumaMatrices. Realiza la suma entre dos matrices (a + b) de datos tipo int.
		 * @param a Primer matriz
		 * @param b Segunda matriz
		 * @return int[][] resultado de la suma
		 * @throws IllegalArgumentException En caso de tener matrices de diferentes tama�os
		 */
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
    		throw new IllegalArgumentException("Matrices de diferentes tamanios"); //si no se cumple la condici�n tira IllegalArgumentException
    	}
    	return c;
    }
    
   
	 
}
