import {useEffect, useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";
import {Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography} from "@mui/material";
import {completeWorkoutSession, startWorkoutSession} from "../api/workoutApi.ts";
import {ExerciseField} from "../types/Exercise.ts";
import BackButton from "../components/BackButton.tsx";
import ExerciseForm from "../components/ExerciseForm.tsx";
import {ExerciseSessionData, WorkoutSession} from "../types/WorkoutSession.ts";
import LoadingIndicator from "../components/LoadingIndicator.tsx";
import AddExerciseButton from "../components/AddExerciseButton.tsx";

function StartWorkoutPage() {
    const {id} = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [session, setSession] = useState<WorkoutSession>();
    const [completedExercises, setCompletedExercises] = useState<string[]>([]);
    const [selectedExercises, setSelectedExercises] = useState<ExerciseSessionData[]>([]);

    const [open, setOpen] = useState(false);

    useEffect(() => {
        if (id) {
            startWorkoutSession(id)
                .then(workoutSession => {
                    setSession(workoutSession);
                })
                .catch(console.error);
        }
    }, [id]);

    if (!session) {
        return <LoadingIndicator/>;
    }

    const handleExerciseChange = (exerciseId: string, field: ExerciseField, value: string | number) => {
        const updatedExercises = session.exercises.map(ex =>
            ex.exerciseId === exerciseId ? {...ex, [field]: value} : ex
        );
        setSession({...session, exercises: updatedExercises});
    };

    const handleDeleteExercise = (exerciseId: string) => {
        const updatedExercises = session.exercises.filter(ex => ex.exerciseId !== exerciseId);
        setSession({...session, exercises: updatedExercises});
    };

    const handleCompleteWorkout = () => {
        const allExercises = [...session.exercises, ...selectedExercises];
        const completed = allExercises.filter(ex => completedExercises.includes(ex.exerciseId));

        if (completed.length === 0) {
            setOpen(true);
        } else {
            completeWorkout(completed);
        }
    };

    const completeWorkout = (completedExercisesList: ExerciseSessionData[]) => {
        const completedSession = {...session, exercises: completedExercisesList};

        completeWorkoutSession(completedSession)
            .then(() => {
                navigate('/');
            })
            .catch(console.error);
    };

    const handleCheckboxChange = (exerciseId: string, checked: boolean) => {
        setCompletedExercises(prev =>
            checked ? [...prev, exerciseId] : prev.filter(id => id !== exerciseId)
        );
    };

    const handleCompleteAllConfirm = () => {
        completeWorkout([...session.exercises, ...selectedExercises]);
        setOpen(false);
    };

    const handleCancel = () => {
        setOpen(false);
    };

    return (
        <Box>
            <BackButton text="Workout Details"/>
            <Typography variant="h5" sx={{margin: 2}}>Start Workout</Typography>

            <ExerciseForm
                workoutExercises={session.exercises}
                handleExerciseChange={handleExerciseChange}
                handleCheckboxChange={handleCheckboxChange}
                completedExercises={completedExercises}
                handleDeleteExercise={handleDeleteExercise}
            />

            <AddExerciseButton
                selectedExercises={selectedExercises}
                setSelectedExercises={setSelectedExercises}
                existingExercises={session.exercises}
                handleCheckboxChange={handleCheckboxChange}
                completedExercises={completedExercises}
            />

            <Button variant="contained" onClick={handleCompleteWorkout} sx={{margin: 1}}>
                Complete Workout
            </Button>

            <Dialog open={open} onClose={handleCancel}>
                <DialogTitle>Complete Workout</DialogTitle>
                <DialogContent>
                    Select all Exercises and complete Workout?
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCancel} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleCompleteAllConfirm} color="secondary">
                        Yes
                    </Button>
                </DialogActions>
            </Dialog>

            <Link to={`/workout/${id}`}>
                <Button variant="outlined" color="secondary" sx={{margin: 1}}>
                    Cancel
                </Button>
            </Link>
        </Box>
    );
}

export default StartWorkoutPage;