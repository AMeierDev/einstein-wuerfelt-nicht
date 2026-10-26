package de.einsteinwuerfelt.shared.protocol;

/**
 * Zug-Antwort vom Server an den Client.
 * Enthält das Ergebnis des Zugs (erfolgreich/fehlgeschlagen) und die neue Brett-Position.
 */
public class ZugAntwort {

    private final boolean erfolgreich;
    private final String nachricht; // Fehlermeldung oder "Zug ausgeführt"
    private final int[] steinPosArray; // Neues 48-Werte-Array (2 Spieler × 6 Steine × 4 Attribute)
    private final int siegerZugehoerigkeit; // -1 = kein Sieger, 0 oder 1

    public ZugAntwort(boolean erfolgreich, String nachricht) {
        this.erfolgreich = erfolgreich;
        this.nachricht = nachricht;
        this.steinPosArray = new int[48];
        this.siegerZugehoerigkeit = -1;
    }

    public ZugAntwort(int[] steinPosArray, int siegerZugehoerigkeit) {
        this.erfolgreich = true;
        this.nachricht = "Zug ausgeführt";
        this.steinPosArray = steinPosArray.clone();
        this.siegerZugehoerigkeit = siegerZugehoerigkeit;
    }

    public boolean gibErfolgreich() { return erfolgreich; }
    public String gibNachricht() { return nachricht; }
    public int[] gibSteinPosArray() { return steinPosArray.clone(); }
    public int gibSiegerZugehoerigkeit() { return siegerZugehoerigkeit; }

    @Override
    public String toString() {
        if (!erfolgreich) return "ZugAntwort{FEHLER: " + nachricht + "}";
        return "ZugAntwort{OK, sieger=" + (siegerZugehoerigkeit == -1 ? "kein" : siegerZugehoerigkeit) + "}";
    }
}
