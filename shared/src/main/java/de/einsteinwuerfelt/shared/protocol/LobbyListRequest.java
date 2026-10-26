package de.einsteinwuerfelt.shared.protocol;

/**
 * Anfrage zur Lobby-Übersicht.
 */
public class LobbyListRequest {

    private final int sessionId; // Authentifizierte Session-ID

    public LobbyListRequest(int sessionId) {
        this.sessionId = sessionId;
    }

    public int gibSessionId() { return sessionId; }

    @Override
    public String toString() {
        return "LobbyListRequest{session=" + sessionId + "}";
    }
}
