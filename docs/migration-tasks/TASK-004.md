# TASK-004: Spielregeln & Modelle in Shared-Module extrahieren

## ID
TASK-004

## Titel
Spiellogik, Modelle und KI aus Swing/Client in das `shared`-Modul extrahieren

## Beschreibung
Die gesamte Spiellogik (Regeln, Würfeln, Zugrecht, Gewinnbedingungen) wird aus dem Swing-Client (`spiel/Spiellogik.java`, `KI.java`) in ein neues Maven-Module `einstein-shared` ausgelagert. Das Shared-Modul enthält **keine UI-Abhängigkeiten** – nur reine Java-Klassen.

## Voraussetzungen
- TASK-001 abgeschlossen (Java 21 + Vaadin BOM)
- TASK-003 abgeschlossen (Shared-Module existiert)
- TASK-002 abgeschlossen (Decompiler-Analyse verfügbar, falls nötig)

## Schritte
1. **Modelle erstellen** (`shared/src/main/java/de/einsteinwuerfelt/shared/model/`):
   - `Stein.java` – Stein-Klasse (Position, Farbe, Typ, ID)
   - `Spieler.java` – Spieler-Modell (Name, Punkte, Steine, Hand)
   - `Spielzustand.java` – Aktueller Spielzustand (aktueller Spieler, Würfelergebnis, etc.)
   - `Karte.java` / `Zug.java` – falls Karten/Züge modelliert werden

2. **Spielregeln extrahieren** (`shared/src/main/java/de/einsteinwuerfelt/shared/rules/`):
   - `Spiellogik.java` – Kern-Spiellogik (Würfeln, Zugrecht weitergeben, Gewinnbedingungen prüfen)
   - `Spielfeld.java` – Spielfeld-Modell (Felder, Regeln für Felder)
   - `RegelPrüfer.java` – Prüft ob ein Zug legal ist

3. **KI extrahieren** (`shared/src/main/java/de/einsteinwuerfelt/shared/ai/`):
   - `KIStrategie.java` – KI-Interface (getBestMove, evaluatePosition)
   - `DefaultKI.java` – Standard-KI-Implementierung (aus `KI.java` migriert)

4. **Interfaces definieren** (`shared/src/main/java/de/einsteinwuerfelt/shared/`):
   - `SpielObserver.java` – Observer-Pattern für Spielstatus-Änderungen
   - `SpielerInterface.java` – Interface für Spieler (aus `Interfaces/SpielerInterface.java`)

5. **Code migrieren:**
   - Aus `client/src/main/java/spiel/Spiellogik.java`: Regeln, Würfeln, Zugrecht → `shared/rules/`
   - Aus `client/src/main/java/spiel/KI.java`: KI-Logik → `shared/ai/`
   - Aus `Interfaces/SpielerInterface.java`: Interface → `shared/`
   - Aus `Interfaces/Bildschirmanzeige.java`: Display-Schnittstelle → **NICHT** ins Shared-Modul (UI-Abhängigkeit!)

6. **Build testen:** `mvn clean compile` im Root – alle Module müssen kompilieren

## Acceptance Criteria
- [ ] `shared/src/main/java/de/einsteinwuerfelt/shared/model/Stein.java` existiert
- [ ] `shared/src/main/java/de/einsteinwuerfelt/shared/model/Spieler.java` existiert
- [ ] `shared/src/main/java/de/einsteinwuerfelt/shared/rules/Spiellogik.java` existiert
- [ ] `shared/src/main/java/de/einsteinwuerfelt/shared/ai/KIStrategie.java` existiert
- [ ] Alle Modelle sind ohne UI-Abhängigkeiten (keine Swing/Vaadin Imports)
- [ ] `mvn clean compile` läuft durch

## Wichtige Dateien
| Quelle | Ziel |
|---|---|
| `client/spiel/Spiellogik.java` | `shared/rules/Spiellogik.java` |
| `client/spiel/KI.java` | `shared/ai/KIStrategie.java` |
| `Interfaces/SpielerInterface.java` | `shared/SpielerInterface.java` |
| `client/spiel/Stein.java` (falls vorhanden) | `shared/model/Stein.java` |

## Hinweis
- Die alten Dateien in `client/` bleiben vorerst unverändert – sie werden später durch Vaadin-Komponenten ersetzt
- Das Shared-Modul ist die **Single Source of Truth** für Spielregeln!
