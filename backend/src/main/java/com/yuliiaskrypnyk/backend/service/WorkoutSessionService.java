package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
import com.yuliiaskrypnyk.backend.model.workoutSession.ExerciseSessionData;
import com.yuliiaskrypnyk.backend.model.workoutSession.WorkoutSession;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutRepository workoutRepository;
    private final IdService idService;

    public WorkoutSession startWorkoutSession(String workoutId) {
        Workout workoutTemplate = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout"));

        String sessionId = idService.generateId();

        List<ExerciseSessionData> exerciseSessionDataList = workoutTemplate.exercises().stream()
                .map(workoutExercise -> ExerciseSessionData.builder()
                        .exerciseId(workoutExercise.exerciseId())
                        .sets(workoutExercise.sets())
                        .reps(workoutExercise.reps())
                        .weight(workoutExercise.weight())
                        .build())
                .toList();

        return WorkoutSession.builder()
                .id(sessionId)
                .workoutId(workoutId)
                .startTime(LocalDateTime.now())
                .exercises(exerciseSessionDataList)
                .build();
    }

    public WorkoutSession completeWorkoutSession(WorkoutSession session) {
        WorkoutSession completedSession = WorkoutSession.builder()
                .id(session.id())
                .workoutId(session.workoutId())
                .startTime(session.startTime())
                .exercises(session.exercises())
                .build();

        return workoutSessionRepository.save(completedSession);
    }
}
