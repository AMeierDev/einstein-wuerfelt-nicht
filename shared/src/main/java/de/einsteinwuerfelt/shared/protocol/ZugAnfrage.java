package de.einsteinwuerfelt.shared.protocol;

/**
 * Zug-Anfrage vom Client an den Server.
 * Enthält die gewürfelte Zahl und die Spieler-Auswahl (1-6).
 */
public class ZugAnfrage {

    private final int sessionId;
    private final int wuerfelzahl; // 1-6
    private final int auswahl;     // 1-6 (Stein-Auswahl des Spielers)
    private final int zugIndex;    // Welcher der möglichen Züge (1-basiert, max 6)

    public ZugAnfrage(int sessionId, int wuerfelzahl, int auswahl, int zugIndex) {
        this.sessionId = sessionId;
        this.wuerfelzahl = wuerfelzahl;
        this.auswahl = auswahl;
        this.zugIndex = zugIndex;
    }

    public int gibSessionId() { return sessionId; }
    public int gibWuerfelzahl() { return wuerfelzahl; }
    public int gibAuswahl() { return auswahl; }
    public int gibZugIndex() { return zugIndex; }

    @Override
    public String toString() {
        return "ZugAnfrage{wurf=" + wuerfelzahl + ", auswahl=" + auswahl + ", zug=" + zugIndex + "}";
    }
}
