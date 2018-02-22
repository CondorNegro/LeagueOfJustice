
package test;

//Hilo que despierta a otro en una cola.
public class HiloResume implements Runnable {
	private monitor.Cola cola;
	private boolean flag;
	
	public HiloResume(monitor.Cola cola){
		this.cola=cola;
		flag=false;
	}
	
	public boolean getFlag(){
		return flag;
	}
	
	@Override
	public void run(){
		cola.resume();
		//System.out.println("Sal� del notify");
		flag=true;
		
		
	}

}
