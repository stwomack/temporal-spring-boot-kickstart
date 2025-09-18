package com.example;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TemporalSandboxApplication {

    @Autowired(required = false)
    private io.opentelemetry.api.OpenTelemetry openTelemetry;

    public static void main(String[] args) {
        SpringApplication.run(TemporalSandboxApplication.class, args);
    }

    @PostConstruct
    public void checkTracing() {
        System.out.println("OpenTelemetry bean found: " + (openTelemetry != null));
        if (openTelemetry != null) {
            System.out.println("OpenTelemetry class: " + openTelemetry.getClass().getName());
        }
    }
}