package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

/** Eine Client-Verbindung samt Spielzustand. Zustandsfelder werden nur unter dem Lobby-Lock geaendert. */
final class Player {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    /** null, solange nicht eingeloggt. */
    String name;
    /** Spieler, den wir eingeladen haben und dessen Antwort aussteht. */
    Player asked;
    /** Spieler, der uns eingeladen hat und auf unsere Antwort wartet. */
    Player invitedBy;
    Game game;

    Player(Socket socket, Charset charset) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
    }

    boolean isLoggedIn() {
        return name != null;
    }

    boolean isFree() {
        return isLoggedIn() && game == null && asked == null && invitedBy == null;
    }

    String readLine() throws IOException {
        return in.readLine();
    }

    void send(String line) {
        synchronized (out) {
            try {
                out.write(line);
                out.write("\n");
                out.flush();
            } catch (IOException e) {
                close();
            }
        }
    }

    void close() {
        try {
            socket.close();
        } catch (IOException e) {
            // schon geschlossen
        }
    }

    @Override
    public String toString() {
        return name != null ? name : String.valueOf(socket.getRemoteSocketAddress());
    }
}
