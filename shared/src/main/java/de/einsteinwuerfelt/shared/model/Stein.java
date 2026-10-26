package de.einsteinwuerfelt.shared.model;

/**
 * Repräsentiert einen Spielstein auf dem Brett.
 * 
 * Das Spielfeld ist ein 5x5-Gitter. Spieler 1 (O) startet oben-links und bewegt sich
 * nach unten-rechts (Ziel: Feld 5,5). Spieler 2 (X) startet unten-rechts und bewegt
 * sich nach oben-links (Ziel: Feld 1,1).
 */
public class Stein {

    private final int zugehoerigkeit; // 0 = Spieler 2 (unten/rechts), 1 = Spieler 1 (oben/links)
    private final int nummer;         // 1-6
    private int zeile;                // Position auf dem Brett (1-5)
    private int spalte;               // Position auf dem Brett (1-5)

    public Stein(int zugehoerigkeit, int nummer, int zeile, int spalte) {
        this.zugehoerigkeit = zugehoerigkeit;
        this.nummer = nummer;
        this.zeile = zeile;
        this.spalte = spalte;
    }

    public int gibZugehoerigkeit() { return zugehoerigkeit; }
    public int gibNummer() { return nummer; }
    public int gibZeile() { return zeile; }
    public int gibSpalte() { return spalte; }

    /** Setzt die Position des Steins auf dem Brett. */
    public void setzePosition(int zeile, int spalte) {
        this.zeile = zeile;
        this.spalte = spalte;
    }

    /** Gibt true zurück, wenn der Stein noch auf dem Brett ist (Position != 0). */
    public boolean istAmBrett() {
        return zeile != 0 && spalte != 0;
    }

    @Override
    public String toString() {
        return "Stein[" + zugehoerigkeit + "," + nummer + "]@" + zeile + "," + spalte;
    }
}
