package Monitor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

import java.io.File;
import jxl.*;





public class RedDePetri{
	private String path;
	private int cantTransiciones;
	
	private int[][] I; // Matriz de incidencia (Plazas x Transiciones)
	private int[][] M; // Matriz de marcado. 
	private int[][] Pinvariantes; //Matriz de P-Invariantes
	private int[][] Tinvariantes; //Matriz de T-Invariantes
	private int[] constantePinvariante;
	private int[][] H; //Matriz H.
	private int[][] B; //Matriz B. B= H * Q
	private int[][] Q;
	private LogicaTemporal logicaTemporal;
	private int[] transicionesInmediatas; //Un uno indica que la transicion es inmediata.
	private int[] prioridadesSubida; 
	private int[] prioridadesBajada;
	
	
	//Verificacion T-Invariantes
	private List<Integer> transicionesDisparadas; //Vector con todos los disparos efectuados
	private int[][] M0; //Marcado inicial
	private int[][] sumaDisparosTransiciones;
	private int contadorTransicionesDisparadas;
	
	
	
	public RedDePetri(String path){
		this.path=path;
		this.setMatricesFromExcel(path);
		setCantTransiciones(I[1].length);
		logicaTemporal=new LogicaTemporal(this.getCantTransiciones());
		this.logicaTemporal.setVectorIntervalosFromExcel(this.path); 
		setTransicionesInmediatas();
		this.B=getMatrizB_Actualizada();
		this.logicaTemporal.updateTimeStamp(this.getConjuncionEAndB(), this.getConjuncionEAndB(),  -1);
		this.transicionesDisparadas=new ArrayList<Integer>();
		this.M0=this.M.clone();
		sumaDisparosTransiciones=new int[this.getCantTransiciones()][1]; //Sumatoria de todos los disparos efectuados por transicion.
		for(int i=0; i<sumaDisparosTransiciones.length;i++){ //Reseteo vector.
			  sumaDisparosTransiciones[i][0]=0;
		  }
		contadorTransicionesDisparadas=0;
	}
	
	
	
	public List<Integer> getTransicionesDisparadas(){
		return this.transicionesDisparadas;
	}
	
	public int[][] getMarcadoInicial(){
		return this.M0;
	}
	
	public int[] getPrioridadesSubida(){
		return this.prioridadesSubida.clone();
	}
	
	public int[] getPrioridadesBajada(){
		return this.prioridadesBajada.clone();
	}
	
	
	public int[] getVectorTransicionesInmediatas(){
		return this.transicionesInmediatas;
	}
	
	
	public void setTransicionesInmediatas(){
		this.transicionesInmediatas=this.logicaTemporal.construirVectorTransicionesInmediatas();
	}
	
	
	/**
	 * Metodo getMatrizM. Usado unicamente para Test.
	 * @return int[][] Marcado actual de la red.
	 */
	public int[][] getMatrizM(){
		return M;
	}
	
	/**
	 * Metodo getMatrizH. Usado unicamente para Test.
	 * @return int[][] Matriz de inhibicion.
	 */
	private int[][] getMatrizH(){
		return H;
	}
	
