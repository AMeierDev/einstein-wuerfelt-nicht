package spiel;
import Interfaces.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import client.*;

/*
 * Klasse: 		 Steuerung 
 * Erstellt am:  10.12.2005 
 * Erstellt von: Sebastian Horchler, Maria Pidiktaki
 * 
 * Änderungen: 
 * 10.12.2005, Sebastian Horchler, Maria Pidiktaki: Vorherige Funktionen der Spiel Main in extra 
 * 												    Klasse verlagert			    
 * 12.12.2005, Patrick Schwarzer, Arne Meier: Hinzufügung von JavaDocs    
 * 17.12.2005, Patrick Schwarzer, Sebastian Horchler: Refactoring, da neuer Ablauf der SteinInitialisierung durch Felder
 * 													  neue Methoden: ausgabeForm(),     
 * 04.01.2006, Arne Meier, Patrick Schwarzer: logdatei eingebaut.
 * 07.01.2006, Arne Meier, Patrick Schwarzer: "setzte Zug" hinzugefügt, für KI. messageReceived modularisiert. 
 * 10.01.2006, Arne Meier, Patrick Schwarzer: logdatei um Spielfeld-Intarray erweitert. 
 * 11.01.2006, Patrick Schwarzer, Arne Meier: Kommentare aktualisiert  
 * 18.01.2006, Arne Meier,Maria Pidiktaki: eindeutschung eng. Bezeichner  
 */
/**
 * @author  Bigmama
 */
public class Steuerung implements AntwortEmpfaenger{

	/* Klassen-Instanzen */
	/**
	 * @uml.property  name="logik"
	 * @uml.associationEnd  
	 */
	Spiellogik logik = new Spiellogik();
	/**
	 * @uml.property  name="bildschirm"
	 * @uml.associationEnd  
	 */
	Bildschirmanzeige bildschirm = null;
	/**
	 * @uml.property  name="quit"
	 * @uml.associationEnd  
	 */
	EingabeThread quit = null;
	/**
	 * @uml.property  name="netzSch"
	 * @uml.associationEnd  
	 */
	NetzwerkSchnittstelle netzSch= null;
	/**
	 * @uml.property  name="spieler1"
	 * @uml.associationEnd  
	 */
	SpielerInterface spieler1 = null;
	/**
	 * @uml.property  name="spieler2"
	 * @uml.associationEnd  
	 */
	SpielerInterface spieler2 = null;
	PrintWriter logdatei = null;
	
	
	/* Variablen */
	int zugehoerigkeit = 0; 					//legt fest, wer den ersten Zug machen darf
	int intWuerfel = 0;							//legt fest, welcher Stein gezogen werden darf
	int ausgabeForm = 0;						//legt Ausgabeform fest: GUI oder Konsole
	int spielArt = 0;							//legt Spielform fest: SinglePlayer, Multiplayer-Spiel
	int wuerfelZahlZaehler = 1;                 //um ersten Wuerfel zu ignorieren
	boolean istKI = false;						//KI spielen lassen: ja oder nein
	String spieler2Name = "vorlaeufig";			//Name des 2. Spielers bevor er festgelegt wird
	Date heute = null;							//Datum fuer die Log Datei
	int zugAuswahl;								//gewaehlter Zug aus den moeglichen Zuegen (1,2,3,4,5 oder 6)
	
