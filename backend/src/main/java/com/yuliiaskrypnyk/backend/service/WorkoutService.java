package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.dto.workout.ExerciseDataDTO;
import com.yuliiaskrypnyk.backend.dto.workout.WorkoutDTO;
import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.exercise.Exercise;
import com.yuliiaskrypnyk.backend.model.workout.ExerciseData;
import com.yuliiaskrypnyk.backend.model.workout.Workout;
import com.yuliiaskrypnyk.backend.repository.ExerciseRepository;
import com.yuliiaskrypnyk.backend.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    public List<Workout> findAllWorkouts() {
        return workoutRepository.findAll();
    }

    public WorkoutDTO findWorkoutWithExercisesById(String id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout"));

        List<ExerciseDataDTO> exerciseDTOs = workout.exercises().stream()
                .map(workoutExercise -> {
                    Exercise exercise = exerciseRepository.findById(workoutExercise.exerciseId())
                            .orElseThrow(() -> new ResourceNotFoundException("Exercise"));

                    return ExerciseDataDTO.builder()
                            .exerciseId(exercise.id())
                            .exerciseName(exercise.name())
                            .exerciseImage(exercise.image())
                            .sets(workoutExercise.sets())
                            .reps(workoutExercise.reps())
                            .weight(workoutExercise.weight())
                            .build();
                })
                .collect(Collectors.toList());

        return WorkoutDTO.builder()
                .name(workout.name())
                .exercises(exerciseDTOs)
                .build();
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