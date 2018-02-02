/**
 * 
 */
package UnitTests;

public class HiloResume implements Runnable {
	private Monitor.Cola cola;
	private boolean flag;
	
	public HiloResume(Monitor.Cola cola){
		this.cola=cola;
		flag=false;
	}
	
	public boolean getFlag(){
		return flag;
	}
	
	@Override
	public void run(){
		cola.resume();
		System.out.println("Sal� del notify");
		flag=true;
		
		
	}

}
