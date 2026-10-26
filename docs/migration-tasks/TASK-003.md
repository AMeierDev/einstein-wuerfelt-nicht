# TASK-003: Shared-Module erstellen (Maven Multi-Module)

## ID
TASK-003

## Titel
Neues Maven Multi-Module-Projekt mit Shared-Spiellogik-Modul erstellen

## Beschreibung
Ein neues Maven Multi-Module-Projekt aufsetzen, das ein `shared`-Modul für die Spiellogik enthält. Dieses Modul wird von Server und Client gemeinsam genutzt – damit beide dieselbe Spiellogik haben (Single Source of Truth).

## Voraussetzungen
- TASK-001 abgeschlossen (Java 21 + Vaadin BOM)
- TASK-002 abgeschlossen (Decompiler-Analyse verfügbar, falls nötig)

## Schritte
1. **Neues Parent POM erstellen** (`pom.xml` im Root):
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>de.einsteinwuerfelt</groupId>
    <artifactId>einstein-wuerfelt-nicht</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    
    <modules>
        <module>shared</module>
        <module>server</module>
        <module>client</module>
    </modules>
    
    <properties>
        <java.version>21</java.version>
        <vaadin.version>25.x.y</vaadin.version>
        <spring-boot.version>3.4.x</spring-boot.version>
    </properties>
    
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
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <release>${java.version}</release>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

2. **Shared-Module erstellen** (`shared/pom.xml`):
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>de.einsteinwuerfelt</groupId>
        <artifactId>einstein-wuerfelt-nicht</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <artifactId>einstein-shared</artifactId>
    
    <dependencies>
        <!-- Nur Java-Standardbibliothek + ggf. Lombok -->
    </dependencies>
</project>
```

3. **Server-Module erstellen** (`server/pom.xml`):
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>de.einsteinwuerfelt</groupId>
        <artifactId>einstein-wuerfelt-nicht</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <artifactId>einstein-server</artifactId>
    
    <dependencies>
        <dependency>
            <groupId>de.einsteinwuerfelt</groupId>
            <artifactId>einstein-shared</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

4. **Client-Module erstellen** (`client/pom.xml`):
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>de.einsteinwuerfelt</groupId>
        <artifactId>einstein-wuerfelt-nicht</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <artifactId>einstein-client</artifactId>
    
    <dependencies>
        <dependency>
            <groupId>de.einsteinwuerfelt</groupId>
            <artifactId>einstein-shared</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.vaadin</groupId>
            <artifactId>vaadin-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

5. **Build testen:** `mvn clean compile` – muss durchlaufen

## Acceptance Criteria
- [ ] Parent POM mit 3 Modulen (shared, server, client) existiert
- [ ] `shared/pom.xml` erstellt und kompiliert
- [ ] `server/pom.xml` mit Spring Boot + WebSocket Dependency
- [ ] `client/pom.xml` mit Vaadin Flow Dependency
- [ ] Alle Module hängen vom `shared`-Modul ab
- [ ] `mvn clean compile` läuft durch

## Wichtige Dateien
| Datei | Inhalt |
|---|---|
| `pom.xml` (Root) | Parent POM, 3 Modules |
| `shared/pom.xml` | Shared-Spiellogik-Modul |
| `server/pom.xml` | Server mit Spring Boot + WebSocket |
| `client/pom.xml` | Client mit Vaadin Flow |

## Hinweis
- Die alten `pom.xml`-Dateien (Parent, client, server) können umbenannt werden (z.B. `pom-old.xml`) als Backup
- Das `shared`-Modul enthält später die Spielregeln, Modelle und KI – keine UI-Abhängigkeiten!
