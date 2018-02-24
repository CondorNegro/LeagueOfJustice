
package monitor;

import java.io.File;


import jxl.Sheet;
import jxl.Workbook;

public class LogicaTemporal {
	
	/*
	 * Atributos de la clase LogicaTemporal
	 */
	private int cantidad_de_transiciones;
	private int[][] vector_de_intervalos; //Por cada transicion, contiene el alfa y beta
	private Cronometro[] vector_de_time_stamps;
	private int [] vector_z; //Contiene un uno si el contador esta entre alfa y beta. De lo contrario un cero.
	
	
	/**
	 * Constructor de LogicaTemporal, se inicializan las variables (Arreglos y Matrices) segun la cantidad de transiciones de la red.
	 * @param cantidad_de_transiciones cantidad de transiciones de la red.
	 */
	public LogicaTemporal(int cantidad_de_transiciones){
		this.cantidad_de_transiciones=cantidad_de_transiciones;
		this.vector_de_time_stamps=new Cronometro[cantidad_de_transiciones];
		for (int i= 0; i < this.cantidad_de_transiciones; i++) {
			this.vector_de_time_stamps[i] = new Cronometro();
        }
		this.vector_z=new int[this.cantidad_de_transiciones];
		
	}
	
	
	/**
	 * Metodo setVectorIntervalosFromExcel.
	 * @param libroExcel Libro de Excel en cuya hoja 9 se encuentran los vectores que contienen los valores de alfa y beta de las transiciones.
	 */
	public void setVectorIntervalosFromExcel(String path){
		 File file = new File(path);
	     Workbook libroExcel;
		try{
			libroExcel = Workbook.getWorkbook(file);
			 Sheet pagina = libroExcel.getSheet(7);
	         int columnas = pagina.getColumns();
	         
	       
	         int  filas = pagina.getRows();
	        
	         this.vector_de_intervalos = new int[columnas-1][filas-1];
	         for (int i = 1; i < columnas; i++) {
	             for (int j = 1; j < filas; j++) {
	                 this.vector_de_intervalos[i - 1][j - 1] = Integer.parseInt(pagina.getCell(i, j).getContents());
	             }
	         }
	         if((columnas-1)!=this.cantidad_de_transiciones){
	        	 throw new IllegalStateException("Error en la cantidad de transiciones");
	        	 
	         }
		}
		catch(Exception e){
			  e.printStackTrace();
	          System.out.println("Error en metodo setVectorIntervalosFromExcel");
		}
		
	}
	
	
	/**
	 * Metodo getVectorDeIntervalos
	 * @return int[][] Copia del vector de intervalos de tiempo.
	 */
	public int[][] getVectorDeIntervalos(){
		return this.vector_de_intervalos.clone();
	}
	
	
	/**
	 * Metodo updateTimeStamp
	 * Actualiza el vector de timeStamps o contadores.
	 * @param t_sensibilizadas_antes_disparar Vector de transiciones sensibilizadas antes de disparar la transicion elegida
	 * @param t_sensibilizadas_despues_disparar Vector de transiciones sensibilizadas despues de disparar la transicion elegida ( E AND B )
	 * @param t_a_disparar Transicion elegida para disparar
	 */
	public void updateTimeStamp(int[] t_sensibilizadas_antes_disparar, int[] t_sensibilizadas_despues_disparar,  int t_a_disparar){
		
		/*
		 * Con este primer if, lo que se hace es analizar cuales transiciones estan sensibilizadas al iniciar la red (sin haber disparado
		 * ni una transicion aun). Aquellas que se encuentren sensibilizadas (por tokens en plaza), se inicia el contador de 
		 * la transicion con setNuevoTimeStamp().
		 */
		if(t_a_disparar==-1) { // Se indica el nicio de red con -1 en el parametro transicion a disparar (no se dispara ninguna t)
			for (int transicion = 0; transicion < this.cantidad_de_transiciones; transicion++) {
				if(t_sensibilizadas_antes_disparar[transicion]==1) {
					this.vector_de_time_stamps[transicion].setNuevoTimeStamp();
				}

			}
		}
		
		/*
		 * De lo contrario, si la red tiene un marcado diferente al inicial (se disparo alguna transicion)
		 * se realiza lo siguiente:
		 */
		for (int transicion = 0; transicion < this.cantidad_de_transiciones; transicion++) { //Se recorren todas las transiciones
			
			if(t_sensibilizadas_antes_disparar[transicion]==1 && t_sensibilizadas_despues_disparar[transicion]==1) { //Si la transicion se encontraba sensibilizada antes del disparo y sigue sensibilizada despues de disparar, no se resetea el contador de la misma 
				if(transicion==t_a_disparar) { //A MENOS QUE justamente sea esa la transicion disparada (tiene que reinizializar su cronometro)
					this.vector_de_time_stamps[transicion].setNuevoTimeStamp();
				}
			}
			
			//Si la transicion estaba sensibilizada antes de disparar, y despues del disparo no se encuentra sensibilizada, el contador de la misma se pone a 0
			// (tiene que esperar sensibilizarse nuevamente para iniciar la cuenta).
			else if(t_sensibilizadas_antes_disparar[transicion]==1 && t_sensibilizadas_despues_disparar[transicion]==0) {
				this.vector_de_time_stamps[transicion].resetearContador();
			}
			
			//Si no estaba sensibilizada y despues de disparar si lo esta, se inicia la cuenta del cronometro alfa con setNuevoTimeStamp.
			else if(t_sensibilizadas_antes_disparar[transicion]==0 && t_sensibilizadas_despues_disparar[transicion]==1) {
				this.vector_de_time_stamps[transicion].setNuevoTimeStamp();
			}
			
			//Si no estaba sensibilizada y sigue sin estarlo despues del disparo, no se empieza la cuenta del cronometro alfa.
			else if(t_sensibilizadas_antes_disparar[transicion]==0 && t_sensibilizadas_despues_disparar[transicion]==0) {
				this.vector_de_time_stamps[transicion].resetearContador();
			}
		
		}
		this.updateVectorZ(t_sensibilizadas_despues_disparar); // (E AND B)
	}
	
	
	/**
	 * Metodo updateVectorZ
	 * Actualiza el vector Z.
	 * @param q Vector resultante de hacer una operacion AND entre el vector E y el vector B
	 */
	public void updateVectorZ(int[] q){
		
		for (int i= 0; i < this.cantidad_de_transiciones; i++) { //recorro todas las transiciones y hago and entre las sensibilizadas por E AND B y su ventana de tiempo
			if(isInWindowsTime(i) & q[i]==1) {
				vector_z[i]=1;
			}
			else {
				vector_z[i]=0;
			}
		}
	}
	
