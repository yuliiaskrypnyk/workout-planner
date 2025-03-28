import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {Exercise} from "../types/Exercise.ts";
import {getExerciseById} from "../api/workoutApi.ts";
import {Box, Grid, Typography} from "@mui/material";
import BackButton from "../components/BackButton.tsx";

const ExerciseDetails = () => {
    const {id} = useParams<{ id: string }>();

    const [exercise, setExercise] = useState<Exercise>();

    useEffect(() => {
        if (id) {
            getExerciseById(id)
                .then(setExercise)
                .catch(console.error);
        }
    }, [id]);

    if (!exercise) return <p>Loading...</p>;

    return (
        <Box>
            <BackButton text="Exercise list"/>

            <Box sx={{display: 'flex', flexDirection: 'column'}}>
                <Typography variant="h5" sx={{margin: 2}}>{exercise.name}</Typography>
                <Grid container spacing={2} justifyContent="center">
                    <Grid component="div" key={exercise.id}
                          sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center'}}>
                        <img src={`/images/exercises/${exercise.image}`} alt={exercise.name}/>
                        <Typography>{exercise.description}</Typography>
                    </Grid>
                </Grid>
            </Box>
        </Box>
    );
};

export default ExerciseDetails;