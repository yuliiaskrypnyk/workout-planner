package com.yuliiaskrypnyk.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuliiaskrypnyk.backend.dto.workout.ExerciseDataDTO;
import com.yuliiaskrypnyk.backend.dto.workout.WorkoutDTO;
import com.yuliiaskrypnyk.backend.model.exercise.Exercise;
import com.yuliiaskrypnyk.backend.model.workout.ExerciseData;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Workout> workouts;
    private List<ExerciseDataDTO> exerciseDataList;

    @BeforeEach
    void setUp() {
        workoutRepository.deleteAll();

        saveMockExercise();
        saveMockWorkoutList();

        exerciseDataList = List.of(
                ExerciseDataDTO.builder().exerciseId("1").sets(3).reps(10).weight(50).build(),
                ExerciseDataDTO.builder().exerciseId("2").sets(4).reps(12).weight(60).build()
        );
    }

    private void saveMockWorkoutList() {
        workouts = List.of(
                Workout.builder().id("1").name("Leg workout").exercises(List.of(
                        ExerciseData.builder().exerciseId("3").sets(3).reps(10).weight(50).build()
                )).build(),
                Workout.builder().id("2").name("Arm workout").build()
        );
        workoutRepository.saveAll(workouts);
    }

    public void saveMockExercise() {
        Exercise exercise = Exercise.builder().id("3").name("Squat").image("squat.jpeg").build();
        exerciseRepository.save(exercise);
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
    void getWorkoutById_shouldReturnWorkoutDTO_whenWorkoutExists() throws Exception {
        String workoutId = "1";

        mockMvc.perform(get(WORKOUTS_URL + "/{id}", workoutId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Leg workout"))
                .andExpect(jsonPath("$.exercises.length()").value(1))
                .andExpect(jsonPath("$.exercises[0].exerciseId").value("3"))
                .andExpect(jsonPath("$.exercises[0].exerciseName").value("Squat"))
                .andExpect(jsonPath("$.exercises[0].exerciseImage").value("squat.jpeg"))
                .andExpect(jsonPath("$.exercises[0].sets").value(3))
                .andExpect(jsonPath("$.exercises[0].reps").value(10))
                .andExpect(jsonPath("$.exercises[0].weight").value(50));
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
}