package com.terminkalender.controller;

import com.terminkalender.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

    private final ReservationService service;

    public IndexController(ReservationService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String zeigHauptseite(Model model) {
        return "index";
    }

    @PostMapping("/schluessel-suche")
    public String sucheNachSchluessel(@RequestParam("schluessel") String key,
                                     RedirectAttributes attrs) {
        if (key == null || key.trim().isEmpty()) {
            attrs.addFlashAttribute("fehler", "Bitte geben Sie einen Schlüssel ein");
            return "redirect:/";
        }

        key = key.trim();

        if (service.findeNachPrivateKey(key).isPresent()) {
            return "redirect:/reservation/bearbeiten/" + key;
        }

        if (service.findeNachPublicKey(key).isPresent()) {
            return "redirect:/reservation/ansicht/" + key;
        }

        attrs.addFlashAttribute("fehler", "Keine Reservation mit diesem Schlüssel gefunden");
        return "redirect:/";
    }
}
