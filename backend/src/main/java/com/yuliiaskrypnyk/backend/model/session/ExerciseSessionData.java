package com.yuliiaskrypnyk.backend.model.session;

import lombok.Builder;

@Builder
public record ExerciseSessionData(
        String exerciseId,
        int sets,
        int reps,
        double weight
) {
}
