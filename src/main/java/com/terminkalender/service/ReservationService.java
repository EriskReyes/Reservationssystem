package com.terminkalender.service;

import com.terminkalender.dto.ReservationDTO;
import com.terminkalender.model.Reservation;
import com.terminkalender.model.Teilnehmer;
import com.terminkalender.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository repository;
    private final ValidationService validation;

    public ReservationService(ReservationRepository repository, ValidationService validation) {
        this.repository = repository;
        this.validation = validation;
    }

    @Transactional
    public Reservation erstelleReservation(ReservationDTO dto) {
        validateReservation(dto, null);

        Reservation reservation = new Reservation();
        reservation.setDatum(dto.getDatum());
        reservation.setVonZeit(dto.getVonZeit());
        reservation.setBisZeit(dto.getBisZeit());
        reservation.setZimmer(dto.getZimmer());
        reservation.setBemerkung(dto.getBemerkung());
        reservation.setPrivateKey(generateUniqueKey(true));
        reservation.setPublicKey(generateUniqueKey(false));

        List<String[]> teilnehmerList = validation.validiereTeilnehmer(dto.getTeilnehmerListe());
        teilnehmerList.forEach(name ->
            reservation.addTeilnehmer(new Teilnehmer(name[0], name[1]))
        );

        return repository.save(reservation);
    }

    @Transactional
    public Reservation aktualisiereReservation(Long id, ReservationDTO dto) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation nicht gefunden"));

        validateReservation(dto, id);

        reservation.setDatum(dto.getDatum());
        reservation.setVonZeit(dto.getVonZeit());
        reservation.setBisZeit(dto.getBisZeit());
        reservation.setZimmer(dto.getZimmer());
        reservation.setBemerkung(dto.getBemerkung());

        reservation.clearTeilnehmer();
        List<String[]> teilnehmerList = validation.validiereTeilnehmer(dto.getTeilnehmerListe());
        teilnehmerList.forEach(name ->
            reservation.addTeilnehmer(new Teilnehmer(name[0], name[1]))
        );

        return repository.save(reservation);
    }

    @Transactional
    public void loescheReservation(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Reservation nicht gefunden");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findeNachPrivateKey(String key) {
        return repository.findByPrivateKey(key);
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findeNachPublicKey(String key) {
        return repository.findByPublicKey(key);
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findeNachId(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findeAlle() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findeZukuenftige() {
        return repository.findAllFutureReservations();
    }

    @Transactional(readOnly = true)
    public boolean istZimmerVerfuegbar(Integer zimmer, LocalDate datum, LocalTime von, LocalTime bis) {
        return isRoomAvailable(zimmer, datum, von, bis, null);
    }

    private void validateReservation(ReservationDTO dto, Long excludeId) {
        if (!validation.istDatumGueltig(dto.getDatum())) {
            throw new IllegalArgumentException("Datum muss in der Zukunft liegen");
        }
        if (!validation.istZimmerGueltig(dto.getZimmer())) {
            throw new IllegalArgumentException("Ungültige Zimmernummer");
        }
        if (!validation.istBemerkungGueltig(dto.getBemerkung())) {
            throw new IllegalArgumentException("Bemerkung muss zwischen 10 und 200 Zeichen lang sein");
        }
        if (!validation.istZeitSinnvoll(dto.getVonZeit(), dto.getBisZeit())) {
            String error = validation.getZeitValidierungsFehler(dto.getVonZeit(), dto.getBisZeit());
            throw new IllegalArgumentException(error != null ? error : "Ungültige Zeitangabe");
        }
        if (!isRoomAvailable(dto.getZimmer(), dto.getDatum(), dto.getVonZeit(), dto.getBisZeit(), excludeId)) {
            throw new IllegalArgumentException("Das Zimmer ist zu dieser Zeit bereits reserviert");
        }
    }

    private boolean isRoomAvailable(Integer zimmer, LocalDate datum, LocalTime von, LocalTime bis, Long excludeId) {
        List<Reservation> conflicts = excludeId != null
                ? repository.findOverlappingReservationsExcludingId(zimmer, datum, von, bis, excludeId)
                : repository.findOverlappingReservations(zimmer, datum, von, bis);
        return conflicts.isEmpty();
    }

    private String generateUniqueKey(boolean isPrivate) {
        String key;
        do {
            key = UUID.randomUUID().toString();
        } while (isPrivate ? repository.findByPrivateKey(key).isPresent()
                           : repository.findByPublicKey(key).isPresent());
        return key;
    }
}
