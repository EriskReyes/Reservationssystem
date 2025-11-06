Projekt von: Rigo Erisk Reyes und Denis Perdomo
Terminkalender - Projektdokumentation
Modul 223 - Raumreservationssystem



  Inhaltsverzeichnis

1. [Projektübersicht](projektübersicht)
2. [Technologie](technologie)
3. [Diagramme](diagramme)
4. [Systemfunktionen](systemfunktionen)
5. [Datenbank](datenbank)
6. [Anwendung starten](anwendung-starten)
7. [Hinweise zur Dokumentation](hinweise-zur-dokumentation)



  Projektübersicht

Das Terminkalender-System ist eine Web-Anwendung für die Reservierung von Besprechungsräumen.

 Hauptmerkmale:
- Keine Benutzerkonten nötig
- Einfache Raumreservierung
- Zwei Zugriffsarten mit Codes:
  - Private Schlüssel: Für Bearbeitung und Löschung
  - Public Schlüssel: Nur zur Ansicht
- 5 verfügbare Räume (Zimmer 101-105)

---

  Technologie

Das Projekt verwendet folgende Technologien:

| Technologie | Version | Verwendung |
|------------|---------|-----------|
| Java | 21 | Programmiersprache |
| Spring Boot | 3.2.0 | Framework |
| Maven | 3.9+ | Build-Tool |
| PostgreSQL | 16 | Datenbank |
| Thymeleaf | 3.2.0 | Template-Engine |
| Bootstrap | 5.3.2 | CSS Framework |

 Spring Boot Module:
- Spring MVC (Web-Controller)
- Spring Data JPA (Datenbank)
- Spring Validation (Formular-Validierung)
- Thymeleaf (HTML-Templates)

---

 Diagramme

In diesem Ordner finden Sie drei wichtige Diagramme:

 1. UML-Zustandsdiagramm
Datei: `UML-Zustandsdiagramm.puml`

Dieses Diagramm zeigt die Navigation der Anwendung:
- Wie kommt man von einer Seite zur anderen?
- Welche Buttons führen wohin?
- Was passiert bei Fehlern?

Hauptseiten:
- Startseite (index.html)
- Neue Reservation (reservation-formular.html)
- Bestätigung (reservation-bestaetigung.html)
- Öffentliche Ansicht (reservation-ansicht.html)
- Bearbeitungsansicht (reservation-bearbeiten.html)

 2. Entity-Relationship-Diagramm (ERD)
Datei: `ERM-Diagramm.puml`

Dieses Diagramm zeigt die Datenbank-Struktur:
- Welche Tabellen gibt es?
- Welche Felder hat jede Tabelle?
- Wie sind die Tabellen verbunden?

Tabellen:
- `reservationen` (Haupttabelle)
- `teilnehmer` (Personen bei der Reservation)

Beziehung: Eine Reservation kann viele Teilnehmer haben (1:N)

 3. UML-Klassendiagramm
Datei: `UML-Klassendiagramm.puml`

Dieses Diagramm zeigt die Code-Struktur:
- Welche Java-Klassen gibt es?
- Welche Methoden hat jede Klasse?
- Wie arbeiten die Klassen zusammen?

Komponenten:
- Controllers: IndexController, ReservationController
- Services: ReservationService, ValidationService
- Repositories: ReservationRepository, TeilnehmerRepository
- Models: Reservation, Teilnehmer
- DTOs: ReservationDTO

---

  Systemfunktionen

 1. Neue Reservation erstellen
Der Benutzer füllt ein Formular aus:
- Datum: Muss in der Zukunft liegen
- Von-Zeit und Bis-Zeit: Mindestens 15 Minuten
- Zimmer: Auswahl zwischen 101-105
- Bemerkung: 10-200 Zeichen
- Teilnehmer: Liste von Namen (z.B. "Max Müller, Anna Schmidt")

Nach dem Speichern bekommt der Benutzer zwei Codes:
- Private Schlüssel (zum Bearbeiten)
- Public Schlüssel (zum Ansehen)

 2. Reservation ansehen
Mit dem Public Schlüssel kann man:
- Alle Details sehen
- Datum, Zeit, Zimmer anzeigen
- Teilnehmer-Liste sehen

Man kann nichts ändern oder löschen.

 3. Reservation bearbeiten
Mit dem Private Schlüssel kann man:
- Alle Felder ändern
- Teilnehmer hinzufügen oder entfernen
- Die Reservation löschen

 4. Validierungen
Das System prüft automatisch:
- Ist das Zimmer zur gewünschten Zeit frei?
- Liegt das Datum in der Zukunft?
- Sind alle Pflichtfelder ausgefüllt?
- Haben die Namen das richtige Format?

---

  Datenbank

 Tabelle: reservationen
