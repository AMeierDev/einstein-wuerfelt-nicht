package de.einsteinwuerfelt.shared.rules;

import de.einsteinwuerfelt.shared.model.Stein;

/**
 * Reine Spielregeln ohne UI-Abhängigkeiten.
 * 
 * Spielfeld: 5x5 Gitter
 * Spieler 1 (O): Start oben-links, Ziel unten-rechts (5,5)
 * Spieler 2 (X): Start unten-rechts, Ziel oben-links (1,1)
 * Jeder Spieler hat 6 Steine (Nummer 1-6).
 * Würfeln: 1-6 → gewählter Stein darf sich bewegen.
 * Bewegung: bis zu 3 Richtungen möglich (diagonal + orthogonal), begrenzt durch Brettgrenzen.
 * Schlagen: Landet ein Stein auf einem Gegnerfeld, wird dieser entfernt.
 * Sieg: Erreichen der Zielseite ODER alle gegnerischen Steine sind geschlagen.
 */
public class Spiellogik {

    private int siegerZugehoerigkeit = -1; // -1 = kein Sieger, 0 oder 1

    /** Gibt das Spielfeld als int-Array zurück (Position des Gegners). */
    public int[] gibIntFeld(String gegnerSteinPos, int spielerZugehoerigkeit) {
        if (gegnerSteinPos == null || gegnerSteinPos.trim().isEmpty()) {
            return new int[]{0, 0};
        }
        String trimmed = gegnerSteinPos.trim();
        int len = trimmed.length();

        // Einzelne Züge: "123" → [zugehoerigkeit, steinNummer, zeile, spalte]
        if (len == 3) {
            return konvertiereZugStringZuInt(trimmed, spielerZugehoerigkeit);
        }
        // Startpositionen: "113 231 322 421 512 611" (23 Zeichen)
        if (len == 23) {
            return konvertiereStringZuInt(trimmed, spielerZugehoerigkeit);
        }
        // Ungültige Länge
        return new int[]{0, 0};
    }

    private int[] konvertiereStringZuInt(String gegnerSteinPos, int spielerZugehoerigkeit) {
        String[] bloecke = gegnerSteinPos.trim().split("\\s+");
        int feldgroesse = bloecke.length; // 6
        int[] ergebnis = new int[feldgroesse + 1];

        for (int k = 0, m = 0; k < feldgroesse; k++) {
            if (k % 4 == 0) {
                ergebnis[k] = spielerZugehoerigkeit; // Zugehörigkeit an Position 0,4,8,...
            } else {
                ergebnis[k] = Integer.parseInt(bloecke[m]);
                m++;
            }
        }
        return ergebnis;
    }

    private int[] konvertiereZugStringZuInt(String zugString, int spielerZugehoerigkeit) {
        String[] ziffern = zugString.split("");
        int feldgroesse = ziffern.length; // 3
        int[] ergebnis = new int[feldgroesse + 1];

        for (int i = 0; i <= feldgroesse; i++) {
            if (i == 0) {
                ergebnis[i] = spielerZugehoerigkeit;
            } else {
                ergebnis[i] = Integer.parseInt(ziffern[i - 1]);
            }
        }
        return ergebnis;
    }

    /** Bewegt einen Stein und löscht ggf. gegnerische Steine an der Zielposition. */
    public int[] bewegeStein(int steinArray[], int zug[]) {
        // Zug: [zugehoerigkeit, steinNummer, zeile, spalte]
        // Suche den Stein im Array (jeder Stein hat 4 Attribute)
        int steinPos = -1;
        for (int i = 0; i < steinArray.length; i += 4) {
            if (steinArray[i] == zug[0] && steinArray[i + 1] == zug[1]) {
                steinPos = i;
                break;
            }
        }

        // Lösche gegnerische Steine an der Zielposition
        steinArray = loescheStein(steinArray, zug);

        // Bewege den Stein (Position im Array ist steinPos+1 für nummer, +2 für zeile, +3 für spalte)
        if (steinPos >= 0) {
            steinArray[steinPos + 2] = zug[2]; // Zeile ändern
            steinArray[steinPos + 3] = zug[3]; // Spalte ändern
        }

        return steinArray;
    }

