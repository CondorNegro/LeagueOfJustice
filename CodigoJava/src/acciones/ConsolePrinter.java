
package acciones;


public class ConsolePrinter implements Accion {
	 private String message;
	 public ConsolePrinter(String message) {
	        this.message=message;
	        
	 }
	 
	 public void setMessage(String message){
		 this.message=message;
	 }
	/* (non-Javadoc)
	 * @see acciones.Accion#ejecutarAcccion()
	 */
	@Override
	public void ejecutarAcccion() {
		 System.out.println(message);
	}
}
