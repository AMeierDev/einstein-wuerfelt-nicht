package client;

/* 
 * Klasse:		 NetworkInterface
 * Erstellt am:  07.10.2005 
 * Erstellt von: HHPaul
 * 
 * Änderungen:
 * 09.10.2005  HHPaul: remove message storage buffer and ingrate callback functionality.
 * 10.12.2005, Maria Pidiktaki, Sebastian Horchler: Anpassung an bereits entworfenes Programm
 * 12.12.2005, Patrick Schwarzer, Arne Meier: Hinzufügung von JavaDocs
 * 18.01.2006, Arne Meier,Maria Pidiktaki: eindeutschung eng. Bezeichner 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import Interfaces.AntwortEmpfaenger;


/**
 * Ecapsulates client network interface to server.  Contains functionality to establish and close network communication. All information is transmitted as String transparantly to server.
 * @author  H.-H. Paul
 */
public class NetzwerkSchnittstelle {
	Socket client = null;
	PrintWriter insNetz;
	BufferedReader vomNetz;
	String eingehenderText = null;
	/**
	 * @uml.property  name="empfaenger"
	 * @uml.associationEnd  
	 */
	AntwortEmpfaenger empfaenger;
	boolean tot = false;
	PrintWriter logdatei = null;

	/**
	 * Create instance of NetworkInterface.
	 * @param hostIP  known IP of host running GameServer. May be "IP-Number",
	 *                "localhost" or "DNS-name"
	 * @param respRcv the object receiving server response
	 */
	public NetzwerkSchnittstelle(String hostIP, AntwortEmpfaenger respRcv, PrintWriter logdatei) {
		this.logdatei = logdatei;
		this.empfaenger = respRcv;
		verbinde(hostIP);
		if(client != null) new NetzEmpfaenger();
	}

	/**
	 * Create instance of NetworkInterface. "localhost" is assumed as the
	 * IP of the GameServer. 
	 * @param respRcv the object receiving server response
	 */
	public NetzwerkSchnittstelle(AntwortEmpfaenger respRcv) {
	   // this("localhost", respRcv);
	} 
	/*
	 * Inner class listens to input from network server asynchronically
	 * Information received ist transmitted to known receiving object
	 *
	 */
	private class NetzEmpfaenger implements Runnable {
		String inText = null;
		NetzEmpfaenger() {
			new Thread(this).start(); // Thread starten
		}

	/**
	 * wurde modifiziert, damit es mit dem Programm spiel zusammenarbeitet und die
	 * gewünschte Funktionen des Datenaustauschs zwischen Server und Client erfüllt
	 */
	public void run() {
			// Wait for incoming commands
			while (!tot) {
				try {
					inText = vomNetz.readLine();
					if (inText == null || inText.indexOf("disconnect") >= 0) {
						throw new IOException();
					}
					if (inText != null)
						empfaenger.nachrichtEmpangen(inText);
				} catch (IOException e) {
					tot = true;
				}
			}
			herunterFahren();
		} // run
	} // class

	/*
	 *  Acitivites required to shut down connection
	 */
	private void herunterFahren() {
		insNetz.close();
		try {
			vomNetz.close();
			client.close();
		} catch (IOException e) {		}
		empfaenger.verbindungVerloren("Verbindung zum Server unterbrochen");
	}

	/*
	 * establish connection to host
	 */
	private void verbinde(String hostIP) {

		/* create Socket  = connect to server */

		try {
			client = new Socket(hostIP, 1078);
			/* open I/O-Streams */
			try {
				vomNetz = new BufferedReader(
						new InputStreamReader(client.getInputStream()));
				insNetz = new PrintWriter(client.getOutputStream(), true);
			} catch (IOException e) {
				empfaenger.verbindungVerloren("Server meldet sich nicht");
			}
		} catch (UnknownHostException e) {
			empfaenger.verbindungVerloren("Unbekannter Host: " + hostIP);
		} catch (IOException e) {
			empfaenger.verbindungVerloren("Host \"" + hostIP
					+ "\": Kein Server an Port 1078 aktiv");
		}
	}


	/**
	 * Transmits String message to server. 
	 * @param message The message to be transmitted.
	 */
	public void zumServerSchicken(String message) {
		logdatei.println("|Programm an Server|: " + message);
		logdatei.flush();
		insNetz.println(message);
	}
}