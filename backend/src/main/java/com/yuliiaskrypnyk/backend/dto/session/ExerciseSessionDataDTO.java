package com.yuliiaskrypnyk.backend.dto.session;

import lombok.Builder;

@Builder
public record ExerciseSessionDataDTO(
        String exerciseId,
        String exerciseName,
        String exerciseImage,
        int sets,
        int reps,
        double weight
) {
}