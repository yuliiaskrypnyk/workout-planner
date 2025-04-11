package com.yuliiaskrypnyk.backend.dto.session;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record WorkoutSessionDTO(
        String workoutId,
        LocalDateTime startTime,
        List<ExerciseSessionDataDTO> exercises
) {
}
