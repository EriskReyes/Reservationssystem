package com.terminkalender.controller;

import com.terminkalender.dto.ReservationDTO;
import com.terminkalender.model.Reservation;
import com.terminkalender.service.ReservationService;
import com.terminkalender.service.ValidationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService service;
    private final ValidationService validation;

    public ReservationController(ReservationService service, ValidationService validation) {
        this.service = service;
        this.validation = validation;
    }

    @GetMapping("/neu")
    public String zeigeFormular(Model model) {
        model.addAttribute("reservationDTO", new ReservationDTO());
        model.addAttribute("verfuegbareZimmer", validation.getVerfuegbareZimmer());
        return "reservation-formular";
    }

    @PostMapping("/erstellen")
    public String erstelleReservation(@Valid @ModelAttribute ReservationDTO dto,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes attrs) {
        if (result.hasErrors()) {
            model.addAttribute("verfuegbareZimmer", validation.getVerfuegbareZimmer());
            return "reservation-formular";
        }

        try {
            if (!validation.istZeitSinnvoll(dto.getVonZeit(), dto.getBisZeit())) {
                String error = validation.getZeitValidierungsFehler(dto.getVonZeit(), dto.getBisZeit());
                result.rejectValue("bisZeit", "error.bisZeit", error);
                model.addAttribute("verfuegbareZimmer", validation.getVerfuegbareZimmer());
                return "reservation-formular";
            }

            Reservation reservation = service.erstelleReservation(dto);
            attrs.addFlashAttribute("erfolg", "Reservation erfolgreich erstellt!");
            return "redirect:/reservation/bestaetigung/" + reservation.getId();

        } catch (IllegalArgumentException e) {
            model.addAttribute("fehler", e.getMessage());
            model.addAttribute("verfuegbareZimmer", validation.getVerfuegbareZimmer());
            return "reservation-formular";
        }
    }

    @GetMapping("/bestaetigung/{id}")
    public String zeigeBestaetigung(@PathVariable Long id, Model model, RedirectAttributes attrs) {
        Optional<Reservation> res = service.findeNachId(id);
        if (res.isEmpty()) {
            attrs.addFlashAttribute("fehler", "Reservation nicht gefunden");
            return "redirect:/";
        }

        model.addAttribute("reservation", res.get());
        return "reservation-bestaetigung";
    }

    @GetMapping("/ansicht/{publicKey}")
    public String zeigeOeffentlicheAnsicht(@PathVariable String publicKey,
                                          Model model,
                                          RedirectAttributes attrs) {
        Optional<Reservation> res = service.findeNachPublicKey(publicKey);
        if (res.isEmpty()) {
            attrs.addFlashAttribute("fehler", "Reservation nicht gefunden");
            return "redirect:/";
        }

        model.addAttribute("reservation", res.get());
        return "reservation-ansicht";
    }

    @GetMapping("/bearbeiten/{privateKey}")
    public String zeigeBearbeitungsansicht(@PathVariable String privateKey,
                                          Model model,
                                          RedirectAttributes attrs) {
        Optional<Reservation> res = service.findeNachPrivateKey(privateKey);
        if (res.isEmpty()) {
            attrs.addFlashAttribute("fehler", "Reservation nicht gefunden");
            return "redirect:/";
        }

        Reservation reservation = res.get();
        ReservationDTO dto = new ReservationDTO();
        dto.setDatum(reservation.getDatum());
        dto.setVonZeit(reservation.getVonZeit());
        dto.setBisZeit(reservation.getBisZeit());
        dto.setZimmer(reservation.getZimmer());
        dto.setBemerkung(reservation.getBemerkung());
        dto.setTeilnehmerListe(reservation.getTeilnehmer().stream()
                .map(t -> t.getVorname() + " " + t.getNachname())
                .collect(Collectors.joining(", ")));

        model.addAttribute("reservationDTO", dto);
        model.addAttribute("reservation", reservation);
        model.addAttribute("verfuegbareZimmer", validation.getVerfuegbareZimmer());
        return "reservation-bearbeiten";
    }

    @PostMapping("/aktualisieren/{privateKey}")
    public String aktualisiereReservation(@PathVariable String privateKey,
                                         @Valid @ModelAttribute ReservationDTO dto,
                                         BindingResult result,
                                         Model model,
                                         RedirectAttributes attrs) {
        Optional<Reservation> res = service.findeNachPrivateKey(privateKey);
        if (res.isEmpty()) {
            attrs.addFlashAttribute("fehler", "Reservation nicht gefunden");
            return "redirect:/";
        }

        Reservation reservation = res.get();

        if (result.hasErrors()) {
            model.addAttribute("reservation", reservation);
            model.addAttribute("verfuegbareZimmer", validation.getVerfuegbareZimmer());
            return "reservation-bearbeiten";
        }

        try {
            if (!validation.istZeitSinnvoll(dto.getVonZeit(), dto.getBisZeit())) {
                String error = validation.getZeitValidierungsFehler(dto.getVonZeit(), dto.getBisZeit());
                result.rejectValue("bisZeit", "error.bisZeit", error);
                model.addAttribute("reservation", reservation);
                model.addAttribute("verfuegbareZimmer", validation.getVerfuegbareZimmer());
                return "reservation-bearbeiten";
            }

            service.aktualisiereReservation(reservation.getId(), dto);
            attrs.addFlashAttribute("erfolg", "Reservation erfolgreich aktualisiert!");
            return "redirect:/reservation/bearbeiten/" + privateKey;

        } catch (IllegalArgumentException e) {
            model.addAttribute("fehler", e.getMessage());
            model.addAttribute("reservation", reservation);
            model.addAttribute("verfuegbareZimmer", validation.getVerfuegbareZimmer());
            return "reservation-bearbeiten";
        }
    }

    @PostMapping("/loeschen/{privateKey}")
    public String loescheReservation(@PathVariable String privateKey, RedirectAttributes attrs) {
        Optional<Reservation> res = service.findeNachPrivateKey(privateKey);
        if (res.isEmpty()) {
            attrs.addFlashAttribute("fehler", "Reservation nicht gefunden");
            return "redirect:/";
        }

        try {
            service.loescheReservation(res.get().getId());
            attrs.addFlashAttribute("erfolg", "Reservation erfolgreich gelöscht!");
            return "redirect:/";
        } catch (Exception e) {
            attrs.addFlashAttribute("fehler", "Fehler beim Löschen der Reservation");
            return "redirect:/reservation/bearbeiten/" + privateKey;
        }
    }
}
