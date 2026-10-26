package de.einsteinwuerfelt.shared.protocol;

/**
 * Anfrage zum Spielstart oder Spielstatus.
 */
public class SpielRequest {

    private final int sessionId;
    private final int lobbyId;
    private final String action; // "start", "join", "leave"

    public SpielRequest(int sessionId, int lobbyId, String action) {
        this.sessionId = sessionId;
        this.lobbyId = lobbyId;
        this.action = action;
    }

    public int gibSessionId() { return sessionId; }
    public int gibLobbyId() { return lobbyId; }
    public String gibAction() { return action; }

    @Override
    public String toString() {
        return "SpielRequest{session=" + sessionId + ", lobby=" + lobbyId + ", action=" + action + "}";
    }
}
