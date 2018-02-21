package Monitor;



import Logueo.Logger;



import java.io.File;
import jxl.*;





public class RedDePetri{
	private String path;
	private int cantidad_transiciones_inmediatas;
	
	private int[][] I; // Matriz de incidencia (Plazas x Transiciones)
	private int[][] M; // Matriz de marcado. 
	private int[][] pinvariantes; //Matriz de P-Invariantes
	private int[][] tinvariantes; //Matriz de T-Invariantes
	private int[] constante_pinvariante;
	private int[][] H; //Matriz H.
	private int[][] B; //Matriz B. B= H * Q
	private int[][] Q;
	private LogicaTemporal logica_temporal;
	private int[] transiciones_inmediatas; //Un uno indica que la transicion es inmediata.
	private int[] prioridades_subida; 
	private int[] prioridades_bajada;
	private Logger log;
	
	
	//Verificacion T-Invariantes
	//private List<Integer> transicionesDisparadas; //Vector con todos los disparos efectuados
	private int[][] M0; //Marcado inicial
	private int[][] suma_disparos_transiciones;
	private int contadorTransicionesDisparadas;
	boolean flagImpresionVerifTInv=false;
	
	
	
	
	
	public RedDePetri(String path, Logger log){
		this.path=path;
		this.setMatricesFromExcel(path);
		setCantTransiciones(I[1].length);
		logica_temporal=new LogicaTemporal(this.getCantTransiciones());
		this.logica_temporal.setVectorIntervalosFromExcel(this.path); 
		setTransicionesInmediatas();
		this.B=getMatrizB_Actualizada();
		this.logica_temporal.updateTimeStamp(this.getConjuncionEAndB(), this.getConjuncionEAndB(),  -1);
		//this.transicionesDisparadas=new ArrayList<Integer>();
		this.M0=this.M.clone();
		
		
		suma_disparos_transiciones=new int[this.getCantTransiciones()][1]; //Sumatoria de todos los disparos efectuados por transicion.
		for(int i=0; i<suma_disparos_transiciones.length;i++){ //Reseteo vector.
			  suma_disparos_transiciones[i][0]=0;
		  }
		contadorTransicionesDisparadas=0;
		this.setLogEventos(log);
	}
	
	
	public int getContadorTransicionesDisparadas(){
		return this.contadorTransicionesDisparadas;
	}
	
	public int[][] getSumaDisparosTransiciones(){
		return this.suma_disparos_transiciones;
	}
	
	/**public List<Integer> getTransicionesDisparadas(){
		return this.transicionesDisparadas;
	}**/
	
	public int[][] getMarcadoInicial(){
		return this.M0;
	}
	
	public int[] getPrioridadesSubida(){
		return this.prioridades_subida.clone();
	}
	
	public int[] getPrioridadesBajada(){
		return this.prioridades_bajada.clone();
	}
	
	
	public int[] getVectorTransicionesInmediatas(){
		return this.transiciones_inmediatas;
	}
	
	
	public void setTransicionesInmediatas(){
		this.transiciones_inmediatas=this.logica_temporal.construirVectorTransicionesInmediatas();
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
		return pinvariantes.clone();
	}
	public int[][] getTInv(){
		return tinvariantes;
	}
	
	
	/**
	 * Metodo setCantTransiciones. Permite setear la cantidad de transiciones de la red.
	 * @param cantidad Cantidad de transiciones de la red
	 */
	private void setCantTransiciones(int cantidad){
		this.cantidad_transiciones_inmediatas=cantidad;
	}
	
	/**
	 * Metodo getCantTransiciones. 
	 * @return int Cantidad de transiciones de la red
	 */
	public int getCantTransiciones(){ //Esto es lo unico que esta OK.
		return cantidad_transiciones_inmediatas;
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
                this.logica_temporal.updateTimeStamp(transSensAntesDisparo, this.getConjuncionEAndB(),  transicion);
                try{
                	this.verificarPInvariantes(); // En cada disparo verifico que se cumplan las ecuaciones del P-Invariante
                	
                	//this.transicionesDisparadas.add(transicion);
                	suma_disparos_transiciones[transicion][0]++;
                	this.contadorTransicionesDisparadas++;
                	this.log.createMessage("Cantidad de transiciones disparadas:\r\n"+String.valueOf(this.contadorTransicionesDisparadas), 2);
                	this.log.addMessage(String.valueOf(transicion)+ new String("\r\n"), 1);
                	
                	/**if(contadorTransicionesDisparadas>4){
                		this.contadorTransicionesDisparadas=0;
                		//this.verificarTInvariantes(this.getMatrizM());
                	}**/
                	
                }
                catch(Exception e){
                	e.printStackTrace();
                	System.out.println("Error en invariantes");
                	System.exit(1);
                }
                this.log.addMessage(new String("Valor de K: true\r\n"), 0);
        		this.log.addMessage(new String("Marcado PostDisparo:\r\n"), 0);
        		for(int plaza=0;plaza<this.M.length;plaza++){
        			this.log.addMessage(String.valueOf(this.M[plaza][0])+"-", 0);
        		}
        		this.log.addMessage("\r\n", 0);
                return true;
         
        }

