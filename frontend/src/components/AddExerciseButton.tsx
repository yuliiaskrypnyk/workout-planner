import {Dispatch, SetStateAction, useEffect, useState} from "react";
import {FormControl, InputLabel, MenuItem, Select, Button} from "@mui/material";
import {Exercise, ExerciseData, ExerciseField} from "../types/Exercise.ts";
import {getExercises} from "../api/workoutApi.ts";
import ExerciseForm from "./ExerciseForm.tsx";
import {ExerciseSessionData} from "../types/WorkoutSession.ts";

type AddExerciseButtonProps = {
    selectedExercises: (ExerciseData | ExerciseSessionData)[];
    setSelectedExercises: Dispatch<SetStateAction<ExerciseData[]>>;
    existingExercises: (ExerciseData | ExerciseSessionData)[];
    handleCheckboxChange?: (exerciseId: string, checked: boolean) => void;
    completedExercises?: string[];
    isAddingExercise?: boolean;
}

function AddExerciseButton({
                               selectedExercises,
                               setSelectedExercises,
                               existingExercises,
                               handleCheckboxChange,
                               completedExercises,
                               isAddingExercise
                           }: Readonly<AddExerciseButtonProps>) {
    const [exercises, setExercises] = useState<Exercise[]>([]);
    const [isExerciseSelectVisible, setIsExerciseSelectVisible] = useState(false);

    const handleAddExerciseClick = () => {
        if(!isAddingExercise) {
            setIsExerciseSelectVisible((prev) => !prev);
        } else{
            setIsExerciseSelectVisible(true);
        }
    };

    useEffect(() => {
        getExercises()
            .then(setExercises)
            .catch((error) => console.error(error));
    }, []);

    const handleAddExercise = (exerciseId: string) => {
        if (!selectedExercises.some(ex => ex.exerciseId === exerciseId)) {
            setSelectedExercises(prev => [...prev, {exerciseId, sets: 0, reps: 0, weight: 0}]);
            if(!isAddingExercise) {
                setIsExerciseSelectVisible(false);
            } else{
                setIsExerciseSelectVisible(true);
            }
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
        !selectedExercises.some(selEx => selEx.exerciseId === exercise.id) &&
        !existingExercises.some(existing => existing.exerciseId === exercise.id)
    );

    return (
        <>
            <ExerciseForm
                workoutExercises={selectedExercises}
                handleExerciseChange={handleExerciseChange}
                handleDeleteExercise={handleDeleteExercise}
                handleCheckboxChange={handleCheckboxChange}
                completedExercises={completedExercises}
            />

            {isExerciseSelectVisible && (
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
            )}

            <Button
                variant="contained"
                color="primary"
                onClick={handleAddExerciseClick}
                sx={{margin: 1}}
            >
                Add Exercise
            </Button>
        </>
    );
}

export default AddExerciseButton;