	/*Arrays*/
	int zug[] = new int [4];	//stellt einen Zug dar, den der Spieler gemacht hat (zugehoerigkeit,nummer,zeile,spalte)=4Werte
	/**
	 * @uml.property  name="initSteinPosSpieler1"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	Stein initSteinPosSpieler1[] = new Stein[6];	//Eigenschaften der Steine des ersten Spielers
	/**
	 * @uml.property  name="initSteinPosSpieler2"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	Stein initSteinPosSpieler2[] = new Stein[6];	//Eigenschaften der Steine des zweiten Spielers
	int SpielerSteinPos[] = new int [48];			//Eigenschaften der Steine aller Spieler (=zusammengebracht in ein Array)int [] moeglicheSpielzuege = new int [24]; 		
	int[] moeglicheSpielzuege = new int[24];        //Feld, welches die moeglichen Spielzuege mit dem Stein x (und y) enthaelt

/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Ausgabeform wählen:
	 * Auswahl zwischen Konsole(1) und GUI(2)
	 * default liefert eine Fehlermeldung
	 */
	public void ausgabeForm(int auswahl){
		
		switch (auswahl){
			case 1: 	bildschirm = new Konsole(); break;
			case 2: 	bildschirm = new SpielGui(); break;
			default: 	bildschirm.ausgabeNachricht("Eingabefehler"); break;
		}//switch - case
	}
	//Menue starten, welches Abfragen an User stellt
/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Methode, die zur ausgewaehlten Spielart in der Konsole, den entsprechenden Spieltyp startet.
	 * 1 = Singleplayer Spiel mit 2 menschlichen Spielern an einem PC
	 * 2 = Singleplayer Spiel mit einem menschlichen Spieler und der KI als Gegner
	 * 3 = Multiplayer Spiel mit 2 menschlichen Spielern ueber das Netzwerk (Gameserver benoetigt)
	 * 4 = Multiplayer Spiel, auf unserer Seite hingegen spielt die KI, Gegner kann Mensch oder KI sein
	 */
	public void spielmenue(){
		spielArt = bildschirm.spielerEingabeMenue();	/*ruft die Menues in der Konsole auf, in welchem Name,
														 *Spieltyp etc. ausgewählt werden muessen */

		/* Prüfung der ausgewählten Werte und Ereignisausloesung */
		switch (spielArt){
		case 1: /*Spieler vs Spieler an einem PC*/
			    spieler1 = new Spieler(zugehoerigkeit+1, bildschirm.gibspielerName(), bildschirm);
				spieler2 = new Spieler(zugehoerigkeit, bildschirm.spieler2EingabeName(), bildschirm); 
				einzelSpieler();	//Spiel nach gewuenschten anforderungen starten
				break;
		case 2: /*Spieler vs Computer an einem PC*/
				//muss spaeter geaendert werden, so dass die KI ziehen kann, spieler 2 = KI, under construction
			    spieler1 = new KI(zugehoerigkeit+1, "KI",bildschirm);
			    spieler2 = new Spieler(zugehoerigkeit,bildschirm.gibspielerName() ,bildschirm); 
				einzelSpieler();	//Spiel nach gewuenschten anforderungen starten
				break;	
		case 3: /*Spieler vs Netzwerk*/
				istKI = false; //KI spielt nicht
			    netzwerkBeginn();
				break;					
				
		case 4: /*Computer vs Netzwerk*/
				istKI = true; //KI spielt unsere Spielzuege
				netzwerkBeginn();
				break;	
		}//switch - case
	}//spielMenue
/*-----------------------------------------------------------------------------------------------------------------*/		
	/**
	 * Methode regelt den Spielablauf in einem SinglePlayer Spiel
	 */
	public void einzelSpieler()
	{	
		/* Spieler1 StartSteinPosition abholen*/
		initSteinPosSpieler1 = spieler1.initSteinPos(spieler1.gibZugehoerigkeit());	
		/* Spieler2 StartSteinPosition abholen*/
		initSteinPosSpieler2 = spieler2.initSteinPos(spieler2.gibZugehoerigkeit());		
		/* Spieler SteinEigenschaften in ein gemeinsames Array schreiben */
		SpielerSteinPos = spieler1.gibSteinEigenschaftenFeld(initSteinPosSpieler1, initSteinPosSpieler2);		
		/* erste Ausgabe des Spielfeldes mit allen Steinen und den Spielernamen */
		bildschirm.brettinitialisierung(SpielerSteinPos,spieler1.gibName(),spieler2.gibName(),spieler1.gibZugehoerigkeit());
///////////////////////////////////////////////////////////////////////////////////////////////
		do{
			/* Spieler 1 spielt*/
			/*simuliere wuerfel*/		
			intWuerfel = (int) Math.round(Math.random()*6+0.5);		
///////////////////////////////////////////////////////////////////////////////////////////////			
			/* fuehrt Spielablauf fuer Spieler1 durch*/
			spielen(spieler1.gibZugehoerigkeit());
			
			/*pruefe auf Sieg von Spieler 1, wenn noch nicht Sieg - Spieler 2 am Zug*/
			if ((logik.siegBedingungen(SpielerSteinPos)==false)){					
				/*simuliere wuerfel*/		
				intWuerfel = (int) Math.round(Math.random()*6+0.5);		
///////////////////////////////////////////////////////////////////////////////////////////////			
				/* fuehrt Spielablauf fuer Spieler2 durch*/
				spielen(spieler2.gibZugehoerigkeit());
			}
		//wiederhole bis ein Spieler gewonnen hat
		}while(logik.siegBedingungen(SpielerSteinPos)==false);
		
		//Ausgabe des Gewinners
		if(logik.gibSieger()==1)
		bildschirm.ausgabeNachricht(spieler1.gibName()+" hat das Spiel gewonnen.");
		if (logik.gibSieger()==0)
		bildschirm.ausgabeNachricht(spieler2.gibName()+" hat das Spiel gewonnen.");
		spielmenue(); //ruft wieder das Anfangmenue auf
	}//spielBeginn
/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Gibt die Nachrichten, die vom Server kommen aus, bzw. wertet sie aus
	 * enthaelt verschiedene Vergleiche, die erkennt, um was fuer Nachrichten es sich handelt
	 * einige Nachrichten loesen wiederum Aktionen aus, 
	 * siehe "sind angemeldet": Server wird aufgefordert die vorhandenen Spieler anzuzeigen
	 *       "Sonst keiner da!": Hinweis fuer den Nutzer
	 * @throws InterruptedException 
	 */

