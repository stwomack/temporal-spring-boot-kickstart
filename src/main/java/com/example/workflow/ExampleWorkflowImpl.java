package com.example.workflow;

import com.example.activity.ExampleActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;

@WorkflowImpl(taskQueues = "example-task-queue")
public class ExampleWorkflowImpl implements ExampleWorkflow {

    private final ExampleActivity activity = Workflow.newActivityStub(
            ExampleActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(5))
                    .setRetryOptions(
                            io.temporal.common.RetryOptions.newBuilder()
                                    .setMaximumAttempts(3)
                                    .build()
                    )
                    .build()
    );

    @Override
    public String execute(String input) {
        activity.logMessage("Starting workflow with input: " + input);

        String processed = activity.processData(input);

        Workflow.sleep(Duration.ofSeconds(2));

        activity.logMessage("Workflow completed");
        return "Processed: " + processed;
    }
}