import {ExerciseData} from "./Exercise.ts";

export type Workout = {
    id: string;
    name: string;
    exercises: ExerciseData[];
}