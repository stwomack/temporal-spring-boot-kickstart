package com.example.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ExampleActivity {
    @ActivityMethod
    String processData(String data);

    @ActivityMethod
    void logMessage(String message);
}