	public void nachrichtEmpangen(String nachricht) {
		
		logdatei.println(nachricht); 	//schreibt Nachricht vom Server in die LogDatei
		logdatei.flush();			//Leert den Puffer
				
//BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
		if(nachricht.startsWith("Server B")){	
			//Verbindung zum Server hergestellt
			if(nachricht.startsWith("Server B> Verbindung")){
				bildschirm.ausgabeNachricht(nachricht);
			}
			//wenn man sich auf den Server angemeldet hat, wird direkt die List-Funktion aufgerufen			
			if(nachricht.endsWith("sind angemeldet")){ 
				bildschirm.ausgabeNachricht(nachricht);
				netzSch.zumServerSchicken("liste");
			}
			//Hilfe
			if(nachricht.startsWith("Server B> Verfuegbare")){
				bildschirm.ausgabeNachricht(nachricht);
			}
			//Antwort auf:  WerBinIch
			if(nachricht.startsWith("Server B> Sie sind")){ 
				bildschirm.ausgabeNachricht(nachricht);
			}
			//Aufruf "liste"-Funktion
			if(nachricht.startsWith("Server B> Folgende")){ 
				if(nachricht.endsWith("Niemand frei!")) 
					bildschirm.ausgabeNachricht(nachricht);
				else if (nachricht.endsWith("Sonst keiner da!"))
				{
					bildschirm.ausgabeNachricht(nachricht+ "\nVerfuegbare Befehle:" +
														"\nlogout = Server verlassen oder " +
														"\nliste  = Liste der auf dem Server befindlichen Spieler erneut aufrufen.");						
				}
				 //es sind Gegner auf dem Server um herausgefordert zu werden, liste - Funktion wird automatisch ausgefuehrt
				else bildschirm.ausgabeNachricht(nachricht);
			}
			//Verbindung getrennt
			if(nachricht.startsWith("Server B> disconnect"))
			{
				logdatei.close();	//logdatei beenden
				spielmenue();		//AnfangsSpielmenue aufrufen
			}
			// Gegner hat Herausforderung akzeptiert 
			if(nachricht.endsWith("akzeptiert"))
			{
				int anfang = 10; 	//der GegnerName beginnt bei Position 11 des Strings
				int laenge;			//Laenge des Strings
				int ende; 			// Ende des Gegnernamens
			    int stringBisEnde = 11; //Zeichen die noch nach dem Gegnernamen kommen
				
			    
			    laenge = nachricht.length(); 	//NachrichtenLaenge messen
				ende = laenge - stringBisEnde;
				spieler2Name = nachricht.substring(anfang,ende); //Gegnernamen aus String ausschneiden
				
				//Initialisierung: KI spielt, bzw. spielt nicht, dann spielt Mensch
				if(istKI == false){
					spieler1 = new Spieler(0, bildschirm.gibspielerName(), bildschirm);//wir haben heraus gefordert und sind somit "unten rechts" -> 0
				}
				if(istKI == true){
					spieler1 = new KI(0, bildschirm.gibspielerName(),bildschirm);//wir haben heraus gefordert und sind somit "unten rechts" -> 0
				}
				spieler2 = new Spieler(1, spieler2Name, bildschirm);//Gegener wurde herausgefordert und ist somit "oben links" -> 1
				
				//Arrays fürs Spiel vorbereiten
				/* Spieler1 StartSteinPosition abholen*/
				initSteinPosSpieler1 = spieler1.initSteinPos(spieler1.gibZugehoerigkeit());
			
				quit.stopAbfrage(); //Thread stoppen
			}
			//Gegner hat uns  herausgefordert 
			if(nachricht.endsWith("Server B> Spiel startet")){
				//Initialisierung: KI spielt, bzw. spielt nicht, dann spielt Mensch
				if(istKI == false){
					spieler1 = new Spieler(1, bildschirm.gibspielerName(), bildschirm);//wir wurden herausgefordert und sind somit "oben links" -> 1
				}
				if(istKI == true){
					spieler1 = new KI(1, bildschirm.gibspielerName(),bildschirm);//wir wurden gefordert und sind somit "oben links" -> 1
				}
				spieler2 = new Spieler(0, spieler2Name,bildschirm);//Gegener hat uns herausgefordert und ist somit "unten rechts" -> 0
				
				//Arrays fürs Spiel vorbereiten
				/* Spieler 1 StartSteinPosition abholen*/
				initSteinPosSpieler1 = spieler1.initSteinPos(spieler1.gibZugehoerigkeit());
								
				quit.stopAbfrage(); // Thread stoppen
				wuerfelZahlZaehler = 1;
				bildschirm.ausgabeNachricht(nachricht);
			}						
		}// Ende B-nachrichts		
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE		
		if(nachricht.startsWith("Server E")){
			
			if(nachricht.startsWith("Server E0"))
			{
				bildschirm.ausgabeNachricht(nachricht);	
			}
			
			if(nachricht.startsWith("Server E1")){
				if(nachricht.startsWith("Server E101"))//Server E101> Bitte einloggen! (login name
				{
					bildschirm.ausgabeNachricht(nachricht);	
				}
			
				if(nachricht.startsWith("Server E102"))//Server E102> Login nicht moeglich. Name existiert bereits: "name"
				{
					bildschirm.ausgabeNachricht(nachricht);
					quit.stopAbfrage();                  //Thread von User zum Server pausieren 
					bildschirm.neuerSpielerName();		 //User unsere Eingabe geben damit sie auch bei uns rauskommt			
					netzSch.zumServerSchicken("login " + bildschirm.gibspielerName());//automatischer Relogin
					quit.startAbfrage();                 // Thread zwischen User und Server wieder reaktivieren
				
				}
			
				if(nachricht.startsWith("Server E103")){//Server E103> Bitte login mit Namen eingeben
					netzSch.zumServerSchicken("login " + bildschirm.gibspielerName());//Spieler versuchen auf den Server zu bringen
				}	
			
				if(nachricht.startsWith("Server E104")){//Server E104> Sie sind bereits eingeloggt
					bildschirm.ausgabeNachricht(nachricht);
				}
			}//Ende E1xx nachrichts
			
			if(nachricht.startsWith("Server E2")){
				
				if(nachricht.startsWith("Server E201")){//Server E201> "Gegner" moechte nicht gegen Sie spielen
					bildschirm.ausgabeNachricht(nachricht + "\n Bitte waehlen sie einen anderen Spieler.");
				}
				
				if(nachricht.startsWith("Server E202")){//Server E202> spiel: Partnernamen angeben!
					bildschirm.ausgabeNachricht(nachricht);
				}
				
				if(nachricht.startsWith("Server E203")){//Server E203> Spieler nicht gefunden: "partnerName"
					bildschirm.ausgabeNachricht("Der von Ihnen gewaehlte Spieler existiert nicht.");
				}
				
				if(nachricht.startsWith("Server E204")){//Server E204> Spieler "partnerName" nicht verfuegbar!
					bildschirm.ausgabeNachricht("Der von Ihnen gewaehlte Spieler spielt bereits mit jemand anderen.");
				}
				
				if(nachricht.startsWith("Server E205")){//Server E205> Sie koennen nicht gegen sich selbst spielen
					bildschirm.ausgabeNachricht("Entschuldigung aber dieses Spiel kann man leider nur zuzweit \n spielen(Schizophren gilt auch nicht).");
				}
				
				if(nachricht.startsWith("Server E206")){//Server E206> Sie werden bereits angefragt
					bildschirm.ausgabeNachricht("Ihnen wurde eine Frage gestellt da wir sie für hoeflich halten \n erwarten wir darauf bitte eine Antwort.");
				}
				
				if(nachricht.startsWith("Server E207")){//Server E207> Sie haben bereits angefragt
					bildschirm.ausgabeNachricht("Bitte warten Sie erst die Antwort ab.");
				}
				
				if(nachricht.startsWith("Server E208")){//Server E208> "partnerName" zieht seine Spieleinladung zurueck!
					bildschirm.ausgabeNachricht(nachricht + "\n Da er den Server verlassen hat.");
				}
				
			}// Ende E2xx nachrichts
			
			if(nachricht.startsWith("Server E3")){
				
				if(nachricht.startsWith("Server E301")){//Server E301> Antwortzeit ueberschritten
					bildschirm.ausgabeNachricht(nachricht + "\n Nicht Kaffee trinken antworten.");
				}
				// Die E302 wird nicht ausgegeben da der Spieler zu dem Zeitpunkt bereits automatisch vom Server gekickt wurde
				if(nachricht.startsWith("Server E303")){//Server E303> Spielanfrage: Antwortzeit ueberschritten
					bildschirm.ausgabeNachricht(nachricht);
					spielmenue();
				}
			}// Ende E3xx nachrichts
			
		}//ENDE der E-nachrichts
//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM		
		if(nachricht.startsWith("Server M")){
			if(nachricht.endsWith("setzt zuerst")){//Server M> Spiel startet. "partnerName" setzt zuerst
				wuerfelZahlZaehler = 1;          // wird auf 1 gesetzt damit man nun die erste WuerfelZahl ignorieren kann
				bildschirm.ausgabeNachricht(nachricht);
			}
			
			if(nachricht.endsWith("gewonnen")){//Server M> "partnerName" hat das Spiel gewonnen
				bildschirm.ausgabeNachricht(nachricht);
			}
			
			if(nachricht.endsWith("gewonnen!")){//Server M> Sie haben gewonnen! 
				bildschirm.ausgabeNachricht(nachricht);
			}
			
			if(nachricht.endsWith("Zeitueberschreitung")){//Server M> "partnerName": Zeitueberschreitung 
				bildschirm.ausgabeNachricht("Zeitueberschreitung!!!\n"+spieler2.gibName()+" hat das Spiel gewonnen.");
			}
			
			if(nachricht.endsWith("weitergegeben")){//Server M> Ablehnung an "partnerName" weitergegeben
				bildschirm.ausgabeNachricht(nachricht);
				//spieler2Name = null;               // Spieler2Name wird wieder auf Null gesetzt weil der Name aus zeitgründen schon vorher erfasst wurde.
			}
			
			if(nachricht.endsWith("Spielende")){//Server M> Spielende
				spielEndeNetzwerk(nachricht);
			}
			
		}//Ende M-nachrichts
//QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ		
		if(nachricht.startsWith("Server Q")){//Server Q> "partnerName" moechte gegen Sie spielen, o.k.? 
			bildschirm.ausgabeNachricht(nachricht);
			
			int anfang = 10; //der GegnerName beginnt bei position 11 des Strings
			int laenge;//laenge des Strings
			int ende; // Ende des Gegnernamens
		    int stringBisEnde = 43; // Zeichen die noch nach dem Gegnernamen kommen
			
		    
		    laenge = nachricht.length();
			ende = laenge - stringBisEnde;
			spieler2Name = nachricht.substring(anfang,ende);
			
			//System.out.println("spieler2 = " + spieler2Name);
		}//Ende Q-nachrichts		
//ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ		
		if(nachricht.startsWith("Server Z")){
			if(nachricht.endsWith("Zug")){//Server Z> Sie sind am Zug 
			bildschirm.ausgabeNachricht(nachricht);
			}
			
			if(nachricht.startsWith("Server Z> W")){//Server Z> Würfel: #würfelzahl
				if(wuerfelZahlZaehler == -1){
					//hier passiert nichts weil das Spiel zu Ende ist
				}
			    else if(wuerfelZahlZaehler == 1){       // wenn das die 1. würfelzahl ist werden dem 
					wuerfelZahlZaehler -= 1;      //Gegener die startpositionen geschickt
			        
					/* Spieler 1 StartSteinPosition abholen*/
					initSteinPosSpieler1 = spieler1.initSteinPos(spieler1.gibZugehoerigkeit());
		            netzSch.zumServerSchicken(spieler1.gibInitialString(initSteinPosSpieler1));
				}
				else{        //wenn nicht wird die Zahl heraus geschnitten und weitergeleitet
					String stringWuerfel;
					int anfang = 18; //der WürfelZahl beginnt bei position 19 des Strings
					int ende = 19; // Ende des Strings
				   
					stringWuerfel = nachricht.substring(anfang,ende);
					bildschirm.ausgabeNachricht("stringWuerfel: " + stringWuerfel);
					intWuerfel = Integer.parseInt(stringWuerfel);
			
					for (int i=0; i< SpielerSteinPos.length; i++){ //Test des SpielerSteinPos
						logdatei.print(SpielerSteinPos[i]);
					    logdatei.flush();
					}
					logdatei.println();
				    logdatei.flush();
					
					spielen(spieler1.gibZugehoerigkeit());
					netzSch.zumServerSchicken(""+zug[1]+zug[2]+zug[3]);
					siegbedingungPrüfenNetzwerk();
					
					
					
					
				}
			}
		}//Ende Z-nachrichts
//spieler2Namespieler2Namespieler2Namespieler2Namespieler2Namespieler2Namespieler2Namespieler2Namespieler2Namespieler2Namespieler2Namespieler2Name
		if(nachricht.startsWith(spieler2Name)){ 
			                                  
			//bildschirm.ausgabeNachricht(nachricht);
			//netIfc.zumServerSchicken("jo Mann");
			
			if(nachricht.endsWith("Spielende")){//"partnerName" Z> Spielende
				spielEndeNetzwerk(nachricht);
			}
            
			else{                            ////"partnerName" (Würfel: #würfelzahlGegner)Z> Botschaft
				int anfang, //soll die länge des Gegnernamens werden
				    mitte = 16, //KONSTANT Mitte ist die Anzahl der Zeichen zwischen Gegner und Botschaft
				    mitteBisWuerfel = 11, //KONSTANT MitteBisWuerfel ist die Anzahl der Zeichen vom Gegner bis zur Position des Wuerfels
				    anfangBotschaft,
				    endeBotschaft,
				    positionWuerfel;
		        String botschaft;
		           	
				anfang = spieler2Name.length();
				anfangBotschaft = (anfang + mitte);
				endeBotschaft = nachricht.length();
				botschaft = nachricht.substring(anfangBotschaft,endeBotschaft);
				//bildschirm.ausgabeNachricht("Botschaft cuted:[" + botschaft + "]");
				
				positionWuerfel = anfang + mitteBisWuerfel;		
				intWuerfel = Integer.parseInt(nachricht.substring((positionWuerfel - 1),positionWuerfel));
				bildschirm.ausgabeNachricht("Der Gegner hatte eine: " + intWuerfel);
				
				if (botschaft.endsWith("Client E01")){
					wirSchummelnReaktion();
				}
					
					
					
				if(botschaft.length() == 3){
					//bildschirm.ausgabeNachricht("3er Botschaft bearbeitet: ");
					bildschirm.ausgabeNachricht("Er zieht nach: " + botschaft);
					int []gegnerZug=logik.gibIntFeld(botschaft, spieler2.gibZugehoerigkeit());
					
					/*wenn der Zug gültig ist, dann wird der Zug ausgeführt*/
					if(logik.pruefeGegnerZug(intWuerfel, gegnerZug,spieler2.gibZugehoerigkeit(), SpielerSteinPos )){
					
						SpielerSteinPos = logik.bewegeStein(SpielerSteinPos, gegnerZug);
						 bildschirm.brettinitialisierung(SpielerSteinPos,spieler1.gibName(),spieler2.gibName(),spieler1.gibZugehoerigkeit());	
					}
					else{
						gegnerSchummelReaktion();
					}
						
					siegbedingungPrüfenNetzwerk();
				}
				
				if(botschaft.length() == 23){
					bildschirm.ausgabeNachricht("Botschaft bearbeitet : 23;");
					bildschirm.ausgabeNachricht(botschaft);
					int[] spieler2IntArray;
					spieler2IntArray = logik.gibIntFeld(botschaft, spieler2.gibZugehoerigkeit());
					
					if(! logik.pruefeGegnerInitialisierung(spieler2IntArray)){
						gegnerSchummelReaktion();
					}
							
					else {
					initSteinPosSpieler2 = spieler2.spieler2InitSteinPos(spieler2IntArray);
					SpielerSteinPos = spieler1.gibSteinEigenschaftenFeld(initSteinPosSpieler1, initSteinPosSpieler2);
				
					
					bildschirm.brettinitialisierung(SpielerSteinPos,spieler1.gibName(),spieler2.gibName(),spieler1.gibZugehoerigkeit());
					}
				}
				
			}//Ende else: nachrichts vom Gegner
			
		}//Ende Gegner-nachrichts
	}//Methode nachrichtReceived ENDE
	


