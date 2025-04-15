package com.yuliiaskrypnyk.backend.controller;

import com.yuliiaskrypnyk.backend.dto.session.WorkoutSessionDTO;
import com.yuliiaskrypnyk.backend.model.session.WorkoutSession;
import com.yuliiaskrypnyk.backend.service.WorkoutSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sessions")
public class WorkoutSessionController {
    private final WorkoutSessionService workoutSessionService;

    @GetMapping
    public ResponseEntity<List<WorkoutSessionDTO>> getAllWorkoutSessions() {
        return ResponseEntity.ok(workoutSessionService.findAllWorkoutSessions());
    }

    @PostMapping("/start/{workoutId}")
    public ResponseEntity<WorkoutSession> startWorkoutSession(@PathVariable String workoutId) {
        WorkoutSession session = workoutSessionService.startWorkoutSession(workoutId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(session);
    }

    @PutMapping("/complete/{sessionId}")
    public ResponseEntity<WorkoutSession> completeSession(@PathVariable String sessionId, @RequestBody WorkoutSessionDTO workoutSessionDTO) {
        WorkoutSession savedSession = workoutSessionService.completeWorkoutSession(sessionId, workoutSessionDTO);
        return ResponseEntity.ok(savedSession);
    }
}