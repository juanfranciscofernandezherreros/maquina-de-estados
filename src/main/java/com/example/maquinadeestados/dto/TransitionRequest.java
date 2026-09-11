package com.example.maquinadeestados.dto;

import com.example.maquinadeestados.model.FileEvent;

public record TransitionRequest(FileEvent event) {
}
