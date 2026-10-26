package spiel;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import Interfaces.Bildschirmanzeige;

/*
 * Klasse:			Konsole
 * Erstellt am: 	18.11.2005 
 * Erstellt von: 	Arne Meier (Bretinittialisierung), 
 * 					Patrick Schwarzer(Ausgabefunktionen)
 * 
 * Menue war vorher eine Einzelklasse die von Patrick Schwarzer und Sebastian Horchler 
 * erstellt wurde welches am 16.11.2005 fertig war
 * 
 * Änderungen: 
 * 26.11.2005, Maria Pidiktaki, Arne Meier: 		Funktionskopplung in der Menuausgabe
 * 26.11.2005, Patrick Schwarzer, Sebastian Horchler : while Bedingungen überarbeitet, die
 * 													   vor Falscheingaben schützen
 * 26.11.2005, Patrick Schwarzer, Maria Pidiktaki : neue Funktionen steinAuswahl(), naechsterZugXAchse() 
 * 													naechsterZugYAchse()
 * 26.11.2005, Patrick Schwarzer, Maria Pidiktaki:  Designverbesserungen, neue Hinweismeldungen bei 
 * 													Falscheingaben im jeweiligen Menue
 * 30.11.2005, Sebastian Horchler, Maria Pidiktaki: HilfeMenu ausgebaut 			    
 * 12.12.2005, Patrick Schwarzer, Arne Meier: Hinzufügung von JavaDocs  
 * 17.12.2005, Patrick Schwarzer, Sebastian Horchler: Hinzufuegung der Methode naechsterZug()
 * 11.01.2206, Patrick Schwarzer, Arne Meier: Hinzufügen der Methode, eingabeIP
 * 18.01.2006, Arne Meier,Maria Pidiktaki: eindeutschung eng. Bezeichner
 * 													            
 */

