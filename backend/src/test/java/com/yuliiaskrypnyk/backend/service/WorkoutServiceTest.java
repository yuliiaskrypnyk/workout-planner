package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.model.Exercise;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        // GIVEN
        when(mockWorkoutRepository.findAll()).thenReturn(workouts);

        // WHEN
        List<Workout> actual = workoutService.findAllWorkouts();

        // THEN
        assertNotNull(actual, "The list of workouts should not be null");
        assertEquals(workouts, actual, "The returned list should match the expected list");
        verify(mockWorkoutRepository, times(1)).findAll();
    }

    @Test
    void findAllWorkouts_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // GIVEN
        List<Workout> emptyWorkouts = Collections.emptyList();
        when(mockWorkoutRepository.findAll()).thenReturn(emptyWorkouts);

        // WHEN
        List<Workout> actual = workoutService.findAllWorkouts();

        // THEN
        assertNotNull(actual, "The list of workouts should not be null");
        assertTrue(actual.isEmpty(), "The list should be empty");
        verify(mockWorkoutRepository, times(1)).findAll();
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
        // GIVEN
        List<Exercise> emptyExercises = Collections.emptyList();
        when(mockExerciseRepository.findAll()).thenReturn(emptyExercises);

        // WHEN
        List<Exercise> actual = workoutService.findAllExercises();

        // THEN
        assertNotNull(actual, "The list of exercises should not be null");
        assertTrue(actual.isEmpty(), "The list should be empty");
        verify(mockExerciseRepository, times(1)).findAll();
    }
}