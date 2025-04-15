package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.dto.session.ExerciseSessionDataDTO;
import com.yuliiaskrypnyk.backend.dto.session.WorkoutSessionDTO;
import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.exercise.Exercise;
import com.yuliiaskrypnyk.backend.model.workout.ExerciseData;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
import com.yuliiaskrypnyk.backend.model.session.ExerciseSessionData;
import com.yuliiaskrypnyk.backend.model.session.WorkoutSession;
import com.yuliiaskrypnyk.backend.repository.ExerciseRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutSessionServiceTest {

    @Mock
    private WorkoutSessionRepository mockWorkoutSessionRepository;

    @Mock
    private WorkoutRepository mockWorkoutRepository;

    @Mock
    private ExerciseRepository mockExerciseRepository;

    @Mock
    private IdService mockIdService;

    @InjectMocks
    private WorkoutSessionService workoutSessionService;

    // Start workout session
    @Test
    void startWorkoutSession_shouldReturnWorkoutSession_whenWorkoutExists() {
        String workoutId = "1";
        String sessionId = "3";
        Workout workoutTemplate =
                Workout.builder().id("1").name("Leg workout").exercises(List.of(
                        ExerciseData.builder()
                                .exerciseId("11")
                                .sets(3)
                                .reps(15)
                                .weight(30)
                                .build()
                )).build();

        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.of(workoutTemplate));
        when(mockIdService.generateId()).thenReturn(sessionId);

        WorkoutSession workoutSession = workoutSessionService.startWorkoutSession(workoutId);

        assertNotNull(workoutSession);
        assertEquals(sessionId, workoutSession.id());
        assertEquals(workoutTemplate.name(), workoutSession.workoutName());
        assertNotNull(workoutSession.startTime());
        assertEquals(workoutTemplate.exercises().size(), workoutSession.exercises().size());

        ExerciseData expectedExercise = workoutTemplate.exercises().get(0);
        ExerciseSessionData actualExercise = workoutSession.exercises().get(0);

        assertEquals(expectedExercise.exerciseId(), actualExercise.exerciseId());
        assertEquals(expectedExercise.sets(), actualExercise.sets());
        assertEquals(expectedExercise.reps(), actualExercise.reps());
        assertEquals(expectedExercise.weight(), actualExercise.weight());

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
        String workoutName = "Leg Workout";
        String sessionId = "3";
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0, 0);
        String exerciseId = "2";
        int sets = 3;
        int reps = 10;
        int weight = 30;

        WorkoutSessionDTO workoutSessionDTO = WorkoutSessionDTO.builder()
                .workoutName(workoutName)
                .startTime(startTime)
                .exercises(List.of(
                        ExerciseSessionDataDTO.builder()
                                .exerciseId(exerciseId)
                                .sets(sets)
                                .reps(reps)
                                .weight(weight)
                                .build()
                ))
                .build();

        WorkoutSession workoutSession = WorkoutSession.builder()
                .id(sessionId)
                .workoutName(workoutName)
                .startTime(startTime)
                .exercises(List.of(
                        ExerciseSessionData.builder()
                                .exerciseId(exerciseId)
                                .sets(sets)
                                .reps(reps)
                                .weight(weight)
                                .build()
                ))
                .build();

        when(mockWorkoutSessionRepository.save(any(WorkoutSession.class))).thenReturn(workoutSession);

        WorkoutSession completedSession = workoutSessionService.completeWorkoutSession(sessionId, workoutSessionDTO);

        assertNotNull(completedSession);
        assertEquals(sessionId, completedSession.id());
        assertEquals(workoutSessionDTO.workoutName(), completedSession.workoutName());
        assertEquals(workoutSessionDTO.startTime(), completedSession.startTime());
        assertEquals(workoutSessionDTO.exercises().size(), completedSession.exercises().size());

        ExerciseSessionData expectedExercise = workoutSession.exercises().get(0);
        ExerciseSessionData completedExercise = completedSession.exercises().get(0);

        assertEquals(expectedExercise.exerciseId(), completedExercise.exerciseId());
        assertEquals(expectedExercise.sets(), completedExercise.sets());
        assertEquals(expectedExercise.reps(), completedExercise.reps());
        assertEquals(expectedExercise.weight(), completedExercise.weight());

        verify(mockWorkoutSessionRepository, times(1)).save(any(WorkoutSession.class));
    }

    // GET all workout sessions
    @Test
    void findAllWorkoutSessions_shouldReturnWorkoutSessionDTOs() {
        String sessionId = "1";
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0);
        String workoutName = "Leg Workout";
        String exerciseName = "Bench Press";
        String image = "bench.png";
        String exerciseId = "1";
        int sets = 3;
        int reps = 10;
        int weight = 30;

        ExerciseSessionData sessionExercise = ExerciseSessionData.builder()
                .exerciseId(exerciseId)
                .sets(sets)
                .reps(reps)
                .weight(weight)
                .build();

        WorkoutSession workoutSession = WorkoutSession.builder()
                .id(sessionId)
                .workoutName(workoutName)
                .startTime(startTime)
                .exercises(List.of(sessionExercise))
                .build();

        Exercise exercise = Exercise.builder()
                .id(exerciseId)
                .name(exerciseName)
                .image(image)
                .build();

        when(mockWorkoutSessionRepository.findAll(any(Sort.class))).thenReturn(List.of(workoutSession));
        when(mockExerciseRepository.findAllById(Set.of(exerciseId))).thenReturn(List.of(exercise));

        List<WorkoutSessionDTO> result = workoutSessionService.findAllWorkoutSessions();

        assertEquals(1, result.size());
        WorkoutSessionDTO dto = result.get(0);

        assertEquals(sessionId, dto.id());
        assertEquals(workoutName, dto.workoutName());
        assertEquals(startTime, dto.startTime());
        assertEquals(1, dto.exercises().size());

        ExerciseSessionDataDTO exerciseDTO = dto.exercises().get(0);
        assertEquals(exerciseId, exerciseDTO.exerciseId());
        assertEquals(exerciseName, exerciseDTO.exerciseName());
        assertEquals(image, exerciseDTO.exerciseImage());
        assertEquals(sets, exerciseDTO.sets());
        assertEquals(reps, exerciseDTO.reps());
        assertEquals(weight, exerciseDTO.weight());

        verify(mockWorkoutSessionRepository, times(1)).findAll(any(Sort.class));
        verify(mockExerciseRepository, times(1)).findAllById(Set.of(exerciseId));
    }
}
