package com.yuliiaskrypnyk.backend.model.workoutSession;

import lombok.Builder;

@Builder
public record ExerciseSessionData(
        String exerciseId,
        int sets,
        int reps,
        double weight
) {
}
