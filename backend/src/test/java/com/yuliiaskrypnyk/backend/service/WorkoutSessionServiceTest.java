package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.dto.workoutSession.ExerciseSessionDataDTO;
import com.yuliiaskrypnyk.backend.dto.workoutSession.WorkoutSessionDTO;
import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
import com.yuliiaskrypnyk.backend.model.workoutSession.ExerciseSessionData;
import com.yuliiaskrypnyk.backend.model.workoutSession.WorkoutSession;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutSessionServiceTest {

    @Mock
    private WorkoutSessionRepository mockWorkoutSessionRepository;

    @Mock
    private WorkoutRepository mockWorkoutRepository;

    @Mock
    private IdService mockIdService;

    @InjectMocks
    private WorkoutSessionService workoutSessionService;

    private final List<Workout> workouts = List.of(
            Workout.builder().id("1").name("Leg workout").exercises(Collections.emptyList()).build(),
            Workout.builder().id("2").name("Arm Workout").exercises(Collections.emptyList()).build()
    );

    // Start workout session
    @Test
    void startWorkoutSession_shouldReturnWorkoutSession_whenWorkoutExists() {
        String workoutId = "1";
        Workout workoutTemplate = workouts.get(0);
        String sessionId = "session123";

        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.of(workoutTemplate));
        when(mockIdService.generateId()).thenReturn(sessionId);

        WorkoutSession workoutSession = workoutSessionService.startWorkoutSession(workoutId);

        assertNotNull(workoutSession);
        assertEquals(sessionId, workoutSession.id());
        assertEquals(workoutId, workoutSession.workoutId());
        assertNotNull(workoutSession.startTime());
        verify(mockWorkoutRepository, times(1)).findById(workoutId);
        verify(mockIdService, times(1)).generateId();
    }

    @Test
    void startWorkoutSession_shouldThrowResourceNotFoundException_whenWorkoutDoesNotExist() {
        String workoutId = "3";

        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                workoutSessionService.startWorkoutSession(workoutId)
        );

        assertEquals("Requested Workout was not found.", exception.getMessage());
        verify(mockWorkoutRepository, times(1)).findById(workoutId);
    }

    // Complete workout session
    @Test
    void completeWorkoutSession_shouldReturnCompletedSession_whenSessionIsValid() {
        String workoutId = "1";
        String sessionId = "3";
        LocalDateTime startTime = LocalDateTime.now();

        WorkoutSessionDTO workoutSessionDTO = WorkoutSessionDTO.builder()
                .workoutId(workoutId)
                .startTime(startTime)
                .exercises(List.of(
                        ExerciseSessionDataDTO.builder()
                                .exerciseId("2")
                                .sets(50)
                                .reps(20)
                                .weight(200)
                                .build()
                ))
                .build();

        WorkoutSession workoutSession = WorkoutSession.builder()
                .id(sessionId)
                .workoutId(workoutId)
                .startTime(startTime)
                .exercises(workoutSessionDTO.exercises().stream()
                        .map(exercise -> new ExerciseSessionData(
                                exercise.exerciseId(),
                                exercise.sets(),
                                exercise.reps(),
                                exercise.weight()
                        ))
                        .toList())
                .build();

        when(mockIdService.generateId()).thenReturn(sessionId);

        when(mockWorkoutSessionRepository.save(any(WorkoutSession.class))).thenReturn(workoutSession);

        WorkoutSession completedSession = workoutSessionService.completeWorkoutSession(workoutSessionDTO);

        assertNotNull(completedSession);
        assertEquals(sessionId, completedSession.id());
        assertEquals(workoutSessionDTO.workoutId(), completedSession.workoutId());
        assertEquals(workoutSessionDTO.startTime(), completedSession.startTime());
        assertEquals(workoutSessionDTO.exercises().size(), completedSession.exercises().size());
        verify(mockWorkoutSessionRepository, times(1)).save(any(WorkoutSession.class));
    }
}
