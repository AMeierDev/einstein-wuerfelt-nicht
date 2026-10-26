package de.einsteinwuerfelt.shared;

import de.einsteinwuerfelt.shared.model.Stein;

/**
 * Interface für Spieler/KI, die Spielzüge ausführen können.
 * Wird vom Shared-Modul definiert und von Client/Server implementiert.
 */
public interface SpielerInterface {

    /** Initialisiert die Startposition der Steine eines Spielers auf dem Brett. */
    Stein[] initSteinPos(int zugehoerigkeit);

    /** Gibt ein int-Array mit allen Steineigenschaften beider Spieler zurück (48 Werte). */
    int[] gibSteinEigenschaftenFeld(Stein initSpieler1[], Stein initSpieler2[]);

    /** Liefert die Startpositionen als String für die Netzwerkübertragung. */
    String gibInitialString(Stein initSpieler1[]);

    /** Konvertiert ein int-Array (von Netzwerk) zurück in ein Stein-Array. */
    Stein[] spieler2InitSteinPos(int[] gegnerInit);

    /** Gibt den Spielernamen zurück. */
    String gibName();

    /** Gibt die Spieler-Zugehörigkeit (0 oder 1) zurück. */
    int gibZugehoerigkeit();

    /** Wählt einen Zug aus den möglichen Zügen basierend auf Strategie/Würfelzahl. */
    int setzeZug(int[] moeglicheZuege, int[] steinPosArray, int wuerfelzahl);
}
