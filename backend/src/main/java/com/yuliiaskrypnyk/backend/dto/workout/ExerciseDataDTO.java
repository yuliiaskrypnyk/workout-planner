package com.yuliiaskrypnyk.backend.dto.workout;

import lombok.Builder;

@Builder
public record ExerciseDataDTO(
        String exerciseId,
        int sets,
        int reps,
        double weight) {
}
