/**
 * 
 */
package UnitTests;

import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class testPolitica {
	private int[] listaTest;
	private Monitor.Politica politica;
	private int[] transicionesInmediatas;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		listaTest=new int[5];
		transicionesInmediatas=new int[5];
		for(int i=0; i<5;i++){
			transicionesInmediatas[i]=1;
		}
		politica=new Monitor.Politica(1, this.transicionesInmediatas);
		listaTest[0]=1;
		listaTest[1]=0;
		listaTest[2]=0;
		listaTest[3]=0;
		listaTest[4]=1;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		//listaTest.clear();
	}

	/**
	 * Test method for {@link Monitor.Politica#Politica(int)}.
	 */
	@Test
	public void testPolitica() {
		politica.setModo(1);
		assertEquals(politica.getModo(), 1);
	}
	
	/**
	 * Test method for {@link Monitor.Politica#cualDisparar(java.util.List)}.
	 */
	@Test
	public void testCualDispararCon1ElementoEnLista() {
		//listaTest.clear();
		listaTest=new int[6];
		listaTest[0]=1;
		listaTest[1]=0;
		listaTest[2]=0;
		listaTest[3]=0;
		listaTest[4]=0;
		listaTest[5]=0;
		politica.setModo(0);
		for(int i=0;i<10000;i++){
			try{
				  int indice= politica.cualDisparar(listaTest, true);
				  if(indice!=0){
					  fail("Indice distinto de cero");
				  }
				  
			}
			catch(IndexOutOfBoundsException e){
				fail("Genero error");
			}
		}
		assertEquals(politica.cualDisparar(listaTest, true),0);
	}

	/**
	 * Test method for {@link Monitor.Politica#cualDisparar(java.util.List)}.
	 */
	@Test
	public void testCualDispararModo0() {
		politica.setModo(0);
		assertEquals(listaTest.length,5);
		int indice=0;
		for(int i=0;i<5000000;i++){
			try{
				  indice = politica.cualDisparar(listaTest, true);
				  if(indice!=0 & indice != 4){
					  fail("Indice distinto de cero o cuatro");
				  }
				  
			}
			catch(IndexOutOfBoundsException e){
				fail("Genero error");
			}
		}
		if(indice==0){ 
			assertEquals(indice,0);
		}
		else if(indice==4){
		    assertEquals(indice,4);
		}
		else{
			System.out.println(indice);
		}
	}
	
	/**
	 * Test method for {@link Monitor.Politica#cualDisparar(java.util.List)}.
	 */
	@Test
	public void testCualDispararModo1() { //Primero suben.
		politica.setModo(1);
		assertEquals(listaTest.length,5);
		int indice=0;
		for(int i=0;i<100000;i++){
			try{
				  indice = politica.cualDisparar(listaTest, true);
				  if(indice != 4){
					  fail("Indice distinto de cuatro");
				  }
				  
			}
			catch(IndexOutOfBoundsException e){
				fail("Genero error");
			}
		}
	
		    assertEquals(indice,4);
		
	}
	
	/**
	 * Test method for {@link Monitor.Politica#cualDisparar(java.util.List)}.
	 */
	@Test
	public void testCualDispararModo2() { //Primero bajan
		politica.setModo(2);
		assertEquals(listaTest.length,5);
		int indice=0;
		for(int i=0;i<100000;i++){
			try{
				  indice = politica.cualDisparar(listaTest, true);
				  if(indice != 4){
					  fail("Indice distinto de cuatro");
				  }
				  
			}
			catch(IndexOutOfBoundsException e){
				fail("Genero error");
			}
		}
	
		    assertEquals(indice,4);
	}
	
	
	
	
	/**
	 * Test method for {@link Monitor.Politica#cualDisparar(java.util.List)}.
	 */
	@Test
	public void testCualDispararModo1Aleatorio() { //Primero suben pero ejecuta aleatorio por no tener sensibilizadas transiciones prioritarias.
		politica.setModo(1);
		//listaTest.clear();
		listaTest=new int[5];
		listaTest[0]=1;
		listaTest[1]=0;
		listaTest[2]=0;
		listaTest[3]=0;
		listaTest[4]=0;		
		assertEquals(listaTest.length,5);
		int indice=0;
		for(int i=0;i<100000;i++){
			try{
				  indice = politica.cualDisparar(listaTest, true);
				  if(indice != 0){
					  fail("Indice distinto de cuatro");
				  }
				  
			}
			catch(IndexOutOfBoundsException e){
				fail("Genero error");
			}
		}
	
		    assertEquals(indice,0);
		
	}
	
	/**
	 * Test method for {@link Monitor.Politica#isThereTransicionInmediataSensibilizada(java.util.List)}.
	 */
	@Test
	public void testIsThereTransicionInmediataSensibilizada() {
		
		assertEquals(listaTest.length,5);
		boolean flagTest= politica.isThereTransicionInmediataSensibilizada(listaTest); 
		assertEquals(flagTest,true);
		this.transicionesInmediatas[0]=0;
		this.transicionesInmediatas[4]=0;
		politica=new Monitor.Politica(1, this.transicionesInmediatas);
		flagTest= politica.isThereTransicionInmediataSensibilizada(listaTest); 
		assertEquals(flagTest,false);
		
		
	}

}
