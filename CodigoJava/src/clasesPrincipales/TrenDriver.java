package clasesPrincipales;


import java.util.HashMap;

import Monitor.Monitor;
import Logueo.LogDeEventos;


public class TrenDriver implements Runnable {
	private Monitor monitor;
	private int indiceLog;
	private LogDeEventos log;
	
	public TrenDriver(Monitor monitor, LogDeEventos log, int indiceLog){
		this.monitor=monitor;
		this.indiceLog=indiceLog;
		this.log=log;
	}
	
	
	
	/**
     * Tren entre las estaciones.
     */
	public void run(){
		HashMap<String, String> tablaLogueo=new HashMap<String, String>();
		tablaLogueo.put( "arriboA",  "0");
		tablaLogueo.put( "salgoA",   "1");
		tablaLogueo.put( "arriboB",  "2");
		tablaLogueo.put( "salgoB",   "3");
		tablaLogueo.put( "arriboC",  "4");
		tablaLogueo.put( "salgoC",   "5");
		tablaLogueo.put( "arriboD",  "6");
		tablaLogueo.put( "salgoD",   "7");
		boolean flagImpresion=true;
		
		while(true){
			if(flagImpresion){
				System.out.println("Tren en A");
				
			}
			this.log.addMessage(tablaLogueo.get("arriboA"), this.indiceLog);
			
			//Ver transiciones a disparar
			
			if(flagImpresion){
				System.out.println("Tren saliendo de A");
				
			}
			this.log.addMessage(tablaLogueo.get("salgoA"), this.indiceLog);
			
			//Ver transiciones a disparar
			
			if(flagImpresion){
				System.out.println("Tren en B");
				
			}
			this.log.addMessage(tablaLogueo.get("arriboB"), this.indiceLog);
			
			//Ver transiciones a disparar
			
			if(flagImpresion){
				System.out.println("Tren saliendo de B");
				
			}
			this.log.addMessage(tablaLogueo.get("salgoB"), this.indiceLog);
			
			//Ver transiciones a disparar
			
			if(flagImpresion){
				System.out.println("Tren en C");
				
			}
			this.log.addMessage(tablaLogueo.get("arriboC"), this.indiceLog);
			
			//Ver transiciones a disparar
			
			if(flagImpresion){
				System.out.println("Tren saliendo de C");
				
			}
			this.log.addMessage(tablaLogueo.get("salgoC"), this.indiceLog);
			
			
			//Ver transiciones a disparar
			
			if(flagImpresion){
				System.out.println("Tren en D");
				
			}
			this.log.addMessage(tablaLogueo.get("arriboD"), this.indiceLog);
			
			
			//Ver transiciones a disparar
			
			if(flagImpresion){
				System.out.println("Tren saliendo de D");
				
			}
			this.log.addMessage(tablaLogueo.get("salgoD"), this.indiceLog);
			
			//Ver transiciones a disparar
			
			this.monitor.dispararTransicion(1); //Entrada a la estacion A
		}
	}
	

}
