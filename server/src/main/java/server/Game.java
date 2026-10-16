package server;

import java.util.Random;

/**
 * Ablauf einer Partie. Der Server wuerfelt und gibt abwechselnd das Zugrecht; Zuege werden
 * nicht geprueft, sondern unveraendert an den Gegner weitergereicht (die Clients pruefen selbst).
 * <p>
 * Die ersten beiden Runden dienen dem Austausch der Startaufstellung (der Client ignoriert
 * dort den Wuerfel und sendet seine Aufstellung), danach wird abwechselnd gezogen.
 */
final class Game {

    private final Player first;
    private final Player second;
    private final Random random;
    private int turn;
    private int die;

    Game(Player first, Player second, Random random) {
        this.first = first;
        this.second = second;
        this.random = random;
    }

    Player[] players() {
        return new Player[] {first, second};
    }

    Player opponent(Player p) {
        return p == first ? second : first;
    }

    private Player current() {
        return turn % 2 == 0 ? first : second;
    }

    void start() {
        first.send(Protocol.gameStartsFirst(first.name));
        second.send(Protocol.gameStartsFirst(first.name));
        beginTurn();
    }

    private void beginTurn() {
        die = random.nextInt(6) + 1;
        Player p = current();
        p.send(Protocol.YOUR_TURN);
        p.send(Protocol.dice(die));
    }

    /** Verarbeitet eine Zeile eines Spielers; gibt false zurueck, wenn sie ignoriert wurde. */
    boolean handle(Player p, String line) {
        if (line.equals(Protocol.CLIENT_CHEAT)) {
            // Meldung "Gegner hat geschummelt" geht unveraendert an den anderen Spieler
            opponent(p).send(Protocol.relay(p.name, die, line));
            return true;
        }
        if (p != current()) {
            return false;
        }
        opponent(p).send(Protocol.relay(p.name, die, line));
        turn++;
        beginTurn();
        return true;
    }
}
