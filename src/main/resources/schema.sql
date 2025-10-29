-- ============================================
-- Datenbank: Raumreservationssystem
-- ============================================
-- Beschreibung: Schema für das Raumreservationssystem
-- Tabellen: reservationen, teilnehmer
-- Engine: PostgreSQL 16+
-- ============================================

-- ============================================
-- Tabelle: reservationen (Reservierungen)
-- ============================================
CREATE TABLE IF NOT EXISTS reservationen (
    id BIGSERIAL PRIMARY KEY,
    datum DATE NOT NULL,
    von_zeit TIME NOT NULL,
    bis_zeit TIME NOT NULL,
    zimmer INTEGER NOT NULL CHECK (zimmer >= 101 AND zimmer <= 105),
    bemerkung VARCHAR(200) NOT NULL,
    private_schluessel VARCHAR(36) NOT NULL UNIQUE,
    public_schluessel VARCHAR(36) NOT NULL UNIQUE,
    erstellt_am TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    aktualisiert_am TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Kommentare für reservationen-Tabelle
COMMENT ON TABLE reservationen IS 'Tabelle der Raumreservierungen';
COMMENT ON COLUMN reservationen.datum IS 'Reservierungsdatum';
COMMENT ON COLUMN reservationen.von_zeit IS 'Startzeit';
COMMENT ON COLUMN reservationen.bis_zeit IS 'Endzeit';
COMMENT ON COLUMN reservationen.zimmer IS 'Zimmernummer (101-105)';
COMMENT ON COLUMN reservationen.bemerkung IS 'Beschreibung oder Notizen';
COMMENT ON COLUMN reservationen.private_schluessel IS 'Privater Schlüssel (UUID) zum Bearbeiten/Löschen';
COMMENT ON COLUMN reservationen.public_schluessel IS 'Öffentlicher Schlüssel (UUID) nur Lesezugriff';
COMMENT ON COLUMN reservationen.erstellt_am IS 'Erstellungsdatum';
COMMENT ON COLUMN reservationen.aktualisiert_am IS 'Datum der letzten Aktualisierung';

-- ============================================
-- Tabelle: teilnehmer (Teilnehmer)
-- ============================================
CREATE TABLE IF NOT EXISTS teilnehmer (
    id BIGSERIAL PRIMARY KEY,
    vorname VARCHAR(50) NOT NULL,
    nachname VARCHAR(50) NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_teilnehmer_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservationen(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Kommentare für teilnehmer-Tabelle
COMMENT ON TABLE teilnehmer IS 'Tabelle der Reservierungsteilnehmer';
COMMENT ON COLUMN teilnehmer.vorname IS 'Vorname des Teilnehmers';
COMMENT ON COLUMN teilnehmer.nachname IS 'Nachname des Teilnehmers';
COMMENT ON COLUMN teilnehmer.reservation_id IS 'ID der zugehörigen Reservierung';

-- ============================================
-- Indizes für bessere Performance
-- ============================================
CREATE INDEX IF NOT EXISTS idx_reservationen_datum ON reservationen(datum);
CREATE INDEX IF NOT EXISTS idx_reservationen_zimmer ON reservationen(zimmer);
CREATE INDEX IF NOT EXISTS idx_reservationen_zimmer_datum ON reservationen(zimmer, datum);
CREATE INDEX IF NOT EXISTS idx_reservationen_private_key ON reservationen(private_schluessel);
CREATE INDEX IF NOT EXISTS idx_reservationen_public_key ON reservationen(public_schluessel);
CREATE INDEX IF NOT EXISTS idx_reservationen_erstellt_am ON reservationen(erstellt_am);
CREATE INDEX IF NOT EXISTS idx_teilnehmer_reservation ON teilnehmer(reservation_id);

-- ============================================
-- Trigger für automatisches Update von aktualisiert_am
-- ============================================
CREATE OR REPLACE FUNCTION update_aktualisiert_am()
RETURNS TRIGGER AS $$
BEGIN
    NEW.aktualisiert_am = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_update_aktualisiert_am ON reservationen;
CREATE TRIGGER trigger_update_aktualisiert_am
    BEFORE UPDATE ON reservationen
    FOR EACH ROW
    EXECUTE FUNCTION update_aktualisiert_am();

-- ============================================
-- Beispieldaten (optional - auskommentiert)
-- ============================================
-- Entfernen Sie die Kommentarzeichen, wenn Sie Testdaten möchten

/*
INSERT INTO reservationen (datum, von_zeit, bis_zeit, zimmer, bemerkung, private_schluessel, public_schluessel) VALUES
('2025-10-28', '09:00:00', '10:00:00', 101, 'Team-Meeting', gen_random_uuid()::text, gen_random_uuid()::text),
('2025-10-28', '14:00:00', '16:00:00', 102, 'Projektpräsentation', gen_random_uuid()::text, gen_random_uuid()::text),
('2025-10-29', '10:00:00', '11:30:00', 103, 'Technisches Interview', gen_random_uuid()::text, gen_random_uuid()::text);

-- Angenommen, die generierten IDs sind 1, 2, 3
INSERT INTO teilnehmer (vorname, nachname, reservation_id) VALUES
('Max', 'Müller', 1),
('Anna', 'Schmidt', 1),
('Peter', 'Weber', 2),
('Lisa', 'Meyer', 2),
('Tom', 'Fischer', 3);
*/
