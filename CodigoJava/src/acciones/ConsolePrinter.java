
package acciones;

import java.io.PrintStream;

public class ConsolePrinter implements Accion {
	 private String message;
	 private PrintStream log_acciones;
	 public ConsolePrinter(String message, PrintStream log_accion) {
	        this.message=message;
	        this.log_acciones=log_accion;
	 }
	 
	 public void setMessage(String message){
		 this.message=message;
	 }
	/* (non-Javadoc)
	 * @see acciones.Accion#ejecutarAcccion()
	 */
	@Override
	public void ejecutarAccion() {
		 System.out.println(message);
		 this.log_acciones.println(message);
	}
}
