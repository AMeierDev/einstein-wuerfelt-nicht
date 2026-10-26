package de.einsteinwuerfelt.shared.protocol;

/**
 * Login-Anfrage vom Client an den Server.
 */
public class LoginRequest {

    private final String benutzername;
    private final String passwort; // Im echten System: gehasht/Token-basiert

    public LoginRequest(String benutzername, String passwort) {
        this.benutzername = benutzername;
        this.passwort = passwort;
    }

    public String gibBenutzername() { return benutzername; }
    public String gibPasswort() { return passwort; }

    @Override
    public String toString() {
        return "LoginRequest{" + benutzername + ", ***}";
    }
}
