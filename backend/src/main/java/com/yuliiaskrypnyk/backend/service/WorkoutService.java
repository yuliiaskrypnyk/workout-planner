package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.dto.WorkoutDTO;
import com.yuliiaskrypnyk.backend.model.Exercise;
import com.yuliiaskrypnyk.backend.model.ExerciseData;
import com.yuliiaskrypnyk.backend.model.Workout;
import com.yuliiaskrypnyk.backend.repository.ExerciseRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutService(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public List<Workout> findAllWorkouts() {
        return workoutRepository.findAll();
    }

    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }

    public Workout findWorkoutById(String id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Workout with ID " + id + " not found."));
    }

    public Exercise findExerciseById(String id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Exercise with ID " + id + " not found."));
    }

    public Workout createWorkout(WorkoutDTO workoutDTO) {
        List<ExerciseData> exerciseDataList = workoutDTO.exercises().stream()
                .map(dto -> ExerciseData.builder()
                        .exerciseId(dto.exerciseId())
                        .sets(dto.sets())
                        .reps(dto.reps())
                        .weight(dto.weight())
                        .build())
                .toList();

        Workout workout = Workout.builder()
                .name(workoutDTO.name())
                .exercises(exerciseDataList)
                .build();

        return workoutRepository.save(workout);
    }
}