package de.einsteinwuerfelt.shared.protocol;

/**
 * Basisklasse für alle Protokoll-Nachrichten.
 * Dient als JSON-Struktur-Ersatz für das alte TCP-Protokoll (server/Protocol.java).
 */
public class GameMessage {

    public enum MessageType {
        LOGIN_REQUEST,
        LOGIN_RESPONSE,
        LOBBY_LIST_REQUEST,
        LOBBY_LIST_RESPONSE,
        SPIEL_START,
        SPIEL_UPDATE,
        ZUG_ANFRAGE,
        ZUG_ANTWORT,
        SIEG_NACHRICHT,
        ERROR
    }

    private final MessageType typ;
    private final String nachricht; // JSON-String der Nutzlast

    public GameMessage(MessageType typ) {
        this.typ = typ;
        this.nachricht = null;
    }

    public GameMessage(MessageType typ, String nachricht) {
        this.typ = typ;
        this.nachricht = nachricht;
    }

    public MessageType gibTyp() { return typ; }
    public String gibNachricht() { return nachricht; }

    @Override
    public String toString() {
        return "GameMessage{" + typ + ", payload=" + (nachricht != null ? nachricht.substring(0, Math.min(nachricht.length(), 50)) : "null") + "...}";
    }
}
