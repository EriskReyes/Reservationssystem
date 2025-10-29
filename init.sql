-- Script de inicialización de la base de datos
-- Se ejecuta automáticamente cuando se crea el contenedor de PostgreSQL

-- Crear tabla de reservaciones
CREATE TABLE IF NOT EXISTS reservationen (
    id BIGSERIAL PRIMARY KEY,
    datum DATE NOT NULL,
    von_zeit TIME NOT NULL,
    bis_zeit TIME NOT NULL,
    zimmer INTEGER NOT NULL CHECK (zimmer >= 101 AND zimmer <= 105),
    bemerkung VARCHAR(200),
    private_schluessel VARCHAR(36) NOT NULL UNIQUE,
    public_schluessel VARCHAR(36) NOT NULL UNIQUE,
    erstellt_am TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    aktualisiert_am TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de participantes
CREATE TABLE IF NOT EXISTS teilnehmer (
    id BIGSERIAL PRIMARY KEY,
    vorname VARCHAR(50) NOT NULL,
    nachname VARCHAR(50) NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservationen(id)
        ON DELETE CASCADE
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_reservationen_datum ON reservationen(datum);
CREATE INDEX IF NOT EXISTS idx_reservationen_zimmer ON reservationen(zimmer);
CREATE INDEX IF NOT EXISTS idx_reservationen_private_key ON reservationen(private_schluessel);
CREATE INDEX IF NOT EXISTS idx_reservationen_public_key ON reservationen(public_schluessel);
CREATE INDEX IF NOT EXISTS idx_teilnehmer_reservation ON teilnehmer(reservation_id);

-- Función para actualizar el timestamp automáticamente
CREATE OR REPLACE FUNCTION update_aktualisiert_am()
RETURNS TRIGGER AS $$
BEGIN
    NEW.aktualisiert_am = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para actualizar aktualisiert_am automáticamente
DROP TRIGGER IF EXISTS trigger_update_aktualisiert_am ON reservationen;
CREATE TRIGGER trigger_update_aktualisiert_am
    BEFORE UPDATE ON reservationen
    FOR EACH ROW
    EXECUTE FUNCTION update_aktualisiert_am();

-- Datos de ejemplo (opcional - comentar si no se desean)
-- INSERT INTO reservationen (datum, von_zeit, bis_zeit, zimmer, bemerkung, private_schluessel, public_schluessel)
-- VALUES
--     ('2025-10-26', '09:00:00', '10:00:00', 101, 'Reunión de equipo', gen_random_uuid()::text, gen_random_uuid()::text),
--     ('2025-10-26', '14:00:00', '16:00:00', 102, 'Presentación proyecto', gen_random_uuid()::text, gen_random_uuid()::text);
