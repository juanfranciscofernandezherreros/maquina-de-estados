package com.example.maquinadeestados.controller;

import com.example.maquinadeestados.dto.StateResponse;
import com.example.maquinadeestados.dto.TransitionRequest;
import com.example.maquinadeestados.model.FileState;
import com.example.maquinadeestados.service.FileWorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class StateMachineController {

    private final FileWorkflowService workflowService;

    public StateMachineController(FileWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StateResponse create() {
        UUID id = workflowService.create();
        return new StateResponse(id, workflowService.getState(id));
    }

    @GetMapping("/{id}")
    public StateResponse get(@PathVariable UUID id) {
        return new StateResponse(id, workflowService.getState(id));
    }

    @PostMapping("/{id}/events")
    public StateResponse fire(@PathVariable UUID id, @RequestBody TransitionRequest request) {
        FileState state = workflowService.fire(id, request.event());
        return new StateResponse(id, state);
    }
}
