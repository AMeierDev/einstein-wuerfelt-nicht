package server;

/**
 * Textprotokoll zwischen Spiel-Client und Server (zeilenbasiert, Port 1078).
 * <p>
 * Die Formate wurden aus dem Client rekonstruiert (client.NetzwerkSchnittstelle und
 * spiel.Steuerung#nachrichtEmpangen). Der Client schneidet Teile der Nachrichten ueber
 * feste Zeichenpositionen aus, deshalb muessen Praefixe und Suffixe exakt stimmen.
 * Alle Nachrichten sind reines ASCII, damit die Positionen unabhaengig vom Zeichensatz
 * stimmen ("Wurfel" hat dieselbe Laenge wie "Wuerfel" mit Umlaut).
 */
final class Protocol {

    static final int DEFAULT_PORT = 1078;

    /** Laenge der Initialaufstellung, z.B. "113 231 322 421 512 611". */
    static final int INIT_LENGTH = 23;

    private Protocol() {
    }

    // ---- Befehle vom Client ----
    static final String CMD_LOGIN = "login";
    static final String CMD_LIST = "liste";
    static final String CMD_WHO = "werbinich";
    static final String CMD_PLAY = "spiel";
    static final String CMD_YES = "ja";
    static final String CMD_NO = "nein";
    static final String CMD_LOGOUT = "logout";
    static final String CMD_QUIT = "quit";
    /** Wird vom Client gesendet, wenn er beim Gegner einen ungueltigen Zug erkannt hat. */
    static final String CLIENT_CHEAT = "Client E01";

    // ---- Nachrichten B (Basis) ----
    static final String CONNECTED = "Server B> Verbindung zum Spieleserver hergestellt";
    static final String DISCONNECT = "Server B> disconnect";
    static final String HELP = "Server B> Verfuegbare Befehle: login <name>, liste, werbinich, "
            + "spiel <name>, ja, nein, logout, quit";
    /** Der Client prueft auf die Endung "Server B> Spiel startet". */
    static final String GAME_STARTS = "Server B> Spiel startet";

    static String welcome(String name, int count) {
        // Client erwartet die Endung "sind angemeldet" und fordert danach automatisch "liste" an
        return "Server B> Willkommen " + name + ", " + count + " Spieler sind angemeldet";
    }

    static String whoAmI(String name) {
        return "Server B> Sie sind " + name;
    }

    static String list(String namesOrHint) {
        return "Server B> Folgende Spieler sind frei: " + namesOrHint;
    }

    static final String LIST_ALONE = "Sonst keiner da!";
    static final String LIST_NONE_FREE = "Niemand frei!";

    static String requestSent(String partner) {
        return "Server B> Anfrage an " + partner + " gesendet";
    }

    /** Client schneidet den Namen mit substring(10, length - 11) heraus. */
    static String accepted(String partner) {
        return "Server B> " + partner + " akzeptiert";
    }

    // ---- Fehlermeldungen E ----
    static final String E101_LOGIN_FIRST = "Server E101> Bitte einloggen! (login name)";

    static String e102NameTaken(String name) {
        return "Server E102> Login nicht moeglich. Name existiert bereits: " + name;
    }

    static final String E103_NAME_NEEDED = "Server E103> Bitte login mit Namen eingeben";
    static final String E104_ALREADY_LOGGED_IN = "Server E104> Sie sind bereits eingeloggt";

    static String e201Declined(String partner) {
        return "Server E201> " + partner + " moechte nicht gegen Sie spielen";
    }

    static final String E202_PARTNER_NEEDED = "Server E202> spiel: Partnernamen angeben!";

    static String e203NotFound(String partner) {
        return "Server E203> Spieler nicht gefunden: " + partner;
    }

    static String e204Unavailable(String partner) {
        return "Server E204> Spieler " + partner + " nicht verfuegbar!";
    }

    static final String E205_SELF = "Server E205> Sie koennen nicht gegen sich selbst spielen";
    static final String E206_ALREADY_ASKED = "Server E206> Sie werden bereits angefragt";
    static final String E207_ALREADY_ASKING = "Server E207> Sie haben bereits angefragt";

    static String e208Withdrawn(String partner) {
        return "Server E208> " + partner + " zieht seine Spieleinladung zurueck!";
    }

    // ---- Einladung Q ----
    /**
     * Client schneidet den Namen mit substring(10, length - 43) heraus; das Suffix
     * " moechte gegen Sie spielen, o.k.? (ja/nein)" ist genau 43 Zeichen lang.
     */
    static String invitation(String challenger) {
        return "Server Q> " + challenger + " moechte gegen Sie spielen, o.k.? (ja/nein)";
    }

    // ---- Spielablauf M / Z ----
    /** Client erwartet die Endung "setzt zuerst" und setzt damit seinen Wuerfelzaehler zurueck. */
    static String gameStartsFirst(String firstPlayer) {
        return "Server M> Spiel startet. " + firstPlayer + " setzt zuerst";
    }

    static String declinedRelayed(String challenger) {
        return "Server M> Ablehnung an " + challenger + " weitergegeben";
    }

    static final String GAME_OVER = "Server M> Spielende";
    static final String YOUR_TURN = "Server Z> Sie sind am Zug";

    /** Client liest die Wuerfelzahl mit substring(18, 19): "Server Z> " (10) + "Wurfel: " (8). */
    static String dice(int value) {
        return "Server Z> Wurfel: " + value;
    }

    /**
     * Weitergeleitete Botschaft eines Spielers an seinen Gegner. Der Client liest den Wuerfel
     * an Position name.length() + 10 und die Botschaft ab name.length() + 16.
     */
    static String relay(String sender, int die, String text) {
        return sender + " (Wurfel: " + die + ") Z> " + text;
    }
}
