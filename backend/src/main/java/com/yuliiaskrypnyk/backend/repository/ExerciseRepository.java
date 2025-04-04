package com.yuliiaskrypnyk.backend.repository;

import com.yuliiaskrypnyk.backend.model.exercise.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {
}
