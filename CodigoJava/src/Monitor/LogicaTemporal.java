/**
 * 
 */
package Monitor;

import jxl.Sheet;
import jxl.Workbook;

public class LogicaTemporal {
	private int cantTransiciones;
	private IDVector vectorID;
	private int[][] vectorDeIntervalos;
	private int[][] vectorDeEstado;
	private Cronometro vectorDeTimeStamps[];
	private int [][] vectorZ; //Contiene un uno si el contador esta entre alfa y beta. De lo contrario un cero.
	
	
	public LogicaTemporal(int cantDeTransiciones){
		this.cantTransiciones=cantTransiciones;
		this.vectorDeTimeStamps=new Cronometro[cantDeTransiciones];
		this.vectorID=new IDVector(this.cantTransiciones);
		this.vectorZ=new int[this.getCantTransiciones()][1];
		
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
	public void setVectorIntervalosFromExcel(Workbook libroExcel){
		try{
			 Sheet pagina = libroExcel.getSheet(8);
	         int columnas = pagina.getColumns();
	         int  filas = pagina.getRows();
	         this.vectorDeIntervalos = new int[columnas-1][filas-1];
	         for (int i = 1; i < columnas; i++) {
	             for (int j = 1; j < filas; j++) {
	                 this.vectorDeIntervalos[i - 1][j - 1] = Integer.parseInt(pagina.getCell(i, j).getContents());
	             }
	         }
	         if(columnas!=this.getCantTransiciones()){
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
	
    
	
	
	
	

}
