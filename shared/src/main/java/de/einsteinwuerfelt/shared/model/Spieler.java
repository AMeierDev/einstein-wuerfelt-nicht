package de.einsteinwuerfelt.shared.model;

/**
 * Repräsentiert einen Spieler ohne UI-Abhängigkeiten.
 */
public class Spieler {

    private final String name;
    private final int zugehoerigkeit; // 0 oder 1

    public Spieler(String name, int zugehoerigkeit) {
        this.name = name;
        this.zugehoerigkeit = zugehoerigkeit;
    }

    public String gibName() { return name; }
    public int gibZugehoerigkeit() { return zugehoerigkeit; }

    @Override
    public String toString() {
        return "Spieler[" + name + "] (zugehörig: " + zugehoerigkeit + ")";
    }
}
