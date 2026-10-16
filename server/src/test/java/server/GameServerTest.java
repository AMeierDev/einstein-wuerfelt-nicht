package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Integrationstests mit echten Sockets. Die Auswertung der Server-Nachrichten verwendet
 * dieselben festen Zeichenpositionen wie der Client (spiel.Steuerung#nachrichtEmpangen).
 */
public class GameServerTest {

    private static final String INIT_A = "113 231 322 421 512 611";
    private static final String INIT_B = "153 235 344 445 554 655";

    private GameServer server;
    private final java.util.List<TestClient> clients = new java.util.ArrayList<TestClient>();

    @Before
    public void startServer() throws IOException {
        server = new GameServer(0, StandardCharsets.UTF_8);
        Thread t = new Thread(new Runnable() {
            public void run() {
                server.run();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @After
    public void stopServer() throws IOException {
        for (TestClient c : clients) {
            c.close();
        }
        server.close();
    }

    private TestClient connect() throws IOException {
        TestClient c = new TestClient(server.getPort());
        clients.add(c);
        assertTrue(c.read().startsWith("Server B> Verbindung"));
        return c;
    }

    private TestClient login(String name) throws IOException {
        TestClient c = connect();
        c.send("login " + name);
        String welcome = c.read();
        assertTrue(welcome, welcome.startsWith("Server B>") && welcome.endsWith("sind angemeldet"));
        return c;
    }

    /** Zwei Spieler; alice laedt bob ein, bob nimmt an. Liest alle Startnachrichten. */
    private TestClient[] startGame() throws IOException {
        TestClient alice = login("alice");
        TestClient bob = login("bob");
        alice.send("spiel bob");
        assertEquals("Server B> Anfrage an bob gesendet", alice.read());
        String q = bob.read();
        assertEquals("alice", q.substring(10, q.length() - 43));
        bob.send("ja");
        String accepted = alice.read();
        assertEquals("bob", accepted.substring(10, accepted.length() - 11));
        assertTrue(accepted.endsWith("akzeptiert"));
        assertEquals("Server B> Spiel startet", bob.read());
        assertEquals("Server M> Spiel startet. alice setzt zuerst", alice.read());
        assertEquals("Server M> Spiel startet. alice setzt zuerst", bob.read());
        return new TestClient[] {alice, bob};
    }

    private static int readDie(TestClient c) throws IOException {
        assertEquals("Server Z> Sie sind am Zug", c.read());
        String dice = c.read();
        assertTrue(dice.startsWith("Server Z> W"));
        return Integer.parseInt(dice.substring(18, 19));
    }

    @Test
    public void loginListeUndNamenskonflikt() throws IOException {
        TestClient alice = login("alice");
        alice.send("liste");
        assertEquals("Server B> Folgende Spieler sind frei: Sonst keiner da!", alice.read());

        TestClient second = connect();
        second.send("login alice");
        assertTrue(second.read().startsWith("Server E102"));
        second.send("liste");
        assertTrue(second.read().startsWith("Server E101"));
        second.send("login bob");
        assertTrue(second.read().endsWith("sind angemeldet"));

        alice.send("liste");
        assertEquals("Server B> Folgende Spieler sind frei: bob", alice.read());
        alice.send("werbinich");
        assertEquals("Server B> Sie sind alice", alice.read());
    }

    @Test
    public void einladungFehlerfaelle() throws IOException {
        TestClient alice = login("alice");
        alice.send("spiel");
        assertTrue(alice.read().startsWith("Server E202"));
        alice.send("spiel alice");
        assertTrue(alice.read().startsWith("Server E205"));
        alice.send("spiel niemand");
        assertTrue(alice.read().startsWith("Server E203"));
        TestClient bob = login("bob");
        alice.send("spiel bob");
        alice.read();
        bob.read();
        alice.send("spiel bob");
        assertTrue(alice.read().startsWith("Server E207"));
        bob.send("spiel alice");
        assertTrue(bob.read().startsWith("Server E206"));
        TestClient carol = login("carol");
        carol.send("spiel bob");
        assertTrue(carol.read().startsWith("Server E204"));
    }

    @Test
    public void ablehnung() throws IOException {
        TestClient alice = login("alice");
        TestClient bob = login("bob");
        alice.send("spiel bob");
        alice.read();
        bob.read();
        bob.send("nein");
        assertEquals("Server M> Ablehnung an alice weitergegeben", bob.read());
        assertEquals("Server E201> bob moechte nicht gegen Sie spielen", alice.read());
        alice.send("liste");
        assertEquals("Server B> Folgende Spieler sind frei: bob", alice.read());
    }

    @Test
    public void spielablaufMitWeiterleitung() throws IOException {
        TestClient[] p = startGame();
        TestClient alice = p[0];
        TestClient bob = p[1];

        // Runde 1: alice sendet ihre Aufstellung (Wuerfel wird vom Client ignoriert)
        int die1 = readDie(alice);
        alice.send(INIT_A);
        String relayed = bob.read();
        assertTrue(relayed.startsWith("alice"));
        assertEquals(die1, Integer.parseInt(relayed.substring("alice".length() + 10, "alice".length() + 11)));
        assertEquals(INIT_A, relayed.substring("alice".length() + 16));
        assertEquals(Protocol.INIT_LENGTH, relayed.substring("alice".length() + 16).length());

        // Runde 2: bob sendet seine Aufstellung
        readDie(bob);
        bob.send(INIT_B);
        assertEquals(INIT_B, alice.read().substring("bob".length() + 16));

        // Runde 3: alice zieht, bob bekommt Zug mit dem Wuerfel von alice
        int die3 = readDie(alice);
        alice.send("234");
        String move = bob.read();
        assertEquals(die3, Integer.parseInt(move.substring("alice".length() + 10, "alice".length() + 11)));
        assertEquals("234", move.substring("alice".length() + 16));

        // Runde 4: bob ist dran; alice darf nicht dazwischenfunken
        readDie(bob);
        alice.send("999");
        bob.send("111");
        assertEquals("111", alice.read().substring("bob".length() + 16));
    }

    @Test
    public void schummelmeldungWirdWeitergegebenUndQuitBeendetSpiel() throws IOException {
        TestClient[] p = startGame();
        TestClient alice = p[0];
        TestClient bob = p[1];
        readDie(alice);
        alice.send(INIT_A);
        bob.read();
        readDie(bob);

        bob.send("Client E01");
        assertTrue(alice.read().endsWith("Client E01"));

        alice.send("quit");
        assertEquals("Server M> Spielende", alice.read());
        assertEquals("Server M> Spielende", bob.read());

        // beide sind wieder frei
        alice.send("liste");
        assertEquals("Server B> Folgende Spieler sind frei: bob", alice.read());
    }

    @Test
    public void verbindungsabbruchImSpielInformiertGegner() throws IOException {
        TestClient[] p = startGame();
        p[0].close();
        // Die Zugnachrichten fuer alice gingen an alice; bob erhaelt das Spielende
        assertEquals("Server M> Spielende", p[1].read());
    }

    @Test
    public void logoutTrenntVerbindung() throws IOException {
        TestClient alice = login("alice");
        alice.send("logout");
        assertEquals("Server B> disconnect", alice.read());
        assertNull(alice.read());
        TestClient again = login("alice");
        again.send("liste");
        assertEquals("Server B> Folgende Spieler sind frei: Sonst keiner da!", again.read());
    }

    /** Zeilenbasierter Testclient mit Lese-Timeout. */
    private static final class TestClient {
        private final Socket socket;
        private final BufferedReader in;
        private final PrintWriter out;

        TestClient(int port) throws IOException {
            socket = new Socket("localhost", port);
            socket.setSoTimeout(3000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(socket.getOutputStream(), true);
        }

        void send(String line) {
            out.println(line);
        }

        String read() throws IOException {
            return in.readLine();
        }

        void close() throws IOException {
            socket.close();
        }
    }
}
