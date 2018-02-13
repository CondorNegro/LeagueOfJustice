
package Logueo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class LogDeEventos {
	private PrintStream[] printLogs; //Logueo de eventos
	private String[] nameFiles;
	private String[] messages;
	
	public LogDeEventos(){
		for(int i=0; i<7;i++){
			  messages[i]="";
			  		   
		    	if((System.getProperty("os.name")).equals("Windows 10")){	
					this.nameFiles[i]="..\\..\\LeagueOfJustice\\CodigoJava\\src\\Logueo\\logFile"+ i +".txt"; 
				}
		    	else{
		    		this.nameFiles[i]="./Logueo/logFile"+ i +".txt"; //Path para Linux.
				    
		    	}
			  try {
		            printLogs[i]=new PrintStream(new File(this.nameFiles[i]));
		        }
		        catch(FileNotFoundException e){
		        	e.printStackTrace();
		        	return;
		        }
		}
	}
	
	/**
	 * Metodo addMessage. Permite crear un mensaje nuevo en el archivo de logueo indicado con el indice. 
	 * @param message Mensaje a añadir
	 * @param indice ID del PrintStream
	 */
	public synchronized void createMessage(String message, int indice){
		this.messages[indice]=message;
	}
	
	/**
	 * Metodo addMessage. Permite añadir al final del archivo de logueo indicado por el indice, el mensaje pasado como parámetro.
	 * @param message Mensaje a añadir
	 * @param indice ID del PrintStream
	 */
	public synchronized void addMessage(String message, int indice){
		this.messages[indice]=this.messages[indice]+message;
	}
	
	
	/**
	 * Metodo getPrintStream.
	 * @param indice ID del PrintStream
	 * @return PrintStream Objeto de la clase PrintStream representado por el ID pasado como parametro
	 */
	public PrintStream getPrintStream(int indice) {
		return printLogs[indice];
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
	public void flushBufferToFile(int indice){
		this.printLogs[indice].println();
	}
}
