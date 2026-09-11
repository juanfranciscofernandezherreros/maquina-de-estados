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
    void shouldPayAndShipOrder() {
        FileState state = stateMachine.transition(FileState.CREATED, FileEvent.PAY);
        assertEquals(FileState.PAID, state);

        state = stateMachine.transition(state, FileEvent.SHIP);
        assertEquals(FileState.SHIPPED, state);
    }

    @Test
    void shouldCancelCreatedOrder() {
        assertEquals(FileState.CANCELLED,
            stateMachine.transition(FileState.CREATED, FileEvent.CANCEL));
    }

    @Test
    void shouldRefundPaidOrder() {
        assertEquals(FileState.REFUNDED,
            stateMachine.transition(FileState.PAID, FileEvent.REFUND));
    }

    @Test
    void shouldRejectInvalidTransition() {
        assertThrows(InvalidTransitionException.class,
            () -> stateMachine.transition(FileState.CREATED, FileEvent.SHIP));
    }
}
