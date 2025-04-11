import {useEffect, useState} from "react";
import {Exercise, ExerciseData, ExerciseField} from "../types/Exercise.ts";
import {createWorkout, getExercises} from "../api/workoutApi.ts";
import {Box, Button, FormControl, InputLabel, MenuItem, Select, TextField, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import BackButton from "../components/BackButton.tsx";
import ExerciseForm from "../components/ExerciseForm.tsx";

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

        createWorkout(newWorkout)
            .then(() => {
                setWorkoutName("")
                setSelectedExercises([])
                navigate("/")
            })
            .catch(console.error);
    };

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

    const handleDeleteExercise = (exerciseId: string) => {
        setSelectedExercises(prev => prev.filter(ex => ex.exerciseId !== exerciseId));
    };

    const availableExercises = exercises.filter(exercise =>
        !selectedExercises.some(selEx => selEx.exerciseId === exercise.id)
    );

    return (
        <Box sx={{alignItems: "center"}}>
            <BackButton text="Workout list"/>
            <Typography variant="h5" sx={{margin: 2}}>Create a new Workout</Typography>

            <TextField
                label="Workout Name"
                value={workoutName}
                onChange={(e) => setWorkoutName(e.target.value)}
                fullWidth
                margin="normal"
                variant="outlined"
            />

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

            <ExerciseForm
                workoutExercises={selectedExercises}
                handleExerciseChange={handleExerciseChange}
                handleDeleteExercise={handleDeleteExercise}
                isAddingExercise={true}
            />

            <Button variant="contained" color="primary" onClick={handleSave}
                    disabled={!workoutName || selectedExercises.length === 0}>
                Save Workout
            </Button>
        </Box>
    );
}

export default NewWorkout;