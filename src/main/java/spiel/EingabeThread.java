package spiel;
import Interfaces.Bildschirmanzeige;
import client.*;

/*
 * Klasse:		 QuitAbfrage 
 * Erstellt am:  10.12.2005 
 * Erstellt von: Maria Pidiktaki, Sebastian Horchler	
 * 
 * Änderungen: 			    
 * 12.12.2005, Patrick Schwarzer, Arne Meier: Hinzufügung von JavaDocs  
 * 18.01.2006, Arne Meier,Maria Pidiktaki: eindeutschung eng. Bezeichner          
 */

/**
 * @author  Bigmama
 */
public class EingabeThread extends Thread {
	/**
	 * @uml.property  name="bildschirm"
	 * @uml.associationEnd  
	 */
	private Bildschirmanzeige bildschirm = null;
	/**
	 * @uml.property  name="netz"
	 * @uml.associationEnd  
	 */
	private NetzwerkSchnittstelle netz = null;
	private volatile boolean aktivAussen = true;
	private volatile boolean aktivInnen = true;
	/* Konstruktor */
	public EingabeThread(Bildschirmanzeige bildschirm,NetzwerkSchnittstelle net){
		this.bildschirm = bildschirm;
		this.netz = net;
		start();
		
	}
	
	public void stopAbfrage()
	{
		aktivInnen = false;
	}
	public void startAbfrage()
	{
		aktivInnen = true;
	}
	
	
	/**
	 * Methode die einen Thread startet, der im Hintergrund laeuft und die  Nachrichten zwischen
	 * Client und Server austauscht
	 * Falls der Thread mit Quit beendet wird, wird das Programm beendet
	 * 
	 * s   		 = String, der die Eingabe des Spielers aus dem Menue heraus enthaelt, bzw. Befehle des
	 *       	   Programms
	 * net 		 = Instanz von Networkinterface,
	 * bidschirm = Instanz von Bildschirmanzeige
	 */
	public void run(){
		/* Schleide die dauerhaft ausgeführt wird - Thread*/
		
		
		
		while(aktivAussen){
		while(aktivInnen)
		{
			String s = bildschirm.eingabe();
			  if(!"quit".equals(s))
			  { 
		        netz.zumServerSchicken(s);  //sende Daten zum Server
				
			  }
			  else
			  {
			    netz.zumServerSchicken("logout");				 //beim Server abmelden		
		        System.err.println("\nProgramm wird beendet");
				System.exit(-1);						 // Programm beenden
			  }
			}//While innen Ende
			try { Thread.sleep(10); } catch (InterruptedException e) { return; }
		}// While aussen Ende
		
		    
	}//Run Ende
	
}//Klasse Ende