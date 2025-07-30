package com.example.controller;

import com.example.service.WorkflowClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final WorkflowClientService workflowClientService;

    public WorkflowController(WorkflowClientService workflowClientService) {
        this.workflowClientService = workflowClientService;
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeWorkflow(@RequestBody WorkflowRequest request) {
        String result = workflowClientService.executeWorkflow(request.getInput());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/execute-async")
    public ResponseEntity<String> executeWorkflowAsync(@RequestBody WorkflowRequest request) {
        String workflowId = workflowClientService.executeWorkflowAsync(request.getInput());
        return ResponseEntity.ok("Workflow started with ID: " + workflowId);
    }

    @GetMapping("/result/{workflowId}")
    public ResponseEntity<String> getWorkflowResult(@PathVariable String workflowId) {
        String result = workflowClientService.getWorkflowResult(workflowId);
        return ResponseEntity.ok(result);
    }

    public static class WorkflowRequest {
        private String input;

        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
    }
}