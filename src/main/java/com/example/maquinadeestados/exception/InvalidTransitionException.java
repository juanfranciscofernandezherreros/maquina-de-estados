package com.example.maquinadeestados.exception;

import com.example.maquinadeestados.model.FileEvent;
import com.example.maquinadeestados.model.FileState;

public class InvalidTransitionException extends RuntimeException {

    public InvalidTransitionException(FileState state, FileEvent event) {
        super("Transición no permitida: estado=" + state + ", evento=" + event);
    }
}