public class Konsole implements Bildschirmanzeige{
	private String spielerName = null;
	private String spielerName2 = null;

/*-----------------------------------------------------------------------------------------------------------------*/
///////////////////////
///   ZUGAUSWAHL   ///
////////////////////// 	
	/**
	 * Methode, die den Spieler zur Auswahl der ermittelten moeglichen Züge, bezügliches
	 * der gewürfelten Steinnumer, auffordert
	 * 
	 * @param moeglicheZuege = int Array, welches die Zuege enthaelt, die der Stein (wuerfelzahl)
	 * 						   machen kann
	 * @param wuerfelzahl	 = wuerfezahlergebnis entweder Server oder Intern je nach SpielArt
	 * @return zug[]		 = intArray mit dem Zug, den der Spieler schließlich machen will
	 */
	public int naechsterZug(int moeglicheZuege[], int wuerfelzahl,int SpielerSteinPos[]){
		/* Initialisierung */
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int feldgroesse = 24;
		String spieler;			//enthaelt den Namen des Spielers, der dran ist
		int auswahl = 0,		//enthalt die case Nummer im den gewuenschten Zug zu waehlen
			anzahl = 0;			//gibt an wieviele case Faelle es geben muss
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////		
		/* ermittle, welcher Spieler am Zug ist */
		if (moeglicheZuege[0] == 1)
			spieler = spielerName;
		else if(spielerName2 != null)
			spieler = spielerName2;
		else
			spieler = "PC";
		System.out.println("Gewuerfelt wurde eine: " +wuerfelzahl);
		System.out.println("Spieler " +spieler +" zieht!");
		System.out.println("-------------------------------------------------");
		for (int i = 1, j = 1; i < feldgroesse; i+=4){			
			/* misst die Anzahl der vorhandenen Zuege durch den Inhalt des Arrays
			 * ist eine Zeile eines Steines = 0, wird nicht weiter ausgegeben */
			if (moeglicheZuege[i+1] != 0){ //solange keine Zeile den Wert 0 hat
				/* gib  Steinnummer, dazugehoerige Zeile und Spalte aus */
				System.out.println(" " +j +"   Stein "+moeglicheZuege[i] +": zieht nach: " 
									+"Zeile(X):" +moeglicheZuege[i+1] +" Spalte(Y):"+moeglicheZuege[i+2]);
				anzahl = j;
				j++; //casenummer die hochzaehlt
			}//if
		}//for
/////////////////////////////////////////////////////////////////////////////////////////////////////////
		/* Spieler wird aufgefordert, einen moeglichen Zug auszuwaehlen */
		System.out.println("-------------------------------------------------");
		System.out.print("Geben Sie ihren naechsten Zug ein: ");
		do{
			try
			{
				auswahl = Integer.parseInt(in.readLine());	//Eingabe einer casenummer	
			}
			catch (Exception e){
				System.out.println("Eingabefehler!" +e);	//Fehleingabe
			}
			if (auswahl > anzahl || auswahl < 1){			//Hinweis bei Falscheingabe
				System.out.println("Eingabefehler! Nur die Zahlen:  1 bzw. 1 - 3 bzw. 1 - 6 zulaessig!");
				System.out.print("Bitte erneut eingeben: ");
			}
		} while (auswahl > anzahl || auswahl < 1);			//wiederhole bei Falscheingabe		

		return auswahl; // rueckgabe des vom Nutzer ausgewaehlten Zuges 
	}//naechsterZug

/*-----------------------------------------------------------------------------------------------------------------*/	
/////////////////////////////
//// AUSGABE VORBEREITEN ////
/////////////////////////////	
	/**
	 * Initialisiert des Spielbrett mit den Spielsteinen so dass die Konsole sie ausgeben kann
	 * @param steinArray = int Array mit allen Eigenschaften (zugehoerigkeit,nummer,zeile,spalte) der Steine
	 * 					   aller Spieler
	 * @param Spieler1 = String, der den Namen des ersten Spielers darstellt
	 * @param Spieler2 = String, der den Namen des zweiten Spielers darstellt
	 */
	public void brettinitialisierung (int[] steinArray, String Spieler1, String Spieler2,int spieler1Zugehoerigkeit)
	{	
	    int attributeAllerSteine = 48; 	//groesse des Feldes wenn beide SpielerSteine + Eigenschaften enthalten sind
		int maxzeilen 			 = 6,  	//maxzeilen-1  = 5 = groeße des Spielbretts (5x5)
			maxspalten 			 = 6,  	//maxspalten-1 = 5 = groeße des Spielbretts (5x5)			
			steinNummer,			   	//Nummer des Steins (Werte 1 bis 6)
			zugehoerigkeit,				//Zugehoerigkeit der Steine zu welchem Spieler
			zeile,                     	//zeilenPosition des Steins (Werte: 1 bis 5)
			spalte;					   	//spaltenPosition des Steins (Werte: 1 bis 5)
			String spielBrett[][] = new String[maxzeilen][maxspalten];

		
		/*StringArray: erstmal leer anlegen */
		for(int k = 0 ; k < maxspalten; k++){ 	// 6 ist die Zeilen- als auch die Spaltenzahl des Spielbretts
			for(int j = 0;j < maxzeilen; j++){	// siehe eine zeile drüber 
				spielBrett[k][j] ="       ";	// 5 Leerzeichen weil das genau die Breite eines Spielfeldes ist
			}
		}
		
		/* Werte aus dem Array holen (pro Stein) */
	    for(int i = 0; i < attributeAllerSteine; i+=4)
	    {	    	
	    	zugehoerigkeit = steinArray[i];
	    	steinNummer    = steinArray[i+1];
	    	zeile          = steinArray[i+2];
	    	spalte         = steinArray[i+3];
	    	
	    	/* Spielerzugehoerigkeit = Spieler 1 oder 2 bestimmen, beide erhalten unterschiedliche Steinsymbole */
	    	if(zugehoerigkeit == 1){	/* 1 = links oben = O*/
	    		/*Jeweils 2 Leerzeichen davor und dahinter damit das mit einer Ziffer dazwischen wieder die 5 Zeichen pro Spielfeld ergibt*/
	    		spielBrett[zeile][spalte] = ("  O" + steinNummer + "O  ");
	    	}
	    	
	    	else if(zugehoerigkeit == 0){	/* 0 = rechts unten = X*/
	    		   /*Jeweils 2 Leerzeichen davor und dahinter damit das mit einer Ziffer dazwischen wieder die 5 Zeichen pro Spielfeld ergibt*/
	    	       spielBrett[zeile][spalte] = ("  X" + steinNummer + "X  ");
	    	     }//if
	    }//for	  
	    
	    /* mit dem erzeugten StringArray, Spielbrettausgabe aufrufen */
		ausgabeSpielBrett(spielBrett,Spieler1,Spieler2,spieler1Zugehoerigkeit);
	   
	}//brettinitialisierung
/*-----------------------------------------------------------------------------------------------------------------*/
////////////////////////////////////////////
////       AUSGABE  DES  SPIELBRETTS    ////
////////////////////////////////////////////	
	/**
	 * Methode, die das Spielbrett ausgibt
	 * erhaelt die 2 Spielernamen, gibt dann das Spielbrett grafisch aus
	 * @param spielBrett 	= String Array mit Spielsteine
	 * @param nameSpieler1	= String: Name des ersten Spielers
	 * @param nameSpieler2	= String: Name des zweiten Spielers
	 * @param wuerfelzahl	= int Wert, der Wuerfelzahl darstellt
	 */
	public void ausgabeSpielBrett(String spielBrett[][],String nameSpieler1, String nameSpieler2,int spieler1Zugehoerigkeit){	

		System.out.println("\n\n");		
		//Ermittelt welcher Spielername auf welcher Seite steht
		if (spieler1Zugehoerigkeit==1)				
			System.out.print(nameSpieler1 +"\n");
		else			
			System.out.print(nameSpieler2 +"\n");
		
		/*waagerechte Feldtrennungslinie |----|----|*/
		for (int i = 1; i < 6; i++){
			for (int ii = 0; ii < 5; ii++){				
				System.out.print("|" +"-------" );
			}
			/*letzter Abschlussstrich extra, da sonst ---- noch hinzu käme*/
			System.out.print("|");
			if (i==1)
				System.out.print("X");							//Bezeichner der ZeilenAchse
			System.out.print("\n");
///////////////////////////////////////////////////////////////////////////////////////////			
			/*stellt das Feld mit Werten da (Feldinnereien)*/
			for (int ii=0; ii < 3; ii++){							//zählt in die Tiefe \|/
				for (int iii = 1; iii < 6; iii++){					//zählt in die Länge -->
					if (ii == 1)
						System.out.print("|" + spielBrett[i][iii]);		//Ausgabe der Spielsteinposition + Felddesign |    |
					else
						System.out.print("|" +"       ");		//Ausgabe eines leeren Feldes,wenn kein Stein drauf steht + Felddesign
				}
			System.out.print("|");								//Abschlusslinie je Zeile+Bezeichnuner x-Achse
								
			if (ii==1)
				System.out.print(i);							//Bezeichnung der X-Achse 1 2 3 4 5
			System.out.print("\n");
			}
		}//for
///////////////////////////////////////////////////////////////////////////////////////////
		/*letzte Feldabschlusslinie*/
		for (int i = 0; i < 5; i++){						
			System.out.print("|" +"-------");					//letzte Abschlusslinie
		}
		System.out.println("|");								//letztes Abschlusszeichen

		System.out.print("Y   "+"1");							//Bezeichner der SpaltenAchse
		for (int iiiii = 1; iiiii < 5; iiiii++){				//Bezeichnung für y-Achse   1 2 3 4 5
			System.out.print("       " +(iiiii+1));
		}
		/*Spielername 2 - Herausgeforderte*/
		//Ermittelt welcher Spielername auf welcher Seite steht
		if (spieler1Zugehoerigkeit==0)				
			System.out.println("    " + nameSpieler1 +"\n");
		else
			System.out.println("    " + nameSpieler2 +"\n");
	}//ausgabeSpielbrett
/*-------------------------------------------------------------------------------------------------------------*/
///////////////////////////////
////    SONDERFUNKTIONEN   ////
///////////////////////////////
	/**
	 * diese Methode zeigt dem Spieler die Nachrichten von Server, die er sehen soll
	 * erhaelt die Nachrichten und gibt sie nur aus
	 * 
	 * @param ausgabe = String: 
	 */
	public void ausgabeNachricht(String ausgabe){
		System.out.println(ausgabe);
	}
	
/*--------------------------------------------------------------------------------------------------------------*/
	/** 
	 * diese Methode nimmt einen Spielernamen entgegen
	 */
	public void neuerSpielerName() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("\n\nBitte geben Sie einen anderen Spielernamen ein: ");

