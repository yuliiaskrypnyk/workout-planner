package com.yuliiaskrypnyk.backend.controller;

import com.yuliiaskrypnyk.backend.model.exercise.Exercise;
import com.yuliiaskrypnyk.backend.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExerciseControllerTest {

    private static final String EXERCISES_URL = "/api/exercises";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private List<Exercise> exercises;

    @BeforeEach
    void setUp() {
        exerciseRepository.deleteAll();
        saveMockExerciseList();
    }

    private void saveMockExerciseList() {
        exercises = List.of(
                Exercise.builder().id("1").name("Bench press").description("Description1").image("image1.png").build(),
                Exercise.builder().id("2").name("Squat").description("Description2").image("image2.png").build()
        );
        exerciseRepository.saveAll(exercises);
    }

    // GET all exercises
    @Test
    void getAllExercises_shouldReturnListOfExercises_whenExercisesExist() throws Exception {
        mockMvc.perform(get(EXERCISES_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(exercises.size()))
                .andExpect(jsonPath("$[0].name").value(exercises.get(0).name()))
                .andExpect(jsonPath("$[1].name").value(exercises.get(1).name()));
    }

    @Test
    void getAllExercises_shouldReturnEmptyList_whenNoExercises() throws Exception {
        exerciseRepository.deleteAll();
        mockMvc.perform(get(EXERCISES_URL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // GET exercise by id
    @Test
    void getExerciseById_shouldReturnExercise_whenExerciseExists() throws Exception {
        String exerciseId = "1";

        mockMvc.perform(get(EXERCISES_URL + "/{id}", exerciseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(exerciseId))
                .andExpect(jsonPath("$.name").value(exercises.get(0).name()));
    }

    @Test
    void getExerciseById_shouldReturnNotFound_whenExerciseDoesNotExist() throws Exception {
        String exerciseId = "3";

        mockMvc.perform(get(EXERCISES_URL + "/{id}", exerciseId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Requested Exercise was not found."));
    }
}