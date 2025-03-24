package com.yuliiaskrypnyk.backend.repository;

import com.yuliiaskrypnyk.backend.model.Workout;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends MongoRepository<Workout, String> {
}