    /** Prüft, welche Steine eines Spielers bewegt werden dürfen (vorher/nachher). */
    public int[] pruefeStein(int steinPos, int steinArray[]) {
        int zugehoerigkeit = steinArray[steinPos - 1]; // Position-1 ist Zugehörigkeit
        int wuerfelzahl = steinArray[steinPos];         // Position ist Steinnummer

        int mglSteinVorher = wuerfelzahl;
        int mglSteinNachher = wuerfelzahl;
        int steinVorher = -1;
        int steinNachher = -1;

        // Prüfe ob Stein noch existiert (Position != 0)
        if (steinArray[steinPos + 1] != 0 && steinArray[steinPos + 2] != 0) {
            return new int[]{0, 0}; // Stein existiert nicht mehr
        }

        // Suche nächst höheren Stein
        do {
            mglSteinNachher++;
            if (mglSteinNachher > 6) break;
            for (int i = 0; i < steinArray.length; i += 4) {
                if (steinArray[i] == zugehoerigkeit && steinArray[i + 1] == mglSteinNachher) {
                    if (steinArray[i + 2] != 0 || steinArray[i + 3] != 0) {
                        steinNachher = i; // Position im Array
                        break;
                    }
                }
            }
        } while (steinNachher == -1 && mglSteinNachher <= 6);

        // Suche nächst niedrigeren Stein
        do {
            mglSteinVorher--;
            if (mglSteinVorher < 1) break;
            for (int i = 0; i < steinArray.length; i += 4) {
                if (steinArray[i] == zugehoerigkeit && steinArray[i + 1] == mglSteinVorher) {
                    if (steinArray[i + 2] != 0 || steinArray[i + 3] != 0) {
                        steinVorher = i; // Position im Array
                        break;
                    }
                }
            }
        } while (steinVorher == -1 && mglSteinVorher >= 1);

        return new int[]{steinVorher, steinNachher};
    }

    /** Ermittelt alle möglichen Züge für einen gewürfelten Stein. */
    public int[] gibmoeglicheSpielzuege(int zugehoerigkeit, int wuerfelzahl, int steinArray[]) {
        // Suche den Stein im Array
        int steinPos = -1;
        for (int i = 0; i < steinArray.length; i += 4) {
            if (steinArray[i] == zugehoerigkeit && steinArray[i + 1] == wuerfelzahl) {
                steinPos = i;
                break;
            }
        }

        // Prüfe ob Stein existiert und welche anderen Steine bewegbar sind
        int[] mglSteine = pruefeStein(steinPos + 1, steinArray); // +1 weil steinPos ist Index im Array

        int[] moeglicheZuege = new int[24]; // max 6 Züge × 4 Attribute

        if (mglSteine[0] == 0 && mglSteine[1] == 0) {
            // Nur der gewürfelte Stein kann bewegt werden
            moeglicheZuege = setzemoeglicheZuege(zugehoerigkeit, steinArray, steinPos + 1, 0, 0, 0, 0, -1);
        } else if (mglSteine[0] != 0 && mglSteine[1] == 0) {
            // Nur vorheriger Stein kann bewegt werden
            moeglicheZuege = setzemoeglicheZuege(zugehoerigkeit, steinArray, mglSteine[0], 0, 0, 0, 0, -1);
        } else if (mglSteine[0] == 0 && mglSteine[1] != 0) {
            // Nur nachfolgender Stein kann bewegt werden
            moeglicheZuege = setzemoeglicheZuege(zugehoerigkeit, steinArray, mglSteine[1], 0, 0, 0, 0, -1);
        } else {
            // Beide Steine können bewegt werden
            int zeile1 = steinArray[mglSteine[1] + 2];
            int spalte1 = steinArray[mglSteine[1] + 3];
            moeglicheZuege = setzemoeglicheZuege(zugehoerigkeit, steinArray, mglSteine[0], 0, 0, zeile1, spalte1, mglSteine[1]);
        }

        return moeglicheZuege;
    }

