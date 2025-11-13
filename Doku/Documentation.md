# Dokumentation des Reservierungssystems

## Inhaltsverzeichnis
- [Einleitung](#einleitung)
- [Was macht diese Anwendung?](#was-macht-diese-anwendung)
- [Technologien die wir verwenden](#technologien-die-wir-verwenden)
- [Wie ist das Projekt organisiert](#wie-ist-das-projekt-organisiert)
- [Die Datenbank](#die-datenbank)
- [Diagramme](#diagramme)
  - [ERM-Diagramm](#1-erm-diagramm-entity-relationship-diagramm)
  - [UML-Klassendiagramm](#2-uml-klassendiagramm)
  - [UML-Zustandsdiagramm](#3-uml-zustandsdiagramm)
- [Der Ablauf der Anwendung](#der-ablauf-der-anwendung)
- [Konfiguration und Umgebungen](#konfiguration-und-umgebungen)
- [Wie startet man das Projekt](#wie-startet-man-das-projekt)
- [Die wichtigsten Teile des Codes](#die-wichtigsten-teile-des-codes)
- [Was wir gelernt haben und Entscheidungen die wir getroffen haben](#was-wir-gelernt-haben-und-entscheidungen-die-wir-getroffen-haben)
- [Häufige Probleme und wie man sie löst](#häufige-probleme-und-wie-man-sie-löst)

---

## Einleitung

Also, dieses Projekt hat als Teil des Moduls 223 angefangen. Im Grunde brauchten wir ein System, mit dem Leute Besprechungsräume reservieren können. In der Firma, für die das gedacht ist, gibt es 5 Räume (Zimmer 101 bis 105) und sie wollten etwas Einfaches aber Funktionelles, damit die Mitarbeiter Reservierungen machen können, ohne sich das Leben schwer zu machen.

Die Hauptidee war, dass wir uns nicht mit Benutzern, Passwörtern und dem ganzen Login-Kram herumschlagen wollten. Stattdessen haben wir ein ziemlich cleveres "Schlüssel"-System implementiert: Wenn du eine Reservierung erstellst, bekommst du zwei einzigartige Schlüssel - einen privaten (zum Bearbeiten) und einen öffentlichen (nur zum Ansehen). Du speicherst deinen Schlüssel und fertig, du musst dir keine Passwörter merken oder so.

---

## Was macht diese Anwendung?

Kurz gesagt, ist es ein gemeinsamer Kalender zum Reservieren von Räumen. Hier ist, was du machen kannst:

### Eine Reservierung erstellen
1. Du kommst auf die Startseite
2. Du klickst auf "Neue Reservierung"
3. Du füllst das Formular aus mit:
   - Dem Datum, das du brauchst (muss natürlich in der Zukunft liegen)
   - Start- und Endzeit (mindestens 15 Minuten)
   - Welchen Raum du willst (101, 102, 103, 104 oder 105)
   - Eine Bemerkung oder Beschreibung, wofür das Meeting ist
   - Die Namen der Personen, die teilnehmen werden
4. Das System prüft automatisch, ob der Raum zu dieser Zeit frei ist
5. Wenn alles passt, erstellt es die Reservierung und gibt dir deine zwei magischen Schlüssel

### Eine Reservierung ansehen
Wenn dir jemand seinen öffentlichen Schlüssel gibt, kannst du alle Details der Reservierung sehen, aber nichts ändern. Es ist sozusagen ein Nur-Lese-Modus.

### Bearbeiten oder Stornieren
Mit dem privaten Schlüssel kannst du reingehen und alles ändern oder sogar die ganze Reservierung stornieren, wenn du sie nicht mehr brauchst.

### Das Clevere am System
Es hat einen Echtzeit-Verfügbarkeitschecker. Während du das Formular ausfüllst, sagt es dir, ob der Raum frei ist oder nicht. Nichts ist schlimmer, als das ganze Formular auszufüllen, nur um am Ende zu erfahren, dass der Raum schon belegt ist, oder?

---

## Technologien die wir verwenden

### Das Herzstück: Spring Boot
Wir haben uns für Spring Boot 3.2.0 entschieden, weil es nun mal der De-facto-Standard für Java-Anwendungen heutzutage ist. Außerdem kommt es mit einem Haufen vorkonfigurierter Sachen, die einem Zeit sparen.

**Warum Spring Boot?**
- Es startet schnell (wirklich, in Sekunden läuft dein Server)
- Es hat alles integriert: Webserver, Datenbankverwaltung, Templates
- Die Community ist riesig, wenn du nicht weiterkommst, hat jemand das Problem schon gelöst
- Es ist einfach zu deployen, sowohl lokal als auch mit Docker

### Java 21
Ja, wir nutzen Version 21 von Java. Warum so neu? Weil es Performance-Verbesserungen hat und einige Features, die uns das Leben leichter gemacht haben, wie Records (obwohl wir die in diesem Projekt nicht viel genutzt haben) und bessere Fehlermeldungen.

### PostgreSQL
Für die Datenbank haben wir PostgreSQL gewählt. Wir hätten MySQL oder H2 nehmen können, aber Postgres ist super robust und hat eine sehr gute Handhabung von Datum und Uhrzeit, was für ein Reservierungssystem entscheidend ist.

### Thymeleaf für die Views
Anstatt eine SPA (Single Page Application) mit React oder Vue zu machen, haben wir uns für den traditionellen Weg mit Thymeleaf entschieden. Wir rendern alles auf dem Server. Warum? Weil:
- Es einfacher ist für diese Art von Anwendung
- Wir keine separate REST-API brauchen
- SEO automatisch funktioniert (obwohl es hier nicht so wichtig ist)
- Weniger JavaScript = weniger Probleme

### Bootstrap 5
Damit es gut aussieht, ohne dass wir Grafikdesigner sein müssen. Bootstrap hat uns fertige Komponenten und ein Grid-System gegeben, das dafür sorgt, dass alles auf Handy und Desktop gut aussieht.

### Maven
Zum Verwalten der Abhängigkeiten und des Builds. Gradle ist moderner, aber Maven ist immer noch das am meisten verwendete und das, was alle kennen.

### Docker
Damit jeder das Projekt starten kann, ohne PostgreSQL auf seiner Maschine installieren zu müssen. Docker Compose startet alles mit einem einzigen Befehl.

---

## Wie ist das Projekt organisiert

### Die Ordnerstruktur

```
Reservationssystem/
│
├── src/
│   ├── main/
│   │   ├── java/com/terminkalender/
│   │   │   ├── TerminkalenderApplication.java    # Der Main, wo alles startet
│   │   │   │
│   │   │   ├── config/                           # Konfigurationen
│   │   │   │   └── DataInitializer.java          # Erstellt Testdaten
│   │   │   │
│   │   │   ├── controller/                       # Die Web-Controller
│   │   │   │   ├── IndexController.java          # Startseite und Suche
│   │   │   │   └── ReservationController.java    # Alles rund um Reservierungen
│   │   │   │
│   │   │   ├── dto/                              # Objekte zum Datentransfer
│   │   │   │   └── ReservationDTO.java           # Das Reservierungsformular
│   │   │   │
│   │   │   ├── model/                            # Die DB-Entitäten
│   │   │   │   ├── Reservation.java              # Die Reservierung
│   │   │   │   └── Teilnehmer.java               # Die Teilnehmer
│   │   │   │
│   │   │   ├── repository/                       # Für die DB-Kommunikation
│   │   │   │   └── ReservationRepository.java
│   │   │   │
│   │   │   └── service/                          # Die Business-Logik
│   │   │       ├── ReservationService.java       # Hauptoperationen
│   │   │       └── ValidationService.java        # Validierungen
│   │   │
│   │   └── resources/
│   │       ├── templates/                        # Die HTML-Seiten
│   │       │   ├── index.html                    # Startseite
│   │       │   ├── reservation-formular.html     # Formular für neue Reservierung
│   │       │   ├── reservation-bestaetigung.html # Bestätigung
│   │       │   ├── reservation-ansicht.html      # Öffentliche Ansicht
│   │       │   └── reservation-bearbeiten.html   # Bearbeiten
│   │       │
│   │       ├── static/css/
│   │       │   └── style.css                     # Custom Styles
│   │       │
│   │       ├── application.properties             # Hauptkonfiguration
│   │       ├── application-dev.properties         # Für Entwicklung
│   │       ├── application-docker.properties      # Für Docker
│   │       └── application-prod.properties        # Für Produktion
│   │
│   └── test/                                      # Tests (hier könnte man mehr hinzufügen)
│
├── Doku/                                          # Technische Dokumentation
│   ├── ERM-Diagramm.puml                         # Datenbankdiagramm
│   ├── UML-Klassendiagramm.puml                  # Klassendiagramm
│   └── UML-Zustandsdiagramm.puml                 # Zustandsdiagramm
│
├── docker-compose.yml                             # Docker-Konfiguration
├── Dockerfile                                     # Zum Bauen des Images
├── init.sql                                       # Initiales DB-Script
├── pom.xml                                        # Maven-Abhängigkeiten
│
└── *.cmd                                          # Scripts für Windows
```

### Warum diese Struktur?

Das ist die Standard-Struktur von Spring Boot, und wir folgen ihr, weil:
1. Jeder Java-Entwickler sie sofort erkennt
2. Sie die Verantwortlichkeiten klar trennt (Controller, Services, Repositories)
3. IDEs sie automatisch verstehen
4. Es einfach ist, Sachen zu finden

---

## Die Datenbank

### Die zwei Haupttabellen

**Tabelle: reservationen**
Das ist die Haupttabelle. Jede Zeile ist eine Raumreservierung.

```
Feld                  Typ             Was es ist
-------------------------------------------------------------------
id                   BIGSERIAL       Die eindeutige ID der Reservierung
datum                DATE            Das Datum der Reservierung
von_zeit             TIME            Startzeit
bis_zeit             TIME            Endzeit
zimmer               INTEGER         Raumnummer (101-105)
bemerkung            VARCHAR(200)    Notizen oder Beschreibung
private_schluessel   VARCHAR(36)     Der private Schlüssel (UUID)
public_schluessel    VARCHAR(36)     Der öffentliche Schlüssel (UUID)
erstellt_am          TIMESTAMP       Wann wurde es erstellt
aktualisiert_am      TIMESTAMP       Letzte Änderung
```

**Tabelle: teilnehmer**
Hier speichern wir, wer an jedem Meeting teilnimmt.

```
Feld            Typ            Was es ist
-------------------------------------------------------------------
id              BIGSERIAL       ID des Teilnehmers
vorname         VARCHAR(50)     Vorname
nachname        VARCHAR(50)     Nachname
reservation_id  BIGINT          Zu welcher Reservierung gehört er (FK)
```

### Die Beziehung zwischen den Tabellen

Es ist eine Eins-zu-viele-Beziehung: Eine Reservierung kann viele Teilnehmer haben, aber jeder Teilnehmer gehört zu einer einzigen Reservierung.

Das Wichtige hier ist, dass wir `ON DELETE CASCADE` konfiguriert haben, was bedeutet, dass wenn du eine Reservierung löschst, automatisch alle ihre Teilnehmer gelöscht werden. Das erspart uns die manuelle Aufräumarbeit.

### Die Indizes

Wir haben Indizes erstellt auf:
- `datum` und `zimmer`: weil wir immer nach Reservierungen nach Datum und Raum suchen
- `private_schluessel` und `public_schluessel`: damit die Suche nach Schlüssel blitzschnell ist
- `reservation_id` in teilnehmer: damit die Joins effizient sind

### Der Timestamp-Trigger

Es gibt einen Trigger, der automatisch `aktualisiert_am` aktualisiert, jedes Mal wenn du eine Reservierung änderst. So müssen wir nicht daran denken, es manuell im Code zu machen.

---

## Diagramme

In der Ordner `Doku/` findest du drei wichtige PlantUML-Diagramme, die die Architektur und Struktur der Anwendung visualisieren. Diese Diagramme helfen dir, das System besser zu verstehen und sind nützlich für neue Entwickler im Team.

### 1. ERM-Diagramm (Entity-Relationship-Diagramm)
**Datei:** `Doku/ERM-Diagramm.puml`

Das Entity-Relationship-Diagramm zeigt die Datenbankstruktur und die Beziehungen zwischen den Tabellen:

**Was es zeigt:**
- Die beiden Haupttabellen: `reservationen` und `teilnehmer`
- Alle Felder mit ihren Datentypen (BIGSERIAL, DATE, TIME, VARCHAR, etc.)
- Primary Keys, Foreign Keys und Unique Constraints
- Die One-to-Many Beziehung zwischen Reservationen und Teilnehmern
- Alle Indizes für Performance-Optimierung
- CHECK Constraints (z.B. Zimmer muss zwischen 101 und 105 sein)
- CASCADE-Verhalten beim Löschen und Aktualisieren

**Warum ist es nützlich?**
- Verstehe auf einen Blick die komplette Datenbankstruktur
- Sieh, wie die Tabellen miteinander verbunden sind
- Erkenne welche Felder erforderlich (NOT NULL) sind
- Verstehe die Validierungsregeln auf Datenbankebene

### 2. UML-Klassendiagramm
**Datei:** `Doku/UML-Klassendiagramm.puml`

Das Klassendiagramm zeigt die vollständige Java-Architektur mit allen Klassen und ihren Beziehungen:

**Was es zeigt:**
- **Controller-Schicht** (grün): `IndexController` und `ReservationController`
  - Alle Endpunkte und ihre Methoden
  - Welche Parameter sie empfangen
  - Welche Views sie zurückgeben

- **Service-Schicht** (blau): `ReservationService` und `ValidationService`
  - Die komplette Business-Logik
  - Alle Validierungsmethoden
  - Methoden zum Erstellen, Aktualisieren, Löschen von Reservierungen

- **Repository-Schicht** (gelb): `ReservationRepository` und `TeilnehmerRepository`
  - Alle Custom-Queries
  - Die Datenbankzugriffsmethoden

- **Model-Schicht** (rot): `Reservation` und `Teilnehmer`
  - Die JPA-Entitäten mit allen Attributen
  - Getter, Setter und Hilfsmethoden

- **DTO-Schicht** (beige): `ReservationDTO`
  - Das Datentransferobjekt für Formulare

**Die Beziehungen:**
- Pfeile zeigen, welche Klasse welche andere verwendet
- Dependency-Injection zwischen Controllern und Services
- Die One-to-Many Beziehung zwischen Reservation und Teilnehmer

**Warum ist es nützlich?**
- Verstehe die Layered Architecture (Schichtenarchitektur)
- Sieh den Datenfluss: Controller → Service → Repository → Database
- Erkenne die Trennung von Verantwortlichkeiten (Separation of Concerns)
- Finde schnell, in welcher Klasse welche Methode ist

### 3. UML-Zustandsdiagramm
**Datei:** `Doku/UML-Zustandsdiagramm.puml`

Das Zustandsdiagramm zeigt die Navigation und den Benutzerfluss durch die Anwendung:

**Was es zeigt:**
- Alle 5 Hauptseiten der Anwendung:
  1. **Startseite** (blau) - Der Einstiegspunkt
  2. **Neue Reservation** (grün) - Das Formular
  3. **Bestätigung** (hellgrün) - Nach erfolgreichem Erstellen
  4. **Öffentliche Ansicht** (gelb) - Mit Public Key
  5. **Bearbeitungsansicht** (rot) - Mit Private Key

- Alle möglichen Übergänge zwischen den Seiten
- Was passiert bei Fehlern (z.B. Validierung fehlgeschlagen)
- Die verschiedenen Aktionen (Button-Klicks, Formular-Submit, etc.)

**Die wichtigsten Flüsse:**
1. **Neue Reservierung erstellen:**
   Startseite → Neue Reservation → (Validierung) → Bestätigung → Startseite

2. **Reservierung ansehen (Public Key):**
   Startseite → Öffentliche Ansicht → Startseite

3. **Reservierung bearbeiten (Private Key):**
   Startseite → Bearbeitungsansicht → (Änderungen) → Bearbeitungsansicht

4. **Reservierung löschen:**
   Startseite → Bearbeitungsansicht → Löschen → Startseite

**Warum ist es nützlich?**
- Verstehe den kompletten User Journey
- Sieh, welche Seiten zu welchen führen
- Erkenne die verschiedenen Pfade durch die Anwendung
- Verstehe das Verhalten bei Fehlerfällen

### Wie du die Diagramme anschaust

**Option 1: PlantUML-Plugin (empfohlen)**

Installiere das PlantUML-Plugin für deine IDE:
- **IntelliJ IDEA**: Settings → Plugins → PlantUML Integration
- **VS Code**: Extension "PlantUML"
- **Eclipse**: Help → Eclipse Marketplace → PlantUML

Dann einfach die `.puml` Dateien öffnen und das Diagramm wird automatisch gerendert.

**Option 2: Online PlantUML Editor**

1. Gehe zu http://www.plantuml.com/plantuml/uml/
2. Kopiere den Inhalt der `.puml` Datei
3. Füge ihn ein und das Diagramm wird generiert

**Option 3: Command Line (mit PlantUML installiert)**

```bash
# Generiere PNG-Images aus den PUML-Dateien
java -jar plantuml.jar Doku/*.puml
```

Dies erstellt `.png` Dateien für jedes Diagramm.

### Warum verwenden wir PlantUML?

PlantUML ist ein Tool zum Erstellen von Diagrammen aus einfachem Text. Die Vorteile:
- **Versionskontrolle**: Du kannst die Änderungen im Diagramm im Git-History sehen
- **Einfach zu bearbeiten**: Kein spezielles Tool nötig, nur einen Texteditor
- **Konsistent**: Alle Diagramme sehen professionell aus
- **Automatisch generiert**: Das Layout wird automatisch berechnet
- **Kollaboration**: Mehrere Entwickler können gleichzeitig an Diagrammen arbeiten

**Wann solltest du die Diagramme aktualisieren?**
- Wenn du neue Tabellen oder Felder zur Datenbank hinzufügst → ERM-Diagramm
- Wenn du neue Klassen, Methoden oder Services erstellst → Klassendiagramm
- Wenn du neue Seiten oder Navigation hinzufügst → Zustandsdiagramm

---

## Der Ablauf der Anwendung

### Ablauf 1: Eine neue Reservierung erstellen

1. **Du kommst auf die Startseite** (`/`)
   - Du siehst einen großen Button "Neue Reservierung"
   - Oder du kannst eine bestehende Reservierung mit deinem Schlüssel suchen

2. **Du klickst auf Neue Reservierung** (führt zu `/reservation/neu`)
   - Der `ReservationController` zeigt dir das leere Formular
   - Lädt `reservation-formular.html`

3. **Du füllst das Formular aus**
   - Während du Datum, Zeit und Raum auswählst...
   - JavaScript macht einen AJAX-Aufruf an `/reservation/verfuegbarkeit`
   - Der Server prüft, ob es Konflikte gibt
   - Es zeigt dir in Echtzeit, ob es verfügbar ist oder nicht

4. **Du drückst Submit** (POST an `/reservation/erstellen`)
   - Der Controller empfängt dein `ReservationDTO`
   - Er schickt es zuerst an den `ValidationService`
   - Wenn es die Validierungen besteht, geht es zum `ReservationService`
   - Der Service:
     - Generiert zwei UUIDs (private und öffentliche Schlüssel)
     - Parst die Teilnehmerliste
     - Erstellt die `Reservation` und `Teilnehmer` Objekte
     - Speichert alles in der DB (die Teilnehmer werden automatisch durch Cascade gespeichert)

5. **Du bekommst deine Bestätigung** (Redirect zu `/reservation/bestaetigung/{id}`)
   - Es zeigt dir deine zwei Schlüssel groß an
   - Es erinnert dich daran, sie zu speichern
   - Es gibt dir Links, um deine Reservierung anzusehen oder zu bearbeiten

### Ablauf 2: Eine Reservierung suchen und ansehen

1. **Auf der Startseite** gibst du deinen Schlüssel ein und drückst Suchen
   - POST an `/schluessel-suche`
   - Der `IndexController` empfängt den Schlüssel

2. **Das System identifiziert, welche Art von Schlüssel es ist**
   - Sucht zuerst in den private keys
   - Wenn nicht gefunden, sucht in den public keys
   - Wenn keiner gefunden wird, sagt es dir, dass der Schlüssel nicht existiert

3. **Es leitet dich entsprechend um**
   - Privater Schlüssel → `/reservation/bearbeiten/{privateKey}` (du kannst bearbeiten)
   - Öffentlicher Schlüssel → `/reservation/ansicht/{publicKey}` (nur Leserecht)

### Ablauf 3: Eine Reservierung bearbeiten

1. **Du kommst mit deinem privaten Schlüssel** zu `/reservation/bearbeiten/{privateKey}`
   - Der Controller sucht die Reservierung in der DB
   - Wenn sie nicht existiert, Fehler 404
   - Wenn sie existiert, lädt das vorgefüllte Formular

2. **Du änderst, was du brauchst** und drückst Update
   - POST an `/reservation/aktualisieren/{privateKey}`
   - Validiert alles erneut (Datum, Zeit, Raum, etc.)
   - Prüft Verfügbarkeit (schließt deine eigene Reservierung vom Check aus)
   - Löscht die alten Teilnehmer und erstellt die neuen
   - Aktualisiert den Timestamp automatisch

3. **Es bestätigt dir**, dass es aktualisiert wurde und bringt dich zurück zur Bearbeitungsansicht

### Ablauf 4: Eine Reservierung stornieren

1. **Auf der Bearbeitungsseite** drückst du den Button "Löschen"
   - JavaScript fragt nach Bestätigung (damit du nicht aus Versehen löschst)

2. **Wenn du bestätigst** macht es POST an `/reservation/loeschen/{privateKey}`
   - Der Service löscht die Reservierung
   - Durch CASCADE werden auch alle Teilnehmer gelöscht

3. **Es leitet dich um** zur Startseite mit einer Erfolgsmeldung

---

## Konfiguration und Umgebungen

### Die verschiedenen Profile

Wir haben drei Umgebungen konfiguriert:

#### Entwicklung (dev)
- Läuft auf `localhost:8080`
- Datenbank: `localhost:5432`
- DB-Benutzer: `reservations_user`
- **DDL auto: update** - Die Datenbank aktualisiert sich selbst mit den Code-Änderungen
- **Show SQL: true** - Du siehst alle SQL-Queries in der Konsole (nützlich fürs Debugging)
- Logging: DEBUG (du siehst alles, was passiert)
- Testdaten werden beim Start automatisch geladen

**Wann dev verwenden?**
Wenn du auf deinem Computer entwickelst. Alles ist ausführlicher, damit du sehen kannst, was passiert.

#### Docker
- Läuft in Containern
- Datenbank: `postgres:5432` (der Servicename in Docker)
- **DDL auto: validate** - Ändert die DB nicht, validiert nur, dass sie mit dem Code übereinstimmt
- Show SQL: false (um Logs nicht unnötig zu füllen)
- Logging: INFO
- Die DB wird mit `init.sql` initialisiert

**Wann Docker verwenden?**
Wenn du die Anwendung "wie in Produktion" testen willst, aber ohne PostgreSQL auf deiner Maschine installieren zu müssen. Auch um das Projekt einfach mit anderen zu teilen.

#### Produktion (prod)
- Ähnlich wie Docker, aber optimierter
- Minimales Logging (nur wichtige Fehler)
- Keine Testdaten

### Umgebungsvariablen

Das Projekt nutzt Umgebungsvariablen für sensible Konfigurationen:

```properties
DB_USERNAME=${DB_USERNAME:postgres}
DB_PASSWORD=${DB_PASSWORD:postgres}
SERVER_PORT=${SERVER_PORT:8080}
SPRING_PROFILES=${SPRING_PROFILES:dev}
DDL_AUTO=${DDL_AUTO:validate}
```

Das Format `${VARIABLE:default}` bedeutet: "nutze die Umgebungsvariable, aber wenn sie nicht existiert, nutze diesen Standardwert".

**Warum so?**
- Du commitest keine Passwörter ins Repository
- Du kannst Konfigurationen ändern, ohne neu zu kompilieren
- Es ist flexibler für verschiedene Umgebungen

---

## Wie startet man das Projekt

### Option 1: Mit Docker (am einfachsten)

Wenn du Docker installiert hast, ist es super einfach:

```bash
docker-compose up
```

Das ist alles. Docker wird:
1. Das PostgreSQL-Image herunterladen, wenn du es nicht hast
2. Die Datenbank erstellen und `init.sql` ausführen
3. Die Java-Anwendung bauen
4. Alles starten
5. In ein paar Minuten ist es bereit unter http://localhost:8080

Zum Stoppen:
```bash
docker-compose down
```

Wenn du auch die Datenbank löschen willst:
```bash
docker-compose down -v
```

### Option 2: Lokal (mehr Kontrolle)

Wenn du lieber alles auf deiner Maschine laufen lässt:

**Schritt 1: Die Datenbank**

Du brauchst PostgreSQL installiert. Dann:

```bash
# Verbinde dich mit PostgreSQL
psql -U postgres

# Erstelle die Datenbank und den Benutzer
CREATE DATABASE reservations_db;
CREATE USER reservations_user WITH PASSWORD 'reservations_pass';
GRANT ALL PRIVILEGES ON DATABASE reservations_db TO reservations_user;

# Verlasse psql
\q

# Führe das Initialisierungsscript aus
psql -U reservations_user -d reservations_db -f init.sql
```

**Schritt 2: Die Anwendung**

```bash
# Kompiliere das Projekt
mvn clean package

# Starte die Anwendung im dev-Modus
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Oder wenn du das JAR direkt ausführen willst:

```bash
java -jar target/terminkalender-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

**Schritt 3: Öffne den Browser**

Das System öffnet automatisch `http://localhost:8080` beim Start. Wenn es sich nicht von selbst öffnet, kannst du manuell zu dieser URL gehen.

### Option 3: Aus der IDE

Wenn du IntelliJ IDEA oder Eclipse nutzt:

1. Importiere das Projekt als Maven-Projekt
2. Konfiguriere dein JDK 21
3. Finde `TerminkalenderApplication.java`
4. Rechtsklick → Run
5. Stelle sicher, dass das aktive Profil `dev` ist

### Die Windows-Scripts (.cmd)

Wenn du auf Windows bist, gibt es ein paar `.cmd` Dateien, die dir das Leben leichter machen:

- `Starten-Alles.cmd` - Startet Docker Compose
- `postgres-starten.cmd` - Nur die DB
- `initialisieren-db.cmd` - Initialisiert die DB
- `stoppen-postgres.cmd` - Stoppt alles

Einfach auf die Datei doppelklicken, die du brauchst.

---

## Die wichtigsten Teile des Codes

### ReservationService.java
Das ist das Gehirn der Anwendung. Hier passieren alle wichtigen Operationen.

**Die Methode zum Erstellen einer Reservierung:**
```java
public Reservation erstelleReservation(ReservationDTO dto) throws ValidationException {
    // Zuerst validieren wir alles
    validationService.validiereReservation(dto);

    // Wir prüfen, ob der Raum frei ist
    if (!istZimmerVerfuegbar(dto.getZimmer(), dto.getDatum(),
                             dto.getVonZeit(), dto.getBisZeit(), null)) {
        throw new ValidationException("Raum besetzt");
    }

    // Wir erstellen die Reservierung
    Reservation reservation = new Reservation();
    // ... setters ...

    // Die magischen Schlüssel
    reservation.setPrivateKey(UUID.randomUUID().toString());
    reservation.setPublicKey(UUID.randomUUID().toString());

    // Wir parsen die Teilnehmer
    parseTeilnehmer(dto.getTeilnehmerListe(), reservation);

    // Wir speichern (die Teilnehmer werden automatisch durch Cascade gespeichert)
    return repository.save(reservation);
}
```

**Warum UUID für die Schlüssel?**
UUIDs haben 36 Zeichen und sind praktisch unmöglich zu erraten. Zum Beispiel: `550e8400-e29b-41d4-a716-446655440000`. Es ist viel sicherer als eine einfache numerische ID.

**Der Verfügbarkeitschecker:**
```java
public boolean istZimmerVerfuegbar(Integer zimmer, LocalDate datum,
                                    LocalTime von, LocalTime bis, Long excludeId) {
    List<Reservation> overlapping;

    if (excludeId == null) {
        // Neue Reservierung
        overlapping = repository.findOverlappingReservations(zimmer, datum, von, bis);
    } else {
        // Aktualisierung, wir schließen die aktuelle Reservierung aus
        overlapping = repository.findOverlappingReservationsExcludingId(
            zimmer, datum, von, bis, excludeId);
    }

    return overlapping.isEmpty();
}
```

Diese Methode ist entscheidend, weil sie Doppelbuchungen verhindert. Sie sucht in der DB, ob es eine Reservierung gibt, die sich zeitlich überschneidet.

### ValidationService.java
Hier validieren wir alles, bevor wir speichern.

**Die Validierung von Datum und Zeit:**
```java
public void validiereDatumUndZeit(LocalDate datum, LocalTime von, LocalTime bis)
        throws ValidationException {
    // Das Datum muss in der Zukunft liegen
    if (datum.isBefore(LocalDate.now())) {
        throw new ValidationException("Du kannst nicht in der Vergangenheit reservieren");
    }

    // Die Startzeit muss vor der Endzeit liegen
    if (!von.isBefore(bis)) {
        throw new ValidationException("Die Startzeit muss vor der Endzeit liegen");
    }

    // Mindestens 15 Minuten Meeting
    if (Duration.between(von, bis).toMinutes() < 15) {
        throw new ValidationException("Das Meeting muss mindestens 15 Minuten dauern");
    }
}
```

**Die Validierung der Teilnehmer:**
```java
public List<String[]> validiereUndParseTeilnehmer(String teilnehmerString)
        throws ValidationException {
    if (teilnehmerString == null || teilnehmerString.trim().isEmpty()) {
        throw new ValidationException("Du brauchst mindestens einen Teilnehmer");
    }

    String[] teilnehmerArray = teilnehmerString.split(",");
    List<String[]> result = new ArrayList<>();

    for (String t : teilnehmerArray) {
        String[] nameParts = t.trim().split("\\s+");

        if (nameParts.length != 2) {
            throw new ValidationException("Format: Vorname Nachname, Vorname Nachname");
        }

        // Nur Buchstaben in Namen
        if (!nameParts[0].matches("[a-zA-ZäöüÄÖÜß]+") ||
            !nameParts[1].matches("[a-zA-ZäöüÄÖÜß]+")) {
            throw new ValidationException("Namen dürfen nur Buchstaben enthalten");
        }

        result.add(nameParts);
    }

    return result;
}
```

Diese Methode ist ziemlich strikt, aber aus gutem Grund. Sie stellt sicher, dass die Namen in einem konsistenten Format sind.

### ReservationRepository.java
Hier haben wir die interessantesten Queries.

**Die Überschneidungs-Query:**
```java
@Query("SELECT r FROM Reservation r WHERE r.zimmer = :zimmer " +
       "AND r.datum = :datum " +
       "AND NOT (r.bisZeit <= :vonZeit OR r.vonZeit >= :bisZeit)")
List<Reservation> findOverlappingReservations(
    @Param("zimmer") Integer zimmer,
    @Param("datum") LocalDate datum,
    @Param("vonZeit") LocalTime vonZeit,
    @Param("bisZeit") LocalTime bisZeit
);
```

Diese Query ist etwas kompliziert, aber super nützlich. Die Logik ist: "finde Reservierungen, die NICHT enden, bevor ich anfange UND NICHT anfangen, nachdem ich ende". Wenn du eine findest, gibt es einen Konflikt.

**Beispiel:**
- Bestehende Reservierung: 10:00 - 12:00
- Deine Reservierung: 11:00 - 13:00
- Überschneiden sie sich? Ja, weil deine Startzeit (11:00) zwischen 10:00 und 12:00 liegt

### Die Controller
Die Controller sind ziemlich direkt. Sie empfangen Requests, rufen den Service auf und geben Views zurück.

**Beispiel der Erstellen-Methode:**
```java
@PostMapping("/reservation/erstellen")
public String erstelleReservation(@Valid @ModelAttribute ReservationDTO dto,
                                   BindingResult result, Model model) {
    // Wenn es Validierungsfehler gibt, zurück zum Formular
    if (result.hasErrors()) {
        return "reservation-formular";
    }

    try {
        Reservation created = reservationService.erstelleReservation(dto);
        return "redirect:/reservation/bestaetigung/" + created.getId();
    } catch (ValidationException e) {
        model.addAttribute("error", e.getMessage());
        return "reservation-formular";
    }
}
```

Das `@Valid` validiert automatisch die Annotationen im DTO (`@NotNull`, `@Future`, etc.). Wenn es Fehler gibt, enthält das `BindingResult` sie und du kannst sie in der View anzeigen.

### Die Thymeleaf-Views

**Das Formular mit Validierung:**
```html
<form th:action="@{/reservation/erstellen}" method="post" th:object="${reservationDTO}">
    <div class="form-group">
        <label for="datum">Datum:</label>
        <input type="date" id="datum" th:field="*{datum}" class="form-control"
               th:classappend="${#fields.hasErrors('datum')} ? 'is-invalid' : ''">
        <div class="invalid-feedback" th:if="${#fields.hasErrors('datum')}"
             th:errors="*{datum}"></div>
    </div>

    <!-- Weitere Felder... -->

    <button type="submit">Reservierung erstellen</button>
</form>
```

Thymeleaf macht die ganze Magie von:
- Felder mit dem Objekt verknüpfen (`th:field`)
- Fehler anzeigen, wenn es welche gibt (`th:errors`)
- Fehler-CSS-Klassen hinzufügen (`is-invalid`)

**Der Verfügbarkeitschecker in JavaScript:**
```javascript
function checkAvailability() {
    const zimmer = document.getElementById('zimmer').value;
    const datum = document.getElementById('datum').value;
    const vonZeit = document.getElementById('vonZeit').value;
    const bisZeit = document.getElementById('bisZeit').value;

    if (zimmer && datum && vonZeit && bisZeit) {
        fetch(`/reservation/verfuegbarkeit?zimmer=${zimmer}&datum=${datum}&vonZeit=${vonZeit}&bisZeit=${bisZeit}`)
            .then(response => response.json())
            .then(data => {
                const indicator = document.getElementById('availability-indicator');
                if (data.verfuegbar) {
                    indicator.textContent = '✓ Verfügbar';
                    indicator.className = 'text-success';
                } else {
                    indicator.textContent = '✗ Nicht verfügbar';
                    indicator.className = 'text-danger';
                }
            });
    }
}

// Diese Funktion aufrufen, wenn sich die Felder ändern
document.getElementById('zimmer').addEventListener('change', checkAvailability);
document.getElementById('datum').addEventListener('change', checkAvailability);
// ... etc
```

Einfach aber effektiv. Sobald du ein Feld änderst, macht es einen AJAX-Aufruf zum Server und aktualisiert den Indikator.

---

## Was wir gelernt haben und Entscheidungen die wir getroffen haben

### Warum haben wir kein Login implementiert?

Das war wahrscheinlich die kontroverseste Entscheidung. Anfangs dachten wir daran, das ganze Benutzersystem zu machen: Registrierung, Login, Sessions, etc. Aber dann haben wir gemerkt:

1. **Es ist viel mehr Code**: Authentifizierung, Autorisierung, Passwort-Wiederherstellung, etc.
2. **Mehr Reibung für den Benutzer**: Du musst dich registrieren, dein Passwort merken...
3. **Für diesen Anwendungsfall ist es nicht nötig**: Es ist eine interne App, handhabt keine super sensiblen Daten
4. **Die UUID-Schlüssel sind sicher genug**: Praktisch unmöglich zu erraten

**Das Schlüsselsystem funktioniert, weil:**
- Jeder Schlüssel ist eine 36-Zeichen-UUID
- Die Wahrscheinlichkeit, einen Schlüssel zu erraten, ist astronomisch niedrig
- Du musst dir keine Passwörter merken
- Du kannst den öffentlichen Schlüssel teilen, ohne dir Sorgen zu machen

**Wann würdest du Login brauchen?**
- Wenn du sensible persönliche Daten hättest
- Wenn du Benutzerprofile bräuchtest
- Wenn du wolltest, dass jeder Benutzer all seine Reservierungen in einem Dashboard sieht
- Wenn es öffentlich zugänglich wäre (nicht nur intern)

### Die Handhabung der Teilnehmer

Anfangs dachten wir daran, ein einfaches Textfeld für die Teilnehmer zu machen. Aber dann haben wir gemerkt, dass wir wollten:
- Nach Reservierungen nach Teilnehmern suchen
- Die Namen getrennt in Vor- und Nachname haben
- Validieren, dass es echte Namen sind

Deshalb haben wir eine separate Tabelle gemacht. Ja, es kompliziert den Code ein bisschen (du musst den String parsen und mehrere Objekte erstellen), aber es lohnt sich für die Flexibilität.

**Alternativen, die wir in Betracht gezogen haben:**
- Ein JSON in einem TEXT-Feld speichern → Einfacher aber weniger flexibel
- Vor- und Nachname nicht trennen → Leichter aber weniger strukturiert

### Die Validierung in mehreren Schichten

Wir validieren an drei Stellen:
1. **Im Browser** (HTML5-Validierung)
2. **Im DTO** (Bean-Validierung)
3. **Im Service** (Business-Logik)

Ist es redundant? Ja. Ist es notwendig? Absolut!

- HTML5-Validierung: Sofortiges Feedback für den Benutzer
- DTO-Validierung: Schützt gegen Leute, die JavaScript deaktivieren
- Service-Validierung: Komplexe Logik (wie Verfügbarkeit)

Vertraue niemals nur auf Frontend-Validierung. Jeder kann JavaScript deaktivieren oder direkte Requests senden.

### Der Auto-Refresh der Verfügbarkeit

Das haben wir hinzugefügt, nachdem bei den ersten Tests mehrere Leute das komplette Formular ausgefüllt haben, nur um am Ende zu entdecken, dass der Raum schon belegt war.

Der Echtzeit-Checker hat die Erfahrung enorm verbessert. Jetzt siehst du sofort, ob es verfügbar ist.

**Probleme, die wir hatten:**
- Am Anfang hat es zu viele Aufrufe gemacht (einen für jeden Tastendruck)
- Wir haben es mit Debouncing behoben: wartet 500ms nach der letzten Änderung
- Jetzt ist es viel effizienter

### Der automatische Timestamp

Die Verwendung von `@CreationTimestamp` und `@UpdateTimestamp` war eine offensichtliche Entscheidung, sobald wir wussten, dass sie existieren. Vorher haben wir es manuell mit `LocalDateTime.now()` in jeder Methode gemacht, aber:
- Wir haben es manchmal vergessen
- Es war wiederholter Code
- Die Annotationen sind zuverlässiger

### PostgreSQL vs. andere Datenbanken

**Warum PostgreSQL und nicht MySQL?**
- Bessere Handhabung von Datum und Zeit
- Bessere Unterstützung von Constraints
- Robuster im Allgemeinen
- Kostenlos und Open Source

**Warum nicht H2 (in-memory)?**
- H2 ist großartig für schnelle Entwicklung
- Aber für Produktion willst du etwas Persistentes
- PostgreSQL gibt dir eine produktionsnähere Erfahrung

---

## Häufige Probleme und wie man sie löst

### "Kann keine Verbindung zur Datenbank herstellen"

**Symptome:**
- Fehler: `Connection refused` oder `Connection timeout`
- Die App startet, scheitert aber beim Versuch, auf Daten zuzugreifen

**Lösungen:**

1. **Überprüfe, dass PostgreSQL läuft**
   ```bash
   # Unter Windows (wenn du PostgreSQL als Dienst installiert hast)
   sc query postgresql

   # In Docker
   docker ps | grep postgres
   ```

2. **Überprüfe die Konfiguration**
   - Der Port ist standardmäßig 5432
   - Benutzer und Passwort in `application-dev.properties` müssen mit deiner DB übereinstimmen
   - Der Datenbankname muss existieren

3. **Wenn du Docker Compose nutzt**
   ```bash
   # Sieh dir die Logs an, um zu sehen, was passiert ist
   docker-compose logs postgres
   ```

### "Die Anwendung startet, aber ich kann keine Reservierungen erstellen"

**Mögliche Ursachen:**

1. **Die Tabellen existieren nicht**
   - Wenn du das Profil `docker` oder `prod` nutzt, musst du `init.sql` manuell ausführen
   - Oder ändere `ddl-auto` temporär auf `update`

2. **Validierungsprobleme**
   - Überprüfe die Browser-Konsole (F12)
   - Überprüfe die Anwendungslogs
   - Sind die Daten, die du eingegeben hast, gültig?

3. **Der Raum ist schon belegt**
   - Die Fehlermeldung sollte es sagen
   - Versuche einen anderen Raum oder eine andere Zeit

### "Änderungen im Code werden nicht reflektiert"

Wenn du Java-Code änderst und die Änderungen nicht siehst:

1. **Neu kompilieren**
   ```bash
   mvn clean compile
   ```

2. **Wenn du die IDE nutzt, musst du manchmal Build → Rebuild Project machen**

3. **Wenn du Docker nutzt, musst du das Image neu bauen**
   ```bash
   docker-compose build
   docker-compose up
   ```

4. **Überprüfe, dass Spring DevTools funktioniert**
   - Es sollte automatisch neu laden
   - Wenn nicht, starte die App neu

### "Der Browser öffnet sich nicht automatisch"

Das ist ein Feature, kein Bug, aber wenn es nicht funktioniert:

1. **Öffne manuell**: http://localhost:8080
2. **Überprüfe den Port**: vielleicht hast du einen anderen konfiguriert
   ```properties
   # Überprüfe in application.properties
   server.port=${SERVER_PORT:8080}
   ```

### "Fehler: Port already in use"

Das bedeutet, dass Port 8080 bereits von einer anderen Anwendung belegt ist.

**Lösungen:**

1. **Nutze einen anderen Port**
   ```bash
   # Beim Ausführen der App
   mvn spring-boot:run -Dserver.port=8081
   ```

2. **Beende den Prozess, der den Port nutzt**
   ```bash
   # Unter Windows
   netstat -ano | findstr :8080
   taskkill /PID <die_nummer_die_erscheint> /F

   # Unter Linux/Mac
   lsof -i :8080
   kill -9 <PID>
   ```

### "Die Schlüssel funktionieren nicht"

Wenn ein Schlüssel dich nicht eine Reservierung sehen oder bearbeiten lässt:

1. **Überprüfe, dass du ihn vollständig kopiert hast**
   - Schlüssel sind 36 Zeichen lang
   - Sie enthalten Bindestriche: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`

2. **Überprüfe, dass es keine Leerzeichen am Anfang oder Ende gibt**

3. **Überprüfe in der DB, ob es existiert**
   ```sql
   SELECT * FROM reservationen
   WHERE private_schluessel = 'dein-schluessel'
   OR public_schluessel = 'dein-schluessel';
   ```

### "Die Teilnehmer werden beim Aktualisieren nicht gelöscht"

Dieses Problem hatten wir am Anfang. Die Lösung war:

```java
// In ReservationService.aktualisiereReservation()
reservation.getTeilnehmer().clear();
parseTeilnehmer(dto.getTeilnehmerListe(), reservation);
```

Zuerst löschen wir die Liste, dann fügen wir die neuen hinzu. Wenn du nur hinzufügst ohne zu löschen, häufen sie sich an.

### "Zeitzonenfehler"

Wenn Daten seltsam oder verschoben aussehen:

1. **Überprüfe die Zeitzone deiner DB**
   ```sql
   SHOW timezone;
   ```

2. **Konfiguriere die Zeitzone in der Anwendung**
   ```properties
   # In application.properties
   spring.jpa.properties.hibernate.jdbc.time_zone=UTC
   ```

3. **Nutze LocalDate und LocalTime** (die keine Zeitzone haben), um Probleme zu vermeiden

---

## Zukünftige Verbesserungen

Wenn wir mehr Zeit hätten, sind das die Sachen, die wir hinzufügen würden:

### 1. Visueller Kalender
Statt nur einem Formular, ein Kalender, wo du alle Reservierungen sehen und klicken kannst, um eine neue zu erstellen. Sowas wie Google Calendar.

### 2. E-Mail-Benachrichtigungen
Wenn du eine Reservierung erstellst, dass es dir eine E-Mail mit deinen Schlüsseln schickt. So verlierst du sie nicht.

### 3. Wiederkehrende Reservierungen
Wenn du ein Meeting hast, das sich jede Woche wiederholt, es einmal für alle Wochen erstellen können.

### 4. Suche nach Teilnehmern
Sehen, in welchen Meetings eine bestimmte Person ist.

### 5. Admin-Dashboard
Eine spezielle Ansicht für Admins, wo sie alle Reservierungen sehen, Nutzungsstatistiken der Räume, etc.

### 6. Validierung der Bürozeiten
Nur Reservierungen zwischen 8 Uhr und 18 Uhr erlauben, zum Beispiel.

### 7. Maximale Dauer
Reservierungen auf, sagen wir, maximal 4 Stunden begrenzen.

### 8. Export zu ICS
ICS-Dateien generieren, damit du das Meeting in deinen persönlichen Kalender importieren kannst.

### 9. Integration mit Slack oder Teams
Benachrichtigungen, wenn jemand eine Reservierung erstellt oder ändert.

### 10. Mobile App
Eine native App für iOS/Android, um vom Handy aus einfacher zu reservieren.

---

## Fazit

Dieses Projekt war eine gute Übung, um zu verstehen, wie moderne Webanwendungen mit Spring Boot funktionieren. Wir haben gelernt über:

- Schichtenarchitektur (Controller → Service → Repository)
- Handhabung von relationalen Datenbanken
- Datenvalidierung
- Server-seitiges Rendering mit Thymeleaf
- Docker und Deployment
- Und vor allem, Design-Entscheidungen zu treffen, die an den Endbenutzer denken

Der Code ist nicht perfekt (welcher Code ist das schon?), aber er ist funktional, wartbar und erfüllt, was gebraucht wurde. Ich hoffe, diese Dokumentation hilft dir zu verstehen, wie alles funktioniert. Wenn du Fragen hast, der Code ist da, um ihn genauer zu erkunden.

Viel Erfolg beim Coden! 🚀

---

**Datum der letzten Aktualisierung**: November 2025
**Version**: 1.0
**Autor**: Team Terminkalender
**Modul**: 223
