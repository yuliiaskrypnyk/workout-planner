package com.yuliiaskrypnyk.backend.model.session;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Document(collection = "workout_sessions")
public record WorkoutSession(
        @Id String id,
        String workoutName,
        LocalDateTime startTime,
        List<ExerciseSessionData> exercises
) {
}
