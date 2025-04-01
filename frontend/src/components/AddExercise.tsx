import {Dispatch, SetStateAction, useEffect, useState} from "react";
import {FormControl, InputLabel, MenuItem, Select, Grid, Typography, TextField, IconButton} from "@mui/material";
import {Exercise, ExerciseData} from "../types/Exercise.ts";
import {getExercises} from "../api/workoutApi.ts";
import DeleteIcon from "@mui/icons-material/Delete";

type AddExerciseProps = {
    selectedExercises: ExerciseData[];
    setSelectedExercises: Dispatch<SetStateAction<ExerciseData[]>>;
    existingExercises: ExerciseData[];
}

type ExerciseField = "sets" | "reps" | "weight";

function AddExercise({selectedExercises, setSelectedExercises, existingExercises}: Readonly<AddExerciseProps>) {
    const [exercises, setExercises] = useState<Exercise[]>([]);

    useEffect(() => {
        getExercises()
            .then(setExercises)
            .catch((error) => console.error(error));
    }, []);

    const handleAddExercise = (exerciseId: string) => {
        if (!selectedExercises.some(ex => ex.exerciseId === exerciseId)) {
            setSelectedExercises(prev => [...prev, {exerciseId, sets: 0, reps: 0, weight: 0}]);
        }
    };

    const handleExerciseChange = (exerciseId: string, field: ExerciseField, value: number | string) => {
        setSelectedExercises(prev =>
            prev.map(ex => ex.exerciseId === exerciseId ? {...ex, [field]: value} : ex)
        );
    };

    const handleFocus = (exerciseId: string, field: ExerciseField, value: number | string) => {
        if (value === 0) {
            handleExerciseChange(exerciseId, field, '');
        }
    };

    const handleBlur = (exerciseId: string, field: ExerciseField, value: string) => {
        if (value === '') {
            handleExerciseChange(exerciseId, field, 0);
        }
    };

    const handleRemoveExercise = (exerciseId: string) => {
        setSelectedExercises(prev => prev.filter(ex => ex.exerciseId !== exerciseId));
    };

    const availableExercises = exercises.filter(exercise =>
        !selectedExercises.some(selEx => selEx.exerciseId === exercise.id) &&
        !existingExercises.some(existing => existing.exerciseId === exercise.id)
    );

    return (
        <>
            <FormControl fullWidth margin="normal">
                <InputLabel>Select an exercise</InputLabel>
                <Select label="Select an exercise" value="" onChange={(e) => handleAddExercise(e.target.value)}>
                    {availableExercises.map(exercise => (
                        <MenuItem key={exercise.id} value={exercise.id} sx={{width: 250}}>
                            {exercise.name}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>

    {selectedExercises.map((exercise) => (
        <Grid container spacing={2} key={exercise.exerciseId} sx={{alignItems: "center", marginBottom: 1}}>
            <Grid component="div">
                <Typography sx={{width: 220}}>
                    {exercises.find(ex => ex.id === exercise.exerciseId)?.name}
                </Typography>
            </Grid>
            <Grid component="div">
                <TextField
                    label="Sets"
                    value={exercise.sets}
                    variant="outlined" margin="normal"
                    onChange={(e) => handleExerciseChange(exercise.exerciseId, 'sets', parseInt(e.target.value) || 0)}
                    onFocus={() => handleFocus(exercise.exerciseId, 'sets', exercise.sets)}
                    onBlur={(e) => handleBlur(exercise.exerciseId, 'sets', e.target.value)}
                />
            </Grid>
            <Grid component="div">
                <TextField
                    label="Reps"
                    value={exercise.reps}
                    variant="outlined" margin="normal"
                    onChange={(e) => handleExerciseChange(exercise.exerciseId, 'reps', parseInt(e.target.value) || 0)}
                    onFocus={() => handleFocus(exercise.exerciseId, 'reps', exercise.reps)}
                    onBlur={(e) => handleBlur(exercise.exerciseId, 'reps', e.target.value)}
                />
            </Grid>
            <Grid component="div">
                <TextField
                    label="Weight"
                    value={exercise.weight}
                    variant="outlined"
                    margin="normal"
                    onChange={(e) => handleExerciseChange(exercise.exerciseId, 'weight', parseInt(e.target.value) || 0)}
                    onFocus={() => handleFocus(exercise.exerciseId, 'weight', exercise.weight)}
                    onBlur={(e) => handleBlur(exercise.exerciseId, 'weight', e.target.value)}
                />
            </Grid>
            <Grid component="div">
                <IconButton onClick={() => handleRemoveExercise(exercise.exerciseId)} color="secondary">
                    <DeleteIcon/>
                </IconButton>
            </Grid>
        </Grid>
    ))}
        </>
    );
}

export default AddExercise;