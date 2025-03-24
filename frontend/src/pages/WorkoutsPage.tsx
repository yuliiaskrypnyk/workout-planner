import { useEffect, useState } from "react";
import axios from "axios";
import {Workout} from "../types/Workout.ts";

function WorkoutsPage() {
    const [workouts, setWorkouts] = useState<Workout[]>([]);

    useEffect(() => {
        axios.get("/api/workouts")
            .then(response => setWorkouts(response.data))
            .catch(error => console.error("Error fetching workouts:", error));
    }, []);

    return (
        <div>
            <h1>Workout List</h1>
            <ul>
                {workouts.map(workout => (
                    <li key={workout.id}>{workout.name}</li>
                ))}
            </ul>
        </div>
    );
}

export default WorkoutsPage;
