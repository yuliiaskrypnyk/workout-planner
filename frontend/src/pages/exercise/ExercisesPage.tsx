import {useEffect, useState} from "react";
import {Exercise} from "../../types/Exercise.ts";
import {getExercises} from "../../api/exerciseApi.ts";
import {Box, Grid, Typography} from "@mui/material";
import {Link} from "react-router-dom";

function ExercisesPage() {
    const [exercises, setExercises] = useState<Exercise[]>([]);

    useEffect(() => {
        getExercises()
            .then(setExercises)
            .catch((error) => console.error(error));
    }, []);

    return (
        <Box sx={{alignItems: "center"}}>
            <Typography variant="h5" sx={{margin: 2}}>All Exercises</Typography>
            <Grid container spacing={2} justifyContent="flex-start">
                {exercises.map(exercise => (
                    <Grid component="div" key={exercise.id}
                          sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center'}}>
                        <Link to={`/exercise/${exercise.id}`}>
                            <img src={`/images/exercises/${exercise.image}`} alt={exercise.name}
                                 style={{width: 135, height: 75, marginRight: 10}}/>
                        </Link>
                        <Typography sx={{marginTop: 1}}>{exercise.name}</Typography>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
}

export default ExercisesPage;
