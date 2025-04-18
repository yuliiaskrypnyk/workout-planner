import {Card, CardContent, Checkbox, Grid, TextField, Typography} from "@mui/material";
import DeleteButton from "./buttons/DeleteButton.tsx";
import {ExerciseData, ExerciseField} from "../types/Workout.ts";
import {ExerciseSessionData} from "../types/WorkoutSession.ts";
import {useEffect, useState} from "react";
import {getExerciseById} from "../api/exerciseApi.ts";
import {Exercise} from "../types/Exercise.ts";

interface Props {
    workoutExercise: (ExerciseData | ExerciseSessionData);
    handleExerciseChange: (exerciseId: string, field: ExerciseField, value: number | string) => void;
    handleDeleteExercise: (exerciseId: string) => void;
    handleCheckboxChange?: (exerciseId: string, checked: boolean) => void;
    completedExercises?: string[];
    isAddingExercise?: boolean;
    handleExerciseFocus: (exerciseId: string, field: ExerciseField, value: number | string) => void;
    handleExerciseBlur: (exerciseId: string, field: ExerciseField, value: string) => void;
}

function ExerciseFormCard ({
                              workoutExercise,
                              handleExerciseChange,
                              handleDeleteExercise,
                              handleCheckboxChange,
                              completedExercises,
                              isAddingExercise,
                              handleExerciseFocus,
                              handleExerciseBlur
                          }: Props) {
    const [exerciseInfo, setExerciseInfo] = useState<Partial<Exercise>>({})

    useEffect(() => {
        const shouldFetch =
            (!workoutExercise.exerciseImage || !workoutExercise.exerciseName) && workoutExercise.exerciseId;

        if (shouldFetch) {
            getExerciseById(workoutExercise.exerciseId)
                .then(setExerciseInfo)
                .catch(console.error);
        }
    }, [workoutExercise.exerciseId, workoutExercise.exerciseImage, workoutExercise.exerciseName]);

    const exerciseImage = workoutExercise.exerciseImage || exerciseInfo.image;
    const exerciseName = workoutExercise.exerciseName || exerciseInfo.name;

    return (
        <Card sx={{marginBottom: 1}}>
            <CardContent>
                <Grid container spacing={2} alignItems="center">
                    <Grid component="div">
                        <img
                            src={`/images/exercises/${exerciseImage}`}
                            alt={exerciseName}
                            style={{width: 125, height: 75}}
                        />
                    </Grid>
                    <Grid component="div">
                        <Typography sx={{marginRight: 1, width: 220}}>{exerciseName}</Typography>
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
}

export default ExerciseFormCard;