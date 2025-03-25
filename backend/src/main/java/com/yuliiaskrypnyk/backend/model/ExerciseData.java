package com.yuliiaskrypnyk.backend.model;

import lombok.Builder;

@Builder
public record ExerciseData(
        String exerciseId,
        int sets,
        int reps,
        double weight) {
}