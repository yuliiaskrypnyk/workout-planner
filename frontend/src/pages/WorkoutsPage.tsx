import {useEffect, useState} from "react";
import {Workout} from "../types/Workout.ts";
import {deleteWorkout, getWorkouts} from "../api/workoutApi.ts";
import {Box, Button, IconButton, List, ListItem, Typography} from "@mui/material";
import {Link} from "react-router-dom";
import DeleteButton from "../components/DeleteButton.tsx";
import EditIcon from "@mui/icons-material/Edit";
import LoadingIndicator from "../components/LoadingIndicator.tsx";

function WorkoutsPage() {
    const [workouts, setWorkouts] = useState<Workout[]>([]);

    useEffect(() => {
        getWorkouts()
            .then(setWorkouts)
            .catch((error) => console.error(error));
    }, []);

    if (!workouts) {
        return <LoadingIndicator />;
    }

    const removeWorkoutFromList = (id: string): void => {
        setWorkouts((prevWorkouts) => prevWorkouts.filter((workout) => workout.id !== id));
    };

    const handleDelete = (id: string): void => {
        deleteWorkout(id)
            .then(() => {
                removeWorkoutFromList(id)
            })
            .catch(console.error);
    };

    const workoutItemStyle = {
        '&:hover': {
            backgroundColor: '#e1f5fe',
            borderRadius: 1,
            transition: 'all 0.3s ease'
        },
        border: '1px solid #ddd',
        borderRadius: 1,
        marginBottom: 1,
        padding: '8px 16px',
        backgroundColor: '#fff',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
    };

    return (
        <Box>
            <Typography variant="h5" sx={{margin: 2}}>Workout List</Typography>
            <Box sx={{marginBottom: 3}}>
                <Link to="/workout/new">
                    <Button variant="contained" color="primary">New Workout</Button>
                </Link>
            </Box>
            <List>
                {workouts.map((workout) => (
                    <ListItem key={workout.id} sx={workoutItemStyle}>
                        <Box component={Link} to={`/workout/${workout.id}`}
                             sx={{textDecoration: "none", color: "inherit", flex: 1}}>
                            <Typography variant="body1">{workout.name}</Typography>
                        </Box>

                        <Box sx={{display: "flex", gap: 1}}>
                            <IconButton component={Link} to={`/workout/${workout.id}/edit`} color="primary">
                                <EditIcon/>
                            </IconButton>
                            <DeleteButton handleDelete={handleDelete} id={workout.id} itemType="workout"/>
                        </Box>
                    </ListItem>
                ))}
            </List>
        </Box>
    );
}

export default WorkoutsPage;
