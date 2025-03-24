package com.yuliiaskrypnyk.backend.model;

public record ExerciseData(
        String exerciseId,
        int sets,
        int reps,
        double weight) {
}