import {useEffect, useState} from "react";
import {Workout} from "../types/Workout.ts";
import {getWorkouts} from "../api/workoutApi.ts";
import {Box, Button, Grid, List, ListItem, Typography} from "@mui/material";
import {Link} from "react-router-dom";

function WorkoutsPage() {
    const [workouts, setWorkouts] = useState<Workout[]>([]);

    useEffect(() => {
        getWorkouts()
            .then(setWorkouts)
            .catch((error) => console.error(error));
    }, []);

    return (
        <Box>
            <Typography variant="h5" sx={{margin: 2}}>Workout List</Typography>

            <Grid container spacing={2} justifyContent="flex-start">
                <Grid component="div">
                    <Link to={`/workouts/new`}>
                        <Button variant="contained" color="primary">
                            New Workout
                        </Button>
                    </Link>
                </Grid>
            </Grid>

            <List>
                {workouts.map(workout => (
                    <ListItem key={workout.id} component="li" sx={{'&:hover': {textDecoration: 'underline'}}}>
                        <Link to={`/workouts/${workout.id}`}
                              style={{textDecoration: 'none', color: 'inherit'}}>{workout.name}</Link>
                    </ListItem>
                ))}
            </List>
        </Box>
    );
}

export default WorkoutsPage;
