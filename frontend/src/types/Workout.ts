export type ExerciseField = "sets" | "reps" | "weight";

export type Workout = {
    id: string;
    name: string;
    exercises: ExerciseData[]
}

export type WorkoutDTO = {
    id: string;
    name: string;
    exercises: ExerciseData[];
    exerciseCount: number;
}

export type ExerciseData = {
    exerciseId: string;
    exerciseName?: string;
    exerciseImage?: string;
    sets: number;
    reps: number;
    weight: number;
}