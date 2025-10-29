package com.terminkalender.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "reservationen")
@Getter @Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Datum darf nicht leer sein")
    @Column(nullable = false)
    private LocalDate datum;

    @NotNull(message = "Von-Zeit darf nicht leer sein")
    @Column(nullable = false, name = "von_zeit")
    private LocalTime vonZeit;

    @NotNull(message = "Bis-Zeit darf nicht leer sein")
    @Column(nullable = false, name = "bis_zeit")
    private LocalTime bisZeit;

    @NotNull(message = "Zimmer muss ausgewählt werden")
    @Min(value = 101, message = "Ungültige Zimmernummer")
    @Max(value = 105, message = "Ungültige Zimmernummer")
    @Column(nullable = false)
    private Integer zimmer;

    @NotBlank(message = "Bemerkung darf nicht leer sein")
    @Size(min = 10, max = 200, message = "Bemerkung muss zwischen 10 und 200 Zeichen lang sein")
    @Column(nullable = false, length = 200)
    private String bemerkung;

    @Column(unique = true, nullable = false, length = 36, name = "private_schluessel")
    private String privateKey;

    @Column(unique = true, nullable = false, length = 36, name = "public_schluessel")
    private String publicKey;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Teilnehmer> teilnehmer = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "erstellt_am")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "aktualisiert_am")
    private LocalDateTime updatedAt;

    public Reservation(LocalDate datum, LocalTime vonZeit, LocalTime bisZeit,
                      Integer zimmer, String bemerkung) {
        this.datum = datum;
        this.vonZeit = vonZeit;
        this.bisZeit = bisZeit;
        this.zimmer = zimmer;
        this.bemerkung = bemerkung;
    }

    public void addTeilnehmer(Teilnehmer teilnehmer) {
        this.teilnehmer.add(teilnehmer);
        teilnehmer.setReservation(this);
    }

    public void removeTeilnehmer(Teilnehmer teilnehmer) {
        this.teilnehmer.remove(teilnehmer);
        teilnehmer.setReservation(null);
    }

    public void clearTeilnehmer() {
        new ArrayList<>(this.teilnehmer).forEach(this::removeTeilnehmer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