			try
			{
				spielerName = in.readLine();
			}
			catch (Exception e){
				System.out.println("Eingabefehler!" +e);
			}
	
	}
/*-----------------------------------------------------------------------------------------------------------------*/
	/** 
	 * diese Methode übergibt der Steuerung-Klasse den Spielername
	 */
	public String gibspielerName() {		
		return spielerName;
	}
/*-----------------------------------------------------------------------------------------------------------------*/

	/** 
	 * Eingabe vom Nutzer
	 */
	public String eingabe() {
		String eingabe = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			eingabe = in.readLine();
		}
		catch (Exception e){
			System.out.println("Eingabefehler!" +e);
		}
		return eingabe;
	}
/*-----------------------------------------------------------------------------------------------------------------*/	
////////////////////////
///   M E N U E     ///
///////////////////////	
	
/*-----------------------------------------------------------------------------------------------------------------*/		
	/** 
	 * Fuehrt die Eingabe des Spielernamens durch
	 * Eingebenen werden duerfel alle moeglichen Zeichen
	 * gibt einen int-Wert zurueck, der spaeter ausgewertet wird
	 * 
	 * spielerName = String, in ihm wird der eingegebene Spielername gesichert 
	 */
	public int spielerEingabeMenue(){
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\n/////////////////////////////////////////////////////////////////");
		System.out.println("\nStarte Spiel: EiNsTeIn WuErFeLt NiChT!!");
		System.out.println("=======================================");
		System.out.print("\n\nBitte geben Sie einen gewuenschten Spielernamen ein: ");

			try
			{
				spielerName = in.readLine();
			}
			catch (Exception e){
				System.out.println("Eingabefehler!" +e);
			}
		
		System.out.println("\nHallo: " +spielerName);
		System.out.println("\nWillkommen bei Einstein wuerfelt nicht!");
		return auswahlMenue();
	}
