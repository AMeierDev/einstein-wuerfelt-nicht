package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * Einfacher Spieleserver fuer "Einstein wuerfelt nicht": meldet Spieler an, vermittelt
 * Partien und leitet die Botschaften zwischen den beiden Spielern weiter.
 * <p>
 * Aufruf: {@code java -jar server.jar [port]}. Zeichensatz ueber {@code -Dserver.charset=...}
 * (Standard UTF-8; muss zum Zeichensatz der Clients passen, wenn Namen Umlaute enthalten).
 */
public final class GameServer implements AutoCloseable {

    private final ServerSocket serverSocket;
    private final Lobby lobby = new Lobby(new Random());
    private final Charset charset;
    private volatile boolean running = true;

    public GameServer(int port, Charset charset) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.charset = charset;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    /** Nimmt Verbindungen an, bis {@link #close()} aufgerufen wird. */
    public void run() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                Thread t = new Thread(new Connection(socket), "client-" + socket.getRemoteSocketAddress());
                t.setDaemon(true);
                t.start();
            } catch (IOException e) {
                if (running) {
                    System.err.println("Fehler beim Annehmen einer Verbindung: " + e);
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        running = false;
        serverSocket.close();
    }

    private final class Connection implements Runnable {
        private final Socket socket;

        Connection(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            Player player = null;
            try {
                player = new Player(socket, charset);
                lobby.connected(player);
                String line;
                while ((line = player.readLine()) != null) {
                    lobby.handle(player, line);
                }
            } catch (IOException e) {
                // Verbindung abgebrochen
            } finally {
                if (player != null) {
                    lobby.disconnected(player);
                    player.close();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : Protocol.DEFAULT_PORT;
        Charset charset = Charset.forName(System.getProperty("server.charset", "UTF-8"));
        GameServer server = new GameServer(port, charset);
        System.out.println("Einstein-wuerfelt-nicht-Server lauscht auf Port " + server.getPort());
        server.run();
    }
}
