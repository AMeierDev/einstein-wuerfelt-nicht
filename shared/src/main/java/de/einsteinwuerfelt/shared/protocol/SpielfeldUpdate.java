package de.einsteinwuerfelt.shared.protocol;

/**
 * Brett-Update-Nachricht für alle Clients.
 * Enthält den aktuellen Zustand des Spiels (Steinpositionen, Würfelergebnis).
 */
public class SpielfeldUpdate {

    private final int[] steinPosArray; // 48-Werte-Array: [zugehoerigkeit, nummer, zeile, spalte] × 12 Steine
    private final int wuerfelErgebnis; // Aktueller Wurf (0 = kein Wurf)
    private final int aktiverSpieler;  // Wer ist am Zug? (0 oder 1)
    private final boolean spielBeendet;

    public SpielfeldUpdate(int[] steinPosArray, int wuerfelErgebnis, int aktiverSpieler) {
        this.steinPosArray = steinPosArray.clone();
        this.wuerfelErgebnis = wuerfelErgebnis;
        this.aktiverSpieler = aktiverSpieler;
        this.spielBeendet = false;
    }

    public SpielfeldUpdate(int[] steinPosArray, int siegerZugehoerigkeit) {
        this.steinPosArray = steinPosArray.clone();
        this.wuerfelErgebnis = 0;
        this.aktiverSpieler = -1;
        this.spielBeendet = true;
    }

    public int[] gibSteinPosArray() { return steinPosArray.clone(); }
    public int gibWuerfelErgebnis() { return wuerfelErgebnis; }
    public int gibAktiverSpieler() { return aktiverSpieler; }
    public boolean gibSpielBeendet() { return spielBeendet; }

    @Override
    public String toString() {
        if (spielBeendet) return "SpielfeldUpdate{SPIEL ENDE, Sieger=" + (1 - aktiverSpieler) + "}";
        return "SpielfeldUpdate{wurf=" + wuerfelErgebnis + ", spieler=" + aktiverSpieler + "}";
    }
}
