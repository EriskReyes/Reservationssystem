# Terminkalender - Raumreservationssystem

## Projektbeschreibung

Dies ist eine vollständige Spring Boot Webanwendung für die Verwaltung von Raumreservationen in einem Unternehmensgebäude. Die Anwendung wurde für **Modul 223** entwickelt und ermöglicht das Erstellen, Ansehen, Ändern und Löschen von Reservationen für 5 verschiedene Räume (Zimmer 101-105).

### Hauptfunktionen

- ✅ Raumreservationen erstellen mit Datum, Uhrzeit, Zimmer, Bemerkung und Teilnehmern
- ✅ Validierung aller Eingaben (Datum in der Zukunft, keine Überschneidungen, etc.)
- ✅ Zwei eindeutige Schlüssel pro Reservation:
  - **Private Schlüssel**: Zum Bearbeiten und Löschen
  - **Public Schlüssel**: Nur zum Ansehen (teilbar mit Teilnehmern)
- ✅ Zimmerverfügbarkeitsprüfung
- ✅ Responsive Design
- ✅ PostgreSQL-Datenbankintegration
- ✅ Docker-Support für einfaches Deployment
- ✅ Automatische Test-Daten-Generierung

---

## Technologie-Stack

| Technologie | Version | Beschreibung |
|------------|---------|--------------|
| Java | 21 | Programmiersprache |
| Spring Boot | 3.2.0 | Framework |
| Spring Data JPA | 3.2.0 | Datenbankzugriff |
| Thymeleaf | 3.2.0 | Template Engine |
| PostgreSQL | 16 | Datenbank |
| Docker | 20.0+ | Containerisierung |
| Maven | 3.8+ | Build-Tool |
| Hibernate | 6.2+ | ORM Framework |

---

## Projektstruktur

```
Reservationssystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── terminkalender/
│   │   │           ├── TerminkalenderApplication.java
│   │   │           ├── controller/
│   │   │           │   ├── IndexController.java
│   │   │           │   └── ReservationController.java
│   │   │           ├── model/
│   │   │           │   ├── Reservation.java
│   │   │           │   └── Teilnehmer.java
│   │   │           ├── repository/
│   │   │           │   ├── ReservationRepository.java
│   │   │           │   └── TeilnehmerRepository.java
│   │   │           ├── service/
│   │   │           │   ├── ReservationService.java
│   │   │           │   └── ValidationService.java
│   │   │           └── dto/
│   │   │               └── ReservationDTO.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/
│   │       │   ├── index.html
│   │       │   ├── reservation-formular.html
│   │       │   ├── reservation-bestaetigung.html
│   │       │   ├── reservation-ansicht.html
│   │       │   └── reservation-bearbeiten.html
│   │       └── static/
│   │           └── css/
│   │               └── style.css
│   └── test/
├── Doku/
│   ├── UML/
│   ├── ERM/
│   └── SQL/
├── pom.xml
└── README.md
```

---

## Voraussetzungen

### Option A: Mit Docker (Empfohlen)

**Einfachste Methode - keine manuelle Datenbank-Installation nötig!**

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (inkludiert Docker Compose)

**Überprüfung:**
```bash
docker --version
docker-compose --version
```

### Option B: Ohne Docker (Manuelle Installation)

