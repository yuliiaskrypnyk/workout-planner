import {getExercises, getWorkoutById} from "../api/workoutApi.ts";
import {useEffect, useState} from "react";
import {Workout} from "../types/Workout.ts";
import {useParams} from "react-router-dom";
import {Box, Grid, List, ListItem, Typography} from "@mui/material";
import {Exercise} from "../types/Exercise.ts";
import BackButton from "../components/BackButton.tsx";

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

    if (!workout || exercises.length === 0) return <p>Loading...</p>;

    return (
        <Box sx={{alignItems: "center"}}>
            <BackButton text="Workout list"/>

            <Typography variant="h5" sx={{margin: 2}}>{workout.name}</Typography>

            <List>
                {workout.exercises.map(workoutExercise => {
                    const exercise = exercises.find(ex => ex.id === workoutExercise.exerciseId);

                    if (!exercise) {
                        return null;
                    }

                    return (
                        <ListItem key={workoutExercise.exerciseId}>
                            <Grid container spacing={2} alignItems="center">
                                <Grid component="div">
                                    <img src={`/images/exercises/${exercise.image}`} alt={exercise.name}
                                         style={{width: 150, height: 100}}/>
                                </Grid>
                                <Grid component="div">
                                    <Typography sx={{marginRight: 1, width: 220}}>{exercise.name}</Typography>
                                </Grid>

                                <Grid component="div">
                                    <Typography sx={{display: 'inline', marginRight: 1}}>
                                        Sets: {workoutExercise.sets}
                                    </Typography>
                                    <Typography sx={{display: 'inline', marginRight: 1}}>
                                        Reps: {workoutExercise.reps}
                                    </Typography>
                                    <Typography sx={{display: 'inline', marginRight: 1}}>
                                        Weight: {workoutExercise.weight}
                                    </Typography>
                                </Grid>
                            </Grid>
                        </ListItem>
                    )
                })}
            </List>
        </Box>
    );
}

export default WorkoutDetails;