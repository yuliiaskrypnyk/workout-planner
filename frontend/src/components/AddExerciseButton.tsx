import {Dispatch, SetStateAction, useState} from "react";
import ExerciseForm from "./ExerciseForm.tsx";
import {ExerciseSessionData} from "../types/WorkoutSession.ts";
import {ExerciseData, ExerciseField} from "../types/Workout.ts";
import StyledButton from "./buttons/StyledButton.tsx";
import ExerciseSelect from "./ExerciseSelect.tsx";

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
    const [isExerciseSelectVisible, setIsExerciseSelectVisible] = useState(false);

    const handleAddExerciseClick = () => {
        if (!isAddingExercise) {
            setIsExerciseSelectVisible((prev) => !prev);
        } else {
            setIsExerciseSelectVisible(true);
        }
    };

    const handleAddExercise = (exerciseId: string) => {
        if (!selectedExercises.some(ex => ex.exerciseId === exerciseId)) {
            setSelectedExercises(prev => [...prev, {exerciseId, sets: 0, reps: 0, weight: 0}]);
            if (!isAddingExercise) {
                setIsExerciseSelectVisible(false);
            } else {
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
                <ExerciseSelect
                    selectedExercises={selectedExercises}
                    existingExercises={existingExercises}
                    handleAddExercise={handleAddExercise}/>
            )}

            <StyledButton onClick={handleAddExerciseClick}>Add Exercise</StyledButton>
        </>
    );
}

export default AddExerciseButton;