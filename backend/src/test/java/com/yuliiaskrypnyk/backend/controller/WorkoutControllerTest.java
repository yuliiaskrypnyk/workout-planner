package com.yuliiaskrypnyk.backend.controller;

import com.yuliiaskrypnyk.backend.model.Exercise;
import com.yuliiaskrypnyk.backend.model.Workout;
import com.yuliiaskrypnyk.backend.repository.ExerciseRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
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
class WorkoutControllerTest {

    private static final String WORKOUTS_URL = "/api/workouts";
    private static final String EXERCISES_URL = "/api/workouts/exercises";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private List<Workout> workouts;
    private List<Exercise> exercises;

    @BeforeEach
    void setUp() {
        workoutRepository.deleteAll();
        exerciseRepository.deleteAll();

        saveMockWorkoutList();
        saveMockExerciseList();
    }

    private void saveMockWorkoutList() {
        workouts = List.of(
                Workout.builder().id("1").name("Leg workout").build(),
                Workout.builder().id("2").name("Arm workout").build()
        );
        workoutRepository.saveAll(workouts);
    }

    private void saveMockExerciseList() {
        exercises = List.of(
                Exercise.builder().id("1").name("Bench press").description("Description1").image("image1.png").build(),
                Exercise.builder().id("2").name("Squat").description("Description2").image("image2.png").build()
        );
        exerciseRepository.saveAll(exercises);
    }

    // GET all workouts
    @Test
    void getAllWorkouts_shouldReturnListOfWorkouts_whenWorkoutsExist() throws Exception {
        mockMvc.perform(get(WORKOUTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(workouts.size()))
                .andExpect(jsonPath("$[0].name").value(workouts.get(0).name()))
                .andExpect(jsonPath("$[1].name").value(workouts.get(1).name()));
    }

    @Test
    void getAllWorkouts_shouldReturnEmptyList_whenNoWorkouts() throws Exception {
        workoutRepository.deleteAll();
        mockMvc.perform(get(WORKOUTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
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
}