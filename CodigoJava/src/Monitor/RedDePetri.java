package Monitor;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import jxl.*;





public class RedDePetri{
	private String path;
	private int cantTransiciones;
	
	private int[][] I; // Matriz de incidencia (Plazas x Transiciones)
	private int[][] M; // Matriz de marcado. 
	
	
	
	public RedDePetri(String path){
		this.path=path;
		this.setMatricesFromExcel(path);
		setCantTransiciones(I[1].length);
	}
	
	
	/**
	 * Metodo getMatrizM. Usado unicamente para Test.
	 * @return int[][] Marcado actual de la red.
	 */
	public int[][] getMatrizM(){
		return M;
	}
	
	
	/**
	 * Metodo setCantTransiciones. Permite setear la cantidad de transiciones de la red.
	 * @param cantidad Cantidad de transiciones de la red
	 */
	private void setCantTransiciones(int cantidad){
		this.cantTransiciones=cantidad;
	}
	
	/**
	 * Metodo getCantTransiciones. 
	 * @return int Cantidad de transiciones de la red
	 */
	public int getCantTransiciones(){ //Esto es lo unico que esta OK.
		return cantTransiciones;
	}
	
	/**
	 * Metodo getSensibilizadas(). Permite obtener el vector de transiciones sensibilizadas.
	 * @return ArrayList<Integer> lista con enteros 1 y 0 indicando transiciones sensibilizadas o no, respectivamente.
	 */
	public ArrayList<Integer> getSensibilizadas() {
		ArrayList<Integer> transicionesSensibilizadas = new ArrayList<>();
		
	    for (int transicion = 0; transicion < getCantTransiciones(); transicion++) {
	            try {
	                if (esDisparoValido(getMarcadoSiguiente(transicion))) {
	                	  transicionesSensibilizadas.add(1);
	                }
	                else{
	                	 transicionesSensibilizadas.add(0);
	                }
	            } 
	            catch (IllegalArgumentException e) {
	            	e.printStackTrace();
	            	System.out.println("Transicion u operaciones matriciales invalidas");
	            }
	            catch (NullPointerException e) {
	            	e.printStackTrace();
	            	System.out.println("Marcado null");
	            }
	            catch (Exception e) {
	            	e.printStackTrace();
	            	System.out.println("Error en getSensibilizadas()");
	            }

	     }
	      
		return transicionesSensibilizadas;
	}
	
	
	/**
	 * Metodo disparar. Permite el disparo de una determina transicion.
	 * @param transicion Transicion a disparar
	 * @return boolean true si la transicion se disparo.
	 */
	public boolean disparar(int transicion){
		int[][] marcado_siguiente = this.getMarcadoSiguiente(transicion);
        if (esDisparoValido(marcado_siguiente)) {
                M = marcado_siguiente;
                return true;
         
        }


	    return false;
 }

	
	
	
	
	
	
	/**
	 * Metodo setMatricesFromExcel. Encargado de cargar las matrices que forman parte de los atributos a partir de un archivo de Excel. 
     * Matrices colocadas en paginas de Excel:
     *
     * Hoja 1:  I+
     * Hoja 2:  I-
     * Hoja 3:  I (matriz de incidencia)
     * Hoja 4:  H
     * Hoja 5:  M (matriz de marcado)
     * 
     * @param path Direccion absoluta donde se encuentra el archivo de Excel
     * 
     */
    private void setMatricesFromExcel(String path) {
        File file = new File(path);
        Workbook archivoExcelMatrices;
        
        try {
        	archivoExcelMatrices = Workbook.getWorkbook(file);
        	Sheet paginaExcelI = archivoExcelMatrices.getSheet(2);
            int columnas = paginaExcelI.getColumns();
            int filas = paginaExcelI.getRows();
            I = new int[filas - 1][columnas - 1];
            for (int i = 1; i < columnas; i++) {
                for (int j = 1; j < filas; j++) {
                    I[j - 1][i - 1] = Integer.parseInt(paginaExcelI.getCell(i, j).getContents());
                }
            }
            
            

            Sheet paginaExcelM= archivoExcelMatrices.getSheet(4);
            columnas = paginaExcelM.getColumns();
            M = new int[columnas - 1][1];
            for (int j = 1; j < columnas; j++) {
                M[j - 1][0]= Integer.parseInt(paginaExcelM.getCell(j, 1).getContents());
            }


          
           

        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en metodo setMatricesFromExcel");
        }

    }
    
    
    /**
     * Metodo getMarcadoSiguiente. Permite obtener el marcado siguiente al disparar una determinada transicion.
     * @param transicion transicion a disparar para obtener el marcado siguiente
     * @return int[][] Matriz del marcado siguiente
     * @throws IllegalArgumentException si el numero de transicion es invalido
     */
    public int[][] getMarcadoSiguiente(int transicion) throws IllegalArgumentException{
    	if (transicion >= this.getCantTransiciones()) {
            throw new IllegalArgumentException("Transicion invalida.");
        }

        //Vector de disparo.

        int[][] deltaDisparo = new int[I[1].length][1];
        deltaDisparo[transicion][0] = 1;
        
        return OperacionesMatricesListas.sumaMatrices(M,OperacionesMatricesListas.productoMatrices(I,deltaDisparo));
    }
    
    
    /**
     * Metodo esDisparoValido. Permite conocer si el marcado siguiente no tiene ningun elemento negativo, lo cual denota un disparo invalido
     * @param marcado_siguiente matriz de marcado siguiente obtenido al disparar una determinada transicion
     * @return boolean true si el arreglo marcado_siguiente pasado como parametro no tiene ningun elemento negativo
     * @throws NullPointerException si el parametro es null
     */
    
    public boolean esDisparoValido(int[][] marcado_siguiente) throws NullPointerException{
   
        if (marcado_siguiente==null){throw new NullPointerException("Marcado null.");}
        
        for (int i = 0; i < marcado_siguiente.length; i++) {
            for (int j = 0; j < marcado_siguiente[0].length; j++) {
                if (marcado_siguiente[i][j] < 0) { //Valor negativo. Disparo invalido.
                    return false;
                }
            }
        }
        return true;
    }
    
    
   
    
    
    
	
	
	
	
	
}