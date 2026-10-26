# Migrationsplan: "Einstein würfelt nicht" → Vaadin 25

## Ziel
Migration des Brett-/Würfelspiels von **Java 8 + Swing/TCP** zu **Vaadin 25 (Flow) + Java 17+**.

## Projektstruktur (aktuell)
```
pom.xml                    # Parent POM, Java 8, modules: client + server
client/                    # Swing-Client mit Spiellogik
  src/main/java/spiel/     # Steuerung.java, SpielGui.java, Spiellogik.java, KI.java, ...
server/                    # TCP-Server für Multiplayer
  src/main/java/server/    # GameServer.java, Lobby.java, Game.java, Player.java
Interfaces/                # Gemeinsame Interfaces (Bildschirmanzeige, AntwortEmpfaenger, SpielerInterface)
```

## Architektur (aktuell)
- **Singleplayer:** `Steuerung` → `Spiellogik` + `Bildschirmanzeige` (Swing GUI)
- **Multiplayer:** `Steuerung` → `NetzwerkSchnittstelle` (TCP zu Server Port 1078)
- **Server:** TCP-Listener auf Port 1078, Textprotokoll (login, liste, spiel, ja/nein, quit...)

## Task-Reihenfolge & Abhängigkeiten

```
Phase 0: Vorbereitung
├── TASK-001: Java-Upgrade + Vaadin-BOM (unabhängig)
└── TASK-002: Decompiler-Analyse der Binary-Dateien (parallel zu 001)

Phase 1: Shared Module (Spiellogik extrahieren)
├── TASK-003: Shared-Module erstellen (Maven Multi-Module)
│   └── Abhängig von: 001, 002
├── TASK-004: Spielregeln & Modelle in Shared-Module
│   └── Abhängig von: 003

Phase 2: Server-Migration (Spring Boot + WebSocket)
├── TASK-005: Server zu Spring Boot + Vaadin Flow
│   └── Abhängig von: 001, 004
├── TASK-006: TCP → WebSocket Migration
│   └── Abhängig von: 005
├── TASK-007: Lobby neu implementieren (WebSocket-basiert)
│   └── Abhängig von: 006

Phase 3: Client-Migration (Vaadin Flow UI)
├── TASK-008: Vaadin UI-Komponenten erstellen
│   └── Abhängig von: 004, 007
├── TASK-009: Spielbrett-Rendering in Vaadin
│   └── Abhängig von: 008
├── TASK-010: WebSocket-Client für Vaadin
│   └── Abhängig von: 006, 007

Phase 4: Integration & Polish
├── TASK-011: Server + Client verbinden (End-to-End)
│   └── Abhängig von: 009, 010
└── TASK-012: Polishing, Tests, Dokumentation
    └── Abhängig von: 011
```

## Wichtige Entscheidungen
| Entscheidung | Wert | Begründung |
|---|---|---|
| Java-Version | **Java 21** (LTS) | Vaadin 25 unterstützt Java 17+, aber 21 ist aktueller LTS |
| Server-Architektur | **Spring Boot + WebSocket** | Echtzeit-Kommunikation, moderne Architektur |
| Client-Architektur | **Vaadin Flow** (server-side UI) | Push-Updates, keine manuelle JS nötig |
| Spiellogik | **Shared Maven Module** | Server und Client teilen dieselbe Logik |
| Protokoll | **JSON über WebSocket** | Ersetzt das textbasierte TCP-Protokoll |

## Auszuführende Tasks (in Reihenfolge)
1. `TASK-001.md` – Java-Upgrade + Vaadin-BOM
2. `TASK-002.md` – Decompiler-Analyse der Binary-Dateien
3. `TASK-003.md` – Shared-Module erstellen
4. `TASK-004.md` – Spielregeln & Modelle in Shared-Module
5. `TASK-005.md` – Server zu Spring Boot + Vaadin Flow
6. `TASK-006.md` – TCP → WebSocket Migration
7. `TASK-007.md` – Lobby neu implementieren
8. `TASK-008.md` – Vaadin UI-Komponenten erstellen
9. `TASK-009.md` – Spielbrett-Rendering in Vaadin
10. `TASK-010.md` – WebSocket-Client für Vaadin
11. `TASK-011.md` – Server + Client verbinden
12. `TASK-012.md` – Polishing, Tests, Dokumentation
