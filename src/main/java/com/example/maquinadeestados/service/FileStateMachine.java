package com.example.maquinadeestados.service;

import com.example.maquinadeestados.exception.InvalidTransitionException;
import com.example.maquinadeestados.model.FileEvent;
import com.example.maquinadeestados.model.FileState;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileStateMachine {

    private static final Map<Key, FileState> TRANSITIONS = Map.of(
        new Key(FileState.CREATED, FileEvent.PAY), FileState.PAID,
        new Key(FileState.CREATED, FileEvent.CANCEL), FileState.CANCELLED,
        new Key(FileState.PAID, FileEvent.SHIP), FileState.SHIPPED,
        new Key(FileState.PAID, FileEvent.REFUND), FileState.REFUNDED
    );

    public FileState transition(FileState currentState, FileEvent event) {
        FileState nextState = TRANSITIONS.get(new Key(currentState, event));
        if (nextState == null) {
            throw new InvalidTransitionException(currentState, event);
        }
        return nextState;
    }

    private record Key(FileState state, FileEvent event) {
    }
}
