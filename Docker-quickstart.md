# Docker Schnellstart-Anleitung

## Schnellstart in 3 Schritten

### 1. Docker Desktop installieren
Lade Docker Desktop herunter und installiere es von: https://www.docker.com/products/docker-desktop/

### 2. Anwendung starten
Öffne ein Terminal im Projektverzeichnis und führe aus:

```bash
docker-compose up --build
```

### 3. Im Browser öffnen
Warte auf die Meldung "Started TerminkalenderApplication" und öffne:
```
http://localhost:8080
```

## Nützliche Befehle

### Starten
```bash
# Erstes Mal oder nach Codeänderungen
docker-compose up --build

# Nachfolgende Starts
docker-compose up

# Im Hintergrund ausführen
docker-compose up -d
```

### Stoppen
```bash
# Container stoppen
docker-compose down

# Container stoppen und Datenbankdaten löschen
docker-compose down -v
```

### Logs anzeigen
```bash
# Alle Logs anzeigen
docker-compose logs -f

# Nur Anwendungs-Logs anzeigen
docker-compose logs -f app

# Nur PostgreSQL-Logs anzeigen
docker-compose logs -f postgres
```

### Auf die Datenbank zugreifen
```bash
# Mit PostgreSQL verbinden
docker exec -it reservations-postgres psql -U reservations_user -d reservations_db

# Innerhalb von psql:
\dt                          # Tabellen anzeigen
SELECT * FROM reservationen; # Reservierungen anzeigen
\q                          # Beenden
```

## Docker-Struktur

### Dienste
- **postgres**: PostgreSQL 16 Datenbank
  - Port: 5432
  - Datenbank: reservations_db
  - Benutzer: reservations_user
  - Passwort: reservations_pass

- **app**: Spring Boot Anwendung
  - Port: 8080
  - Abhängig von postgres

### Volumes
- **postgres_data**: Persistiert die Datenbankdaten
  - Die Daten bleiben erhalten, auch wenn du die Container stoppst
  - Um Daten zu löschen: `docker-compose down -v`

### Netzwerk
- **reservations-network**: Privates Netzwerk für die Kommunikation zwischen Containern

## Konfiguration

### Umgebungsvariablen
Du kannst die Variablen in `docker-compose.yml` ändern:

```yaml
environment:
  POSTGRES_DB: reservations_db
  POSTGRES_USER: reservations_user
  POSTGRES_PASSWORD: reservations_pass
```

### Ports
Um die freigegebenen Ports zu ändern, bearbeite `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"  # App-Port auf 8081 ändern
  - "5433:5432"  # PostgreSQL-Port auf 5433 ändern
```

## Fehlerbehebung

### Port bereits in Verwendung
**Fehler**: "port is already allocated"

**Lösung**: Ändere den Port in docker-compose.yml oder stoppe die Anwendung, die den Port verwendet:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <pid_nummer> /F

# macOS/Linux
lsof -i :8080
kill -9 <pid>
```

### Verbindung zu Docker nicht möglich
**Fehler**: "Cannot connect to the Docker daemon"

**Lösung**: Starte Docker Desktop über das Startmenü

### Anwendung startet nicht
**Lösung**:
```bash
# Detaillierte Logs anzeigen
docker-compose logs app

# Ohne Cache neu erstellen
docker-compose build --no-cache
docker-compose up
```

### Alles löschen und neu beginnen
```bash
# Container, Netzwerke und Volumes stoppen und entfernen
docker-compose down -v

# Images ebenfalls entfernen
docker-compose down -v --rmi all

# Alles neu erstellen
docker-compose up --build
```

## Lokale Entwicklung

### Option 1: Alles in Docker
Am besten für Produktion oder wenn du PostgreSQL nicht lokal installieren möchtest.
```bash
docker-compose up
```

### Option 2: Nur Datenbank in Docker
Am besten für aktive Entwicklung (Hot Reload mit Spring DevTools).

1. Starte nur PostgreSQL:
```bash
docker-compose up postgres
```

2. Starte die App in einem anderen Terminal lokal:
```bash
mvn spring-boot:run
```

### Option 3: Entwicklung mit IDE
1. Starte nur PostgreSQL:
```bash
docker-compose up postgres
```

2. Führe die Anwendung aus deiner IDE aus (IntelliJ IDEA, Eclipse, etc.)

## Architektur

```
┌─────────────────────────────────────────┐
│         Docker Compose                  │
│                                         │
│  ┌────────────┐      ┌──────────────┐  │
│  │    App     │─────▶│  PostgreSQL  │  │
│  │ (Port 8080)│      │  (Port 5432) │  │
│  └────────────┘      └──────────────┘  │
│        │                     │          │
└────────┼─────────────────────┼──────────┘
         │                     │
         │                     │
      Browser             postgres_data
   localhost:8080         (Volume)
```

## Wichtige Dateien

- **Dockerfile**: Definiert, wie das Anwendungs-Image erstellt wird
- **docker-compose.yml**: Orchestriert alle Dienste
- **init.sql**: Initialisierungsskript für die Datenbank
- **application-docker.properties**: Konfiguration für Docker-Umgebung

## Nächste Schritte

1. Öffne http://localhost:8080
2. Erstelle eine neue Reservierung
3. Speichere die generierten Schlüssel
4. Versuche, die Reservierung anzusehen und zu bearbeiten

Für weitere Details konsultiere die Haupt-README.md.
