import {useState} from "react";
import {ExerciseData} from "../types/Exercise.ts";
import {createWorkout} from "../api/workoutApi.ts";
import {Box, Button, TextField, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import BackButton from "../components/BackButton.tsx";
import AddExercise from "../components/AddExercise.tsx";

function NewWorkout() {
    const [workoutName, setWorkoutName] = useState('');
    const [selectedExercises, setSelectedExercises] = useState<ExerciseData[]>([]);

    const navigate = useNavigate();

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
                navigate("/workouts")
            })
            .catch(console.error);
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

            <AddExercise
                selectedExercises={selectedExercises}
                setSelectedExercises={setSelectedExercises}
                existingExercises={[]}
            />

            <Button variant="contained" color="primary" onClick={handleSave}
                    disabled={!workoutName || selectedExercises.length === 0}>
                Save Workout
            </Button>
        </Box>
    );
}

export default NewWorkout;