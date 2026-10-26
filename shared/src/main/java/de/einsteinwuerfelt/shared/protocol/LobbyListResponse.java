package de.einsteinwuerfelt.shared.protocol;

import java.util.List;

/**
 * Antwort mit der Liste der verfügbaren Lobbys.
 */
public class LobbyListResponse {

    private final List<LobbyInfo> lobbys;

    public static class LobbyInfo {
        private final int lobbyId;
        private final String name;
        private final int spielerAnzahl;
        private final int maxSpieler;
        private final boolean offen;

        public LobbyInfo(int lobbyId, String name, int spielerAnzahl, int maxSpieler, boolean offen) {
            this.lobbyId = lobbyId;
            this.name = name;
            this.spielerAnzahl = spielerAnzahl;
            this.maxSpieler = maxSpieler;
            this.offen = offen;
        }

        public int gibLobbyId() { return lobbyId; }
        public String gibName() { return name; }
        public int gibSpielerAnzahl() { return spielerAnzahl; }
        public int gibMaxSpieler() { return maxSpieler; }
        public boolean gibOffen() { return offen; }

        @Override
        public String toString() {
            return "LobbyInfo{" + name + ", " + spielerAnzahl + "/" + maxSpieler + "}";
        }
    }

    public LobbyListResponse(List<LobbyInfo> lobbys) {
        this.lobbys = lobbys;
    }

    public List<LobbyInfo> gibLobbys() { return lobbys; }

    @Override
    public String toString() {
        return "LobbyListResponse{" + lobbys.size() + " Lobbys}";
    }
}
