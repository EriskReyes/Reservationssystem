package com.terminkalender.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zÄäÖöÜüß\\s-]+$");
    private static final int MIN_BEMERKUNG_LAENGE = 10;
    private static final int MAX_BEMERKUNG_LAENGE = 200;

    public List<String[]> validiereTeilnehmer(String teilnehmerString) {
        if (teilnehmerString == null || teilnehmerString.trim().isEmpty()) {
            throw new IllegalArgumentException("Teilnehmerliste darf nicht leer sein");
        }

        List<String[]> teilnehmer = new ArrayList<>();
        String[] namen = teilnehmerString.split(",");

        for (String name : namen) {
            name = name.trim();
            if (name.isEmpty()) continue;

            if (!NAME_PATTERN.matcher(name).matches()) {
                throw new IllegalArgumentException("Ungültiger Name: " + name + ". Nur Buchstaben sind erlaubt.");
            }

            String[] teile = name.split("\\s+");
            if (teile.length < 2) {
                throw new IllegalArgumentException("Ungültiges Format für: " + name + ". Bitte Vor- und Nachname angeben.");
            }

            teilnehmer.add(new String[]{
                teile[0],
                String.join(" ", Arrays.copyOfRange(teile, 1, teile.length))
            });
        }

        if (teilnehmer.isEmpty()) {
            throw new IllegalArgumentException("Mindestens ein Teilnehmer muss angegeben werden");
        }

        return teilnehmer;
    }

    public boolean istDatumGueltig(LocalDate datum) {
        return datum != null && datum.isAfter(LocalDate.now());
    }

    public boolean istZimmerGueltig(Integer zimmer) {
        return zimmer != null && zimmer >= 101 && zimmer <= 105;
    }

    public boolean istBemerkungGueltig(String bemerkung) {
        if (bemerkung == null || bemerkung.trim().isEmpty()) {
            return false;
        }
        int length = bemerkung.trim().length();
        return length >= MIN_BEMERKUNG_LAENGE && length <= MAX_BEMERKUNG_LAENGE;
    }

    public boolean istZeitSinnvoll(LocalTime von, LocalTime bis) {
        if (von == null || bis == null) return false;
        return von.isBefore(bis) &&
               (von.plusMinutes(15).isBefore(bis) || von.plusMinutes(15).equals(bis));
    }

    public String getZeitValidierungsFehler(LocalTime von, LocalTime bis) {
        if (von == null) return "Von-Zeit darf nicht leer sein";
        if (bis == null) return "Bis-Zeit darf nicht leer sein";
        if (!von.isBefore(bis)) return "Von-Zeit muss vor Bis-Zeit liegen";
        if (von.plusMinutes(15).isAfter(bis)) return "Reservation muss mindestens 15 Minuten dauern";
        return null;
    }

    public List<Integer> getVerfuegbareZimmer() {
        return Arrays.asList(101, 102, 103, 104, 105);
    }
}
