package com.yuliiaskrypnyk.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuliiaskrypnyk.backend.dto.session.ExerciseSessionDataDTO;
import com.yuliiaskrypnyk.backend.dto.session.WorkoutSessionDTO;
import com.yuliiaskrypnyk.backend.model.workout.ExerciseData;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
import com.yuliiaskrypnyk.backend.model.session.ExerciseSessionData;
import com.yuliiaskrypnyk.backend.model.session.WorkoutSession;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutSessionControllerTest {

    private static final String SESSIONS_URL = "/api/sessions";
    private final String workoutName = "Leg Workout";
    private final String exerciseId = "2";
    private final int sets = 3;
    private final int reps = 10;
    private final int weight = 30;
    private final LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final String sessionId = "3";

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

    // GET all workout sessions
    @Test
    void getAllWorkoutSessions_shouldReturnListOfWorkoutSessions() throws Exception {
        WorkoutSession session = WorkoutSession.builder()
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

        workoutSessionRepository.save(session);

        mockMvc.perform(get(SESSIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(sessionId))
                .andExpect(jsonPath("$[0].workoutName").value(workoutName))
                .andExpect(jsonPath("$[0].startTime").value(startTime.format(dateFormatter)))
                .andExpect(jsonPath("$[0].exercises[0].exerciseId").value(exerciseId))
                .andExpect(jsonPath("$[0].exercises[0].sets").value(sets))
                .andExpect(jsonPath("$[0].exercises[0].reps").value(reps))
                .andExpect(jsonPath("$[0].exercises[0].weight").value(weight));
    }

    // POST start a workout session
    @Test
    void startWorkoutSession_shouldReturnWorkoutSession_withStatusAccepted() throws Exception {
        String workoutId = "1";

        Workout workout = Workout.builder()
                .id(workoutId)
                .name(workoutName)
                .exercises(List.of(
                        ExerciseData.builder()
                                .exerciseId(exerciseId)
                                .sets(sets)
                                .reps(reps)
                                .weight(weight)
                                .build()
                ))
                .build();

        workoutRepository.save(workout);

        mockMvc.perform(post(SESSIONS_URL + "/start/{workoutId}", workoutId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.workoutName").value(workoutName))
                .andExpect(jsonPath("$.startTime").exists())
                .andExpect(jsonPath("$.exercises[0].exerciseId").value(exerciseId))
                .andExpect(jsonPath("$.exercises[0].sets").value(sets))
                .andExpect(jsonPath("$.exercises[0].reps").value(reps))
                .andExpect(jsonPath("$.exercises[0].weight").value(weight));
    }

    // PUT complete a workout session
    @Test
    void completeWorkoutSession_shouldReturnCompletedSession_whenRequestIsValid() throws Exception {
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
                .exercises(workoutSessionDTO.exercises().stream()
                        .map(exercise -> ExerciseSessionData.builder()
                                .exerciseId(exercise.exerciseId())
                                .exerciseId(exercise.exerciseId())
                                .sets(exercise.sets())
                                .reps(exercise.reps())
                                .weight(exercise.weight())
                                .build())
                        .toList()
                )
                .build();

        workoutSessionRepository.save(workoutSession);

        mockMvc.perform(put(SESSIONS_URL + "/complete/{sessionId}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutSessionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.workoutName").value(workoutSession.workoutName()))
                .andExpect(jsonPath("$.startTime").value(startTime.format(dateFormatter)))
                .andExpect(jsonPath("$.exercises[0].exerciseId").value(exerciseId))
                .andExpect(jsonPath("$.exercises[0].sets").value(sets))
                .andExpect(jsonPath("$.exercises[0].reps").value(reps))
                .andExpect(jsonPath("$.exercises[0].weight").value(weight));
    }
}