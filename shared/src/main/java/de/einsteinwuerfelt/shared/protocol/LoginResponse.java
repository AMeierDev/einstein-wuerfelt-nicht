package de.einsteinwuerfelt.shared.protocol;

/**
 * Login-Antwort vom Server an den Client.
 */
public class LoginResponse {

    private final boolean erfolgreich;
    private final String nachricht; // Fehlermeldung oder "Erfolgreich"
    private final int sessionId;    // Session-ID für authentifizierte Clients

    public LoginResponse(boolean erfolgreich, String nachricht) {
        this.erfolgreich = erfolgreich;
        this.nachricht = nachricht;
        this.sessionId = 0;
    }

    public LoginResponse(boolean erfolgreich, int sessionId) {
        this.erfolgreich = erfolgreich;
        this.nachricht = "Erfolgreich";
        this.sessionId = sessionId;
    }

    public boolean gibErfolgreich() { return erfolgreich; }
    public String gibNachricht() { return nachricht; }
    public int gibSessionId() { return sessionId; }

    @Override
    public String toString() {
        return "LoginResponse{" + (erfolgreich ? "OK" : "FEHLER: " + nachricht) + "}";
    }
}
