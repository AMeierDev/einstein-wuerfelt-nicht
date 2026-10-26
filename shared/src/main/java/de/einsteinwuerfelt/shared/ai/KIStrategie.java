package de.einsteinwuerfelt.shared.ai;

/**
 * Interface für KI-Strategien im Spiel.
 */
public interface KIStrategie {

    /** Wählt den besten Zug basierend auf Strategie und Würfelzahl aus. */
    int setzeZug(int[] moeglicheZuege, int[] steinPosArray, int wuerfelzahl);

    /** Gibt einen Namen/Label für diese KI-Strategie zurück. */
    String gibName();
}
