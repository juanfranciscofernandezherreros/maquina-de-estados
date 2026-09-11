package com.example.maquinadeestados.service;

import com.example.maquinadeestados.model.FileEvent;
import com.example.maquinadeestados.model.FileState;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileWorkflowServiceTest {

    private final FileWorkflowService workflow = new FileWorkflowService(new FileStateMachine());

    @Test
    void shouldCreateOrderInCreatedState() {
        UUID id = workflow.create();
        assertEquals(FileState.CREATED, workflow.getState(id));
    }

    @Test
    void shouldPersistStateInMemoryAfterEvent() {
        UUID id = workflow.create();
        workflow.fire(id, FileEvent.PAY);
        assertEquals(FileState.PAID, workflow.getState(id));
    }
}
