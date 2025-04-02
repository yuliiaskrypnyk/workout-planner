package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.dto.ExerciseDataDTO;
import com.yuliiaskrypnyk.backend.dto.WorkoutDTO;
import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.Exercise;
import com.yuliiaskrypnyk.backend.model.ExerciseData;
import com.yuliiaskrypnyk.backend.model.Workout;
import com.yuliiaskrypnyk.backend.repository.ExerciseRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository mockWorkoutRepository;

    @Mock
    private ExerciseRepository mockExerciseRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private final List<Workout> workouts = List.of(
            Workout.builder().id("1").name("Leg workout").build(),
            Workout.builder().id("2").name("Arm Workout").build()
    );

    private final List<Exercise> exercises = List.of(
            Exercise.builder().id("1").name("Bench press").description("Description1").image("image1.png").build(),
            Exercise.builder().id("2").name("Squat").description("Description2").image("image2.png").build()
    );

    // GET all workouts
    @Test
    void findAllWorkouts_shouldReturnListOfWorkouts_whenRepositoryHasData() {
        when(mockWorkoutRepository.findAll()).thenReturn(workouts);

        List<Workout> actual = workoutService.findAllWorkouts();

        assertNotNull(actual, "The list of workouts should not be null");
        assertEquals(workouts, actual, "The returned list should match the expected list");
        verify(mockWorkoutRepository, times(1)).findAll();
    }

    @Test
    void findAllWorkouts_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        List<Workout> emptyWorkouts = Collections.emptyList();
        when(mockWorkoutRepository.findAll()).thenReturn(emptyWorkouts);

        List<Workout> actual = workoutService.findAllWorkouts();

        assertNotNull(actual, "The list of workouts should not be null");
        assertTrue(actual.isEmpty(), "The list should be empty");
        verify(mockWorkoutRepository, times(1)).findAll();
    }

    // GET workout by id
    @Test
    void findWorkoutById_shouldReturnWorkout_whenWorkoutExists() {
        String workoutId = "1";
        Workout expectedWorkout = workouts.get(0);
        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.of(expectedWorkout));

        Workout actualWorkout = workoutService.findWorkoutById(workoutId);

        assertNotNull(actualWorkout, "The workout should not be null");
        assertEquals(expectedWorkout, actualWorkout, "The returned workout should match the expected workout");
        verify(mockWorkoutRepository, times(1)).findById(workoutId);
    }

    @Test
    void findWorkoutById_shouldThrowResourceNotFoundException_whenWorkoutDoesNotExist() {
        String workoutId = "3";
        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                workoutService.findWorkoutById(workoutId)
        );

        assertEquals("Requested Workout was not found.", exception.getMessage());
        verify(mockWorkoutRepository, times(1)).findById(workoutId);
    }

    // Create workout
    @Test
    void createWorkout_shouldReturnSavedWorkout_whenWorkoutDTOIsValid() {
        WorkoutDTO workoutDTO = WorkoutDTO.builder()
                .name("Leg workout")
                .exercises(List.of(
                        ExerciseDataDTO.builder().exerciseId("1").sets(3).reps(10).weight(50).build(),
                        ExerciseDataDTO.builder().exerciseId("2").sets(4).reps(12).weight(60).build()
                ))
                .build();

        Workout expectedWorkout = Workout.builder()
                .name("Leg workout")
                .exercises(List.of(
                        ExerciseData.builder().exerciseId("1").sets(3).reps(10).weight(50).build(),
                        ExerciseData.builder().exerciseId("2").sets(4).reps(12).weight(60).build()
                ))
                .build();

        when(mockWorkoutRepository.save(any(Workout.class))).thenReturn(expectedWorkout);

        Workout actualWorkout = workoutService.createWorkout(workoutDTO);

        assertNotNull(actualWorkout, "The created workout should not be null");
        assertEquals(expectedWorkout.name(), actualWorkout.name(), "Workout name should match");
        assertEquals(expectedWorkout.exercises(), actualWorkout.exercises(), "Workout exercises should match");
        verify(mockWorkoutRepository, times(1)).save(any(Workout.class));
    }

    // Update workout
    @Test
    void updateWorkout_shouldUpdateAndReturnWorkout_whenWorkoutExists() {
        String workoutId = "1";
        Workout existingWorkout = Workout.builder().id(workoutId).name("Old Workout").build();
        WorkoutDTO updatedWorkoutDTO = WorkoutDTO.builder().name("Updated Workout").exercises(Collections.emptyList()).build();
        Workout updatedWorkout = Workout.builder().id(workoutId).name("Updated Workout").exercises(Collections.emptyList()).build();

        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.of(existingWorkout));
        when(mockWorkoutRepository.save(any(Workout.class))).thenReturn(updatedWorkout);

        Workout result = workoutService.updateWorkout(workoutId, updatedWorkoutDTO);

        assertNotNull(result);
        assertEquals(updatedWorkoutDTO.name(), result.name());
        verify(mockWorkoutRepository, times(1)).findById(workoutId);
        verify(mockWorkoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    void updateWorkout_shouldThrowResourceNotFoundException_whenWorkoutDoesNotExist() {
        String workoutId = "3";
        WorkoutDTO updatedWorkoutDTO = WorkoutDTO.builder().name("Updated Workout").exercises(Collections.emptyList()).build();

        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                workoutService.updateWorkout(workoutId, updatedWorkoutDTO)
        );

        assertEquals("Requested Workout was not found.", exception.getMessage());
        verify(mockWorkoutRepository, times(1)).findById(workoutId);
    }

    // Delete workout
    @Test
    void deleteWorkout_ShouldDelete_WhenWorkoutExists() {
        String workoutId = "1";
        Workout workout = workouts.get(0);

        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        workoutService.deleteWorkout(workoutId);

        verify(mockWorkoutRepository, times(1)).delete(workout);
    }

    @Test
    void deleteWorkout_ShouldThrowException_WhenWorkoutNotFound() {
        String workoutId = "3";

        when(mockWorkoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workoutService.deleteWorkout(workoutId));

        verify(mockWorkoutRepository, never()).delete(any());
    }

    // GET all exercises
    @Test
    void findAllExercises_shouldReturnListOfExercises_whenRepositoryHasData() {
        when(mockExerciseRepository.findAll()).thenReturn(exercises);

        List<Exercise> actual = workoutService.findAllExercises();

        assertNotNull(actual, "The list of exercises should not be null");
        assertEquals(exercises, actual, "The returned list should match the expected list");
        verify(mockExerciseRepository, times(1)).findAll();
    }

    @Test
    void findAllExercises_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        List<Exercise> emptyExercises = Collections.emptyList();
        when(mockExerciseRepository.findAll()).thenReturn(emptyExercises);

        List<Exercise> actual = workoutService.findAllExercises();

        assertNotNull(actual, "The list of exercises should not be null");
        assertTrue(actual.isEmpty(), "The list should be empty");
        verify(mockExerciseRepository, times(1)).findAll();
    }

    // GET exercise by id
    @Test
    void findExerciseById_shouldReturnExercise_whenExerciseExists() {
        String exerciseId = "1";
        Exercise expectedExercise = exercises.get(0);
        when(mockExerciseRepository.findById(exerciseId)).thenReturn(Optional.of(expectedExercise));

        Exercise actualExercise = workoutService.findExerciseById(exerciseId);

        assertNotNull(actualExercise, "The exercise should not be null");
        assertEquals(expectedExercise, actualExercise, "The returned exercise should match the expected exercise");
        verify(mockExerciseRepository, times(1)).findById(exerciseId);
    }

    @Test
    void findExerciseById_shouldThrowResourceNotFoundException_whenExerciseDoesNotExist() {
        String exerciseId = "3";
        when(mockExerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                workoutService.findExerciseById(exerciseId)
        );

        assertEquals("Requested Exercise was not found.", exception.getMessage());
        verify(mockExerciseRepository, times(1)).findById(exerciseId);
    }
}