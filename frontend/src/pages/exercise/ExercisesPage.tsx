import {useEffect, useState} from "react";
import {Exercise, ExerciseType} from "../../types/Exercise.ts";
import {getExercises} from "../../api/exerciseApi.ts";
import {Box, Button, Grid, Typography} from "@mui/material";
import {Link} from "react-router-dom";

function ExercisesPage() {
    const [exercises, setExercises] = useState<Exercise[]>([]);
    const [selectedType, setSelectedType] = useState<ExerciseType | null>(null);

    useEffect(() => {
        getExercises()
            .then((data) => {
                setExercises(data);
            })
            .catch(error => console.error(error));
    }, []);

    const filteredExercises = selectedType
        ? exercises.filter((exercise) => exercise.type === selectedType)
        : exercises;

    const handleFilterClick = (type: ExerciseType | null) => {
        setSelectedType(type);
    };

    const renderButton = (type: ExerciseType | null, label: string) => (
        <Button
            onClick={() => handleFilterClick(type)}
            sx={{
                textDecoration: selectedType === type ? "underline" : "none",
                fontWeight: selectedType === type ? "bold" : "normal",
            }}
        >
            {label}
        </Button>
    );

    return (
        <Box sx={{alignItems: "center"}}>
            <Typography variant="h5" sx={{margin: 2}}>Exercises</Typography>
            <Box sx={{ marginBottom: 2 }}>
                {renderButton(null, "All")}
                {renderButton(ExerciseType.UPPER_BODY, "Upper Body")}
                {renderButton(ExerciseType.LOWER_BODY, "Lower Body")}
            </Box>

            <Grid container spacing={2} justifyContent="flex-start">
                {filteredExercises.map(exercise => (
                    <Grid component="div" key={exercise.id}
                          sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center', width: 200}}>
                        <Link to={`/exercise/${exercise.id}`}>
                            <img src={`/images/exercises/${exercise.image}`} alt={exercise.name}
                                 style={{width: 155, height: 90, marginRight: 10}}/>
                        </Link>
                        <Typography sx={{marginTop: 1}}>{exercise.name}</Typography>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
}

export default ExercisesPage;
