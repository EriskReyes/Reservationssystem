package com.terminkalender.repository;

import com.terminkalender.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByPrivateKey(String privateKey);

    Optional<Reservation> findByPublicKey(String publicKey);

    List<Reservation> findByZimmerAndDatum(Integer zimmer, LocalDate datum);

    List<Reservation> findByZimmer(Integer zimmer);

    List<Reservation> findByDatum(LocalDate datum);

    @Query("SELECT r FROM Reservation r WHERE r.zimmer = :zimmer " +
           "AND r.datum = :datum " +
           "AND ((r.vonZeit < :bisZeit AND r.bisZeit > :vonZeit))")
    List<Reservation> findOverlappingReservations(
            @Param("zimmer") Integer zimmer,
            @Param("datum") LocalDate datum,
            @Param("vonZeit") LocalTime vonZeit,
            @Param("bisZeit") LocalTime bisZeit
    );

    @Query("SELECT r FROM Reservation r WHERE r.zimmer = :zimmer " +
           "AND r.datum = :datum " +
           "AND r.id != :excludeId " +
           "AND ((r.vonZeit < :bisZeit AND r.bisZeit > :vonZeit))")
    List<Reservation> findOverlappingReservationsExcludingId(
            @Param("zimmer") Integer zimmer,
            @Param("datum") LocalDate datum,
            @Param("vonZeit") LocalTime vonZeit,
            @Param("bisZeit") LocalTime bisZeit,
            @Param("excludeId") Long excludeId
    );

    @Query("SELECT r FROM Reservation r WHERE r.datum >= CURRENT_DATE ORDER BY r.datum, r.vonZeit")
    List<Reservation> findAllFutureReservations();

    @Query("SELECT r FROM Reservation r WHERE r.datum BETWEEN :vonDatum AND :bisDatum ORDER BY r.datum, r.vonZeit")
    List<Reservation> findReservationsBetweenDates(
            @Param("vonDatum") LocalDate vonDatum,
            @Param("bisDatum") LocalDate bisDatum
    );
}
