import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";
import {Exercise} from "../types/Exercise.ts";
import {useEffect, useState} from "react";
import {getExercises} from "../api/exerciseApi.ts";
import {ExerciseData} from "../types/Workout.ts";
import {ExerciseSessionData} from "../types/WorkoutSession.ts";

interface Props {
    selectedExercises: ExerciseData[];
    existingExercises?: (ExerciseData | ExerciseSessionData)[];
    handleAddExercise: (exerciseId: string) => void;
}

const ExerciseSelect = ({selectedExercises, existingExercises, handleAddExercise}: Props) => {
    const [exercises, setExercises] = useState<Exercise[]>([]);

    useEffect(() => {
        getExercises()
            .then(setExercises)
            .catch((error) => console.error(error));
    }, []);

    const availableExercises = exercises.filter(exercise =>
        !selectedExercises.some(selEx => selEx.exerciseId === exercise.id) &&
        !existingExercises?.some(existing => existing.exerciseId === exercise.id)
    );

    return (
        <FormControl fullWidth margin="normal">
            <InputLabel>Select an exercise</InputLabel>
            <Select label="Select an exercise" value="" onChange={(e) => handleAddExercise(e.target.value)}
                    MenuProps={{PaperProps: {style: {maxHeight: 300}}}}>
                <em></em>
                {availableExercises.map(exercise => (
                    <MenuItem key={exercise.id} value={exercise.id}>
                        <img src={`/images/exercises/${exercise.image}`} alt={exercise.name}
                             style={{width: 80, height: 50, marginRight: 10}}/>
                        {exercise.name}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    );
};

export default ExerciseSelect;