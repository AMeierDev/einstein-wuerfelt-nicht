package test;
import spiel.*;
import junit.framework.TestCase;
import Interfaces.Bildschirmanzeige;
/*
 * Klasse:			Spiellogik
 * Erstellt am: 	22.12.2005
 * Erstellt von: 	Patrick Schwarzer
 * 					Arne Meier
 * 
 * testet die SpielLogik
 * 
 * 
 * 													            
 */

/**
 * @author  Bigmama
 */
public class Test_Spieler extends TestCase{
	/**
	 * @uml.property  name="bildschirm"
	 * @uml.associationEnd  
	 */
	Bildschirmanzeige bildschirm = new Konsole();
	/**
	 * @uml.property  name="testSpielerObenLinks"
	 * @uml.associationEnd  
	 */
	Spieler testSpielerObenLinks = new Spieler(1,"ObenLinks", bildschirm);
	/**
	 * @uml.property  name="testSpielerUntenRechts"
	 * @uml.associationEnd  
	 */
	Spieler testSpielerUntenRechts = new Spieler(0,"UntenRechts",bildschirm);
	
	
	public void testInitSteinPos(){
		Stein[] testSteinArrayInitSteinPos;
		int zugehoerigkeitEins = 1,
		    zugehoerigkeitNull = 0;
		    
		    int[] intArraySpielerOben = {1,1,2,2,3,1,3,2,2,4,2,1,5,1,3,6,1,1},
		         intArraySpielerUnten = {1,5,4,2,3,5,3,4,4,4,4,5,5,5,3,6,5,5};
		    
		
		    testSteinArrayInitSteinPos = testSpielerObenLinks.initSteinPos(testSpielerObenLinks.gibZugehoerigkeit());
		    for(int i = 0, k = 0; i < 6; i++){
			assertEquals(zugehoerigkeitEins,testSteinArrayInitSteinPos[i].gibZugehoerigkeit());
			assertEquals(intArraySpielerOben[k],testSteinArrayInitSteinPos[i].gibNummer());
			assertEquals(intArraySpielerOben[k+1],testSteinArrayInitSteinPos[i].gibZeile());
			assertEquals(intArraySpielerOben[k+2],testSteinArrayInitSteinPos[i].gibSpalte());
			k+=3;
		    }
		    
			testSteinArrayInitSteinPos = testSpielerUntenRechts.initSteinPos(testSpielerUntenRechts.gibZugehoerigkeit());
		    for(int i = 0, k = 0; i < 6; i++){
			assertEquals(zugehoerigkeitNull,testSteinArrayInitSteinPos[i].gibZugehoerigkeit());
			assertEquals(intArraySpielerUnten[k],testSteinArrayInitSteinPos[i].gibNummer());
			assertEquals(intArraySpielerUnten[k+1],testSteinArrayInitSteinPos[i].gibZeile());
			assertEquals(intArraySpielerUnten[k+2],testSteinArrayInitSteinPos[i].gibSpalte());
			k+=3;
		    }
	}/*113 231 322 421 512 611
111312311322142115121611
*/
	public void spieler2InitSteinPos(){
		Stein[] testSteinArrayInitSteinPosSpieler2;
		int[] testEingabeArrayEins = {1,1,1,3,1,2,3,1,1,3,2,2,1,4,2,1,1,5,1,2,1,6,1,1};
		int[] testEingabeArrayNull = {0,1,1,3,0,2,3,1,0,3,2,2,0,4,2,1,0,5,1,2,0,6,1,1};
		
		testSteinArrayInitSteinPosSpieler2 = testSpielerObenLinks.spieler2InitSteinPos(testEingabeArrayEins);
		for(int i = 0, k = 0; i < 6; i++){
			assertEquals(testEingabeArrayEins[k],testSteinArrayInitSteinPosSpieler2[i].gibZugehoerigkeit());
			assertEquals(testEingabeArrayEins[k+1],testSteinArrayInitSteinPosSpieler2[i].gibNummer());
			assertEquals(testEingabeArrayEins[k+2],testSteinArrayInitSteinPosSpieler2[i].gibZeile());
			assertEquals(testEingabeArrayEins[k+3],testSteinArrayInitSteinPosSpieler2[i].gibSpalte());
			k+=4;
		    }
		testSteinArrayInitSteinPosSpieler2 = testSpielerUntenRechts.spieler2InitSteinPos(testEingabeArrayNull);
		for(int i = 0, k = 0; i < 6; i++){
			assertEquals(testEingabeArrayNull[k],testSteinArrayInitSteinPosSpieler2[i].gibZugehoerigkeit());
			assertEquals(testEingabeArrayNull[k+1],testSteinArrayInitSteinPosSpieler2[i].gibNummer());
			assertEquals(testEingabeArrayNull[k+2],testSteinArrayInitSteinPosSpieler2[i].gibZeile());
			assertEquals(testEingabeArrayNull[k+3],testSteinArrayInitSteinPosSpieler2[i].gibSpalte());
			k+=4;
		    }
	}
	public void testGibInitialString(){
		Stein[] TestSteinArrayInitialString;
		//String SpielerObenLinks = "112 231 322 421 513 611";
		//String SpielerUntenRechts = "154 235 344 445 553 655";
		
		TestSteinArrayInitialString = testSpielerObenLinks.initSteinPos(testSpielerObenLinks.gibZugehoerigkeit());	
		assertEquals("112 231 322 421 513 611",testSpielerObenLinks.gibInitialString(TestSteinArrayInitialString));
		
		TestSteinArrayInitialString = testSpielerUntenRechts.initSteinPos(testSpielerUntenRechts.gibZugehoerigkeit());		
		assertEquals("154 235 344 445 553 655",testSpielerUntenRechts.gibInitialString(TestSteinArrayInitialString));
	}

	public void testGibName(){
		assertEquals("ObenLinks",testSpielerObenLinks.gibName());
		assertEquals("UntenRechts",testSpielerUntenRechts.gibName());
	}
	public void testGibZugehoerigkeit(){
		assertEquals(1,testSpielerObenLinks.gibZugehoerigkeit());
		assertEquals(0,testSpielerUntenRechts.gibZugehoerigkeit());
	}
}
