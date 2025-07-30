package com.example.activity;

import io.temporal.spring.boot.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl(taskQueues = "example-task-queue")
public class ExampleActivityImpl implements ExampleActivity {

    private static final Logger logger = LoggerFactory.getLogger(ExampleActivityImpl.class);

    @Override
    public String processData(String data) {
        logger.info("Processing data: {}", data);

        // Simulate processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return data.toUpperCase() + "_PROCESSED";
    }

    @Override
    public void logMessage(String message) {
        logger.info("Activity log: {}", message);
    }
}