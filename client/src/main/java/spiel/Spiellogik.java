package spiel;
import java.util.*;
/*
 * Klasse:			Spiellogik
 * Erstellt am: 	17.12.2005 
 * Erstellt von: 	Patrick Schwarzer
 * 					Sebastian Horchler
 * 
 * Regelt Spielablaufsfunktionen
 * 
 * Änderungen: 
 * 17.12.2005, Sebastian Horchler, Patrick Schwarzer: Hinzufuegung der Methode bewegeStein,
 * 													  gibmoeglicheSpielzuege, 
 * 20.12.2005, Pidiktaki Maria, Patrik Schwarzer : Methoden pruefeStein,setzemoeglicheZuege 
 * 													hinzugefügt und gibmoeglicheSpielzuege angepasst 
 * 21.12.2005, Pidiktaki Maria, Arne Meier: Methoden pruefeGegnerInitialisierung, pruefeGegnerZug hinzugefügt 	
 * 07.01.2006, Arne Meier, Patrick Schwarzer:     neue Methode: userAuswahlInZug, Funktion ehemals in der Konsole	
 * 											                    in Methode naechsterZug, 
 * 08.01.2006, Pidiktaki Maria, Patrick Schwarzer:neue Methode: setzteSieger, gibSieger
 * 18.01.2006, Arne Meier,Maria Pidiktaki: eindeutschung eng. Bezeichner											            
 */
public class Spiellogik {	

	protected int siegerzugehoerigkeit;
	
	
	/**
	 * @param GegnerSteinPos
	 * @param spielerZugehoerigkeit
	 * @return
	 */
	public int[] gibIntFeld(String GegnerSteinPos, int spielerZugehoerigkeit){
		Spiellogik Konvertiere = new Spiellogik();
		int stringLaenge = 0;
		
		GegnerSteinPos = GegnerSteinPos.trim();	//stellt sicher das keine Leerzeichen am Anfang und Ende sind
		stringLaenge = GegnerSteinPos.length(); //Laenge des String messen
		
		/* einzelne Zuege des Gegenspielers */
		if (stringLaenge == 3){
			return Konvertiere.konvertiereZugStringZuInt(GegnerSteinPos, spielerZugehoerigkeit, stringLaenge);			
		}
		/* Startposition der Steine */
		else if(stringLaenge == 23){
			return Konvertiere.konvertiereStringZuInt(GegnerSteinPos, spielerZugehoerigkeit, stringLaenge);
		}
		/* Problem: String zu lang oder zu kurz - (if(stringLaenge > 23 || stringLaenge < 3)*/
		else { 
			int[] GegnerSteinPosIntWerte = {0,0};
			
			return GegnerSteinPosIntWerte;
		}
	}//gibIntArray
/*----------------------------------------------------------------------------------------------------------------------*/
	/**
	 * Methode, die den String mit den Spielsteinpositionen erhaelt und dieses in int Werte wandelt und in ein Array 
	 * schreibt
	 * 
	 * stringLaenge				 = int Wert, der die Laenge des empfangenen String repraesentiert
	 * feldgroesse				 = int Wert, der dir Groesse der einzelnen Felder angibt
	 * GegnerSteinPosBloecke 	 = Instanz von StringTokenizer, welches dn Ausgangsstring zugewiesen bekommt 
	 * GegnerSteinPosBloecke1	 = String Array, welches  Ausgangs String in zerhackter Form enthaelt (3'er Bloecke)
	 * GegnerSteinPosStringArray = String Array, welches jedes einzelne Element des String in einem Feldelement enthaelt
	 * GegnerSteinPosIntWerte	 = Int Array, welches das konvertierte GegnerSteinPosStringArray in Int Werte enthaelt 
	 * 
	 * @param GegnerSteinPos = String vom Server, der die Positionen der Steine des 
	 * 						   Gegners enthaelt
	 * @return GegnerSteinPosIntWerte[] = int Feld der gegnerischen Steinpositionen
	 */
	