Speichert alle Reservations-Informationen:
- `id`: Eindeutige Nummer (automatisch)
- `datum`: Das Reservationsdatum
- `von_zeit`: Startzeit
- `bis_zeit`: Endzeit
- `zimmer`: Raumnummer (101-105)
- `bemerkung`: Beschreibung (10-200 Zeichen)
- `private_schluessel`: Code für Bearbeitung (UUID)
- `public_schluessel`: Code für Ansicht (UUID)
- `erstellt_am`: Wann wurde die Reservation erstellt?
- `aktualisiert_am`: Wann wurde sie zuletzt geändert?

 Tabelle: teilnehmer
Speichert alle Personen einer Reservation:
- `id`: Eindeutige Nummer (automatisch)
- `vorname`: Vorname der Person
- `nachname`: Nachname der Person
- `reservation_id`: Zu welcher Reservation gehört die Person?

 Beziehung
Jede Reservation kann viele Teilnehmer haben.
Wenn man eine Reservation löscht, werden auch alle Teilnehmer gelöscht.

---

  Anwendung starten

 Option 1: Mit Docker (empfohlen)
```bash
docker-compose up
```

Die Anwendung startet automatisch auf: http://localhost:8080

 Option 2: Lokal (mit Maven)
1. PostgreSQL starten
2. Terminal öffnen:
```bash
mvn spring-boot:run
```

 Test-Daten
Das System erstellt automatisch 3 Test-Reservationen beim Start.
Die Codes (Schlüssel) sehen Sie in der Konsole.

---

  Hinweise zur Dokumentation

 PlantUML Diagramme anzeigen

Die `.puml` Dateien sind PlantUML-Diagramme. Sie können sie anzeigen mit:

Online:
- Website: http://www.plantuml.com/plantuml/
- Datei-Inhalt kopieren und einfügen

In IntelliJ IDEA:
1. Plugin installieren: "PlantUML Integration"
2. Datei öffnen
3. Diagramm wird automatisch angezeigt

In Visual Studio Code:
1. Extension installieren: "PlantUML"
2. Datei öffnen
3. `Alt+D` drücken für Vorschau

 PDF erstellen (optional)

Um ein PDF mit allen Diagrammen zu erstellen:

1. PlantUML-Diagramme als PNG/SVG exportieren
2. In ein Word-Dokument einfügen
3. Beschreibungen hinzufügen
4. Als PDF speichern

Oder: Online-Tools verwenden wie:
- draw.io (Diagramme nachzeichnen)
- PlantUML Server (automatische PNG-Generierung)

---

  Projekt-Checkliste

- [x] Spring Boot + Maven konfiguriert
- [x] PostgreSQL Datenbank erstellt
- [x] Alle Controller implementiert
- [x] Alle Models mit JPA erstellt
- [x] Validierungen funktionieren
- [x] Private/Public Schlüssel-System funktioniert
- [x] HTML-Templates mit Thymeleaf
- [x] Test-Daten werden automatisch erstellt
- [x] UML-Zustandsdiagramm erstellt
- [x] Entity-Relationship-Diagramm erstellt
- [x] UML-Klassendiagramm erstellt
- [x] Dokumentation in Deutsch verfasst

---

 Projektinformationen

Projektname: Terminkalender
Modul: 223
Typ: Raumreservationssystem
Framework: Spring Boot 3.2.0
Datenbank: PostgreSQL 16
Sprache: Deutsch

---

  Wichtige Dateien im Projekt

 Java-Klassen:
```
src/main/java/com/terminkalender/
├── controller/
│   ├── IndexController.java
│   └── ReservationController.java
├── service/
│   ├── ReservationService.java
│   └── ValidationService.java
├── model/
│   ├── Reservation.java
│   └── Teilnehmer.java
├── repository/
│   ├── ReservationRepository.java
│   └── TeilnehmerRepository.java
└── dto/
    └── ReservationDTO.java
```

 HTML-Templates:
```
src/main/resources/templates/
├── index.html
├── reservation-formular.html
├── reservation-bestaetigung.html
├── reservation-ansicht.html
└── reservation-bearbeiten.html
```

 Datenbank:
```
src/main/resources/
├── schema.sql (Tabellen-Struktur)
├── application.properties (Konfiguration)
└── application-docker.properties (Docker-Konfiguration)
```

---

  Lernziele (Modul 223)

Dieses Projekt zeigt folgende Kenntnisse:

1. Spring Boot Grundlagen
   - Controllers erstellen
   - Services implementieren
   - Dependency Injection verwenden

2. Spring Data JPA
   - Entities definieren
   - Repositories erstellen
   - Beziehungen (OneToMany, ManyToOne)
   - JPQL Queries schreiben

3. MVC-Architektur
   - Model-View-Controller Pattern
   - Separation of Concerns
   - Layer Architecture

4. Web-Entwicklung
   - HTML-Formulare mit Thymeleaf
   - Request Handling
   - Validierung
   - Error Handling

5. Datenbank-Design
   - Normalisierung
   - Primary Keys
   - Foreign Keys
   - Indizes

6. Dokumentation
   - UML-Diagramme
   - ERD
   - Code-Dokumentation
   - README-Dateien

---

Ende der Dokumentation
