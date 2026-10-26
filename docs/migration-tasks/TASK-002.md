# TASK-002: Decompiler-Analyse der Binary-Dateien

## ID
TASK-002

## Titel
Decompiler-Analyse der .class-Binary-Dateien für die Spiellogik

## Beschreibung
Das Projekt enthält `.class`-Binary-Dateien (kompilierte Klassen), die nicht im Git sind. Diese müssen decompiliert werden, um den vollständigen Code zu verstehen – insbesondere `SpielGui.java`, `Spiellogik.java`, `KI.java`, `Stein.java`, `Spieler.java`, `Steuerung.java`, `EingabeThread.java`, `Konsole.java`, `NetzwerkSchnittstelle.java`, `Bildschirmanzeige.java`.

## Voraussetzungen
- TASK-001 abgeschlossen (Java 21 + Vaadin BOM)
- Decompiler verfügbar: `javap` (JDK-Bordmittel), `fernflower`, oder `cfr`
- Zugriff auf die `.class`-Dateien im Projekt (vermutlich in `client/target/classes/` oder einem `bin/`-Ordner)

## Schritte
1. **Binary-Dateien lokalisieren:**
   - Suche nach allen `.class`-Dateien: `find_files` mit Glob `**/*.class`
   - Prüfen ob sie im Git sind (`.gitignore` prüfen) oder nur lokal vorhanden

2. **Decompilieren:**
   ```bash
   # Option A: javap (JDK-Bordmittel, textbasiert)
   javap -c -p -classpath <pfad> <Klassenname> > decompiled.txt
   
   # Option B: cfr (empfohlen für vollständigen Code)
   java -jar cfr.jar <datei.class> --outputdir ./decompiled/
   
   # Option C: fernflower
   java -jar fernflower.jar <datei.class> ./decompiled/
   ```

3. **Decompilierten Code analysieren:**
   - `SpielGui.java`: Swing-Komponenten, Layout, Event-Handling
   - `Spiellogik.java`: Spielregeln, Würfeln, Zugrecht, Gewinnbedingungen
   - `KI.java`: KI-Logik (Welche Züge macht die KI?)
   - `Stein.java`: Stein-Modell (Position, Farbe, Typ)
   - `Spieler.java`: Spieler-Modell (Name, Punkte, Steine)
   - `Steuerung.java`: Hauptcontroller – koordiniert Spiellogik + GUI + Netzwerk
   - `NetzwerkSchnittstelle.java`: TCP-Client für Multiplayer
   - `Bildschirmanzeige.java`: Display-Schnittstelle

4. **Ergebnis dokumentieren:**
   - Jede decompilierte Klasse in einem eigenen File unter `docs/migration-tasks/decompiled/` speichern
   - Wichtige Methoden und ihre Signaturen notieren
   - Unklare Stellen markieren (die müssen im Original-Code nachgeschaut werden)

## Acceptance Criteria
- [ ] Alle 10 decompilierten Klassen sind verfügbar unter `docs/migration-tasks/decompiled/`
- [ ] Wichtige Methoden-Signaturen dokumentiert
- [ ] Unklare Stellen markiert (mit Fragezeichen oder TODO-Kommentar)
- [ ] Ergebnis in `TASK-002.md` als Referenz verlinkt

## Wichtige Dateien
| Datei | Bedeutung für Migration |
|---|---|
| `SpielGui.java` | Swing-GUI → Vaadin UI-Komponenten |
| `Spiellogik.java` | Spielregeln → Shared Module |
| `KI.java` | KI-Logik → Shared Module (als Service) |
| `Stein.java` | Stein-Modell → Shared Module |
| `Spieler.java` | Spieler-Modell → Shared Module |
| `Steuerung.java` | Hauptcontroller → Vaadin View/Controller |
| `NetzwerkSchnittstelle.java` | TCP-Client → WebSocket-Client |
| `Bildschirmanzeige.java` | Display-Schnittstelle → Vaadin UI-Komponenten |

## Hinweis
- Falls keine `.class`-Dateien im Repo sind: Der Code ist bereits in den Java-Quelldateien (`spiel/`, `Interfaces/`) vorhanden. Dieser Task ist dann entbehrlich.
- Die decompilierten Dateien dienen als Referenz, wenn der Originalcode unklar ist.
