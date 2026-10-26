package Interfaces;

/* 
 * Interface:	 Bildschirmanzeige
 * Erstellt am:  24.11.2005 
 * Erstellt von: Arne Meier, Patrick Schwarzer
 * 
 * Änderungen:
 * 26.11.2005, Maria Pidiktaki: Methode bewegeStein wurde hinzugefügt
 * 12.12.2005, Patrick Schwarzer, Arne Meier: Hinzufügung von JavaDocs 
 * 18.01.2006, Arne Meier,Maria Pidiktaki: eindeutschung eng. Bezeichner
 */

/**
 * Interface, welches die Methoden der Bildschirmausgabe vorschreibt *
 */
public interface Bildschirmanzeige {
	public abstract String gibspielerName();
	public abstract void brettinitialisierung (int[] steinArray, String Spieler1, String Spieler2, int spieler1Zugehoerigkeit);
	public abstract int naechsterZug(int moeglicheZuege[], int wuerfelzahl,int SpielerSteinPos[]);
	public abstract int spielerEingabeMenue();
	public abstract String eingabeIP();
	public abstract int auswahlMenue();
	public abstract void ausgabeNachricht(String ausgabe);
	public abstract String eingabe ();
    public abstract void neuerSpielerName();
	public abstract String spieler2EingabeName();
	
}
