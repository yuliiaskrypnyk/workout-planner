import { useEffect, useState } from "react";
import axios from "axios";
import {Exercise} from "../types/Workout.ts";

function ExercisesPage() {
    const [exercises, setExercises] = useState<Exercise[]>([]);

    useEffect(() => {
        axios.get("/api/workouts/exercises")
            .then(response => setExercises(response.data))
            .catch(error => console.error("Error fetching exercises:", error));
    }, []);

    return (
        <div>
            <h1>Exercise List</h1>
            <ul>
                {exercises.map(exercise => (
                    <li key={exercise.id}>{exercise.name}</li>
                ))}
            </ul>
        </div>
    );
}

export default ExercisesPage;
