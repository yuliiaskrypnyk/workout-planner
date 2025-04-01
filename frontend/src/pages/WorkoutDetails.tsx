import {editWorkout, getExercises, getWorkoutById} from "../api/workoutApi.ts";
import {ChangeEvent, useEffect, useState} from "react";
import {Workout} from "../types/Workout.ts";
import {useParams} from "react-router-dom";
import {Box, Button, Card, CardContent, Grid, List, TextField, Typography} from "@mui/material";
import {Exercise, ExerciseData} from "../types/Exercise.ts";
import BackButton from "../components/BackButton.tsx";
import AddExercise from "../components/AddExercise.tsx";

function WorkoutDetails() {
    const {id} = useParams<{ id: string }>();

    const [workout, setWorkout] = useState<Workout>();
    const [exercises, setExercises] = useState<Exercise[]>([]);
    const [selectedExercises, setSelectedExercises] = useState<ExerciseData[]>([]);
    const [isExerciseSelectVisible, setIsExerciseSelectVisible] = useState(false);

    const [isEditing, setIsEditing] = useState(false);
    const [editedWorkout, setEditedWorkout] = useState<Workout>();

    useEffect(() => {
        if (id) {
            getWorkoutById(id)
                .then(workoutData => {
                    setWorkout(workoutData);
                    setEditedWorkout(workoutData);
                    return getExercises();
                })
                .then(setExercises)
                .catch(console.error);
        }
    }, [id]);

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        if (editedWorkout) {
            setEditedWorkout({...editedWorkout, [e.target.name]: e.target.value});
        }
    };

    const handleExerciseChange = (exerciseId: string, field: string, value: string | number) => {
        if (!editedWorkout) return;
        const updatedExercises = editedWorkout.exercises.map(ex =>
            ex.exerciseId === exerciseId ? {...ex, [field]: value} : ex
        );
        setEditedWorkout({...editedWorkout, exercises: updatedExercises});
    };

    const handleSave = () => {
        if (!editedWorkout || !id) return;

        const updatedExercises = [
            ...editedWorkout.exercises,
            ...selectedExercises.map(ex => ({
                exerciseId: ex.exerciseId,
                sets: ex.sets || 0,
                reps: ex.reps || 0,
                weight: ex.weight || 0
            }))
        ];

        const updatedWorkout = {...editedWorkout, exercises: updatedExercises};

        editWorkout(id, updatedWorkout)
            .then(updated => {
                setWorkout(updated);
                setEditedWorkout(updated);
                setSelectedExercises([]);
                setIsExerciseSelectVisible(false);
                setIsEditing(false);
            })
            .catch(console.error);
    };

    const handleCancel = () => {
        setEditedWorkout(workout);
        setSelectedExercises([]);
        setIsExerciseSelectVisible(false);
        setIsEditing(false);
    };

    const handleAddExerciseClick = () => {
        setIsExerciseSelectVisible(true);
    };

    if (!workout || !editedWorkout || exercises.length === 0) return <p>Loading...</p>;

    return (
        <Box sx={{alignItems: "center"}}>
            <BackButton text="Workout list"/>
            <Typography variant="h5" sx={{margin: 2}}>Workout Details</Typography>

            {isEditing ? (
                <TextField name="name" label="Workout Name" value={editedWorkout.name} onChange={handleChange}
                           fullWidth/>
            ) : (
                <Typography variant="h5" sx={{margin: 2}}>{workout.name}</Typography>
            )}

            <List>
                {workout.exercises.map(workoutExercise => {
                    const exercise = exercises.find(ex => ex.id === workoutExercise.exerciseId);
                    if (!exercise) {
                        return null;
                    }
                    const currentExercise = editedWorkout.exercises.find(ex => ex.exerciseId === workoutExercise.exerciseId);

                    return (
                        <Card key={workoutExercise.exerciseId} sx={{marginBottom: 2, padding: 2}}>
                            <CardContent>
                                <Grid container spacing={2} alignItems="center">
                                    <Grid component="div">
                                        <img src={`/images/exercises/${exercise.image}`} alt={exercise.name}
                                             style={{width: 150, height: 100}}/>
                                    </Grid>
                                    <Grid component="div">
                                        <Typography sx={{marginRight: 1, width: 220}}>{exercise.name}</Typography>
                                    </Grid>

                                    <Grid component="div">
                                        {isEditing ? (
                                            <>
                                                <TextField
                                                    label="Sets" sx={{marginRight: 1}}
                                                    value={currentExercise?.sets || 0}
                                                    onChange={(e) => handleExerciseChange(workoutExercise.exerciseId, "sets", Number(e.target.value))}
                                                />
                                                <TextField
                                                    label="Reps" sx={{marginRight: 1}}
                                                    value={currentExercise?.reps || 0}
                                                    onChange={(e) => handleExerciseChange(workoutExercise.exerciseId, "reps", Number(e.target.value))}
                                                />
                                                <TextField
                                                    label="Weight"
                                                    value={currentExercise?.weight || 0}
                                                    onChange={(e) => handleExerciseChange(workoutExercise.exerciseId, "weight", Number(e.target.value))}
                                                />
                                            </>
                                        ) : (
                                            <>
                                                <Typography sx={{display: 'inline', marginRight: 1}}>
                                                    Sets: {workoutExercise.sets}
                                                </Typography>
                                                <Typography sx={{display: 'inline', marginRight: 1}}>
                                                    Reps: {workoutExercise.reps}
                                                </Typography>
                                                <Typography sx={{display: 'inline', marginRight: 1}}>
                                                    Weight: {workoutExercise.weight}
                                                </Typography>
                                            </>
                                        )}
                                    </Grid>
                                </Grid>
                            </CardContent>
                        </Card>
                    )
                })}
            </List>

            {isEditing ? (
                <>
                    {isExerciseSelectVisible && (
                        <AddExercise
                            selectedExercises={selectedExercises}
                            setSelectedExercises={setSelectedExercises}
                            existingExercises={workout.exercises}
                        />
                    )}

                    <Button variant="contained" color="primary" onClick={handleSave} sx={{margin: 1}}>
                        Save
                    </Button>

                    <Button variant="contained" color="primary" onClick={handleAddExerciseClick} sx={{margin: 1}}>
                        Add Exercise
                    </Button>

                    <Button variant="outlined" color="secondary" onClick={handleCancel} sx={{margin: 1}}>
                        Cancel
                    </Button>
                </>
            ) : (
                <Button variant="contained" onClick={() => setIsEditing(true)} sx={{margin: 1}}>
                    Edit Workout
                </Button>
            )}
        </Box>
    );
}

export default WorkoutDetails;