	/* 
	 * falls die Verbindung getrennt wird, wird das Programm mit einer Meldung beendet
	 */
	public void verbindungVerloren(String nachricht) {
		System.err.println(nachricht       + "\nProgramm wird beendet");
		System.exit(-1);
	}
    
	public void gegnerSchummelReaktion(){
		wuerfelZahlZaehler = -1;
		netzSch.zumServerSchicken("Client E01");
		bildschirm.ausgabeNachricht("Weil der Gegner geschummelt hat, \n" + spieler1.gibName() + " hat das Spiel gewonnen.");

	}

	public void wirSchummelnReaktion(){
		bildschirm.ausgabeNachricht("Der Gegner hat einen Schummelfehler entdeckt!"+"\n"+spieler2.gibName()+" hat das Spiel gewonnen");
		wuerfelZahlZaehler = -1;
		netzSch.zumServerSchicken("quit");
	}
	
	public void siegbedingungPrüfenNetzwerk(){
		if (logik.siegBedingungen(SpielerSteinPos)==true){
			if(logik.gibSieger()==spieler1.gibZugehoerigkeit()){
				bildschirm.ausgabeNachricht(spieler1.gibName()+" hat das Spiel gewonnen.");
			//hier folgt eine Nachricht an den gegner, das wir gewonnen haben
			}
			if(logik.gibSieger()==spieler2.gibZugehoerigkeit()){
				bildschirm.ausgabeNachricht(spieler2.gibName()+" hat das Spiel gewonnen.");
				netzSch.zumServerSchicken("quit");
			}
			wuerfelZahlZaehler = -1;//-1 bedeuted dass der Wuerfel nun ignoriert wird
			/*bildschirm.ausgabeNachricht("Sie koennen jetzt mit einem der folgenden Spieler nochmal spielen");
			netIfc.zumServerSchicken("liste");
			spieler1 = null;                  //alle spieler sowie der spieler2name werden wieder  
			spieler2 = null;                  //auf null gesetzt damit man sie neu initialisieren kann
			//spieler2Name = null;
			
			quit.startAbfrage();              /* der während des Spiels gestoppte Thread wird wieder reaktiviert
		                                     damit der User wieder direkt mit dem Server kommunizieren kann*/
			//System.out.println("Spielende erfolgreich.");
		}
	}
	
