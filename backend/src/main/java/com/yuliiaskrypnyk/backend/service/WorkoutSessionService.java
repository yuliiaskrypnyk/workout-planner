package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.dto.session.ExerciseSessionDataDTO;
import com.yuliiaskrypnyk.backend.dto.session.WorkoutSessionDTO;
import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.exercise.Exercise;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
import com.yuliiaskrypnyk.backend.model.session.ExerciseSessionData;
import com.yuliiaskrypnyk.backend.model.session.WorkoutSession;
import com.yuliiaskrypnyk.backend.repository.ExerciseRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutRepository workoutRepository;
    private final IdService idService;
    private final ExerciseRepository exerciseRepository;

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
                .workoutName(workoutTemplate.name())
                .startTime(LocalDateTime.now())
                .exercises(exerciseSessionDataList)
                .build();
    }

    public WorkoutSession completeWorkoutSession(String sessionId, WorkoutSessionDTO workoutSessionDTO) {
        List<ExerciseSessionData> updatedExercises = workoutSessionDTO.exercises().stream()
                .map(exercise -> ExerciseSessionData.builder()
                        .exerciseId(exercise.exerciseId())
                        .sets(exercise.sets())
                        .reps(exercise.reps())
                        .weight(exercise.weight())
                        .build())
                .toList();

        WorkoutSession completedSession = WorkoutSession.builder()
                .id(sessionId)
                .workoutName(workoutSessionDTO.workoutName())
                .startTime(workoutSessionDTO.startTime())
                .exercises(updatedExercises)
                .build();

        return workoutSessionRepository.save(completedSession);
    }

    public List<WorkoutSessionDTO> findAllWorkoutSessions() {
        List<WorkoutSession> sessions = workoutSessionRepository.findAll(Sort.by(Sort.Order.desc("startTime")));

        Set<String> exerciseIds = sessions.stream()
                .flatMap(s -> s.exercises().stream())
                .map(ExerciseSessionData::exerciseId)
                .collect(Collectors.toSet());

        Map<String, Exercise> exerciseMap = exerciseRepository.findAllById(exerciseIds).stream()
                .collect(Collectors.toMap(Exercise::id, Function.identity()));

        return sessions.stream()
                .map(session -> {
                    List<ExerciseSessionDataDTO> exerciseDtoList = session.exercises().stream()
                            .map(ex -> {
                                Exercise exercise = exerciseMap.get(ex.exerciseId());
                                return ExerciseSessionDataDTO.builder()
                                        .exerciseId(ex.exerciseId())
                                        .exerciseName(exercise != null ? exercise.name() : "Unknown Exercise")
                                        .exerciseImage(exercise != null ? exercise.image() : null)
                                        .sets(ex.sets())
                                        .reps(ex.reps())
                                        .weight(ex.weight())
                                        .build();
                            })
                            .toList();

                    return WorkoutSessionDTO.builder()
                            .id(session.id())
                            .workoutName(session.workoutName())
                            .startTime(session.startTime())
                            .exercises(exerciseDtoList)
                            .build();
                })
                .toList();
    }
}
