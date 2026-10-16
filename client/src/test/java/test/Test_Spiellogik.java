package test;
import spiel.Spiellogik;
import junit.framework.TestCase;
import static org.junit.Assert.assertArrayEquals;
/*
 * Klasse:			Test_Spiellogik
 * Erstellt am: 	22.12.2005 
 * Erstellt von: 	Patrick Schwarzer
 * 					Arne Meier
 * 
 * Testet die Funktionsfaehigkeit der Methoden aus Spiellogik
 * 
 * Änderungen: 
 * 													            
 */

/**
 * @author  Bigmama
 */
public class Test_Spiellogik extends TestCase{
	/**
	 * @uml.property  name="testLogik"
	 * @uml.associationEnd  
	 */
	Spiellogik testLogik = new Spiellogik();
	
	/**
	 * Testet die Methode gibIntArray aus der Spiellogik auf ihre Funktionsfaehigkeit
	 */
	public void testGibIntArray(){
		int spielerzugehoerigkeit0 = 0;
		int spielerzugehoerigkeit1 = 1;
		String spielzug = "123";
		String anfangspositionen = "113 231 322 421 512 611";
		String fehlerStringGibIntArray = "dieser String ist nicht 3 oder 23 Zeichen lang";
		int[] spielzugArray0 = {0,1,2,3};
		int[] spielzugArray1 = {1,1,2,3};
		int[] spielInitArray1 = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1};
		int[] spielInitArray0 = {0,1,1,3,0,2,3,1,0,3,2,2,0,4,2,1,0,5,1,2,0,6,1,1};
		
