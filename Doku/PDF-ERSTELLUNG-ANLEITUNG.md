# Anleitung: PDF-Dokumentation erstellen

Diese Anleitung erklärt, wie Sie aus den PlantUML-Diagrammen ein PDF-Dokument erstellen.

---

## Methode 1: Online PlantUML Server (Einfachste Methode)

### Schritt 1: Diagramme als PNG exportieren

1. Gehen Sie zu: **http://www.plantuml.com/plantuml/**

2. Für jedes Diagramm:
   - Öffnen Sie die `.puml` Datei in einem Text-Editor
   - Kopieren Sie den gesamten Inhalt
   - Fügen Sie ihn in den PlantUML-Server ein
   - Klicken Sie auf "Submit"
   - Klicken Sie mit rechts auf das Diagramm → "Bild speichern als..."
   - Speichern Sie als PNG

3. Wiederholen Sie dies für alle 3 Diagramme:
   - `UML-Zustandsdiagramm.puml` → speichern als `Zustandsdiagramm.png`
   - `ERM-Diagramm.puml` → speichern als `ERM-Diagramm.png`
   - `UML-Klassendiagramm.puml` → speichern als `Klassendiagramm.png`

### Schritt 2: Word-Dokument erstellen

1. Öffnen Sie Microsoft Word (oder Google Docs / LibreOffice)

2. Fügen Sie folgende Inhalte ein:

```
=== Seite 1: Titelseite ===
Terminkalender
Projektdokumentation
Modul 223

Raumreservationssystem mit Spring Boot

[Ihr Name]
[Datum]

=== Seite 2: UML-Zustandsdiagramm ===
## 1. UML-Zustandsdiagramm

Dieses Diagramm zeigt die Navigation zwischen den Seiten.

[Bild: Zustandsdiagramm.png einfügen]

Beschreibung:
- Startseite: Hauptseite der Anwendung
- Formular: Neue Reservation erstellen
- Bestätigung: Codes werden angezeigt
- Ansicht: Öffentliche Nur-Lese-Ansicht
- Bearbeitung: Private Vollzugriff-Ansicht

=== Seite 3: Entity-Relationship-Diagramm ===
## 2. Entity-Relationship-Diagramm (ERD)

Dieses Diagramm zeigt die Datenbank-Struktur.

[Bild: ERM-Diagramm.png einfügen]

Beschreibung:
- Tabelle "reservationen": Haupttabelle mit allen Reservation-Daten
- Tabelle "teilnehmer": Personen, die an einer Reservation teilnehmen
- Beziehung: 1 Reservation hat 0 bis viele Teilnehmer (1:N)

=== Seite 4: UML-Klassendiagramm ===
## 3. UML-Klassendiagramm

Dieses Diagramm zeigt die Java-Klassen-Struktur.

[Bild: Klassendiagramm.png einfügen]

Beschreibung:
- Controllers: Verarbeiten HTTP-Anfragen
- Services: Enthalten die Geschäftslogik
- Repositories: Greifen auf die Datenbank zu
- Models: Repräsentieren die Datenbank-Tabellen
- DTOs: Übertragen Daten zwischen Schichten
```

3. Fügen Sie die PNG-Bilder ein:
   - Klicken Sie an die Stelle, wo `[Bild: ... einfügen]` steht
   - Menü: Einfügen → Bilder → Dieses Gerät
   - Wählen Sie das entsprechende PNG-Bild
   - Passen Sie die Größe an (ca. 80% Seitenbreite)

### Schritt 3: Als PDF speichern

1. In Microsoft Word:
   - Datei → Speichern unter
   - Dateityp: **PDF**
   - Dateiname: **Terminkalender_Dokumentation.pdf**

2. In Google Docs:
   - Datei → Herunterladen → PDF-Dokument (.pdf)

3. In LibreOffice:
   - Datei → Exportieren als PDF

---

## Methode 2: IntelliJ IDEA (wenn verfügbar)

### Voraussetzung: PlantUML Plugin

1. IntelliJ IDEA öffnen
2. File → Settings → Plugins
3. Suchen: "PlantUML Integration"
4. Installieren und IntelliJ neu starten

### Diagramme exportieren

1. Öffnen Sie eine `.puml` Datei in IntelliJ
2. Das Diagramm wird automatisch rechts angezeigt
3. Rechtsklick auf das Diagramm
4. "Export to PNG" oder "Export to SVG"
5. Speichern Sie das Bild

Wiederholen Sie dies für alle 3 Diagramme.

Dann folgen Sie **Methode 1, Schritt 2 und 3** oben.

---

## Methode 3: Visual Studio Code (wenn verfügbar)

### Voraussetzung: PlantUML Extension

