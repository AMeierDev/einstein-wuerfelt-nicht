# TASK-001: Java-Upgrade auf 21 + Vaadin-BOM einrichten

## ID
TASK-001

## Titel
Java-Upgrade auf Java 21 und Vaadin 25 BOM im Parent POM konfigurieren

## Beschreibung
Das Projekt läuft aktuell auf Java 8 (pom.xml: `maven.compiler.release=8`). Vaadin 25 benötigt mindestens Java 17, empfohlen ist Java 21 LTS. Dieser Task richtet die Build-Umgebung ein.

## Voraussetzungen
- Git-Branch `Vaadin-25-rebuild` existiert und ist aktiv
- Maven ≥ 3.9 installiert
- JDK 21 (oder neuer) auf dem System verfügbar (`java -version`)

## Schritte
1. **Parent POM aktualisieren:**
   - `maven.compiler.release` von `8` auf `21` ändern
   - `<properties>`: `vaadin.version=25.x.y` setzen (aktuellste Vaadin 25 Version prüfen)
   - `spring-boot-starter-parent` als Parent hinzufügen (oder Vaadin BOM + Spring Boot Dependency Management)

2. **Vaadin BOM im Parent POM einfügen:**
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.vaadin</groupId>
            <artifactId>vaadin-bom</artifactId>
            <version>${vaadin.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

3. **Client POM aktualisieren:**
   - `maven.compiler.release` auf `21` setzen
   - Vaadin Flow Dependency hinzufügen:
```xml
<dependency>
    <groupId>com.vaadin</groupId>
    <artifactId>vaadin-spring-boot-starter</artifactId>
</dependency>
```

4. **Server POM aktualisieren:**
   - `maven.compiler.release` auf `21` setzen
   - Spring Boot Starter + Vaadin Flow Dependency hinzufügen:
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
</dependencies>
```

5. **Build testen:** `mvn clean compile` – muss ohne Fehler durchlaufen (auch wenn Code noch nicht angepasst ist, nur Kompilierung prüfen)

## Acceptance Criteria
- [ ] Parent POM hat Java 21 als Compiler-Version
- [ ] Vaadin BOM ist im Parent POM importiert
- [ ] Client und Server haben die neuen Dependencies
- [ ] `mvn clean compile` läuft durch (ohne Fehler, auch wenn Code noch nicht angepasst)
- [ ] Branch `Vaadin-25-rebuild` enthält alle Änderungen

## Wichtige Dateien
| Datei | Änderung |
|---|---|
| `pom.xml` (Parent) | Java 21, Vaadin BOM |
| `client/pom.xml` | Vaadin Flow Dependency |
| `server/pom.xml` | Spring Boot + Vaadin + WebSocket |

## Hinweis
- Keine Codeänderungen an Java-Quelldateien nötig – nur POM-Änderungen
- Die alten Swing/Java 8-Dateien bleiben vorerst unverändert (werden in TASK-004 ersetzt)
