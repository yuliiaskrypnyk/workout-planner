import {useState} from "react";
import {createWorkout} from "../../api/workoutApi.ts";
import {Box, TextField, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import BackButton from "../../components/buttons/BackButton.tsx";
import ExerciseForm from "../../components/ExerciseForm.tsx";
import {ExerciseData, ExerciseField} from "../../types/Workout.ts";
import StyledButton from "../../components/buttons/StyledButton.tsx";
import ExerciseSelect from "../../components/ExerciseSelect.tsx";

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

    return (
        <Box sx={{alignItems: "center"}}>
            <BackButton/>
            <Typography variant="h5" sx={{margin: 2}}>Create a new Workout</Typography>

            <TextField
                label="Workout Name"
                value={workoutName}
                onChange={(e) => setWorkoutName(e.target.value)}
                fullWidth
                margin="normal"
                variant="outlined"
            />

            <ExerciseSelect selectedExercises={selectedExercises} handleAddExercise={handleAddExercise}/>

            <ExerciseForm
                workoutExercises={selectedExercises}
                handleExerciseChange={handleExerciseChange}
                handleDeleteExercise={handleDeleteExercise}
                isAddingExercise={true}
            />

            <StyledButton onClick={handleSave} disabled={!workoutName || selectedExercises.length === 0}>
                Save Workout
            </StyledButton>
        </Box>
    );
}

export default NewWorkout;