import {useEffect, useState} from "react";
import {Workout} from "../../types/Workout.ts";
import {deleteWorkout, getWorkouts} from "../../api/workoutApi.ts";
import {Box, Typography} from "@mui/material";
import AddIcon from '@mui/icons-material/Add';
import {Link} from "react-router-dom";
import LoadingIndicator from "../../components/LoadingIndicator.tsx";
import WorkoutCard from "../../components/workout/WorkoutCard.tsx";
import StyledButton from "../../components/buttons/StyledButton.tsx";

function WorkoutsPage() {
    const [workouts, setWorkouts] = useState<Workout[]>([]);

    useEffect(() => {
        getWorkouts()
            .then(setWorkouts)
            .catch((error) => console.error(error));
    }, []);

    if (workouts === null) {
        return <LoadingIndicator/>;
    }

    const removeWorkoutFromList = (id: string) => {
        setWorkouts((prev) => prev.filter((workout) => workout.id !== id));
    };

    const handleDelete = (id: string) => {
        deleteWorkout(id)
            .then(() => removeWorkoutFromList(id))
            .catch(console.error);
    };

    return (
        <Box sx={{margin: 2}}>
            <Typography variant="h5" sx={{marginLeft: 3}}>All Workouts</Typography>
            <Box sx={{margin: 2}}>
                <Link to={"/workout/new"}>
                    <StyledButton startIcon={<AddIcon />}>New Workout</StyledButton>
                </Link>
            </Box>
            <Box
                sx={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    gap: 2,
                    padding: 2,
                    justifyContent: 'flex-start',
                }}
            >
                {workouts.map((workout) => (
                    <WorkoutCard key={workout.id} workout={workout} onDelete={handleDelete}/>
                ))}
            </Box>
        </Box>
    );
}

export default WorkoutsPage;
