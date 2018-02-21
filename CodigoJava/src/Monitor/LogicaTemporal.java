/**
 * 
 */
package Monitor;

import java.io.File;


import jxl.Sheet;
import jxl.Workbook;

public class LogicaTemporal {
	private int cantidad_de_transiciones;
	
	private int[][] vector_de_intervalos;
	private Cronometro[] vector_de_time_stamps;
	private int [] vector_z; //Contiene un uno si el contador esta entre alfa y beta. De lo contrario un cero.
	
	
	public LogicaTemporal(int cantidad_de_transiciones){
		this.cantidad_de_transiciones=cantidad_de_transiciones;
		this.vector_de_time_stamps=new Cronometro[cantidad_de_transiciones];
		for (int i= 0; i < this.getCantidadDeTransiciones(); i++) {
			this.vector_de_time_stamps[i] = new Cronometro();
        }
		this.vector_z=new int[this.getCantidadDeTransiciones()];
		
	}
	
	/**
	 * Metodo getCantidadDeTransiciones.
	 * @return int Cantidad de transiciones de la red
	 */
	private int getCantidadDeTransiciones(){
		return this.cantidad_de_transiciones;
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
	         if((columnas-1)!=this.getCantidadDeTransiciones()){
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
	 */
	public void updateTimeStamp(int[] t_sensibilizadas_antes_disparar, int[] t_sensibilizadas_despues_disparar,  int t_a_disparar){
		
		if(t_a_disparar==-1) { //inicio de red, no se dispara ninguna t
			for (int transicion = 0; transicion < this.cantidad_de_transiciones; transicion++) {
				if(t_sensibilizadas_antes_disparar[transicion]==1) {
					this.vector_de_time_stamps[transicion].setNuevoTimeStamp();
				}

			}
		}
		
		for (int transicion = 0; transicion < this.cantidad_de_transiciones; transicion++) {
			
			if(t_sensibilizadas_antes_disparar[transicion]==1 && t_sensibilizadas_despues_disparar[transicion]==1) {
				if(transicion==t_a_disparar) {
					this.vector_de_time_stamps[transicion].setNuevoTimeStamp();
				}
			}
			else if(t_sensibilizadas_antes_disparar[transicion]==1 && t_sensibilizadas_despues_disparar[transicion]==0) {
				this.vector_de_time_stamps[transicion].resetearContador();
			}
			
			else if(t_sensibilizadas_antes_disparar[transicion]==0 && t_sensibilizadas_despues_disparar[transicion]==1) {
				this.vector_de_time_stamps[transicion].setNuevoTimeStamp();
			}
			
			else if(t_sensibilizadas_antes_disparar[transicion]==0 && t_sensibilizadas_despues_disparar[transicion]==0) {
				this.vector_de_time_stamps[transicion].resetearContador();
			}
		
		}
		this.updateVectorZ( t_sensibilizadas_despues_disparar);
	}
	
	
	/**
	 * Metodo updateVectorZ
	 */
	public void updateVectorZ(int[] q){
		
		for (int i= 0; i < this.cantidad_de_transiciones; i++) {
			if(isInWindowsTime(i) & q[i]==1) {
				vector_z[i]=1;
			}
			else {
				vector_z[i]=0;
			}
		}
	}
	/**
	 * Metodo getVectorZ
	 */
	public int[] getVectorZ_Actualizado(int[] q){
		this.updateVectorZ(q);
		return this.vector_z;
	}
	
	
	/**
	 * Metodo getVectorEstados
	 * 
	 * @return int[] vector de transiciones sensibilizadas y dentro de su ventana temporal.
	 */
	public int[] getVectorEstados(int[] transiciones_sensibilizadas) {
		int[] tSensAndWindowsTime = new int[this.cantidad_de_transiciones];
		for (int transicion = 0; transicion < this.cantidad_de_transiciones; transicion++) {
			if((transiciones_sensibilizadas[transicion]==1)&&isInWindowsTime(transicion)) {
				tSensAndWindowsTime[transicion]=1;
			}
			else {
				tSensAndWindowsTime[transicion]=0;
			}
		}
		return tSensAndWindowsTime;
	}
	
	
	/**
	 * Metodo isInWindowsTime
	 * 
	 * @return boolean true si se encuentra dentro de la ventana, de lo contrario false.
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
	
	
	
	public int[] construirVectorTransicionesInmediatas(){
		int aux[]=new int[this.getCantidadDeTransiciones()];
		for(int i=0; i< this.getCantidadDeTransiciones();i++){
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
	 * Metodo isInWindowsTime
	 * 
	 * @return boolean true si se encuentra dentro de la ventana, de lo contrario false.
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
			
			//System.out.println(this.vector_de_intervalos[transicion][0]-this.vector_de_time_stamps[transicion].getMillis());
			if((this.vector_de_intervalos[transicion][0]-this.vector_de_time_stamps[transicion].getMillis())<=0) {
				return (long)0;
			}
			else {
				//System.out.println(this.vector_de_intervalos[transicion][0]-this.vector_de_time_stamps[transicion].getMillis());
				return ((long)this.vector_de_intervalos[transicion][0]-this.vector_de_time_stamps[transicion].getMillis());
			}
		}
	
	}
	

}