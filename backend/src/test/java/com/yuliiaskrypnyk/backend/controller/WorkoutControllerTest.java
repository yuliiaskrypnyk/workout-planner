package com.yuliiaskrypnyk.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuliiaskrypnyk.backend.dto.ExerciseDataDTO;
import com.yuliiaskrypnyk.backend.dto.WorkoutDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private ObjectMapper objectMapper;

    private List<Workout> workouts;
    private List<Exercise> exercises;
    private List<ExerciseDataDTO> exerciseDataList;

    @BeforeEach
    void setUp() {
        workoutRepository.deleteAll();
        exerciseRepository.deleteAll();

        saveMockWorkoutList();
        saveMockExerciseList();

        exerciseDataList = List.of(
                ExerciseDataDTO.builder().exerciseId("1").sets(3).reps(10).weight(50).build(),
                ExerciseDataDTO.builder().exerciseId("2").sets(4).reps(12).weight(60).build()
        );
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

    // GET workout by id
    @Test
    void getWorkoutById_shouldReturnWorkout_whenWorkoutExists() throws Exception {
        String workoutId = "1";

        mockMvc.perform(get(WORKOUTS_URL + "/{id}", workoutId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(workoutId))
                .andExpect(jsonPath("$.name").value(workouts.get(0).name()));
    }

    @Test
    void getWorkoutById_shouldReturnNotFound_whenWorkoutDoesNotExist() throws Exception {
        String workoutId = "3";

        mockMvc.perform(get(WORKOUTS_URL + "/{id}", workoutId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Requested Workout was not found."));
    }

    // Create workout
    @Test
    void addWorkout_shouldReturnCreatedWorkout_whenRequestIsValid() throws Exception {
        WorkoutDTO workoutDTO = WorkoutDTO.builder()
                .name("Leg workout")
                .exercises(exerciseDataList)
                .build();

        mockMvc.perform(post(WORKOUTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Leg workout"))
                .andExpect(jsonPath("$.exercises[0].exerciseId").value("1"))
                .andExpect(jsonPath("$.exercises[0].sets").value(3))
                .andExpect(jsonPath("$.exercises[0].reps").value(10))
                .andExpect(jsonPath("$.exercises[0].weight").value(50))
                .andExpect(jsonPath("$.exercises[1].exerciseId").value("2"))
                .andExpect(jsonPath("$.exercises[1].sets").value(4))
                .andExpect(jsonPath("$.exercises[1].reps").value(12))
                .andExpect(jsonPath("$.exercises[1].weight").value(60));
    }

    // Update workout
    @Test
    void updateWorkout_shouldReturnUpdatedWorkout_whenRequestIsValid() throws Exception {
        String workoutId = "1";

        WorkoutDTO updatedWorkoutDTO = WorkoutDTO.builder()
                .name("Updated Leg Workout")
                .exercises(exerciseDataList)
                .build();

        mockMvc.perform(put(WORKOUTS_URL + "/{id}", workoutId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedWorkoutDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Leg Workout"))
                .andExpect(jsonPath("$.exercises[0].exerciseId").value("1"))
                .andExpect(jsonPath("$.exercises[0].sets").value(3))
                .andExpect(jsonPath("$.exercises[0].reps").value(10))
                .andExpect(jsonPath("$.exercises[0].weight").value(50))
                .andExpect(jsonPath("$.exercises[1].exerciseId").value("2"))
                .andExpect(jsonPath("$.exercises[1].sets").value(4))
                .andExpect(jsonPath("$.exercises[1].reps").value(12))
                .andExpect(jsonPath("$.exercises[1].weight").value(60));
    }

    @Test
    void updateWorkout_shouldReturnNotFound_whenWorkoutDoesNotExist() throws Exception {
        String nonExistentWorkoutId = "000";

        WorkoutDTO workoutDTO = WorkoutDTO.builder().name("Non-existent Workout").build();

        mockMvc.perform(put(WORKOUTS_URL + "/{id}", nonExistentWorkoutId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Requested Workout was not found."));
    }

    // Delete workout
    @Test
    void deleteWorkout_shouldReturnNoContent_WhenWorkoutExists() throws Exception {
        String workoutId = "1";
        mockMvc.perform(delete(WORKOUTS_URL + "/{id}", workoutId))
                .andExpect(status().isNoContent());
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