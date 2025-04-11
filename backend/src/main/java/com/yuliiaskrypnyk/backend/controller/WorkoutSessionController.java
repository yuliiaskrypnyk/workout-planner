package com.yuliiaskrypnyk.backend.controller;

import com.yuliiaskrypnyk.backend.dto.workoutSession.WorkoutSessionDTO;
import com.yuliiaskrypnyk.backend.model.workoutSession.WorkoutSession;
import com.yuliiaskrypnyk.backend.service.WorkoutSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sessions")
public class WorkoutSessionController {
    private final WorkoutSessionService workoutSessionService;

    @PostMapping("/start/{workoutId}")
    public ResponseEntity<WorkoutSession> startWorkoutSession(@PathVariable String workoutId) {
        WorkoutSession session = workoutSessionService.startWorkoutSession(workoutId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(session);
    }

    @PutMapping("/complete")
    public ResponseEntity<WorkoutSession> completeSession(@RequestBody WorkoutSessionDTO workoutSessionDTO) {
        WorkoutSession savedSession = workoutSessionService.completeWorkoutSession(workoutSessionDTO);
        return ResponseEntity.ok(savedSession);
    }
}
