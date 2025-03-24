package com.yuliiaskrypnyk.backend.dto;

public record ExerciseDataDTO(
        String name,
        int sets,
        int reps,
        double weight) {
}
