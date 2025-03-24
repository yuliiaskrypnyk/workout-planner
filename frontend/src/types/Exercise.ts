export type ExerciseData = {
    exerciseId: string;
    sets: number;
    reps: number;
    weight: number;
}

export type Exercise = {
    id: string;
    name: string;
    description?: string;
    image?: string;
}