	/**
	 * Methode misst die Laenge des Strings vom Server und entscheidet dann nach dieser,
	 * welche Methode den String in Int-Werte konvertiert
	 * 
	 * stringLaenge = int Wert, der die Laenge des Strings darstellt
	 * Konvertiere	= Instanz von Sicherung
	 * 
	 * @param GegnerSteinPos 		= String mit den Steinpositionen vom Server
	 * @param spielerZugehoerigkeit = int Wert, der Spieler repraesentiert
	 * @return stringLaenge 		= int Wert, der die Laenge des String repraesentiert
	 */
	public int[] konvertiereStringZuInt(String GegnerSteinPos, int spielerZugehoerigkeit, int stringLaenge){
		
		StringTokenizer GegnerSteinPosBloecke = new StringTokenizer(GegnerSteinPos);
		int feldgroesse = stringLaenge+1; 	//Laenge des Strings mit der zugehoerigkeit
		String[] GegnerSteinPosBloecke1 = new String[6];	//in 3'er Blöcke gehackter String
		String[] GegnerSteinPosStringArray = new String[feldgroesse];	//zerhackte 3'er Bloecke in einzelne Bloecke
		int[] GegnerSteinStartPosIntWerte = new int[feldgroesse];
		
		/* zerhackt den String mit Leerzeilen in 3'er Bloecke ohne Leerzeilen */
		for (int i = 0; GegnerSteinPosBloecke.hasMoreTokens(); i++) {
			GegnerSteinPosBloecke1[i] = GegnerSteinPosBloecke.nextToken();
		}
		
		/* Zerhackt die 3'er Bloecke in einzelne Elemente */
		int i = 0, j = 0;
		while (i < (stringLaenge-5)){
			GegnerSteinPosStringArray[i] = GegnerSteinPosBloecke1[j].substring((i%3), (i%3)+1);
			i++;
			if (i%3==0)
				j++;
		}
		/*  zugrunde liegende Erkenntnis */
		/*	GegnerSteinPosBloecke2[0]=GegnerSteinPosBloecke1[0].substring(0, 0+1);
			GegnerSteinPosBloecke2[1]=GegnerSteinPosBloecke1[0].substring(1, 1+1);
			GegnerSteinPosBloecke2[0]=GegnerSteinPosBloecke1[0].substring(2, 2+1);			
			GegnerSteinPosBloecke2[3]=GegnerSteinPosBloecke1[1].substring(0, 0+1);
			GegnerSteinPosBloecke2[4]=GegnerSteinPosBloecke1[1].substring(1, 1+1);
			GegnerSteinPosBloecke2[5]=GegnerSteinPosBloecke1[1].substring(2, 2+1);
			GegnerSteinPosBloecke2[6]=GegnerSteinPosBloecke1[2].substring(0, 0+1);
			GegnerSteinPosBloecke2[7]=GegnerSteinPosBloecke1[2].substring(1, 1+1);
			GegnerSteinPosBloecke2[8]=GegnerSteinPosBloecke1[2].substring(2, 2+1);
			GegnerSteinPosBloecke2[9]=GegnerSteinPosBloecke1[3].substring(0, 0+1);
			GegnerSteinPosBloecke2[10]=GegnerSteinPosBloecke1[3].substring(1, 1+1);
			GegnerSteinPosBloecke2[11]=GegnerSteinPosBloecke1[3].substring(2, 2+1);	
		*/
		/* Wandelt das String-Array in ein Int-Array */
		//int m = 0;
		for (int k = 0, m = 0 ; k < feldgroesse; k++){
			if (k%4==0)
				GegnerSteinStartPosIntWerte[k] = spielerZugehoerigkeit;
			else{
					GegnerSteinStartPosIntWerte[k]= Integer.parseInt(GegnerSteinPosStringArray[m]);
					m++; //2. laufvariable
				}
		}
		/* gibt das Feld zurueck, welches die Steinpositionen als Int-Werte des Gegners enthaelt */
		return GegnerSteinStartPosIntWerte;
	}//konvertiereStringToInt
	
/*----------------------------------------------------------------------------------------------------------------------*/	
	/**
	 * @param GegnerSteinPos
	 * @param spielerZugehoerigkeit
	 * @param stringLaenge
	 * @return
	 */
	public int[] konvertiereZugStringZuInt(String GegnerSteinPos, int spielerZugehoerigkeit, int stringLaenge){
		
		int feldgroesse = stringLaenge;
		String[] GegnerSteinPosStringArray = new String[feldgroesse];	//zerhackte 3'er Bloecke in einzelne Bloecke
		int[] GegnerSteinPosIntWerte = new int[feldgroesse+1];

		/* Zerhackt den 3'er Block in einzelne Elemente und schreibt Positionsweise in neues Array*/
		for (int i = 0; i < feldgroesse; i++){
			GegnerSteinPosStringArray[i] = GegnerSteinPos.substring(i, (i+1));
		}
		/* Wandelt das String-Array in ein Int-Array */
		for (int i = 0; i < (feldgroesse+1); i++){
			if (i==0)
				GegnerSteinPosIntWerte[i] = spielerZugehoerigkeit;
			else
				GegnerSteinPosIntWerte[i]= Integer.parseInt(GegnerSteinPosStringArray[(i-1)]);
		}
		/* Testausgabe*/
		//for (int i = 0; i<(feldgroesse+1); i++)
			//System.out.print(GegnerSteinPosIntWerte[i]);
		return GegnerSteinPosIntWerte;
	}
/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Methode die die Steine verschiebt/bewegt
	 * 
	 * @param steinArray = int Array mit den Eigenschaften aller Steine
	 * @param Zug	     = int Array mit den Eigenschaften des neuen Zuges
	 * @return steinArray = int Array, mit der veraenderten Steinposition
	 */
	public int[] bewegeStein(int steinArray[], int zug[]){
		
		int feldgroesse = 48; //alle Eigenschaften der Steine 2 Spieler, 4 Eigenschaften, 6 Steine,
		int steinPos = 0;	  /*Hilfsvariable um die Position zu speichern, die ausfindig gemacht 
							   *werden muss, an der sich der entsprechend gesuchte Stein befindet */
		
		/* ermittle die Position an der die Steinnumer aus Zug[] steht */
		for (int i = 0; i < feldgroesse; i+=4){
			if((steinArray[i] == zug[0])&&(steinArray[i+1] == zug[1])) //zugehoerigkeit && nummer müssen ausfindig gemacht werden
				steinPos = i+1; //position merken
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//IST BEREITS EIN STEIN AN DER POSITION//
		/* nachsehen ob der neue Zug einen Stein ueberschreibt */
		//System.out.println();
		//for (int i = 0; i <feldgroesse; i++)
			//System.out.print(steinArray[i]);
		steinArray = loescheStein(steinArray, zug); 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//STEIN BEWEGEN//
		/* überschreiben der Alten Zeile und Spalte mit der vom neuen Zug */
			steinArray[steinPos+1]	= zug[2]; //Spalte aendern
			//System.out.println(steinPos+1);
			steinArray[steinPos+2]  = zug[3]; //Zeile aendern
			//System.out.println(steinPos+2);
		/* verändertes Array zurückgeben */
		return steinArray;
	}//bewegeStein
/*-----------------------------------------------------------------------------------------------------------------*/		
	/**
	 * Methode, die prueft ob ein gewuerfelter Stein noch existiert
	 * tut er es nicht, wird dies gemeldet
	 * 
	 * @param steinPos 	 = int Wert, Position des Steins mit der gewuerfelten Steinnummer des entsprechenden Spielers
	 * @param steinArray = int Array, mit den Eigenschaften aller Steine
	 * @return pruefung  = boolean Wert, der angibt ob ein Stein noch existiert oder nicht (false = existiert nicht mehr)
	 */
	public int[] pruefeStein(int steinPos, int steinArray[]){
		int wuerfelzahl = steinArray[steinPos];
		int mglSteinVorher = wuerfelzahl;
		int mglSteinNachher = wuerfelzahl;
		int steinVorher=-1;
		int steinNachher=-1;
	
		int zugehoerigkeit = steinArray[steinPos-1];
		int mglSteine[] =new int [2];			//enthaelt die Steine, die vor und oder nach dem gewuerfelten Stein existieren
		int feldgroesse = 48; 					//alle Eigenschaften der Steine 2 Spieler, 4 Eigenschaften, 6 Steine
		int steinPosTemp=steinPos; // Prüfung für die aktuelle Stein
		int steinPosTemp1=steinPos;// Prüfung für die nachkomende Steine
		int steinPosTemp2=steinPos;// Prüfung für die vorherige Steine
		
		if (steinArray[steinPosTemp+1] != 0 && steinArray[steinPosTemp+2] != 0){
			steinVorher=0;
			steinNachher=0;
		}
		else{
				do{
						if (steinArray[steinPosTemp1+1] == 0 && steinArray[steinPosTemp1+2] == 0){
							if(mglSteinNachher==6)
								steinNachher = 0; // es gibt keinen höheren Stein der bewegt werden darf
							else{							
									mglSteinNachher = mglSteinNachher + 1;	
									
									/* existiert ein Stein nachher? */	
									for (int i = 0; i < feldgroesse; i+=4){
										if((steinArray[i] == zugehoerigkeit)&&(steinArray[i+1] == mglSteinNachher)) //suche zugehoerigkeit und steinnummer
											steinPosTemp1 = i+1;	//merke SteinNummer an dem beides zu finden ist
									}								
								}
							}
						else{steinNachher = steinPosTemp1;}//es gibt einen Stein der bewegt werden darf,und hier wird die Position davon gemerkt
						
				}while(steinNachher == -1);
					
				
				do {
					if (steinArray[steinPosTemp2+1] == 0 && steinArray[steinPosTemp2+2] == 0){
						if(mglSteinVorher==1)
							steinVorher = 0; // es gibt keinen kleineren Stein der bewegt werden darf
						else{
						
							mglSteinVorher = mglSteinVorher - 1;	
								/* existiert ein Stein vorher? */	
								for (int i = 0; i < feldgroesse; i+=4){
									if((steinArray[i] == zugehoerigkeit)&&(steinArray[i+1] == mglSteinVorher)) //suche zugehoerigkeit und steinnummer
										steinPosTemp2 = i+1;	//merke SteinNummer an dem beides zu finden ist
								}						
						}
					}
						else		steinVorher = steinPosTemp2;
					//es gibt einen Stein der bewegt werden darf,und hier wird die Position davon gemerkt	
					
						
				} while(steinVorher == -1);
		
		}//Ende aller erste If Bedingung
		
		mglSteine[0]=steinVorher;  //Position im Array, an dem ein vorgehender Stein existiert
		mglSteine[1]=steinNachher; //Position im Array, an dem ein nachfolgender Stein existiert
		
		//System.out.println("Position1: " +mglSteine[0] +" von Stein: " +steinArray[mglSteine[0]]);
		//System.out.println("Position2: " +mglSteine[1] +" von Stein: " +steinArray[mglSteine[1]]);
		
		return mglSteine;
	}//pruefeStein
/*-----------------------------------------------------------------------------------------------------------------*/		
	/**
	 * Methode die zur gewuefelten Zahl die moeglichen Zuege eines Steins bestimmt
	 * 
	 * @param zugehoerigkeit	= int Wert, der die Zugehoerigkeit der Steine zum Spieler darstellt
	 * @param wuerfelzahl		= int Wert mit der gewuerfelten Zahl
	 * @param steinArray		= int Array, mit den Steineigenschaften beider Spieler
	 * @return moeglicheZuege 	= int Array mit den moeglichen Zuegen des entsprechenden Steins
	 */
	public int[] gibmoeglicheSpielzuege(int zugehoerigkeit, int wuerfelzahl, int steinArray[]){
		int feldgroesse = 48;	//wie weit gesucht werden muss
		int steinPos = 0;		//Positionsspeicher	
		int zeile=0;			//Speicher für Wert an der Stelle 
		int spalte=0;			//Speicher für Wert an der Stelle
		int [] moeglicheZuege = new int [24];	//speichert am Ende alle machbaren Zuege	
		int mglSteine[] = new int [2];			//enthaelt die Steine, die vor und oder nach dem gewuerfelten Stein existieren
		
		
		/* suche den Stein vom Spieler im Array und merke die seine Position*/
		for (int i = 0; i < feldgroesse; i+=4){
			if((steinArray[i] == zugehoerigkeit)&&(steinArray[i+1] == wuerfelzahl)) //suche zugehoerigkeit und steinnummer
				steinPos = i+1;	//merke SteinNummer an dem beides zu finden ist
		}
		
		/*pruefe ob es den gefundenen Stein an der Stelle noch gibt, wenn nicht gibt es die daneben existierenden Steine (2 int Werte) zurueck */
		mglSteine = pruefeStein(steinPos, steinArray);
		
		//Fall 1 gewürfelter Stein existiert
		if (mglSteine[0]==0 && mglSteine[1]==0){
			zeile = steinArray[steinPos+1]; 
			spalte = steinArray[steinPos+2];
				moeglicheZuege = setzemoeglicheZuege(zugehoerigkeit, steinArray, steinPos, zeile, spalte,0,0,0);	
		}//ENDE FALL 1
		
        //Fall 2 es existiert nur ein vorheriger Stein der bewegt werden darf
		if (mglSteine[0]!=0 && mglSteine[1]==0){ 
			zeile = steinArray[mglSteine[0]+1];
			spalte = steinArray[mglSteine[0]+2];
			/* neues Feld an jeden Stellen schon einmal mit zugehoerigkeit und wuerfelzahl initialisieren */
			moeglicheZuege = setzemoeglicheZuege(zugehoerigkeit, steinArray, mglSteine[0], zeile, spalte,0,0,0);
		}//ENDE FALL 2
		
		
       //Fall 3 es existiert nur ein nachfolgender Stein der bewegt werden darf
		if (mglSteine[0]==0 && mglSteine[1]!=0){
			zeile = steinArray[mglSteine[1]+1];
			spalte = steinArray[mglSteine[1]+2];
			/* neues Feld an jeden Stellen schon einmal mit zugehoerigkeit und wuerfelzahl initialisieren */
			moeglicheZuege = setzemoeglicheZuege(zugehoerigkeit, steinArray, mglSteine[1], zeile, spalte,0,0,0);
		}//ENDE FALL 3
		
		
		//Fall 4 2 Steine dürfen bewegt werden
		if (mglSteine[0]!=0 && mglSteine[1]!=0){
			zeile = steinArray[mglSteine[0]+1];
			spalte = steinArray[mglSteine[0]+2];
			
			int zeile1 = steinArray[mglSteine[1]+1];
			int spalte1 = steinArray[mglSteine[1]+2];
			/* neues Feld an jeden Stellen schon einmal mit zugehoerigkeit und wuerfelzahl initialisieren */
			moeglicheZuege = setzemoeglicheZuege(zugehoerigkeit, steinArray, mglSteine[0], zeile, spalte,zeile1,spalte1,mglSteine[1]);
		}//ENDE FALL 4
				
		//rueckgabe aller moeglichen ermittelten Zuege
		return moeglicheZuege;
	}//gibmoeglicheSpielzuege	
	
/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Methode ermittelt alle Positionen der Steine, die bewegt werden koennen
	 * und speichert diese in einem Array ab 
	 * @param zugehoerigkeit	= int Wert, der die Spielerzugehoerigkeit des Steins darstellt
	 * @param steinArray		= int Array, welches alles aktuellen Steineigenschaften beider Spieler enthaelt
	 * @param steinPos			= ist die Position im SteinArray, von dem ersten Stein der bewegt werden darf
	 * @param moeglicheZuege	= int Array, welches keine Werte hat, dient nur zur Erzeugung	
	 * @param zeile				= int Wert, welches die Position der Zeile des ersten Steines, der bewegt werden kann, enthaelt
	 * @param spalte			= int Wert, welches die Position der Spalte des ersten Steines, der bewegt werden kann, enthaelt
	 * @param zeile1			= int Wert, welches die Position der Zeile des zweiten Steines, der bewegt werden kann, enthaelt
	 * @param spalte1			= int Wert, welches die Position der Spalte des zweiten Steines, der bewegt werden kann, enthaelt
	 * @param steinPos1			= ist die Position im SteinArray, von dem zweiten Stein der bewegt werden darf
	 * @return moeglicheZuege	= int Array, mit den moeglichen Zuegen, die der Spieler mit den gewuerfelten Stein machen kann
	 */
	public int[] setzemoeglicheZuege(int zugehoerigkeit, int steinArray [], int steinPos, int zeile, int spalte,int zeile1, int spalte1, int steinPos1){
		int [] moeglicheZuege = new int [24];	//speichert am Ende alle machbaren Zuege	
		int i = 0;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		/* Zuege von Spieler1 = links oben = O */
		if (zugehoerigkeit == 0){
			/* moegliche Bewegungen anhand der Position einschraenken */
			if (zeile != 1 && spalte != 1 ){		//solange nicht oben in der gegnerischen Ecke, alle Zuege erlauben
				moeglicheZuege[0] = zugehoerigkeit; 		
				moeglicheZuege[1] = steinArray[steinPos];
				moeglicheZuege[2] = zeile - 1; 		
				moeglicheZuege[3] = spalte;
				moeglicheZuege[4] = zugehoerigkeit; 		
				moeglicheZuege[5] = steinArray[steinPos];
				moeglicheZuege[6] = zeile;
				moeglicheZuege[7] = spalte - 1; 	
				moeglicheZuege[8] = zugehoerigkeit; 		
				moeglicheZuege[9] = steinArray[steinPos];
				moeglicheZuege[10] = zeile - 1;		
				moeglicheZuege[11] = spalte - 1;
				i = 12; //naechst folgendes Element merken
			}			
			
			/*ganz links im Feld nur nach oben bewegbar*/
			else if (zeile == 1 && spalte != 1){ 
				moeglicheZuege[0] = zugehoerigkeit; 		
				moeglicheZuege[1] = steinArray[steinPos];
				moeglicheZuege[2] = zeile;
				moeglicheZuege[3] = spalte - 1;	
				i = 4; //naechst folgendes Element merken 
			}
			/*ganz oben im Feld nur nach links bewegbar*/
			else if (zeile != 1 && spalte == 1) { 
				moeglicheZuege[0] = zugehoerigkeit; 		
				moeglicheZuege[1] = steinArray[steinPos];
				moeglicheZuege[2] = zeile - 1;	
			 	moeglicheZuege[3] = spalte;
			 	i = 4; //naechst folgendes Element merken
			}
			else System.out.println("FEHLER");
		}// if(zugehoerigkeit == 0)
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/* Zuege von Spieler1 = links oben = O */
		if (zugehoerigkeit == 1){	
			/* moegliche Bewegungen anhand der Position einschraenken*/
			if (zeile !=5 && spalte !=5){		//solange nicht unten in der gegnerischen Ecke, alle Zuege erlauben
				moeglicheZuege[0] = zugehoerigkeit; 		
				moeglicheZuege[1] = steinArray[steinPos];
				moeglicheZuege[2] = zeile + 1;
				moeglicheZuege[3] = spalte;
				moeglicheZuege[4] = zugehoerigkeit; 		
				moeglicheZuege[5] = steinArray[steinPos];
				moeglicheZuege[6] = zeile;
				moeglicheZuege[7] = spalte + 1;
				moeglicheZuege[8] = zugehoerigkeit; 		
				moeglicheZuege[9] = steinArray[steinPos];
				moeglicheZuege[10] = zeile + 1;
				moeglicheZuege[11] = spalte + 1;
				i = 12;
			}
			
			else if (zeile == 5 && spalte != 5){ //ganz rechts im Feld nur nach unten bewegbar
				moeglicheZuege[0] = zugehoerigkeit; 		
				moeglicheZuege[1] = steinArray[steinPos];
				moeglicheZuege[2] = zeile;
				moeglicheZuege[3] = spalte+1;
				i = 4;
			}
				
			else if (zeile != 5 && spalte == 5  ) { //ganz unten im Feld nur nach rechts bewegbar
				moeglicheZuege[0] = zugehoerigkeit; 		
				moeglicheZuege[1] = steinArray[steinPos];
				moeglicheZuege[2] = zeile+1;
			 	moeglicheZuege[3] = spalte;	
			 	i = 4;			
			 	}
			else System.out.println("FEHLER");	
		}//if(zugehoerigkeit==1)*/		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Für denn Fall 4, mehrere Steine duerfen bewegt werden
		if (steinPos!=0 && steinPos1!=0){
			/* Zuege von Spieler2 = rechts unten = X */
			if (zugehoerigkeit == 0){	
				/* moegliche Bewegungen anhand der Position einschraenken */
				if (zeile1 != 1 && spalte1 != 1){		//alle Zuege sind erlaubt
					moeglicheZuege[i]   = zugehoerigkeit; 		
					moeglicheZuege[i+1] = steinArray[steinPos1];
					moeglicheZuege[i+2] = zeile1 - 1;
					moeglicheZuege[i+3] = spalte1;
					moeglicheZuege[i+4] = zugehoerigkeit; 		
					moeglicheZuege[i+5] = steinArray[steinPos1];
					moeglicheZuege[i+6] = zeile1;
					moeglicheZuege[i+7] = spalte1 - 1;	
					moeglicheZuege[i+8] = zugehoerigkeit; 		
					moeglicheZuege[i+9] = steinArray[steinPos1];
					moeglicheZuege[i+10]= zeile1 - 1;	
					moeglicheZuege[i+11]= spalte1 - 1;	
				}
				/* ganz links im Feld nur Zuege nach oben erlaubt */
				else if (zeile1 == 1 && spalte1 != 1){
					moeglicheZuege[i]   = zugehoerigkeit; 		
					moeglicheZuege[i+1] = steinArray[steinPos1];
					moeglicheZuege[i+2] = zeile1;
					moeglicheZuege[i+3] = spalte1 - 1;			
				}
				/* ganz oben im Feld nur Zuege nach links erlaubt */
				else if (zeile1 != 1 && spalte1 == 1) {
					moeglicheZuege[i]   = zugehoerigkeit; 		
					moeglicheZuege[i+1] = steinArray[steinPos1];
					moeglicheZuege[i+2] = zeile1 - 1 ;
				 	moeglicheZuege[i+3] = spalte1;				
				 	}
				else System.out.println("FEHLER");
			}//if (zugehoerigkeit == 0)
////////////////////////////////////////////////////////////////////////////////////////////////////			
			/* Zuege von Spieler1 = links oben = O*/
			if (zugehoerigkeit == 1){	
				/* moegliche Bewegungen anhand der Position einschraenken */
				if (zeile1 != 5 && spalte1 != 5){		//solange nicht unten in der gegnerischen Ecke, alle Zuege erlauben
					moeglicheZuege[i] = zugehoerigkeit; 		
					moeglicheZuege[i+1] = steinArray[steinPos1];
					moeglicheZuege[i+2] = zeile1 + 1;
					moeglicheZuege[i+3] = spalte1;
					moeglicheZuege[i+4] = zugehoerigkeit; 		
					moeglicheZuege[i+5] = steinArray[steinPos1];
					moeglicheZuege[i+6] = zeile1;
					moeglicheZuege[i+7] = spalte1 + 1;
					moeglicheZuege[i+8] = zugehoerigkeit; 		
					moeglicheZuege[i+9] = steinArray[steinPos1];
					moeglicheZuege[i+10]= zeile1 + 1;
					moeglicheZuege[i+11]= spalte1 + 1;		
				}
				/* ganz rechts im Feld nur nach unten bewegbar */
				else if (zeile1 == 5 && spalte1 != 5){
					moeglicheZuege[i] = zugehoerigkeit; 		
					moeglicheZuege[i+1] = steinArray[steinPos1];
					moeglicheZuege[i+2] = zeile1;
					moeglicheZuege[i+3] = spalte1 + 1;			
				}
				/* ganz unten im Feld nur nach rechts bewegbar */
				else if (zeile1 != 5 && spalte1 == 5  ) {
					moeglicheZuege[i] = zugehoerigkeit; 		
					moeglicheZuege[i+1] = steinArray[steinPos1];
					moeglicheZuege[i+2] = zeile1 + 1;
				 	moeglicheZuege[i+3] = spalte1;				
				 	}
				else System.out.println("FEHLER");	
			}//if(zugehoerigkeit==1)			
			
		}//Ende If für Fall 4*/	
		
	return moeglicheZuege;
	}//setzemoeglicheZuege
	
/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Methode, die einen geschlagenen Stein aus dem SteinArray loescht
	 * 
	 * @param steinArray  = int Array, mit allen Steinen und ihren Eigenschaften
	 * @param zug		  = int Array, mit dem neuen SpielZug
	 * @return steinArray = int Array, mit den neuen Steineigenschaften, nachdem stein rausgeloescht wurde
	 */
	public int[] loescheStein(int steinArray[], int zug[]){
		int feldgroesse = 48; //alle Eigenschaften der Steine 2 Spieler, 4 Eigenschaften, 6 Steine,
		
		/* suche Positionen im Feld, an dem Zeile und Spalte mehrerer Steine gleich sind */
		for (int i = 0; i < feldgroesse; i+=4){
			/* spalte und Zeile der Spielsteine mit Spalte und Zeile des Zugs vergleichen */
			if (steinArray[i+2]==zug[2] && steinArray[i+3] == zug[3]) {			// beide muessen gleichen Wert haben --> Stein loeschen	
				steinArray[i+2] = 0; //zeile loeschen
				steinArray[i+3] = 0; //spalte loeschen
				
				/* Testausgabe ob alles funktioniert */
				//System.out.print(steinArray[i]);   //zugehoerigkeit
				//System.out.print(steinArray[i+1]); //steinnummer
				//System.out.print(steinArray[i+2]); //zugehoerigkeit
				//System.out.print(steinArray[i+3]); //steinnummer
			}
		}
		return steinArray;		
	}//loescheStein
/*-----------------------------------------------------------------------------------------------------------------*/		
	/**
	 * Methode die erkennt, wenn ein Spieler das Spiel gewonnen hat, 
	 * erkennt dies, wenn einer der beiden Spieler an den Ecken 5,5 oder 1,1 ist
	 */
	public boolean siegBedingungen(int SpielerSteinPosBewegt[]){
		int feldgroesse = 48;	//wie weit gesucht werden muss
		boolean siegbedingung = false;
		
		/* suche einen Stein mit den Werten 5 5 oder 1 1 */
		for (int i = 0; i < feldgroesse; i+=4){
			//Spieler 1 gewinnt wenn Spielstein in gegenerischer Ecke
			if((SpielerSteinPosBewegt[i]== 1 && SpielerSteinPosBewegt[i+2]==5) && SpielerSteinPosBewegt[i]== 1 && SpielerSteinPosBewegt[i+3] ==5){ //zeile == 5 und spalte == 5
				siegbedingung = true;
			setzeSieger(1);
			}
			//Spieler 2 gewinnt wenn Spielstein in gegenerischer Ecke
			if(SpielerSteinPosBewegt[i]== 0 && SpielerSteinPosBewegt[i+2]==1 && SpielerSteinPosBewegt[i]== 0 && SpielerSteinPosBewegt[i+3] ==1){ //zeile == 1 und spalte == 1
				siegbedingung = true;
				setzeSieger(0);
			}
			//Spieler 1 hat keine Spielsteine mehr
			if ((SpielerSteinPosBewegt[2] ==0 && SpielerSteinPosBewegt[3]  == 0) &&
			    (SpielerSteinPosBewegt[6] ==0 && SpielerSteinPosBewegt[7]  == 0) &&
			    (SpielerSteinPosBewegt[10]==0 && SpielerSteinPosBewegt[11] == 0) &&
			    (SpielerSteinPosBewegt[14]==0 && SpielerSteinPosBewegt[15] == 0) &&
			    (SpielerSteinPosBewegt[18]==0 && SpielerSteinPosBewegt[19] == 0) &&
			    (SpielerSteinPosBewegt[22]==0 && SpielerSteinPosBewegt[23] == 0)){
				siegbedingung = true;
				setzeSieger(0);
			}
			//Spieler 2 hat keine Spielsteine mehr
			if ((SpielerSteinPosBewegt[26]==0 && SpielerSteinPosBewegt[27] == 0) &&
			    (SpielerSteinPosBewegt[30]==0 && SpielerSteinPosBewegt[31] == 0) &&
			    (SpielerSteinPosBewegt[34]==0 && SpielerSteinPosBewegt[35] == 0) &&
			    (SpielerSteinPosBewegt[38]==0 && SpielerSteinPosBewegt[39] == 0) &&
			    (SpielerSteinPosBewegt[42]==0 && SpielerSteinPosBewegt[43] == 0) &&
			    (SpielerSteinPosBewegt[46]==0 && SpielerSteinPosBewegt[47] == 0)){
				siegbedingung = true;
				setzeSieger(1);
			}
		}
		return siegbedingung;
	}

/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Die Methode setzt welcher Spieler (mit seiner entsprechenden Zugehoerigkeit) gewonnen hat und
	 * gibt die entsprechende zugehoerigkeit zurueck
	 * @param zugehoerigkeit		= int Wert, der eine zugehoerigkeit enthaelt
	 * @return siegerzugehoerigkeit = int Wert, der die zugehoerigkeit zurueck gibt
	 */
	public int setzeSieger(int zugehoerigkeit){
		siegerzugehoerigkeit=zugehoerigkeit;
		return siegerzugehoerigkeit;
	}
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Hier kann die zugehoerigkeit, des Spielers, der gewonnen hat, abgefragt werden
	 * @return siegerzugehoerigkeit = int Wert, enthaelt zugehoerigkeit des Spielers, der gewonnen hat
	 */
	public int gibSieger(){
		return siegerzugehoerigkeit;
	}
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Die Methode prueft, ob die Steine vom Gegner richtig positioniert sind
	 * @param spieler2IntArray 	= int Array, welches die Steineigenschaften des Gegners enthaelt
	 * @return initpruefung 	= boolean, der Aussagt, ob die Pruefung erfolgreich oder nicht erfolgreich war
	 */
	public boolean pruefeGegnerInitialisierung(int[] spieler2IntArray){
		boolean initpruefung=false;
		int  [] richtigeSteine={1,2,3,4,5,6};
		 /*variable die hochgezählt wird fals eine stein ein mal mit den 
		  * jeweiligen nummer existiert. Am ende muss rs=6 sein um weiter prüfen zu können*/
		int rs=0;
		/*variable die die anzahl der richtige Startposition hochzählt, am ende muss rp=6 sein*/
		int rp=0;
		
		int [] initArrayPositionen1Zeile={1,3,2,2,1,1};  //fuer zugehoerigkeit 1
		int [] initArrayPositionen1Spalte={3,1,2,1,2,1};
		
		int [] initArrayPositionen0Zeile={5,3,4,4,5,5};	//fuer zugehoerigkeit 0
		int [] initArrayPositionen0Spalte={3,5,4,5,4,5};
		
		/*prüfen ob die steine richtig 
		 * sind und keine ungültige bzw. doppelte nummer vorhanden sind*/
		for (int i=1;i<23;i=i+4){
			for(int j=0;j<6;j++){
				if(spieler2IntArray[i]==richtigeSteine[j]){
					rs=rs+1;
					richtigeSteine[j]=0;
				}									   
			}
		}		
	
		/*Prüft die Positionen für die Zugehörigkeit 1 des Spielers */
		if (spieler2IntArray[0]==1){
			for (int z=2,s=3; z<24 && s<24; z=z+4,s=s+4){
				for(int j=0;j<6;j++){
					if(spieler2IntArray[z] == initArrayPositionen1Zeile[j]&& spieler2IntArray[s]==initArrayPositionen1Spalte[j]){
						rp=rp+1;
						initArrayPositionen1Zeile[j]=0;	
						initArrayPositionen1Spalte[j]=0;
						}
				}
			}
		}			

		/*Prüft die Positionen für die Zugehörigkeit 0 des Spielers */	
		if (spieler2IntArray[0]==0){
			for (int z=2,s=3; z<24 && s<24; z=z+4,s=s+4){ //zeile und Spalete müssen gleichzeitig hochgezählt werden
				for(int j=0;j<6;j++){
					if(spieler2IntArray[z] == initArrayPositionen0Zeile[j]&& spieler2IntArray[s]==initArrayPositionen0Spalte[j]){
						rp=rp+1;
						initArrayPositionen0Zeile[j]=0;	
						initArrayPositionen0Spalte[j]=0;
					}
				}
			}	
		}
		//es wird geprueft, ob alle Werte nur einmal vorkamen, wenn ja Test erfolgreich
		if (rs==6 && rp==6){
			initpruefung=true;
		}
		else initpruefung=false;
		return initpruefung;
	}//pruefeGegnerInitialisierung
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Methode prueft, ob der jeweilige Zug vom Gegner korrekt ist, d.h. erlaubt ist nach aktueller Spielsituation
	 * @param wuerfelZahl		= int Wert, mit der gewuerfelten Zahl
	 * @param gegnerZug			= int Array, mit dem Zug den der Gegner gemacht hat
	 * @param zugehoerigkeit	= int Wert, den der Gegner zugewiesen bekommen hat
	 * @param SpielerSteinPos	= int Array, mit allen Steineigenschaften von beiden Spielern
	 * @return gepruefterZug	= boolean Wert, der Aussagt, ob eingabe erfolreich oder nicht erfolgreich war 
	 */
	public boolean pruefeGegnerZug(int wuerfelZahl,int [] gegnerZug , int zugehoerigkeit, int SpielerSteinPos[] ){
		boolean gepruefterZug=false;
		
		/*hier werden die moegliche Züge des Gegner berechnet und in ein Array geschrieben*/
		int []moeglichegegenerZuege=gibmoeglicheSpielzuege(zugehoerigkeit,wuerfelZahl,SpielerSteinPos);
		
		/*hier wird vergliechen ob der Zug des Gegners in 
		 * einem seinen möglichen Zugmöglichkeiten befindet.
		 * Sobald sie gefunden wird, ist der Zug dann gültig und somit gepruefterZug=true*/
		for(int i=1; i<=24; i=i+4){
			if (gegnerZug[1]==moeglichegegenerZuege[i] && 
				gegnerZug[2]==moeglichegegenerZuege[i+1] && 
				gegnerZug[3]==moeglichegegenerZuege[i+2])
				gepruefterZug=true;	
		}	
		return gepruefterZug;
	}//pruefeGegnerZug		
	
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Methode legt, aus der Auswahl des Spielers (in der Konsole) bzw. der KI welcher Zug gesetzt werden soll
	 * 
	 * @param auswahl			= int Wert, der aussagt, welcher der moeglichen Zuege gesetzt werden soll
	 * @param moeglicheZuege	= int Array, welches alle aktuell moeglichen Zuege enthaelt
	 * @return zug				= int Array, mit dem gesetzten Zug
	 */
	public int[] benutzerAuswahlInZug(int[] moeglicheZuege, int auswahl){
		int[] zug = new int[4];
		switch (auswahl){
		case 1: {  /* Zug der ersten auswahl als Rückgabewert*/ 
					zug[0]=moeglicheZuege[0]; //zugehoerigkeit
					zug[1]=moeglicheZuege[1]; //steinnummer
					zug[2]=moeglicheZuege[2]; //zeile
					zug[3]=moeglicheZuege[3]; //spalte
					break;
				}
		case 2: {  
					zug[0]=moeglicheZuege[4]; //zugehoerigkeit
					zug[1]=moeglicheZuege[5]; //steinnummer
					zug[2]=moeglicheZuege[6]; //zeile
					zug[3]=moeglicheZuege[7]; //spalte
					break;
				}
		case 3: {	
					zug[0]=moeglicheZuege[8];  //zugehoerigkeit
					zug[1]=moeglicheZuege[9];  //steinnummer
					zug[2]=moeglicheZuege[10]; //zeile
					zug[3]=moeglicheZuege[11]; //spalte
					break;
				}
		case 4: {	 
					zug[0]=moeglicheZuege[12]; //zugehoerigkeit
					zug[1]=moeglicheZuege[13]; //steinnummer
					zug[2]=moeglicheZuege[14]; //zeile
					zug[3]=moeglicheZuege[15]; //spalte
					break;
				}
		case 5: {	
					zug[0]=moeglicheZuege[16]; //zugehoerigkeit
					zug[1]=moeglicheZuege[17]; //steinnummer
					zug[2]=moeglicheZuege[18]; //zeile
					zug[3]=moeglicheZuege[19]; //spalte
					break;
				}
		case 6: {	
					zug[0]=moeglicheZuege[20]; //zugehoerigkeit
					zug[1]=moeglicheZuege[21]; //steinnummer
					zug[2]=moeglicheZuege[22]; //zeile
					zug[3]=moeglicheZuege[23]; //spalte
					break;
				}
		}
		return zug;
	}//userAuswahlInZug
}//Spiellogik	
	
	