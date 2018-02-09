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
	private int[][] vectorDeEstado;
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
	public void updateTimeStamp(ArrayList<Integer> tSensibilizadasAntesDisparar, ArrayList<Integer> tSensibilizadasDespuesDisparar, int transicionDisparar){
		
		for (int transicion = 0; transicion < this.cantTransiciones; transicion++) {
			
			if(tSensibilizadasAntesDisparar.get(transicion)==1 && tSensibilizadasDespuesDisparar.get(transicion)==1) {
				if(transicion==transicionDisparar) {
					this.vectorDeTimeStamps[transicion].setNuevoTimeStamp();
				}
			}
			else if(tSensibilizadasAntesDisparar.get(transicion)==1 && tSensibilizadasDespuesDisparar.get(transicion)==0) {
				this.vectorDeTimeStamps[transicion].resetearContador();
			}
			
			else if(tSensibilizadasAntesDisparar.get(transicion)==0 && tSensibilizadasDespuesDisparar.get(transicion)==1) {
				this.vectorDeTimeStamps[transicion].setNuevoTimeStamp();
			}
			
			else if(tSensibilizadasAntesDisparar.get(transicion)==0 && tSensibilizadasDespuesDisparar.get(transicion)==0) {
				this.vectorDeTimeStamps[transicion].resetearContador();
			}
		
		}
	}
	
	
	/**
	 * Metodo updateVectorZ
	 */
	public void updateVectorZ(){
		for (int i= 0; i < this.cantTransiciones; i++) {
			if(isInWindowsTime(i)) {
				vectorZ[i]=1;
			}
			else {
				vectorZ[i]=0;
			}
		}
	}
	
	
	/**
	 * Metodo getVectorEstados
	 * 
	 * @return ArrayList<Integer> vector de transiciones sensibilizadas y dentro de su ventana temporal.
	 */
	public ArrayList<Integer> getVectorEstados(ArrayList<Integer> transicionesSensibilizadas) {
		ArrayList<Integer> tSensAndWindowsTime = new ArrayList<>();
		for (int transicion = 0; transicion < this.cantTransiciones; transicion++) {
			if((transicionesSensibilizadas.get(transicion)==1)&&isInWindowsTime(transicion)) {
				tSensAndWindowsTime.add(1);
			}
			else {
				tSensAndWindowsTime.add(0);
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
		
		boolean comparacion1=this.vectorDeTimeStamps[transicion].getMillis()>=(long)vectorDeIntervalos[0][transicion];
		boolean comparacion2=this.vectorDeTimeStamps[transicion].getMillis()<=(long)vectorDeIntervalos[1][transicion];
		boolean comparacion3=(long)vectorDeIntervalos[1][transicion]==(long)-1;
		
		if(comparacion1&&(comparacion2||comparacion3)) {
			return true;
		}
		else {
			return false;
		}
	
	}
	
	
	
	public int[][] construirVectorTransicionesInmediatas(){
		int aux[][]=new int[this.getCantTransiciones()][1];
		for(int i=0; i< this.getCantTransiciones();i++){
			if(this.vectorDeIntervalos[i][0]==0 & this.vectorDeIntervalos[i][1]==-1){
				aux[i][0]=1;
			}
			else{
				aux[i][0]=0;
			}
				
		}
		return aux;
	}

}
