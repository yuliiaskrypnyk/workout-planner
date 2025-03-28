import {useEffect, useState} from "react";
import {Exercise} from "../types/Exercise.ts";
import {Link} from "react-router-dom";
import {getExercises} from "../api/workoutApi.ts";
import {Box, Button, Grid, Typography} from "@mui/material";

function ExercisesPage() {
    const [exercises, setExercises] = useState<Exercise[]>([]);

    useEffect(() => {
        getExercises()
            .then(setExercises)
            .catch((error) => console.error(error));
    }, []);

    return (
        <Box sx={{alignItems: "center"}}>
            <Typography variant="h5" sx={{margin: 2}}>Exercise List</Typography>
            <Grid container spacing={2} justifyContent="flex-start">
                {exercises.map(exercise => (
                    <Grid component="div" key={exercise.id}
                          sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center'}}>
                        <img src={`/images/exercises/${exercise.image}`} alt={exercise.name}
                             style={{width: 270, height: 150}}/>
                        <Typography sx={{marginTop: 1}}>{exercise.name}</Typography>
                        <Link to={`/exercises/${exercise.id}`}>
                            <Button variant="contained" color="primary">Read Description</Button>
                        </Link>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
}

export default ExercisesPage;
