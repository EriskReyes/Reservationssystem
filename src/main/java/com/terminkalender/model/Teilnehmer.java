package com.terminkalender.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "teilnehmer")
@Getter @Setter
@NoArgsConstructor
public class Teilnehmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vorname darf nicht leer sein")
    @Pattern(regexp = "^[A-Za-zÄäÖöÜüß\\s-]+$", message = "Vorname darf nur Buchstaben enthalten")
    @Column(nullable = false, length = 50)
    private String vorname;

    @NotBlank(message = "Nachname darf nicht leer sein")
    @Pattern(regexp = "^[A-Za-zÄäÖöÜüß\\s-]+$", message = "Nachname darf nur Buchstaben enthalten")
    @Column(nullable = false, length = 50)
    private String nachname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    public Teilnehmer(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Teilnehmer(String vorname, String nachname, Reservation reservation) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.reservation = reservation;
    }

    public String getVollstaendigerName() {
        return vorname + " " + nachname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teilnehmer)) return false;
        Teilnehmer that = (Teilnehmer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
