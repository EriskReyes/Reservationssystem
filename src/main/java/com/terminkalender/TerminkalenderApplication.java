package com.terminkalender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
            log.error("Failed to start application", e);
            System.exit(1);
        }
    }
}
