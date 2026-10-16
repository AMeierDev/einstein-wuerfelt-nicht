package server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/** Spielerverwaltung: Login, Spielerliste, Einladungen sowie Start und Ende von Partien. */
final class Lobby {

    private static final int MAX_NAME_LENGTH = 20;

    private final Map<String, Player> players = new LinkedHashMap<String, Player>();
    private final Random random;

    Lobby(Random random) {
        this.random = random;
    }

    synchronized void connected(Player p) {
        p.send(Protocol.CONNECTED);
    }

    synchronized void handle(Player p, String rawLine) {
        String line = rawLine.trim();
        if (line.isEmpty()) {
            return;
        }
        if (p.game != null) {
            handleInGame(p, line);
            return;
        }
        String cmd = line;
        String arg = "";
        int space = line.indexOf(' ');
        if (space > 0) {
            cmd = line.substring(0, space);
            arg = line.substring(space + 1).trim();
        }
        cmd = cmd.toLowerCase(Locale.ROOT);

        if (cmd.equals(Protocol.CMD_LOGOUT) || cmd.equals(Protocol.CMD_QUIT)) {
            logout(p);
        } else if (cmd.equals(Protocol.CMD_LOGIN)) {
            login(p, arg);
        } else if (!p.isLoggedIn()) {
            p.send(Protocol.E101_LOGIN_FIRST);
        } else if (cmd.equals(Protocol.CMD_LIST)) {
            list(p);
        } else if (cmd.equals(Protocol.CMD_WHO)) {
            p.send(Protocol.whoAmI(p.name));
        } else if (cmd.equals(Protocol.CMD_PLAY)) {
            invite(p, arg);
        } else if (p.invitedBy != null && cmd.equals(Protocol.CMD_YES)) {
            answer(p, true);
        } else if (p.invitedBy != null && cmd.equals(Protocol.CMD_NO)) {
            answer(p, false);
        } else {
            p.send(Protocol.HELP);
        }
    }

    synchronized void disconnected(Player p) {
        cleanup(p);
    }

    // ---- Befehle ----

    private void login(Player p, String name) {
        if (p.isLoggedIn()) {
            p.send(Protocol.E104_ALREADY_LOGGED_IN);
            return;
        }
        // Namen mit "Server" am Anfang oder "disconnect" wuerden der Client-Auswertung in die Quere kommen
        if (name.isEmpty() || name.length() > MAX_NAME_LENGTH || name.startsWith("Server")
                || name.contains("disconnect") || containsControlChars(name)) {
            p.send(Protocol.E103_NAME_NEEDED);
            return;
        }
        if (players.containsKey(name)) {
            p.send(Protocol.e102NameTaken(name));
            return;
        }
        p.name = name;
        players.put(name, p);
        p.send(Protocol.welcome(name, players.size()));
    }

    private static boolean containsControlChars(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isISOControl(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private void list(Player p) {
        List<String> free = new ArrayList<String>();
        for (Player other : players.values()) {
            if (other != p && other.isFree()) {
                free.add(other.name);
            }
        }
        if (!free.isEmpty()) {
            p.send(Protocol.list(String.join(", ", free)));
        } else if (players.size() > 1) {
            p.send(Protocol.list(Protocol.LIST_NONE_FREE));
        } else {
            p.send(Protocol.list(Protocol.LIST_ALONE));
        }
    }

    private void invite(Player p, String partnerName) {
        if (partnerName.isEmpty()) {
            p.send(Protocol.E202_PARTNER_NEEDED);
            return;
        }
        if (p.invitedBy != null) {
            p.send(Protocol.E206_ALREADY_ASKED);
            return;
        }
        if (p.asked != null) {
            p.send(Protocol.E207_ALREADY_ASKING);
            return;
        }
        if (partnerName.equals(p.name)) {
            p.send(Protocol.E205_SELF);
            return;
        }
        Player partner = players.get(partnerName);
        if (partner == null) {
            p.send(Protocol.e203NotFound(partnerName));
            return;
        }
        if (!partner.isFree()) {
            p.send(Protocol.e204Unavailable(partnerName));
            return;
        }
        p.asked = partner;
        partner.invitedBy = p;
        partner.send(Protocol.invitation(p.name));
        p.send(Protocol.requestSent(partnerName));
    }

    private void answer(Player p, boolean accepted) {
        Player challenger = p.invitedBy;
        p.invitedBy = null;
        challenger.asked = null;
        if (!accepted) {
            challenger.send(Protocol.e201Declined(p.name));
            p.send(Protocol.declinedRelayed(challenger.name));
            return;
        }
        // Der Einladende beginnt
        Game game = new Game(challenger, p, random);
        challenger.game = game;
        p.game = game;
        challenger.send(Protocol.accepted(p.name));
        p.send(Protocol.GAME_STARTS);
        game.start();
    }

    private void handleInGame(Player p, String line) {
        String cmd = line.toLowerCase(Locale.ROOT);
        if (cmd.equals(Protocol.CMD_QUIT)) {
            endGame(p.game);
        } else if (cmd.equals(Protocol.CMD_LOGOUT)) {
            logout(p);
        } else {
            p.game.handle(p, line);
        }
    }

    // ---- Aufraeumen ----

    private void logout(Player p) {
        p.send(Protocol.DISCONNECT);
        cleanup(p);
        p.close();
    }

    private void endGame(Game game) {
        for (Player player : game.players()) {
            player.game = null;
            player.send(Protocol.GAME_OVER);
        }
    }

    private void cleanup(Player p) {
        if (p.game != null) {
            endGame(p.game);
        }
        if (p.asked != null) {
            p.asked.invitedBy = null;
            p.asked.send(Protocol.e208Withdrawn(p.name));
            p.asked = null;
        }
        if (p.invitedBy != null) {
            p.invitedBy.asked = null;
            p.invitedBy.send(Protocol.e201Declined(p.name));
            p.invitedBy = null;
        }
        if (p.name != null && players.get(p.name) == p) {
            players.remove(p.name);
        }
        p.name = null;
    }
}