		int[] zuTestendesArray = testLogik.gibIntFeld(spielzug,spielerzugehoerigkeit0);
		for(int i = 0; i < 4; i++){
			assertEquals(spielzugArray0[i], zuTestendesArray[i]);
		}
		zuTestendesArray = testLogik.gibIntFeld(spielzug,spielerzugehoerigkeit1);
		for(int i = 0; i < 4; i++){
			assertEquals(spielzugArray1[i], zuTestendesArray[i]);
		}
		zuTestendesArray = testLogik.gibIntFeld(anfangspositionen,spielerzugehoerigkeit0);
		for(int i = 0; i < 18; i++){
			assertEquals(spielInitArray0[i], zuTestendesArray[i]);
		}
		zuTestendesArray = testLogik.gibIntFeld(anfangspositionen,spielerzugehoerigkeit1);
		for(int i = 0; i < 18; i++){
			assertEquals(spielInitArray1[i], zuTestendesArray[i]);
		}
		zuTestendesArray = testLogik.gibIntFeld(fehlerStringGibIntArray,spielerzugehoerigkeit1);
		for(int i = 0; i < 2; i++){
			assertEquals(0, zuTestendesArray[i]);
		}
	}
	
	
	/**
	 * Testfunktion der Siegbedingungen
	 */
	public void testSiegbedingungen(){
		int SpielerSteinPosWins[] = {1,1,5,5,1,2,3,1,1,3,2,2,1,4,3,2,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,0,0};
		int SpielerSteinPosLoose[]= {1,1,1,3,1,2,3,1,1,3,2,2,1,4,3,2,1,5,1,2,1,6,0,0,0,1,1,1,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		int SpielerSteinPosElse[] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,3,2,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		int SpielerSteinPosSp1NoSteine[]= {1,1,0,0,1,2,0,0,1,3,0,0,1,4,0,0,1,5,0,0,1,6,0,0,0,1,1,1,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		int SpielerSteinPosSp2NoSteine[] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,3,2,1,5,1,2,1,6,1,1,0,1,0,0,0,2,0,0,0,3,0,0,0,4,0,0,0,5,0,0,0,6,0,0};
		
		assertEquals(true,testLogik.siegBedingungen(SpielerSteinPosWins)); //falls Spieler1 gewinnt gibt es true zurueck
		assertEquals(true,testLogik.siegBedingungen(SpielerSteinPosLoose)); //falls Spieler2 gewinnt gibt es true zurueck
		assertEquals(false,testLogik.siegBedingungen(SpielerSteinPosElse)); //solang kein Spieler gewonnen hat
		assertEquals(true,testLogik.siegBedingungen(SpielerSteinPosSp1NoSteine)); //Spieler1 hat keine Steine mehr
		assertEquals(true,testLogik.siegBedingungen(SpielerSteinPosSp2NoSteine)); //Spieler2 hat keine Steine mehr
	}
	
	/**
	 * Testet, ob ein Stein geloescht wird
	 * Methode geht nicht, wenn man statt geloeschtesIntArray das intArray einsetzt, wird es gruen, aber das ist 
	 * ja gedanklich falsch, weil der Stein so als nicht geloescht anzusehen ist
	 */
	public void testloescheStein(){
	 	int intArray[] 			  = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
	 	int geloeschterSteinArray[] = {1,1,1,3,1,2,3,1,1,3,0,0,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
	 	int zug1 [] = {1,6,2,2};
	 	
	 	int testArray [] = testLogik.loescheStein(intArray, zug1);
	 	int feldgroesse = testArray.length;
	 	
	 	/*prueft Elementweise ob die Methode die richtige Loesung liefert*/
	 	for (int i = 0; i < feldgroesse; i++)
	 		assertEquals(geloeschterSteinArray[i],testArray[i]); 
	}
	
	/**
	 * Testet die Funktion von konvertiereZugStringToInt, welche einen einzelnen Zug, der als String vom Server kommt
	 * in einzelne int-Werte in ein Array ablegt
	 */
	public void testkonvertiereZugStringToInt(){
		String gegnerSteinPos = "113";					//die 2 Zuege, die vom Gegner kommen (im Multiplayer)
		String spielerSteinPos = "231";
		int gegnerZugehoerigkeit = 0;					//die  moeglichen spielerzugehoerigkeiten
		int spielerZugehoerigkeit = 1;
		int stringLaenge = gegnerSteinPos.length(); 	//Laenge der Strings messen
		int stringLaenge2 = spielerSteinPos.length(); 	
		int gegnerSteinPosInt[] = {0,1,1,3}; 			//Loesungen, welche die Methode liefern soll
		int spielerSteinPosInt[] = {1,2,3,1};			
		int testArray  [] = testLogik.konvertiereZugStringZuInt(gegnerSteinPos, gegnerZugehoerigkeit, stringLaenge);
		int testArray2 [] = testLogik.konvertiereZugStringZuInt(spielerSteinPos, spielerZugehoerigkeit, stringLaenge2);
		int feldgroesse   = testArray.length;	//Laenge der Arrays messen
		int feldgroesse2  = testArray2.length;
		
		/*prueft Elementweise ob die Methode die richtige Loesung liefert*/
		for (int i = 0; i < feldgroesse; i++)
			assertEquals(gegnerSteinPosInt[i],testArray[i]);
		
		/*prueft Elementweise ob die Methode die richtige Loesung liefert*/
		for (int i = 0; i < feldgroesse2; i++)
			assertEquals(spielerSteinPosInt[i],testArray2[i]);

	}
	
	/**
	 * Testet die funktionsfähigkeit der Methode: GibmoeglicheSpielzuege
	 */
	public void testGibmoeglicheSpielzuege(){		
		//Stein 5 soll bewegt werden
		int steinArray1[] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		int LoesungsArray1[] = {0,5,4,4,0,5,5,3,0,5,4,3,0,0,0,0,0,0,0,0,0,0,0,0};	//Zuege Stein 5, Spieler 2
		int testArray1[] = testLogik.gibmoeglicheSpielzuege (0,5,steinArray1);
		int feldgroesse1 = testArray1.length;
		
		for (int i = 0; i < feldgroesse1; i++)
			assertEquals(LoesungsArray1[i],testArray1[i]);
/*- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/		
		//Stein 2 soll bewegt werden
		//int steinArray1[] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		int LoesungsArray2[] = {1,2,4,1,1,2,3,2,1,2,4,2,0,0,0,0,0,0,0,0,0,0,0,0};   //Zuege Stein 2, Spieler 1
		int testArray2[] = testLogik.gibmoeglicheSpielzuege (1,2,steinArray1);
		int feldgroesse2 = testArray2.length;
		
		for (int i = 0; i < feldgroesse2; i++)
			assertEquals(LoesungsArray2[i],testArray2[i]);
/*- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
		//Stein 2 soll bewegt werden, jedoch existiert dieser Stein nicht mehr
		int steinArray2[] = {1,1,1,3,1,2,0,0,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		int LoesungsArray3[] = {1,1,2,3,1,1,1,4,1,1,2,4,1,3,3,2,1,3,2,3,1,3,3,3};   //Zuege Stein 2, Spieler 1 bei geloeschtem Stein 2
		int testArray3[] = testLogik.gibmoeglicheSpielzuege (1,2,steinArray2);
		int feldgroesse3 = testArray3.length;
		
		for (int i = 0; i < feldgroesse3; i++)
			assertEquals(LoesungsArray3[i],testArray3[i]);
/*- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/		
		//es existiert nur ein Stein, der groesser ist als der Ausgangsstein Stein 2
		int steinArray3[] = {1,1,0,0,1,2,0,0,1,3,0,0,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		int testArray4[] = testLogik.gibmoeglicheSpielzuege (1,2,steinArray3);
		int LoesungsArray4[] = {1,4,3,1,1,4,2,2,1,4,3,2,0,0,0,0,0,0,0,0,0,0,0,0};
		int feldgroesse4 = testArray4.length;
		
		for (int i = 0; i < feldgroesse4; i++)
			assertEquals(LoesungsArray4[i],testArray4[i]);
/*- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/	
		//der gewuerfelte Stein darf nur in eine Richtung ziehen, er befindet sich ganz links unten
		int steinArray4[] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,0,0,0,2,0,0,0,3,0,0,0,4,0,0,0,5,0,0,0,6,5,1};
		int testArray5[] = testLogik.gibmoeglicheSpielzuege (0 ,6,steinArray4);
		int LoesungsArray5[] = {0,6,4,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int feldgroesse5 = testArray4.length;
				
		for (int i = 0; i < feldgroesse5; i++)
			assertEquals(LoesungsArray5[i],testArray5[i]);
	}
	
	/**
	 * Test, der prueft ob die Anfangsposition der Steine ordnungsgemaeß umgewandelt werden
	 */
	public void testKonvertiereStringToInt(){	
		String SteinPos = "113 231 322 421 512 611"; 	//simuliertes StringArray, was vom Netzwerk kommt
		int spielerZugehoerigkeit = 0;					//zugewiesene Spielerzugehoerigkeit
		int gegnerZugehoerigkeit = 1;	
		int stringLaenge = SteinPos.length();			//Laenge des Ausgangsstrings messen
		int LoesungsArraySpieler[] = {0,1,1,3,0,2,3,1,0,3,2,2,0,4,2,1,0,5,1,2,0,6,1,1}; // Loesungen, die Methode liefern soll
		int LoesungsArrayGegner [] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1};
		int testArraySpieler [] = testLogik.konvertiereStringZuInt(SteinPos, spielerZugehoerigkeit, stringLaenge); // Methode ausfuehren
		int testArrayGegner  [] = testLogik.konvertiereStringZuInt(SteinPos, gegnerZugehoerigkeit, stringLaenge);
		int feldgroesse = testArraySpieler.length; //Laengen der Arrays, die die Methode liefert ermitteln
		int feldgroesse2 = testArrayGegner.length;
		
		for (int i = 0; i < feldgroesse; i++)
			assertEquals(LoesungsArraySpieler[i],testArraySpieler[i]);
		
		for (int i = 0; i < feldgroesse2; i++)
			assertEquals(LoesungsArrayGegner[i],testArrayGegner[i]);
		
	}	

	/*Prüung der 6 ersten Positinoen des Gegners(von Netzwerk)*/
	public void testpruefeGegnerInitialisierung(){
		int[] testGegnerInitArray1 = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1};
	
		assertEquals(true,testLogik.pruefeGegnerInitialisierung(testGegnerInitArray1));
		int[] testGegnerInitArray2 = {1,1,1,0,1,2,3,1,1,3,2,2,1,4,2,1,1,0,1,2,1,6,1,1};
		assertEquals(false,testLogik.pruefeGegnerInitialisierung(testGegnerInitArray2));
	}
	
	/**
	 * Methode testet die Funktionsfaehigkeit der Methode bewegeStein
	 */
	public void testBewegeStein(){
		int steinArray[] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		                  
		/* normaler Zug auf ein freies Feld */
		int zug1[] = {0,1,4,3};
		int LoesungsArray1[] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,4,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		assertArrayEquals(LoesungsArray1, testLogik.bewegeStein(steinArray,zug1)); //pruefen ob Zug1 richtig durchgefuehrt wird
		
		/* Zug bei dem ein anderer Stein geloeschte wird */
		int zug2[] = {0,6,4,4};		
		int LoesungsArray2[] = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,4,3,0,2,3,5,0,3,0,0,0,4,4,5,0,5,5,4,0,6,4,4};
		assertArrayEquals(LoesungsArray2, testLogik.bewegeStein(steinArray,zug2)); //pruefen ob Zug2 richtig durchgefuehrt wird
	}
	
	public void testsetzemoeglicheZuege(){
		/* der gewuerfelte Stein darf auch bewegt werden */
		int zugehoerigkeit 	= 1;
		int steinArray [] 	= {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		int steinPos		= 5;
		int zeile			= 3;
		int spalte			= 1;
		int zeile1			= 0;
		int spalte1			= 0;
		int steinPos1		= 0;
		int LoesungsArray[]	= {1,2,4,1,1,2,3,2,1,2,4,2,0,0,0,0,0,0,0,0,0,0,0,0};
		assertArrayEquals(LoesungsArray,testLogik.setzemoeglicheZuege(zugehoerigkeit, steinArray, steinPos, zeile, spalte, zeile1, spalte1, steinPos1));
		
		/* der gewuerfelte Stein existiert nicht mehr, nur kleinere und groesere */
		zugehoerigkeit 			= 1;
		int steinArray2 [] 		= {1,1,1,3,1,2,0,0,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1,0,1,5,3,0,2,3,5,0,3,4,4,0,4,4,5,0,5,5,4,0,6,5,5};
		steinPos				= 1;
		zeile					= 1;
		spalte					= 3;
		zeile1					= 2;
		spalte1					= 2;
		steinPos1				= 9;
		int LoesungsArray2 []	= {1,1,2,3,1,1,1,4,1,1,2,4,1,3,3,2,1,3,2,3,1,3,3,3};
		assertArrayEquals(LoesungsArray2,testLogik.setzemoeglicheZuege(zugehoerigkeit, steinArray2, steinPos, zeile, spalte, zeile1, spalte1, steinPos1));
	}

	
}