        //System.out.println(transicion);
        this.log.addMessage("Valor de K: false\r\n", 0);
		this.log.addMessage("Marcado PostDisparo: \r\n", 0);
		for(int plaza=0;plaza<this.M.length;plaza++){
			this.log.addMessage(String.valueOf(this.M[plaza][0])+"-", 0);
		}
		this.log.addMessage("\r\n", 0);
	    return false;
	    

 }

	
	/**
	 * Metodo verificarPInvariantes.
	 * @throws IllegalStateException
	 * 
	 * El objetivo es verificar que la cantidad de marcas se mantiene constante en las plazas P-invariantes, para ello
	 * 	compara el valor que retorna gerMarcadoPinvariante() con el marcado inicial "constante_pinvariante" obtenida al momento de configurar la red de petri
	 */
	
	private void verificarPInvariantes() throws IllegalStateException{
		for (int i = 0; i < pinvariantes.length; i++) {	//Recorro todas las filas del pinvariantes
			if(gerMarcadoPinvariante()[i]!=this.constante_pinvariante[i]) { //verifico que cada solucion del P-invariante con el marcado actual sea igual a la solucion arrojada al momento de configurar la red de petri en "constante_pinvariante"
				throw new IllegalStateException("Error en los pinvariantes");	//en otras palabras, si el marcado en esas plazas resulta diferente, se dispara IllegalStateException
			}
		}
	}
	
	
	/**
	 * Metodo gerMarcadoPinvariante.
	 * @return int[]					Vector que contiene las soluciones a las ecuaciones de los P-invariantes
	 */
    private int[] gerMarcadoPinvariante() {
        int[] resultado=new int[pinvariantes.length];			//Inicializacion del vector resultado
        for (int i = 0; i < pinvariantes.length; i++) {			//Recorro las filas del vector pinvariantes
            for (int j = 0; j < pinvariantes[0].length; j++) {	// 'j' representa cada columna del vector pinvariantes[fila]
                resultado[i]=resultado[i]+(this.pinvariantes[i][j]*this.M[j][0]); //Multiplico pinvariantes[fila][columna] con marcado[fila][columna]
            }
        }
        
        return resultado; //retorno resultado, que contiene las soluciones a las ecuaciones de los pinvariantes
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
            tinvariantes = new int[filas-1][columnas];
            for (int i = 1; i < filas-1; i++) {
                for (int j = 0; j < columnas; j++) {
                	tinvariantes[i - 1][j] = Integer.parseInt(paginaExcelTInv.getCell(j,i).getContents());
                }
            }
            
            
            Sheet paginaExcelPInv = archivoExcelMatrices.getSheet(6);//carga los P-invariantes
            columnas = paginaExcelPInv.getColumns();
            filas = paginaExcelPInv.getRows();
            pinvariantes = new int[filas-1][columnas];
            for (int i = 1; i < filas-1; i++) {
                for (int j = 0; j < columnas; j++) {
                	pinvariantes[i - 1][j] = Integer.parseInt(paginaExcelPInv.getCell(j,i).getContents());
                }
            }
            
           
            
            
            this.constante_pinvariante=gerMarcadoPinvariante(); //Obtiene el resultado de las ecuaciones del P-invariante
            //System.out.println(constante_pinvariante.length);
            
            
            Sheet paginaExcel = archivoExcelMatrices.getSheet(8);//Prioridades subida
            columnas = paginaExcel.getColumns();
           // filas = paginaExcel.getRows();
            this.prioridades_subida = new int[columnas];
           
                for (int j = 0; j < columnas; j++) {
                	 this.prioridades_subida[j] = Integer.parseInt(paginaExcel.getCell(j,1).getContents());
                }
            
            
            paginaExcel = archivoExcelMatrices.getSheet(9);//Prioridades bajada
            columnas = paginaExcel.getColumns();
            //filas = paginaExcel.getRows();
            this.prioridades_bajada = new int[columnas];
            for (int j = 0; j < columnas; j++) {
                	 this.prioridades_bajada[j] = Integer.parseInt(paginaExcel.getCell(j,1).getContents());
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
        return this.tinvariantes;
    }
    
    public LogicaTemporal getLogicaTemporal() {
        return this.logica_temporal;
    }
    
    public int[] getSensibilizadasExtendido(){
    	int Ex[];
    	int E[]= this.getSensibilizadas();
    
    	int B[][]=this.getMatrizB_Actualizada();
    	
    	
    	int Baux[]=new int[B.length];
    	for(int i=0; i<B.length;i++){
    		Baux[i]=B[i][0];
    	}
    	int Z[]= logica_temporal.getVectorZ_Actualizado(this.getConjuncionEAndB());
    	
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
    
    
    
    public void setLogEventos(Logger log){
		this.log=log;
		this.log.createMessage("Evolucion del marcado: \r\n\r\nMarcado M0: \r\n", 0);
		for(int plaza=0;plaza<M0.length;plaza++){
			this.log.addMessage(String.valueOf(this.M0[plaza][0])+"-", 0);
		}
		this.log.addMessage("\r\n", 0);
	}
    
	
}