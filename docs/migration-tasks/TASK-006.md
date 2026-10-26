# TASK-006: TCP → WebSocket Migration (Server-Seite)

## ID
TASK-006

## Titel
TCP-Kommunikation im Server durch WebSocket ersetzen

## Beschreibung
Der aktuelle `GameServer.java` nutzt ein textbasiertes TCP-Protokoll auf Port 1078. Dieses wird durch ein WebSocket-basiertes Protokoll ersetzt, das JSON-Nachrichten über standardisierte WebSocket-Sessions sendet/empfängt.

## Voraussetzungen
- TASK-005 abgeschlossen (Spring Boot + Vaadin Flow existiert)
- TASK-004 abgeschlossen (Shared-Module mit Spiellogik)

## Schritte
1. **WebSocket-Konfiguration erstellen** (`server/src/main/java/server/config/`):
   - `WebSocketConfig.java`:
     ```java
     @Configuration
     @EnableWebSocketMessageBroker
     public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
         @Override
         public void configureMessageBroker(MessageBrokerRegistry config) {
             config.enableSimpleBroker("/topic", "/queue");
             config.setApplicationDestinationPrefixes("/app");
             config.setUserDestinationPrefix("/user");
         }
         
         @Override
         public void registerStompEndpoints(StompEndpointRegistry registry) {
             registry.addEndpoint("/ws").setAllowedOrigins("*");
         }
     }
     ```

2. **Spiel-Session-Manager erstellen** (`server/src/main/java/server/session/`):
   - `GameSession.java` – Repräsentiert eine laufende Spiel-Sitzung (WebSocket + Spieler)
   - `LobbyManager.java` – Verwaltet alle aktiven Sessions und Lobby-Zustand
   - `PlayerConnection.java` – Wrapper um WebSocket-Session mit Spieler-Info

3. **WebSocket-Handler erstellen** (`server/src/main/java/server/handler/`):
   - `GameWebSocketHandler.java`:
     ```java
     @Component
     public class GameWebSocketHandler extends TextMessageWebSocketHandler {
         // onTextMessage: empfängt JSON, parst Befehle, leitet an LobbyManager weiter
         // sendToPlayer: sendet JSON-Nachrichten an einen bestimmten Spieler
     }
     ```

4. **Protokoll-Definition erstellen** (`shared/src/main/java/de/einsteinwuerfelt/shared/protocol/`):
   - `GameMessage.java` – Basisklasse für alle Nachrichten (JSON-serialisierbar)
   - `LoginRequest.java`, `LoginResponse.java`
   - `LobbyListRequest.java`, `LobbyListResponse.java`
   - `SpielRequest.java`, `SpielResponse.java`
   - `ZugAnfrage.java`, `ZugAntwort.java` (ja/nein)
   - `SpielfeldUpdate.java` – Sendet Spielstatus an alle Spieler

5. **Alten TCP-Code deaktivieren:**
   - `GameServer-old.java` umbenennen oder aus dem Build ausschließen
   - Port 1078 wird nicht mehr benötigt (WebSocket läuft über Spring Boot Port)

6. **Testen:**
   - Server starten (`mvn spring-boot:run`)
   - Mit einem WebSocket-Client (z.B. `wscat` oder Browser-Konsole) verbinden
   - JSON-Nachrichten senden und empfangen prüfen

## Acceptance Criteria
- [ ] `WebSocketConfig.java` existiert mit STOMP-Broker-Konfiguration
- [ ] `LobbyManager.java` existiert und verwaltet Sessions
- [ ] Protokoll-Klassen (`GameMessage`, `LoginRequest`, etc.) im Shared-Modul
- [ ] Server startet ohne TCP-Listener auf Port 1078
- [ ] WebSocket-Verbindung über `/ws` möglich

## Wichtige Dateien
| Datei | Inhalt |
|---|---|
| `server/config/WebSocketConfig.java` | STOMP-Broker-Konfiguration |
| `server/session/LobbyManager.java` | Session-Management |
| `server/handler/GameWebSocketHandler.java` | WebSocket-Message-Handler |
| `shared/protocol/GameMessage.java` | Protokoll-Basisklasse (JSON) |

## Hinweis
- Das alte TCP-Protokoll (`Protocol.java`) bleibt als Referenz erhalten
- Neue Nachrichten sind JSON-basiert: `{"type":"login","username":"Max"}`
- STOMP wird verwendet für strukturierte Message-Broker-Kommunikation
