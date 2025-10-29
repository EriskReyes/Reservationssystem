package com.terminkalender.config;

import com.terminkalender.model.Reservation;
import com.terminkalender.model.Teilnehmer;
import com.terminkalender.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Component
@Profile("dev")
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final ReservationRepository repository;

    public DataInitializer(ReservationRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        if (repository.count() > 0) {
            return;
        }

        log.info("Inicializando datos de prueba...");

        createReservation(5, 101, 9, 0, 11, 0,
            "Teambesprechung für Projekt Alpha mit allen Teammitgliedern",
            new String[][]{{"Max", "Mustermann"}, {"Anna", "Schmidt"}});

        createReservation(7, 103, 14, 0, 16, 30,
            "Kundenpräsentation für neues Produkt mit ausführlicher Demonstration",
            new String[][]{{"Peter", "Müller"}, {"Sarah", "Weber"}, {"Thomas", "Fischer"}});

        createReservation(10, 105, 10, 0, 12, 0,
            "Workshop für neue Mitarbeiter zur Einführung in die Unternehmenskultur",
            new String[][]{{"Julia", "Schneider"}});

        log.info("Datos de prueba creados exitosamente");
    }

    private void createReservation(int daysFromNow, int room, int startHour, int startMin,
                                   int endHour, int endMin, String remark, String[][] participants) {
        Reservation res = new Reservation();
        res.setDatum(LocalDate.now().plusDays(daysFromNow));
        res.setVonZeit(LocalTime.of(startHour, startMin));
        res.setBisZeit(LocalTime.of(endHour, endMin));
        res.setZimmer(room);
        res.setBemerkung(remark);
        res.setPrivateKey(UUID.randomUUID().toString());
        res.setPublicKey(UUID.randomUUID().toString());

        for (String[] participant : participants) {
            res.addTeilnehmer(new Teilnehmer(participant[0], participant[1]));
        }

        repository.save(res);
    }
}
