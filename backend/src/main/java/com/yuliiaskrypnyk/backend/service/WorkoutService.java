package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.dto.workout.WorkoutDTO;
import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.workout.ExerciseData;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    public List<Workout> findAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Workout findWorkoutById(String id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout"));
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

    public Workout updateWorkout(String id, WorkoutDTO updatedWorkoutDTO) {
        Workout existingWorkout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout"));

        List<ExerciseData> updatedExerciseData = updatedWorkoutDTO.exercises().stream()
                .map(updatedDto -> ExerciseData.builder()
                        .exerciseId(updatedDto.exerciseId())
                        .sets(updatedDto.sets())
                        .reps(updatedDto.reps())
                        .weight(updatedDto.weight())
                        .build())
                .toList();

        String updatedName = (updatedWorkoutDTO.name() != null && !updatedWorkoutDTO.name().isBlank())
                ? updatedWorkoutDTO.name()
                : existingWorkout.name();

        Workout updatedWorkout = Workout.builder()
                .id(existingWorkout.id())
                .name(updatedName)
                .exercises(updatedExerciseData)
                .build();

        return workoutRepository.save(updatedWorkout);
    }

    public void deleteWorkout(String id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout"));

        workoutRepository.delete(workout);
    }
}