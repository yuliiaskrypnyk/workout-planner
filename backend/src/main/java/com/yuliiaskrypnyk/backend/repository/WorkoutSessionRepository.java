package com.yuliiaskrypnyk.backend.repository;

import com.yuliiaskrypnyk.backend.model.session.WorkoutSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutSessionRepository extends MongoRepository<WorkoutSession, String> {
}
