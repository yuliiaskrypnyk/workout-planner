package com.yuliiaskrypnyk.backend.dto.session;

import lombok.Builder;

@Builder
public record ExerciseSessionDataDTO(
        String exerciseId,
        int sets,
        int reps,
        double weight
) {
}
