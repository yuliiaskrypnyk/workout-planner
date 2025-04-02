package com.yuliiaskrypnyk.backend.controller;

import com.yuliiaskrypnyk.backend.dto.WorkoutDTO;
import com.yuliiaskrypnyk.backend.model.Exercise;
import com.yuliiaskrypnyk.backend.model.Workout;
import com.yuliiaskrypnyk.backend.service.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    //Workout
    @GetMapping
    public ResponseEntity<List<Workout>> getAllWorkouts() {
        return ResponseEntity.ok(workoutService.findAllWorkouts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable String id) {
        Workout workout = workoutService.findWorkoutById(id);
        return ResponseEntity.ok(workout);
    }

    @PostMapping
    public ResponseEntity<Workout> addWorkout(@RequestBody WorkoutDTO workoutDTO) {
        Workout createdWorkout = workoutService.createWorkout(workoutDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkout);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable String id, @RequestBody WorkoutDTO workoutDTO) {
        Workout updatedWorkout = workoutService.updateWorkout(id, workoutDTO);
        return ResponseEntity.ok(updatedWorkout);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    // Exercise
    @GetMapping("/exercises")
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(workoutService.findAllExercises());
    }

    @GetMapping("/exercises/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable String id) {
        Exercise exercise = workoutService.findExerciseById(id);
        return ResponseEntity.ok(exercise);
    }
}
