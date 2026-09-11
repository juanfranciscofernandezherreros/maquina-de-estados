package com.example.maquinadeestados.service;

import com.example.maquinadeestados.exception.InvalidTransitionException;
import com.example.maquinadeestados.model.FileEvent;
import com.example.maquinadeestados.model.FileState;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileStateMachine {

    private static final Map<Key, FileState> TRANSITIONS = Map.of(
        new Key(FileState.RECEIVED, FileEvent.DATA_READY), FileState.DATA_READY,
        new Key(FileState.DATA_READY, FileEvent.FILE_CREATED), FileState.FILE_CREATED,
        new Key(FileState.FILE_CREATED, FileEvent.FILE_SAVED), FileState.FILE_SAVED,
        new Key(FileState.FILE_SAVED, FileEvent.RESPONSE_SENT), FileState.RESPONSE_SENT,
        new Key(FileState.RESPONSE_SENT, FileEvent.ACK_RECEIVED), FileState.FINISHED
    );

    public FileState transition(FileState currentState, FileEvent event) {
        if (event == FileEvent.FAIL && !isTerminal(currentState)) {
            return FileState.ERROR;
        }

        FileState nextState = TRANSITIONS.get(new Key(currentState, event));
        if (nextState == null) {
            throw new InvalidTransitionException(currentState, event);
        }

        return nextState;
    }

    private boolean isTerminal(FileState state) {
        return state == FileState.FINISHED || state == FileState.ERROR;
    }

    private record Key(FileState state, FileEvent event) {
    }
}
