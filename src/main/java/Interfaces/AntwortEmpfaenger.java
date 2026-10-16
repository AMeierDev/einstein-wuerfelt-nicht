/*
 * Interface: ResponseReceicer		Author: HHPaul
 * Date: 09.10.2005	                Version: 1.0
 * 
 * Interface ResponseReceiver defines two methods for server callback
 * functionality of the NetworkInterface. Must be implemented by 
 * suitable class to provide callback capability.
 * 
 * Aenderungen:
 * 10.12.2005 Maria Pidiktaki, Arne Meier: Methode wird unveraendert eingebaut
 * 18.01.2006, Arne Meier,Maria Pidiktaki: eindeutschung eng. Bezeichner  
 */

package Interfaces;

/**
 * Specifies interface for callback functions used by the NetworkInterface class.
 * Must be implemented by instances using the NetworkInterface to communicate with
 * GameServer.
 * 
 * @author H.-H. Paul
 *
 */
public interface AntwortEmpfaenger {
	/**
	 * This method is called by instance of NetworkInterface for each message received from GameServer.
	 * @param message The message obtained
	 */
	public void nachrichtEmpangen(String message);
	/**
	 * Method called by instance of NetworkInterface to signal nonexistence of 
	 * connection to GameServer. This may be either when trying to establish connection
	 * or after explicite during connection shutdown.
	 * @param message Errormessage identifying cause of loss of connection.
	 */
	public void verbindungVerloren(String message);
}
