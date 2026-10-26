# TASK-005: Server zu Spring Boot + Vaadin Flow migrieren

## ID
TASK-005

## Titel
Server von TCP/Java 8 zu Spring Boot 3 + Vaadin Flow 25 migrieren

## Beschreibung
Der aktuelle `GameServer.java` (TCP-Listener auf Port 1078) wird durch eine Spring Boot Application ersetzt, die Vaadin Flow als UI-Framework nutzt. Der Server bleibt ein eigenständiger Prozess (kein eingebetteter Browser).

## Voraussetzungen
- TASK-001 abgeschlossen (Java 21 + Vaadin BOM)
- TASK-004 abgeschlossen (Shared-Module mit Spiellogik existiert)

## Schritte
1. **Spring Boot Application erstellen** (`server/src/main/java/server/`):
   - `EinsteinServerApplication.java` – Hauptklasse mit `@SpringBootApplication`
   - `application.yml` oder `application.properties`:
     ```yaml
     server:
       port: 8080
     vaadin:
       url: /
     spring:
       websocket:
         enabled: true
     ```

2. **Vaadin UI erstellen** (`server/src/main/java/server/ui/`):
   - `ServerView.java` – Haupt-UI-Klasse (extends `VerticalLayout`)
   - Zeigt Spielerliste, Lobby-Status, Spielstatus an
   - Verwendet Vaadin Components: `Grid`, `Button`, `Label`, `TextField`

3. **Spring Boot Starter hinzufügen** (`server/pom.xml`):
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.x</version>
</parent>
<dependencies>
    <dependency>
        <groupId>com.vaadin</groupId>
        <artifactId>vaadin-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
    <dependency>
        <groupId>de.einsteinwuerfelt</groupId>
        <artifactId>einstein-shared</artifactId>
        <version>${project.version}</version>
    </dependency>
</dependencies>
```

4. **Vaadin Route konfigurieren:**
   - `@Route("")` auf der Haupt-UI-Klasse
   - Vaadin Flow Router für Navigation zwischen Views (Lobby, Spiel, etc.)

5. **Spring Boot starten testen:**
   ```bash
   cd server && mvn spring-boot:run
   ```
   - Server startet auf Port 8080
   - Vaadin UI ist unter `http://localhost:8080` erreichbar (als Admin-View)

## Acceptance Criteria
- [ ] `EinsteinServerApplication.java` existiert mit `@SpringBootApplication`
- [ ] Spring Boot Starter Dependencies in `server/pom.xml`
- [ ] Vaadin UI-Klasse (`ServerView`) existiert und rendert eine einfache View
- [ ] Server startet mit `mvn spring-boot:run` auf Port 8080
- [ ] Vaadin UI ist unter `http://localhost:8080` erreichbar

## Wichtige Dateien
| Datei | Inhalt |
|---|---|
| `server/src/main/java/server/EinsteinServerApplication.java` | Spring Boot Hauptklasse |
| `server/src/main/java/server/ui/ServerView.java` | Vaadin UI (Admin-View) |
| `server/pom.xml` | Spring Boot + Vaadin Dependencies |

## Hinweis
- Der alte `GameServer.java` bleibt vorerst als Backup erhalten (umbenannt zu `GameServer-old.java`)
- Die TCP-Kommunikation wird in TASK-006 durch WebSocket ersetzt
- Dieser Task erstellt nur die **Admin-View** des Servers – die Spieler-Views kommen in TASK-008
