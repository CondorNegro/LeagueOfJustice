/**
 * 
 */
package Monitor;

import java.io.File;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

public class LogicaTemporal {
	private int cantTransiciones;
	private IDVector vectorID;
	private int[][] vectorDeIntervalos;
	private int[] vectorDeEstado;
	private Cronometro vectorDeTimeStamps[];
	private int [] vectorZ; //Contiene un uno si el contador esta entre alfa y beta. De lo contrario un cero.
	
	
	public LogicaTemporal(int cantTransiciones){
		this.cantTransiciones=cantTransiciones;
		this.vectorDeTimeStamps=new Cronometro[cantTransiciones];
		for (int i= 0; i < this.getCantTransiciones(); i++) {
			this.vectorDeTimeStamps[i] = new Cronometro();
        }
		this.vectorID=new IDVector(this.cantTransiciones);
		this.vectorZ=new int[this.getCantTransiciones()];
		
	}
	
	/**
	 * Metodo getCantTransiciones.
	 * @return int Cantidad de transiciones de la red
	 */
	private int getCantTransiciones(){
		return this.cantTransiciones;
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
	        
	         this.vectorDeIntervalos = new int[columnas-1][filas-1];
	         for (int i = 1; i < columnas; i++) {
	             for (int j = 1; j < filas; j++) {
	                 this.vectorDeIntervalos[i - 1][j - 1] = Integer.parseInt(pagina.getCell(i, j).getContents());
	             }
	         }
	         if((columnas-1)!=this.getCantTransiciones()){
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
		return this.vectorDeIntervalos.clone();
	}
	
	
	
	
	
	
	/**
	 * Metodo updateTimeStamp
	 */
	public void updateTimeStamp(int[] tSensibilizadasAntesDisparar, int[] tSensibilizadasDespuesDisparar,  int transicionDisparar){
		
		if(transicionDisparar==-1) { //inicio de red, no se dispara ninguna t
			for (int transicion = 0; transicion < this.cantTransiciones; transicion++) {
				if(tSensibilizadasAntesDisparar[transicion]==1) {
					this.vectorDeTimeStamps[transicion].setNuevoTimeStamp();
				}

			}
		}
		
		for (int transicion = 0; transicion < this.cantTransiciones; transicion++) {
			
			if(tSensibilizadasAntesDisparar[transicion]==1 && tSensibilizadasDespuesDisparar[transicion]==1) {
				if(transicion==transicionDisparar) {
					this.vectorDeTimeStamps[transicion].setNuevoTimeStamp();
				}
			}
			else if(tSensibilizadasAntesDisparar[transicion]==1 && tSensibilizadasDespuesDisparar[transicion]==0) {
				this.vectorDeTimeStamps[transicion].resetearContador();
			}
			
			else if(tSensibilizadasAntesDisparar[transicion]==0 && tSensibilizadasDespuesDisparar[transicion]==1) {
				this.vectorDeTimeStamps[transicion].setNuevoTimeStamp();
			}
			
			else if(tSensibilizadasAntesDisparar[transicion]==0 && tSensibilizadasDespuesDisparar[transicion]==0) {
				this.vectorDeTimeStamps[transicion].resetearContador();
			}
		
		}
		this.updateVectorZ( tSensibilizadasDespuesDisparar);
	}
	
	
	/**
	 * Metodo updateVectorZ
	 */
	public void updateVectorZ(int[] q){
		
		for (int i= 0; i < this.cantTransiciones; i++) {
			if(isInWindowsTime(i) & q[i]==1) {
				vectorZ[i]=1;
			}
			else {
				vectorZ[i]=0;
			}
		}
	}
	/**
	 * Metodo getVectorZ
	 */
	public int[] getVectorZ_Actualizado(int[] q){
		this.updateVectorZ(q);
		return this.vectorZ;
	}
	
	
	/**
	 * Metodo getVectorEstados
	 * 
	 * @return int[] vector de transiciones sensibilizadas y dentro de su ventana temporal.
	 */
	public int[] getVectorEstados(int[] transicionesSensibilizadas) {
		int[] tSensAndWindowsTime = new int[this.cantTransiciones];
		for (int transicion = 0; transicion < this.cantTransiciones; transicion++) {
			if((transicionesSensibilizadas[transicion]==1)&&isInWindowsTime(transicion)) {
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
		
		if(transicion>this.cantTransiciones) {
			throw new IllegalArgumentException("Transicion invalida");	
		}
		
		boolean comparacion1=this.vectorDeTimeStamps[transicion].getMillis()/1000>=(long)vectorDeIntervalos[transicion][0];
		boolean comparacion2=this.vectorDeTimeStamps[transicion].getMillis()/1000<=(long)vectorDeIntervalos[transicion][1];
		boolean comparacion3=(long)vectorDeIntervalos[transicion][1]==(long)-1;
		boolean comparacion4=(long)vectorDeIntervalos[transicion][0]==0;
		if((comparacion1&&(comparacion2||comparacion3))||construirVectorTransicionesInmediatas()[transicion]==1||comparacion4) {
			return true;
		}
		else {
			return false;
		}
	
	}
	
	
	
	public int[] construirVectorTransicionesInmediatas(){
		int aux[]=new int[this.getCantTransiciones()];
		for(int i=0; i< this.getCantTransiciones();i++){
			if(this.vectorDeIntervalos[i][0]==0 & this.vectorDeIntervalos[i][1]==-1){
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
		
		if(transicion>this.cantTransiciones) {
			throw new IllegalArgumentException("Transicion invalida");	
		}
		
		boolean comparacion1=this.vectorDeTimeStamps[transicion].getMillis()/1000>=(long)vectorDeIntervalos[transicion][0];
		boolean comparacion2=this.vectorDeTimeStamps[transicion].getMillis()/1000<=(long)vectorDeIntervalos[transicion][1];
		boolean comparacion3=(long)vectorDeIntervalos[transicion][1]==(long)-1;
		boolean comparacion4=(long)vectorDeIntervalos[transicion][0]==0;
		if((comparacion1&&(comparacion2||comparacion3))||construirVectorTransicionesInmediatas()[transicion]==1||comparacion4) {
			return 0;
		}
		else {
			return (this.vectorDeIntervalos[transicion][0]-this.vectorDeTimeStamps[transicion].getMillis());
		}
	
	}
	

}