	public void spielEndeNetzwerk(String nachricht){
		bildschirm.ausgabeNachricht(nachricht);
		bildschirm.ausgabeNachricht("Sie koennen jetzt mit einem der folgenden Spieler nochmal spielen");
		netzSch.zumServerSchicken("liste");
		spieler1 = null;
		spieler2 = null;
		//spieler2Name = null;
		quit.startAbfrage();
		bildschirm.ausgabeNachricht("Spielende erfolgreich.");
	}
	
	public void spielen(int spielerZugehoerigkeit){
		/* ermittle die moeglichen Zuege des Steins mit der Wuerfelzahl */
		if (spielerZugehoerigkeit == spieler1.gibZugehoerigkeit()){
			moeglicheSpielzuege =  logik.gibmoeglicheSpielzuege(spieler1.gibZugehoerigkeit(), intWuerfel, SpielerSteinPos);
			zugAuswahl = spieler1.setzeZug(moeglicheSpielzuege,SpielerSteinPos, intWuerfel);
		}
		else if(spielerZugehoerigkeit == spieler2.gibZugehoerigkeit()){
			moeglicheSpielzuege =  logik.gibmoeglicheSpielzuege(spieler2.gibZugehoerigkeit(), intWuerfel, SpielerSteinPos);
			zugAuswahl = spieler2.setzeZug(moeglicheSpielzuege,SpielerSteinPos, intWuerfel);
		}
	
		
		zug = logik.benutzerAuswahlInZug(moeglicheSpielzuege,zugAuswahl);
            
		
		/* Steinpositionen aller Steine nach der Bewegung */
		SpielerSteinPos = logik.bewegeStein(SpielerSteinPos, zug);
		
		/* Ausgabe des veraenderten SteinArrays */
		bildschirm.brettinitialisierung(SpielerSteinPos,spieler1.gibName(),spieler2.gibName(),spieler1.gibZugehoerigkeit());
	}
	
