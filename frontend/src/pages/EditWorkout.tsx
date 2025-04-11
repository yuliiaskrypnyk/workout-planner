import {ChangeEvent, useEffect, useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";
import {Box, Button, TextField, Typography} from "@mui/material";
import {editWorkout, getWorkoutById} from "../api/workoutApi.ts";
import {Workout} from "../types/Workout.ts";
import {ExerciseData, ExerciseField} from "../types/Exercise.ts";
import BackButton from "../components/BackButton.tsx";
import ExerciseForm from "../components/ExerciseForm.tsx";
import LoadingIndicator from "../components/LoadingIndicator.tsx";
import AddExerciseButton from "../components/AddExerciseButton.tsx";

function EditWorkout() {
    const {id} = useParams<{ id: string }>();
    const [workout, setWorkout] = useState<Workout>();
    const [selectedExercises, setSelectedExercises] = useState<ExerciseData[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        if (id) {
            getWorkoutById(id)
                .then(workoutData => {
                    setWorkout(workoutData);
                })
                .catch(console.error);
        }
    }, [id]);

    if (!workout) {
        return <LoadingIndicator/>;
    }

    const handleChangeName = (e: ChangeEvent<HTMLInputElement>) => {
        setWorkout({...workout, [e.target.name]: e.target.value});
    };

    const handleExerciseChange = (exerciseId: string, field: ExerciseField, value: string | number) => {
        const updatedExercises = workout.exercises.map(ex =>
            ex.exerciseId === exerciseId ? {...ex, [field]: value} : ex
        );
        setWorkout({...workout, exercises: updatedExercises});
    };

    const handleSave = () => {
        if (!id) return;

        const updatedExercises: ExerciseData[] = [...workout.exercises, ...selectedExercises];
        const updatedWorkout = {...workout, exercises: updatedExercises};

        editWorkout(id, updatedWorkout)
            .then(updated => {
                setWorkout(updated);
                setSelectedExercises([]);
                navigate('/');
            })
            .catch(console.error);
    };

    const handleDeleteExercise = (exerciseId: string) => {
        if (!id) return;

        const updatedExercises = workout.exercises.filter(ex => ex.exerciseId !== exerciseId);
        const updatedWorkout = {...workout, exercises: updatedExercises};

        editWorkout(id, updatedWorkout)
            .then(updated => {
                setWorkout(updated);
            })
            .catch(console.error);
    };

    return (
        <Box sx={{alignItems: "center"}}>
            <BackButton text="Workout list"/>
            <Typography variant="h5" sx={{margin: 2}}>Workout Details</Typography>

            <TextField
                name="name"
                label="Workout Name"
                value={workout.name}
                onChange={handleChangeName}
                fullWidth
                margin="normal"
            />

            <ExerciseForm
                workoutExercises={workout.exercises}
                handleExerciseChange={handleExerciseChange}
                handleDeleteExercise={handleDeleteExercise}
            />

            <AddExerciseButton
                selectedExercises={selectedExercises}
                setSelectedExercises={setSelectedExercises}
                existingExercises={workout.exercises}
            />

            <Button variant="contained" color="primary" onClick={handleSave} sx={{margin: 1}}>
                Save
            </Button>

            <Link to={`/`}>
                <Button variant="outlined" color="secondary" sx={{margin: 1}}>
                    Close
                </Button>
            </Link>
        </Box>
    );
}

export default EditWorkout;