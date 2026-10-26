package de.einsteinwuerfelt.shared.protocol;

/**
 * Antwort mit Spielstatus (Startpositionen, Spieler-Info).
 */
public class SpielResponse {

    private final boolean erfolgreich;
    private final String nachricht;
    private final int spieler1Zugehoerigkeit; // 0 oder 1
    private final int spieler2Zugehoerigkeit; // 0 oder 1
    private final String spieler1Name;
    private final String spieler2Name;

    public SpielResponse(boolean erfolgreich, String nachricht) {
        this.erfolgreich = erfolgreich;
        this.nachricht = nachricht;
        this.spieler1Zugehoerigkeit = -1;
        this.spieler2Zugehoerigkeit = -1;
        this.spieler1Name = "";
        this.spieler2Name = "";
    }

    public SpielResponse(boolean erfolgreich, int spieler1Zugehoerigkeit, int spieler2Zugehoerigkeit,
                         String spieler1Name, String spieler2Name) {
        this.erfolgreich = erfolgreich;
        this.nachricht = "Spiel bereit";
        this.spieler1Zugehoerigkeit = spieler1Zugehoerigkeit;
        this.spieler2Zugehoerigkeit = spieler2Zugehoerigkeit;
        this.spieler1Name = spieler1Name;
        this.spieler2Name = spieler2Name;
    }

    public boolean gibErfolgreich() { return erfolgreich; }
    public String gibNachricht() { return nachricht; }
    public int gibSpieler1Zugehoerigkeit() { return spieler1Zugehoerigkeit; }
    public int gibSpieler2Zugehoerigkeit() { return spieler2Zugehoerigkeit; }
    public String gibSpieler1Name() { return spieler1Name; }
    public String gibSpieler2Name() { return spieler2Name; }

    @Override
    public String toString() {
        if (!erfolgreich) return "SpielResponse{FEHLER: " + nachricht + "}";
        return "SpielResponse{" + spieler1Name + "(O) vs " + spieler2Name + "(X)}";
    }
}
