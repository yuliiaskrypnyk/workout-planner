package com.yuliiaskrypnyk.backend.dto.workout;

import lombok.Builder;

import java.util.List;

@Builder
public record WorkoutDTO(
        String name,
        List<ExerciseDataDTO> exercises) {
}