package de.einsteinwuerfelt.shared.ai;

import de.einsteinwuerfelt.shared.rules.Spiellogik;

/**
 * Standard-KI-Implementierung mit Strategie-Array und Verteidigungszone.
 * 
 * Strategie: Wählt den Stein, der am nächsten an die Zielseite kommt (höchster Wert im Strategie-Array).
 * Priorität: Gegner schlagen > Strategie-basiert bewegen.
 */
public class DefaultKI implements KIStrategie {

    private final String name;
    private final int zugehoerigkeit; // 0 oder 1
    private final Spiellogik logik = new Spiellogik();

    // Strategie-Array: Wert = "Nähe zur Zielseite" (höher = besser)
    // Für Spieler 2 (unten-rechts, Ziel oben-links):
    private static final int[][] STRATEGIE_UNTEN_RECHTS = {
        {100, 90, 65, 60, 0},   // Zeile 5 (nächste an Zeile 1)
        {90, 85, 75, 55, 50},   // Zeile 4
        {70, 75, 70, 50, 47},   // Zeile 3
        {75, 60, 55, 45, 40},   // Zeile 2
        {0, 50, 47, 40, 0}      // Zeile 1 (nächste an Zeile 5)
    };

    // Für Spieler 1 (oben-links, Ziel unten-rechts):
    private static final int[][] STRATEGIE_OBEN_LINKS = {
        {0, 25, 49, 0, 0},      // Zeile 1 (nächste an Zeile 5)
        {25, 45, 40, 70, 0},    // Zeile 2
        {30, 40, 65, 75, 80},   // Zeile 3
        {50, 55, 75, 85, 90},   // Zeile 4
        {0, 60, 65, 90, 100}    // Zeile 5 (nächste an Zeile 1)
    };

    // Verteidigungszone: Positionen wo Gegner geschlagen werden können
    private static final int[][] VERTeidigungsZone_UNTEN = {
        {3, 3}, {3, 4}, {3, 5}, {4, 3}, {4, 4}, {4, 5}, {5, 3}, {5, 4}
    };

    private static final int[][] VERTeidigungsZone_OBEN = {
        {1, 2}, {1, 3}, {2, 1}, {2, 2}, {2, 3}, {3, 1}, {3, 2}, {3, 3}
    };

    public DefaultKI(String name, int zugehoerigkeit) {
        this.name = name;
        this.zugehoerigkeit = zugehoerigkeit;
    }

    @Override
    public String gibName() {
        return name;
    }

    @Override
    public int setzeZug(int[] moeglicheZuege, int[] steinPosArray, int wuerfelzahl) {
        // Ermittle Anzahl möglicher Züge (max 6 × 4 Attribute = 24)
        int positionZuege = 0;
        for (int i = 0; i < 24; i += 4) {
            if (moeglicheZuege[i + 2] != 0 && moeglicheZuege[i + 3] != 0) {
                positionZuege++;
            }
        }

        // Prüfe ob Gegner in Verteidigungszone ist und geschlagen werden kann
        boolean kannGegnerGeschlagen = pruefeGegnerSchlagbar(moeglicheZuege, steinPosArray);

        if (kannGegnerGeschlagen) {
            return findeBestenSchlagzug(moeglicheZuege, steinPosArray);
        }

        // Kein Gegner schlagbar → wähle nach Strategie-Array
        return findeBestenStrategiezug(moeglicheZuege);
    }

    private boolean pruefeGegnerSchlagbar(int[] moeglicheZuege, int[] steinPosArray) {
        int anzahlGegner = 0;
        int[][] zone = (zugehoerigkeit == 0) ? VERTeidigungsZone_UNTEN : VERTeidigungsZone_OBEN;

        // Suche Gegner-Steine in der Zone
        for (int i = 0; i < steinPosArray.length; i += 4) {
            int gegnerZugehoerigkeit = 1 - zugehoerigkeit;
            if (steinPosArray[i] == gegnerZugehoerigkeit &&
                steinPosArray[i + 2] != 0 && steinPosArray[i + 3] != 0) {
                for (int j = 0; j < zone.length; j++) {
                    if (steinPosArray[i + 2] == zone[j][0] && steinPosArray[i + 3] == zone[j][1]) {
                        anzahlGegner++;
                        break;
                    }
                }
            }
        }

        // Prüfe ob ein möglicher Zug auf eine Gegner-Position zeigt
        if (anzahlGegner > 0) {
            for (int i = 1; i <= positionZuege * 4; i += 4) {
                if (moeglicheZuege[i + 2] != 0 && moeglicheZuege[i + 3] != 0) {
                    for (int j = 0; j < steinPosArray.length; j += 4) {
                        int gegnerZugehoerigkeit = 1 - zugehoerigkeit;
                        if (steinPosArray[j] == gegnerZugehoerigkeit &&
                            steinPosArray[j + 2] != 0 && steinPosArray[j + 3] != 0) {
                            if (moeglicheZuege[i + 2] == steinPosArray[j + 2] &&
                                moeglicheZuege[i + 3] == steinPosArray[j + 3]) {
                                return true; // Gegner kann geschlagen werden
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private int findeBestenSchlagzug(int[] moeglicheZuege, int[] steinPosArray) {
        int bestePosition = 1; // Index im moeglicheZuege-Array (1-basiert)
        int gegnerZugehoerigkeit = 1 - zugehoerigkeit;

        for (int i = 1; i <= positionZuege * 4; i += 4) {
            if (moeglicheZuege[i + 2] != 0 && moeglicheZuege[i + 3] != 0) {
                // Prüfe ob dieser Zug einen Gegner schlägt
                for (int j = 0; j < steinPosArray.length; j += 4) {
                    if (steinPosArray[j] == gegnerZugehoerigkeit &&
                        steinPosArray[j + 2] != 0 && steinPosArray[j + 3] != 0) {
                        if (moeglicheZuege[i + 2] == steinPosArray[j + 2] &&
                            moeglicheZuege[i + 3] == steinPosArray[j + 3]) {
                            bestePosition = i;
                            break;
                        }
                    }
                }
            }
        }

        return (bestePosition / 4) + 1; // Umrechnung auf 1-basierten Index
    }

    private int findeBestenStrategiezug(int[] moeglicheZuege) {
        int[][] strategie = (zugehoerigkeit == 0) ? STRATEGIE_UNTEN_RECHTS : STRATEGIE_OBEN_LINKS;
        int bestePosition = 1;
        int besterWert = -1;

        for (int i = 1; i <= positionZuege * 4; i += 4) {
            if (moeglicheZuege[i + 2] != 0 && moeglicheZuege[i + 3] != 0) {
                int zeile = moeglicheZuege[i + 2];
                int spalte = moeglicheZuege[i + 3];
                // Strategie-Array ist 1-basiert (Positionen 1-5 → Index 0-4)
                int wert = strategie[zeile - 1][spalte - 1];
                if (wert > besterWert) {
                    besterWert = wert;
                    bestePosition = i;
                }
            }
        }

        return (bestePosition / 4) + 1; // Umrechnung auf 1-basierten Index
    }

    private int positionZuege = 0;
}
