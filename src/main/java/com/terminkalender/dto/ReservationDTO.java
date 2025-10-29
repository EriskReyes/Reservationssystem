package com.terminkalender.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    @NotNull(message = "Datum darf nicht leer sein")
    @Future(message = "Datum muss in der Zukunft liegen")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datum;

    @NotNull(message = "Von-Zeit darf nicht leer sein")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime vonZeit;

    @NotNull(message = "Bis-Zeit darf nicht leer sein")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime bisZeit;

    @NotNull(message = "Zimmer muss ausgewählt werden")
    @Min(value = 101, message = "Ungültige Zimmernummer")
    @Max(value = 105, message = "Ungültige Zimmernummer")
    private Integer zimmer;

    @NotBlank(message = "Bemerkung darf nicht leer sein")
    @Size(min = 10, max = 200, message = "Bemerkung muss zwischen 10 und 200 Zeichen lang sein")
    private String bemerkung;

    @NotBlank(message = "Mindestens ein Teilnehmer muss angegeben werden")
    @Pattern(regexp = "^[A-Za-zÄäÖöÜüß\\s-]+(,\\s*[A-Za-zÄäÖöÜüß\\s-]+)*$",
            message = "Ungültiges Format für Teilnehmer. Format: Vorname Nachname, Vorname Nachname")
    private String teilnehmerListe;
}
