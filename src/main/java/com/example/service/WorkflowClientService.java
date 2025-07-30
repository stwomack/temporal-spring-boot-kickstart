package com.example.service;

import com.example.exception.WorkflowExecutionException;
import com.example.workflow.ExampleWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class WorkflowClientService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowClientService.class);

    private final WorkflowClient workflowClient;

    public WorkflowClientService(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    private final String taskQueue = "example-task-queue";

    public String executeWorkflow(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        
        logger.info("Starting workflow execution with input: {}", input);

        try {
            ExampleWorkflow workflow = workflowClient.newWorkflowStub(
                    ExampleWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setTaskQueue(taskQueue)
                            .setWorkflowId("example-workflow-" + System.currentTimeMillis())
                            .setWorkflowExecutionTimeout(Duration.ofMinutes(10))
                            .build()
            );

            String result = workflow.execute(input);
            logger.info("Workflow completed with result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Failed to execute workflow with input: {}", input, e);
            throw new WorkflowExecutionException("Failed to execute workflow: " + e.getMessage(), e);
        }
    }

    public String executeWorkflowAsync(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        
        logger.info("Starting async workflow execution with input: {}", input);

        try {
            ExampleWorkflow workflow = workflowClient.newWorkflowStub(
                    ExampleWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setTaskQueue(taskQueue)
                            .setWorkflowId("example-workflow-async-" + System.currentTimeMillis())
                            .setWorkflowExecutionTimeout(Duration.ofMinutes(10))
                            .build()
            );

            WorkflowClient.start(workflow::execute, input);
            String workflowId = WorkflowStub.fromTyped(workflow).getExecution().getWorkflowId();
            logger.info("Async workflow started with ID: {}", workflowId);
            return workflowId;
        } catch (Exception e) {
            logger.error("Failed to start async workflow with input: {}", input, e);
            throw new WorkflowExecutionException("Failed to start async workflow: " + e.getMessage(), e);
        }
    }

    public String getWorkflowResult(String workflowId) {
        if (workflowId == null || workflowId.trim().isEmpty()) {
            throw new IllegalArgumentException("Workflow ID cannot be null or empty");
        }
        
        logger.info("Getting result for workflow: {}", workflowId);

        try {
            WorkflowStub workflowStub = workflowClient.newUntypedWorkflowStub(workflowId);
            String result = workflowStub.getResult(String.class);
            logger.info("Workflow {} result: {}", workflowId, result);
            return result;
        } catch (Exception e) {
            logger.error("Failed to get result for workflow: {}", workflowId, e);
            throw new WorkflowExecutionException("Failed to get workflow result: " + e.getMessage(), e);
        }
    }
}