1. VS Code öffnen
2. Extensions (Ctrl+Shift+X)
3. Suchen: "PlantUML"
4. Extension von "jebbs" installieren
5. Ggf. Java installieren (falls noch nicht vorhanden)

### Diagramme exportieren

1. Öffnen Sie eine `.puml` Datei in VS Code
2. Drücken Sie `Alt+D` für Vorschau
3. Rechtsklick auf das Diagramm → "Export current diagram"
4. Wählen Sie Format: PNG
5. Speichern Sie das Bild

Wiederholen Sie dies für alle 3 Diagramme.

Dann folgen Sie **Methode 1, Schritt 2 und 3** oben.

---

## Methode 4: Command Line (PlantUML JAR)

### Voraussetzung: Java installiert

1. Laden Sie PlantUML JAR herunter:
   - https://plantuml.com/download
   - Datei: `plantuml.jar`

2. Legen Sie `plantuml.jar` in den Doku-Ordner

3. Öffnen Sie Terminal/CMD im Doku-Ordner

4. Führen Sie aus:
```bash
java -jar plantuml.jar UML-Zustandsdiagramm.puml
java -jar plantuml.jar ERM-Diagramm.puml
java -jar plantuml.jar UML-Klassendiagramm.puml
```

5. Es werden automatisch PNG-Dateien erstellt

Dann folgen Sie **Methode 1, Schritt 2 und 3** oben.

---

## Methode 5: Online Draw.io (Alternativ)

Falls PlantUML nicht funktioniert, können Sie die Diagramme mit **draw.io** neu zeichnen:

1. Gehen Sie zu: https://app.diagrams.net/
2. Erstellen Sie ein neues Diagramm
3. Nutzen Sie die Vorlagen:
   - Für Zustandsdiagramm: UML → State Machine
   - Für ERD: Entity Relation
   - Für Klassendiagramm: UML → Class Diagram
4. Zeichnen Sie die Diagramme basierend auf den `.puml` Dateien nach
5. Exportieren Sie als PNG oder direkt als PDF

---

## Empfohlene Reihenfolge im PDF

1. **Titelseite**
   - Projektname: Terminkalender
   - Untertitel: Projektdokumentation Modul 223
   - Ihr Name
   - Datum

2. **Inhaltsverzeichnis** (optional)

3. **Projekt-Übersicht** (1 Seite)
   - Was macht das System?
   - Welche Technologien werden verwendet?

4. **UML-Zustandsdiagramm** (1-2 Seiten)
   - Diagramm-Bild
   - Kurze Erklärung der Zustände

5. **Entity-Relationship-Diagramm** (1-2 Seiten)
   - Diagramm-Bild
   - Kurze Erklärung der Tabellen

6. **UML-Klassendiagramm** (1-2 Seiten)
   - Diagramm-Bild
   - Kurze Erklärung der Klassen

7. **Zusammenfassung** (optional)
   - Wichtigste Erkenntnisse
   - Erreichte Lernziele

---

## Tipps für ein gutes PDF

### Formatierung:
- **Schriftgröße**: 11-12pt für Text
- **Überschriften**: 14-16pt für Hauptüberschriften
- **Seitenränder**: 2-3cm auf allen Seiten
- **Zeilenabstand**: 1,5-fach

### Diagramme:
- Stellen Sie sicher, dass alle Texte lesbar sind
- Verwenden Sie gute Auflösung (mindestens 300 DPI)
- Zentrieren Sie die Diagramme auf der Seite

### Beschreibungen:
- Halten Sie Erklärungen kurz und präzise
- Verwenden Sie Stichpunkte
- Schreiben Sie in einfachem Deutsch (A2-B1 Niveau)

### Struktur:
- Jedes Diagramm auf einer eigenen Seite
- Seitenzahlen hinzufügen
- Überschriften für jedes Diagramm

---

## Checkliste vor dem Einreichen

- [ ] Alle 3 Diagramme sind im PDF enthalten
- [ ] Alle Diagramme sind lesbar und gut sichtbar
- [ ] Jedes Diagramm hat eine kurze Beschreibung
- [ ] Titelseite mit Projektname und Ihrem Namen
- [ ] Alle Texte sind auf Deutsch
- [ ] PDF ist korrekt benannt: `Terminkalender_Dokumentation.pdf`
- [ ] Datei ist nicht zu groß (< 10 MB)
- [ ] PDF öffnet sich ohne Fehler

---

## Fertig!

Nach diesen Schritten haben Sie ein vollständiges PDF mit allen geforderten Diagrammen.

Legen Sie das PDF in den `Doku` Ordner und benennen Sie es:
**Terminkalender_Dokumentation.pdf**