/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Falls ein SinglePlayerSpiel gegen einen 2. Spieler gewaehlt wurde, wird er hier aufgefordert, seinen
	 * Spielernamen einzugeben
	 * 
	 * @return spielerName2 = String, der den Namen des 2 Spielers zwischenspeichert
	 */
	public String spieler2EingabeName(){
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\n/////////////////////////////////////////////////////////////////");
		System.out.println("\nEingabe Spielernamen 2!");
		System.out.println("=========================");
		System.out.print("\nSpieler2, bitte geben Sie einen gewuenschten Spielernamen ein: ");
		
			try
			{
				spielerName2 = in.readLine();
			}
			catch (Exception e){
				System.out.println("Eingabefehler!" +e);
			}

		return spielerName2;
	}
/*-----------------------------------------------------------------------------------------------------------------*/	
	/** 
	 * Menue, in welchem die Spielform (Single- oder Multiplayer), die Hilfe aufgerufen wird und 
	 * das Programm beendet werden kann
	 * Es enthaelt weiterhin eine Schutzfunktion, die nur die Werte 1 - 4 zulaesst
	 * Bei Falscheingabe wird zur Neueingabe aufgefordert
	 * Wertet die Eingabe aus und ruft das entsprechende Untermenue auf
	 * Liefert einen int-Wert zurueck, der spaeter ausgewertet wird
	 * 
	 * eingabe = int Wert, die die entsprechende Auswahl enthaelt
	 */
	public int auswahlMenue() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int eingabe = 0;		
		System.out.println("\n/////////////////////////////////////////////////////////////////");
		System.out.println("\n-AuswahlMenue-");
		System.out.println("==============\n");
		System.out.println("Auswahl des Spieltypes:");
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - \n");
		
		System.out.println("             1            Spiel an einem Computer");
		System.out.println("             2            Spiel ueber das Netzwerk");
		System.out.println("             3            Hilfe");
		System.out.println("             4            Beenden");
		
		System.out.print("\nBitte waehlen Sie eine der gegebenen Zahlen: ");
		
		/* Eingabe mit Schutzfunktion */
		do
		{
			try
			{
				eingabe = Integer.parseInt(in.readLine());		
			}
			catch (Exception e){
				System.out.println("Eingabefehler!" +e);
			}
			if (eingabe > 4 || eingabe < 1){					//Hinweis bei Falscheingabe
				System.out.println("Eingabefehler! Nur die Zahlen  1, 2, 3 und 4 zulaessig!");
				System.out.print("Bitte erneut eingeben: ");
			}
		} while (eingabe > 4 || eingabe < 1);	//wiederhole bei Falscheingabe (wenn nich 1 - 4 eingetippt wurde */
		
		/*Auswertung der Eingabe, d.h. jeweiliges Untermenue aufrufen */
		switch (eingabe){
			case 1: {  /*SinglePlayer Menue aufrufen */
						eingabe = singlePlayerMenue(); break;
					}
			case 2: {  /*MultiPlayer Menue aufrufen */	
						eingabe = multiPlayerMenue(); break;
					}
			case 3: {	/*Hilfe Menue aufrufen */
						eingabe = hilfeMenue(); break;
					}
			case 4: {	/*Beendet das Proramm */
						System.out.println("Danke das Sie unser Programm genutzt haben!"); 
						System.exit(-1); break;						
					}
			default: System.out.println("Eingabefehler!"); break; 		/*dürfte nie eintreten*/
		}
		return eingabe;	//rueckgabe der Eingabewerte der Untermenues, die aufgerufen werden
	}//auswahlMenue
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * stellt das Singleplayer Menue dar, auch hier mussen Eingaben getaetigt werden um die Art
	 * des Singleplayer Spiels zu definieren
	 * Es enthaelt auch eine Schutzfunktion, die nur die Werte 1 oder 2 zulaesst
	 * Bei Falscheingabe wird zur Neueingabe aufgefordert
	 * Wertet die Eingabe aus
	 * Liefert einen int-Wert zurueck, der spaeter ausgewertet wird
	 * 
	 * eingabe = int Wert, die die entsprechende Auswahl enthaelt
	 * 
	 * @return = liefert int - Wert zurueck, der Eingabe enthaelt 
	 */
	private int singlePlayerMenue (){
		
		int eingabe = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		/*Ausgabemenue*/
		System.out.println("\n/////////////////////////////////////////////////////////////////");
		System.out.println("\n-SinglePlayerMenue-");
		System.out.println("===================");
		System.out.println("\n \n");
		
		System.out.println("             1            Spieler vs Spieler");
		System.out.println("             2            Spieler vs Computer");
		System.out.println("             3            AuswahlMenue");
		
		System.out.println("\n \n");
		System.out.print("Bitte Auswahl treffen: ");
		
		/*Eingabe, Bedingung: nur 1,2 erlaubt*/
		do
		{
			try
			{
				eingabe = Integer.parseInt(in.readLine());		//Eingabe
			}
			catch (Exception e){
				System.out.println("Eingabefehler!" +e);
			}
			if (eingabe > 3 || eingabe < 1){					//Hinweis bei Falscheingabe
				System.out.println("Eingabefehler! Nur die Zahlen  1, 2 und 3 zulaessig!");
				System.out.print("Bitte erneut eingeben: ");
			}
		
		} while (eingabe > 3 || eingabe < 1); 	//wiederhole bei Falscheingabe
		
		/* Auswertung der Eingabe */
		switch (eingabe){
			case 1: eingabe = 1;break; 
			case 2: eingabe = 2;break; 				//wird spaeter ausgewertet um entsprechendes Spiel zu starten 
			case 3: eingabe = auswahlMenue();break;	//zurueckspringen zum Auswahlmenue		
		}
		return eingabe;
	}//singlePlayerMenue
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * stellt das MultiPlayer Menue dar, es mussen Eingaben getaetigt werden um die Art
	 * des Multiplayer Spiels zu definieren
	 * Es enthaelt eine Schutzfunktion, die nur die Werte 1 - 3 zulaesst
	 * Bei Falscheingabe wird zur Neueingabe aufgefordert
	 * Wertet die Eingabe aus
	 * Liefert einen int-Wert zurueck, der spaeter ausgewertet wird
	 * 
	 * eingabe = int Wert, die die entsprechende Auswahl enthaelt 
	 * @return = liefert int - Wert zurueck, der Eingabe enthaelt
	 */
	private int multiPlayerMenue (){
		
		int eingabe = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		/*Ausgabemenue*/
		System.out.println("\n/////////////////////////////////////////////////////////////////");
		System.out.println("\n-MultiPlayerMenue-");
		System.out.println("===================");
		System.out.println("\n \n");
		
		System.out.println("             1            Spieler  vs Netzwerk");
		System.out.println("             2            Computer vs Netzwerk");
		System.out.println("             3            AuswahlMenue");
		
		System.out.println("\n \n");
		System.out.print("Bitte Auswahl treffen: ");
		
		/*Eingabe mit Bedingung: 1,2,3 als erlaubt Eingabe */
		do
		{
			try
			{
				eingabe = Integer.parseInt(in.readLine());
			}
			catch (Exception e){
				System.out.println("Eingabefehler!" +e);
			}
			if (eingabe > 3 || eingabe < 1){					//Hinweis bei Falscheingabe
				System.out.println("Eingabefehler! Nur die Zahlen  1, 2 und 3 zulaessig!");
				System.out.print("Bitte erneut eingeben: ");
			}
		} while (eingabe > 3 || eingabe < 1); //Wiederholen bei Falscheingabe
		
		//Auswertung der Eingabe
		switch (eingabe){
		case 1: eingabe=3; break; // Ist der Schlüssel im Hauptmenu für Spieler vs Netzwerk
		case 2: eingabe=4; break; // Ist der Schlüssel im Hauptmenu für Computer vs Netzwerk
		case 3: eingabe = auswahlMenue();break;//zurueckspringen zum Auswahlmenue
		}
		return eingabe;
	}//multiPlayerMenue
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Das Hilfemenue inklusive Untermenues
	 * Es enthaelt eine Schutzfunktion, die nur die Werte 1 - 7 zulaesst
	 * Bei Falscheingabe wird zur Neueingabe aufgefordert
	 * Wertet die Eingabe aus und ruft zur Eingabe das entsprechende Untermenue auf
	 * 
	 * eingabe = int Wert, die die entsprechende Auswahl enthaelt 
	 * @return = liefert int - Wert zurueck, der Eingabe enthaelt
	 */
	private int hilfeMenue(){
		int eingabe = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		/*Hilfemenue*/
		System.out.println("\n/////////////////////////////////////////////////////////////////");
		System.out.println("\n-HilfeMenue-");
		System.out.println("===================");
		System.out.println("\n \n");
		
		System.out.println("             1            Einleitung");
		System.out.println("             2            Wer ist am Zug?");
		System.out.println("             3            Bewegen von Steinen");
		System.out.println("             4            Siegbedingungen");
		System.out.println("             5            Multiplayerspiel");
		System.out.println("             6            Tipps & Tricks");
		System.out.println("             7            Zurück zum Hauptmenu");
		
		System.out.println("\n \n");
		System.out.print("Bitte Auswahl treffen: ");

		/* Eingabe der entsprechenden Auswahl 1 - 7 */
		do
		{
			try
			{
				eingabe = Integer.parseInt(in.readLine());
			}
			catch (Exception e){
				System.out.println("Eingabefehler!" +e);
			}
			if (eingabe > 7 || eingabe < 1 ) {
				System.out.println("Eingabefehler! Nur die Zahlen 1 bis 7 zulaessig!");
				System.out.print("Bitte erneut eingeben: ");
			}
		} while (eingabe > 7 || eingabe < 1); //Wiederhole bei Falscheingabe
		
		/* Auswertung der Eingabe */
		switch (eingabe){
			case 1: konsolenHilfeEinleitung(); break; 		// Einleitung Untermenue aufrufen
			case 2: konsolenHilfeZug(); break; 				// Wer ist am Zug? Menue aufrufen
			case 3: konsolenHilfeSteinBewegen(); break; 	// Bewegen von Steinen Hilfe aufrufen
			case 4: konsolenHilfeSiegebedingung(); break; 	// Siegbedingungen Untermenue aufrufen
			case 5: konsolenHilfeMultiplayerSpiel(); break;	// Multiplayerspiel Hilfe
			case 6: konsolenHilfeTippsUndTricks(); break; 	// Tipps & Tricks anzeigen
			case 7: eingabe = auswahlMenue();break; 		// Zurück zum Hauptmenu springen		
		}
		return eingabe;
	}//hilfeMenue
