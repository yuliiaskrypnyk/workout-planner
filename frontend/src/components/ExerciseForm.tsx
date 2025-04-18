import {ExerciseSessionData} from "../types/WorkoutSession.ts";
import {ExerciseData, ExerciseField} from "../types/Workout.ts";
import ExerciseFormCard from "./ExerciseFormCard.tsx";

interface Props {
    workoutExercises: (ExerciseData | ExerciseSessionData)[];
    handleExerciseChange: (exerciseId: string, field: ExerciseField, value: number | string) => void;
    handleDeleteExercise: (exerciseId: string) => void;
    handleCheckboxChange?: (exerciseId: string, checked: boolean) => void;
    completedExercises?: string[];
    isAddingExercise?: boolean;
}

function ExerciseForm({
                          workoutExercises,
                          handleExerciseChange,
                          handleDeleteExercise,
                          handleCheckboxChange,
                          completedExercises,
                          isAddingExercise,
                      }: Readonly<Props>) {

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
    console.log(workoutExercises);
    return (
        <>
            {workoutExercises.map(workoutExercise => (
            <ExerciseFormCard
                key={workoutExercise.exerciseId}
                workoutExercise={workoutExercise}
                handleExerciseChange={handleExerciseChange}
                handleDeleteExercise={handleDeleteExercise}
                handleCheckboxChange={handleCheckboxChange}
                completedExercises={completedExercises}
                isAddingExercise={isAddingExercise}
                handleExerciseFocus={handleExerciseFocus}
                handleExerciseBlur={handleExerciseBlur}
            />
            ))}
        </>
    );
}

export default ExerciseForm;