export type WorkoutSession = {
    id: string;
    workoutId: string;
    exercises: ExerciseSessionData[];
    startTime: string;
}

export type ExerciseSessionData = {
    exerciseId: string;
    sets: number;
    reps: number;
    weight: number;
}