/*-----------------------------------------------------------------------------------------------------------------*/
///////////////////////////////////////////
///  	UNTERMENUES VOM HILFEMENUE		///
///////////////////////////////////////////
	/**
	 * Hilfeuntermenue 1 Einleitung
	 * enthaelt eine kleine Einleitung zum Spiel und eine kleine Spielanleitung
	 * @return: ruft das HauptHilfeMenue auf
	 */
	public int konsolenHilfeEinleitung(){
		System.out.println("\n\n\n\n„EinStein würfelt nicht“ ist ein Spiel für 2 Personen. Es wurde vom deutschen " 
				+ "\nProf. Ingo Althöfer entwickelt."
				+ "\nDie sechs Spielsteine der beiden Spieler befinden sich in einem Raster von "
				+ "\nfünf mal fünf Spielfeldern in den Ecken, die sich gegenüberstehen. Jeder Stein "
				+ "\nkann sich immer nur 1 Feld bewegen, aber es ist möglich, sowohl eigene als "
				+ "\nauch gegnerische Spielsteine zu schlagen, in dem man auf sie drauf zieht. Die "
				+ "\nbeiden Spieler ziehen abwechselnd, ein Würfelergebnis bestimmt, welcher Stein "
				+ "\ngezogen werden darf. \n"
				+ "\nDie Besonderheit ist, dass es auch möglich ist, selbst zu entscheiden, welchen "
				+ "\nSpielstein man bewegen darf, wenn der Stein für das Würfelergebnis nicht mehr "
				+ "\nim Spiel ist. Wenn nur noch ein Stein bei einem Spieler im Spiel ist, wird das "
				+ "\nWürfelergebnis ignoriert, denn dieser Stein darf immer ziehen, denn „Ein Stein "
				+ "\nwürfelt nicht“.\n"
				+ "\nEin Beispiel:"
				+ "\nAuf dem Spielfeld existieren nur noch die Steine 1, 3 und sechs eines "
				+ "\nSpielers. Der Würfel gibt als Ergebnis eine 2 aus. Nun hat dieser Spieler die "
				+ "\nWahl, ob er den Stein Nr. 1 oder den Stein Nr 3 bewegen möchte.\n"
				+ "\nDieses Spiel wurde von der Entwicklergruppe BlackByte des 3. Semesters der"
				+ "\nFachhochschule Fulda im Rahmen des Programmieren-Praktikums entwickelt"
				+ "\nund getestet.");
		return hilfeMenue();	//zurueck zum Hilfemenue
	}//konsolenHilfeEinleitung
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Methode liest die IP Adresse ein
	 * @return ip = String mit der eingegebenen IP Adresse
	 */
	public String eingabeIP(){
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String ip = null;
		
		System.out.println("Achten Sie hier bitte auf die Richtigeingabe!!");
		System.out.println("Bitte geben Sie Ihre IP ein: ");
		
		try
		{
			ip = in.readLine(); //einlesen der IP
		}
		catch (Exception e){
			System.out.println("Eingabefehler!" +e);
		}
		return ip;//gibt die eingegebene IP zurueck
	}//eingabeIP
	
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * kurze Hilfe darueber, welcher Spieler wann ziehen darf
	 * @return: ruft das HauptHilfeMenue auf
	 */
	private int konsolenHilfeZug(){
		System.out.println("\n\n\nWer ist am Zug?\n" 
				+ "\nEs wird abwechselnd gespielt. Jeder Spieler erhält ein Würfelergebnis, welches " 
				+ "\ndie möglichen Steine bestimmt, die Sie ziehen können. Wenn Sie am Zug sind, " 
				+ "\nwerden Sie aufgefordert ihren Zug zu machen. " );
		        
		return hilfeMenue();	//zurueck zum Hilfemenue
	}//konsolenHilfeZug
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Hilfe zum Ablauf des Spiels, d.h. wie das Programm ablaeuft
	 * @return: ruft das HauptHilfeMenue auf
	 */
	private int konsolenHilfeSteinBewegen(){
		System.out.println("\n\n\nBewegen von Steinen\n" 
				+ "\nWährend des Spiels wird das Programm Ihnen alle Spielmöglichkeiten zeigen, die " 
				+ "\nihnen jeweils zur Verfügung stehen. Sie werden dann z.B. aufgefordert, sich " 
				+ "\nzwischen den Steinen 1 und 3 zu entscheiden, die Sie bewegen wollen. Nachdem " 
				+ "\nsie diese Entscheidung durch die Eingabe der Spielsteinnummer getroffen haben, " 
				+ "\nwird das Programm ihnen auch alle möglichen Züge vorgeben, aus der Sie " 
				+ "\nwiederum auswählen dürfen. Dabei wird das Programm von Ihnen lediglich die " 
				+ "\nneue Zielkoordinate des Steins verlangen. \n" 
				+ "\nWenn sie während der Steinbewegungsphase eine Null statt der Spielsteinnummer " 
				+ "\neingeben, wird das Spiel sofort beendet.");
		return hilfeMenue(); 	//zurueck zum Hilfemenue
	}//konsolenHilfeSteinBewegen
