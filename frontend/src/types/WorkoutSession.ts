export type WorkoutSession = {
    id: string;
    workoutName: string;
    exercises: ExerciseSessionData[];
    startTime: string;
}

export type ExerciseSessionData = {
    exerciseId: string;
    exerciseName?: string;
    exerciseImage?: string;
    sets: number;
    reps: number;
    weight: number;
}