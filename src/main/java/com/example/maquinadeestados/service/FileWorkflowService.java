package com.example.maquinadeestados.service;

import com.example.maquinadeestados.model.FileEvent;
import com.example.maquinadeestados.model.FileState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileWorkflowService {

    private final FileStateMachine stateMachine;
    private final Map<UUID, FileState> states = new ConcurrentHashMap<>();

    public FileWorkflowService(FileStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public UUID create() {
        UUID id = UUID.randomUUID();
        states.put(id, FileState.CREATED);
        return id;
    }

    public FileState getState(UUID id) {
        FileState state = states.get(id);
        if (state == null) {
            throw new IllegalArgumentException("No existe el pedido: " + id);
        }
        return state;
    }

    public FileState fire(UUID id, FileEvent event) {
        return states.compute(id, (key, currentState) -> {
            if (currentState == null) {
                throw new IllegalArgumentException("No existe el pedido: " + id);
            }
            return stateMachine.transition(currentState, event);
        });
    }
}