/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Hilfe, unter welchen Bedinungen man das Spiel gewinnt
	 * @return: ruft das HauptHilfeMenue auf
	 */
	private int konsolenHilfeSiegebedingung(){
		System.out.println("\n\n\nSiegbedingungen\n" 
				+ "\nDas Ziel des Spiel ist es, einen der eigenen Steine in die gegnerische Ecke zu " 
				+ "\nbewegen oder einfach alle gegnerischen Steine zu schlagen.");
		return hilfeMenue(); 	//zurueck zum Hilfemenue
	}//konsolenHilfeSiegebedingung
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * kurze Hilfe zum Multiplayer ueber das Netzwerk
	 * @return: ruft das HauptHilfeMenue auf
	 */
	private int konsolenHilfeMultiplayerSpiel(){
		System.out.println("\n\n\nMultiplayerhilfe\n"
				+ "\nDie Auswahl des Multiplayerspiels ohne Netzwerk erlaubt Ihnen gegen einem " 
				+ "\nFreund an einem Rechner zu spielen. Die Option Multiplayerspiel mit Netzwerk " 
				+ "\ngestattet Ihnen über das Netzwerk gegen einen anderen Spieler an seinem " 
				+ "\nComputer oder dessen Künstliche Intelligenz zu spielen.");
		return hilfeMenue(); 	//zurueck zum Hilfemenue
	}//konsolenHilfeMultiplayerSpiel
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Menue, was kurze Tipps zum schnelleren Sieg enthaelt
	 * @return: ruft das HauptHilfeMenue auf
	 */
	private int konsolenHilfeTippsUndTricks(){
		System.out.println("\n\n\nTipps & Tricks\n" 
				+ "\n-  Sie haben es einfacher, wenn Sie Ihre Steine früh dezimieren, um zwischen " 
				+ "\n   weniger Steinen wählen zu müssen und damit sich schneller bewegen zu können" 
				+ "\n-  Wenn Sie Ihre Steine am Rand des Spielfelds bewegen, haben Sie zwar einen " 
				+ "\n   längeren Weg zur gegnerischen Ecke, allerdings reduziert sich dadurch auch " 
				+ "\n   das Risiko von anderen Steinen geschlagen zu werden.");
		return hilfeMenue();	//zurueck zum Hilfemenue
	}//konsolenHilfeTippsUndTricks
	
}//KONSOLE