	public int[][] getPInv(){
		return Pinvariantes.clone();
	}
	public int[][] getTInv(){
		return Tinvariantes;
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
	public int[] getSensibilizadas() {
		int[] transicionesSensibilizadas = new int[getCantTransiciones()];
		
	    for (int transicion = 0; transicion < getCantTransiciones(); transicion++) {
	            try {
	                if (esDisparoValido(getMarcadoSiguiente(transicion))) {
	                	  transicionesSensibilizadas[transicion]=1;
	                }
	                else{
	                	transicionesSensibilizadas[transicion]=0;
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
		//System.out.println(this.getSensibilizadasExtendido()[transicion]==1);
        if (this.getSensibilizadasExtendido()[transicion]==1) {
        		int[] transSensAntesDisparo=this.getConjuncionEAndB();
                M = marcado_siguiente;
                this.logicaTemporal.updateTimeStamp(transSensAntesDisparo, this.getConjuncionEAndB(),  transicion);
                try{
                	this.verificarPInvariantes(); // En cada disparo verifico que se cumplan las ecuaciones del P-Invariante
                	this.transicionesDisparadas.add(transicion);
                	this.contadorTransicionesDisparadas++;
                	if(contadorTransicionesDisparadas>4){
                		this.contadorTransicionesDisparadas=0;
                		this.verificarTInvariantes();
                	}
                }
                catch(Exception e){
                	e.printStackTrace();
                	System.out.println("Error en invariantes");
                }
                
                return true;
         
        }

        //System.out.println(transicion);
	    return false;
	    

 }

	
	/**
	 * Metodo verificarPInvariantes.
	 * @throws IllegalStateException
	 * 
	 * El objetivo es verificar que la cantidad de marcas se mantiene constante en las plazas P-invariantes, para ello
	 * 	compara el valor que retorna marcadoPinvariante() con el marcado inicial "constantePinvariante" obtenida al momento de configurar la red de petri
	 */
	
	private void verificarPInvariantes() throws IllegalStateException{
		for (int i = 0; i < Pinvariantes.length; i++) {	//Recorro todas las filas del Pinvariantes
			if(marcadoPinvariante()[i]!=this.constantePinvariante[i]) { //verifico que cada solucion del P-invariante con el marcado actual sea igual a la solucion arrojada al momento de configurar la red de petri en "constantePinvariante"
				throw new IllegalStateException("Error en los Pinvariantes");	//en otras palabras, si el marcado en esas plazas resulta diferente, se dispara IllegalStateException
			}
		}
	}
	
	
	
	private void verificarTInvariantes() throws IllegalStateException{
		 
		  
		  for(int indice=0; indice<this.transicionesDisparadas.size();indice++){ //Realizo la suma de disparos por cada transicion
			  sumaDisparosTransiciones[transicionesDisparadas.get(indice)][0]++;
		  }
		   
		 
		 
		  int[] minimo=new int[Tinvariantes.length];
		  
		
		  boolean flagPrimero=true;
		//Determino el numero de T-invariantes completos por cada fila de la matriz T-Invariantes.
		  for(int fila=0; fila<this.Tinvariantes.length;fila++){ 
			  minimo[fila]=0;
			  flagPrimero=true;
			 
			  for(int columna=0; columna<this.Tinvariantes[0].length;columna++){
				  if(flagPrimero & Tinvariantes[fila][columna]==1){
					  flagPrimero=false;
					  minimo[fila]=sumaDisparosTransiciones[columna][0];
				  }
				  
				  if(sumaDisparosTransiciones[columna][0]<minimo[fila]){
					  
					  minimo[fila]=sumaDisparosTransiciones[columna][0]; 
					  //Determino el minimo numero de transiciones disparadas por cada fila de la matriz de T-Inv.
					  
				  }  
			  }
			  
		  }
		  for(int fila=0; fila<this.Tinvariantes.length;fila++){ //Chequeo el numero de T-Invariantes completos.
			  
			  for(int columna=0; columna<this.Tinvariantes[0].length;columna++){
				  if(Tinvariantes[fila][columna]==1){
					  //Resto el minimo. 
					  sumaDisparosTransiciones[columna][0]=sumaDisparosTransiciones[columna][0]-minimo[fila];
					  if(sumaDisparosTransiciones[columna][0]<0){
						  throw new IllegalStateException("Suma de disparos negativa");	
					  }
				  }
				 
			  }
			  
		  }
		  
		//sumaDisparosTransiciones en este punto ya contiene el excedente de disparo de los T-Invariantes y la cantidad de disparos de transiciones que 
		  //no pertenecen a T-Invariantes.
		  
		
		  
		  //Verificacion de TInv
		  int[][] Maux = OperacionesMatricesListas.restaMatrices(this.getMatrizM(),OperacionesMatricesListas.productoMatrices(this.I, sumaDisparosTransiciones));
		  for(int plaza=0; plaza<Maux.length; plaza++){
			  if(Maux[plaza][0] != this.getMarcadoInicial()[plaza][0]){
				  throw new IllegalStateException("Error en los Tinvariantes");
			  }
		  }
		  
		//resetear lista
		  this.transicionesDisparadas.clear();
		//El contador de disparos de cada transicion de la red queda actualizado.
				
	}
	
	
	/**
	 * Metodo marcadoPinvariante.
	 * @return int[]					Vector que contiene las soluciones a las ecuaciones de los P-invariantes
	 */
    private int[] marcadoPinvariante() {
        int[] resultado=new int[Pinvariantes.length];			//Inicializacion del vector resultado
        for (int i = 0; i < Pinvariantes.length; i++) {			//Recorro las filas del vector Pinvariantes
            for (int j = 0; j < Pinvariantes[0].length; j++) {	// 'j' representa cada columna del vector Pinvariantes[fila]
                resultado[i]=resultado[i]+(this.Pinvariantes[i][j]*this.M[j][0]); //Multiplico Pinvariantes[fila][columna] con marcado[fila][columna]
            }
        }
        
        return resultado; //retorno resultado, que contiene las soluciones a las ecuaciones de los Pinvariantes
    }
	
	
	/**
	 * Metodo setMatricesFromExcel. Encargado de cargar las matrices que forman parte de los atributos a partir de un archivo de Excel. 
     * Matrices colocadas en paginas de Excel:
     *
     * Primer archivo de Excel
     * Hoja 1:  I+
     * Hoja 2:  I-
     * Hoja 3:  I (matriz de incidencia)
     * Hoja 4:  H
     * Hoja 5:  M (matriz de marcado)
     * Hoja 6:  T-invariantes
     * Hoja 7:  P-invariantes
     * Hoja 8:  Intervalos [alfa,beta]
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
            
            Sheet paginaExcelH = archivoExcelMatrices.getSheet(3);
            columnas = paginaExcelH.getColumns();
            filas = paginaExcelH.getRows();
           
            H = new int[filas - 1][columnas - 1];
            for (int i = 1; i < columnas; i++) {
                for (int j = 1; j < filas; j++) {
                    H[j - 1][i - 1] = Integer.parseInt(paginaExcelH.getCell(i, j).getContents());
                    
                }
               
            }
            
           
            

            Sheet paginaExcelM= archivoExcelMatrices.getSheet(4);
            columnas = paginaExcelM.getColumns();
            M = new int[columnas - 1][1];
            for (int j = 1; j < columnas; j++) {
                M[j - 1][0]= Integer.parseInt(paginaExcelM.getCell(j, 2).getContents());
            }
            
            
            
            Sheet paginaExcelTInv = archivoExcelMatrices.getSheet(5);//carga los T-invariantes
            columnas = paginaExcelTInv.getColumns();
            filas = paginaExcelTInv.getRows();
            Tinvariantes = new int[filas-1][columnas];
            for (int i = 1; i < filas-1; i++) {
                for (int j = 0; j < columnas; j++) {
                	Tinvariantes[i - 1][j] = Integer.parseInt(paginaExcelTInv.getCell(j,i).getContents());
                }
            }
            
            
            Sheet paginaExcelPInv = archivoExcelMatrices.getSheet(6);//carga los P-invariantes
            columnas = paginaExcelPInv.getColumns();
            filas = paginaExcelPInv.getRows();
            Pinvariantes = new int[filas-1][columnas];
            for (int i = 1; i < filas-1; i++) {
                for (int j = 0; j < columnas; j++) {
                	Pinvariantes[i - 1][j] = Integer.parseInt(paginaExcelPInv.getCell(j,i).getContents());
                }
            }
            
           
            
            
            this.constantePinvariante=marcadoPinvariante(); //Obtiene el resultado de las ecuaciones del P-invariante
            //System.out.println(constantePinvariante.length);
            
            
            Sheet paginaExcel = archivoExcelMatrices.getSheet(8);//Prioridades subida
            columnas = paginaExcel.getColumns();
           // filas = paginaExcel.getRows();
            this.prioridadesSubida = new int[columnas];
           
                for (int j = 0; j < columnas; j++) {
                	 this.prioridadesSubida[j] = Integer.parseInt(paginaExcel.getCell(j,1).getContents());
                }
            
            
            paginaExcel = archivoExcelMatrices.getSheet(9);//Prioridades bajada
            columnas = paginaExcel.getColumns();
            //filas = paginaExcel.getRows();
            this.prioridadesBajada = new int[columnas];
            for (int j = 0; j < columnas; j++) {
                	 this.prioridadesBajada[j] = Integer.parseInt(paginaExcel.getCell(j,1).getContents());
            }
            
            
           
            
            
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en metodo setMatricesFromExcel");
            
        }
        
       
    }
    
    
    /**
     * Metodo getMatrizB_Actualizada. 
     * Matriz B= Matriz H * Vector Q.
     * @return int[][] Matriz B. Utilizada en la ecuaci�n de estado.
     */
    private int[][] getMatrizB_Actualizada(){
    	this.Q=getVectorQ_Actualizado();
    	int Htranspuesta[][]=OperacionesMatricesListas.transpuesta(this.H);
    	int aux[][]=OperacionesMatricesListas.productoMatrices(Htranspuesta, this.Q);
    	/*
    	System.out.println("H: "+aux[0][0]);
    	System.out.println("H: "+aux[1][0]);
    	System.out.println("H: "+aux[2][0]);
    	System.out.println("H: "+aux[3][0]);
    	System.out.println("H: "+aux[4][0]);*/
    	for(int i=0; i<aux.length;i++){
    		if(aux[i][0]==0){
    			aux[i][0]=1;
    		}
    		else{
    			aux[i][0]=0;
    		}
    	}
    	
    	this.B=aux;
    	return this.B.clone();
    }
    
    /**
     * Metodo getVectorQ_Actualizado. 
     * El vector Q contiene un uno si la marca de la plaza es cero. 
     * De lo contrario posee un cero en esa posicion. 
     * @return int[][] Vector Q. 
     */
    private int[][] getVectorQ_Actualizado(){
    	int aux[][]=new int[this.M.length][1];
    	for(int i=0;i<this.M.length;i++){
    		if(M[i][0]!=0){
    			aux[i][0]=1;
    		}
    		else{
    			aux[i][0]=0;
    		}
    	}
    	return aux;
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
    
    
    private int[][] getTinvariant() {
        return this.Tinvariantes;
    }
    
    public LogicaTemporal getlogicaTemporal() {
        return this.logicaTemporal;
    }
    
    public int[] getSensibilizadasExtendido(){
    	int Ex[];
    	int E[]= this.getSensibilizadas();
    
    	int B[][]=this.getMatrizB_Actualizada();
    	
    	
    	int Baux[]=new int[B.length];
    	for(int i=0; i<B.length;i++){
    		Baux[i]=B[i][0];
    	}
    	int Z[]= logicaTemporal.getVectorZ_Actualizado(this.getConjuncionEAndB());
    	
    	Ex=OperacionesMatricesListas.andVector(OperacionesMatricesListas.andVector(E, Z),Baux);
    	
    	
    	return Ex;
    }
    
    public int[] getConjuncionEAndB(){
    	int E[]= this.getSensibilizadas();
        
    	int B[][]=this.getMatrizB_Actualizada();
    	
    	int Baux[]=new int[B.length];
    	for(int i=0; i<B.length;i++){
    		Baux[i]=B[i][0];
    	}
    	
    	
    	int q[]=OperacionesMatricesListas.andVector(Baux,E);

    	return q;
    }
    
	
}