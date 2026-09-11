package com.example.maquinadeestados.dto;

import com.example.maquinadeestados.model.FileState;

import java.util.UUID;

public record StateResponse(UUID id, FileState state) {
}
