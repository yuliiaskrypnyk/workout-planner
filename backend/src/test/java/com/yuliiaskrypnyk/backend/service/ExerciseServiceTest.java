package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.exercise.Exercise;
import com.yuliiaskrypnyk.backend.repository.ExerciseRepository;
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
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository mockExerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    private final List<Exercise> exercises = List.of(
            Exercise.builder().id("1").name("Bench press").description("Description1").image("image1.png").build(),
            Exercise.builder().id("2").name("Squat").description("Description2").image("image2.png").build()
    );

    // GET all exercises
    @Test
    void findAllExercises_shouldReturnListOfExercises_whenRepositoryHasData() {
        when(mockExerciseRepository.findAll()).thenReturn(exercises);

        List<Exercise> actual = exerciseService.findAllExercises();

        assertNotNull(actual, "The list of exercises should not be null");
        assertEquals(exercises, actual, "The returned list should match the expected list");
        verify(mockExerciseRepository, times(1)).findAll();
    }

    @Test
    void findAllExercises_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        List<Exercise> emptyExercises = Collections.emptyList();
        when(mockExerciseRepository.findAll()).thenReturn(emptyExercises);

        List<Exercise> actual = exerciseService.findAllExercises();

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

        Exercise actualExercise = exerciseService.findExerciseById(exerciseId);

        assertNotNull(actualExercise, "The exercise should not be null");
        assertEquals(expectedExercise, actualExercise, "The returned exercise should match the expected exercise");
        verify(mockExerciseRepository, times(1)).findById(exerciseId);
    }

    @Test
    void findExerciseById_shouldThrowResourceNotFoundException_whenExerciseDoesNotExist() {
        String exerciseId = "3";
        when(mockExerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                exerciseService.findExerciseById(exerciseId)
        );

        assertEquals("Requested Exercise was not found.", exception.getMessage());
        verify(mockExerciseRepository, times(1)).findById(exerciseId);
    }
}