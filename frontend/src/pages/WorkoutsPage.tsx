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

            <Grid container spacing={2} justifyContent="flex-start" sx={{marginBottom: 3}}>
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
                    <ListItem key={workout.id} component="li"
                              sx={{
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
                              }}
                    >
                        <Link to={`/workouts/${workout.id}`}
                              style={{textDecoration: 'none', color: 'inherit'}}>{workout.name}</Link>
                    </ListItem>
                ))}
            </List>
        </Box>
    );
}

export default WorkoutsPage;
