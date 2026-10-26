package spiel;
import Interfaces.*;

/*
 * 
 * Klasse 			KI
 * Erstellt am: 	07.01.2006 
 * Erstellt von:   	Patrick Schwarzer
 * 				 	Arne Meier  
 * 
 *
 * Aenderungen:
 * 11.01.2006, Arne Meier, Patrick Schwarzer: setzte Zug um die ausgabe der WuerfelZahl erweitert,
 *                                           KI mit verteidigungsZone und Zugstrategie umgebaut
 * 
 */
/**
 * @author  Bigmama
 */
public class KI implements SpielerInterface{
	private String name;
	private int brettSeite;
	/**
	 * @uml.property  name="bildschirm"
	 * @uml.associationEnd  
	 */
	Bildschirmanzeige bildschirm = null;
	
			
	/* Konstruktor */
	public KI(int brettSeite, String name,Bildschirmanzeige bildschirm){ // brettSeite = zugehoerigkeit
		this.name = name;				//Name des entsprechenden Spielers
		this.brettSeite = brettSeite;	//Position oben oder unten auf dem Spielfeld (=zugehoerigkeit)
		this.bildschirm= bildschirm;
	}
/*-----------------------------------------------------------------------------------------------------------------*/		
	/**
	 * Methode, die je Spieler, die Anfangsposition der Steine, auf dem Spielbrett,
	 * initialisiert
	 * @param zugehoerigkeit = int Wert, der die Spielerzugehoerigkeit der Steine darstellt
	 * @return = int Array, welches für einen Spieler die Steineigenschaften (zugehoerigkeit,
	 * 			 nummer,zeile,spalte) enthält
	 */
	public Stein[] initSteinPos(int zugehoerigkeit){
		int feldgroesse = 6;
		Stein steinInitArray[] = new Stein[feldgroesse];

		/* Spieler1 bekommt Steinpositionen zugewiesen  */
		if (zugehoerigkeit == 1){  	/* 1 = links oben = O */
			//Aufbau - Stein(zugehoerigkeit,nummer,zeile,spalte)
			steinInitArray[0] = new Stein(zugehoerigkeit, 1, 1, 3);
			steinInitArray[1] = new Stein(zugehoerigkeit, 2, 3, 1);
			steinInitArray[2] = new Stein(zugehoerigkeit, 3, 2, 2); 
			steinInitArray[3] = new Stein(zugehoerigkeit, 4, 2, 1); 
			steinInitArray[4] = new Stein(zugehoerigkeit, 5, 1, 2);
			steinInitArray[5] = new Stein(zugehoerigkeit, 6, 1, 1);
		}		
		/* Spieler2 bekommt Steinpositionen zugewiesen  */
		if (zugehoerigkeit == 0){	/* 0 = rechts unten = X */			
			//Aufbau - Stein(zugehoerigkeit,nummer,zeile,spalte)
			steinInitArray[0] = new Stein(zugehoerigkeit, 1, 5, 3); 
			steinInitArray[1] = new Stein(zugehoerigkeit, 2, 3, 5);  
			steinInitArray[2] = new Stein(zugehoerigkeit, 3, 4, 4); 
			steinInitArray[3] = new Stein(zugehoerigkeit, 4, 4, 5); 
			steinInitArray[4] = new Stein(zugehoerigkeit, 5, 5, 4);
			steinInitArray[5] = new Stein(zugehoerigkeit, 6, 5, 5);
		}
		/*Rueckgabe eines der beiden Felder mit den SteinEigenschaften*/
		return steinInitArray;		
	} //initSteinPos
/*-----------------------------------------------------------------------------------------------------------------*/	
	/**
	 * Methode, die ein int Array erzeugt und die Steineigenschaften (Nummer, Zugehoerigkeit, 
	 * Zeile und Spalte) eines jeden Steines beider Spieler in je einem Element des Feldes ablegt
	 * 
	 * @param initSteinPosSpieler1[] = Stein Array mit den Steineigenschaften des ersten Spielers
	 * @param initSteinPosSpieler2[] = Stein Array mit den Steineigenschaften des zweiten Spielers
	 * 
	 * @return steinArray1 = int Array mit den Eigenschaften aller Steine aller Spieler
	 */
	public int[] gibSteinEigenschaftenFeld(Stein initSteinPosSpieler1[], Stein initSteinPosSpieler2[]){

		int feldgroesse = 48;					  //2 Spieler, 4 Eigenschaften je Stein, 6 Steine = 48
		int steinArray[] = new int [feldgroesse]; //Array in die alle Eigenschaften als IntWerte sollen
			
		/* SteinEigenschaften von Spieler1 */				
		for (int i = 0, j = 0; i < (feldgroesse/2); i+=4){
			steinArray[i]	= initSteinPosSpieler1[j].gibZugehoerigkeit(); 
			steinArray[i+1] = initSteinPosSpieler1[j].gibNummer();
			steinArray[i+2] = initSteinPosSpieler1[j].gibZeile();
			steinArray[i+3] = initSteinPosSpieler1[j].gibSpalte();
			j++;
		}
			
		/* Steine Spieler2 */		
		for (int i = (feldgroesse/2), j = 0; i < feldgroesse; i+=4){
			steinArray[i]	= initSteinPosSpieler2[j].gibZugehoerigkeit();
			steinArray[i+1] = initSteinPosSpieler2[j].gibNummer();			
			steinArray[i+2] = initSteinPosSpieler2[j].gibZeile();
			steinArray[i+3] = initSteinPosSpieler2[j].gibSpalte();
			j++;
			}
		//Array mit allen Eigenschaften der Steine beider Spieler zurückgeben
		return steinArray;
	}//gibSteinEigenschaftenArray
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Liefert die Steinpositionen an das Netzwerk
	 * @param initSteinPosSpieler1 = Steineigenschaften der Steine (zueghoerigkeit, steinnummer, zeile, spalte)
	 * @return  String mit den Steineigenschagten, der ueber das Netzwerk zum 
	 * 			Gegenspieler gesendet werden soll
	 */
	public String gibInitialString(Stein initSteinPosSpieler1[]){
			
		String stein1 = null, stein2 = null, stein3 = null, stein4 = null, stein5 = null, stein6 = null;
		stein1 = (initSteinPosSpieler1[0].gibNummer() + "" +
	              initSteinPosSpieler1[0].gibZeile() + "" +
	              initSteinPosSpieler1[0].gibSpalte());
		stein2 = (initSteinPosSpieler1[1].gibNummer() + "" + 
		          initSteinPosSpieler1[1].gibZeile() + ""+
                  initSteinPosSpieler1[1].gibSpalte());
		stein3 = (initSteinPosSpieler1[2].gibNummer() + "" + 
		          initSteinPosSpieler1[2].gibZeile() + ""+
	              initSteinPosSpieler1[2].gibSpalte());
		stein4 = (initSteinPosSpieler1[3].gibNummer() + "" + 
		          initSteinPosSpieler1[3].gibZeile() + ""+
	              initSteinPosSpieler1[3].gibSpalte());
		stein5 = (initSteinPosSpieler1[4].gibNummer() + "" + 
		          initSteinPosSpieler1[4].gibZeile() + ""+
	              initSteinPosSpieler1[4].gibSpalte());
		stein6 = (initSteinPosSpieler1[5].gibNummer() + "" + 
		          initSteinPosSpieler1[5].gibZeile() + ""+
	              initSteinPosSpieler1[5].gibSpalte());

		return "" + stein1 + " " + stein2 + " " + stein3 + " " + stein4 + " " + stein5 + " " + stein6;
	}//gibInitialString
/*-----------------------------------------------------------------------------------------------------------------*/		
	/**
	 * konvertiert das int Array, welches die Steineigenschaften enthaelt, in ein Stein Array
	 * @param gegnerInit	= int Array, mit den Steineigenschaten
	 * @return				= Stein Array, ebenfalls mit den Steineigenschaften
	 */
	public Stein[] spieler2InitSteinPos(int[] gegnerInit){
		int feldgroesse = 6;
		Stein steinInitArray[] = new Stein[feldgroesse];

		/* Spieler2 bekommt Steinpositionen zugewiesen  */			
		steinInitArray[0] = new Stein(gegnerInit[0], gegnerInit[1], gegnerInit[2],gegnerInit[3]);
		steinInitArray[1] = new Stein(gegnerInit[4], gegnerInit[5], gegnerInit[6],gegnerInit[7]);
		steinInitArray[2] = new Stein(gegnerInit[8], gegnerInit[9], gegnerInit[10],gegnerInit[11]); 
		steinInitArray[3] = new Stein(gegnerInit[12], gegnerInit[13], gegnerInit[14],gegnerInit[15]); 
		steinInitArray[4] = new Stein(gegnerInit[16], gegnerInit[17], gegnerInit[18],gegnerInit[19]);
		steinInitArray[5] = new Stein(gegnerInit[20], gegnerInit[21], gegnerInit[22],gegnerInit[23]);
				
		/*Rueckgabe eines der beiden Felder mit den SteinEigenschaften*/
		return steinInitArray;		
	} //initSteinPos
/*-----------------------------------------------------------------------------------------------------------------*/			
	/**
	 * Methode, die den Spielernamen zurückgibt
	 * @return name = String, der den jeweiligen Namen des Spielers zurückgibt
	 */
	public String gibName(){
		return name;
	 } 	
/*-----------------------------------------------------------------------------------------------------------------*/		
	/**
	 * Methode, die die Spielerzugehoerigkeit (brettseite) zurückgibt
	 * @return brettSeite = int Wert, der die Brettseite des Spielers charakterisiert
	 */
	public int gibZugehoerigkeit(){
		 return brettSeite;
	}
/*-----------------------------------------------------------------------------------------------------------------*/
	/**
	 * Methode waehlt aus den moeglichen Zuegen einen aus und gibt den gewaehlten zurueck
	 * @param = int Array, welches die naechst moeglichen Zuege enthaelt
	 * @param = int Wert, der die wuerfelzahl enthaelt: wird hier aber nicht benoetigt
	 * @param = int Array, welches alle Steineigenschaften enthaelt
	 * @return = int Wert, enthaelt den ausgewaehlten Zug aus den moeglichen Zuegen
	 */
	public int setzeZug(int[] moeglicheZuege, int[] steinPosArray, int wuerfelzahl){
		int kurzEntferungZumZiel = 1;
		int entferungsWertNeu = 100 // strtet mit der höchsten entferung damit beim vergleichen jeder eine Chance hat
		    , entfernungsWertAlt = 100;
		int siegWert = 100;
		int[][] strategieArrayUntenRechts = {{100,90,65,60,0},{90,85,75,55,50},{70,75,70,50,47},{75,60,55,45,40},{0,50,47,40,0}};//alt
		//int[][] strategieArrayObenLinks   = {{0,40,47,50,0},{40,45,55,60,75},{47,50,70,75,70},{50,55,75,85,90},{0,60,65,90,100}};//alt
		//int[][] strategieArrayUntenRechts = {{100,90,65,60,0},{90,85,75,55,50},{80,75,65,40,30},{0,70,40,45,25},{0,0,49,25,0}};//neu
		int[][] strategieArrayObenLinks   = {{0,25,49,0,0},{25,45,40,70,0},{30,40,65,75,80},{50,55,75,85,90},{0,60,65,90,100}};//neu
		int[][] verteidigungsZoneUnten = {{3,3},{3,4},{3,5},{4,3},{4,4},{4,5},{5,3},{5,4}};
		int[][] verteidigungsZoneOben = {{1,2},{1,3},{2,1},{2,2},{2,3},{3,1},{3,2},{3,3}};
		int feldgroesse = 24;
		int positionZuege = 0;			//ermittelte Position, wieviele Zuege existieren
		int anzahlGegner = 0;           // Anzahl der Gegner in der Zone
		int[] positionGegner = new int[12];// maximal 6 Steine á 2 Positionsdaten
		boolean kannGegnerGeschlagenwerden = false;
		
		//Test für uns
		//System.out.println("Moegliche Zuege: ");
		//for(int i = 0; i < moeglicheZuege.length; i++){
		//	System.out.print(moeglicheZuege[i]);
		//}
		
		bildschirm.ausgabeNachricht(">>>>" + name + " hat gewurfelt: " + wuerfelzahl + "<<<<");
					
		
		/*ermittle die Anzahl der moeglichen Zuege*/
		for (int i = 0; i < feldgroesse; i+=4){
			if (moeglicheZuege[i+2]== 0 && moeglicheZuege[i+3]==0){} //wenn Zeile und Spalte == 0 sind, soll nicht mehr gezaehlt werden
			else
				positionZuege++;	//Anzahl moeglicher Zuege zaehlen (1, 3, 4 oder 6)
		}
		
		//System.out.println("anzahl der möglichkeiten; " + positionZuege);
		
		//Prüfen Ob Gegner in gefaehrlicher Zone ist
		if(moeglicheZuege[0]==0){
			//Prüfen wo Gegner sind Gegner = 1
			for(int i = 0, k = 0; i < steinPosArray.length; i+=4){ // i= SteinPosArray-Zaehler, k = PositionGegner-Array-Zaehler, j-ZonenArray-Zaehler
				if(steinPosArray[i] == 1 && steinPosArray[i+2]!=0 && steinPosArray[i+3] != 0){ // Suchte die Steine des Gegners die noch nicht geloescht sind
					for(int j = 0; j < 8; j++){   // 8 ist die Anzahl der Zeilen des 2d-ZonenArrays
//						Hier wird verglichen welche der nichtgeloeschten Steine mit den Positionen des ZonesArrays übereinstimmen
						if(steinPosArray[i+2] == verteidigungsZoneUnten[j][0] && steinPosArray[i+3] == verteidigungsZoneUnten[j][1]){
							//System.out.println("Gegner 1 in Zone");
							anzahlGegner++;
							positionGegner[k] = steinPosArray[i+2];   // Wenn eine Uebereinstimmung besteht dann wird die Position
							positionGegner[k+1] = steinPosArray[i+3]; // in ein "Gegner ist auf Postion X in Zone"-Array geschrieben 
							k+=2;
						}
					} 
				}
			}
		}else if(moeglicheZuege[0]==1){
//			    Prüfen wo Gegner sind Gegner = 0
				for(int i = 0, k = 0; i < steinPosArray.length; i+=4){ // i= SteinPosArray-Zaehler, k = PositionGegner-Array-Zaehler, j-ZonenArray-Zaehler
					if(steinPosArray[i] == 0 && steinPosArray[i+2]!=0 && steinPosArray[i+3] != 0){
						for(int j = 0; j < 8; j++){   // 8 ist die Anzahl der Zeilen des 2d-ZonenArrays
//							Hier wird verglichen welche der nichtgeloeschten Steine mit den Positionen des ZonesArrays übereinstimmen
							if(steinPosArray[i+2] == verteidigungsZoneOben[j][0] && steinPosArray[i+3] == verteidigungsZoneOben[j][1]){
								//System.out.println("Gegner 0 in Zone");
								anzahlGegner++;
								positionGegner[k] = steinPosArray[i+2];  // Wenn eine Uebereinstimmung besteht dann wird die Position
								positionGegner[k+1] = steinPosArray[i+3];// in ein "Gegner ist auf Postion X in Zone"-Array geschrieben 
								k+=2;
							}
						}
					}
				}
		}
		
		//System.out.println("anzahlGegner: " + anzahlGegner);
		
		// pruefen ob eine Stein von Uns diesen Gegner schlagen kann
		int zuegeGegnerSchlagen = positionZuege; //positionZuege sichern damit es wenn der Gegner nicht geschlagen werden kann nicht schon durch gelaufen ist
		if(anzahlGegner  != 0){
			//Schauen ob Gegner geschlagen werden kann
			zuegeGegnerSchlagen = zuegeGegnerSchlagen * 4;  //Anzahl der zu ziehende Steine *4 Attribute
			anzahlGegner = anzahlGegner * 2;            //Anzahl der Gegner in der Zone *2 PositionsAttribute
			for(int i =0, k = 1; i < zuegeGegnerSchlagen; i+=4){
				for(int j = 0; j < anzahlGegner; j+=2){
					if(moeglicheZuege[i+2] == positionGegner[j] && moeglicheZuege[i+3] == positionGegner[j+1]){
						//Gegner kann geschlagen werden 
						kannGegnerGeschlagenwerden = true;
						kurzEntferungZumZiel = k;
						
					}
					
				}	
				k++; // in der i+=4Schleife das k für die nächste Zug-Moeglichkeit rauf setzten
			}
		}
		if(anzahlGegner  == 0 || kannGegnerGeschlagenwerden == false){//Kein Gegner in Zone ODER kann nicht geschlagen werden
			
			positionZuege = positionZuege * 4;  //Anzahl der zu ziehende Steine *4 Attribute
			
			//Sucht den Stein der nach unserem StrategieArray am besten waere fuer den Fall dass wir UNTEN sind
			if(moeglicheZuege[0]==0){
				for(int i =0, k = 1; i < positionZuege; i+=4){
					if(entfernungsWertAlt >= entferungsWertNeu){
						entfernungsWertAlt = entferungsWertNeu;
					}			
					
					entferungsWertNeu = siegWert - strategieArrayUntenRechts[moeglicheZuege[i+2]-1][moeglicheZuege[i+3]-1];
					if(entferungsWertNeu < entfernungsWertAlt){
						kurzEntferungZumZiel = k;
					}
					k++; 
				}		
			}else if(moeglicheZuege[0]==1){//Sucht den Stein der nach unserem StrategieArray am besten waere fuer den Fall dass wir OBEN sind
				for(int i =0, k = 1; i < positionZuege; i+=4){
					if(entfernungsWertAlt >= entferungsWertNeu){
						entfernungsWertAlt = entferungsWertNeu;
					}
					
					entferungsWertNeu = siegWert - strategieArrayObenLinks[moeglicheZuege[i+2]-1][moeglicheZuege[i+3]-1];
					if(entferungsWertNeu < entfernungsWertAlt){
						kurzEntferungZumZiel = k;
					}
					k++;
				}
			}
		}
		
		
		
		/* von der KI ausgewaehlter Zug */
		//auswahl = (int) Math.round(Math.random()*position+0.5);	
		try {
			Thread.sleep(0000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* das kurzEntferungZumZiel sagt aus welcher der bis zu 6 möglichen Zuege die KI ausgwählt
		 *hat die zahl kann aber nur mit Kenntnis der Uebergabewerte an einen Menschlichen Spieler 
		 *interpretiert werden.
		 */
		bildschirm.ausgabeNachricht(">>>>" + name + " hat ausgewaehlt: " + kurzEntferungZumZiel + "<<<<");
		return kurzEntferungZumZiel;

	}
}//KI