    /** Setzt alle möglichen Züge für einen Stein (bis zu 3 Richtungen). */
    private int[] setzemoeglicheZuege(int zugehoerigkeit, int steinArray[], int steinPos,
                                       int zeile, int spalte, int zeile1, int spalte1, int steinPos1) {
        int[] moeglicheZuege = new int[24]; // max 6 Züge × 4 Attribute
        int i = 0;

        if (steinPos == -1 || steinArray[steinPos + 2] == 0 && steinArray[steinPos + 3] == 0) {
            return moeglicheZuege; // Stein existiert nicht
        }

        int currentZeile = zeile != 0 ? zeile : steinArray[steinPos + 2];
        int currentSpalte = spalte != 0 ? spalte : steinArray[steinPos + 3];

        if (zugehoerigkeit == 1) { // Spieler 1: bewegt sich nach unten-rechts (→5,5)
            // Richtung: Zeile+1, Spalte+1 (diagonal), Zeile+1 (unten), Spalte+1 (rechts)
            if (currentZeile != 5 && currentSpalte != 5) {
                moeglicheZuege[0] = zugehoerigkeit; moeglicheZuege[1] = steinArray[steinPos];
                moeglicheZuege[2] = currentZeile + 1; moeglicheZuege[3] = currentSpalte; // unten
                i = 4;
                moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos];
                moeglicheZuege[i++] = currentZeile; moeglicheZuege[i++] = currentSpalte + 1; // rechts
                i = 8;
                moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos];
                moeglicheZuege[i++] = currentZeile + 1; moeglicheZuege[i++] = currentSpalte + 1; // diagonal
            } else if (currentZeile == 5 && currentSpalte != 5) {
                moeglicheZuege[0] = zugehoerigkeit; moeglicheZuege[1] = steinArray[steinPos];
                moeglicheZuege[2] = currentZeile; moeglicheZuege[3] = currentSpalte + 1; // nur rechts
            } else if (currentZeile != 5 && currentSpalte == 5) {
                moeglicheZuege[0] = zugehoerigkeit; moeglicheZuege[1] = steinArray[steinPos];
                moeglicheZuege[2] = currentZeile + 1; moeglicheZuege[3] = currentSpalte; // nur unten
            }
        } else { // Spieler 2: bewegt sich nach oben-links (→1,1)
            // Richtung: Zeile-1, Spalte-1 (diagonal), Zeile-1 (oben), Spalte-1 (links)
            if (currentZeile != 1 && currentSpalte != 1) {
                moeglicheZuege[0] = zugehoerigkeit; moeglicheZuege[1] = steinArray[steinPos];
                moeglicheZuege[2] = currentZeile - 1; moeglicheZuege[3] = currentSpalte; // oben
                i = 4;
                moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos];
                moeglicheZuege[i++] = currentZeile; moeglicheZuege[i++] = currentSpalte - 1; // links
                i = 8;
                moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos];
                moeglicheZuege[i++] = currentZeile - 1; moeglicheZuege[i++] = currentSpalte - 1; // diagonal
            } else if (currentZeile == 1 && currentSpalte != 1) {
                moeglicheZuege[0] = zugehoerigkeit; moeglicheZuege[1] = steinArray[steinPos];
                moeglicheZuege[2] = currentZeile; moeglicheZuege[3] = currentSpalte - 1; // nur links
            } else if (currentZeile != 1 && currentSpalte == 1) {
                moeglicheZuege[0] = zugehoerigkeit; moeglicheZuege[1] = steinArray[steinPos];
                moeglicheZuege[2] = currentZeile - 1; moeglicheZuege[3] = currentSpalte; // nur oben
            }
        }

        // Fall 4: zweiter Stein (wenn beide bewegbar)
        if (steinPos1 != -1 && zeile1 != 0 && spalte1 != 0) {
            int z2 = steinArray[steinPos1 + 2];
            int s2 = steinArray[steinPos1 + 3];

            if (zugehoerigkeit == 1) { // Spieler 1 bewegt sich nach unten-rechts
                if (z2 != 5 && s2 != 5) {
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2 + 1; moeglicheZuege[i++] = s2; // unten
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2; moeglicheZuege[i++] = s2 + 1; // rechts
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2 + 1; moeglicheZuege[i++] = s2 + 1; // diagonal
                } else if (z2 == 5 && s2 != 5) {
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2; moeglicheZuege[i++] = s2 + 1; // nur rechts
                } else if (z2 != 5 && s2 == 1) {
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2 + 1; moeglicheZuege[i++] = s2; // nur unten
                }
            } else { // Spieler 2 bewegt sich nach oben-links
                if (z2 != 1 && s2 != 1) {
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2 - 1; moeglicheZuege[i++] = s2; // oben
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2; moeglicheZuege[i++] = s2 - 1; // links
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2 - 1; moeglicheZuege[i++] = s2 - 1; // diagonal
                } else if (z2 == 1 && s2 != 1) {
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2; moeglicheZuege[i++] = s2 - 1; // nur links
                } else if (z2 != 1 && s2 == 5) {
                    moeglicheZuege[i++] = zugehoerigkeit; moeglicheZuege[i++] = steinArray[steinPos1];
                    moeglicheZuege[i++] = z2 - 1; moeglicheZuege[i++] = s2; // nur oben
                }
            }
        }

        return moeglicheZuege;
    }

    /** Löscht gegnerische Steine an der Zielposition des Zugs. */
    public int[] loescheStein(int steinArray[], int zug[]) {
        for (int i = 0; i < steinArray.length; i += 4) {
            if (steinArray[i + 2] == zug[2] && steinArray[i + 3] == zug[3]) {
                // Gleiche Position → Stein wird geschlagen
                steinArray[i + 2] = 0;
                steinArray[i + 3] = 0;
            }
        }
        return steinArray;
    }

    /** Prüft die Siegbedingungen. */
    public boolean siegBedingungen(int[] SpielerSteinPosBewegt) {
        // Spieler 1 gewinnt: Stein in Ecke (5,5) ODER alle gegnerischen Steine geschlagen
        for (int i = 0; i < SpielerSteinPosBewegt.length; i += 4) {
            if (SpielerSteinPosBewegt[i] == 1 && SpielerSteinPosBewegt[i + 2] == 5 && SpielerSteinPosBewegt[i + 3] == 5) {
                setzeSieger(1);
                return true;
            }
        }

        // Spieler 2 gewinnt: Stein in Ecke (1,1) ODER alle gegnerischen Steine geschlagen
        for (int i = 0; i < SpielerSteinPosBewegt.length; i += 4) {
            if (SpielerSteinPosBewegt[i] == 0 && SpielerSteinPosBewegt[i + 2] == 1 && SpielerSteinPosBewegt[i + 3] == 1) {
                setzeSieger(0);
                return true;
            }
        }

        // Alle Steine von Spieler 1 geschlagen (Position = 0)
        boolean alleSpieler1Geschlagen = true;
        for (int i = 0; i < 24; i += 4) {
            if (SpielerSteinPosBewegt[i + 2] != 0 || SpielerSteinPosBewegt[i + 3] != 0) {
                alleSpieler1Geschlagen = false;
                break;
            }
        }
        if (alleSpieler1Geschlagen) {
            setzeSieger(0);
            return true;
        }

        // Alle Steine von Spieler 2 geschlagen
        boolean alleSpieler2Geschlagen = true;
        for (int i = 24; i < 48; i += 4) {
            if (SpielerSteinPosBewegt[i + 2] != 0 || SpielerSteinPosBewegt[i + 3] != 0) {
                alleSpieler2Geschlagen = false;
                break;
            }
        }
        if (alleSpieler2Geschlagen) {
            setzeSieger(1);
            return true;
        }

        return false;
    }

    public int setzeSieger(int zugehoerigkeit) {
        this.siegerZugehoerigkeit = zugehoerigkeit;
        return siegerZugehoerigkeit;
    }

    public int gibSieger() {
        return siegerZugehoerigkeit;
    }

    /** Prüft, ob die Startpositionen der Steine korrekt sind. */
    public boolean pruefeGegnerInitialisierung(int[] spieler2IntArray) {
        int[] richtigeSteine = {1, 2, 3, 4, 5, 6};
        int rs = 0; // Anzahl richtiger Steinnummern
        int rp = 0; // Anzahl richtiger Positionen

        // Prüfe: alle Nummern 1-6 vorhanden (einmalig)
        for (int i = 1; i < 24; i += 4) {
            for (int j = 0; j < 6; j++) {
                if (spieler2IntArray[i] == richtigeSteine[j]) {
                    rs++;
                    richtigeSteine[j] = 0;
                    break;
                }
            }
        }

        // Prüfe Startpositionen für Spieler 1 (oben-links)
        int[][] initPos1 = {{1, 2}, {3, 1}, {2, 2}, {2, 1}, {1, 3}, {1, 1}};
        if (spieler2IntArray[0] == 1) {
            for (int z = 2, s = 3; z < 24 && s < 24; z += 4, s += 4) {
                for (int j = 0; j < 6; j++) {
                    if (spieler2IntArray[z] == initPos1[j][0] && spieler2IntArray[s] == initPos1[j][1]) {
                        rp++;
                        break;
                    }
                }
            }
        }

        // Prüfe Startpositionen für Spieler 2 (unten-rechts)
        int[][] initPos0 = {{5, 4}, {3, 5}, {4, 4}, {4, 5}, {5, 3}, {5, 5}};
        if (spieler2IntArray[0] == 0) {
            for (int z = 2, s = 3; z < 24 && s < 24; z += 4, s += 4) {
                for (int j = 0; j < 6; j++) {
                    if (spieler2IntArray[z] == initPos0[j][0] && spieler2IntArray[s] == initPos0[j][1]) {
                        rp++;
                        break;
                    }
                }
            }
        }

        return rs == 6 && rp == 6;
    }

    /** Prüft, ob ein Zug des Gegners gültig ist. */
    public boolean pruefeGegnerZug(int wuerfelZahl, int[] gegnerZug, int zugehoerigkeit, int[] SpielerSteinPos) {
        int[] moeglicheGeGenerZuege = gibmoeglicheSpielzuege(zugehoerigkeit, wuerfelZahl, SpielerSteinPos);

        // Prüfe ob der Zug in den möglichen Zügen enthalten ist
        for (int i = 1; i <= 24; i += 4) {
            if (gegnerZug[1] == moeglicheGeGenerZuege[i] &&
                gegnerZug[2] == moeglicheGeGenerZuege[i + 1] &&
                gegnerZug[3] == moeglicheGeGenerZuege[i + 2]) {
                return true;
            }
        }
        return false;
    }

    /** Wandelt eine Benutzer-Auswahl (1-6) in einen konkreten Zug um. */
    public int[] benutzerAuswahlInZug(int[] moeglicheZuege, int auswahl) {
        int[] zug = new int[4];
        int offset = (auswahl - 1) * 4;

        if (offset + 3 < moeglicheZuege.length && moeglicheZuege[offset] != 0) {
            zug[0] = moeglicheZuege[offset];     // zugehoerigkeit
            zug[1] = moeglicheZuege[offset + 1]; // steinnummer
            zug[2] = moeglicheZuege[offset + 2]; // zeile
            zug[3] = moeglicheZuege[offset + 3]; // spalte
        }
        return zug;
    }

    /** Gibt die Startpositionen für einen Spieler zurück. */
    public int[][] gibStartPositionen(int zugehoerigkeit) {
        if (zugehoerigkeit == 1) { // oben-links
            return new int[][]{{1, 2}, {3, 1}, {2, 2}, {2, 1}, {1, 3}, {1, 1}};
        } else { // unten-rechts
            return new int[][]{{5, 4}, {3, 5}, {4, 4}, {4, 5}, {5, 3}, {5, 5}};
        }
    }

    /** Gibt die Zielseite für einen Spieler zurück. */
    public int[] gibZielSeite(int zugehoerigkeit) {
        if (zugehoerigkeit == 1) return new int[]{5, 5}; // unten-rechts
        return new int[]{1, 1}; // oben-links
    }

    /** Gibt die Brettgröße zurück. */
    public int gibBrettGroesse() {
        return 5;
    }
}
