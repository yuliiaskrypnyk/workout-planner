import {useEffect, useState} from "react";
import {Exercise, ExerciseData} from "../types/Exercise.ts";
import {createWorkout, getExercises} from "../api/workoutApi.ts";
import {Box, Button, FormControl, Grid, IconButton, InputLabel, MenuItem, Select, TextField, Typography} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import {useNavigate} from "react-router-dom";
import BackButton from "../components/BackButton.tsx";

function NewWorkout() {
    const [workoutName, setWorkoutName] = useState('');
    const [selectedExercises, setSelectedExercises] = useState<ExerciseData[]>([]);
    const [exercises, setExercises] = useState<Exercise[]>([]);

    const navigate = useNavigate();

    useEffect(() => {
        getExercises()
            .then(setExercises)
            .catch((error) => console.error(error));
    }, []);

    const handleSave = async () => {
        const newWorkout = {
            id: '',
            name: workoutName,
            exercises: selectedExercises
        };

        try {
            await createWorkout(newWorkout);
            setWorkoutName("");
            setSelectedExercises([]);
        } catch (error) {
            console.error("Error creating workout:", error);
        }

        navigate("/workouts")
    };

    const handleAddExercise = (exerciseId: string) => {
        if (!selectedExercises.some(ex => ex.exerciseId === exerciseId)) {
            setSelectedExercises(prev => [...prev, {exerciseId, sets: 0, reps: 0, weight: 0}]);
        }
    };

    const handleExerciseChange = (exerciseId: string, field: "sets" | "reps" | "weight", value: number | string) => {
        setSelectedExercises(prev =>
            prev.map(ex => ex.exerciseId === exerciseId ? {...ex, [field]: value} : ex)
        );
    };

    const handleFocus = (exerciseId: string, field: 'sets' | 'reps' | 'weight', value: number | string) => {
        if (value === 0) {
            handleExerciseChange(exerciseId, field, '');
        }
    };

    const handleBlur = (exerciseId: string, field: 'sets' | 'reps' | 'weight', value: string) => {
        if (value === '') {
            handleExerciseChange(exerciseId, field, 0);
        }
    };

    const handleRemoveExercise = (exerciseId: string) => {
        setSelectedExercises(prev => prev.filter(ex => ex.exerciseId !== exerciseId));
    };

    return (
        <Box sx={{alignItems: "center"}}>
            <BackButton text="Workout list"/>

            <Typography variant="h5" sx={{margin: 2}}>Create a new Workout</Typography>

            <TextField
                label="Workout Name"
                value={workoutName}
                onChange={(e) => setWorkoutName(e.target.value)}
                fullWidth margin="normal"
                variant="outlined"
            />

            <FormControl fullWidth margin="normal">
                <InputLabel>Select an exercise</InputLabel>
                <Select label="Select an exercise" value="" onChange={(e) => handleAddExercise(e.target.value)}>
                    {exercises.map((exercise) => (
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
                            label="Sets" value={exercise.sets} variant="outlined" margin="normal"
                            onChange={(e) => handleExerciseChange(exercise.exerciseId, 'sets', parseInt(e.target.value) || 0)}
                            onFocus={() => handleFocus(exercise.exerciseId, 'sets', exercise.sets)}
                            onBlur={(e) => handleBlur(exercise.exerciseId, 'sets', e.target.value)}
                        />
                    </Grid>
                    <Grid component="div">
                        <TextField
                            label="Reps" value={exercise.reps} variant="outlined" margin="normal"
                            onChange={(e) => handleExerciseChange(exercise.exerciseId, 'reps', parseInt(e.target.value) || 0)}
                            onFocus={() => handleFocus(exercise.exerciseId, 'reps', exercise.reps)}
                            onBlur={(e) => handleBlur(exercise.exerciseId, 'reps', e.target.value)}
                        />
                    </Grid>
                    <Grid component="div">
                        <TextField
                            label="Weight" value={exercise.weight} variant="outlined" margin="normal"
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

            <Button variant="contained" color="primary" onClick={handleSave}
                    disabled={!workoutName || selectedExercises.length === 0}>
                Save Workout
            </Button>
        </Box>
    );
}

export default NewWorkout;