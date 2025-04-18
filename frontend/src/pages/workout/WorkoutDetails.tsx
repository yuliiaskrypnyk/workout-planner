import {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {Box, Typography} from "@mui/material";
import {getWorkoutById} from "../../api/workoutApi.ts";
import {Workout} from "../../types/Workout.ts";
import BackButton from "../../components/buttons/BackButton.tsx";
import LoadingIndicator from "../../components/LoadingIndicator.tsx";
import WorkoutExerciseCard from "../../components/workout/WorkoutExerciseCard.tsx";
import StyledButton from "../../components/buttons/StyledButton.tsx";

function WorkoutDetails() {
    const {id} = useParams<{ id: string }>();
    const [workout, setWorkout] = useState<Workout>();

    useEffect(() => {
        if (id) {
            getWorkoutById(id)
                .then(workoutData => setWorkout(workoutData))
                .catch(console.error);
        }
    }, [id]);

    if (!workout) {
        return <LoadingIndicator/>;
    }

    return (
        <Box sx={{alignItems: "center"}}>
            <BackButton text="All Workouts"/>
            <Typography variant="h5" sx={{margin: 2}}>{workout.name}</Typography>

            {workout.exercises.map(exercise => (
                <WorkoutExerciseCard key={exercise.exerciseId} exercise={exercise}/>
            ))}

            <Link to={`/workout/${id}/start`}>
                <StyledButton>Start Workout</StyledButton>
            </Link>
        </Box>
    );
}

export default WorkoutDetails;