	/**
	 * Methode steuert den Ablauf eines MultiplayerSpiels
	 */
	public void netzwerkBeginn(){
		String hostIP;
		hostIP = bildschirm.eingabeIP();	/* IP festlegung */
		
		heute = new Date();
		try {
			logdatei = new PrintWriter( new FileWriter("Logdatei " + heute.getTime() + ".txt"));
		} catch (Exception e){
			bildschirm.ausgabeNachricht("FileWriter Fehler!" +e);
		}
		
		/* Starts Client */
		netzSch = new NetzwerkSchnittstelle (hostIP,this,logdatei);	/* sendet sich an andere Klasse, die Verbindung zum Server 
															 	 * herstellen soll, Uebergabe der IP logdatei*/
		quit = new EingabeThread(bildschirm,netzSch); // Thread starten
		
		//HiLFE//
		bildschirm.ausgabeNachricht("HILFE:" +
				                   "\n Auf dem Server vor dem Spiel: " +
				                   "\n logout(von Server disconnect, wieder ins Menue), quit(Spiel komplett verlassen), " +
				                   "\n werBinlch(falls se vergessen haben wie sie heissen); spiel Gegner(damit fangen Sie an zu spielen) " +
				                   "\n Während des Spiels: " +
				                   "\n NUR quit(komplettes Spiel verlassen)");

		
		netzSch.zumServerSchicken("login " + bildschirm.gibspielerName());	/* Einloggen mit eingegebenem Spielernamen */
		
		

		/* Hilfe aufrufen - Testzweck */	
	}
}//Steuerung ende
