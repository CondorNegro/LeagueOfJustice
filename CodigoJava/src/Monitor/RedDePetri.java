package Monitor;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.*;
import jxl.*;





public class RedDePetri{
	private String path;
	private int cantTransiciones;
	
	private int[][] I; // Matriz de incidencia (Plazas x Transiciones)
	private int[] M; // Matriz de marcado. 
	
	
	
	public RedDePetri(String path){
		this.path=path;
		this.setMatricesFromExcel(path);
		
		
		
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
	
	
	/**
     * Matrices colocadas en páginas de Excel:
     *
     * Hoja 1:  I (matriz de incidencia)
     * Hoja 2:  M (matriz de marcado)
     * 
     */
    private void setMatricesFromExcel(String path) {
        File file = new File(path);
        Workbook archivoExcelMatrices;
        
        try {
        	archivoExcelMatrices = Workbook.getWorkbook(file);
        	Sheet paginaExcel0 = archivoExcelMatrices.getSheet(0);
            int columnas = paginaExcel0.getColumns();
            int filas = paginaExcel0.getRows();
            I = new int[filas - 1][columnas - 1];
            for (int i = 1; i < columnas; i++) {
                for (int j = 1; j < filas; j++) {
                    I[j - 1][i - 1] = Integer.parseInt(paginaExcel0.getCell(i, j).getContents());
                }
            }

            Sheet paginaExcel1= archivoExcelMatrices.getSheet(1);
            columnas = paginaExcel1.getColumns();
            M = new int[columnas - 1];
            for (int j = 1; j < columnas; j++) {
                M[j - 1] = Integer.parseInt(paginaExcel1.getCell(j, 1).getContents());
            }


          
           

        } 
        catch (Exception e) {
            e.printStackTrace();
        }

    }
	
	
	
	
	
}