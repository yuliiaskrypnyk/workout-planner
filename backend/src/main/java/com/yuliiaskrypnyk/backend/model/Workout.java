package com.yuliiaskrypnyk.backend.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document(collection = "workouts")
public record Workout(
        @Id String id,
        String name,
        List<ExerciseData> exercises
) {
}