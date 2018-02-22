
package logueo;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class Logger {
	
	

	private String[] name_files; //Nombres de archivos log.
	private String[] messages; //Mensajes a escribir en los archivos
	
	public Logger(int number_of_logs){ //Limite de 15 archivos de log.
		if(number_of_logs<1){
			number_of_logs=1;
		}
		if(number_of_logs>15){
			number_of_logs=15;
		}
		
	
	
		name_files=new String[number_of_logs];
		messages=new String[number_of_logs];
		for(int i=0; i<number_of_logs;i++){  //Inicializacion de atributos y manejo de paths
				   messages[i]=new String("");
				  		   
			    	if((System.getProperty("os.name")).equals("Windows 10")){	
			    		 if(System.getProperty("user.name").equals("kzAx")){
			    			 this.name_files[i]="..\\src\\Logueo\\logFile"+ this.getLetraAbecedario(i) +".txt"; 
						 }
						 else{
							 this.name_files[i]="..\\..\\LeagueOfJustice\\CodigoJava\\src\\Logueo\\logFile"+ this.getLetraAbecedario(i) +".txt"; 
						 }
			    	}
			    	else{
			    		this.name_files[i]="./Logueo/logFile"+ this.getLetraAbecedario(i) +".txt"; //Path para Linux.
					    
			    	}
				 
			}
	}
	
	/**
	 * Metodo createMessage. Permite crear un mensaje nuevo en el archivo de logueo indicado con el indice. 
	 * @param message Mensaje a anadir
	 * @param indice ID del PrintStream
	 */
	public synchronized void createMessage(String message, int indice){
		this.messages[indice]=message;
		
	}
	
	/**
	 * Metodo addMessage. Permite anadir al final del archivo de logueo indicado por el indice, el mensaje pasado como parametro.
	 * @param message Mensaje a anadir
	 * @param indice ID del PrintStream
	 */
	public synchronized void addMessage(String message, int indice){
		this.messages[indice]=this.messages[indice]+message;
	}
	
	
	
	/**
	 * Metodo getMessage.
	 * @param indice ID del PrintStream. Representa un archivo de logueo.
	 * @return String Mensaje contenido en el archivo y/o buffer del archivo de logueo.
	 */
	public String getMessage(int indice){
		return messages[indice];
	}
	
	/**
	 * Metodo flushBufferToFile. Permite imprimir el contenido del buffer en el archivo de logueo.
	 * @param indice Indice del PrintStream utilizado como buffer de logueo.
	 */
	public synchronized void flushBufferToFile(int indice){
		
		//this.getPrintWriter(indice).print(this.messages[indice]);
		try{
			
			//this.getBufferedWriter(indice).write(new String(this.messages[indice]));
		
			//this.getBufferedWriter(indice).flush();
			FileWriter fw=new FileWriter(this.name_files[indice]);
			BufferedWriter wr=new BufferedWriter(fw);
			wr.write(new String(this.messages[indice]));
			wr.flush();
			wr.close();
			fw.close();
			//this.getBufferedWriter(indice).close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Metodo que permite obtener una letra del abecedario.
	 * @param indice Numero de orden de la letra del abecedario. Cero es 'A'.
	 * @return char Letra del abecedario
	 */
	private synchronized char getLetraAbecedario(int indice) {
		char letra;
		return (char) ('A' + indice); 
	}
}