1. **Java Development Kit (JDK) 21**
   - Download: [Eclipse Temurin](https://adoptium.net/)
   - Überprüfung: `java -version`

2. **PostgreSQL 16+**
   - Download: [PostgreSQL](https://www.postgresql.org/download/)
   - Überprüfung: `psql --version`

3. **Maven 3.8+**
   - Download: [Apache Maven](https://maven.apache.org/download.cgi)
   - Überprüfung: `mvn -version`

4. **IDE (optional)**
   - [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
   - [Visual Studio Code](https://code.visualstudio.com/)

---

## Installation und Ausführung

### Methode 1: Mit Docker (Empfohlen - Am Einfachsten!)

**Alles in einem Befehl starten:**

```bash
cd Reservationssystem
docker-compose up --build
```

Das war's! Die Anwendung läuft jetzt auf:
```
http://localhost:8080
```

**Was passiert automatisch:**
- PostgreSQL-Datenbank wird erstellt und gestartet
- Datenbank-Schema wird initialisiert
- Spring Boot Anwendung wird kompiliert und gestartet
- Alle Verbindungen werden automatisch konfiguriert

**Nützliche Docker-Befehle:**

```bash
# Im Hintergrund starten
docker-compose up -d

# Logs anzeigen
docker-compose logs -f

# Stoppen
docker-compose down

# Stoppen und Daten löschen
docker-compose down -v

# Nur Datenbank starten (für lokale Entwicklung)
docker-compose up postgres
```

### Methode 2: Ohne Docker (Manuelle Installation)

 Schritt 1: Projekt klonen
```bash
cd /pfad/zu/ihrem/workspace
git clone <repository-url>
```

 Schritt 2: PostgreSQL-Datenbank vorbereiten

1. **PostgreSQL starten:**
   ```bash
   # Windows (als Administrator):
   net start postgresql-x64-16

   # macOS/Linux:
   sudo systemctl start postgresql
   ```

2. **Datenbank erstellen:**
   ```bash
   # Als postgres-Benutzer einloggen
   psql -U postgres
   ```

   ```sql
   -- Datenbank und Benutzer erstellen
   CREATE DATABASE reservations_db;
   CREATE USER reservations_user WITH PASSWORD 'reservations_pass';
   GRANT ALL PRIVILEGES ON DATABASE reservations_db TO reservations_user;

   -- Verbindung zur neuen Datenbank
   \c reservations_db

   -- Schema initialisieren (init.sql ausführen)
   \i C:/Users/Rigo Erisk Reyes/IdeaProjects/Reservationssystem/init.sql

   \q
   ```

 Schritt 3: Anwendung starten

```bash
cd Reservationssystem

# Kompilieren und starten
mvn spring-boot:run
```

**ODER in IntelliJ IDEA:**
1. Projekt öffnen
2. Maven Dependencies laden lassen
3. `TerminkalenderApplication.java` ausführen

 Schritt 4: Browser öffnen

```
http://localhost:8080
```

---

## Verwendung der Anwendung

### 1. Neue Reservation erstellen

1. Klicken Sie auf der Hauptseite auf **"Reservationen erfassen"**
2. Füllen Sie alle Pflichtfelder aus:
   - **Datum**: Muss in der Zukunft liegen
   - **Von/Bis**: Startzeit muss vor Endzeit liegen
   - **Zimmer**: Wählen Sie aus 101-105
   - **Bemerkung**: 10-200 Zeichen
   - **Teilnehmer**: Format: "Vorname Nachname, Vorname Nachname"
3. Klicken Sie auf **"Reservation erstellen"**
4. **Speichern Sie beide Schlüssel!**
   - Private Schlüssel: Zum Bearbeiten/Löschen
   - Public Schlüssel: Zum Teilen mit Teilnehmern

### 2. Bestehende Reservation aufrufen

1. Geben Sie auf der Hauptseite einen Schlüssel ein
2. Je nach Schlüssel-Typ werden Sie weitergeleitet:
   - **Private Schlüssel** → Bearbeitungsseite
   - **Public Schlüssel** → Nur-Lese-Ansicht

### 3. Reservation bearbeiten

1. Geben Sie den **Private Schlüssel** ein
2. Ändern Sie die gewünschten Felder
3. Klicken Sie auf **"Änderungen speichern"**

### 4. Reservation löschen

1. Geben Sie den **Private Schlüssel** ein
2. Scrollen Sie nach unten zu "Reservation löschen"
3. Klicken Sie auf **"Reservation löschen"**
4. Bestätigen Sie die Löschung

---

## Test-Daten

Die Anwendung erstellt beim ersten Start automatisch 3 Test-Reservationen. Die Schlüssel werden in der Konsole ausgegeben:

```
INFO  c.t.service.ReservationService - Test-Reservation 1 erstellt - Private Schlüssel: abc-123-def
INFO  c.t.service.ReservationService - Test-Reservation 2 erstellt - Private Schlüssel: ghi-456-jkl
INFO  c.t.service.ReservationService - Test-Reservation 3 erstellt - Private Schlüssel: mno-789-pqr
```

---

## Validierungsregeln

Die Anwendung validiert alle Eingaben:

| Feld | Regel |
|------|-------|
| Datum | Muss in der Zukunft liegen |
| Von-Zeit | Muss vor Bis-Zeit liegen |
| Bis-Zeit | Mindestens 15 Minuten nach Von-Zeit |
| Zimmer | Muss 101, 102, 103, 104 oder 105 sein |
| Zimmer | Darf nicht zu dieser Zeit bereits reserviert sein |
| Bemerkung | 10-200 Zeichen, alphanumerisch |
| Teilnehmer | Format: "Vorname Nachname", nur Buchstaben |

---

## Konfigurationsdateien

### application.properties
Für lokale Entwicklung (ohne Docker):
- Verbindet zu `localhost:5432`
- Verwendet Datenbank `reservations_db`

### application-docker.properties
Für Docker-Umgebung:
- Verbindet zu `postgres:5432` (Docker-Netzwerk)
- Konfiguration über Umgebungsvariablen

## Fehlerbehebung

### Docker-Probleme

**Problem: Port 8080 oder 5432 bereits belegt**
```bash
# Ports prüfen (Windows)
netstat -ano | findstr :8080
netstat -ano | findstr :5432

# Ports prüfen (macOS/Linux)
lsof -i :8080
lsof -i :5432

# Lösung: Ports im docker-compose.yml ändern
```

**Problem: "Cannot connect to Docker daemon"**
```bash
# Docker Desktop starten
# Windows: Docker Desktop aus Startmenü öffnen
# macOS/Linux: systemctl start docker
```

**Problem: Container startet nicht**
```bash
# Logs prüfen
docker-compose logs app
docker-compose logs postgres

# Container neu erstellen
docker-compose down
docker-compose up --build
```

### PostgreSQL-Probleme

**Problem: Datenbankverbindung schlägt fehl**
```bash
# Prüfen ob PostgreSQL läuft
# Windows
sc query postgresql-x64-16

# macOS/Linux
sudo systemctl status postgresql

# Verbindung testen
psql -U reservations_user -d reservations_db -h localhost
```

**Problem: "relation does not exist"**
```bash
# Schema manuell initialisieren
psql -U reservations_user -d reservations_db -f init.sql
```

### Allgemeine Probleme

**Problem: Port 8080 bereits belegt**

In `application.properties` oder `docker-compose.yml` ändern:
```properties
server.port=8081
```

**Problem: Maven Build schlägt fehl**
```bash
# Maven Clean durchführen
mvn clean install -U
```

---

## Datenbankschema

### Tabelle: reservationen
| Spalte | Typ | Beschreibung |
|--------|-----|--------------|
| id | BIGINT | Primärschlüssel |
| datum | DATE | Reservationsdatum |
| von_zeit | TIME | Startzeit |
| bis_zeit | TIME | Endzeit |
| zimmer | INTEGER | Zimmernummer (101-105) |
| bemerkung | VARCHAR(200) | Beschreibung |
| private_schluessel | VARCHAR(36) | UUID für Bearbeitung |
| public_schluessel | VARCHAR(36) | UUID für Ansicht |
| erstellt_am | TIMESTAMP | Erstellungszeitpunkt |
| aktualisiert_am | TIMESTAMP | Änderungszeitpunkt |

### Tabelle: teilnehmer
| Spalte | Typ | Beschreibung |
|--------|-----|--------------|
| id | BIGINT | Primärschlüssel |
| vorname | VARCHAR(50) | Vorname |
| nachname | VARCHAR(50) | Nachname |
| reservation_id | BIGINT | Fremdschlüssel zu reservationen |

---

## API-Endpunkte

| Methode | Pfad | Beschreibung |
|---------|------|--------------|
| GET | `/` | Hauptseite |
| GET | `/reservation/neu` | Formular für neue Reservation |
| POST | `/reservation/erstellen` | Reservation erstellen |
| GET | `/reservation/bestaetigung/{id}` | Bestätigung mit Schlüsseln |
| GET | `/reservation/ansicht/{publicSchluessel}` | Öffentliche Ansicht |
| GET | `/reservation/bearbeiten/{privateSchluessel}` | Bearbeitungsansicht |
| POST | `/reservation/aktualisieren/{privateSchluessel}` | Reservation aktualisieren |
| POST | `/reservation/loeschen/{privateSchluessel}` | Reservation löschen |
| POST | `/schluessel-suche` | Schlüsselsuche |

---

## Zusätzliche Hinweise

### Produktiv-Deployment

Für ein Produktiv-System sollten Sie:

1. **Sicherere Datenbankkonfiguration**:
   ```properties
   spring.datasource.username=secure_user
   spring.datasource.password=${DB_PASSWORD}
   ```

2. **HTTPS aktivieren**
3. **Logging konfigurieren**
4. **Backup-Strategie implementieren**

### Performance-Optimierung

- Verwenden Sie Connection Pooling (HikariCP ist bereits inkludiert)
- Aktivieren Sie Caching für häufig abgerufene Daten
- Erstellen Sie Indizes für häufige Abfragen

### Weitere Entwicklung

Mögliche Erweiterungen:

- E-Mail-Benachrichtigungen für Teilnehmer
- Kalenderansicht (Monats-/Wochenansicht)
- Export als PDF oder iCal
- Benutzer-Authentifizierung
- REST API für mobile Apps

---

## Support und Kontakt

- **Projektdokumentation**: Siehe `/Doku` Ordner
- **Issues**: Kontaktieren Sie Ihren Modulverantwortlichen
- **Version**: 1.0.0

---

## Lizenz

Dieses Projekt wurde für Bildungszwecke (Modul 223) erstellt.

---

## Autoren

- Entwickelt für Modul 223
- Spring Boot 3.2.0
- Stand: 2025
- eriskreyes/denisperdomo


