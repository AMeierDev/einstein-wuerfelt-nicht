package spiel;

/*
 * 
 * Klasse:		 Stein 
 * Erstellt am:  16.11.2005 
 * Erstellt von: Maria Pidiktaki, Arne Meier
 * 
 * Änderungen: 
 * 26.11.2005 Patrick Schwarzer, Arne Meier:  Methode bewegeStein() wurde um die 
 * 											  Ausgabebewegung erweitert.
 * 12.12.2005, Patrick Schwarzer, Arne Meier: Hinzufügung von JavaDocs
 * 17.12.2005, Patrick Schwarzer, Sebastian Horchler: Methoden geloescht bewegeStein()
 * 
 * 
 */
public class Stein {
 private int zugehoerigkeit;
 private int zeile;
 private int spalte;
 private int nummer;
 
 	/* Konstruktor - Verwendet von Spieler.java */
 	public Stein(int brettseite, int nummer, int zeile, int spalte)
 	{
 		/* uebergebene Variablen werden in lokale Variablen geschrieben */
 		this.zugehoerigkeit = brettseite;		//zugehoerigkeit des Steins
 		this.zeile = zeile;						//Zeilenposition des Steins
 		this.spalte = spalte;					//Spaltenposition des Steins
 		this.nummer = nummer;					//Steinnummer  		
 	} 	
/*-----------------------------------------------------------------------------------------------------------------*/
 	/**
 	 * Methode gibt Zugehoerigkeit des entsprechenden Steins zurück  
 	 * @return Boolean-Wert, der die Zugehoerigkeit des entsprechenden Steins darstellt
 	 */
 	public int gibZugehoerigkeit(){
 		return zugehoerigkeit;
 	}
/*-----------------------------------------------------------------------------------------------------------------*/	
 	/**
 	 * Methode, die Zeilenposition des entsprechenden Steins zurueckgibt
 	 * @return int Wert, der die Zeilenposition des entsprechenden Steins darstellt
 	 */
 	public int gibZeile(){
 		return zeile;
 	} 	
/*-----------------------------------------------------------------------------------------------------------------*/	
 	/**
 	 * Methode, die Spaltenposition des entsprechenden Steins zurueckgibt
 	 * @return int Wert, der die Spaltenposition des entsprechen Steins darstellt
 	 */
 	public int gibSpalte(){
 		return spalte;
 	}
/*-----------------------------------------------------------------------------------------------------------------*/
 	/**
 	 * Methode, die Steinnummer des entsprechenden Steins zurueckgibt
 	 * @return int Wert, der die Steinnummer des entsprechenden Steins darstellt
 	 */
 	public int gibNummer(){
 		return nummer;
 	}
}
