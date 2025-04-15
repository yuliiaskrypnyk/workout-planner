import {TextField, Grid, Typography, Card, CardContent, Checkbox} from "@mui/material";
import {Exercise, ExerciseData, ExerciseField} from "../types/Exercise";
import {useEffect, useState} from "react";
import {getExercises} from "../api/workoutApi.ts";
import DeleteButton from "./DeleteButton.tsx";
import {ExerciseSessionData} from "../types/WorkoutSession.ts";

type Props = {
    workoutExercises: (ExerciseData | ExerciseSessionData)[];
    handleExerciseChange: (exerciseId: string, field: ExerciseField, value: number | string) => void;
    handleDeleteExercise: (exerciseId: string) => void;
    handleCheckboxChange?: (exerciseId: string, checked: boolean) => void;
    completedExercises?: string[];
    isAddingExercise?: boolean;
};

function ExerciseForm({
                          workoutExercises,
                          handleExerciseChange,
                          handleDeleteExercise,
                          handleCheckboxChange,
                          completedExercises,
                          isAddingExercise,
                      }: Props) {
    const [exercises, setExercises] = useState<Exercise[]>([]);

    useEffect(() => {
        getExercises()
            .then(setExercises)
            .catch(console.error);
    }, []);

    const handleExerciseFocus = (exerciseId: string, field: ExerciseField, value: number | string) => {
        if (value === 0) {
            handleExerciseChange(exerciseId, field, '');
        }
    };

    const handleExerciseBlur = (exerciseId: string, field: ExerciseField, value: string) => {
        if (value === '') {
            handleExerciseChange(exerciseId, field, 0);
        }
    };

    return (
        workoutExercises.map(workoutExercise => {
            const exercise = exercises.find(ex => ex.id === workoutExercise.exerciseId);
            if (!exercise) return null;
            return (
                <Card key={workoutExercise.exerciseId} sx={{marginBottom: 1}}>
                    <CardContent>
                        <Grid container spacing={2} alignItems="center">
                            <Grid component="div">
                                <img
                                    src={`/images/exercises/${exercise.image}`}
                                    alt={exercise.name}
                                    style={{width: 125, height: 75}}
                                />
                            </Grid>
                            <Grid component="div">
                                <Typography sx={{marginRight: 1, width: 220}}>{exercise.name}</Typography>
                            </Grid>
                            <Grid component="div">
                                <TextField
                                    label="Sets"
                                    value={workoutExercise.sets}
                                    onChange={(e) =>
                                        handleExerciseChange(workoutExercise.exerciseId, "sets", parseInt(e.target.value) || 0)
                                    }
                                    onFocus={() => handleExerciseFocus(workoutExercise.exerciseId, 'sets', workoutExercise.sets)}
                                    onBlur={(e) => handleExerciseBlur(workoutExercise.exerciseId, 'sets', e.target.value)}
                                />
                            </Grid>
                            <Grid component="div">
                                <TextField
                                    label="Reps"
                                    value={workoutExercise.reps}
                                    onChange={(e) =>
                                        handleExerciseChange(workoutExercise.exerciseId, "reps", parseInt(e.target.value) || 0)
                                    }
                                    onFocus={() => handleExerciseFocus(workoutExercise.exerciseId, 'reps', workoutExercise.reps)}
                                    onBlur={(e) => handleExerciseBlur(workoutExercise.exerciseId, 'reps', e.target.value)}
                                />
                            </Grid>
                            <Grid component="div">
                                <TextField
                                    label="Weight"
                                    value={workoutExercise.weight}
                                    onChange={(e) =>
                                        handleExerciseChange(workoutExercise.exerciseId, "weight", parseInt(e.target.value) || 0)
                                    }
                                    onFocus={() => handleExerciseFocus(workoutExercise.exerciseId, 'weight', workoutExercise.weight)}
                                    onBlur={(e) => handleExerciseBlur(workoutExercise.exerciseId, 'weight', e.target.value)}
                                />
                            </Grid>
                            {handleCheckboxChange && (
                            <Grid component="div">
                                <Checkbox
                                    checked={completedExercises?.includes(workoutExercise.exerciseId) || false}
                                    onChange={(e) =>
                                        handleCheckboxChange(workoutExercise.exerciseId, e.target.checked)
                                    }
                                    sx={{'& .MuiSvgIcon-root': {fontSize: 28}}}
                                />
                            </Grid>
                            )}
                            <Grid component="div">
                                <DeleteButton
                                    handleDelete={handleDeleteExercise}
                                    id={workoutExercise.exerciseId}
                                    itemType="exercise"
                                    isAddingExercise={isAddingExercise}
                                />
                            </Grid>
                        </Grid>
                    </CardContent>
                </Card>
            );
        })
    );
}

export default ExerciseForm;