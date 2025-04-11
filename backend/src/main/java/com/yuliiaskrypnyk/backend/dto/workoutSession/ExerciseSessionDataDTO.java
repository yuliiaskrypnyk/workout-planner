package com.yuliiaskrypnyk.backend.dto.workoutSession;

import lombok.Builder;

@Builder
public record ExerciseSessionDataDTO(
        String exerciseId,
        int sets,
        int reps,
        double weight
) {
}
