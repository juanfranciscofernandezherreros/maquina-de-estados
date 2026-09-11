package com.example.maquinadeestados.service;

import com.example.maquinadeestados.exception.InvalidTransitionException;
import com.example.maquinadeestados.model.FileEvent;
import com.example.maquinadeestados.model.FileState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileStateMachineTest {

    private final FileStateMachine stateMachine = new FileStateMachine();

    @Test
    void shouldCompleteHappyPath() {
        FileState state = FileState.RECEIVED;

        state = stateMachine.transition(state, FileEvent.DATA_READY);
        assertEquals(FileState.DATA_READY, state);

        state = stateMachine.transition(state, FileEvent.FILE_CREATED);
        assertEquals(FileState.FILE_CREATED, state);

        state = stateMachine.transition(state, FileEvent.FILE_SAVED);
        assertEquals(FileState.FILE_SAVED, state);

        state = stateMachine.transition(state, FileEvent.RESPONSE_SENT);
        assertEquals(FileState.RESPONSE_SENT, state);

        state = stateMachine.transition(state, FileEvent.ACK_RECEIVED);
        assertEquals(FileState.FINISHED, state);
    }

    @Test
    void shouldMoveToErrorWhenFailEventArrives() {
        assertEquals(
            FileState.ERROR,
            stateMachine.transition(FileState.FILE_CREATED, FileEvent.FAIL)
        );
    }

    @Test
    void shouldRejectInvalidTransition() {
        assertThrows(
            InvalidTransitionException.class,
            () -> stateMachine.transition(FileState.RECEIVED, FileEvent.FILE_SAVED)
        );
    }

    @Test
    void shouldNotLeaveTerminalState() {
        assertThrows(
            InvalidTransitionException.class,
            () -> stateMachine.transition(FileState.FINISHED, FileEvent.FAIL)
        );
    }
}
