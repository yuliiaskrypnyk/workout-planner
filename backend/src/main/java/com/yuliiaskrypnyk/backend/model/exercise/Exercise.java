package com.yuliiaskrypnyk.backend.model.exercise;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "exercises")
public record Exercise(
        @Id String id,
        String name,
        String description,
        String image) {
}
