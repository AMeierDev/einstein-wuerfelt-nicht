package Interfaces;

import spiel.Stein;

public interface SpielerInterface {

	public abstract Stein[] initSteinPos(int zugehoerigkeit);
	public abstract int[] gibSteinEigenschaftenFeld(Stein initSteinPosSpieler1[], Stein initSteinPosSpieler2[]);
	public abstract String gibInitialString(Stein initSteinPosSpieler1[]);
	public abstract Stein[] spieler2InitSteinPos(int[] gegnerInit);
	public abstract String gibName();
	public abstract int gibZugehoerigkeit();
	public abstract int setzeZug(int[] moeglicheZuege, int []steinPosArray, int wuerfelzahl);
}
