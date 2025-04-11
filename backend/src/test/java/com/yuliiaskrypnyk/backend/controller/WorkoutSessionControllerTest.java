package com.yuliiaskrypnyk.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuliiaskrypnyk.backend.model.workout.ExerciseData;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
import com.yuliiaskrypnyk.backend.model.workoutSession.ExerciseSessionData;
import com.yuliiaskrypnyk.backend.model.workoutSession.WorkoutSession;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutSessionControllerTest {

    private static final String SESSIONS_URL = "/api/sessions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp() {
        workoutRepository.deleteAll();
        workoutSessionRepository.deleteAll();
    }

    @Test
    void startWorkoutSession_shouldReturnWorkoutSession_withStatusAccepted() throws Exception {
        String workoutId = "1";

        Workout workout = Workout.builder()
                .id(workoutId)
                .name("Leg workout")
                .exercises(List.of(
                        ExerciseData.builder()
                                .exerciseId("2")
                                .sets(50)
                                .reps(20)
                                .weight(200)
                                .build()
                ))
                .build();

        workoutRepository.save(workout);

        mockMvc.perform(post(SESSIONS_URL + "/start/{workoutId}", workoutId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.workoutId").value(workoutId))
                .andExpect(jsonPath("$.startTime").exists())
                .andExpect(jsonPath("$.exercises[0].exerciseId").value("2"))
                .andExpect(jsonPath("$.exercises[0].sets").value(50))
                .andExpect(jsonPath("$.exercises[0].reps").value(20))
                .andExpect(jsonPath("$.exercises[0].weight").value(200));
    }

    // PUT complete a workout session
    @Test
    void completeWorkoutSession_shouldReturnCompletedSession_whenRequestIsValid() throws Exception {
        WorkoutSession workoutSession = WorkoutSession.builder()
                .id("11")
                .workoutId("1")
                .startTime(LocalDateTime.now())
                .exercises(List.of(
                        ExerciseSessionData.builder()
                                .exerciseId("2")
                                .sets(50)
                                .reps(20)
                                .weight(200)
                                .build()
                ))
                .build();

        workoutSessionRepository.save(workoutSession);

        mockMvc.perform(put(SESSIONS_URL + "/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutSession)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(workoutSession.id()))
                .andExpect(jsonPath("$.workoutId").value(workoutSession.workoutId()))
                .andExpect(jsonPath("$.startTime").exists())
                .andExpect(jsonPath("$.exercises[0].exerciseId").value("2"))
                .andExpect(jsonPath("$.exercises[0].sets").value(50))
                .andExpect(jsonPath("$.exercises[0].reps").value(20))
                .andExpect(jsonPath("$.exercises[0].weight").value(200));
    }
}