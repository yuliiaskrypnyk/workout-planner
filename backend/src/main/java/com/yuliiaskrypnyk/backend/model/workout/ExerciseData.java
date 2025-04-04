package com.yuliiaskrypnyk.backend.model.workout;

import lombok.Builder;

@Builder
public record ExerciseData(
        String exerciseId,
        int sets,
        int reps,
        double weight) {
}