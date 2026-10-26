/* 
 * Klasse:		 SpielMain
 * Erstellt am:  23.11.2005 
 * Erstellt von: Arne Meier, Sebastian Horchler
 * 
 * Änderungen: 
 * 26.11.2005, Maria Pidiktaki, Arne Meier: Menuauswahl wurde hinzugefügt und die 
 * 											Initialisierungen entsprechend der Caseanweisung
 * 10.12.2005, Maria Pidiktaki, Arne Meier: komplette Ueberarbeitung, Kuerzung		
 * 12.12.2005, Patrick Schwarzer, Arne Meier: Hinzufügung von JavaDocs
 */

package spiel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//import java.io.BufferedReader;

public class SpielMain {	
	
	/**
	 * Hauptprogramm, welches die Ausgabeform abfragt und dann das Auswahlmenue startet 
	 * s = Instanz von Steuerung, fragt Ausgabeform ab Konsole oder GUI
	 */
	
		
  
	public static void main(String[] args) {
		Steuerung s = new Steuerung();
		int ausgabeForm = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("PC Spiel: EINSTEIN WUERFELT NICHT!");
		System.out.println("==================================");
		
		System.out.println("\n \n" +"Auswahl der graphischen Ausgabeform:");
		
		System.out.println("\n \n"+" 1  Konsole");
		System.out.println("\n"+" 2  GUI");
		System.out.print("\n"+"Bitte geben sie eine der Nummern ein: ");
		
		/* Schutz vor Falscheingaben */
		do
		{
			try
			{
				ausgabeForm = Integer.parseInt(in.readLine()); //Eingabe und Wertueberschreibung
			}
			catch (Exception e){
				System.out.println("Eingabefehler" +e);
			}
			if (ausgabeForm > 2 || ausgabeForm < 1){					//Hinweis bei Falscheingabe
				System.out.println("Eingabefehler! Nur die Zahlen 1 und 2 zulaessig!");
				System.out.print("Bitte erneut eingeben: ");
			}
		} while (ausgabeForm > 2 || ausgabeForm < 1);
		
		
		s.ausgabeForm(ausgabeForm);	//Auswahl der Ausgabeform
		s.spielmenue();		//startet zur Auswahl die entsprechenden Methoden
	}	
  
}