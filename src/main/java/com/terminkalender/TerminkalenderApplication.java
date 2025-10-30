package com.terminkalender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class TerminkalenderApplication {

    private static final Logger log = LoggerFactory.getLogger(TerminkalenderApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(TerminkalenderApplication.class, args);
        } catch (Exception e) {
            if (e.getClass().getName().contains("SilentExitException")) {
                throw e;
            }
            log.error("Anwendung konnte nicht gestartet werden", e);
            System.exit(1);
        }
    }

    @Bean
    public CommandLineRunner openBrowser(Environment environment) {
        return args -> {
            String port = environment.getProperty("server.port", "8080");
            String url = "http://localhost:" + port;

            log.info("Anwendung erfolgreich gestartet!");
            log.info("Browser wird geöffnet: {}", url);

            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                    log.info("Browser erfolgreich geöffnet");
                } else {
                    log.warn("Desktop-Browsing nicht unterstützt. Bitte manuell öffnen: {}", url);
                }
            } catch (Exception e) {
                log.error("Browser konnte nicht automatisch geöffnet werden. Bitte manuell öffnen: {}", url);
                log.error("Fehler: {}", e.getMessage());
            }
        };
    }
}
//