package spiel;
import Interfaces.SpielerInterface;
import Interfaces.Bildschirmanzeige;

/*
 * 
 * Klasse Spieler
 * Erstellt am: 	16.11.2005 
 * Erstellt von: Maria Pidiktaki,
 * 				 Arne Meier  
 * Mitgearbeitet: Sebastian Horchler(String Methode)
 *
 * Aenderungen:
 * 26.11.2005, Arne Meier, Maria Pidiktaki: kleine Veraenderungen des Aufbaus
 * 12.12.2005, Patrick Schwarzer, Arne Meier: Hinzufügung von JavaDocs 
 * 17.12.2005, Patrick Schwarzer, Sebastian Horchler: neue Methoden: initSteinPost, gibSteinEigenschaftenArray
 * 													  loeschen einiger alter Methoden, da uneffizient und sinnlos
 * 07.01.2006, Patrick Schwarzer, Arne Meier: Hinzfügen der Methode setzeZug
 */

/**
 * @author  Bigmama
 */
public class Spieler implements SpielerInterface {
	private String name;
	private int brettSeite;
	/**
	 * @uml.property  name="bildschirm"
	 * @uml.associationEnd  
	 */
	private Bildschirmanzeige bildschirm;	//benoetigt in setze Zug
		
	/* Konstruktor */
	public Spieler(int brettSeite, String name, Bildschirmanzeige bildschirm){ // brettSeite = zugehoerigkeit
		this.name = name;				//Name des entsprechenden Spielers
		this.brettSeite = brettSeite;	//Position oben oder unten auf dem Spielfeld (=zugehoerigkeit)
		this.bildschirm = bildschirm;
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
			steinInitArray[0] = new Stein(zugehoerigkeit, 1, 1, 2);
			steinInitArray[1] = new Stein(zugehoerigkeit, 2, 3, 1);
			steinInitArray[2] = new Stein(zugehoerigkeit, 3, 2, 2); 
			steinInitArray[3] = new Stein(zugehoerigkeit, 4, 2, 1); 
			steinInitArray[4] = new Stein(zugehoerigkeit, 5, 1, 3);
			steinInitArray[5] = new Stein(zugehoerigkeit, 6, 1, 1);
		}		
		/* Spieler2 bekommt Steinpositionen zugewiesen  */
		if (zugehoerigkeit == 0){	/* 0 = rechts unten = X */			
			//Aufbau - Stein(zugehoerigkeit,nummer,zeile,spalte)
			steinInitArray[0] = new Stein(zugehoerigkeit, 1, 5, 4); 
			steinInitArray[1] = new Stein(zugehoerigkeit, 2, 3, 5);  
			steinInitArray[2] = new Stein(zugehoerigkeit, 3, 4, 4); 
			steinInitArray[3] = new Stein(zugehoerigkeit, 4, 4, 5); 
			steinInitArray[4] = new Stein(zugehoerigkeit, 5, 5, 3);
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
	}
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
	 * Methode lasst den menschlichen Spieler aus den moeglichen Zuegen einen auswaehlen und gibt den gewaehlten Zug
	 * zurueck
	 * @param moeglicheZuege 	= int Array, welches die naechst moeglichen Zuege enthaelt
	 * @param steinPosArray		= int Array, mit den Steineigenschaften, wird hier nicht benoetigt
	 * @param wuerfelzahl 		= int Wert, der die Wuerfelzahl enhaelt, wird in Methode naechster Zug gebraucht
	 */
	public int setzeZug(int[] moeglicheZuege,int []steinPosArray, int wuerfelzahl){
		int auswahl = 0;
		auswahl = bildschirm.naechsterZug(moeglicheZuege, wuerfelzahl,steinPosArray); //zu setzenden Zug vom Nutzer eingeben lassen
		return auswahl;
	}
	
}//Spieler
