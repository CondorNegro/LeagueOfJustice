
package Logueo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class LogDeEventos {
	
	

	private String[] nameFiles;
	private String[] messages;
	
	public LogDeEventos(int numberOfLogs){
		if(numberOfLogs<1){
			numberOfLogs=1;
		}
		if(numberOfLogs>15){
			numberOfLogs=15;
		}
		
	
	
		nameFiles=new String[numberOfLogs];
		messages=new String[numberOfLogs];
		for(int i=0; i<numberOfLogs;i++){
				   messages[i]=new String("");
				  		   
			    	if((System.getProperty("os.name")).equals("Windows 10")){	
						this.nameFiles[i]="..\\..\\LeagueOfJustice\\CodigoJava\\src\\Logueo\\logFile"+ this.getLetraAbecedario(i) +".txt"; 
					}
			    	else{
			    		this.nameFiles[i]="./Logueo/logFile"+ this.getLetraAbecedario(i) +".txt"; //Path para Linux.
					    
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
			FileWriter fw=new FileWriter(this.nameFiles[indice]);
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
	
	
	
	public char getLetraAbecedario(int indice) {
		char letra;
		return (char) ('A' + indice ); 
	}
}
