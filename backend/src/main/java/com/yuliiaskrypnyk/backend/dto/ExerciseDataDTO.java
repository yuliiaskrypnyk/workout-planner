package com.yuliiaskrypnyk.backend.dto;

import lombok.Builder;

@Builder
public record ExerciseDataDTO(
        String exerciseId,
        //String name,
        int sets,
        int reps,
        double weight) {
}