	/**
	 * Metodo getVectorZ_Actualizado
	 * Permite obtener la ultima version del vector Z
	 * @param  q Vector resultante de hacer una operacion AND entre el vector E y el vector B
	 * @return int[] Ultima version del vector Z.
	 */
	public int[] getVectorZ_Actualizado(int[] q){
		this.updateVectorZ(q);
		return this.vector_z;
	}
	
	
	/**
	 * Metodo isInWindowsTime
	 * @param transicion transicion a determinar si su contador se encuentra dentro de la ventana de tiempo
	 * @return boolean true si se encuentra dentro de la ventana, de lo contrario false.
	 * @throws IllegalArgumentException En caso de transicion invalida
	 */
	public boolean isInWindowsTime(int transicion) throws IllegalArgumentException{
		
		if(transicion>this.cantidad_de_transiciones) {
			throw new IllegalArgumentException("Transicion invalida");	
		}
		
		boolean comparacion1=this.vector_de_time_stamps[transicion].getMillis()>=(long)vector_de_intervalos[transicion][0];
		boolean comparacion2=this.vector_de_time_stamps[transicion].getMillis()<=(long)vector_de_intervalos[transicion][1];
		boolean comparacion3=(long)vector_de_intervalos[transicion][1]==(long)-1;
		boolean comparacion4=(long)vector_de_intervalos[transicion][0]==0;
		if((comparacion1&&(comparacion2||comparacion3))||construirVectorTransicionesInmediatas()[transicion]==1||comparacion4) {
			return true;
		}
		else {
			return false;
		}
	
	}
	
	
	/**
	 * Metodo construirVectorTransicionesInmediatas.
	 * Genera un vector de transiciones indicando con un uno si la transicion es inmediata o con un cero si no lo es.
	 * Lo que hace es fijarse si en la tabla del excel, la transicion correspondiente tiene alfa 0 y beta -1
	 * @return int[] vector de transiciones inmediatas
	 */
	public int[] construirVectorTransicionesInmediatas(){
		int aux[]=new int[this.cantidad_de_transiciones];
		for(int i=0; i < this.cantidad_de_transiciones;i++){
			if(this.vector_de_intervalos[i][0]==0 & this.vector_de_intervalos[i][1]==-1){
				aux[i]=1;
			}
			else{
				aux[i]=0;
			}
				
		}
		return aux;
	}
	
	
	/**
	 * Metodo getTiempoFaltanteParaAlfa. 
	 * @param transicion 
	 * @return long Tiempo faltante que posee el contador de la transicion para alcanzar el valor de alfa. 
	 * Devuelve cero en caso de haber alcanzado o superado dicho valor.
	 * @throws IllegalArgumentException En caso de transicion invalida
	 */
	public long getTiempoFaltanteParaAlfa(int transicion) throws IllegalArgumentException{
		
		if(transicion>this.cantidad_de_transiciones) {
			throw new IllegalArgumentException("Transicion invalida");	
		}
		
		boolean comparacion1=this.vector_de_time_stamps[transicion].getMillis()>=(long)vector_de_intervalos[transicion][0];
		boolean comparacion2=this.vector_de_time_stamps[transicion].getMillis()<=(long)vector_de_intervalos[transicion][1];
		boolean comparacion3=(long)vector_de_intervalos[transicion][1]==(long)-1;
		boolean comparacion4=(long)vector_de_intervalos[transicion][0]==0;
		if((comparacion1&&(comparacion2||comparacion3))||construirVectorTransicionesInmediatas()[transicion]==1||comparacion4) {
			return 0;
		}
		else {
			if((this.vector_de_intervalos[transicion][0]-this.vector_de_time_stamps[transicion].getMillis())<=0) {
				return (long)0;
			}
			else {
				return ((long)this.vector_de_intervalos[transicion][0]-this.vector_de_time_stamps[transicion].getMillis());
			}
		}
	
	}
}