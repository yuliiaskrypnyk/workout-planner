import {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {Box, Button, Card, CardContent, Grid, List, Typography} from "@mui/material";
import {getExercises, getWorkoutById} from "../api/workoutApi.ts";
import {Workout} from "../types/Workout.ts";
import {Exercise} from "../types/Exercise.ts";
import BackButton from "../components/BackButton.tsx";
import LoadingIndicator from "../components/LoadingIndicator.tsx";

function WorkoutDetails() {
    const {id} = useParams<{ id: string }>();
    const [workout, setWorkout] = useState<Workout>();
    const [exercises, setExercises] = useState<Exercise[]>([]);

    useEffect(() => {
        if (id) {
            getWorkoutById(id)
                .then(workoutData => {
                    setWorkout(workoutData);
                    return getExercises();
                })
                .then(setExercises)
                .catch(console.error);
        }
    }, [id]);

    if (!workout || exercises.length === 0) {
        return <LoadingIndicator />;
    }

    return (
        <Box sx={{alignItems: "center"}}>
            <BackButton text="All Workouts"/>
            <Typography variant="h5" sx={{margin: 2}}>Workout Details</Typography>
            <Typography variant="h5" sx={{margin: 2}}>{workout.name}</Typography>

            <List>
                {workout.exercises.map(workoutExercise => {
                    const exercise = exercises.find(ex => ex.id === workoutExercise.exerciseId);
                    if (!exercise) return null;
                    return (
                        <Card key={workoutExercise.exerciseId} sx={{marginBottom: 1}}>
                            <CardContent>
                                <Grid container spacing={2} alignItems="center">
                                    <Grid component="div">
                                        <img
                                            src={`/images/exercises/${exercise.image}`}
                                            alt={exercise.name}
                                            style={{width: 125, height: 75}}
                                        />
                                    </Grid>
                                    <Grid component="div">
                                        <Link to={`/exercise/${exercise.id}`} target="_blank" style={{ textDecoration: 'none' }}>
                                            <Typography sx={{marginRight: 1, width: 220, color: 'primary.main'}}>{exercise.name}</Typography>
                                        </Link>
                                        <Typography sx={{display: 'inline', marginRight: 1}}>
                                            Sets: {workoutExercise.sets}
                                        </Typography>
                                        <Typography sx={{display: 'inline', marginRight: 1}}>
                                            Reps: {workoutExercise.reps}
                                        </Typography>
                                        <Typography sx={{display: 'inline', marginRight: 1}}>
                                            Weight: {workoutExercise.weight} kg
                                        </Typography>
                                    </Grid>
                                </Grid>
                            </CardContent>
                        </Card>
                    );
                })}
            </List>
            <Link to={`/workout/${id}/start`}>
                <Button variant="contained" color="primary" sx={{margin: 1}}>Start Workout</Button>
            </Link>
        </Box>
    );
}

export default WorkoutDetails;