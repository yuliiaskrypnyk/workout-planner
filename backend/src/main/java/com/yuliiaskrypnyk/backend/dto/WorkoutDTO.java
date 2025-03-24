package com.yuliiaskrypnyk.backend.dto;

import java.util.List;

public record WorkoutDTO(
        String name,
        List<ExerciseDataDTO> exercises) {
}